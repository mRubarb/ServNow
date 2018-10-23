package TestSuite1;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.UpgradeService;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;



/** TC0040 -- Upgrade Service  **/  

public class TC0040UpgradeServiceApprove extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}

	@Test
	public static void tc0040UpgradeServiceApprove() throws Exception
	{
		BaseClass.stepComplete("------------------ Starting upgrade service action with approve. -----------------------", "");

		approverAction = ApproverAction.approve; // setup enum for indicating approval type.	

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.		
		approvalActionType = ApprovalActionType.upgradeDevice;

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();

		// go to the services page through the home page. 
		CommonTestSteps.goToServicesPage();
		
		// this does this. 
		// 1) run through the action.
		// 2) setup order details expected object for correct values.
		// 3) verify results in order-submitted/order-details page
		// 4) verify the rest of the in user's info using 'my orders' order-submitted/order-details page. 
		UpgradeService.runUpgradeService();

		CommonTestSteps.Logout();

		BaseClass.stepComplete("Run upgrade service action complete. Now will approve the order.", "");

		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the Upgrade Service order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndApproveOrder();

		CommonTestSteps.Logout();

		BaseClass.stepComplete("Order has been approved. Now will verify limited user's details in 'my orders' page and order details page.", "");
		
		UpgradeService.SetOrderTypeForPostApproval();
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen(); 
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		

		// this verifies the order info in the 'my orders' page. this run's a loop on the 
		// verification to allow  SN to sync with command. 
		UpgradeService.VerifyLimitedUserOrderMyOrdersMainPage();
		
		// this verifies the order details under the order that was verified in 'my orders' page. 
		UpgradeService.VerifyOrderDetailsPagePostApproval(); 
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		UpgradeService.verifyOrderDetailsHistoryPostApproval(ApproverAction.approve); 
		
		BaseClass.stepComplete("Upgrade Service Test Complete.", "");
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		if(orderDetailsObjectExpected != null)
		{
		//	orderDetailsObjectExpected.Show();			
		}
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}




