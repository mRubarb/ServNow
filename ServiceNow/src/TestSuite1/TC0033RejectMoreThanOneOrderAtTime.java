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

		//testCaseStatus = false;
		
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
		DeactivateService.RunDeactivateService(true);

		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		DeactivateService.RunDeactivateService(false);
		
		/* Oct 27 Ana
		 * If you want to avoid adding more orders and want to use existing orders (that have been recently added so they show up on the first page; and are in 'Requested' state)
		 * Comment code above - from line CommonTestSteps.GoToDevicesPage() - and uncomment section below - 
		 * --> Replace values for orderId and externalOrderId for 2 orders
		 */

		/*
		CreateOrderDetailsExpectedObject();
		DeactivateService.SetupOrderDetailsExpectedObject();
		
		orderDetailsObjectExpected.orderId = "13296612";
		orderDetailsObjectExpected.orderIdTwo = "13296614";
		orderDetailsObjectExpected.externalOrderId = "1544012378435c9158780c917a708dc6";
		orderDetailsObjectExpected.externalOrderIdTwo = "1544012441177c02cfbae0815768cb2f";

		fullServiceNumber = "+1 (111) 222-3333";
		serviceNumber = "1112223333";
		*/
		//* end
		
		// Show orderId and externalOrderId for both orders. 
		ShowText("Order Ids:");
		ShowText(BaseClass.orderDetailsObjectExpected.orderId);
		ShowText(BaseClass.orderDetailsObjectExpected.orderIdTwo);
		ShowText("External Order Ids:");
		ShowText(BaseClass.orderDetailsObjectExpected.externalOrderId);
		ShowText(BaseClass.orderDetailsObjectExpected.externalOrderIdTwo);
	
		CommonTestSteps.Logout(); // **

		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen(); // **		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage(); // **
		
		// It rejects the orders that were created on previous steps
		ApprovalAction.approveOrRejectOrders(approverAction);  // **
		
		CommonTestSteps.Logout(); //**
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen(); // **
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		

		orderDetailsObjectExpected.status = "Approval Rejected"; // make sure this is set for last steps below		
		
		// ---- verify first rejected transaction ----
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		DeactivateService.VerifyLimitedUserOrderMyOrdersMainPage();
		
		orderDetailsObjectExpected.orderId = orderDetailsObjectExpected.orderIdTwo;
		orderDetailsObjectExpected.externalOrderId = orderDetailsObjectExpected.externalOrderIdTwo;

		// ---- verify second rejected transaction ---- 
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		DeactivateService.VerifyLimitedUserOrderMyOrdersMainPage();
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