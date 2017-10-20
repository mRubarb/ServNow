package testSuiteDefects;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import ActionClasses.SuspendService;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Approvals;
import ServiceNow.BaseClass;
import ServiceNow.MyDevicesPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;
import ServiceNow.BaseClass.ApprovalActionType;
import ServiceNow.BaseClass.ApproverAction;

public class SFD112978_Suspend extends BaseClass
{

	@Test
	public static void tc0022SuspendTest() throws Exception
	{

		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();		
		
		// go through the suspend pages.
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectSuspendAction();
		ServiceNow.ProvideAdditionalInfoPage.WaitForPageToLoadSuspend();
		ProvideAdditionalInfoPage.EnterMissingInfoSuspend();
		ProvideAdditionalInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		// VerifyOrderPage.VerifyHoldServiceHasData(); // this is verification.

		Pause("");
		
		//ServiceNow.ProvideAdditionalInfoPage.WaitForPageToLoadSuspend();
		//ProvideAdditionalInfoPage.EnterMissingInfoSuspend();

		
		

		
		
		/*
		CommonTestSteps.Logout();
		
		BaseClass.stepComplete("Run suspend action complete. Now will approve the order.", "");
		
		// login as approver.
		CommonTestSteps.LoginApproverBrowserOpen();		
		
		// go to approvals page.
		CommonTestSteps.GoToMyApprovalsPage();
		
		// this finds the approval entry for the deactivate order just placed by the limited user, approves it, and 
		// verifies it's result in the approvals main page. it also sets the orderDetailsObjectExpected object 
		// status to 'In Fulfillment' if everything goes OK.
		Approvals.ApprovalAction(ApproverAction.approve);
		
		CommonTestSteps.Logout();

		BaseClass.stepComplete("Order has been approved. Now will verify limited user's details in 'my orders' page and order details psge.", "");

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
		*/
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		// JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}		
	
	
	
}
