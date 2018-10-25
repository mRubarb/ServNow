package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.PortNumber;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;

public class TC0021PortNumberReject extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0021PortNumberReject() throws Exception
	{
		BaseClass.stepComplete("------------------ Starting port number action with reject. -----------------------", "");

		approverAction = ApproverAction.reject; // setup enum for indicating approval type.	

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.		
		approvalActionType = ApprovalActionType.portNumber;

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();

		// need to store away current plan vendor.
		PortNumber.GetCurrentVendor();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();

		// this does this. 
		// 1) run through the action
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		PortNumber.RunPortNumber();
		/*
		CreateOrderDetailsExpectedObject();

		orderDetailsObjectExpected.orderId = "13295456";
		orderDetailsObjectExpected.externalOrderId = "15404160649773f8aca1f191aae50bde";
		orderDetailsObjectExpected.orderType = "Port Number";		
		orderDetailsObjectExpected.status = "Approval Rejected";
		*/
		
		CommonTestSteps.Logout();
		
		BaseClass.stepComplete("Run port number action complete. Now will reject the order.", "");

		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the Port Number order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndRejectOrder();

		CommonTestSteps.Logout();

		BaseClass.stepComplete("Order has been rejected. Now will verify limited user's details in 'my orders' page and order details page.", "");
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
	
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		PortNumber.VerifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		PortNumber.VerifyOrderDetailsPagePostApproval(ApproverAction.reject);
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		PortNumber.VerifyOrderDetailsHistoryAccessoriesAfterApproval(ApproverAction.reject);
		
		BaseClass.stepComplete("Port Number Test Complete.", "");
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




