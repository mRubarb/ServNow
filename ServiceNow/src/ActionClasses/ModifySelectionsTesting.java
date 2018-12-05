package ActionClasses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

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
	
	public static List<Device> listOfDevices = new ArrayList<Device>();
	
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
		
		SpecialInstructions.checkAllOptions();
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
		
		ClearDeviceSelections();
		FindDevicesWithAssessories();
	}
	
	public static void FindDevicesWithAssessories() throws Exception
	{

		ShowText(" *** Start FindDevicesWithAssessories()  \r\n");
		for(Device dv : listOfDevices)
		{
			WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]"), MediumTimeout, "");
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]")).click();
			System.out.println(dv.name  + " " + dv.index);
			ChooseDevicePage.clickNextButton(); 
			ChoosePlanPage.WaitForPageToLoadPlanOrig();
			ChoosePlanPage.SelectFirstPlanIndexOne();
			ChoosePlanPage.clickNextButton();	
			ChoosePlanPage.clickNextButton();
			Pause("Accessories");
			ChooseAccessoriesPage.clickBackBtn();
			ChoosePlanPage.WaitForPageToLoadPlanSelected();
			ChoosePlanPage.clickBackButton();
			ClearDeviceSelections();
			Pause("Where?");
			WaitForElementClickable(By.xpath("//button[text()='OK']"), ShortTimeout, "");
			driver.findElement(By.xpath("//button[text()='OK']")).click();

			Pause("");
		}
	}
	
	public static void ClearDeviceSelections()
	{
		String buttonXpath = "//button[text()='Remove from Cart']";
		WaitForElementClickable(By.xpath(buttonXpath), MediumTimeout, "");
		driver.findElement(By.xpath(buttonXpath)).click();
	}
	
	public static class Device
	{
		public String name = "";
		public int index = -1;
		public boolean hasAccessory = false;
		
		Device(String nm, int ind)
		{
			name = nm;
			index = ind;
		}
	}	
	
	
	
}
