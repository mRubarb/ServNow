package TestSuite1;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.SuspendService;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;

public class TC0023SuspendReject extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0023Home()throws Exception
	{
		BaseClass.stepComplete("------------------ Starting suspend action with reject. -----------------------", "");

		approverAction = ApproverAction.reject; // setup enum for indicating approval type.		

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.		
		approvalActionType = ApprovalActionType.suspend;		
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();		
		
		// this does this. 
		// 1) run through the suspend action
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		SuspendService.RunSuspendService();
		
		CommonTestSteps.Logout();
		
		//driver.close();
		//driver.quit();
		
		BaseClass.stepComplete("Run suspend action complete. Now will reject the order.", "");
		
		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the deactivate order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.ApprovalAction(ApproverAction.reject);
		
		CommonTestSteps.Logout();
		
		//driver.close();
		//driver.quit();

		BaseClass.stepComplete("Order has been rejected. Now will verify limited user's details in 'my orders' page and order details psge.", "");
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
	
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		SuspendService.VerifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		SuspendService.VerifyOrderDetailsPagePostApproval();
		
		BaseClass.stepComplete("Suspend Service Test Complete.", "");
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		if(orderDetailsObjectExpected != null)
		{
			orderDetailsObjectExpected.Show();			
		}
		// JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}	
}
