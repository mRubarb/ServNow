package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.ModifySelectionsTesting;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;

public class TC0051_TransferServiceInAndPortModifyActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void TC0051_TransferServiceInAndPortModifyActionsTest() throws Exception 
	{
		BaseClass.stepComplete("------------------ Starting Smoke Test. -----------------------", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();

		ModifySelectionsTesting.VerifyModifySelections();
		Pause("");
		
		
		CommonTestSteps.Logout();
		//BaseClass.stepComplete("Smoke Test Complete.", "");
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
