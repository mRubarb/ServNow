package testSuiteDefects;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import HelperObjects.DeviceInfoActions;
import ServiceNow.BaseClass;
import ServiceNow.MyDevicesPage;
import ServiceNow.TangoeMobilityOrderRequestsPage;
import ActionClasses.DeactivateService;

public class SFD113063_DeactivateNoService extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void SFD113063_DeactivateNoServiceTest()throws Exception
	{
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.SelectFirstDeviceWithNoService();

		Pause("");
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
