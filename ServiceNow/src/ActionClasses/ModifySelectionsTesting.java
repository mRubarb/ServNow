package ActionClasses;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.SelectCurrentCarrier;
import ServiceNow.SelectRegionPage;
import ServiceNow.SideBar;
import ServiceNow.SpecialInstructions;

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

	public static List<Device> listOfDevices = new ArrayList<Device>(); 
	public static List<Device> finalListOfDevices = new ArrayList<Device>();

	int deviceListSize = -1;
	
	// load the devices and their index that have plans.
	public static void LoadDeviceData() throws Exception
	{
		// go to home page
		Frames.switchToGsftNavFrame();		
		SideBar.clickHomeButton();
		
		// Click 'Transfer my phone number'. You are in the first step of the process: Select Region.
		Frames.switchToGsftMainFrame(); // switch frame so waits can work. 
		HomePage.WaitForPageToLoad();
		HomePage.clickTransferServiceInButton();

		SelectRegionPage.selectCountryFromDropDown(); // setup postal code
		SelectRegionPage.fillPostalCodeTextBox("01803");
		SelectRegionPage.clickNextButtonSelectRegion(); // move to device select page.
		
		SelectCurrentCarrier.selectCurrentCarrier(currentCarrierLocal); // Select current carrier from drop-down list
		SelectCurrentCarrier.selectNewCarrier(newCarrierLocal); // Select a new carrier - service will be moved to a different carrier.
		SelectCurrentCarrier.clickNextButton();
		
		SpecialInstructions.checkAllOptions(); // accept message 
		SpecialInstructions.clickNextButton();
		
		ChooseDevicePage.WaitForPageToLoad();
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
	}
	
	public static void TestOne()
	{
		JumpToDevicesPage(); // expecting selected page to be past 
		ClearListSelection();
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// go through all device objects that have a plan in 'listOfDevices' and add items to the devices.
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	public static void PopulatedDeviceList() throws Exception
	{

		ShowText(" *** Start FindDevicesWithAssessories()  \r\n");
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
				dv.optionsList = driver.findElements(By.xpath(xpathToOptionsList));
			}
			
			ChoosePlanPage.clickNextButton();
			
			if(WaitForAccessoriesPage()) // see if there are accessories
			{
				dv.accessoryList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
				// for(WebElement ele : dv.accessoryList){ShowText(ele.getText());}
			}
			
			ChooseAccessoriesPage.clickBackBtn();
			ChoosePlanPage.WaitForPageToLoadPlanSelected();
			ChoosePlanPage.clickBackButton();

			ClearListSelection();
			
			WaitForElementClickable(By.xpath("//button[text()='OK']"), MediumTimeout, "");
			driver.findElement(By.xpath("//button[text()='OK']")).click();
			
			Thread.sleep(2000); // need pause here to allow pop-up to go away. if no pause, there is a click conflict. 
		}
	}
	
	public static void CreateFinalDeviceList()
	{
		for(Device dev : listOfDevices)
		{
			if (dev.accessoryList.size() > 0 && dev.optionsList.size() > 0)
			{
				finalListOfDevices.add(dev);
			}
		}
		Assert.assertTrue("Final device list is incomplete. Test can not be run.", finalListOfDevices.size() > 1);
	}
	
	public static void JumpToDevicesPage()
	{
		WaitForElementClickable(By.xpath("//div[text()='Choose a Device']"), MediumTimeout, "");
		driver.findElement(By.xpath("//div[text()='Choose a Device']")).click();
	}
	
	public static void ClearListSelection()
	{
		String buttonXpath = "//button[text()='Remove from Cart']";
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
		public int index = -1;
		public List<WebElement> optionsList = new ArrayList<WebElement>();  
		public List<WebElement> accessoryList = new ArrayList<WebElement>();
		
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
			System.out.println("accessory list size =  " + accessoryList.size());
		}
	}	
}
