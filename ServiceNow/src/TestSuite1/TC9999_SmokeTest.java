package TestSuite1;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.DevicePage;

public class TC9999_SmokeTest extends BaseClass
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

		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		WaitForElementVisible(By.xpath("(//button[text()='View Device'])[1]"), ExtremeTimeout - MainTimeout);
		
		CommonTestSteps.goToServicesPage();
		BaseClass.DebugTimeout(2, "");
		
		CommonTestSteps.GoToHomePage();
		
		CommonTestSteps.GoToMyOrders();
		
		CommonTestSteps.Logout();
		

		BaseClass.stepComplete("Smoke Test Complete.", "");
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		if(orderDetailsObjectExpected != null)
		{
		//	orderDetailsObjectExpected.Show();			
		}
	    // JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}	
	
	
}
