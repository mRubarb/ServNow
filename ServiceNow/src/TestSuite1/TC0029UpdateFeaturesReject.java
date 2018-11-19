package TestSuite1;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.UpdateFeatures;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;

public class TC0029UpdateFeaturesReject extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0029HomeUpdateFeaturesReject() throws Exception
	{

		BaseClass.stepComplete("------------------ Starting update features action with reject. -----------------------", "");

		approverAction = ApproverAction.reject; // setup enum for indicating approval type.	

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.		
		approvalActionType = ApprovalActionType.updateFeatures;

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();

		// this does this. 
		// 1) run through the deactivate action
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		UpdateFeatures.RunUpdateFeatures();
		
		CommonTestSteps.Logout();

		//CreateOrderDetailsExpectedObject();

		//orderDetailsObjectExpected.orderId = "13296161";
		//orderDetailsObjectExpected.externalOrderId = "1542289163192d0c9bea6ae3d783fdb5";
		//orderDetailsObjectExpected.orderType = "Update Features";		
		//orderDetailsObjectExpected.status = "Awaiting Approval in ServiceNow";
		
		BaseClass.stepComplete("Run update features action complete. Now will reject the order.", "");
		
		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the Update Features order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndRejectOrder();
		
		CommonTestSteps.Logout();

		BaseClass.stepComplete("Order has been approved. Now will verify limited user's details in 'my orders' page and order details page.", "");

		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
		
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		UpdateFeatures.VerifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		UpdateFeatures.VerifyOrderDetailsPagePostApproval();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		UpdateFeatures.verifyOrderDetailsUpdateFeaturesPostApproval(ApproverAction.reject);

		BaseClass.stepComplete("Update Features Test Complete.", "");
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




