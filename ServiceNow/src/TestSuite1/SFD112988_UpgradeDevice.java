package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.UpgradeDevice;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.BaseClass;


public class SFD112988_UpgradeDevice extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		
	}

	@Test
	public static void tc0030UpgradeDeviceApprove() throws Exception
	{
		BaseClass.stepComplete("------------------ Starting Upgrade Device - SFD 112988 -----------------------", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();

		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();

		UpgradeDevice.runUpgradeDevice_SFD112988();
		
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
