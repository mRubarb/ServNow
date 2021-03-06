package ServiceNow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;

import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.CalendarDateTimeObject;
import HelperObjects.DeviceInfoActions;
import HelperObjects.Feature;
import HelperObjects.PlanInfoActions;
import HelperObjects.PlanOptionalFeatures;


public class OrderSubmittedPage extends BaseClass
{
	
	public static String tmpString = "";
	public static String [] tmpStringArray;
	public static String errMessage = "";
	
	
	public static void SelectViewOrder() // throws Exception
	{
		driver.findElement(By.xpath("//button[text()='View this Order']")).click();	
	}		
	
	public static void WaitForOrderDetailsPageToLoad() throws Exception
	{
		// wait for some information sections of page to load - not history section.
		WaitForElementVisible(By.xpath("//label[text()='Date Submitted']"), MainTimeout);	
		WaitForElementVisible(By.xpath("//label[text()='External Order #']"), MainTimeout);		
		WaitForElementPresent(By.cssSelector("div.sn-accordion__header"), MainTimeout);
		WaitForElementPresent(By.xpath(".//*[@id='panel-history']"), MainTimeout);
		// WaitForElementPresent(By.xpath("//td[text()='Entered Order Processing']"), MainTimeout); // this is too risky to wait for. timed out once. 		
	}
	

	// get some of the information "ABOVE" the Account Holder information. not all information is present when an order is first submitted.
	public static void VerifyTopSection() throws Exception 
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyTopSection";
		
		// this gets section with date submit, service #, external order Id.
		tmpString = driver.findElement(By.xpath("//label[text()='Date Submitted']/../..")).getText();
		tmpStringArray = tmpString.split(" ");

		// for(int x = 0; x < tmpStringArray.length; x++){System.out.println(x + " " + tmpStringArray[x]);} // DEBUG view all strings

		LocalDateTime ldt = LocalDateTime.now();
		
		for(int z = 0; z < tmpStringArray.length; z ++)
		{
			switch(z)
			{                      
				case 3: // day
				{
					Assert.assertEquals(tmpStringArray[z], Integer.toString(ldt.getDayOfMonth()), errorMessage);
					break;
				}
				case 4: // month
				{
					Assert.assertEquals(tmpStringArray[z], GetMonth(), errorMessage);					
					break;
				}
			}
		}

		// verify year
		Assert.assertEquals(driver.findElement(By.id("dateSubmitted")).getText().split(" ")[2].substring(0, 4), Integer.toString(ldt.getYear()) , errorMessage);
		
		// service number
		Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='serviceNumber']")).getText(), fullServiceNumber, "");
		
		// set external order id in order details expected.
		orderDetailsObjectExpected.externalOrderId = driver.findElement(By.id("externalOrderNumber")).getText().trim();
		
		//System.out.println("orderDetailsObjectExpected.externalOrderId: " + orderDetailsObjectExpected.externalOrderId);
		
	}

	// this verifies items in order details page that weren't verified before the command sync. this is before approval.
	public static void VerifyTopSectionActionsAfterCommandSync() throws Exception
	{
		// service number - this was checked already. putting in sanity test.
		Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='serviceNumber']")).getText(), fullServiceNumber, "");
		
		// order status 
		Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='dateSubmitted']/span")).getText(), orderDetailsObjectExpected.status.toUpperCase(), "");		
	
		// tangoe order status 
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Tangoe Order Status']/following ::span")).getText(), orderDetailTangoeOrderStatus, "");		
	}
	
	public static void VerifyAccountHolderInformation() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyTopSection";
		
		tmpString = driver.findElement(By.xpath("//h3[text()='Account Holder']/..")).getText();
		tmpStringArray = tmpString.split("\n");

		
		for(int X = 0; X < tmpStringArray.length; X ++)
		{
			switch(X)
			{
				case 1:
				{
					Assert.assertEquals(tmpStringArray[X] , userLimitedShorterName, errorMessage);					
					break;
				}
				case 2:
				{
					Assert.assertEquals(tmpStringArray[X] , userDepartmentName, errorMessage);					
					break;
				}				
			}
		}		
	}
	
	// //////////////////////////////////////////////////////////////////
	// get all text in section additional information and verify
	// //////////////////////////////////////////////////////////////////		
	public static void VerifyAdditionalInformation() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformation";

		//tmpString = driver.findElement(By.xpath("(//table/tbody)[1]")).getText();		

		//tmpStringArray = tmpString.split("\n");
		
		String[] additionalInformation = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");
		
		for (int i = 0; i < additionalInformation.length; i++)
		{
			if (additionalInformation[i].startsWith("Preferred Area Code")) {
				
				Assert.assertEquals(additionalInformation[i], "Preferred Area Code " + preferredAreaCode, errorMessage);
			
			} else if (additionalInformation[i].startsWith("Contact Phone Number")) {
				
				Assert.assertEquals(additionalInformation[i], "Contact Phone Number " + contactNumber, errorMessage);
			
			} else if (additionalInformation[i].startsWith("Additional Instructions")) {
				
				Assert.assertEquals(additionalInformation[i], "Additional Instructions " + additionalInstructions, errorMessage);
					
			} else if (additionalInformation[i].startsWith("Reason")) {
				
				// *** SFD 113227 entered for missing value *** <-- UNCOMMENT WHEN DEFECT IS FIXED 
				//Assert.assertEquals(additionalInformation[i], "Reason " + reasonAddDeviceAndService, errorMessage);
				
			} else if (additionalInformation[i].startsWith("Business Unit")) {
				
				Assert.assertEquals(additionalInformation[i], "Business Unit " + businessUnit, errorMessage);
				
			}					
			
		}
		
			
	}

	
	// //////////////////////////////////////////////////////////////////
	// verify additional information section for deactivate action.
	// //////////////////////////////////////////////////////////////////		
	public static void VerifyAdditionalInformationDeactivate() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformationDeactivate.";
		tmpStringArray = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");

		Assert.assertEquals(tmpStringArray[0].replace("Contact Phone Number ", ""), contactNumber, errorMessage);
		Assert.assertEquals(tmpStringArray[1].replace("Additional Instructions ",""), additionalInstructions, errorMessage);		
		Assert.assertEquals(tmpStringArray[2].replace("Service Number ",""), serviceNumber, errorMessage);		
		Assert.assertEquals(tmpStringArray[3].replace("Reason ",""), reasonAction, errorMessage);		
	}	

	// ///////////////////////////////////////////////////////////////////////
	// verify additional information section for upgrade device action.
	// ///////////////////////////////////////////////////////////////////////		
	public static void VerifyAdditionalInformationUpgradeDevice() 
	{
		VerifyAdditionalInformationOrderAccessories(); // use existing for most of the items.
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Reason']/../following ::td[1]")).getText(), DeviceInfoActions.reasonUpgradeAction,
				            "Failed to find correct reason in OrderSubmittedPage.VerifyAdditionalInformationUpgradeDevice");
	}
	
	// ///////////////////////////////////////////////////////////////////////
	// verify additional information section for order accessories action.
	// ///////////////////////////////////////////////////////////////////////		
	public static void VerifyAdditionalInformationOrderAccessories() 
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationOderAccessories.";		

		tmpStringArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		
		
		Assert.assertEquals(tmpStringArray[0].replace("Contact Phone Number ", ""), contactNumber, errMessage);
		Assert.assertEquals(tmpStringArray[1].replace("Ext ", ""), extension, errMessage);		
		Assert.assertEquals(tmpStringArray[2].replace("Additional Instructions ", ""), additionalInstructions, errMessage);
		Assert.assertEquals(tmpStringArray[3].replace("Service Number ", ""), serviceNumber, errMessage);
	}		
	
	// /////////////////////////////////////////////////////////////////////////////
	// verify additional information section for suspend  and un-suspend actions.
	// ////////////////////////////////////////////////////////////////////////////		
	public static void VerifyAdditionalInformationBothSuspends() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformationDeactivate.";
		tmpStringArray = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");

		Assert.assertEquals(tmpStringArray[0].replace("Contact Phone Number ", ""), contactNumber, errorMessage);
		Assert.assertEquals(tmpStringArray[1].replace("Additional Instructions ",""), additionalInstructions, errorMessage);		
		Assert.assertEquals(tmpStringArray[2].replace("Service Number ",""), serviceNumber, errorMessage);	
		Assert.assertTrue(CalendarDateTimeObject.VerifyMonthAndYear(tmpStringArray[3]), errorMessage);		
	}		

	public static void VerifyAdditionalInformationPortNumber() 
	{
	
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformationPortNumber.";
		tmpStringArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");
		//ShowArray(tmpStringArray);
		
		Assert.assertEquals(tmpStringArray[0].replace("Carrier Account Number ",""), PlanInfoActions.carrierAccountNumber, errorMessage);		
		Assert.assertEquals(tmpStringArray[1].replace("Name on Invoice ",""), userLimitedShorterName, errorMessage);		
		Assert.assertEquals(tmpStringArray[2].replace("Contact Phone Number ",""), contactNumber, errorMessage);		
		Assert.assertEquals(tmpStringArray[3].replace("Ext ",""), extension, errorMessage);		
		Assert.assertEquals(tmpStringArray[4].replace("Additional Instructions ",""), additionalInstructions, errorMessage);		
		Assert.assertEquals(tmpStringArray[5].replace("Current Carrier ",""), DeviceInfoActions.currentVendorPortNumber, errorMessage);		
		Assert.assertEquals(tmpStringArray[6].replace("Service Number ",""), serviceNumber, errorMessage);		
	}
	
	public static void VerifyAdditionalInformationSwapDevice() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformationSwapDevice.";
		
		String[] additionalInformation = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");
			
		
		for (int i = 0; i < additionalInformation.length; i++) {
			
			String label = additionalInformation[i].split("\\:")[0];
			System.out.println("Label: " + label);
			
			switch (label) {
			
				case "Current Device Make ": 
					
					Assert.assertEquals(additionalInformation[i], "Current Device Make " + IdentifyDevices.oldManufacturer, errorMessage);
					break;
					
				case "New Device Make ":
					
					Assert.assertEquals(additionalInformation[i], "New Device Make " + IdentifyDevices.newManufacturer, errorMessage);
					break;
				
				case "Current Device Model ":
					
					Assert.assertEquals(additionalInformation[i], "Current Device Model " + IdentifyDevices.oldModel, errorMessage);
					break;
				
				case "New Device Model ":
					
					Assert.assertEquals(additionalInformation[i], "New Device Model " + IdentifyDevices.newModel, errorMessage);
					break;
				
				case "Current Device Serial Number ":
					
					Assert.assertEquals(additionalInformation[i], "Current Device Serial Number " + IdentifyDevices.oldSerialNumber, errorMessage);
					break;
				
				case "Current Device Serial Number Type ":
					
					Assert.assertEquals(additionalInformation[i], "Current Device Serial Number Type " + IdentifyDevices.oldSerialNumberType, errorMessage);
					break;
				
				case "New Device Serial Number ":
					
					Assert.assertEquals(additionalInformation[i], "New Device Serial Number " + IdentifyDevices.newSerialNumber, errorMessage);
					break;
				
				case "New Device Serial Number Type ":
					
					Assert.assertEquals(additionalInformation[i], "New Device Serial Number Type " + IdentifyDevices.newSerialNumberType, errorMessage);
					break;
				
				case "SIM ID / ICCID":
					
					Assert.assertEquals(additionalInformation[i], "SIM ID / ICCID " + IdentifyDevices.simId, errorMessage);
					break;
				
				case "Contact Phone Number":
					
					Assert.assertEquals(additionalInformation[i], "Contact Phone Number " + contactNumber, errorMessage);
					break;
				
				case "Ext":
					
					Assert.assertEquals(additionalInformation[i], "Ext " + extension, errorMessage);
					break;
				
				case "Additional Instructions":
					
					Assert.assertEquals(additionalInformation[i], "Additional Instructions " + additionalInstructions, errorMessage);
					break;
						
				case "Service Number ":
					
					Assert.assertEquals(additionalInformation[i], "Service Number " + serviceNumber, errorMessage);
					break;
				
				case "Preferred Area Code":
					
					Assert.assertEquals(additionalInformation[i], "Preferred Area Code " + preferredAreaCode, errorMessage);
					break;
						
			}
					
		}
		
	
	}		
	
	public static void VerifyApprovals()  
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyApprovals";
		
		//Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[5]")).getText(), approverAna, errorMessage);
		//Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[7]")).getText(), approverAdmin, errorMessage);
		//Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[8]")).getText(), approverAdminMail, errorMessage);		
		//Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[9]")).getText(), approverBob, errorMessage);
		
		
		// If the approval section is displayed 
		// The approval section may or may not be displayed. It depends on the configuration of the vendor in CMD
		
		boolean approvalSectionDisplayed = true;
		
		try {
			
			driver.findElement(By.xpath("//*[@id='panel-approvals']"));
			
		} catch (Exception e) {
			
			approvalSectionDisplayed = false;
			System.out.println("Approval section is not displayed.");
		}
		
		if (approvalSectionDisplayed) {
			
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='panel-approvals']/div/div[2]/div/table/tbody/tr[1]/td[2]/span/div[1]")).getText(), approverAdmin, errorMessage);
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='panel-approvals']/div/div[2]/div/table/tbody/tr[2]/td[2]/span/div[1]")).getText(), userApproverShorterName, errorMessage);
			
		}
		
	}	
	
	
	
	public static void VerifyShippingInformationOrderAccessoriesPreApproval() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyShippingInformationOrderAccessoriesPreApproval";

		String [] strArray = driver.findElement(By.xpath("(//tbody)[3]")).getText().split("\n");		
		//		
		Assert.assertEquals(strArray[0].replace("Line 1 ", ""), addressLineOneOrderActions  , errorMessage); // verify full address
		Assert.assertEquals(strArray[1].replace("City ", ""), cityOrderActions  , errorMessage); // verify city
		Assert.assertEquals(strArray[2].replace("State ", ""), stateOrderActions  , errorMessage); // verify state
		Assert.assertEquals(strArray[3].replace("Postal Code ", ""), zipCodeOrderActions  , errorMessage); // verify postal code
	}	
	
	// this is for shipping information after approval.
	public static void VerifyShippingInformationOrderAccessoriesPostApproval() throws Exception
	{
		String errMessage = "Wrong information found in test for shipping information in OrderSubmittedPage.VerifyShippingInformationOrderAccessoriesPostApproval.";
		
		String [] strArray = driver.findElement(By.xpath("//div[text()='Shipping Information']/following ::table[1]")).getText().split("\n");
		
		Assert.assertEquals(strArray[0].replace("Line 1 ", ""), addressLineOne, errMessage);
		Assert.assertEquals(strArray[1].replace("City ","" ), cityOrderActions, errMessage);
		Assert.assertEquals(strArray[2].replace("State ", ""), stateOrderActions, errMessage);
		Assert.assertEquals(strArray[3].replace("Postal Code ", ""), zipCodeOrderActions, errMessage);		
	}
	
	public static void VerifyShippingInformation()
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyShippingInformation";
		
		// verify full address
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Line 1']/../following ::td[1]")).getText(), addressLineOne, errorMessage);

		// verify city
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='City']/../following ::td[1]")).getText(), userCity, errorMessage);

		// verify state
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='State']/../following ::td[1]")).getText(), userStateOrderDetails, errorMessage);

		// verify postal code
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Postal Code']/../following::td")).getText(), userPostalCode, errorMessage);
	}	
	
	// this section has accessory information for element at bottom of accessory list.
	// this accessory was picked when device/plan were being added.
	public static void VerifyFirstOrderSegment()
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyFirstOrderSegment";
		
		// first item
		Assert.assertEquals(driver.findElement(By.xpath("//div/h3[text()='Accessories']/../following ::div[1]/span")).getText(), 
							accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).name, errorMessage);   

		// second item
		Assert.assertEquals(driver.findElement(By.xpath("//div/h3[text()='Accessories']/../following ::div[2]/span")).getText(), 
				accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).GetManufacturer(), errorMessage);
		
		// third item
		Assert.assertEquals(driver.findElement(By.xpath("//div/h3[text()='Accessories']/../following ::div[3]/span")).getText(), 
				accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).cost, errorMessage);
	}

	// this section tests device, plan, and accessory items.   
	// this information was picked when device/plan/accessories were being added.
	// NOTE: IDs didn't work in xpath or CSS. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public static void VerifySecondOrderSegment()
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifySecondOrderSegment";
		
		// this get device block
		//h3[text()='Device']/../..
		
		// device vendor
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText(), 
				deviceInfoActions.vendor, errorMessage);   

		// device name		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../../div[2]/span")).getText(), 
				deviceInfoActions.name, errorMessage);   

		// device manufacturer		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../../div[3]/span")).getText(), 
				deviceInfoActions.GetDeviceManufacturer(), errorMessage);   

		// device price		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../../div[4]/span")).getText(), 
				deviceInfoActions.cost, errorMessage);
		
		// plan name
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[1]/span")).getText(), 
				planInfoActions.planSelectedName, errorMessage);

		// plan price
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[2]/span")).getText(), 
				planInfoActions.SetupCostForOrderDetails(), errorMessage);

		// optional features
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Optional Features']/following ::span/div[1]")).getText().toLowerCase(), 
				optionalFeaturesList.get(optionalFeaturesList.size() - 1).CompleteNameForOrderDetails(), errorMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Optional Features']/following ::span/div[2]")).getText().toLowerCase(), 
				optionalFeaturesList.get(0).CompleteNameForOrderDetails(), errorMessage);
	}	
	
	// this section tests device, plan, and accessory items.   
	// this information was picked when device/plan/accessories were being added.
	// NOTE: IDs didn't work in xpath or CSS. !!!
	public static void VerifySecondAccessories()
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifySecondAccessories";
		
		// accessory name
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[text()='Accessories']/../following ::div[1]/span)[2]")).getText(), 
				accessoriesDetailsListExpected.get(0).name, errorMessage);   

		// accessory manufacturer
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[text()='Accessories']/../following ::div[2]/span)[2]")).getText(), 
				accessoriesDetailsListExpected.get(0).GetManufacturer(), errorMessage);
		
		// accessory price
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[text()='Accessories']/../following ::div[3]/span)[2]")).getText(), 
				accessoriesDetailsListExpected.get(0).cost, errorMessage);
	}
	
	// order status takes time to sync with command sometimes. this provides a quick check for the order status only.
	public static void VerifyOrderStatus()
	{
		Assert.assertEquals(driver.findElement(By.xpath("//span[@id='dateSubmitted']/span")).getText(),orderDetailsObjectExpected.status.toUpperCase(),
							"Failed order status in OrderSubmittedPage.VerifyOrderStatus.");
	}
	
	//  
	public static void VerifyUpdateFeatures() throws Exception
	{
		if(driver.findElements(By.xpath("//div[contains(text(),'Order Segment')]")).size() != 1) // this means both accessories are in one block of information.
		{
			Assert.fail("failure in OrderSubmittedPage.VerifyUpdateFeatures(). NEED TO FIX CODE FOR MORE THAN ONE ORDER SEGMENT.");
		}

		Feature featureOne =  new Feature(driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[2]/span")).getText(),
										  driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[4]/span")).getText().replace("monthly", "(Monthly)"));

		featureOne.action = driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[3]/span")).getText() + ":";
		

		Feature featureTwo =  new Feature(driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[6]/span")).getText(),
				  			  driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[8]/span")).getText().replace("monthly", "(Monthly)"));

		featureTwo.action = driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[7]/span")).getText() + ":";

		// NOTE: HACK here for DE# 108276
		// this method: SetActions(BaseClass.UpdateFeatureActions actionType) MAY!! need to have some additions. I don't think so though. 
		Feature featureThree =  new Feature(driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[10]/span")).getText(),
				  			         	    driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[12]/span")).getText().replace("monthly", "(Monthly)").replace("$-","$"));

		featureThree.action = driver.findElement(By.xpath("(//h3[text()='Features']/../../div)[11]/span")).getText() + ":";
		
		Feature.VerifyUpdateFeatures(featureOne, featureTwo, featureThree);
	}
	
	
	// the two selected accessories can be in can be one block of information or two blocks of information in the order details page. it is not guaranteed they are in 
	// the same order as they are in the accessoriesDetailsListExpected list.   
	public static void VerifyOrderSegmentAccessoriesOrderAction() throws Exception
	{
		String errMessage = "Failed verification of Accessories in OrderSubmittedPage.VerifyOrderSegmentAccessoriesOrderAction";
	
		// declare objects to be instantiated.
		AccessoriesDetailsExpected accessoryOne = new AccessoriesDetailsExpected();
		AccessoriesDetailsExpected accessoryTwo = new AccessoriesDetailsExpected();;		
		
		 
		//if(driver.findElements(By.xpath("//div[contains(text(),'Order Segment')]")).size() == 1) // this means both accessories are in one block of information.
		// if statement on line above commented by Ana, and replaced by:
		// If there are 2 accessories listed 
		if(driver.findElements(By.xpath("//div/h3[text()='Accessories']/../following-sibling::div/label[text()='Name']")).size() == 2)
		{
			// get the first accessory into AccessoriesDetailsExpected object. cost field is left empty.
			accessoryOne = new AccessoriesDetailsExpected(
					driver.findElement(By.xpath("(//label[text()='Name'])[1]/following ::span[1]")).getText(), 
					"", 
					driver.findElement(By.xpath("(//label[text()='Price'])[1]/following ::span[1]")).getText());
			
			// setup manufacturer. it's not in the constructor.
			accessoryOne.manufacturer = driver.findElement(By.xpath("(//label[text()='Manufacturer'])[1]/following ::span[1]")).getText();
			
			// get the second accessory into AccessoriesDetailsExpected object. cost field is left empty.
			accessoryTwo = new AccessoriesDetailsExpected(
					driver.findElement(By.xpath("(//label[text()='Name'])[2]/following ::span[1]")).getText(), 
					"",
					driver.findElement(By.xpath("(//label[text()='Price'])[2]/following ::span[1]")).getText());
			
			// setup manufacturer. it's not in the constructor.
			accessoryTwo.manufacturer = driver.findElement(By.xpath("(//label[text()='Manufacturer'])[2]/following ::span[1]")).getText();
			
			
			// this verifies the accessory section.
			AccessoriesDetailsExpected.verifyOrderAccessories(accessoryOne, accessoryTwo);
		
			List<AccessoriesDetailsExpected> listAccessoriesDetails = new ArrayList<>();
			listAccessoriesDetails.add(accessoryOne);
			listAccessoriesDetails.add(accessoryTwo);
			// this verifies the manufacturers. 			
			AccessoriesDetailsExpected.verifyOrderAccessoriesManufacturers(listAccessoriesDetails);
			
		}
		else if(driver.findElements(By.xpath("//div/h3[text()='Accessories']/../following-sibling::div/label[text()='Name']")).size() == 1)// this means there is one accessory in one block of information with a total of two.
		{
			// get the first accessory into AccessoriesDetailsExpected object. cost field is left empty.
			accessoryOne = new AccessoriesDetailsExpected(
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[1]/../following ::div[1]")).getText().replace("Name", ""), 
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[1]/../following ::div[2]")).getText().replace("Manufacturer", ""), 
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[1]/../following ::div[3]")).getText().replace("Price", ""));

			// setup manufacturer. it's not in the constructor.
			accessoryOne.manufacturer = driver.findElement(By.xpath("(//h3[text()='Accessories'])[1]/../following ::div[2]")).getText().replace("Manufacturer", "");
			
			// get the second accessory into AccessoriesDetailsExpected object. cost field is left empty.
			/*accessoryTwo = new AccessoriesDetailsExpected(
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[2]/../following ::div[1]")).getText().replace("Name", ""), 
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[2]/../following ::div[2]")).getText().replace("Manufacturer", ""), 
					driver.findElement(By.xpath("(//h3[text()='Accessories'])[2]/../following ::div[3]")).getText().replace("Price", ""));

			// setup manufacturer. it's not in the constructor.
			accessoryTwo.manufacturer = driver.findElement(By.xpath("(//h3[text()='Accessories'])[2]/../following ::div[2]")).getText().replace("Manufacturer", "");			
			*/
			// this verifies the accessory section.
			AccessoriesDetailsExpected.verifyOrderAccessories(accessoryOne);
			
			// this verifies the manufacturers. 			
			List<AccessoriesDetailsExpected> listAccessoriesDetails = new ArrayList<>();
			listAccessoriesDetails.add(accessoryOne);
			
			AccessoriesDetailsExpected.verifyOrderAccessoriesManufacturers(listAccessoriesDetails);
		}
	}
	
	
	// this is status and vendor. the status text is only part of the complete expected status. 
	public static void verifyStatusAndVendor()
	{
		errMessage = "Failed verification in OrderSubmittedPage.VerifyStatusAndVendor."; 
		
		// setup the expected status string
		String statusText = orderDetailsObjectExpected.status.split(" ")[0] + " " + orderDetailsObjectExpected.status.split(" ")[1];
		
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Status']/following ::span[1]")).getText(), statusText, errMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText(), deviceInfoActions.vendor, errMessage);
	}
	
	// this is for checking these items when they are contained in an order segment. order segments can vary in size so text names are used here.
	// the device info was setup when the device was selected as the upgrade device.
	public static void VerifyDeviceSectionUpgradeDevice()
	{
		errMessage = "Failed verification in OrderSubmittedPage.VerifyDeviceSectionUpgradeDevice."; 
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../following ::div[1]/span")).getText(), deviceInfoActions.name, errMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../following ::div[2]/span")).getText(), deviceInfoActions.GetDeviceManufacturer(), errMessage);		
		// Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../following ::div[3]/span")).getText(), deviceInfoActions.cost, errMessage);
		// <-- UNCOMMENT ABOVE WHEN SFD112988 IS FIXED
		// *************************************		
	}
	
	// this is for checking these items when they are contained in an order segment. order segments can vary in size so text names are used here.
	// the plan info was setup when the plan was selected with the upgrade device.	
	public static void VerifyPlanSectionUpgradeDevice() throws Exception
	{
		errMessage = "Failed verification in OrderSubmittedPage.VerifyDeviceSectionUpgradeDevice."; 
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[1]/span")).getText(), planInfoActions.planSelectedName, errMessage);		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[2]/span")).getText(), planInfoActions.SetupCostForOrderDetails(), errMessage);
		
		PlanOptionalFeatures.VerifyPlanOptionsFirstLast(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[3]/span/div[1]")).getText(),
														driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[3]/span/div[2]")).getText());
	}	
	
	public static void VerifyAccessorySectionUpgradeDevice() throws Exception
	{
		errMessage = "Failed verification in OrderSubmittedPage.VerifyAccessorySectionUpgradeDevice."; 
		int lastAccessory = accessoriesDetailsListExpected.size() - 1;
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Accessories']/../following ::div[1]/span")).getText(), // name 
							accessoriesDetailsListExpected.get(lastAccessory).name, errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Accessories']/../following ::div[2]/span")).getText(), // manufacturer
				accessoriesDetailsListExpected.get(lastAccessory).GetManufacturer(), errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Accessories']/../following ::div[3]/span")).getText(), // price
				accessoriesDetailsListExpected.get(lastAccessory).cost, errMessage);
	}
	
	// ////////////////////////////////////////////
	// helper methods
	// ////////////////////////////////////////////
	public static int FindIndex(String cost)
	{
		for(int x = 0; x < accessoriesDetailsListExpected.size(); x++)
		{
			if(cost.equals(accessoriesDetailsListExpected.get(x).cost))
			{
				return x;
			}
		}
		return 0;
	}
	
		
	
	static public String [] GetTopSectionItems()
	{
		String fullTitle = driver.findElement(By.cssSelector(".sn-section-heading.ng-binding")).getText();
		String [] strArray = fullTitle.split(" ");
		return new String [] {strArray[0].replace("#","").replace(":",""),strArray[1] + " " + strArray[2]};
	}

	
	public static void verifyOrderSegments() {
		
		verifyOrderSegmentGeneral();
		verifyOrderSegmentDevice();
		verifyOrderSegmentPlan();
		verifyOrderSegmentAccessories();
		
	}

	
	public static void verifyOrderSegmentGeneral() {
		
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.verifyOrderSegmentGeneral";

		// Verify vendor's name
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText(), deviceInfoActions.vendor, errorMessage);   
		System.out.println("Vendor: " + driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText());
		
	}
	
	public static void verifyOrderSegmentDevice() {
		
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.verifyOrderSegmentDevice";
		
		// Verify device's name		
		String deviceNameFound = driver.findElement(By.xpath("//h3[text()='Device']/../../div[2]/span")).getText();
		Assert.assertEquals(deviceNameFound, deviceInfoActions.name, errorMessage);   
		
		// Verify device's manufacturer		
		//Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../../div[3]/span")).getText(), deviceInfoActions.GetDeviceManufacturer(), errorMessage);   
		String manufacturerFound = driver.findElement(By.xpath("//h3[text()='Device']/../../div[3]/span")).getText();
		
		Assert.assertTrue(deviceNameFound.startsWith(manufacturerFound), errorMessage);
		
		// Verify device's price	// -->  **** COMMENTED UNTIL SFD112988 IS FIXED ****	
		//Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Device']/../../div[4]/span")).getText(), deviceInfoActions.cost, errorMessage);
		
		System.out.println("Device name: " + driver.findElement(By.xpath("//h3[text()='Device']/../../div[2]/span")).getText());
		System.out.println("Manufacturer name: " + driver.findElement(By.xpath("//h3[text()='Device']/../../div[3]/span")).getText());
		System.out.println("Price: " + driver.findElement(By.xpath("//h3[text()='Device']/../../div[4]/span")).getText());
		
	}

	public static void verifyOrderSegmentPlan() {

		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.verifyOrderSegmentPlan";
		
		// Verify plan's name
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[1]/span")).getText(), planInfoActions.planSelectedName, errorMessage);

		// Verify plan's price
		Assert.assertEquals(driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[2]/span")).getText(), planInfoActions.SetupCostForOrderDetails(), errorMessage);

		System.out.println("Plan name: " + driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[1]/span")).getText());
		System.out.println("Price: " + driver.findElement(By.xpath("//h3[text()='Plan']/../following ::div[2]/span")).getText());
		
	}

	private static void verifyOrderSegmentAccessories() {
		
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.verifyOrderSegmentAccessories";
		
		// System.out.println("Order segment line: " + driver.findElement(By.cssSelector("#panel-segments>div>div")).getText());
		// E.g.: Order Segment #11569545
		//       ?
		String orderSegmentNumber = driver.findElement(By.cssSelector("#panel-segments>div>div")).getText().split("\\#")[1].replaceAll("[^0-9]", "");
		
		// System.out.println("orderSegmentNumber: " + orderSegmentNumber);
		
		for (int i = 0; i < accessoriesInCartHashMap.size(); i++) {
			
			// #seg-accessoryName-1-11569545
			// #seg-accessory-manufacturer-1-11569545
			// #seg-accessory-price-1-11569545
			
			// Accessory's name
			String accessoryNameFound = driver.findElement(By.id("seg-accessoryName-" + i + "-" + orderSegmentNumber)).getText();
			String accessoryNameExpected = accessoriesInCartHashMap.get(accessoryNameFound).name; 
			Assert.assertEquals(accessoryNameFound, accessoryNameExpected, errorMessage);   
	
			// Accessory's manufacturer
			String accessoryMnfFound = driver.findElement(By.id("seg-accessory-manufacturer-" + i + "-" + orderSegmentNumber)).getText();
			//String accessoryMnfExpected = accessoriesInCartHashMap.get(accessoryNameFound).GetManufacturer(); 
			Assert.assertTrue(accessoryNameFound.startsWith(accessoryMnfFound), errorMessage);
				
			// Accessory's price
			String accessoryPriceFound = driver.findElement(By.id("seg-accessory-price-" + i + "-" + orderSegmentNumber)).getText();
			String accessoryPriceExpected = accessoriesInCartHashMap.get(accessoryNameFound).cost; 
			Assert.assertEquals(accessoryPriceFound, accessoryPriceExpected, errorMessage);
			
			//System.out.println("Accessory name: " + accessoryNameFound);
			//System.out.println("Accessory manufacturer: " + accessoryMnfFound); 
			//System.out.println("Accessory price: " + accessoryPriceFound); 
			
		}
		
	}
	
	

}
