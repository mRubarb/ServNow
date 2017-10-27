package TestSuite1;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.OrderAccessories;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;

public class TC0019OrderAccessoriesReject extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc00019OrderAccessoriesReject() throws Exception
	{
		BaseClass.stepComplete("------------------ Starting order accessories action with reject. -----------------------", "");		

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval page description sections.		
		approvalActionType = ApprovalActionType.orderAccessories;

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();

		// this verifies all the pre-approval inf0..
		OrderAccessories.RunOrderAccessories();

		CommonTestSteps.Logout();

		BaseClass.stepComplete("Run order accessories action complete. Now will reject the order.", "");
		
		OrderAccessories.SetOrderTypeForApproval(); // order type is worded differently in the approvals page.
		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the Order Accessories order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndRejectOrder();
		
		CommonTestSteps.Logout();
		

		BaseClass.stepComplete("Order has been rejected. Now will verify limited user's details in 'my orders' page and order details page.", "");
		
		OrderAccessories.SetOrderTypeForPostApproval();	// set order type is wording back to normal.	

		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
	
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		OrderAccessories.VerifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		OrderAccessories.VerifyOrderDetailsPagePostApproval();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		OrderAccessories.VerifyOrderDetailsOrderAccessories(ApproverAction.reject);

		BaseClass.stepComplete("Order accessories Test Complete.", "");
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
