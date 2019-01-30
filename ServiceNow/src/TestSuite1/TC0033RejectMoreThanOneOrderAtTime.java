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

		/*
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		DeactivateService.RunDeactivateService(true);

		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();
		DeactivateService.RunDeactivateService(false);
		*/
		
		CreateOrderDetailsExpectedObject();
		DeactivateService.SetupOrderDetailsExpectedObject();
		
		orderDetailsObjectExpected.orderId = "13297772";
		orderDetailsObjectExpected.orderIdTwo = "13297770";
		orderDetailsObjectExpected.externalOrderId = "1548799655829acea109b5e02b80ef2b";
		orderDetailsObjectExpected.externalOrderIdTwo = "1548799594666f698fa45dd6913276a8";

		fullServiceNumber = "+1 (111) 222-3333";
		serviceNumber = "1112223333";
		
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
		
		// this rejects the orders that were created on previous steps
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