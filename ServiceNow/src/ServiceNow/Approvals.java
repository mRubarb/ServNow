package ServiceNow;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.CalendarDateTimeObject;
import HelperObjects.DeviceInfoActions;
import HelperObjects.Feature;


// this is for all actions related to the accessories page.   

public class Approvals extends BaseClass
{
	
	public static int loopMax = 10;
	public static int rowNumContainingOrder = 0;

	static String [] strArray;
	static String tmpStr;	
	
	// this waits for first check box at top of approvals list and the 'to' text at the bottom of the page.
	public static void WaitForPageToLoad() throws Exception
	{
		//commented by Ana, replaced by line below - 9/5/17
		//WaitForElementClickable(By.xpath("(//a[text()='Requested'])[1]/../a"), MainTimeout, "Failed wait for clickable element.");		
		WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MainTimeout, "Failed wait for clickable element.");
		WaitForElementPresent(By.xpath("(//span[contains(text(),'to')])[3]"), MediumTimeout);		
	}
	
	// assume the approval page is opened and loaded.
	public static void ApprovalAction(ApproverAction approverAction) throws Exception
	{

		// ** Added on 9/19/17 to replace former 'for' loop *** TEST *** 
		boolean correctUserAndType = false;
		boolean correctExternalOrderId = false;
		int x = 1;
		
		do {	
			
			WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MediumTimeout, "Failed waiting for row in Approvals.ApprovalAction");
			
			// select row
			System.out.println(" *** Click on order listed # " + x + " ***");
			
			WebElement orderNumLink = driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[1]/td[7]/a[@class='linked']"));
			orderNumLink.click(); 
			
			// wait for the short description and  description text areas to become visible. 
			WaitForElementVisible(By.xpath(".//*[@id='sys_readonly.x_tango_mobility_tangoe_mobility_order_request.short_description']"), MediumTimeout);		
			WaitForElementVisible(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']"), MediumTimeout);
			
			// Verify order type and user name in short description
			correctUserAndType = IsCorrectOrderTypeAndUser(driver.findElement(By.xpath(".//*[@id='sys_readonly.x_tango_mobility_tangoe_mobility_order_request.short_description']")).getAttribute("value"));

			// Verify external order number and order id in full description
			correctExternalOrderId = ContainsCorrectExternalOrderIdAndOrderId(driver.findElement(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']")).getText().split("\n"));
			
			if(correctUserAndType && correctExternalOrderId)
			{
				rowNumContainingOrder = x;
				
			} else { // If any of the expected values is not correct, then click 'Back' button
				
				driver.findElement(By.xpath("//span[text()='Back']/..")).click();
				WaitForPageToLoad();		
				
				x++;
			}
			
		} while ((x <= loopMax) && !(correctUserAndType && correctExternalOrderId));
		
		
		
		// verify order to approve was found. if the order action wasn't found within loop max rows it looks like it can't be found. 
		Assert.assertTrue(x <= loopMax, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");

		// verify the items in the short and full description.
		VerifyApprovalPageData();
		
		
		// If get to here the order to be approved has been found. do the approval and wait for main page to load. doing the approval brings user back to the main page.		
		
		
		// 1. The approver must be selected first, from a table located at the bottom of the UI
		// Get the approvers' names listed:
		List<WebElement> approversList = driver.findElements(By.xpath("//tbody[@class='list2_body']/tr/td[4]/a[@class='linked']"));
		
		int approverIndex = 0;
		
		for (int i = 0; i < approversList.size(); i++) {
								
			if (approversList.get(i).getText().trim().equals(approverBob)){
				approverIndex = i + 1;
			}
			//System.out.println("Approver " + (i + 1) + ": " + approversList.get(i).getText().trim());
		}
		
		Assert.assertTrue(approverIndex > 0, "Approver was not found in the approvers list.");
		
		// 2. Select the approver --> Click on the checkbox
		
		driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + approverIndex + "]/td[1]")).click();			
		
		switch(approverAction)
		{
			case approve:
			{
				// The 'Approve' button is not listed anymore. --> // driver.findElement(By.xpath("(//button[text()='Approve'])[2]")).click(); 
								
				// 3. The action ('Approve' in this case) must be selected from a dropdown list located below 
				
				new Select(driver.findElement(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select"))).selectByVisibleText("Approve");
				
				// 4. Verify that the State changes to 'Approved'
				
				Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + approverIndex + "]/td[3]")).getText(), "Approved", "Failed. State is not Approved.");	
								
		 		//WaitForPageToLoad();
				
				// verify the order row that was approved shows approved.
				//Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumContainingOrder + "]/td[3]/a")).getText(), "Approved", 
					//				"Item that was approved in Row " + rowNumContainingOrder + " does not show as approved in the main Approvals list in Approvals.FindApprovalAndApprove.");
				
				// now set the order details expected status to 'In Fulfillment'
				orderDetailsObjectExpected.status = "In Fulfillment";			
				
				System.out.println("Order has been approved.");
				
				break;
			}
			
			case reject: // TODO -- 
			{
				// The 'Comments' textbox is not displayed anymore. Text for Comment is something like: 
				// "Order was rejected in ServiceNow. Approvers: rejected:Bob Lichtenfels-Approver,
				//  not_required:Mike McPadden, not_required:ServiceNow Certifier - Admin, not_required:ServiceNow Certifier - Approver ."
				// -- > driver.findElement(By.xpath(".//*[@id='sysapproval_approver.comments']")).clear();
				// --> driver.findElement(By.xpath(".//*[@id='sysapproval_approver.comments']")).sendKeys("Reject This Order");
				// The 'Reject' button is not listed anymore. --> // driver.findElement(By.xpath("(//button[text()='Reject'])[2]")).click();				
				//WaitForPageToLoad();
				
				// verify the order row that was rejected shows rejected.
				//Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumContainingOrder + "]/td[3]/a")).getText(), "Rejected", 
					//				"Item that was approved in Row " + rowNumContainingOrder + " does not show as approved in the main Approvals list in Approvals.FindApprovalAndApprove.");
				
				
				
				// 3. The action ('Reject' in this case) must be selected from a dropdown list located below 
				
				//driver.findElement(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select")).click();
				
				//Thread.sleep(5000);
				
				List<WebElement> options = driver.findElements(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select>option"));
				int indexReject = 0;
				
				for (int i = 0; i < options.size(); i++) {
					
					if (options.get(i).getText().trim().equals("Reject")) {
						indexReject = i;
					}
					
				}
				
				//driver.findElement(By.xpath("//select/option[text()='Reject']")).click(); // <-- it doesn't work, replaced by line below
				new Select(driver.findElement(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select"))).selectByIndex(indexReject);
				
				// 4. Verify that the State changes to 'Rejected'
				
				Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + approverIndex + "]/td[3]")).getText(), "Rejected", "Failed. State is not Rejected.");	
				
				// now set the order details expected status to 'Approval Rejected'
				orderDetailsObjectExpected.status = "Approval Rejected";				
				
				System.out.println("Order has been rejected.");
				
				break;			
			}
			
			case none:
			{
				Assert.fail("FAILURE: Approval page not setting expected status.");
			}
		}
		
		Thread.sleep(5000);  // ** Giving time for order to have status updated 
		
	}
	
	
	// this looks at the short description for the full user name of the limited user that requested the order and the order type.
	public static boolean IsCorrectOrderTypeAndUser(String shortDescription)
	{
		
		if(shortDescription.contains(userLimitedShorterName) && shortDescription.contains(orderDetailsObjectExpected.orderType))
		{
			return true;
		}
		return false;
		
	}	
	
	
	public static boolean ContainsCorrectExternalOrderIdAndOrderId(String[] descriptionAllLines) throws Exception
	{
		
		boolean externOrderIdOK = descriptionAllLines[descriptionAllLines.length - 1].split(":")[1].equals(orderDetailsObjectExpected.externalOrderId);
		boolean orderIdOK = descriptionAllLines[descriptionAllLines.length - 2].split(":")[1].equals(orderDetailsObjectExpected.orderId);
		
		return (externOrderIdOK && orderIdOK);
		
	}	
	
	
	public static void VerifyApprovalPageData() throws Exception
	{
		switch(approvalActionType)
		{
			case deactivate:
			{
				VerifyDeactivate();
				break;
			}
			case suspend:
			{
				VerifySuspend();
				break;
			}
			case unsuspend:
			{
				VerifyUnsuspend();				
				break;
			}
			case swapDevice:
			{
				VerifySwapDevice();				
				break;
			}
			case orderAccessories:
			{
				VerifyOrderAccessories();				
				break;
			}			
			case upgradeDevice:
			{
				VerifyUpgradeDevice();				
				break;
			}			
			case updateFeatures:
			{
				VerifyUpdateFeatures();
				break;
			}
			case portNumber:
			{
				//VerifyPortNumber();  //<-- TODO
				break;
			}
				
			case none:
			{
				Assert.fail("Error in Approvals.VerifyApprovalPageData, reached an incorrect case value 'none'.");
				break;
			}
		
		default:
			break;			
		}
	}

	public static void VerifyDeactivate() throws Exception
	{
		DebugTimeout(0, "Verify Deactivate in approval page.");
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");

		Assert.assertEquals(strArray[2],"Additional Info:","");		
		Assert.assertEquals(strArray[6].replace("Reason:",""), reasonAction,"");	

		// this covers the rest of the items.
		VerifySubset(strArray);
	}	
	
	public static void VerifySuspend() throws Exception
	{
		DebugTimeout(0, "Verify Suspend in approval page.");
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		
		// this verifies 'preferred suspension date' month and year.
		CalendarDateTimeObject.VerifyPreferredSuspendionDateForApproval(strArray[6].replace("Preferred Suspension Date:", ""));
		
		// this covers the rest of the items.
		VerifySubset(strArray);
	}		
	
	public static void VerifyUnsuspend() throws Exception
	{
		DebugTimeout(0, "Verify Unsuspend in approval page.");
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		
		// this verifies 'preferred suspension date' month and year.
		CalendarDateTimeObject.VerifyPreferredSuspendionDateForApproval(strArray[6].replace("Preferred Suspension Date:", ""));
		
		// this verifies 'preferred suspension date' month and year.
		CalendarDateTimeObject.VerifyPreferredSuspendionDateForApproval(strArray[6].replace("Preferred Suspension Date:", ""));

		// this verifies everything else.
		VerifyUnsuspendSubset(strArray);		
	}			

	public static void VerifySwapDevice() throws Exception
	{
		DebugTimeout(0, "Verify SwapDevice in approval page.");
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		
		VerifySwapDevices(strArray);
	}				
	
	public static void VerifyUpgradeDevice() throws Exception
	{	
		String errMessage = "Failure in Approvals.VerifyUpgradeDevice.";
			
		// Lines found in 'Description'
		
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		List<String> descriptionLines = new ArrayList<String>(); 
				
		System.out.println("Description:");
		
		for (int i = 0; i < strArray.length; i++) {
			
			descriptionLines.add(strArray[i].trim());
			System.out.println(descriptionLines.get(i));
		}
		
		
		// Lines expected in 'Description'		
		
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		String totalCost = "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; 
		
		String itemsOrderedLabel = "Items Ordered:";
		
		String deviceModelAndCost = deviceInfoActions.name + " " + deviceInfoActions.cost; 
		
		String planNameAndCost = planInfoActions.planSelectedName + " " + planInfoActions.PlanTextCost() + " Monthly";
		
		List<String> optFeatures = new ArrayList<>();
		
		for (int i = 0; i < BaseClass.optionalFeaturesList.size(); i++) {
			
			
		}
		
		for (int i = 0; i < BaseClass.accessoriesDetailsListExpected.size(); i++) {
			
			
		}
		
		String additionalInfoLabel = "Additional Info:";
		
		String additionalInfoContactNumber = "Contact Phone Number:" + contactNumber; 
		
		String additionalInfoExt = "Ext:" + extension; 
		
		String additionalInfoAdditionalInstructions = "Additional Instructions:" + additionalInstructions; 
		
		String additionalInfoServiceNumber = "Service Number:" + serviceNumber; 
		
		String additionalInfoReason = "Reason:" + DeviceInfoActions.reasonUpgradeAction; 
		
		String shipTo = "Ship to: " + userLimitedShorterName + " " + addressLineOne + " " + cityOrderActions + " " + stateOrderActions + " " + zipCodeOrderActions + " ."; 
		
		String orderId = "Tangoe Order ID:" + orderDetailsObjectExpected.orderId;
		
		String externalOrderNumber = "External Order Number:" + orderDetailsObjectExpected.externalOrderId;
		
		
		if(descriptionLines.contains(title)) System.out.println("Ok"); else {System.out.println("Not found - " + title);}
		if(descriptionLines.contains(totalCost)) System.out.println("Ok"); else {System.out.println("Not found - " + totalCost);}
		if(descriptionLines.contains(itemsOrderedLabel)) System.out.println("Ok"); else {System.out.println("Not found - " + itemsOrderedLabel);}
		if(descriptionLines.contains(deviceModelAndCost)) System.out.println("Ok"); else {System.out.println("Not found - " + deviceModelAndCost);}
		if(descriptionLines.contains(planNameAndCost)) System.out.println("Ok"); else {System.out.println("Not found - " + planNameAndCost);}
		if(descriptionLines.contains(additionalInfoLabel)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoLabel);}
		if(descriptionLines.contains(additionalInfoContactNumber)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoContactNumber);}
		if(descriptionLines.contains(additionalInfoExt)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoExt);}
		if(descriptionLines.contains(additionalInfoAdditionalInstructions)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoAdditionalInstructions);}
		if(descriptionLines.contains(additionalInfoServiceNumber)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoServiceNumber);}
		if(descriptionLines.contains(additionalInfoReason)) System.out.println("Ok"); else {System.out.println("Not found - " + additionalInfoReason);}
		if(descriptionLines.contains(shipTo)) System.out.println("Ok"); else {System.out.println("Not found - " + shipTo);}
		if(descriptionLines.contains(orderId)) System.out.println("Ok"); else {System.out.println("Not found - " + orderId);}
		if(descriptionLines.contains(externalOrderNumber)) System.out.println("Ok"); else {System.out.println("Not found - " + externalOrderNumber);}
		
		
		
		Assert.assertTrue(descriptionLines.contains(title), errMessage);
		// Assert.assertTrue(descriptionLines.contains(totalCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		Assert.assertTrue(descriptionLines.contains(itemsOrderedLabel), errMessage);
		// Assert.assertTrue(descriptionLines.contains(deviceModelAndCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		Assert.assertTrue(descriptionLines.contains(planNameAndCost), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoLabel), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoContactNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoExt), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoAdditionalInstructions), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoServiceNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoReason), errMessage);
		Assert.assertTrue(descriptionLines.contains(shipTo), errMessage);
		Assert.assertTrue(descriptionLines.contains(orderId), errMessage);
		Assert.assertTrue(descriptionLines.contains(externalOrderNumber), errMessage);
		
		
		
		
		// *************************************
/*		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		
		// build title that is in short description. 
		//String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(strArray[0].trim(), title, errMessage);
		
		tmpStr =  "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; //total costs
		// Assert.assertEquals(strArray[2].trim(), tmpStr, errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED

		Assert.assertEquals(strArray[4].trim(), "Items Ordered:", errMessage); // title for items ordered.
		
		tmpStr = deviceInfoActions.name + " " + deviceInfoActions.cost; // device and cost
		//Assert.assertEquals(strArray[5], tmpStr, errMessage);	// -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED	

		tmpStr = planInfoActions.planSelectedName + " " + planInfoActions.PlanTextCost() + " Monthly"; // plan and cost.  
		Assert.assertEquals(strArray[6], tmpStr , errMessage);		
		
		// need to insert "-" to make the two planOptions match format of plan options stored in the 'optionalFeaturesList' list.
		PlanOptionalFeatures.VerifyPlanOptionsFirstLast(strArray[7].replace("$", "- $"), strArray[8].replace("$", "- $"));
		
		Assert.assertEquals(strArray[11], "Additional Info:" , errMessage); // title		
		
		Assert.assertEquals(strArray[12].replace("Contact Phone Number:", ""), contactNumber, errMessage); 
		Assert.assertEquals(strArray[13].replace("Ext:", ""), extension, errMessage); 
		Assert.assertEquals(strArray[14].replace("Additional Instructions:", ""), additionalInstructions, errMessage); 		
		Assert.assertEquals(strArray[15].replace("Service Number:", ""), serviceNumber, errMessage); 
		Assert.assertEquals(strArray[16].replace("Reason:", "").trim(), DeviceInfoActions.reasonUpgradeAction, errMessage);		
		
		// build and verify 'ship to' string.
		String space = " ";
		tmpStr = userLimitedShorterName + space + addressLineOne + space + cityOrderActions + space + stateOrderActions + space + zipCodeOrderActions; 
		Assert.assertEquals(strArray[18].replace("Ship to:", "").replace(".","").trim(), tmpStr, errMessage);
		
		Assert.assertEquals(strArray[20].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId, errMessage);		
		Assert.assertEquals(strArray[21].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId, errMessage);		
	*/
	}
	
	
	public static void VerifyUpdateFeatures() throws Exception
	{
		String errMessage = "Failure in Approvals.VerifyUpdateFeatures.";
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		DebugTimeout(0, "Verify UpdateFeatures in approval page.");		

		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(strArray[0].trim(), title, errMessage);
		
		// NOTE: 'Feature.finalCost' has been hacked until DE# 108276 is fixed -- Defect is fixed -- 9/20/17 
		//Assert.assertEquals(strArray[2].replace("$-", "-$").replace("Total Monthly Cost:", "").trim(), Feature.finalCost, errMessage);
		
		String totalCostFound = strArray[2].trim();
		String totalCostExpected = Feature.finalCost.replace("$", "");
		
		if (totalCostExpected.startsWith("-")) {
			
			Assert.assertEquals(totalCostFound, "Total Monthly Cost Savings:$" + totalCostExpected.replace("-", ""));
					
		} else {
			
			Assert.assertEquals(totalCostFound, "Total Monthly Cost:$" + totalCostExpected);
			
		}
		
		//Assert.assertEquals(strArray[2].replace("$", "").replace("Total Monthly Cost:", "").trim(), Feature.finalCost, errMessage);
		Assert.assertEquals(strArray[4].trim(), "Items Ordered:" , errMessage);		
		
		// create a Feature object for each of the features listed in "Items: Ordered" 
		Feature featureOne = new Feature(strArray[5].split(":")[1].split("\\$")[0].trim(), "$" + strArray[5].split(":")[1].split("\\$")[1].trim());
		Feature featureTwo = new Feature(strArray[6].split(":")[1].split("\\$")[0].trim(), "$" + strArray[6].split(":")[1].split("\\$")[1].trim());
		
		// this has a hack with the minus and dollar sign because of DE# 108276
		Feature featureThree = new Feature(strArray[7].split(":")[1].split("\\$")[0].trim(), strArray[7].split(":")[1].split("\\$")[1].trim().replace("-", "-$"));
		
		Feature.VerifyUpdateFeatures(featureOne, featureTwo, featureThree); // verify the feature update objects are correct. 
		
		Assert.assertEquals(strArray[9].trim(), "Additional Info:", errMessage);
		Assert.assertEquals(strArray[10].trim().replace("Contact Phone Number:", "").trim(), contactNumber, errMessage);
		Assert.assertEquals(strArray[11].trim().replace("Ext:", ""), preferredAreaCode, errMessage);		
		Assert.assertEquals(strArray[12].replace("Additional Instructions:", "").trim(), additionalInstructions, errMessage);
		Assert.assertEquals(strArray[13].replace("Service Number:", "").trim(), serviceNumber, errMessage);
		Assert.assertEquals(strArray[15].replace("Tangoe Order ID:", ""), orderDetailsObjectExpected.orderId.trim(), errMessage);		
		Assert.assertEquals(strArray[16].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId.trim(), errMessage);		
	}
	
	public static void VerifyOrderAccessories() throws Exception
	{
		String errMessage = "Failure in Approvals.VerifyOrderAccessories.";
		
		DebugTimeout(0, "Verify OrderAccessories in approval page.");
		strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		
		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(strArray[0].trim(), title, errMessage);
		Assert.assertEquals(strArray[2].replace("Total Cost:", "").trim(), AccessoriesDetailsExpected.finalCost, errMessage);		
		Assert.assertEquals(strArray[4],"Items Ordered: ", errMessage);		
		
		// create AccessoriesDetailsExpected objects from the accessories in the main description section.
		AccessoriesDetailsExpected actualObjectOne = new AccessoriesDetailsExpected(strArray[5].split("\\$")[0].trim(), "", "$" + strArray[5].split("\\$")[1]);
		AccessoriesDetailsExpected actualObjectTwo;

		int lineIndex = 0; // Needed for the case of a second accessory in the order, there will be one more line in the description - Ana add 9/20/17
		
		// If there's 1 accessory in the order
		if (accessoriesInCartHashMap.size() == 1) {
			
			// verify accessory.
			AccessoriesDetailsExpected.verifyOrderAccessories(actualObjectOne);
		}
					
		
		// If there are 2 accessories in the order
		if (accessoriesInCartHashMap.size() == 2) {
			
			actualObjectTwo  = new AccessoriesDetailsExpected(strArray[6].split("\\$")[0].trim(), "", "$" + strArray[6].split("\\$")[1]);
			
			// verify accessories.
			AccessoriesDetailsExpected.verifyOrderAccessories(actualObjectOne, actualObjectTwo);
			lineIndex = 1;
		}
		
		Assert.assertEquals(strArray[7 + lineIndex],"Additional Info:", errMessage);		
		Assert.assertEquals(strArray[8 + lineIndex].replace("Contact Phone Number:", ""), contactNumber, errMessage);		
		Assert.assertEquals(strArray[9 + lineIndex].replace("Ext:", ""), extension, errMessage);		
		Assert.assertEquals(strArray[10 + lineIndex].replace("Additional Instructions:", ""), additionalInstructions, errMessage);		
		Assert.assertEquals(strArray[11 + lineIndex].replace("Service Number:", ""), serviceNumber, errMessage);		
		
		// build and verify 'ship to' string.
		String space = " ";
		String expedite = " - please expedite this shipment.";
		title = userLimitedShorterName + space + addressLineOne + space + cityOrderActions + space + stateOrderActions + space + zipCodeOrderActions + 
				space + expedite;
		Assert.assertEquals(strArray[13 + lineIndex].replace("Ship to: ", ""), title, errMessage);
		
		Assert.assertEquals(strArray[15 + lineIndex].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId, errMessage);		
		Assert.assertEquals(strArray[16 + lineIndex].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId, errMessage);		
		
	}					
	
	// this tests items similar to many actions. 
	public static void VerifySubset(String [] tmpArray) throws Exception
	{
		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(tmpArray[0].trim(), title, "");
		Assert.assertEquals(tmpArray[2],"Additional Info:","");		
		Assert.assertEquals(tmpArray[3].replace("Contact Phone Number:",""), contactNumber,"");		
		Assert.assertEquals(tmpArray[4].replace("Additional Instructions:",""), additionalInstructions,"");		
		Assert.assertEquals(tmpArray[5].replace("Service Number:",""), serviceNumber,"");		
		Assert.assertEquals(tmpArray[8].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId,"");		
		Assert.assertEquals(tmpArray[9].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId,"");		
	}
	
	// this tests items similar to many actions. 
	public static void VerifySwapDevices(String [] additionalInformation) throws Exception
	{
		// build title that is in short description. 		
		String title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(additionalInformation[0].trim(), title, "");
		
		for (int i = 1; i < additionalInformation.length; i++) {
			
			String label = additionalInformation[i].split("\\:")[0];
						
			switch (label) {
			
				case "Contact Phone Number":
					
					Assert.assertEquals(additionalInformation[i], "Contact Phone Number:" + contactNumber);
					break;
				
				case "Ext":
					
					Assert.assertEquals(additionalInformation[i], "Ext:" + extension);
					break;
				
				case "Additional Instructions":
					
					Assert.assertEquals(additionalInformation[i], "Additional Instructions:" + additionalInstructions);
					break;
						
				case "Service Number":
					
					Assert.assertEquals(additionalInformation[i], "Service Number:" + serviceNumber);
					break;
				
				case "Preferred Area Code":
					
					Assert.assertEquals(additionalInformation[i], "Preferred Area Code:" + preferredAreaCode);
					break;
						
				case "Tangoe Order ID":
					
					Assert.assertEquals(additionalInformation[i], "Tangoe Order ID:" + orderDetailsObjectExpected.orderId);
					break;
					
				case "External Order Number":
					
					Assert.assertEquals(additionalInformation[i], "External Order Number:" + orderDetailsObjectExpected.externalOrderId);
					break;
			
			}
		
		}
		
	}	
	
	// this tests items similar to many actions. 
	public static void VerifyUnsuspendSubset(String [] tmpArray) throws Exception
	{
		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(tmpArray[0].trim(), title, "");
		Assert.assertEquals(tmpArray[2],"Additional Info:","");		
		Assert.assertEquals(tmpArray[3].replace("Contact Phone Number:",""), contactNumber,"");		
		Assert.assertEquals(tmpArray[4].replace("Additional Instructions:",""), additionalInstructions,"");		
		Assert.assertEquals(tmpArray[5].replace("Service Number:",""), serviceNumber,"");	
		Assert.assertEquals(tmpArray[7].replace("User:",""), userLimitedFullNameExtended,"");		
		
		Assert.assertEquals(tmpArray[9].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId,"");		
		Assert.assertEquals(tmpArray[10].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId,"");		
	}	
}
