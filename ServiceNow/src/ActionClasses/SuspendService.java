package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.MyDevicesPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;


public class SuspendService extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a Suspend service
	// and does verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunSuspendService() throws Exception
	{

		// go through the suspend pages.
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectSuspendAction();

		ServiceNow.ProvideAdditionalInfoPage.WaitForPageToLoadSuspend();
		VerifyPageTitle(suspendActionTitle);
		ProvideAdditionalInfoPage.EnterMissingInfoSuspend();
		ProvideAdditionalInfoPage.clickNextBtn();

		VerifyOrderPage.WaitForPageToLoad();
		VerifyPageTitle(suspendActionTitle);		
		// VerifyOrderPage.verifyAdditionalInformationBlock();  
		VerifyOrderPage.VerifyAdditionalInformationSuspend();
		VerifyOrderPage.clickSubmitBtn(); // submit order.
		VerifyOrderPage.WaitForOrderComplete();
		StoreOrderNumberToVariable(); // in suspend the order number is shown in the order submitted page.
		
		Thread.sleep(2000); // wait two seconds before selecting to view the order.
		OrderSubmittedPage.SelectViewOrder();	
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		
		// create and setup order details expected object with order type, order id, and expected status. 
		CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
		SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for deactivate service. 

		// this verifies the order number in verify page matches the order number in order details 
		// page and verifies the correct order type at the top of the order details (submitted) page.
		VerifyOrderNumberAndOrderTypeBetweenPages(); 

		// more verifications here.
		OrderSubmittedPage.VerifyTopSection(); // this also sets external order id in orderDetailsObjectExpected object that was setup further above.
		OrderSubmittedPage.VerifyTopSectionLowerPart(); // 9/23/18
		
		// OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationBothSuspends();
		OrderSubmittedPage.VerifyAdditionalInformationBothSuspends();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.  
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();

		// the order details page is open. it has synced with command so now the history section can be verified.
		VerifyHistoryDetailsForOrderSubmitted();
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////
	//									Helper Methods Below
	////////////////////////////////////////////////////////////////////////////////////////////////	
	// //////////////////////////////////////////////////////////////////////////////////////////////	
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePreApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.VerifyTopSectionLowerPart(); // 9/23/18
		ServiceNow.OrderSubmittedPage.VerifyTopSectionActionsAfterCommandSync();		
		// ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationBothSuspends();
		ServiceNow.OrderSubmittedPage.VerifyAdditionalInformationBothSuspends(); // 923/18
		ServiceNow.OrderSubmittedPage.VerifyApprovals();		
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyHistoryDetailsForOrderSubmitted();
	}
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePostApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		OrderSubmittedPage.VerifyTopSectionLowerPart(); // 9/26/18
		ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationBothSuspends();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyOrderDetailsHistoryAfterApproval();
	}	
	
	
	// this loops on the test that checks the order block in the 'my orders' main page. the page sometimes doesn't sync correctly 
	// so it is looped on.
	public static void VerifyLimitedUserOrderMyOrdersMainPage() throws Exception
	{
		if(!RunTimedCall_VerifyLimitedUserOrdersPage(loopTime))
		{
			Assert.fail("Failed to find correct user information in users order information in DeactivateService.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
	}
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPage() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationBothSuspends();
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyOrderDetailsHistoryAfterApproval();
	}	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helper methods below.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	// change order type in orderDetailsObjectExpected for deactivate service.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Suspend Service"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}
}
