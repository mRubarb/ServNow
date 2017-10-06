package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.SettingsPage;
import ActionClasses.Features;

public class TC0035Feature10 extends BaseClass 
{
	static boolean testPassed = false;
	
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0035Feature10()throws Exception
	{
		// login as admin user.
		CommonTestSteps.LoginAdmin();
		CommonTestSteps.GoToAdminSettings();
		
		SettingsPage.SetCheckboxesTrue();
		SettingsPage.SelectFeature10();
		
		CommonTestSteps.Logout();
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		Features.RunFeature10();

		CommonTestSteps.Logout();
		CommonTestSteps.LoginAdminBrowserOpen();
		CommonTestSteps.GoToAdminSettings();
		
		SettingsPage.SetCheckboxesTrue();

		
		testPassed =  true;
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		if(!testPassed)
		{
			// Features.CloseClean(); // not worth the time.
			CommonTestSteps.Logout();
			CommonTestSteps.LoginAdminBrowserOpen();
			CommonTestSteps.GoToAdminSettings();
			SettingsPage.SetCheckboxesTrue();			
		}
		
		
		System.out.println("Close Browser.");
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}




