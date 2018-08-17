package TestSuite1;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.DeactivateService;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;
import ServiceNow.Frames;
import ServiceNow.SideBar;
import ServiceNow.TangoeMobilityOrderRequestsPage;

public class TC0039CommandUpdatesSubmittedOrder extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void TC0039CommandUpdatesSubmittedOrderTest()throws Exception
	{
		
		// TangoeMobilityOrderRequestsPage.Foo();

		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		
		DeactivateService.RunDeactivateServiceSubmitOnly();
		
		CommonTestSteps.Logout();
		
		CommonTestSteps.LoginAdminBrowserOpen();
		//CommonTestSteps.LoginAdmin();
		
		CommonTestSteps.GoToTangoeMobilityOrderRequests();
		
		TangoeMobilityOrderRequestsPage.VerifyNeededColumnsPresent();

		TangoeMobilityOrderRequestsPage.ObserveStartingStates();

		TangoeMobilityOrderRequestsPage.StartPolling();
		//Pause("Test Passed.");
		

		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		//if(orderDetailsObjectExpected != null)
		//{
		//	orderDetailsObjectExpected.Show();			
		//}
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}




