package ActionClasses;

import static org.testng.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import HelperObjects.PlanInfoActions;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.ModifyOptions;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;

public class ModifySelectionsTesting extends ServiceNow.ModifySelectionsTesting 
{

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE !!! - the two carrier variables directly below are for this test only. This combination makes a large list of devices that can be added.
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static String currentCarrierLocal = "Sprint";
	public static String newCarrierLocal = "Verizon Wireless";
	
	public static String lineFeed = "\r\n";
	public static String noOptionalFeaturesOptionsPage = "No optional features have been selected";
	public static String actual = "";
	public static String expected = "";
	
	public static List<Device> listOfDevices = new ArrayList<Device>(); 
	// public static List<Device> finalListOfDevices = new ArrayList<Device>();

	public static int deviceListSize = -1;
	//public static int verifyPageDeviceIndex = -1;
	//public static int verifyPagePlanOptionIndex = -1;	
	//public static int verifyPageTempIndex = -1;
	
	public static BufferedWriter output = null;
	
	// load the devices, that have plans, and their index onto a list
	// add info about plan options, and accessories to each.
	// create final list for testing.
	public static void LoadDeviceData() throws Exception
	{
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);

		ChooseDevicePage.SetupForDeviceSelection(); // setup list of objects that hold information for each device.

		// this goes through the list of devices and adds all devices that that have a plan to the local 'listOfDevices' list of device objects.
		for(int x = 0; x < ChooseDevicePage.deviceVendorList.size(); x++)
		{
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			ChooseDevicePage.clickNextButton(); //Click Next. You are in the Choose a Plan step. The available plans for the selected device are listed.
			if(ChoosePlanPage.waitForPageToLoadPlans())
			{
				ChoosePlanPage.clickBackButton();
				listOfDevices.add(new Device(ChooseDevicePage.deviceVendorList.get(x).device, x));
			}
			ChooseDevicePage.WaitForPageToLoad();
		}
		
		ClearListSelection();
		// for(Device dev : listOfDevices){dev.Show();} // show device list
		
		PopulatedDeviceList();
		
		CreateFinalDeviceList();
		// for(Device dev : finalListOfDevices){dev.Show();} // show final device list
		
		// output final device list to file.
		OutputFinalDeviceListToFile();
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 											TEST BLOCKS/CALLS  BELOW
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// select modify in plan page, select the first option, go back to device page, and verify it shows up in the device page. go to the 
	// plan page, select 'Modify' to get to devices page, select new device, go to plan page, select plan, and verify new device is in plan page.
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestOne() throws Exception 
	{
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		
		ChoosePlanPage.clickBackButton(); // go back to devices page
		ChooseDevicePage.WaitForPageToLoad();
		
		// verify the proper plan optional feature selected through the modify selection above is present in the Plan Features section of device page.
		// NOTE: the exact text is not tested for. the plan name is only verified to be in the text block of the  
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout);
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = finalListOfDevices.get(0).optionsList.get(0).split("-")[0].trim();
		Assert.assertTrue("", actual.contains(expected));
		
		// go to plan page and select to modify the device that's selected. this will take user to devices page.
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanAndOptionSelected();
		WaitForElementClickable(By.xpath(xpathClickFirstModify), MediumTimeout, "");
		driver.findElement(By.xpath(xpathClickFirstModify)).click(); // select modify
		
		// clear device list selection, verify warning message, select a new device,  
		ClearListSelection(); 
		VerifyWarnigIsPresentAndClose(true, 0);
		Thread.sleep(1500); // need pause here to allow pop-up to go away. if no pause, there is a click conflict.
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(1).index + 1 ) + "]")).click(); // add new device
		ChooseDevicePage.clickNextButton();
		
		// select plan and verify new selected device is in plan page.
		ChoosePlanPage.WaitForPageToLoadPlanOrig(); 
		ChoosePlanPage.SelectFirstPlan();
		WaitForElementPresent(By.xpath("//div[@ng-if='catalogs.device.selectedItem']"), ShortTimeout); // verify new device is listed 
		actual = driver.findElement(By.xpath("//div[@ng-if='catalogs.device.selectedItem']")).getText().replace("\n", ""); 
		expected = finalListOfDevices.get(1).name;
		Assert.assertTrue("", actual.contains(expected));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// go to accessory page,  remove plan option using modify, and verify in accessory page   
	// go to accessory page, select plan using modify, verify error on next, add plan, next, verify three modifies are present.
	// from accessory page, go to devices page using Modify selection in accessory page,  change the device selection, and verify warning message.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestTwo() throws Exception 
	{
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		
		// go to the accessories page, select modify for plan options, remove the option, go forward to the
		// accessory page and verify there are no options. 
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories()); // accessories page should have at least one accessory. // intermittent error here.
		UnSelectOptionOne();
		ChoosePlanPage.clickNextButton();
		
		// verify the proper plan optional feature is un-selected through the modify selection is not present in the Plan Features section.
		// NOTE: the exact text is not tested for. this only verifies a no plan message to be in the text block of the plan options section.  
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout);
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = "No optional features have been selected";
		Assert.assertTrue("", actual.contains(expected));
		
		// select plan page through the plan Modify selection, remove the selected plan, select next and verify error message.
		// put the plan back. go to the accessories page and verify there are three modify selections there.
		driver.findElement(By.xpath(xpathClickSecondModify)).click();
		ChoosePlanPage.WaitForPageToLoadPlanSelected();
		ClearListSelection();
		ChoosePlanPage.clickNextButton();
		WaitForElementVisible(By.xpath("//li[text()='Please select a Plan']"), MediumTimeout);
		driver.findElement(By.xpath(xpathAddToCartForIndexing +  "[1]")).click();
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();		
		// WaitForAccessoriesPage();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());

		// click first modify to get back to device page, verify and cancel message after removing selection.
		VerifyRemoveMessage(xpathClickFirstModify, 0);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE: removing accessory from 'remove selection had problems. see 'TestThreeAccessoryProblemWithRemove()'   
	// Go to provide additional info adding plan option and accessory on the way. 
	// use accessory 'add more' link to remove accessory and verify in provide additional info page.
	// use first modify to get to device page, create error and verify basic error.
	// use second  modify to get to plan page, create error and verify basic error.
	// use third modify to change plan options (remove) and verify change in provide additional info page.
	// TODO: something with accessories
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestThree() throws Exception 
	{
		String tempString = "";
		
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		ChoosePlanPage.clickNextButton();

		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // add accessory.
		ChooseAccessoriesPage.clickNextBtn();
		
		// verify accessory is there in additional info. select 'Add More' accessory selection in additional info to go back and 
		// remove accessory, then verify it's gone.
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		WaitForElementClickable(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div/span/a[text()='Add More']"), ShortTimeout, "");
		Assert.assertTrue(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")).getText().contains(tempString)); // accessory there
		driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div/span/a[text()='Add More']")).click(); // go back using 'Add More'

		// at accessory page remove accessory from list, go forward, and verify it is not present in additional info.  
		WaitForElementClickable(By.xpath(xpathRemoveFromCart), MediumTimeout, "");
		driver.findElement(By.xpath(xpathRemoveFromCart)).click(); 
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		Thread.sleep(1500); // need wait time for accessory text to go away.
		Assert.assertFalse(CatchFail("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")); // not present.

		// click first modify to get back to device page, verify and cancel message after removing selection, go back to provide additional info.
		VerifyRemoveMessage(xpathClickFirstModify, 0);
		
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanSelected();
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();		

		// click second modify to see  plan page, clear the list and verify the basic error.
		VerifyRemoveMessage(xpathClickSecondModify, 1);
		
		//go back to provide additional info page, select third modify, remove select plan option, go back to 
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();		
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		UnSelectOptionOne(); // plan option select remove.
		
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad(); // get to additional info page and verify no options shown. 	
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout);
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = finalListOfDevices.get(0).optionsList.get(0).split("-")[0].trim();
		Assert.assertTrue("", actual.contains(noOptionalFeaturesOptionsPage));
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// shipping info - TODO: accessory stuff   
	// click modify to go back to device page, create message, and basic verify
	// click modify to go back to plan page, create message, and basic verify
	// click modify to go back to plan option page, create message, and basic verify
	// do accessory --- see comments.
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestFour() throws Exception
	{
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		ChoosePlanPage.clickNextButton();
		
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.EnterMissingInfoPortNumber();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();

		// click first modify to get back to device page, verify and cancel message after removing selection, go back to shipping info
		VerifyRemoveMessage(xpathClickFirstModify, 0);
		
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanSelected();
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();		
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		
		// click second modify to get back to plan page, verify and cancel message after removing selection, go back to shipping info
		VerifyRemoveMessage(xpathClickSecondModify, 1);
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		UnSelectOptionOne();
		
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // need accessory added for next test
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad(); 
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout); // TODO: xpath variable
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = finalListOfDevices.get(0).optionsList.get(0).split("-")[0].trim();
		Assert.assertTrue("", actual.contains(noOptionalFeaturesOptionsPage));
		
		// go back to accessories and remove select accessory.
		driver.findElement(By.xpath("//a[text()='Add More']")).click();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathRemoveFromCart)).click();
		//driver.findElement(By.xpath(xpathAddToCartForIndexing + "[2]")).click(); // different accessory.
		
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad(); 
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		Thread.sleep(1500); // need wait time for accessory text to go away.
		Assert.assertFalse(CatchFail("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")); // not present.

		// select second accessory if the device from 'finalDeviceList' being used has more than one accessory.
		if(finalListOfDevices.get(0).accessoryList.size() > 1)
		{
			// go back to accessories and select second accessory.
			driver.findElement(By.xpath("//a[text()='Add More']")).click();
			Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
			driver.findElement(By.xpath(xpathAddToCartForIndexing + "[2]")).click(); // second accessory.
			
			ChooseAccessoriesPage.clickNextBtn();
			ProvideAdditionalInfoPage.WaitForPageToLoad(); 
			ProvideAdditionalInfoPage.clickNextBtn();
			EnterShippingInfoPage.WaitForPageLoad();

			actual = driver.findElement(By.xpath("//div[@ng-if='catalogs.accessory.selectedItems']/div[2]/div/div[1]")).getText();
			ShowText(actual);
			Assert.assertEquals(finalListOfDevices.get(0).accessoryList.get(1), actual);
		}
	}
	
	// get to verify order page. select to go back to device page. Depending upon the size of the finalListOfDevices size, select to change the device or delete 
	// the existing one. verify error message. continue to the verify order page and verify correct device name is shown.   
	public static void TestFiveDevice() throws Exception 
	{
		SetupAllToVerifyOrder();
		
		// verify correct device name.
		expected = finalListOfDevices.get(0).name;
		actual = driver.findElement(By.xpath("//div[text()='Device']/following-sibling::div[2]/div")).getText();
		Assert.assertEquals(actual, expected);
		
		driver.findElement(By.xpath(xpathClickFirstModifyVerify)).click(); // go to device page through modify button.
		ChooseDevicePage.WaitForPageToLoadDeviceSelected();
		
		// if the final device list has more than one device select the second device in the list to be the device for the Verify Order page.
		if(finalListOfDevices.size() > 1)
		{
			driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // this will select the second device. the first is already selected.
			VerifyWarnigIsPresentAndClose(true, 2); // verify pop-up and select OK.
			Thread.sleep(1500); // wait for pop-up to go away. 
			verifyPageTempIndex = 1; 
		}
		else
		{
			driver.findElement(By.xpath(xpathRemoveFromCart)).click(); 
			VerifyWarnigIsPresentAndClose(false, 0); // verify and cancel pop-up.
			Thread.sleep(1500); // wait for pop-up to go away.
			verifyPageTempIndex = 0;
		}
		
		ChooseDevicePage.clickNextButton();
		
		if(verifyPageTempIndex > 0) // need to re-add plan if new device was selected.
		{
			ChoosePlanPage.WaitForPageToLoadNoPlanSelected();
			driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click();			
		}
		else // no new device added, plan is already there.
		{
			ChoosePlanPage.WaitForPageToLoadPlanAndOptionSelected();
		}
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();
		assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		WaitForElementVisible(By.xpath("//div[text()='Device']/following-sibling::div[2]/div"), MediumTimeout);
		
		// verify correct device name.
		expected = finalListOfDevices.get(verifyPageTempIndex).name;
		actual = driver.findElement(By.xpath("//div[text()='Device']/following-sibling::div[2]/div")).getText();
		Assert.assertEquals(actual, expected);
	}
	
	// get to verify order page. select to go back to plan page. Select to delete the plan and verify/close basic error message.  
	// the existing one. verify error message. continue to the verify order page and verify correct device name is shown.   
	public static void TestFivePlan() throws Exception 
	{
		SetupAllToVerifyOrder();
		
		// go back to plan modify, select to delete plan, and verify basic message. select cancel.
		VerifyRemoveMessage(xpathClickSecondModifyVerify, 1);
		
		// remove plan from cart, select OK, and then add back. verify basic messages.
		WaitForElementClickable(By.xpath(xpathRemoveFromCart), ShortTimeout, "");
		driver.findElement(By.xpath(xpathRemoveFromCart + "[1]")).click(); // remove
		VerifyWarnigIsPresentAndClose(true, 1);
		Thread.sleep(1500); // wait for pop-up to close.
		
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // add plan back
		VerifyWarnigIsPresentAndClose(true, 3);
		Thread.sleep(1500); // wait for pop-up to close.
		
		driver.findElement(By.xpath(xpathClickSecondModify)).click(); // click modify to modify plan options.
		WaitForElementClickable(By.xpath(xpathPlanOptionOne), MediumTimeout, "");
		
		// select plan option to select. if
		if(finalListOfDevices.get(0).optionsList.size() > 1)  
		{
			verifyPageTempIndex = 1;
			driver.findElement(By.xpath(xpathPlanOptionTwo)).click();
		}
		else
		{
			verifyPageTempIndex = 0;
			driver.findElement(By.xpath(xpathPlanOptionOne)).click();
		}
		
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageToLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		
		WaitForElementVisible(By.xpath("(//span[@class='sn-field__value']/ul)[2]/li"), ExtremeTimeout - MainTimeout);
		Assert.assertEquals(driver.findElement(By.xpath("(//span[@class='sn-field__value']/ul)[2]/li")).getText(), finalListOfDevices.get(0).optionsList.get(verifyPageTempIndex));
	}
	
	// click modify in verify order page for accessories. change accessory and and verify change in verify order page.
	public static void TestFiveAccessories() throws Exception  
	{
		SetupAllToVerifyOrder();
		
		driver.findElement(By.xpath(xpathClickThirddModifyVerify)).click(); // select to go back to accessory page.
		ChooseAccessoriesPage.WaitForPageToLoadItemSelected(); 
		
		driver.findElement(By.xpath(xpathRemoveFromCart)).click(); // remove first item.
		
		
		// select accessory. if device on final list of devices has more than one accessory select a new one else put the same one back.
		if(finalListOfDevices.get(0).accessoryList.size() > 1)  
		{
			verifyPageTempIndex = 1;
			driver.findElement(By.xpath(xpathAddToCartForIndexing + "[2]")).click();
		}
		else
		{
			verifyPageTempIndex = 0;
			driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click();
		}
		
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoadPortNumber();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		
		WaitForElementVisible(By.xpath("//div[text()='Accessories']"), MainTimeout);
		actual = driver.findElement(By.xpath("(//div[contains(@class, 'tg-strong ng-binding')])[3]")).getText();
		expected = finalListOfDevices.get(0).accessoryList.get(verifyPageTempIndex);
		Assert.assertEquals(actual, expected);
	}

	// click modify in verify order page for additional info. change values and and verify changes in verify order page.
	public static void TestFiveProvideAddiionalInfo() throws Exception 
	{
		SetupAllToVerifyOrder();
		
		// select to go back to provide additional info
		driver.findElement(By.xpath(xpathClickFourthdModifyVerify)).click();
		ProvideAdditionalInfoPage.WaitForPageToLoadPortNumber();
		
		// change variables used to populate provide additional info page before re-populating and verify in verify orders.
		newServiceNumber = "8984543567";
		PlanInfoActions.carrierAccountNumber = "02347";
		contactNumber = "7818834148";
		extension = "7645";
		additionalInstructions = "Test Modify";

		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		//WaitForElementVisible(By.xpath("//div[text()='Additional Information']"), MainTimeout);
		//WaitForElementVisible(By.xpath("//div[text()='Shipping Information']"), MediumTimeout);
		
		VerifyOrderPage.VerifyAdditionalInformationTransferServiceInAndPort();
	}
	
	// click modify in verify order page for shipping. change line 1, 2, and 3 in shipping info, and verify changes in verify order page.   
	public static void TestFiveShippingInformation() throws Exception
	{
		SetupAllToVerifyOrder();
		
		// select to go back to shipping info page.
		driver.findElement(By.xpath(xpathClickFivedModifyVerify)).click();
		EnterShippingInfoPage.WaitForPageLoad();
		
		// change some fields.
		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_1']")).clear();
		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_2']")).clear();
		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_3']")).clear();

		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_1']")).sendKeys(lineOne);
		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_2']")).sendKeys(lineTwo);
		driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_3']")).sendKeys(lineThree);

		// go to verify order page and verify changes.
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		
		VerifyShippingInfo(driver.findElement(By.xpath("//div[@ng-if='orderConfirmation.shipTo']/table/tbody/tr[1]")).getText(), lineOne);
		VerifyShippingInfo(driver.findElement(By.xpath("//div[@ng-if='orderConfirmation.shipTo']/table/tbody/tr[2]")).getText(), lineTwo);
		VerifyShippingInfo(driver.findElement(By.xpath("//div[@ng-if='orderConfirmation.shipTo']/table/tbody/tr[3]")).getText(), lineThree);
	}
	
	// go to plan page, jump back to device page, attempt to delete device, do detailed verify of the error message. 
	public static void DetailedErrorsOne() throws Exception 
	{
		// get to plan page
		PopulatedDevicePlanPages();
		driver.findElement(By.xpath(xpathClickFirstModify)).click();
		ChooseDevicePage.WaitForPageToLoad();
		ClearListSelection();
		WaitForElementClickable(By.xpath("(//button[text()='Cancel'])[3]"), ShortTimeout, "");

		// store list items
		tempWebEleList = driver.findElements(By.xpath("//ul[@ng-if='catalogCompatibilityWarning.warnings']")); 

		// verify text message.
		Assert.assertEquals(detailedMessageArray[0], driver.findElement(By.xpath("//p[@class='ng-binding ng-scope'][1]")).getText());
		
		// verify 1 item in the list
		Assert.assertEquals(detailedMessageItems[0], tempWebEleList.get(0).getText());
	}

	// go to accessories, jump back to device page, attempt to delete device, do detailed verify of the error message.
	public static void DetailedErrorsTwo() throws Exception 
	{
		// get to plan page
		PopulatedDevicePlanPages();
		
		// go to accessories page, select accessory, use modify to go back to plan, 
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click();
		driver.findElement(By.xpath(xpathClickFirstModify)).click();
		ClearListSelection();
		WaitForElementClickable(By.xpath("(//button[text()='Cancel'])[3]"), ShortTimeout, "");

		// store list items
		tempWebEleList = driver.findElements(By.xpath("//ul[@ng-if='catalogCompatibilityWarning.warnings']/li")); 

		// verify text message.
		Assert.assertEquals(detailedMessageArray[0], driver.findElement(By.xpath("//p[@class='ng-binding ng-scope'][1]")).getText());
		
		// verify 2 items in the list
		Assert.assertEquals(detailedMessageItems[0], tempWebEleList.get(0).getText());
		Assert.assertEquals(detailedMessageItems[1], tempWebEleList.get(1).getText());		
	}
	
	// go to accessories, select accessory, go to provide additional info and enter info, jump back to device page using modify selection, 
	// attempt to delete device, do detailed verify of the error message.
	public static void DetailedErrorsThree() throws Exception 
	{
		// get to plan page
		PopulatedDevicePlanPages();
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // select accessory
		ChooseAccessoriesPage.clickNextBtn();
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		driver.findElement(By.xpath(xpathClickFirstModify)).click(); // click first modify
		ClearListSelection();
		WaitForElementClickable(By.xpath("(//button[text()='Cancel'])[3]"), ShortTimeout, "");

		// store list items
		tempWebEleList = driver.findElements(By.xpath("//ul[@ng-if='catalogCompatibilityWarning.warnings']/li")); 

		// verify text message.
		Assert.assertEquals(detailedMessageArray[0], driver.findElement(By.xpath("//p[@class='ng-binding ng-scope'][1]")).getText());
		
		// verify 2 items in the list
		Assert.assertEquals(detailedMessageItems[0], tempWebEleList.get(0).getText());
		Assert.assertEquals(detailedMessageItems[1], tempWebEleList.get(1).getText());		
		Assert.assertEquals(detailedMessageItems[2], tempWebEleList.get(2).getText());
	}

	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 															TEST BLOCKS/CALLS ABOVE
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// go through all device objects that have a plan in 'listOfDevices' and add items to the devices.
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	public static void PopulatedDeviceList() throws Exception 
	{
		List<WebElement> tempWebList = new ArrayList<WebElement>();
		
		ShowText(" *** Start PopulatedDeviceList");
		for(Device dv : listOfDevices)
		{
			// select a device in list of devices.
			WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]"), MediumTimeout, "");
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]")).click();
			System.out.println(dv.name  + " " + dv.index);
			
			// go through plan, accessory options, to accessories and mark each device that has each of these.   
			ChooseDevicePage.clickNextButton(); 
			
			ChoosePlanPage.WaitForPageToLoadPlanOrig();
			ChoosePlanPage.SelectFirstPlanIndexOne(); // select first plan
			ChoosePlanPage.clickNextButton();

			if(WaitForPlanOptions() > 0)
			{
				tempWebList = driver.findElements(By.xpath(xpathToOptionsList));
				for(WebElement ele : tempWebList)
				{
					dv.optionsList.add(ele.getText());
				}
			}
			
			ChoosePlanPage.clickNextButton();
			
			if(WaitForAccessoriesPage()) // see if there are accessories
			{
				tempWebList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
				for(WebElement ele : tempWebList)
				{
					dv.accessoryList.add(ele.getText());
				}
			}
			
			// go back to devices page
			ChooseAccessoriesPage.clickBackBtn();
			ChoosePlanPage.WaitForPageToLoadPlanSelected();
			ChoosePlanPage.clickBackButton();
			
			// clear device page and pop-up
			ClearListSelection();
			Thread.sleep(2000); // need pause here to allow pop-up to go away. if no pause, there is a click conflict. 
		}
	}

	// this starts at home page and sets up everything to verify orders.   
	public static void SetupAllToVerifyOrder() throws Exception
	{
		PopulatedDevicePlanPages();
		ChoosePlanPage.clickNextButton();
		assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // select an accessory.
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		//WaitForElementVisible(By.xpath("//div[text()='Device']/following-sibling::div[2]/div"), MediumTimeout);
		WaitForElementVisible(By.xpath("//div[text()='Additional Information']"), MainTimeout);
		WaitForElementVisible(By.xpath("//div[text()='Shipping Information']"), MediumTimeout);
	}

	public static void PopulatedDeviceListFromFile() throws Exception
	{
		ReadInFinalDeviceList();
	}

	public static void ReadInFinalDeviceList() throws IOException
	{
		String projFile = System.getProperty("user.dir");
		projFile = projFile.replace("\\", "/");
		String inputFile = projFile + "/DevicesOutFile.txt";
		BufferedReader in = new BufferedReader(new FileReader(inputFile));
		
		String propertyName = "";
		String tempName = "";
		String tempOptions = "";
		String tempAccessories = "";		
        String str;
        String [] strArray;
		int tempIndex = -1;        
        
		finalListOfDevices.clear();

		while ((str = in.readLine()) != null) 
        {
        	propertyName = str.split(":::")[0];
        	
        	switch(propertyName)
        	{
	        	case "name":
	        		tempName = str.split(":::")[1];
	        		break;
	        	case "index":
	        		tempIndex = Integer.parseInt(str.split(":::")[1]);
	        		break;
	        	case "OptionsList":
	        		tempOptions = str.split(":::")[1];
	        		break;
	        	case "AccessoriesList": // this is the last item read in for one block of information for a device.
	        		tempAccessories = str.split(":::")[1];
	        		
	        		Device tempDevice = new Device(tempName, tempIndex);
	        		
	        		// build list for plan options and put in Device object	
	        		strArray = tempOptions.split(",");
	        		for(String strLoop : strArray){tempDevice.optionsList.add(strLoop);}
 
	        		// build list for accessories and put in Device object	
	        		strArray = tempAccessories.split(",");
	        		for(String strLoop : strArray){tempDevice.accessoryList.add(strLoop);}

	        		finalListOfDevices.add(tempDevice); // add the device that was setup.
	        		
	        		break;	        		
	        	default:
	        		Assert.fail("Method 'ReadInFinalDeviceList' in 'ModifySelectionsTesting' class has bad input in input file.");
        	}

        }
        in.close();
        
        //for(Device de : finalListOfDevices){de.Show();}
	}
	
	
	public static void CreateFinalDeviceList()
	{
		ShowText(" *** Start CreateFinalDeviceList");
		for(Device dev : listOfDevices)
		{
			if (dev.accessoryList.size() > 0 && dev.optionsList.size() > 0)
			{
				finalListOfDevices.add(dev);
			}
		}
		Assert.assertTrue("Final device list is incomplete. Test can not be run.", finalListOfDevices.size() > 1);
	}

	public static void OutputFinalDeviceListToFile() throws IOException
	{
		String projFile = System.getProperty("user.dir");
		projFile = projFile.replace("\\", "/");
		
		File file = new File(projFile +   "/DevicesOutFile.txt");
        output = new BufferedWriter(new FileWriter(file));
		
		for(Device dev : finalListOfDevices)
		{
			dev.OutputToFile(output);
		}
		output.close();		
	}
	
	// special for loading device list.
	public static boolean WaitForAccessoriesPage()
	{
		try
		{
			WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout - ShortTimeout, "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static int WaitForPlanOptions()
	{
		try
		{
			List<WebElement> eleList = driver.findElements(By.xpath(xpathToOptionsList));			
			return eleList.size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	// //////////////////////////////////////////////////////
	// 						objects
	// //////////////////////////////////////////////////////
	public static class Device
	{
		public String name = "";
		public String outFileName = ""; 
		public int index = -1;
		public boolean isFirstLoop = true;
		public List<String> optionsList = new ArrayList<String>();  
		public List<String> accessoryList = new ArrayList<String>();
		
		Device(String nm, int ind)
		{
			name = nm;
			index = ind;
		}
		
		public void Show()
		{
			System.out.println("------------------------ ");
			System.out.println("name =  " + name);
			System.out.println("index =  " + index);
			System.out.println("options list size =  " + optionsList.size());
			System.out.println(optionsList);
			System.out.println(accessoryList);
		}
		
		public void OutputToFile(BufferedWriter fileHandle) throws IOException
		{
		    String lineFeed = "\r\n";
		    String strOptionsList = "";
		    String strAccessoriesList = "";		    
			fileHandle.write("name:::" + name + lineFeed);
			fileHandle.write("index:::" + index + lineFeed);
			for(String str : optionsList)
			{
				if(isFirstLoop)
				{
					strOptionsList += str.trim();					
				}
				else
				{
					strOptionsList += "," + str.trim();					
				}
				isFirstLoop = false;
			}
			
			isFirstLoop = true;
			for(String str : accessoryList)
			{
				if(isFirstLoop)
				{
					strAccessoriesList += str.trim();
				}
				else
				{
					strAccessoriesList += "," + str.trim();					
				}
				isFirstLoop = false;
			}
			
			fileHandle.write("OptionsList:::" + strOptionsList + lineFeed);
			fileHandle.write("AccessoriesList:::" + strAccessoriesList + lineFeed);
			
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////
	//    
	// Problem with selecting 'Remove' in accessory page
	// 
	// ////////////////////////////////////////////////////////////////////////
	public static void TestThreeAccessoryProblemWithRemove() throws Exception  
	{
		//String []  strArray;
		String tempString = "";
		
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
	
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		
		// add an accessory
		// remove accessory with remove selection - verify 

		// verify no accessories added, add accessory, and remove it using remove link.
		//System.out.println(WaitForElementNotClickableNoThrow(By.xpath("//a[text()='Remove']"), ShortTimeout, ""));
		Assert.assertTrue(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]")).getText().contains("Cost Monthly")); // no accessories
		
		
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // add
		WaitForElementClickable(By.xpath("//span/a[text()='Remove']"), ShortTimeout, "");
		tempString = finalListOfDevices.get(0).accessoryList.get(0);
		Assert.assertTrue(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")).getText().contains(tempString)); // verify add
		//System.out.println(WaitForElementNotClickableNoThrow(By.xpath("//a[text()='Remove']"), ShortTimeout, ""));		
		Pause("ready remove");
		driver.findElement(By.xpath("//span/a[text()='Remove']")).click(); // remove accessory
		Pause("ready remove done ???");
		DebugTimeout(2, "");
		//System.out.println(WaitForElementNotClickableNoThrow(By.xpath("//a[text()='Remove']"), ShortTimeout, ""));
		//Assert.assertTrue(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]")).getText().contains("Cost Monthly")); // no accessories
		//  ?? Assert.assertFalse(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")).getText().contains(tempString)); // verify add
		Pause("");
		ShowText(driver.findElement(By.xpath("//h3[@class='sn-h3']/following-sibling::div[4]/div[2]/div")).getText());
	}
}
