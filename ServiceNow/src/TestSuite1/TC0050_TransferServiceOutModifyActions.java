package TestSuite1;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;

public class TC0050_TransferServiceOutModifyActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void TC9999() throws Exception 
	{
		BaseClass.stepComplete("------------------ Starting Smoke Test. -----------------------", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		CommonTestSteps.GoToHomePage();
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
