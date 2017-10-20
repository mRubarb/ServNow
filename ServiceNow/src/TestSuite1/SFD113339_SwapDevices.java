package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.SwapDevice;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.BaseClass;


public class SFD113339_SwapDevices extends BaseClass {
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		
	}

	@Test
	public static void sfd113339_SwapDevices() throws Exception
	{

		BaseClass.stepComplete("------------------ Starting Swap Devices - SFD 113339 -----------------------", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		
		 
		SwapDevice.runSwapDevices_SFD113339();
		

		CommonTestSteps.Logout();
		
				
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}
