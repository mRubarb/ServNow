 package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.MyServicesPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;
 

public class UpgradeService extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a deactivate service
	// and die s verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void runUpgradeService() throws Exception
	{
		// Go through the Upgrade Device/Service pages.

		actionOnlyOnService = false; // change to false 8/1/18 bob
		
		MyServicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyServicesPage.SelectUpgradeServiceAction(); // call different version of this method 8/1/18 bob 
		
		ChooseDevicePage.WaitForPageToLoadUpgradeService();
		ShowText("Start");
		ChooseDevicePage.getExistingDeviceInfo();
		VerifyPageTitle(upgradeService);
		
		//ChooseDevicePage.SelectUpgradeDeviceAndStoreDeviceInfoIndex();   
		ChooseDevicePage.clickNextButton();
		
		ChoosePlanPage.WaitForPageToLoadNoPlanSelected();
		VerifyPageTitle(upgradeService);		
		ChoosePlanPage.StoreNameOfFirstPlanInList();
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.storeIncludedFeatures();
		ChoosePlanPage.storeOptionalFeaturesNamesAndInfoNew();
		ChoosePlanPage.selectFirstLastOptionalFeature();
		ChoosePlanPage.clickNextButton();
		
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		VerifyPageTitle(upgradeService);		
		ProvideAdditionalInfoPage.EnterMissingInfoUpgradeDevice();
		ProvideAdditionalInfoPage.clickNextBtn();

		EnterShippingInfoPage.WaitForPageLoad();
		VerifyPageTitle(upgradeService);
		EnterShippingInfoPage.VerifyCorrectDataDeviceAction();
		EnterShippingInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
		VerifyOrderPage.VerifyShippingInformationOrderAccessoriesAction(); // shipping info section - reuse order accessories. this has the same data organized the same way.
		// VerifyOrderPage.verifyAdditionalInformationBlock();  // additional info section 
		VerifyOrderPage.VerifyAdditionalInformationUpgradeDevice(); // common to upgrade service
		VerifyOrderPage.VerifyPlanInfoUpgradeDevice(); // plan section - common to upgrade service
		VerifyOrderPage.verifyCostMonthlyUpgradeDevice(); // cost monthly - common to upgrade service
		
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
		OrderSubmittedPage.VerifyTopSectionLowerPart();
		//OrderSubmittedPage.verifyAdditionalInformationBlock();
		OrderSubmittedPage.verifyAdditionalInformationUpgradeService();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
		
		// at this point the order type has to be changed so the order can be found in the order approvals list.
		// in the order approvals page the status is called  "Upgrade Order";
		orderDetailsObjectExpected.orderType = "Upgrade Order";
	}
	
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////
	//									Helper Methods Below
	////////////////////////////////////////////////////////////////////////////////////////////////	
	// //////////////////////////////////////////////////////////////////////////////////////////////	

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
		OrderSubmittedPage.verifyAdditionalInformationBlock();   //VerifyAdditionalInformationUpgradeDevice();
		OrderSubmittedPage.VerifyApprovals();	
		OrderSubmittedPage.VerifyShippingInformation(); // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		OrderSubmittedPage.verifyAdditionalInformationUpgradeService();
		//OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.VerifyPlanSectionUpgradeDevice();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		// use VerifyHistoryDetailsForOrderSubmittedOrderAccessories. it doesn't use tbody to find the history section. 
		VerifyHistoryDetailsForOrderSubmitted();
	}
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePostApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		/*
		OrderSubmittedPage.verifyAdditionalInformationBlock();   //VerifyAdditionalInformationUpgradeDevice();
		OrderSubmittedPage.VerifyShippingInformation(); // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		//OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.VerifyPlanSectionUpgradeDevice();
		*/
		// new below
		OrderSubmittedPage.verifyAdditionalInformationUpgradeService(); 
		OrderSubmittedPage.VerifyShippingInformation();
		OrderSubmittedPage.VerifyPlanSectionUpgradeDevice();
		OrderSubmittedPage.VerifyTopSection();
		OrderSubmittedPage.VerifyTopSectionLowerPart();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		//VerifyOrderDetailsHistoryAccessoriesAfterApproval(action); // this is called in the main test suite
	}
	
	public static void SetOrderTypeForPostApproval()
	{
		orderDetailsObjectExpected.orderType = "Upgrade Service";
	}
	
	
	public static void verifyOrderDetailsHistoryPostApproval(ApproverAction appAction) throws Exception
	{
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
	}
	
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helper methods below.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	
	// change order type in orderDetailsObjectExpected for deactivate service.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Upgrade Service"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}
}
