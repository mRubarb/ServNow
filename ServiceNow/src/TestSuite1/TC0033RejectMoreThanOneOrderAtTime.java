package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.ApprovalAction;
import ActionClasses.DeactivateService;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.BaseClass;

public class TC0033RejectMoreThanOneOrderAtTime extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tC0033RejectMoreThanOneOrderAtTime() throws Exception
	{

		BaseClass.stepComplete("------------------ Starting reject more than one order. -----------------------", "");

		testCaseStatus = false;
		
		approverAction = ApproverAction.reject; // setup enum for indicating approval type. 
		
		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.
		approvalActionType = ApprovalActionType.deactivate; 

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		
		// this does this. 
		// 1) run through the deactivate action
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page after su
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		DeactivateService.RunDeactivateService(true); // jnupp.

		// junpp below
		// got to the devices page through the home page. 
		//Pause("First device done.");
		CommonTestSteps.GoToDevicesPage();
		DeactivateService.RunDeactivateService(false);
		
		// jnupp above
		
		/* Oct 27 Ana
		 * If you want to avoid adding more orders and want to use existing orders (that have been recently added so they show up on the first page; and are in 'Requested' state)
		 * Comment code above - from line CommonTestSteps.GoToDevicesPage() - and uncomment section below - 
		 * --> Replace values for orderId and externalOrderId for 2 orders
		 */
		
		/*CreateOrderDetailsExpectedObject();
		DeactivateService.SetupOrderDetailsExpectedObject();
		
		orderDetailsObjectExpected.orderId = "11571990";
		orderDetailsObjectExpected.orderIdTwo = "11571992";
		orderDetailsObjectExpected.externalOrderId = "1509042083909227ada780fdda399eba";
		orderDetailsObjectExpected.externalOrderIdTwo = "15090421400612401178bfe3fa76b9c2";
		*/
		
		
		// Show orderId and externalOrderId for both orders. 
		ShowText(BaseClass.orderDetailsObjectExpected.orderId);
		ShowText(BaseClass.orderDetailsObjectExpected.orderIdTwo);
		ShowText(BaseClass.orderDetailsObjectExpected.externalOrderId);
		ShowText(BaseClass.orderDetailsObjectExpected.externalOrderIdTwo);
	
		CommonTestSteps.Logout();

		BaseClass.stepComplete("Run deactivate action complete. Now will reject the order.", "");

		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// It rejects the orders that were created on previous steps
		ApprovalAction.approveOrRejectOrders(approverAction);
		
		CommonTestSteps.Logout();
		
		BaseClass.stepComplete("Reject more than one order complete.", "");

	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		if(orderDetailsObjectExpected != null)
		{
			orderDetailsObjectExpected.Show();			
		}
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}	
	
	
}