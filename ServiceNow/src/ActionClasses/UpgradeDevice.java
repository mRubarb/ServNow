 package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import HelperObjects.ShoppingCart;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;
 

public class UpgradeDevice extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a deactivate service
	// and die s verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunUpgradeDevice() throws Exception
	{
		// Go through the Upgrade Device/Service pages.

		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectUpgradeDeviceAction();
		
		ChooseDevicePage.WaitForPageToLoadUpgradeDevice();
		ChooseDevicePage.SetupForDeviceSelection();
		VerifyPageTitle(upgradeDevice);
	
		ChooseDevicePage.SelectUpgradeDeviceAndStoreDeviceInfoIndex();   
		ChooseDevicePage.clickNextButton();
		
		ChoosePlanPage.WaitForPageToLoadNoPlanSelected();
		VerifyPageTitle(upgradeDevice);		
		ChoosePlanPage.StoreNameOfFirstPlanInList();
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.storeIncludedFeatures();
		ChoosePlanPage.storeOptionalFeaturesNamesAndInfoNew();
		ChoosePlanPage.selectFirstLastOptionalFeature();
		ChoosePlanPage.clickNextButton();

				
		VerifyPageTitle(upgradeDevice);
		ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
		ChooseAccessoriesPage.addAccessoryToCart_Upgrade(); 
		

		ProvideAdditionalInfoPage.WaitForPageToLoad();
		VerifyPageTitle(upgradeDevice);		
		ProvideAdditionalInfoPage.EnterMissingInfoUpgradeDevice();
		ProvideAdditionalInfoPage.clickNextBtn();
		
		EnterShippingInfoPage.WaitForPageLoad();
		VerifyPageTitle(upgradeDevice);
		EnterShippingInfoPage.VerifyCorrectDataDeviceAction();
		EnterShippingInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
		VerifyOrderPage.VerifyShippingInformationOrderAccessoriesAction(); // shipping info section - reuse order accessories. this has the same data organized the same way.
		VerifyOrderPage.verifyAdditionalInformationBlock();  // VerifyAdditionalInformationUpgradeDevice(); // additional info section
		VerifyOrderPage.VerifySelectedDeviceDetailsUpgradeDevice(); // device section
		VerifyOrderPage.VerifyPlanInfoUpgradeDevice(); // plan section
		
		VerifyOrderPage.verifyUpgradeDeviceAccessoriesAction();	// accessory section	
		VerifyOrderPage.verifyCostUpgradeDevice(); // cost 
		VerifyOrderPage.verifyCostMonthlyUpgradeDevice(); // cost monthly
		
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
		OrderSubmittedPage.VerifyAdditionalInformationUpgradeDevice();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
		
		// at this point the order type has to be changed so the order can be found in the order approvals list.
		// in the order approvals page the status is called  "Accessories Order";
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
		OrderSubmittedPage.VerifyAdditionalInformationUpgradeDevice();
		OrderSubmittedPage.VerifyApprovals();	
		OrderSubmittedPage.VerifyShippingInformation();  // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.VerifyDeviceSectionUpgradeDevice();
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
		
		OrderSubmittedPage.VerifyAdditionalInformationUpgradeDevice();
		OrderSubmittedPage.VerifyShippingInformation();  // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.VerifyDeviceSectionUpgradeDevice();
		OrderSubmittedPage.VerifyPlanSectionUpgradeDevice();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		// VerifyOrderDetailsHistoryAccessoriesAfterApproval(action);;
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


	public static void runUpgradeDevice_SFD112988() throws Exception {
		
		
		// Initialize shopping cart
		shoppingCart = new ShoppingCart();
		
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectUpgradeDeviceAction();
		
		ChooseDevicePage.WaitForPageToLoadUpgradeDevice();
		ChooseDevicePage.SetupForDeviceSelection();
	
		ChooseDevicePage.SelectUpgradeDeviceAndStoreDeviceInfoIndex();   
		ChooseDevicePage.clickNextButton();
		
		// Keep existing plan
		ChoosePlanPage.selectExistingPlan();
		ChoosePlanPage.clickNextButton();
		
		// Don't add accessories to shopping cart
		ChooseAccessoriesPage.clickNextBtn();

		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.EnterMissingInfoUpgradeDevice();
		ProvideAdditionalInfoPage.clickNextBtn();
		
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.VerifyCorrectDataDeviceAction();
		EnterShippingInfoPage.clickNextBtn();
		
		VerifyOrderPage.WaitForPageToLoad();
		VerifyOrderPage.VerifyShippingInformationOrderAccessoriesAction(); // shipping info section - reuse order accessories. this has the same data organized the same way.
		VerifyOrderPage.verifyAdditionalInformationBlock();  // additional info section
		VerifyOrderPage.VerifySelectedDeviceDetailsUpgradeDevice(); // device section
				
		VerifyOrderPage.verifyCostUpgradeDevice(); // cost 
		
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
		OrderSubmittedPage.VerifyAdditionalInformationUpgradeDevice();
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
		
		// at this point the order type has to be changed so the order can be found in the order approvals list.
		// in the order approvals page the status is called  "Accessories Order";
		orderDetailsObjectExpected.orderType = "Upgrade Order";
		
	}
	
}
