package ActionClasses;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.NeedsLocalLogs;

import com.google.common.base.Verify;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.ModifyOptions;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;

public class ModifySelectionsTesting extends BaseClass 
{

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE !!! - the carrier variables below are for this test only. This combination makes a large list of devices that can be added.
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String currentCarrierLocal = "Sprint";
	public static String newCarrierLocal = "Verizon Wireless";
	public static String xpathToOptionsList = "//h3[text()='Optional Features']/following-sibling::ul/li";
	public static String xpathClickFirstModify = "(//a[text()='Modify'])[1]";
	public static String xpathClickSecondModify = "(//a[text()='Modify'])[2]";
	public static String xpathClickThirdModify = "(//a[text()='Modify'])[3]";
	public static String xpathPlanOptionOne = "(" + xpathToOptionsList + ")[1]";
	public static String xpathAddToCartForIndexing = "(//button[text()='Add to Cart'])";
	
	public static String xpathClickFirstModifyVerify = "(//button[text()='Modify'])[1]";
	
	//(//button[text()='Add to Cart'])[
	public static String lineFeed = "\r\n";
	public static String noOptionalFeaturesOptionsPage = "No optional features have been selected";
	public static String actual = "";
	public static String expected = "";
	
	
	public static String [] messageArray = 
	{
		"Removing device will empty your shopping cart.", // device
		"Removing your plan will also remove any related additional info you entered previously." // plan
	};
    
	public static List<Device> listOfDevices = new ArrayList<Device>(); 
	public static List<Device> finalListOfDevices = new ArrayList<Device>();

	int deviceListSize = -1;
	
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
	// select modify in plan page, select the first option, go back to device page, and verify it shows up in the device page. go back to 
	// plan page, select 'Modify' to get to devices page, select new device, go to plan page, select plan, and verify new device is in plan page.
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestOne() throws Exception 
	{
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		
		ChoosePlanPage.clickBackButton(); // go back to devices page
		ChooseDevicePage.WaitForPageToLoad();
		
		// verify the proper plan optional feature selected through the modify selection is present in the Plan Features section.
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
		Thread.sleep(2000); // need pause here to allow pop-up to go away. if no pause, there is a click conflict.
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
	// go to accessory page,  option using modify, and verify in accessory page   
	// go to accessory page, select plan using modify,  plan, verify error on next, add plan, next, verify three modifies are present.
	// from accessory page, go to devices page using Modify selection in accessory page,  the device selection, verify and  warning message.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void TestTwo() throws Exception 
	{
		// go to devices page, select first device in devices list and select the first plan.
		// click modify for plan optional features and add the first option.
		PopulatedDevicePlanPages();
		
		// go to the accessories page, select modify for plan options, remove the option, go forward to the
		// accessory page and verify there are no options. 
		ChoosePlanPage.clickNextButton();
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories()); // accessories page should have at least one accessory. 
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
		WaitForAccessoriesPage();

		// click first modify to get back to device page, verify and cancel message after removing selection.
		VerifyRemoveMessage(xpathClickFirstModify, 0);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE: removing accessory from 'remove selection had problems. see 'TestThreeAccessoryProblemWithRemove()'   
	// Provide additional info
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

		VerifyRemoveMessage(xpathClickSecondModify, 1);
		
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton();		
		Assert.assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		UnSelectOptionOne();
		
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
	// 
	// 
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
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad(); // get to additional info page and verify no options shown.
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout);
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = finalListOfDevices.get(0).optionsList.get(0).split("-")[0].trim();
		Assert.assertTrue("", actual.contains(noOptionalFeaturesOptionsPage));
		//ShowText(actual);
		//Pause("passed.....");
	}
	
	public static void TestFive() throws Exception // bladd
	{
		PopulatedDevicePlanPages();
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
		
		expected = finalListOfDevices.get(0).name;
		actual = driver.findElement(By.xpath("//div[text()='Device']/following-sibling::div[2]/div")).getText();
		Assert.assertEquals(actual, expected);
		
		VerifyRemoveMessage(xpathClickFirstModifyVerify, 0);
		
		//driver.findElement(By.xpath("//div[text()='Device']/following-sibling::div[2]/div")).getText();
		
		//div[text()='Device']/following-sibling::div[2]/div
		Pause("");
	}
	
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
			CloseDevicePageWarningPopUp(true);
			Thread.sleep(2000); // need pause here to allow pop-up to go away. if no pause, there is a click conflict. 
		}
	}

	// verify test in remove device warning and close the warning 
	public static void VerifyWarnigIsPresentAndClose(boolean trueClose, int messageIndex) throws Exception
	{
		WaitForElementVisible(By.xpath("//p[text()='" + messageArray[messageIndex]  + "']"), ShortTimeout);
		//WaitForElementVisible(By.xpath("//p[text()='Removing device will empty your shopping cart.']"), ShortTimeout);		
		if(trueClose)
		{
			CloseDevicePageWarningPopUp(true);
		}
		else
		{
			CloseDevicePageWarningPopUp(false);
		}
	}

	// click first modify to get back to device page, verify and cancel message after removing selection, go back to provide additional info.	
	public static void VerifyRemoveMessage(String modifyXpath, int errorMessageIndex) throws Exception
	{
		driver.findElement(By.xpath(modifyXpath)).click();
		ClearListSelection();
		VerifyWarnigIsPresentAndClose(false, errorMessageIndex);
		Thread.sleep(1500); // wait for pop-up to go away to avoid click conflict 
	}
	
	public static void PopulatedDeviceListFromFile() throws Exception
	{
		ReadInFinalDeviceList();
	}

	public static boolean CatchFail(String xpath)
	{
		try
		{
			ShowText(driver.findElement(By.xpath(xpath)).getText());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static void UnSelectOptionOne() throws Exception
	{
		// click third modify to get back to option page, remove selected option, go forward to additional info and verify removed option.    
		driver.findElement(By.xpath(xpathClickThirdModify)).click();
		WaitForElementPresent(By.xpath(xpathPlanOptionOne), ShortTimeout);
		driver.findElement(By.xpath(xpathPlanOptionOne)).click(); // select first option to remove selection
	}
	
	// go to devices page, select first device in devices list and select the first plan.
	// click modify for plan optional features and add the first option.
	public static void PopulatedDevicePlanPages() throws Exception
	{
		// go to devices page, select first device in devices list and select the first plan.
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(0).index + 1 ) + "]")).click();
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanOrig();
		ChoosePlanPage.SelectFirstPlan();
		
		// click modify for plan optional features and add the first option.
		WaitForElementClickable(By.xpath(xpathClickSecondModify), MediumTimeout, "");
		driver.findElement(By.xpath(xpathClickSecondModify)).click(); // select modify
		WaitForElementPresent(By.xpath(xpathPlanOptionOne), ShortTimeout);
		driver.findElement(By.xpath(xpathPlanOptionOne)).click(); // select first option
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
	
	
	public static void CloseDevicePageWarningPopUp(boolean trueOk) throws Exception
	{
		if(trueOk)
		{
			WaitForElementClickable(By.xpath("//button[text()='OK']"), MediumTimeout, "");
			driver.findElement(By.xpath("//button[text()='OK']")).click();
		}
		else
		{
			WaitForElementClickable(By.xpath("(//button[text()='Cancel'])[3]"), MediumTimeout, "");
			driver.findElement(By.xpath("(//button[text()='Cancel'])[3]")).click();
		}
	}
	
	public static void JumpToDevicesPage()
	{
		WaitForElementClickable(By.xpath("//div[text()='Choose a Device']"), MediumTimeout, "");
		driver.findElement(By.xpath("//div[text()='Choose a Device']")).click();
	}
	
	public static void ClearListSelection()
	{
		String buttonXpath =  xpathRemoveFromCart;   // "//button[text()='Remove from Cart']";
		try
		{
			WaitForElementClickable(By.xpath(buttonXpath), MediumTimeout, "");
			driver.findElement(By.xpath(buttonXpath)).click();
		}
		catch(Exception e)
		{
			
		}
	}
	
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
