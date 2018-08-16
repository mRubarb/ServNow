package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.IdentifyDevices;
import ServiceNow.MyDevicesPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;

public class SwapDevice extends ActionsBase 
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a deactivate service
	// and die s verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunSwapDevice() throws Exception
	{
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectSwapDevicesAction();

		IdentifyDevices.WaitForPageToLoad();
		IdentifyDevices.PopulateNewDevice();
		
		VerifyPageTitle(swapDevice);
		IdentifyDevices.ShowIdentityData();
		IdentifyDevices.ClickNext();
		
		ProvideAdditionalInfoPage.WaitForPageToLoadUnsuspend();
		VerifyPageTitle(swapDevice);	
		ProvideAdditionalInfoPage.EnterMissingInfoSwapDevice();
		ProvideAdditionalInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
		VerifyPageTitle(swapDevice);		
		VerifyOrderPage.VerifySwapDeviceExistingAndNewDevices();
		VerifyOrderPage.verifyAdditionalInformationBlock();
		VerifyOrderPage.clickSubmitBtn(); // submit order.
		VerifyOrderPage.WaitForOrderComplete();
		
		StoreOrderNumberToVariable(); 
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
		OrderSubmittedPage.verifyAdditionalInformationBlock();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////
	//									Helper Methods Below
	////////////////////////////////////////////////////////////////////////////////////////////////	
	// //////////////////////////////////////////////////////////////////////////////////////////////	


	public static void SetupOrderDetailsExpectedObjectPreApproval() throws Exception
	{
		orderDetailsObjectExpected.orderType = "Swap Devices";
	}

	public static void SetupOrderDetailsExpectedObjectPostApproval() throws Exception
	{
		orderDetailsObjectExpected.orderType = "Swap a Device";
	
	}
	
	// this loops on the test that checks the order block in the 'my orders' main page. it sometimes doesn't sync correctly 
	// so it is looped on.
	public static void VerifyLimitedUserOrderMyOrdersMainPage() throws Exception
	{
		if(!RunTimedCall_VerifyLimitedUserOrdersPage(loopTime))
		{
			Assert.fail("Failed to find correct users information in users order information in SwapDevice.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
	}
	
	// this assumes the user's 'my orders' page is open. 
	public static void VerifyOrderDetailsPagePreApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); 
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.VerifyTopSectionActionsAfterCommandSync();		
		ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock();
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
		ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyOrderDetailsHistoryAfterApproval();
	}
	
	
	
	
	// change order type in orderDetailsObjectExpected for deactivate service.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Swap a Device"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}

	public static void runSwapDevices_SFD113339() throws Exception {
		
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectSwapDevicesAction();

		IdentifyDevices.WaitForPageToLoad();
		IdentifyDevices.PopulateNewDevice();
		
		IdentifyDevices.ClickNext();
		
		ProvideAdditionalInfoPage.WaitForPageToLoadUnsuspend();
			
		ProvideAdditionalInfoPage.enterMissingInfoSwapDevice_SFD113339();
		ProvideAdditionalInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
				
		VerifyOrderPage.VerifySwapDeviceExistingAndNewDevices();
		VerifyOrderPage.verifyAdditionalInformationBlock();
		VerifyOrderPage.clickSubmitBtn(); // submit order.
		VerifyOrderPage.WaitForOrderComplete();
		
		StoreOrderNumberToVariable(); // in deactivate the order number is shown in the order submitted page.
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
		OrderSubmittedPage.verifyAdditionalInformationBlock();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
		
	}
}
