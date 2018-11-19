package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.UpgradeDevice;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;
import ServiceNow.BaseClass.ApprovalActionType;
import ServiceNow.BaseClass.ApproverAction;

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
		//CommonTestSteps.GoToDevicesPage();
		
		//CommonTestSteps.goToServicesPage();
		
		//CommonTestSteps.GoToMyOrders();

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
