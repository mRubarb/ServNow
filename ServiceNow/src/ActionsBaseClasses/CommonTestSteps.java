package ActionsBaseClasses;

import org.openqa.selenium.By;

import ServiceNow.Approvals;
import ServiceNow.BaseClass;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.LoginPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.MyServicesPage;
import ServiceNow.SideBar;

public class CommonTestSteps extends BaseClass 
{
	public static void LoginLimitedUser() throws Exception
	{
		userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		setUpDriver(DriverSetup.ServiceNow);

		// Log into ServiceNow as test user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForNonAdminMainPageLoad();
	}
	
	public static void LoginLimitedUserBrowserOpen() throws Exception
	{
		userLoginMode = UserLoginMode.Limited; // setup login for admin user.

		// Log into ServiceNow as test user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForNonAdminMainPageLoad();
	}	
	
	public static void LoginAdmin() throws Exception  // jnupp
	{
		userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		setUpDriver(DriverSetup.ServiceNow);

		// Log into ServiceNow as test user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForAdminMainPageLoad();
	}
	
	public static void LoginAdminBrowserOpen() throws Exception  
	{

		userLoginMode = UserLoginMode.Admin; // setup login for limited user.
		
		// Log into ServiceNow as test user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForAdminMainPageLoad();
	}
	
	public static void Logout() throws Exception
	{
		Frames.switchToDefaultFrame();
		WaitForElementClickable(By.xpath("//button[text()='Logout']"), MainTimeout, "Failed to find logout button in CommonTestSteps.Logout.");
		driver.findElement(By.xpath("//button[text()='Logout']")).click();
		Frames.switchToGsftMainFrame();
		WaitForElementClickable(By.xpath("//button[text()='Login']"), MainTimeout, "Failed to find logout button in CommonTestSteps.Logout.");		
	}
	
	public static void GoToDevicesPage() throws Exception
	{
		// get to main side bar frame.
		Frames.switchToGsftNavFrame();		
		SideBar.clickHomeButton();
		
		// wait for home page to load. in 'My Services' section choose deactivate service action. 
		Frames.switchToGsftMainFrame(); 
		HomePage.WaitForPageToLoad();
		HomePage.SelectViewDevices();	
	}

	public static void GoToHomePage() throws Exception
	{
		Frames.switchToGsftNavFrame(); // get to main side bar frame.		
		SideBar.clickHomeButton();
		Frames.switchToGsftMainFrame(); // get to main frame. 
		HomePage.WaitForPageToLoad();
	}
	
	// Added by Ana - Oct 12
	public static void goToServicesPage() throws Exception
	{
		// get to main side bar frame.
		Frames.switchToGsftNavFrame();		
		SideBar.clickMyServicesBtn();
		
		// wait for My Services page to load 
		Frames.switchToGsftMainFrame(); 
		MyServicesPage.WaitForPageToLoad();
		
	}
	
	
	public static void GoToMyOrders() throws Exception
	{
		// select side bar 'all my orders'.
		Frames.switchToGsftNavFrame();
		SideBar.clickAllMyOrders();
		Frames.switchToGsftMainFrame();
		MyOrdersPage.WaitForPageToLoad();
	}

	public static void GoToAdminSettings() throws Exception // jnupp
	{
		// select side bar 'all my orders'.
		Frames.switchToGsftNavFrame();
		SideBar.clickAdminSettings();
		Frames.switchToGsftMainFrame();
	}
	
	public static void GoToTangoeMobilityOrderRequests() throws Exception 
	{
		// select side bar 'TangoeMobilityOrderRequests'.
		Frames.switchToGsftNavFrame();
		SideBar.adminClickTangoeMobilityOrderRequests();
		Frames.switchToGsftMainFrame();
	}
	
	public static void LoginApprover() throws Exception
	{
		// login as approver and approve the order.
		setUpDriver(DriverSetup.ServiceNow);
		userLoginMode = UserLoginMode.Approver; // setup login for approver user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForNonAdminMainPageLoad();
	}	
	
	public static void LoginApproverBrowserOpen() throws Exception
	{
		// login as approver and approve the order.
		userLoginMode = UserLoginMode.Approver; // setup login for approver user.
		Frames.switchToGsftMainFrame();
		LoginPage.Login();
		
		// this waits for main page (page after login) to load.
		Frames.switchToGsftMainFrame();
		WaitForNonAdminMainPageLoad();
	}	
	
	public static void GoToMyApprovalsPage() throws Exception
	{
		// select side bar approve.
		Frames.switchToGsftNavFrame();
		SideBar.clickApprovalButton();

		// wait for approve list to load.
		Frames.switchToGsftMainFrame();
		Approvals.WaitForPageToLoad();
	}		
}
