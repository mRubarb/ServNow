package TestSuite1;

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

public class TC0039CommandUpdatesSubmittedOrder extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void TC0039CommandUpdatesSubmittedOrder()throws Exception
	{

		CommonTestSteps.LoginAdmin();
		
		CommonTestSteps.GoToTangoeMobilityOrderRequests();
		
		WaitForElementVisible(By.cssSelector(".navbar-title.list_title"), ExtremeTimeout - MainTimeout);
		
		Pause("");
		
		//SideBar.adminClickTangoeMobilityOrderRequests();

		/*
		// (//a[text()='Awaiting Approval'])[1]
		Frames.switchToGsftMainFrame();
		
		
		ShowPopup("wait");
		
		
		driver.findElement(By.xpath("(//a[text()='Awaiting Approval'])[1]")).click();
		
		DebugTimeout(9999, "Freeze");
		
		WaitForElementClickable(By.id("sysparm_search"), MainTimeout , "Failed wait for top frame search box."); // this waits for an item in the top window.
		Frames.switchToGsftMainFrame();
		WaitForElementVisible(By.xpath("//h2[normalize-space()='User Administration']"), MainTimeout);
		Frames.switchToGsftNavFrame();		
		*/
		
		// this does this. 
		// 1) run through the deactivate action
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page after su
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		/*
		DeactivateService.RunDeactivateService();
		
		CommonTestSteps.Logout();

		BaseClass.stepComplete("Run deactivate action complete. Now will approve the order.", "");


		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the deactivate order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.ApprovalAction(ApproverAction.approve);
		
		CommonTestSteps.Logout();

		BaseClass.stepComplete("Order has been approved. Now will verify limited user's details in 'my orders' page and order details psge.", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
		
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		DeactivateService.VerifyLimitedUserOrderMyOrdersMainPage();

		// this verifies the order details under the order that was verified in 'my orders' page. 
		DeactivateService.VerifyOrderDetailsPagePostApproval();

		BaseClass.stepComplete("Deactivate Service Test Complete.", "");
		
		testCaseStatus = true;
		*/
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		//if(orderDetailsObjectExpected != null)
		//{
		//	orderDetailsObjectExpected.Show();			
		//}
	    // JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}




