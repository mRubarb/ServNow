package TestSuite1;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.ModifySelectionsTesting;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;

public class TC0051_TransferServiceInAndPortModifyActionsDetailedErrors extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void TC0051_TransferServiceInAndPortModifyActionsDetailedErrorsTest() throws Exception 
	{
		// login as limited user.
		CommonTestSteps.LoginLimitedUser();

		// create a list of device to be used for testing. all devices have at least one plan, plan option, and accessory.
		//ModifySelectionsTesting.LoadDeviceData();   
		ModifySelectionsTesting.PopulatedDeviceListFromFile();
		
		ModifySelectionsTesting.DetailedErrorsOne();
		ShowText("DetailedErrorsOne complete.");

		ModifySelectionsTesting.DetailedErrorsTwo();
		ShowText("DetailedErrorsTwo complete.");

		ModifySelectionsTesting.DetailedErrorsThree();
		ShowText("DetailedErrorsThree complete.");
		
		CommonTestSteps.Logout();
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
	    //JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}		
	
	
	
}