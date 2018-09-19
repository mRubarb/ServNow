package ServiceNow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ActionClasses.UpgradeDevice;
import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.CalendarDateTimeObject;
import HelperObjects.DeviceInfoActions;
import HelperObjects.DevicePortNumber;
import HelperObjects.Feature;
import HelperObjects.FeatureShoppingCart;
import HelperObjects.PlanInfoActions;
import HelperObjects.ShoppingCart;


public class VerifyOrderPage extends BaseClass
{
	public static WebElement element = null;
	
	public static String [] strArray;
	public static String strTemp = "";

	public static String errMessage = "";
	
	
	// ** NOT USED **
	public static void VerifyDeactivateTopSection() throws Exception
	{
		String orderNumber = driver.findElement(By.xpath("//h1[contains(text(),'Order')]")).getText().split(":")[0].replace("#","");
		Assert.assertEquals(driver.findElement(By.xpath("//h1[contains(text(),'Order')]")).getText().split(":")[0].replace("#",""), "","");
	}
	
	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[1]"), ExtremeTimeout, "Page open for VerifyOrderPage.WaitForPageToLoad() failed looking for button.");		
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[2]"), MainTimeout, "Page open for VerifyOrderPage.WaitForPageToLoad() failed looking for button.");		
	}
	
	// ** NOT USED **
	public static void WaitForPageToLoadDeactivate() throws Exception
	{
		WaitForElementVisible(By.xpath("(//button[text()='Submit Order'])[1]"), MainTimeout);
		WaitForElementVisible(By.xpath("(//button[text()='Submit Order'])[2]"), MainTimeout);
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[1]"), MainTimeout, "Page open for VerifyOrderPage.WaitForPageToLoad() failed looking for button.");		
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[2]"), MainTimeout, "Page open for VerifyOrderPage.WaitForPageToLoad() failed looking for button.");		
	}	
	
	public static void clickSubmitBtn()
	{
		driver.findElement(By.xpath("(//button[text()='Submit Order'])[1]")).click();
	}
	
	public static void ClickBackButton() 
	{
		WaitForElementClickable(By.xpath("//button[text()='Back']"), MediumTimeout, "");
		driver.findElement(By.xpath("//button[text()='Back']")).click();
	}
	
	public static void WaitForOrderComplete() throws Exception
	{
		WaitForElementVisible(By.xpath("//span[text()='Your order has been submitted.']"),	ExtremeTimeout);	
	}
	
	// there are three added update features. indexes selectionOne, selectionTwo, and selectionThree from the 'featuresList' have been selected.
	public static void VerifyFeatures() throws Exception
	{
		String errMessage = "";
		String costText = "Cost: "; //"Price: "; <-- changed by Ana - 9/7/17
		String costSavingText = "Cost Saving: ";
		
		// feature one.
		strArray = driver.findElement(By.cssSelector("#verify_features:nth-child(1) > div:nth-of-type(2)")).getText().split("\n");
		System.out.println("strArray: " + strArray[0] + "  " + strArray[1]);
		Assert.assertEquals(strArray[0], featuresList.get(Feature.selectionOne).action + " " + featuresList.get(Feature.selectionOne).fullName, "");
		Assert.assertEquals(strArray[1], costText + featuresList.get(Feature.selectionOne).monthlyTag + " " + featuresList.get(Feature.selectionOne).dollarCost,"");		
		
		// feature two.
		strArray = driver.findElement(By.cssSelector("#verify_features:nth-child(1) > div:nth-of-type(3)")).getText().split("\n");
		System.out.println("strArray: " + strArray[0] + "  " + strArray[1]);
		Assert.assertEquals(strArray[0], featuresList.get(Feature.selectionTwo).action + " " + featuresList.get(Feature.selectionTwo).fullName, "");
		Assert.assertEquals(strArray[1], costSavingText + featuresList.get(Feature.selectionTwo).monthlyTag + " " + featuresList.get(Feature.selectionTwo).dollarCost.replace("-", ""),"");		
		
		// feature three.
		// NOTE: This has no dollar value displayed so the last assert is different than others above - DE 108239. 
		strArray = driver.findElement(By.cssSelector("#verify_features:nth-child(1) > div:nth-of-type(4)")).getText().split("\n");
		System.out.println("strArray: " + strArray[0] + "  " + strArray[1]);
		Assert.assertEquals(strArray[0], featuresList.get(Feature.selectionThree).action + " " + featuresList.get(Feature.selectionThree).fullName, "");
		//Assert.assertEquals(strArray[1].trim(), costText + featuresList.get(Feature.selectionThree).monthlyTag,"");
		Assert.assertEquals(strArray[1], costText + featuresList.get(Feature.selectionThree).monthlyTag + " " + featuresList.get(Feature.selectionThree).dollarCost,"");
	} 
	
	// there are three added update features. indexes selectionOne, selectionTwo, and selectionThree from the 'featuresList' have been selected.
	public static void VerifyFeaturesCost() 
	{
		errMessage = "Fail when checking 'Cost Monthly' in VeifyOrderPage.VerifyFeaturesCost"; 
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]/span")).getText(), FeatureShoppingCart.costMonthly, errMessage);
	}
	
	// verify the items on the Order New Service page and 
	public static void VerifyOrderSubmittedInfo() throws Exception
	{
		String tempStr = "";
		String [] tempStrArray;
		
		WaitForElementVisible(By.xpath("//span[text()='Your order has been submitted.']"), ShortTimeout);	
		
		String errorMessage = "Failed verification of text messages in Order Submitted page in VerifyOrderPage.VerifyOrderSubmittedInfo";
		
		// verify main heading
		Assert.assertEquals(driver.findElement(By.cssSelector(".tg-infoBar.tg-pad--half")).getText(), "Order submitted.", errorMessage);
		
		// verify message below main heading.
		Assert.assertEquals(driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText(), 
							"Your order request has been submitted for processing.", errorMessage);
		
		// get last batch of strings shown on the page.
		tempStr = driver.findElement(By.cssSelector(".sn-wizard-content__steps")).getText();
		tempStrArray = tempStr.split("\n");
		
		// verify the message above the order number section.		
		Assert.assertEquals(tempStrArray[0], "Your order has been submitted.   Please take note of your order number for future reference.", errorMessage);
		
		orderDetailsObjectExpected.orderId = driver.findElement(By.xpath("//p[contains(text(),'Order #')]")).getText().split(" ")[1].replace("#", "");
	}	
	
	public static void SelectViewOrder() throws Exception
	{
		driver.findElement(By.xpath("//button[text()='View this Order']")).click();	
	}
	
	//Submit Order Button
	public static WebElement submitBtn()
	{
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[10]/div/button[3]"));
		return element;
	}
	
	
	// this verifies the device name, vendor, and price. below is an example of what the xpath returns into a three element array.
	// 1) "Apple iPhone 5 (16GB) - Black"
	// 2) "Vendor Verizon Wireless"
	// 3) "Price $199.99"
	public static void VerifySelectedDeviceDetails()  // Asserts and xpaths modified - 9/11/17 - working fine
	{
		String errorMessage = "Wrong information found in test for device details in VerifyOrderPge.VerifySelectedDeviceDetails.";		
		//String tempString;
		
		String deviceName = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/div")).getText();
		Assert.assertEquals(deviceName, deviceInfoActions.name, errorMessage);
		
		String vendor = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/table/tbody/tr[1]/td[2]/span")).getText();
		Assert.assertEquals(vendor, deviceInfoActions.vendor, errorMessage); 	
		
		String price = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/table/tbody/tr[2]/td[2]/span")).getText();
		Assert.assertEquals(price, deviceInfoActions.cost, errorMessage);
		
	}

	
	public static void verifySelectedDeviceDetailsPortNumber()  // Added by Ana - 9/15/17
	{
		String errorMessage = "Wrong information found in test for device details in VerifyOrderPage.verifySelectedDeviceDetailsPortNumber.";		
				
		String deviceName = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/div")).getText();
		Assert.assertEquals(deviceName, DevicePortNumber.selectedDeviceName, errorMessage);
		
		String vendor = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/table/tbody/tr[1]/td[2]/span")).getText();
		Assert.assertEquals(vendor, DevicePortNumber.selectedDeviceVendor, errorMessage); 	
		
		String price = driver.findElement(By.xpath("//div[@id='verify_device']/div[3]/table/tbody/tr[2]/td[2]/span")).getText();
		//Assert.assertEquals(price, DevicePortNumber.selectedDevicePrice, errorMessage);// <-- UNCOMMENT WHEN SFD112988 IS FIXED
		// *************************************
	
	}
	
	// verify all existing device items.
	public static void VerifySelectedDeviceDetailsUpgradeDevice() throws Exception  
	{
		String errMessage = "Wrong information found in test for device info  in VerifyOrderPge.VerifySelectedDeviceDetailsUpgradeDevice.";		
		
		strArray = driver.findElement(By.xpath("//div[text()='Device']/following ::div[2]")).getText().split("\n");
		// for(int x = 0; x < strArray.length; x++){System.out.println(x + " " + strArray[x]);} // DEBUG

		Assert.assertEquals(strArray[0], deviceInfoActions.name, errMessage);		
		Assert.assertEquals(strArray[1].replace("Vendor ", ""), deviceInfoActions.vendor, errMessage);		
		//Assert.assertEquals(strArray[2].replace("Price ", ""), deviceInfoActions.cost, errMessage); // <-- UNCOMMENT WHEN SFD112988 IS FIXED
																										// *************************************
	}
	
	
	// verify all existing device items.
	public static void VerifyPlanInfoUpgradeDevice() throws Exception  
	{
		errMessage = "Wrong information found in test for plan info in VerifyOrderPge.VerifyPlanInfoUpgradeDevice.";		
				
		strArray = driver.findElement(By.xpath("//div[text()='Plan']/following ::div[2]")).getText().split("\n");
		// for(int x = 0; x < strArray.length; x++){System.out.println(x + " " + strArray[x]);} // DEBUG
		
		Assert.assertEquals(strArray[0], planInfoActions.planSelectedName, errMessage);		
		Assert.assertEquals(strArray[1].replace("Price ", ""), planInfoActions.PlanTextCost(), errMessage);
		Assert.assertEquals(strArray[2], PlanInfoActions.includedFeaturesTitle , errMessage);
		Assert.assertEquals(strArray[3], PlanInfoActions.includedFeatures.get(0) , errMessage);		
		Assert.assertEquals(strArray[4], PlanInfoActions.includedFeatures.get(1), errMessage);
		Assert.assertEquals(strArray[5], PlanInfoActions.includedFeatures.get(2), errMessage);
		
		//Assert.assertEquals(strArray[6], PlanInfoActions.optionalFeaturesTitle, errMessage);
		
		// Ana add - line above replaced by: 
		Assert.assertTrue(driver.findElement(By.xpath("//td/span[text()='Optional Features']/../following-sibling::td/span/ul/li")).isDisplayed(), errMessage);
		
		// plan options
		//Assert.assertEquals(strArray[7], optionalFeaturesList.get(0).completeName, errMessage);
		//Assert.assertEquals(strArray[8], optionalFeaturesList.get(optionalFeaturesList.size() - 1).completeName, errMessage);		
		
		// Ana add - two lines above replaced by:
		
		List<WebElement> optionalFeaturesValues = driver.findElements(By.xpath("//td/span[text()='Optional Features']/../following-sibling::td/span/ul/li"));
		
		System.out.println("Optional Features:");
		
		for (int i = 0; i < optionalFeaturesValues.size(); i++){
			
			String feature = optionalFeaturesValues.get(i).getText().trim();
			System.out.println(feature);
			Assert.assertEquals(feature, selectedOptionalFeatures.get(i), errMessage);
			
		}
		
		
		
		
		
		/*
		0 Nationwide for Business Talk Share Plan
		1 Price $58.67
		2 Included Features
		3 Total Equipment Coverage
		4 VZ Navigator
		5 Base Package
		6 Optional Features
		7 Voice Mail - $300.00 (Monthly)
		8 1000 Messages - $10.00 (Monthly)
		*/
		
		/*
		----------------------
		Name:Voice Mail
		Cost:$300.00
		Billing Interval:(Monthly)
		----------------------
		Name:Roadside Assistance
		Cost:$200.00
		Billing Interval:(Monthly)
		----------------------
		Name:Total Equipment Coverage (Advanced Devices)
		Cost:$100.00
		Billing Interval:(Monthly)
		----------------------
		Name:Visual Voice Mail
		Cost:$2.99
		Billing Interval:(Monthly)
		----------------------
		Name:Nationwide for Business Data Share 5GB
		Cost:$43.00
		Billing Interval:(Monthly)
		----------------------
		Name:1000 Messages
		Cost:$10.00
		Billing Interval:(Monthly)
		Freeze
		 */
	
	}	
	
	
	// verify all existing and new device items.
	public static void VerifySwapDeviceExistingAndNewDevices() throws Exception  
	{
		String errMessage = "Wrong information found in test for existing and new devices in VerifyOrderPge.VerifySwapDeviceExistingAndNewDevices.";		
		
		String[] existingDeviceInfo = getExistingDeviceSwapDevice();
		
		Assert.assertEquals(existingDeviceInfo[0], IdentifyDevices.oldManufacturer, errMessage);		
		Assert.assertEquals(existingDeviceInfo[1], IdentifyDevices.oldModel, errMessage);
		Assert.assertEquals(existingDeviceInfo[2].replace("_", " "), IdentifyDevices.oldSerialNumberType, errMessage);  // When Serial Number Type is CAP_CODE, it's displayed with no underscore, and it's causes error 
		Assert.assertEquals(existingDeviceInfo[3], IdentifyDevices.oldSerialNumber, errMessage);
		
		String[] newDeviceInfo = getNewDeviceSwapDevice();
		
		Assert.assertEquals(newDeviceInfo[0], IdentifyDevices.newManufacturer, errMessage);		
		Assert.assertEquals(newDeviceInfo[1], IdentifyDevices.newModel, errMessage);
		Assert.assertEquals(newDeviceInfo[2], IdentifyDevices.newSerialNumberTypeForVerify, errMessage);
		Assert.assertEquals(newDeviceInfo[3], IdentifyDevices.newSerialNumber, errMessage);
		
	}
	
	// verify all the items in the plan section. 
	public static void verifySelectedPlanAndOptionalFeaturesDetails() throws Exception 
	{
		String errorMessage = "Wrong information found in test for plan option features in VerifyOrderPage.verifySelectedPlanAndOptionalFeaturesDetails.";
		
		// this get all of the string information in the plan section.
		//String [] allPlanItems = driver.findElement(By.xpath("//div[@id='verify_plan']/div[3]")).getText().split("\n");
		
		String planName = driver.findElement(By.xpath("//div[@id='verify_plan']/div[3]/div")).getText();
		Assert.assertEquals(planName, planInfoActions.planSelectedName, errorMessage);
		
		System.out.println("planInfoActions.planCostCompleteField: " + planInfoActions.planCostCompleteField);
		
		String price = driver.findElement(By.xpath("//div[@id='verify_plan']/div[3]/table/tbody/tr[1]/td[2]/span")).getText();
		Assert.assertEquals(price, planInfoActions.planCostCompleteField.split("\\)")[1].trim(), errorMessage); 	
		
		// Included Features
		
		List<WebElement> includedFeaturesFound = driver.findElements(By.cssSelector("div#verify_plan>div.tg-display--inline-block>table>tbody>tr:nth-of-type(2)>td:nth-of-type(2)>span>ul>li"));
		
		System.out.println("# Included Features: " + PlanInfoActions.includedFeatures.size());
		
		for (int i = 0; i < PlanInfoActions.includedFeatures.size(); i++) {
			
			System.out.println("Included Feature: " + PlanInfoActions.includedFeatures.get(i));
			Assert.assertEquals(includedFeaturesFound.get(i).getText().trim(), PlanInfoActions.includedFeatures.get(i).trim());
			
		}
		
		// Optional Features
		
		List<WebElement> optionalFeaturesFound = driver.findElements(By.cssSelector("div#verify_plan>div.tg-display--inline-block>table>tbody>tr:nth-of-type(3)>td:nth-of-type(2)>span>ul>li"));
				
		System.out.println("# Optional Features: " + PlanInfoActions.optionalFeatures.size());
		
		for (int i = 0; i < PlanInfoActions.optionalFeatures.size(); i++) {
			
			System.out.println("Optional Feature: " + PlanInfoActions.optionalFeatures.get(i));
			Assert.assertTrue(optionalFeaturesFound.get(i).getText().trim().startsWith(PlanInfoActions.optionalFeatures.get(i).trim()));
			
		}
		
	}
	
	
	public static void verifyAccessoriesDetails()  
	{
		String errorMessage = "Wrong information found in test for accessories in VerifyOrderPage.verifyAccessoriesDetails.";
		
		for (int i = 0; i < accessoriesInCartHashMap.size(); i++) {
		
			int indexForXpath = i+2;
			String accessoryNameExpected = driver.findElement(By.xpath("//div[@id='verify_accessories']/div[" + indexForXpath + "]/div[2]/div")).getText();
			Assert.assertEquals(accessoryNameExpected, accessoriesInCartHashMap.get(accessoryNameExpected).name, errorMessage);
			
			String accessoryVendorExpected = driver.findElement(By.xpath("//div[@id='verify_accessories']/div[" + indexForXpath + "]/div[2]/table/tbody/tr[1]/td[2]/span")).getText();
			Assert.assertEquals(accessoryVendorExpected, accessoriesInCartHashMap.get(accessoryNameExpected).vendor, errorMessage);
			
			String accessoryPriceExpected = driver.findElement(By.xpath("//div[@id='verify_accessories']/div[" + indexForXpath + "]/div[2]/table/tbody/tr[2]/td[2]/span")).getText();
			Assert.assertEquals(accessoryPriceExpected, accessoriesInCartHashMap.get(accessoryNameExpected).cost, errorMessage);
			
			System.out.println("Accessory Name: " + accessoryNameExpected);
			//System.out.println("Accessory Vendor: " + accessoryVendorExpected);
			//System.out.println("Accessory Price: " + accessoryPriceExpected);
			
		}
		
	}
	
 
	// this verifies the accessories listed in the 'verify order page' for order accessories match
	// what accessories that were selected
	public static void VerifyOrderAccessoriesAction() throws Exception
	{
		String errMessage = "Failed to verify order accessories in verify order page with method VerifyOrderPage.VerifyOrderAccessories.";
		
		// this list will hold the accessories that were selected in the choose accessories page.   
		List<AccessoriesDetailsExpected> selectedAccessories = new ArrayList<>();

		// this puts the item 0 from the accessoriesDetailsListExpected list onto local list selectedAccessories.
		// items 1 from the accessories list in choose accessories were selected. accessoriesDetailsListExpected list
		// was populated from the accessories shown in the 'choose accessories' page.  
		selectedAccessories.add(accessoriesDetailsListExpected.get(AccessoriesDetailsExpected.orderAccessoriesActionSelectionOneArrayIndex));
		
		
		// get info for first accessory and verify.
		strArray = driver.findElement(By.xpath("//div[text()='Accessories']/following ::div[1]")).getText().split("\n");
		
		Assert.assertEquals(strArray[0], selectedAccessories.get(0).name, errMessage);
		Assert.assertEquals(strArray[1].replace("Vendor ",""), selectedAccessories.get(0).vendor, errMessage);		
		Assert.assertEquals(strArray[2].replace("Price ",""), selectedAccessories.get(0).cost, errMessage);		
		
		
		if (accessoriesDetailsListExpected.size() > 1) {
			
			// this puts the item 1 from the accessoriesDetailsListExpected list onto local list selectedAccessories.
			// items 2 from the accessories list in choose accessories were selected. accessoriesDetailsListExpected list
			// was populated from the accessories shown in the 'choose accessories' page.
			selectedAccessories.add(accessoriesDetailsListExpected.get(AccessoriesDetailsExpected.orderAccessoriesActionSelectionTwoArrayIndex));
			
			// get info for second accessory and verify.
			strArray = driver.findElement(By.xpath("//div[text()='Accessories']/following ::div[1]/following-sibling ::div[1]")).getText().split("\n");

			Assert.assertEquals(strArray[0], selectedAccessories.get(1).name, errMessage);
			Assert.assertEquals(strArray[1].replace("Vendor ",""), selectedAccessories.get(1).vendor, errMessage);		
			Assert.assertEquals(strArray[2].replace("Price ",""), selectedAccessories.get(1).cost, errMessage);
			
		}
				
	}
	
	// it is assumed the last accessory on the accessoriesDetailsListExpected list was selected.
	public static void verifyUpgradeDeviceAccessoriesAction() throws Exception
	{
		errMessage = "Fail in VerifyOrderPage.VerifyUpgaredDeviceAccessoriesAction."; 
		// use the name of the accessory to get an array of the accessory name, vendor, and price.
		strArray =  driver.findElement(By.xpath("//div[text()='" + accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).name + "']/..")).getText().split("\n");
		//for(int x = 0; x < strArray.length; x++){System.out.println(x + " " + strArray[x])}; // DEBUG

		// verify the name, vendor, and cost.
		Assert.assertEquals(strArray[0], accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).name, errMessage);
		Assert.assertEquals(strArray[1].replace("Vendor ", ""), accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).vendor, errMessage);
		Assert.assertEquals(strArray[2].replace("Price ", ""), accessoriesDetailsListExpected.get(accessoriesDetailsListExpected.size() - 1).cost, errMessage);		
	}
	
	// verify final cost and final cost monthly. 
	public static void verifyCostMonthlyUpgradeDevice()
	{
		errMessage = "Fail in VerifyOrderPage.verifyCostMonthlyUpgradeDevice";
		
		String costMonthlyActual = driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]")).getText();
		String costMonthlyExpected = "$" + ShoppingCart.costMonthly;
		
		System.out.println("Cost monthly actual: " + costMonthlyActual);
		System.out.println("Cost monthly expected: " + costMonthlyExpected);
		
		Assert.assertEquals(costMonthlyActual, costMonthlyExpected, errMessage);	
		
		//Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]")).getText(), planInfoActions.costMonthlyTotal, errMessage);		
		//Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]")).getText(), AccessoriesDetailsExpected.finalCostMonthly, errMessage);		
	}
	
	public static void verifyCostUpgradeDevice()
	{
		errMessage = "Fail in VerifyOrderPage.verifyCostUpgradeDevice";
		
		//Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost']/../following ::td[1]")).getText(), ShoppingCart.costOneTime, errMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost']/../following ::td[1]")).getText(), "$" + ShoppingCart.costOneTime, errMessage);
				// AccessoriesDetailsExpected.finalCost
	}
	
	
	// verify final cost and final cost monthly. 
	public static void VerifyCostAndCostmonthlyPortNumber()
	{
		errMessage = "Fail in VerifyOrderPage.VerifyCostAndCostmonthlyPortNumber";
		
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost']/../following ::td[1]")).getText(), DevicePortNumber.selectedDevicePrice, errMessage);
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]")).getText(), PlanInfoActions.portCostMonthly, errMessage);		
	}
	
	// verify final cost and final cost monthly. 
	public static void VerifyPlanPortNumber()
	{
		errMessage = "Fail in VerifyOrderPage.VerifyPlanPortNumber";
		
		strArray =  driver.findElement(By.cssSelector("#verify_plan >div:nth-of-type(3)")).getText().split("\n");
		
		Assert.assertEquals(strArray[0], PlanInfoActions.portPlanName, errMessage);
		Assert.assertEquals(strArray[1], PlanInfoActions.portPlanCost.replace("(Monthly)", "Price"), errMessage);	
		Assert.assertEquals(strArray[2], "Included Features", errMessage);

		
		int x = 3;
		
		for(String str : PlanInfoActions.includedFeatures) // verify included features.
		{
			Assert.assertEquals(strArray[x], str, errMessage);
			x++;
		}
		
		// verify 
		Assert.assertEquals(strArray[18], optionalFeaturesList.get(0).completeName, errMessage);
		Assert.assertEquals(strArray[19], optionalFeaturesList.get(optionalFeaturesList.size() - 1).completeName, errMessage);		
		
	}	

	// verify device info. 
	public static void VerifyDevice()
	{
		errMessage = "Fail in VerifyOrderPage.VerifyDevice";
		
		strArray = driver.findElement(By.cssSelector(".tg-display--inline-block.tg-valign--top.tg-space--half--top > table")).getText().split("\n");
		
		Assert.assertEquals(driver.findElement(By.cssSelector("#verify_device > div:nth-of-type(3) > div")).getText() , DevicePortNumber.selectedDeviceName, errMessage); // name
		Assert.assertEquals(strArray[0].replace("Vendor ", "") , DevicePortNumber.selectedDeviceVendor,  errMessage); // vendor		
		Assert.assertEquals(strArray[1].replace("Price ", "") , DevicePortNumber.selectedDevicePrice,  errMessage); // price
	}
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	/*
	public static void VerifyAdditionalInformation() throws Exception
	{
		String errorMessage = "Wrong information found in test for additional information in VerifyOrderPge.VerifyAdditionalInformation.";
		
		// this gets all of the string information for the additional information listed.
		String [] additionalInformation = driver.findElement(By.xpath("//div[@id='verify_properties']/div[2]/table")).getText().split("\n");
		
		for (int i = 0; i < additionalInformation.length; i++)
		{
			if (additionalInformation[i].startsWith("Preferred Area Code")) {
				
				Assert.assertEquals(additionalInformation[i], "Preferred Area Code " + preferredAreaCode, errorMessage);
			
			} else if (additionalInformation[i].startsWith("Contact Phone Number")) {
				
				Assert.assertEquals(additionalInformation[i], "Contact Phone Number " + contactNumber, errorMessage);
			
			} else if (additionalInformation[i].startsWith("Additional Instructions")) {
				
				Assert.assertEquals(additionalInformation[i], "Additional Instructions " + additionalInstructions, errorMessage);
					
			} else if (additionalInformation[i].startsWith("Reason")) {
				
				Assert.assertEquals(additionalInformation[i], "Reason " + reasonAddDeviceAndService, errorMessage);
				
			} else if (additionalInformation[i].startsWith("Business Unit")) {
				
				Assert.assertEquals(additionalInformation[i], "Business Unit " + businessUnit, errorMessage);
				
			}					
			
		}
		
	}*/


	// this verification is called when processing a deactivate. 
	public static void VerifyAdditionalInformationDeactivate() throws Exception
	{
		String errorMessage = "Failure in checking additional information for Deactivate Service action in DeactivateService.VerifyAdditionalInformationDeactivate.";		
		
		Assert.assertEquals(GetAdditionalInfoDeactivate()[0], contactNumber, errorMessage);
		Assert.assertEquals(GetAdditionalInfoDeactivate()[1], additionalInstructions, errorMessage);
		Assert.assertEquals(GetAdditionalInfoDeactivate()[2], serviceNumber, errorMessage);
		Assert.assertEquals(GetAdditionalInfoDeactivate()[3], reasonAction, errorMessage);

	}
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// this verification is called when processing an update action. 
	/*
	public static void VerifyAdditionalInformationUpdateFeature() throws Exception
	{
		errMessage = "Fail in VerifyOrderPage.VerifyAdditionalInformationUpdateFeature";
		strArray = driver.findElement(By.cssSelector("#verify_properties > div:nth-child(2) table tbody ")).getText().split("\n");
		
		VerifyAdditionalInformationCommon(strArray, errMessage);

		// css= #verify_properties > div:nth-child(2) table tbody tr:nth-of-type(1) // gets one element of additional info in verify orders page.
	}*/
	
	
	// This verification is called when processing any kind of order. It applies to all orders --- 10/3/17 Ana
	public static void verifyAdditionalInformationBlock() throws Exception
	{
		String errorMessage = "Failure in checking additional information in VerifyOrder.verifyAdditionalInformationBlock.";		

		HashMap<String, String> additionalInfoMap = getAdditionalInformationData();
			
		
		if (additionalInfoMap.containsKey("Contact Phone Number")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Contact Phone Number"));
			Assert.assertEquals(additionalInfoMap.get("Contact Phone Number"), contactNumber, errorMessage);
			
		} if (additionalInfoMap.containsKey("Ext")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Ext"));
			Assert.assertEquals(additionalInfoMap.get("Ext"), extension, errorMessage);
			
		} if (additionalInfoMap.containsKey("Additional Instructions")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Additional Instructions"));
			Assert.assertEquals(additionalInfoMap.get("Additional Instructions"), additionalInstructions, errorMessage);
			
		} if (additionalInfoMap.containsKey("Service Number")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Service Number"));
			
			if (approvalActionType.equals(ApprovalActionType.transferServiceIn) 
					|| approvalActionType.equals(ApprovalActionType.transferServiceInAndPort)) {
				Assert.assertEquals(additionalInfoMap.get("Service Number"), newServiceNumber, errorMessage);
			} else {
				Assert.assertEquals(additionalInfoMap.get("Service Number"), serviceNumber, errorMessage);
			}
			
		} if (additionalInfoMap.containsKey("Authorization Code")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Authorization Code"));
			Assert.assertEquals(additionalInfoMap.get("Authorization Code"), IdentifyDevices.authorizationCode, errorMessage);
			 
		} if (additionalInfoMap.containsKey("Preferred Area Code")) {
			
			System.out.println(" Key: " + additionalInfoMap.get("Preferred Area Code"));
			Assert.assertEquals(additionalInfoMap.get("Preferred Area Code"), preferredAreaCode, errorMessage);
			
		} if (additionalInfoMap.containsKey("Reason") && approvalActionType.equals(ApprovalActionType.deactivate)) {  // for Deactivate 
			
			System.out.println(" Key: " + additionalInfoMap.get("Reason"));
			Assert.assertEquals(additionalInfoMap.get("Reason"), reasonAction, errorMessage);
			
		} if (additionalInfoMap.containsKey("Reason") && approvalActionType.equals(ApprovalActionType.newActivation)) {  // for New activation 
			
			System.out.println(" Key: " + additionalInfoMap.get("Reason"));
			Assert.assertEquals(additionalInfoMap.get("Reason"), reasonAddDeviceAndService, errorMessage);
			
		} if (additionalInfoMap.containsKey("Reason") && approvalActionType.equals(ApprovalActionType.upgradeDevice)) {  // for Upgrade Device 
			
			System.out.println(" Key: " + additionalInfoMap.get("Reason"));
			Assert.assertEquals(additionalInfoMap.get("Reason"), UpgradeDevice.reasonUpgradeDevice, errorMessage);
			
		} if (additionalInfoMap.containsKey("Business Unit")) {  // for New activation 
			
			System.out.println(" Key: " + additionalInfoMap.get("Business Unit"));
			Assert.assertEquals(additionalInfoMap.get("Business Unit"), businessUnit, errorMessage);
			
		} if (additionalInfoMap.containsKey("Preferred Suspension Date")) {  // for Suspend
			
			System.out.println(" Key: " + additionalInfoMap.get("Preferred Suspension Date"));
			Assert.assertTrue(CalendarDateTimeObject.VerifyMonthAndYear(additionalInfoMap.get("Preferred Suspension Date")), errorMessage);
			
		} if (additionalInfoMap.containsKey("Hold Service")) {  // for Suspend 
			
			System.out.println(" Key: " + additionalInfoMap.get("Hold Service"));
			//Assert.assertEquals(additionalInfoMap.get("Hold Service"), limitedUserPulldownSelection, errorMessage); // <-- UNCOMMENT WHEN SFD 112978 IS FIXED
			
		} if (additionalInfoMap.containsKey("Preferred Unsuspend Date")) {  // for Unsuspend
			
			System.out.println(" Key: " + additionalInfoMap.get("Preferred Unsuspend Date"));
			Assert.assertTrue(CalendarDateTimeObject.VerifyMonthAndYear(additionalInfoMap.get("Preferred Unsuspend Date")), errorMessage);
			
		} if (additionalInfoMap.containsKey("User")) {  // for Unsuspend
			
			System.out.println(" Key: " + additionalInfoMap.get("User"));
			Assert.assertEquals(additionalInfoMap.get("User"), userLimitedFullNameExtended, errorMessage);
			
		} if (additionalInfoMap.containsKey("Carrier Account Number")) {  // for Port Number
			
			System.out.println(" Key: " + additionalInfoMap.get("Carrier Account Number"));
			Assert.assertEquals(additionalInfoMap.get("Carrier Account Number"), PlanInfoActions.carrierAccountNumber, errorMessage);
			
		} if (additionalInfoMap.containsKey("Name on Invoice")) {  // for Port Number
			
			System.out.println(" Key: " + additionalInfoMap.get("Name on Invoice"));
			Assert.assertEquals(additionalInfoMap.get("Name on Invoice"), userLimitedShorterName, errorMessage);
			
		} if (additionalInfoMap.containsKey("Current Carrier")) {  // for Port Number
			
			System.out.println(" Key: " + additionalInfoMap.get("Current Carrier"));
			Assert.assertEquals(additionalInfoMap.get("Current Carrier"), DeviceInfoActions.currentVendorPortNumber, errorMessage);
			
		} if (additionalInfoMap.containsKey("Personal E-mail Address")) {  // for personal email address
		       
            System.out.println(" Key: " + additionalInfoMap.get("Personal E-mail Address"));
            Assert.assertEquals(additionalInfoMap.get("Personal E-mail Address"), approverAdminMail, errorMessage);
		}


		
		
	}	
	
	
	
	// 9/22/17 Ana - Adding **GENERIC** method to obtain Additional Information into a HashMap
	public static HashMap<String, String> getAdditionalInformationData() {
		
		HashMap<String, String> additionalInfoMap = new HashMap<>();
		
		List<WebElement> listAdditionalInfoRows = driver.findElements(By.xpath("//div[text()='Additional Information']/following-sibling::div/table/tbody/tr"));
		
		for (int i = 1; i < listAdditionalInfoRows.size(); i++) {
			
			// System.out.println("Row: " + listAdditionalInfoRows.get(i-1).getText());
			
			String label = driver.findElement(By.xpath("//div[text()='Additional Information']/following-sibling::div/table/tbody/tr[" + i + "]/td[1]")).getText();  //row.getText().split(":")[0].trim();
			String value = driver.findElement(By.xpath("//div[text()='Additional Information']/following-sibling::div/table/tbody/tr[" + i + "]/td[2]")).getText();   //row.getText().split(":")[1].trim();
			
			additionalInfoMap.put(label, value);
			System.out.println("Label: " + label + ", value: " + value);
			
		}
		
		return additionalInfoMap;
		
	}
		
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// this verification is called when processing a suspend.
	// NOTE: 'Hold Service' has defect DE# 107772.
	/*
	public static void VerifyAdditionalInformationSuspend() throws Exception
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationSuspend.";
		Assert.assertEquals(GetAdditionalInfoSuspend()[0], contactNumber, errMessage);
		Assert.assertEquals(GetAdditionalInfoSuspend()[1], additionalInstructions, errMessage);
		Assert.assertEquals(GetAdditionalInfoSuspend()[2], serviceNumber, errMessage);
		Assert.assertTrue(CalendarDateTimeObject.VerifyMonthAndYear(GetAdditionalInfoSuspend()[3]));		
		
		String holdServiceFound = driver.findElement(By.xpath("//td/label[text()='Hold Service']/../following-sibling::td/span")).getText(); 
		// Assert.assertEquals(holdServiceFound, limitedUserPulldownSelection, errMessage); // <-- UNCOMMENT WHEN SFD 112978 IS FIXED
		
	}*/
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// this verification is called when processing a suspend.
	// NOTE: 'Hold Service' has defect DE# 107772.
	/*public static void VerifyAdditionalInformationUnsuspend() throws Exception
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationSuspend.";
     
		Assert.assertEquals(GetAdditionalInfoUnsuspend()[0], contactNumber, errMessage);
		Assert.assertEquals(GetAdditionalInfoUnsuspend()[1], additionalInstructions, errMessage);
		Assert.assertEquals(GetAdditionalInfoUnsuspend()[2], serviceNumber, errMessage);
		Assert.assertTrue(CalendarDateTimeObject.VerifyMonthAndYear(GetAdditionalInfoSuspend()[3]));	
		Assert.assertEquals(GetAdditionalInfoUnsuspend()[4], userLimitedFullNameExtended, errMessage);
	}*/	
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/3/17 -- Ana
	/*
	public static void VerifyAdditionalInformationOderAccessories() throws Exception 
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationOderAccessories.";		

		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		

		VerifyAdditionalInformationCommon(strArray, errMessage);
	}*/

	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana

	public static void VerifyAdditionalInformationUpgradeDevice() throws Exception 
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationOderAccessories.";		
		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		
		
		VerifyAdditionalInformationCommon(strArray, errMessage);
		Assert.assertEquals(strArray[4].replace("Reason ", ""), UpgradeDevice.reasonUpgradeDevice, errMessage);		
	} 
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	/*
	public static void VerifyAdditionalInformationPortNumber() throws Exception 
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationPortNumber.";		
		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		
		
		Assert.assertEquals(strArray[0].replace("Carrier Account Number ", ""), PlanInfoActions.carrierAccountNumber, errMessage);
		Assert.assertEquals(strArray[1].replace("Name on Invoice ", ""), userLimitedFullName, errMessage);		
		Assert.assertEquals(strArray[2].replace("Contact Phone Number ", ""), contactNumber, errMessage);
		Assert.assertEquals(strArray[3].replace("Ext ", ""), extension, errMessage);
		Assert.assertEquals(strArray[4].replace("Additional Instructions ", ""), additionalInstructions, errMessage);		
		Assert.assertEquals(strArray[5].replace("Current Carrier ", ""), DeviceInfoActions.currentVendorPortNumber, errMessage);
		Assert.assertEquals(strArray[6].replace("Service Number ", ""), serviceNumber, errMessage);
	}*/
	
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// can be used by other methods for common sections. 
	public static void VerifyAdditionalInformationCommon(String [] strArray, String errMessage)
	{
		Assert.assertEquals(strArray[0].replace("Contact Phone Number ", ""), contactNumber, errMessage);
		Assert.assertEquals(strArray[1].replace("Ext ", ""), extension, errMessage);		
		Assert.assertEquals(strArray[2].replace("Additional Instructions ", ""), additionalInstructions, errMessage);
		Assert.assertEquals(strArray[3].replace("Service Number ", ""), serviceNumber, errMessage);
	}
	
	public static void VerifyShippingInformation() throws Exception
	{
		String errorMessage = "Wrong information found in test for shipping information in VerifyOrderPage.VerifyShippingInformation.";

		// this gets all of the string information for the shipping information listed.
		String [] additionalInformation = driver.findElement(By.xpath("//div[@ng-if='orderConfirmation.shipTo']/table")).getText().split("\n");
		
		for(int x = 0; x < additionalInformation.length; x++)
		{
			switch(x)
			{
				case 0:
				{
					Assert.assertEquals(additionalInformation[x], "Address Line1 " + userLineOne, errorMessage);  // "Address Line1 9847 Main"
					break;
				}
				case 1:
				{
					Assert.assertEquals(additionalInformation[x], "Address Line2", errorMessage);
					break;
				}
				case 2:
				{
					Assert.assertEquals(additionalInformation[x], "Address Line3", errorMessage);
					break;
				}
				case 3:
				{
					Assert.assertEquals(additionalInformation[x], "City " + userCity, errorMessage);  // "City Boston"
					break;
				}
				case 4:
				{
					Assert.assertEquals(additionalInformation[x], "State " + userStateShort, errorMessage);  // "State MA"
					break;
				}
				case 5:
				{
					Assert.assertEquals(additionalInformation[x], "Zip Code " + userPostalCode, errorMessage);  // "Zip Code 02334"
					break;
				}				
			}
		}
	}

	public static void VerifyShippingInformationOrderAccessoriesAction() throws Exception
	{
		String errMessage = "Wrong information found in test for shipping information in VerifyOrderPge.VerifyShippingInformationOrderAccessoriesAction.";
		
		strArray = driver.findElement(By.xpath("//div[text()='Shipping Information']/following ::table[1]")).getText().split("\n");
		
		Assert.assertEquals(strArray[0].replace("Address Line1 ", ""), addressLineOne, errMessage);
		Assert.assertEquals(strArray[3].replace("City ","" ), cityOrderActions, errMessage);
		Assert.assertEquals(strArray[4].replace("State ", ""), stateOrderActions, errMessage);
		Assert.assertEquals(strArray[5].replace("Zip Code ", ""), zipCodeOrderActions, errMessage);		
		
	}
	
	public static void VerifyShippingInformationPortNumber() throws Exception
	{
		String errMessage = "Wrong information found in test for shipping information in VerifyOrderPge.VerifyShippingInformationPortNumber."; 
		
		// this gets many of the items. 
		VerifyShippingInformationOrderAccessoriesAction();
	
		strArray = driver.findElement(By.xpath("//div[text()='Shipping Information']/following ::table[1]")).getText().split("\n");		
		
		Assert.assertEquals(strArray[1].replace("Address Line2 ", ""), addressLineTwoDummyInfo, errMessage);		
		Assert.assertEquals(strArray[2].replace("Address Line3 ", ""), addressLineThreeDummyInfo, errMessage);		
		
		/*
		Show Array ------------------------
		0 Address Line1 40 Main
		1 Address Line2 LineTwoDummyInfo
		2 Address Line3 LineThreeDummyInfo
		3 City Boston
		4 State MA
		5 Zip Code 06234
		Freeze
		
		 */
	}
	
	
	public static void VerifyCostAndCostMonthly()
	{
		
		String costMonthlyFound = driver.findElement(By.xpath("//label[text()='Cost Monthly']/../following ::td[1]")).getText();
		String costMonthlyExpected = CostMonthlyCalculatedConvertToFullText(ShoppingCart.costMonthly);  // planInfoActions.costMonthlyTotal 
		
		Assert.assertEquals(costMonthlyFound, costMonthlyExpected, "");
		
		String costFound = driver.findElement(By.xpath("//label[text()='Cost']/../following ::td[1]/span")).getText();
		String costExpected = CostMonthlyCalculatedConvertToFullText(ShoppingCart.costOneTime);  // AccessoriesDetailsExpected.finalCost
		
		Assert.assertEquals(costFound, costExpected, "");
		
	}
	
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												HELPER METHOD
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	public static String [] GetAdditionalInfoSuspend()
	{
 
		strArray = driver.findElement(By.xpath("//tbody")).getText().split("\n");
		return new String [] {strArray[0].replace("Contact Phone Number ", ""), strArray[1].replace("Additional Instructions ", ""),
							 strArray[2].replace("Service Number ", ""), strArray[3].replace("Preferred Suspension Date ", ""),
							 strArray[4].replace("Hold Service ","")};
	}
	
	
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	public static String [] GetAdditionalInfoUnsuspend()
	{
		strArray = driver.findElement(By.xpath("//tbody")).getText().split("\n");
		return new String [] {strArray[0].replace("Contact Phone Number ", ""), strArray[1].replace("Additional Instructions ", ""),
							 strArray[2].replace("Service Number ", ""), strArray[3].replace("Preferred Suspension Date ", ""),
							 strArray[4].replace("User ","")};
	}	
	
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	public static String [] GetAdditionalInfoDeactivate()
	{
		strArray = driver.findElement(By.xpath("//tbody")).getText().split("\n");
		return new String [] {strArray[0].replace("Contact Phone Number ", ""), 
				              strArray[1].replace("Additional Instructions ", ""),
							  strArray[2].replace("Service Number ", ""), 
							  strArray[3].replace("Reason ", "")};
	}
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	/*public static String [] getAdditionalInfoSwapDevice()
	{
		strArray = driver.findElement(By.xpath("(//tbody)[3]")).getText().split("\n");
		return new String [] {strArray[0].replace("Contact Phone Number ", ""), 
							  strArray[1].replace("Ext ", ""),
							  strArray[2].replace("Additional Instructions ", ""),
							  strArray[3].replace("Preferred Area Code ", ""),							  
							  strArray[4].replace("Service Number ", "") }; // removed 9/16 -- ** Uncommented since it's present - 9/15/17 - Ana 
	}*/	
	
	
	
	
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	public static String [] getExistingDeviceSwapDevice()
	{
 
		strArray = driver.findElement(By.xpath("(//tbody)[1]")).getText().split("\n");
		return new String [] {strArray[0].replace("Manufacturer ", ""), 
				              strArray[1].replace("Model ", ""),
							  strArray[2].replace("Serial Number Type ", ""), 
							  strArray[3].replace("Serial Number ", "")};
	}	
	
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	public static String [] getNewDeviceSwapDevice()
	{
 
		strArray = driver.findElement(By.xpath("(//tbody)[2]")).getText().split("\n");
		return new String [] {strArray[0].replace("Manufacturer ", ""), 
				              strArray[1].replace("Model ", ""),
							  strArray[2].replace("Serial Number Type ", ""), 
							  strArray[3].replace("Serial Number ", "")};
	}		
	
	// ********* NOT USED ******* TO BE REMOVED ****************** 10/4/17 -- Ana
	// return all items in additional info block. this is a certain format. it isn't global for everything.
	/*public static String [] GetAdditionalInfoOrderAccessories()
	{
 
		strArray = driver.findElement(By.xpath("(//tbody)[2]")).getText().split("\n");
		return new String [] {strArray[0].replace("Contact Phone Number ", ""), 
				              strArray[1].replace("Additional Instructions ", ""),
							  strArray[2].replace("Service Number ", "")}; 
	}*/	
	
	// this is for SFD112978_Suspend. bob 10/20/17
	public static void VerifyHoldServiceHasData()
	{
		// NOTE: xpath may need to be "label[text()='Hold Service']/../following-sibling::td/span"

		ShowText(driver.findElement(By.xpath("//label[text()='Hold Service']/../following-sibling::td")).getText());
		
		//Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Hold Service']/../following-sibling::td")).getText(), limitedUserPulldownSelection, "");
	}
}
