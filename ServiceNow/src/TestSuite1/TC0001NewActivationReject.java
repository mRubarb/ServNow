package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.DeactivateService;
import ActionClasses.NewActivation;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.SelectRegionPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.SideBar;
import ServiceNow.VerifyOrderPage;


public class TC0001NewActivationReject extends BaseClass
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited;  //UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}
	
	@Test
	public static void tc0001NewActivationReject() throws Exception
	{

		approverAction = ApproverAction.reject; // setup enum for indicating approval type.	

		// setup enum for indicating approval action type. this is used to tell which module will verify info in approval description sections.		
		approvalActionType = ApprovalActionType.newActivation;		
		
		CommonTestSteps.LoginLimitedUser();
		
		NewActivation.RunNewActivation();
		
		//ShowText(orderDetailsObjectExpected.externalOrderId);
		//ShowText(orderDetailsObjectExpected.orderId);		
		
		CommonTestSteps.Logout();
		
		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the deactivate order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.selectAndRejectOrder();

		CommonTestSteps.Logout();
		
		// login as limited user.
		CommonTestSteps.LoginLimitedUserBrowserOpen();
		
		// go to 'my orders main page'
		CommonTestSteps.GoToMyOrders();		

		NewActivation.VerifyLimitedUserOrderMyOrdersMainPage();
		
		NewActivation.VerifyOrderDetailsPostApproval(); // this includes verifying history.
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
