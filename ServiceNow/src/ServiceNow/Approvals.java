package ServiceNow;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.BIPUSH;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.gargoylesoftware.htmlunit.javascript.host.dom.ShadowRoot;

import ActionClasses.NewActivation;
import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.CalendarDateTimeObject;
import HelperObjects.DeviceInfoActions;
import HelperObjects.Feature;
import HelperObjects.PlanInfoActions;
import HelperObjects.ShoppingCart;



public class Approvals extends BaseClass
{
	
	public static int loopMax = 20;
	public static int rowNumContainingOrder = 0;

	static String [] strArray;
	static String tmpStr;	
	
	// this is common in many places. bob 7/27/18
	public static String commonXpathForDescriptions = ".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/../div/following-sibling::input[1]"; 	
	public static String commonXpathForDescriptionsSimple = "//input[@id='x_tango_mobility_tangoe_mobility_order_request.description']"; // 11/1/18 
	
	// this waits for first check box at top of approvals list and the 'to' text at the bottom of the page.
	public static void WaitForPageToLoad() throws Exception
	{
		//commented by Ana, replaced by line below - 9/5/17
		//WaitForElementClickable(By.xpath("(//a[text()='Requested'])[1]/../a"), MainTimeout, "Failed wait for clickable element.");		
		WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MainTimeout, "Failed wait for clickable element.");
		WaitForElementPresent(By.xpath("(//span[contains(text(),'to')])[3]"), MediumTimeout);		
	}
	
	
	// new method to replace ApprovalAction() method - approval part .... - 10/25/2017 - Ana 
	public static void selectAndApproveOrder() throws Exception {
	

		// Select order from list
		openOrderDetails(); 
		
		//Pause("");
	
		// Verify the items in the short and full description.
		verifyApprovalPageData();
		
		// If get to here the order to be approved has been found. do the approval and wait for main page to load. doing the approval brings user back to the main page.		
		
		// Select approver from dropdown list 
		int approverIndex = selectApprover();
		 
		// Approve order 
		approveOrder(approverIndex); 

		Thread.sleep(5000);  // ** Giving time for order to have status updated 
		
	}
	
	
	private static void refreshList() throws InterruptedException {
		
		System.out.println(".. Giving time for the order to be added to the list for " + timeOutBeforeLookingInApprovalList/1000 + " seconds ....");
 
		Thread.sleep(timeOutBeforeLookingInApprovalList); // sometimes it takes some time until the order is added to the list after is created. 
														  // giving some time for the order to be added to the list
		
		driver.findElement(By.xpath("//button[@data-list_id='sysapproval_approver']")).click();
		driver.findElement(By.xpath("//div[text()='Refresh List']")).click();
		
		Thread.sleep(2000);
		
	}


	// new method to replace ApprovalAction() method - reject part .... - 10/25/2017 - Ana
	public static void selectAndRejectOrder() throws Exception {
		
		// Select order from list using 'openOrderDetails()' method. in this method there is a wait 
		// time before searching through the approval page for the submitted order to approve. 
		// if first attempt to find the submitted order fails, call 'openOrderDetails()' again.
		if(!openOrderDetails())  
		{
			if(!openOrderDetails())
			{
				Assert.fail("Failed to find the submitted order in the approval page.");
			}
		}
	
		// Verify the items in the short and full description.
		verifyApprovalPageData();
		
		// If get to here the order to be approved has been found. do the approval and wait for main page to load. doing the approval brings user back to the main page.		
		
		// Select approver from dropdown list 
		int approverIndex = selectApprover();
		 
		// Approve order 
		rejectOrder(approverIndex);

		Thread.sleep(5000);  // ** Giving time for order to have status updated
	}
	
	//tbody[@class='list2_body']/tr[1]/td[3]/a // 12/16/18 - for WebElement orderNumLink
	
	private static boolean openOrderDetails() throws Exception {
		
		// added 8/9/2018 - modified 8/22/2018
		refreshList(); 					
		
		boolean correctUserAndType = false;
		boolean correctExternalOrderId = false;
		int x = 1;
		int numAttempts = 7;
		
		do {	
			
			WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MediumTimeout, "Failed waiting for row in Approvals.ApprovalAction");
			
			// select row
			System.out.println(" *** Click on order listed # " + x + " ***");
			
			WebElement orderNumLink = driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + x + "]/td[7]/a[@class='linked']"));
			orderNumLink.click(); 
			
			// wait for the short description and  description text areas to become visible. 
			WaitForElementVisible(By.xpath(".//*[@id='sys_readonly.x_tango_mobility_tangoe_mobility_order_request.short_description']"), MediumTimeout);		
			WaitForElementVisible(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/.."), MediumTimeout); 			
			
			// Verify order type and user name in short description
			// **** UNCOMMENT LINE BELOW when defect 114917 is fixed *** Defect fixed
			correctUserAndType = isCorrectOrderTypeAndUser(driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value"));			

			// Verify external order number and order id in full description
			correctExternalOrderId = containsCorrectExternalOrderIdAndOrderId(driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n")); 			
			
			ShowText("------------------------------------------------------------------------");
			System.out.println(correctUserAndType + " " + correctExternalOrderId);
			ShowText("------------------------------------------------------------------------");
			
			if(correctUserAndType && correctExternalOrderId)
			{
				rowNumContainingOrder = 1;  //x;
				
			} else { // If any of the expected values is not correct, then click 'Back' button
				
				driver.findElement(By.xpath("//span[text()='Back']/..")).click();
				WaitForPageToLoad();		
				
				x++;
			}
		
		} while ((x <= numAttempts) && !(correctUserAndType && correctExternalOrderId));
		//} while ((x <= loopMax) && !(correctUserAndType && correctExternalOrderId));
		
		// 1/17/19 - removed assert below.
		// verify order to approve was found. if the order action wasn't found within loop max rows it looks like it can't be found. 
		//Assert.assertTrue(x <= loopMax, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");
		//Assert.assertTrue(x <= 10, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");
		
		// added 1/17/19
		if(x > numAttempts)
		{
			ShowText("Have not found expected approval record in 'openOrderDetails()' method..." );
			return false;
		}
		return true;
	}
	
	// 12/16/18 - to be used if this not fixed - TNGMOB-9 - Service Now Submitted Order Can’t Be Approved/Rejected From Pulldown Selection.
	private static void OpenOrderDetailsTwo() throws Exception  
	{
		
		// added 8/9/2018 - modified 8/22/2018
		// refreshList(); bladd uncomment 					
		
		boolean correctUserAndType = false;
		boolean correctExternalOrderId = false;
		int x = 1;
		
		do {	
			
			WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MediumTimeout, "Failed waiting for row in Approvals.ApprovalAction");
			
			// select row
			System.out.println(" *** Click on order listed # " + x + " ***");
			
			//tbody[@class='list2_body']/tr[1]/td[3]/a // 12/16/18 - for WebElement orderNumLink
			
			WebElement orderNumLink = driver.findElement(By.xpath("tbody[@class='list2_body']/tr[1]/td[3]/a")); // new to select second column
			//WebElement orderNumLink = driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + x + "]/td[7]/a[@class='linked']"));
			orderNumLink.click(); 
			
			// wait for the short description and  description text areas to become visible. 
			WaitForElementVisible(By.xpath(".//*[@id='sys_readonly.x_tango_mobility_tangoe_mobility_order_request.short_description']"), MediumTimeout);		
			WaitForElementVisible(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/.."), MediumTimeout); 			
			
			// Verify order type and user name in short description
			// **** UNCOMMENT LINE BELOW when defect 114917 is fixed *** Defect fixed
			correctUserAndType = isCorrectOrderTypeAndUser(driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value"));			

			// Verify external order number and order id in full description
			correctExternalOrderId = containsCorrectExternalOrderIdAndOrderId(driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n")); 			
			
			ShowText("------------------------------------------------------------------------");
			System.out.println(correctUserAndType + " " + correctExternalOrderId);
			ShowText("------------------------------------------------------------------------");
			
			if(correctUserAndType && correctExternalOrderId)
			{
				rowNumContainingOrder = 1;  //x;
				
			} else { // If any of the expected values is not correct, then click 'Back' button
				
				driver.findElement(By.xpath("//span[text()='Back']/..")).click();
				WaitForPageToLoad();		
				
				x++;
			}
		
		} while ((x <= 10) && !(correctUserAndType && correctExternalOrderId));
		//} while ((x <= loopMax) && !(correctUserAndType && correctExternalOrderId));
		
		// verify order to approve was found. if the order action wasn't found within loop max rows it looks like it can't be found. 
		//Assert.assertTrue(x <= loopMax, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");
		Assert.assertTrue(x <= 10, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");
	}
	
	
	
	
	public static void openOrderDetails(String orderId, String externalOrderId) throws Exception {
		
		boolean correctUserAndType = false;
		boolean correctExternalOrderId = false;
		int x = 1;

		do {	
			
			WaitForElementClickable(By.cssSelector("tbody.list2_body>tr>td>span>input.checkbox:nth-of-type(1)"), MediumTimeout, "Failed waiting for row in Approvals.ApprovalAction");
			
			// select row
			System.out.println(" *** Click on order listed # " + x + " ***");
			
			WebElement orderNumLink = driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + x + "]/td[7]/a[@class='linked']"));  // "//tbody[@class='list2_body']/tr[1]/td[7]/a[@class='linked']"
			orderNumLink.click(); 
			
			// wait for the short description and  description text areas to become visible. 
			
			By shortDescription = By.xpath(".//*[@id='sys_readonly.x_tango_mobility_tangoe_mobility_order_request.short_description']");
			By fullDescription = By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/..");
			
			WaitForElementVisible(shortDescription, MediumTimeout);		
			WaitForElementVisible(fullDescription, MediumTimeout);
			
			// Verify order type and user name in short description
			correctUserAndType = isCorrectOrderTypeAndUser(driver.findElement(shortDescription).getAttribute("value"));

			// Verify external order number and order id in full description
			correctExternalOrderId = containsCorrectExternalOrderIdAndOrderId(driver.findElement(fullDescription).getText().split("\n"), orderId, externalOrderId);
			
			if(correctUserAndType && correctExternalOrderId)
			{
				rowNumContainingOrder = x;
				
			} else { 
			
				x++;
			}
			
			driver.findElement(By.xpath("//span[text()='Back']/..")).click();
			
			WaitForPageToLoad();
			
		} while ((x <= loopMax) && !(correctUserAndType && correctExternalOrderId));
		
		
		// verify order to approve was found. if the order action wasn't found within loop max rows it looks like it can't be found. 
		Assert.assertTrue(x <= loopMax, "Failed to find user with correct Order Type, External Order Id, or Order Id in  Approvals.FindApprovalAndApprove.");
		
	}
	
	
	

	private static int selectApprover() {
		
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
		
		return approverIndex;
		
	}
	
	

	
	private static void rejectOrder(int approverIndex) {
		
		// The 'Comments' textbox is not displayed anymore. Text for Comment is something like: 
		// "Order was rejected in ServiceNow. Approvers: rejected:Bob Lichtenfels-Approver,
		//  not_required:Mike McPadden, not_required:ServiceNow Certifier - Admin, not_required:ServiceNow Certifier - Approver ."
		// The 'Reject' button is not listed anymore. --> So the Reject action must be selected from a dropdown list 			
		
		
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
		
		// verify the order row that was rejected shows rejected. -- ** NEEDS TO BE REVIEWED ** 
		// Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumContainingOrder + "]/td[3]/a")).getText(), "Rejected", 
		//				"Item that was approved in Row " + rowNumContainingOrder + " does not show as approved in the main Approvals list in Approvals.FindApprovalAndApprove.");
			
		
		System.out.println("Order has been rejected.");
		
		
	}
	
	

	
	private static void approveOrder(int approverIndex) {
		
		// The 'Approve' button is not listed anymore. --> So the Approve action must be selected from a dropdown list 
		
		// 3. The action ('Approve' in this case) must be selected from a dropdown list located below 
		
		new Select(driver.findElement(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select"))).selectByVisibleText("Approve");
		
		// 4. Verify that the State changes to 'Approved'
		
		Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + approverIndex + "]/td[3]")).getText(), "Approved", "Failed. State is not Approved.");	
		
		
		// now set the order details expected status to 'In Fulfillment'
		orderDetailsObjectExpected.status = "In Fulfillment";			
		
		// verify the order row that was approved shows approved.  -- ** NEEDS TO BE REVIEWED ** 
		//Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumContainingOrder + "]/td[3]/a")).getText(), "Approved", 
		//				"Item that was approved in Row " + rowNumContainingOrder + " does not show as approved in the main Approvals list in Approvals.FindApprovalAndApprove.");	
		
		System.out.println("Order has been approved.");
		
		
	}
	
	

	// this looks at the short description for the full user name of the limited user that requested the order and the order type.
	public static boolean isCorrectOrderTypeAndUser(String shortDescription)
	{
		
		if(approvalActionType.equals(approvalActionType.newActivation))
		{
			if(shortDescription.contains(userLimitedShorterName) && shortDescription.contains(orderDetailsObjectExpected.orderTypeNewActivation))
			{
				return true;
			}
			return false;			
		}
		else
		{
			if(shortDescription.contains(userLimitedShorterName) && shortDescription.contains(orderDetailsObjectExpected.orderType))
			{
				return true;
			}
			return false;			
		}
		
		//if(shortDescription.contains(userLimitedShorterName) && shortDescription.contains(orderDetailsObjectExpected.orderType))
		//{
		//	return true;
		//}
		//return false;
		
	}	
	
	
	public static boolean containsCorrectExternalOrderIdAndOrderId(String[] descriptionAllLines) throws Exception
	{
		
		boolean externOrderIdOK = descriptionAllLines[descriptionAllLines.length - 1].split(":")[1].equals(orderDetailsObjectExpected.externalOrderId);
		boolean orderIdOK = descriptionAllLines[descriptionAllLines.length - 2].split(":")[1].equals(orderDetailsObjectExpected.orderId);
		
		return (externOrderIdOK && orderIdOK);
		
	}	
	
	
	public static boolean containsCorrectExternalOrderIdAndOrderId(String[] descriptionAllLines, String orderId, String externalOrderId) throws Exception
	{
		
		System.out.println("External Order Id Expected: " + descriptionAllLines[descriptionAllLines.length - 1].split(":")[1]);
		System.out.println("Order Id Expected: " + descriptionAllLines[descriptionAllLines.length - 2].split(":")[1]);
		
		System.out.println("External Order Id Found: " + externalOrderId);
		System.out.println("Order Id Found: " + orderId);
		
		boolean externOrderIdOK = descriptionAllLines[descriptionAllLines.length - 1].split(":")[1].equals(externalOrderId);
		boolean orderIdOK = descriptionAllLines[descriptionAllLines.length - 2].split(":")[1].equals(orderId);
		
		return (externOrderIdOK && orderIdOK);
		
	}	
	
	
	public static void verifyApprovalPageData() throws Exception
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
			case upgradeService:
			{
				verifyUpgradeService();				
				break;
			}
			case updateFeatures:
			{
				VerifyUpdateFeatures();
				break;
			}
			case portNumber:
			{
				verifyPortNumber();
				break;
			}
			case transferServiceInAndPort:
			{
				verifyPortNumber();
				break;
			}
			case transferServiceIn:
			{
				verifyTransferServiceIn();
				break;
			}	
			case transferServiceOut:
			{
				VerifyTransferServiceOut();
				break;
			}	
			case newActivation:
			{
				VerifyNewActivation();
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

	
	private static void verifyTransferServiceIn() throws Exception {
		
		String errMessage = "Failure in Approvals.verifyTransferServiceIn.";
		
		// Lines found in 'Description'
		//strArray = driver.findElement(By.name("x_tango_mobility_tangoe_mobility_order_request.description")).getAttribute("value").split("\n");
		
		WaitForElementPresent(By.name("x_tango_mobility_tangoe_mobility_order_request.description"), MediumTimeout);
		//WaitForElementPresent(By.xpath(commonXpathForDescriptions), 5);
		
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n");
		//strArray = driver.findElement(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/..")).getAttribute("value").split("\n");  	
		List<String> descriptionLines = new ArrayList<String>(); 
				
		System.out.println("Description:");
		
		for (int i = 0; i < strArray.length; i++) {
			
			descriptionLines.add(strArray[i].trim());
			System.out.println(descriptionLines.get(i));
		}
		
		
		// Lines expected in 'Description'		
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + ".";  //"Order for " + userLimitedShorterName + "."; // Change it to:
																	// orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + newServiceNumber + "]."; 
																	// it after defect 114917 is fixed 
				
		//String totalCost = "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; 
		String totalCost = "Total Cost:$" + ShoppingCart.costOneTime + " Total Monthly Cost:$" + ShoppingCart.costMonthly;
		
		String itemsOrderedLabel = "Items Ordered:";
		
		//String deviceModelAndCost = deviceInfoActions.name + " " + deviceInfoActions.cost; 
		
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
		
		String additionalInfoServiceNumber = "Service Number:" + newServiceNumber; 
		
		String additionalInfoCarrierAcctNumber = "Carrier Account Number:" + PlanInfoActions.carrierAccountNumber; 
		
		String shipTo = "Ship to: " + userName + " " + addressLineOne + " " + cityOrderActions + " " + stateOrderActions + " " + zipCodeOrderActions + "  - please expedite this shipment."; 
		
		String orderId = "Tangoe Order ID:" + orderDetailsObjectExpected.orderId;
		
		String externalOrderNumber = "External Order Number:" + orderDetailsObjectExpected.externalOrderId;

	
		
		System.out.println("totalCost: " + totalCost);
		//System.out.println("deviceModelAndCost: " + deviceModelAndCost);
		
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
		Assert.assertTrue(descriptionLines.contains(additionalInfoCarrierAcctNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(shipTo), errMessage);
		Assert.assertTrue(descriptionLines.contains(orderId), errMessage);
		Assert.assertTrue(descriptionLines.contains(externalOrderNumber), errMessage);
		
	}


	private static void verifyPortNumber() throws Exception {
		
		String errMessage = "Failure in Approvals.verifyTransferServiceIn.";
		
		// Lines found in 'Description'
		//strArray = driver.findElement(By.name("x_tango_mobility_tangoe_mobility_order_request.description")).getAttribute("value").split("\n");
		
		WaitForElementPresent(By.name("x_tango_mobility_tangoe_mobility_order_request.description"), 5);
		//WaitForElementPresent(By.xpath(commonXpathForDescriptions), 5);
		
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n");
		//strArray = driver.findElement(By.xpath(".//*[@id='x_tango_mobility_tangoe_mobility_order_request.description']/..")).getAttribute("value").split("\n");  	
		List<String> descriptionLines = new ArrayList<String>(); 
				
		System.out.println("Description:");
		
		for (int i = 0; i < strArray.length; i++) {
			
			descriptionLines.add(strArray[i].trim());
			System.out.println(descriptionLines.get(i));
		}
		
		
		// Lines expected in 'Description'		
		//String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + ".";  //"Order for " + userLimitedShorterName + "."; // Change it to:
																	// orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + newServiceNumber + "]."; 
																	// it after defect 114917 is fixed 

		String title = "";
		
		if((approvalActionType.equals(approvalActionType.portNumber)))
		{
			title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName +  " [" +  fullServiceNumber  + "]" +  ".";  //"Order for " + userLimitedShorterName + "."; // Change it to:
			// orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + newServiceNumber + "]."; 
			// it after defect 114917 is fixed
		}
		else
		{
			// Lines expected in 'Description'		
			title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + ".";  //"Order for " + userLimitedShorterName + "."; // Change it to:
																		// orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + newServiceNumber + "]."; 
																		// it after defect 114917 is fixed 
		}
		
		//String totalCost = "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; 
		String totalCost = "Total Cost:$" + ShoppingCart.costOneTime + " Total Monthly Cost:$" + ShoppingCart.costMonthly;
		
		String itemsOrderedLabel = "Items Ordered:";
		
		//String deviceModelAndCost = deviceInfoActions.name + " " + deviceInfoActions.cost; 
		
		String planNameAndCost = planInfoActions.planSelectedName + " " + planInfoActions.PlanTextCost() + " Monthly";
		
		List<String> optFeatures = new ArrayList<>();
		
		for (int i = 0; i < BaseClass.optionalFeaturesList.size(); i++) {
			
			
		}
		
		for (int i = 0; i < BaseClass.accessoriesDetailsListExpected.size(); i++) {
			
			
		}
		
		String additionalInfoLabel = "Additional Info:";
		
		String additionalInfoCarrierAcctNumber = "Carrier Account Number:" + PlanInfoActions.carrierAccountNumber; 
		
		String additionalInfoNameOnInvoice = "Name on Invoice:" + userLimitedShorterName;
		
		String additionalInfoContactNumber = "Contact Phone Number:" + contactNumber; 
		
		String additionalInfoExt = "Ext:" + extension; 
		
		String additionalInfoAdditionalInstructions = "Additional Instructions:" + additionalInstructions; 
		
		String additionalInfoServiceNumber = "";
		
		if((approvalActionType.equals(approvalActionType.portNumber))) 
		{
			additionalInfoServiceNumber = "Service Number:" + serviceNumber;
		}
		else
		{
			additionalInfoServiceNumber = "Service Number:" + newServiceNumber;
		}
		
		//String additionalInfoServiceNumber = "Service Number:" + newServiceNumber; // orig 
		
		String shipTo = "Ship to: " + userName + " " + addressLineOne + " " + cityOrderActions + " " + stateOrderActions + " " + zipCodeOrderActions + "  - please expedite this shipment."; 
		
		String orderId = "Tangoe Order ID:" + orderDetailsObjectExpected.orderId;
		
		String externalOrderNumber = "External Order Number:" + orderDetailsObjectExpected.externalOrderId;

	
		
		System.out.println("totalCost: " + totalCost);
		//System.out.println("deviceModelAndCost: " + deviceModelAndCost);
		
		Assert.assertTrue(descriptionLines.contains(title), errMessage); 
		// Assert.assertTrue(descriptionLines.contains(totalCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		Assert.assertTrue(descriptionLines.contains(itemsOrderedLabel), errMessage);
		// Assert.assertTrue(descriptionLines.contains(deviceModelAndCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		Assert.assertTrue(descriptionLines.contains(planNameAndCost), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoLabel), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoCarrierAcctNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoNameOnInvoice), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoContactNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoExt), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoAdditionalInstructions), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoServiceNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(shipTo), errMessage);
		Assert.assertTrue(descriptionLines.contains(orderId), errMessage);
		Assert.assertTrue(descriptionLines.contains(externalOrderNumber), errMessage);
		
	}


	private static void verifyUpgradeService() {
		
		String errMessage = "Failure in Approvals.VerifyUpgradeService.";
		
		// Lines found in 'Description'
		
		// strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n");
		
		List<String> descriptionLines = new ArrayList<String>(); 
				
		System.out.println("Description:");
		
		for (int i = 0; i < strArray.length; i++) {
			
			descriptionLines.add(strArray[i].trim());
			System.out.println(descriptionLines.get(i));
		}
		
		
		// Lines expected in 'Description'		
		
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		//String totalCost = "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; 
		String totalCost = "Total Cost:$" + ShoppingCart.costOneTime + " Total Monthly Cost:$" + ShoppingCart.costMonthly;
		
		String itemsOrderedLabel = "Items Ordered:";
		
		//String deviceModelAndCost = deviceInfoActions.name + " " + deviceInfoActions.cost; 
		
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

	
		
		System.out.println("totalCost: " + totalCost);
		//System.out.println("deviceModelAndCost: " + deviceModelAndCost);
		
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
	}

	public static void VerifyTransferServiceOut() throws Exception 
	{
		DebugTimeout(0, "Verify transfer service out in approval page.");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
		String expectedTitle = DeviceInfoActions.transferServiceOut + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";

		// verify title
		Assert.assertEquals(strArray[0].trim(), expectedTitle, "");
		
		Assert.assertEquals(strArray[2],"Additional Info:","");		
		Assert.assertEquals(strArray[3].replace("Additional Instructions:",""), additionalInstructions,"");		
		Assert.assertEquals(strArray[4].replace("Date of Birth:",""), birthDate + " 00:00:00",""); 
		Assert.assertEquals(strArray[5].replace("Contact Phone Number:",""), contactNumber,"");
		Assert.assertEquals(strArray[6].replace("Ext:",""), extension,"");
		Assert.assertEquals(strArray[7].replace("Personal E-mail Address:",""), approverAdminMail,"");
		Assert.assertEquals(strArray[8].replace("Social Security Number:",""), socialSecurityNumber,"");		
		Assert.assertEquals(strArray[9].replace("Driver's License Number:",""), licenseNumber,"");
		Assert.assertEquals(strArray[10].replace("Driver's License Exp. Date:",""), licenseExpire + " 00:00:00","");		
		Assert.assertEquals(strArray[11].replace("Driver's License State/ Province:",""), userStateShort,"");		
		Assert.assertEquals(strArray[12].replace("Service Number:",""), serviceNumber,"");		
		Assert.assertEquals(strArray[14].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId,"");		
		Assert.assertEquals(strArray[15].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId,"");		
	}		
	
	public static void VerifyDeactivate() throws Exception
	{
		DebugTimeout(0, "Verify Deactivate in approval page.");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
		

		Assert.assertEquals(strArray[2],"Additional Info:","");		
		Assert.assertEquals(strArray[6].replace("Reason:",""), reasonAction,"");	

		// this covers the rest of the items.
		VerifySubset(strArray);
	}	
	
	public static void VerifyNewActivation() throws Exception
	{
		DebugTimeout(0, "Verify New Activation in approval page.");
		String strTemp = "";

		WaitForElementVisible(By.xpath("//span[text()='Short description']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//span[text()='Description']"), ShortTimeout);		
		
		//span[text()='Short description']
		//span[text()='Description']
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
		
		//for(String str: strArray){ShowText(str);}
		
		Assert.assertEquals(strArray[0], "New Device Order for " + userLimitedShorterName + ".  ");

		strTemp = " Total Cost:" +  PlanInfoActions.costValueInShoppingCart + " Total Monthly Cost:" + PlanInfoActions.planCostCompleteField.split(" ")[1];
		Assert.assertEquals(strArray[2], strTemp);
		
		Assert.assertEquals(strArray[4], "Items Ordered: ");		
		Assert.assertEquals(strArray[5], "AT&T Unite MiFi " + PlanInfoActions.costValueInShoppingCart);
		Assert.assertEquals(strArray[6], "AT&T 4GB Mobile Share Data Plan " + PlanInfoActions.planCostCompleteField.split(" ")[1] + " Monthly");		
		Assert.assertEquals(strArray[8], "Additional Info:");
		Assert.assertEquals(strArray[9], "Preferred Area Code:" + preferredAreaCode);
		Assert.assertEquals(strArray[10], "Contact Phone Number:" + contactNumber);		
		Assert.assertEquals(strArray[11], "Ext:" + extension);		
		Assert.assertEquals(strArray[12], "Additional Instructions:" + additionalInstructions);
		Assert.assertEquals(strArray[13], "Business Unit:" + buisnessUnit);		
		Assert.assertEquals(strArray[14], "Service Number Alias:" + serviceNumberAlias);
		Assert.assertEquals(strArray[15], "Reason:" + reasonOtherText);		
		
		strTemp = "  - please expedite this shipment.";
		Assert.assertEquals(strArray[17], "Ship to: " + userLimitedShorterName + " " + userLineOne + " " + userCity + " " + userStateShort + " " + userPostalCode + strTemp);		
		Assert.assertEquals(strArray[19], "Tangoe Order ID:" + orderDetailsObjectExpected.orderId);		
		Assert.assertEquals(strArray[20], "External Order Number:" + orderDetailsObjectExpected.externalOrderId);
	}	
	
	
	public static void VerifySuspend() throws Exception
	{
		DebugTimeout(0, "Verify Suspend in approval page.");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 
		
		// this verifies 'preferred suspension date' month and year.
		CalendarDateTimeObject.VerifyPreferredSuspendionDateForApproval(strArray[6].replace("Preferred Suspension Date:", ""));
		
		// this covers the rest of the items.
		//VerifySubset(strArray);
		
		VerifySubsetSuspend(strArray);
		
	}		
	
	public static void VerifyUnsuspend() throws Exception
	{
		DebugTimeout(0, "Verify Unsuspend in approval page.");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 
		
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
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
		
		VerifySwapDevices(strArray);
	}				
	
	public static void VerifyUpgradeDevice() throws Exception
	{	
		String errMessage = "Failure in Approvals.VerifyUpgradeDevice.";
			
		// Lines found in 'Description'
		
		// strArray = driver.findElement(By.id("x_tango_mobility_tangoe_mobility_order_request.description")).getText().split("\n");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n");
		
		List<String> descriptionLines = new ArrayList<String>(); 
				
		System.out.println("Description:");
		
		for (int i = 0; i < strArray.length; i++) {
			
			descriptionLines.add(strArray[i].trim());
			System.out.println(descriptionLines.get(i));
		}
		
		
		// Lines expected in 'Description'		
		
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		//String totalCost = "Total Cost:" + AccessoriesDetailsExpected.finalCost + " Total Monthly Cost:" + AccessoriesDetailsExpected.finalCostMonthly; 
		String totalCost = "Total Cost:$" + ShoppingCart.costOneTime + " Total Monthly Cost:$" + ShoppingCart.costMonthly;
		
		String itemsOrderedLabel = "Items Ordered:";
		
		String deviceModelAndCost = deviceInfoActions.name + " " + deviceInfoActions.cost; 
		
		//String planNameAndCost = planInfoActions.planSelectedName + " " + planInfoActions.PlanTextCost() + " Monthly";
		
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

	
		
		System.out.println("totalCost: " + totalCost);
		System.out.println("deviceModelAndCost: " + deviceModelAndCost);
		
		Assert.assertTrue(descriptionLines.contains(title), errMessage);
		// Assert.assertTrue(descriptionLines.contains(totalCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		Assert.assertTrue(descriptionLines.contains(itemsOrderedLabel), errMessage);
		// Assert.assertTrue(descriptionLines.contains(deviceModelAndCost), errMessage); // -- COMMENTING ASSERT UNTIL SFD112988 IS FIXED
		//Assert.assertTrue(descriptionLines.contains(planNameAndCost), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoLabel), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoContactNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoExt), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoAdditionalInstructions), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoServiceNumber), errMessage);
		Assert.assertTrue(descriptionLines.contains(additionalInfoReason), errMessage);
		Assert.assertTrue(descriptionLines.contains(shipTo), errMessage);
		Assert.assertTrue(descriptionLines.contains(orderId), errMessage);
		Assert.assertTrue(descriptionLines.contains(externalOrderNumber), errMessage);
		
				
	}
	
	
	public static void VerifyUpdateFeatures() throws Exception
	{
		String errMessage = "Failure in Approvals.VerifyUpdateFeatures.";
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
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
		Assert.assertEquals(strArray[11].trim().replace("Ext:", ""), extension, errMessage);		
		Assert.assertEquals(strArray[12].replace("Additional Instructions:", "").trim(), additionalInstructions, errMessage);
		Assert.assertEquals(strArray[13].replace("Service Number:", "").trim(), serviceNumber, errMessage);
		Assert.assertEquals(strArray[15].replace("Tangoe Order ID:", ""), orderDetailsObjectExpected.orderId.trim(), errMessage);		
		Assert.assertEquals(strArray[16].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId.trim(), errMessage);
	}
	
	public static void VerifyOrderAccessories() throws Exception
	{
		String errMessage = "Failure in Approvals.VerifyOrderAccessories.";
		
		DebugTimeout(0, "Verify OrderAccessories in approval page.");
		strArray = driver.findElement(By.xpath(commonXpathForDescriptions)).getAttribute("value").split("\n"); 		
		
		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   
		
		Assert.assertEquals(strArray[0].trim(), title, errMessage);
		Assert.assertEquals(strArray[2].replace("Total Cost:", "").trim(), ShoppingCart.costOneTime, errMessage);  // AccessoriesDetailsExpected.finalCost		
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
	
	// this tests suspend details 
	public static void VerifySubsetSuspend(String [] tmpArray) throws Exception
	{
		// build title that is in short description. 
		String title = orderDetailsObjectExpected.orderType + " Order for " + userLimitedShorterName + " [" + fullServiceNumber + "].";   

		//for(String str : tmpArray)
		//{
		//	ShowText(str);			
		//}
		//Pause("STOP");
		
		Assert.assertEquals(tmpArray[0].trim(), title, "");
		Assert.assertEquals(tmpArray[2],"Additional Info:","");		
		Assert.assertEquals(tmpArray[3].replace("Contact Phone Number:",""), contactNumber,"");		
		Assert.assertEquals(tmpArray[4].replace("Additional Instructions:",""), additionalInstructions,"");		
		Assert.assertEquals(tmpArray[5].replace("Service Number:",""), serviceNumber,"");	
		// preferred suspension date was done by caller.
		Assert.assertEquals(tmpArray[7].replace("Hold Service:",""),"In pool for Bob Limited"); // added 9/26/18
		Assert.assertEquals(tmpArray[9].replace("Tangoe Order ID:",""), orderDetailsObjectExpected.orderId,"");		
		Assert.assertEquals(tmpArray[10].replace("External Order Number:",""), orderDetailsObjectExpected.externalOrderId,"");
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
