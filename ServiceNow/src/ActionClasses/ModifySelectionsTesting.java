package ActionClasses;

import ServiceNow.BaseClass;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.SelectRegionPage;
import ServiceNow.SideBar;

public class ModifySelectionsTesting extends BaseClass 
{

	// get to choose a device page in transfer service in and port.
	public static void VerifyModifySelections() throws Exception
	{
		// go to home page
		Frames.switchToGsftNavFrame();		
		SideBar.clickHomeButton();
		
		// 3. Click 'Transfer my phone number'. You are in the first step of the process: Select Region.
		Frames.switchToGsftMainFrame(); // switch frame so waits can work. 
		HomePage.WaitForPageToLoad();
		HomePage.clickTransferServiceInButton();

		SelectRegionPage.selectCountryFromDropDown();
		SelectRegionPage.fillPostalCodeTextBox("01803");
		SelectRegionPage.clickNextButtonSelectRegion(); // move to device select page.
		
		// add  device that has a plan in the plans list. 
		boolean planForDevice = false; 
		while (!planForDevice)
		{
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			ChooseDevicePage.clickNextButton();
			planForDevice = ChoosePlanPage.waitForPageToLoadPlans();
		}
		
		ShowInt(indexDeviceSelectedNewActivation);
		Pause("Wait --------------------");
		
		
		
	}
	
	
	
}
