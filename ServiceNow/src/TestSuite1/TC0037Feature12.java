package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.SettingsPage;
import ActionClasses.Features;

public class TC0037Feature12 extends BaseClass 
{
	static boolean testPassed = false;
	
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0037Feature12()throws Exception
	{
		// login as admin user.
		CommonTestSteps.LoginAdmin();
		CommonTestSteps.GoToAdminSettings();

		// make sure check-boxes are all checked for feature 10 to 13.
		SettingsPage.SetCheckboxesTrue();
		CommonTestSteps.Logout(); 

		CommonTestSteps.LoginLimitedUserBrowserOpen();
		// run test for feature 12 checked. 
		Features.RunFeature12(Features.checkBoxState.checked);
		
		CommonTestSteps.Logout();
		
		// uncheck feature 12 check-box.
		CommonTestSteps.LoginAdminBrowserOpen();
		CommonTestSteps.GoToAdminSettings();
		SettingsPage.SelectFeature12();
		CommonTestSteps.Logout();
		
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// run test for feature 12 not checked.
		Features.RunFeature12(Features.checkBoxState.notChecked);
		
		CommonTestSteps.Logout();

		// set feature 10 to 13 check-boxes true.
		CommonTestSteps.LoginAdminBrowserOpen();
		CommonTestSteps.GoToAdminSettings();
		SettingsPage.SetCheckboxesTrue();

		CommonTestSteps.Logout(); // remove this - not supposed to be here.
		testPassed =  true;
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		if(!testPassed) // if testing didn't go all the way through, make sure features 10 to 13 are all disabled in admin settings.
		{
			Features.CloseClean();
		}
		
		System.out.println("Close Browser.");
	    //JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}




