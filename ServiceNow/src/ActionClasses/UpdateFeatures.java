package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.UpdateFeature;
import ServiceNow.VerifyOrderPage;

public class UpdateFeatures extends ActionsBase
{

	public static void RunUpdateFeatures() throws Exception
	{
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectUpdateFeaturesAction();
		
		UpdateFeature.WaitForPageToLoad();
		VerifyPageTitle(updateServiceTitle);
		UpdateFeature.VerifyAddAndNoChangeSelections();
		UpdateFeature.LoadFeatures(numUpdateFeaturesToLoad); // need a list of some of the features.
		UpdateFeature.VerifyAddFeatures();
		UpdateFeature.VerifyRemoveActions();
		UpdateFeature.SetupAddedFeaturesForNextPages(numUpdateFeaturesToLoad);
		UpdateFeature.ClickNext();	

		ProvideAdditionalInfoPage.WaitForPageToLoadUnsuspend(); 		
		VerifyPageTitle(updateServiceTitle);
		ProvideAdditionalInfoPage.EnterMissingInfoOrderAccessories();
		ProvideAdditionalInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
		VerifyPageTitle(updateServiceTitle);		
		VerifyOrderPage.VerifyFeatures();	
		VerifyOrderPage.VerifyAdditionalInformationUpdateFeature();
		VerifyOrderPage.VerifyFeaturesCost();
		VerifyOrderPage.clickSubmitBtn(); // submit order.
		VerifyOrderPage.WaitForOrderComplete();
		StoreOrderNumberToVariable(); 
		Thread.sleep(2000); // wait two seconds before selecting to view the order.
		
		OrderSubmittedPage.SelectViewOrder(); // view order.	
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();

		// create and setup order details expected object with order type, order id, and expected status. 
		CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
		SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for update device. 
		
		// this verifies the order number in verify page matches the order number in order details 
		// page and verifies the correct order type at the top of the order details (submitted) page.
		VerifyOrderNumberAndOrderTypeBetweenPages(); 

		VerifyFullServiceNumber();
		OrderSubmittedPage.VerifyTopSection(); // this also sets external order id in orderDetailsObjectExpected object that was setup further above.
		OrderSubmittedPage.VerifyAdditionalInformationOrderAccessories();  	
  
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.   
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helper methods below.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	
	// change order type in orderDetailsObjectExpected for deactivate service.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Update Features"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}	

	// this loops on the test that checks the order block in the 'my orders' main page. it sometimes doesn't sync correctly 
	// so it is looped on.
	public static void VerifyLimitedUserOrderMyOrdersMainPage() throws Exception
	{
		if(!RunTimedCall_VerifyLimitedUserOrdersPage(loopTime))
		{
			Assert.fail("Failed to find correct users information in users order information in DeactivateService.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
	}	
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePreApproval() throws Exception
	{
		MyOrdersPage.WaitForPageToLoad(); // sanity check.
		MyOrdersPage.SelectOrderActionBlock();	
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		OrderSubmittedPage.VerifyTopSection();
		OrderSubmittedPage.VerifyTopSectionActionsAfterCommandSync();		
		OrderSubmittedPage.VerifyAdditionalInformationOrderAccessories(); // this works for update features.
		OrderSubmittedPage.VerifyApprovals();	
		OrderSubmittedPage.VerifyUpdateFeatures();	
		
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
		ServiceNow.OrderSubmittedPage.VerifyAdditionalInformationOrderAccessories(); // this works for update features.
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
	}
	
		
	// the order details page is open. it has synced with command so now the history section can be verified.
	public static void verifyOrderDetailsUpdateFeaturesPostApproval(ApproverAction appAction) throws Exception
	{
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
		
	}
	
		
	
	
}
