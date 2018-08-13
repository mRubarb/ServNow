package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.TransferServiceIn;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;



public class TC0043TransferServiceInReject extends BaseClass {

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited;  //UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}
	
	
	@Test
	public static void tC0043TransferServiceInReject() throws Exception
	{
		
		approverAction = ApproverAction.reject; // setup enum for indicating approval type.	
		
		approvalActionType = ApprovalActionType.transferServiceIn; 
	
		// 1. Log into ServiceNow as test user.
		CommonTestSteps.LoginLimitedUser();
		
		// 2. Create a Transfer Service In order
		TransferServiceIn.runTransferServiceInPhoneNumber();
		
		CommonTestSteps.Logout();
		
		BaseClass.stepComplete("Run upgrade device action complete. Now will approve the order.", "");

		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the Upgrade Device order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndApproveOrder();
		
		CommonTestSteps.Logout();
		
		BaseClass.stepComplete("Order has been rejected. Now will verify limited user's details in 'my orders' page and order details page.", "");

		TransferServiceIn.setOrderTypeForPostApproval();
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		
		
		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		TransferServiceIn.verifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		TransferServiceIn.verifyOrderDetailsPagePostApproval();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		TransferServiceIn.verifyOrderDetailsHistoryPostApproval(ApproverAction.reject);

		BaseClass.stepComplete("Transfer Service In Test Complete.", "");
		
		
	}
	
	@AfterClass
	public void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
	
	
	
}
