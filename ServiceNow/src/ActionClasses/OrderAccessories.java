package ActionClasses;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.CommonVerifyPageSubmitPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;

public class OrderAccessories extends ActionsBase  
{

	public static boolean RunOrderAccessories() throws Exception
	{
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectOrderAccessoriesAction();
		
		VerifyPageTitle(orderAccessoriesActionTitle);	
		ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
		// This test can be performed only if there are available accessories to be ordered for the selected device
		boolean accessoriesAvailable = ChooseAccessoriesPage.addAccessoryToCart_OrderAccessories(); 
		
		if (accessoriesAvailable) {
			
			ProvideAdditionalInfoPage.WaitForPageToLoadUnsuspend(); // use unsuspend wait. it's the same as order accessories.
			VerifyPageTitle(orderAccessoriesActionTitle);		
			ProvideAdditionalInfoPage.EnterMissingInfoOrderAccessories();
			monthlyCostOrderAccessories = ProvideAdditionalInfoPage.GetCostTotal(); // save the total cost of the accessories ordered.
			ProvideAdditionalInfoPage.clickNextBtn();

			EnterShippingInfoPage.WaitForPageLoad();
			EnterShippingInfoPage.selectExpeditedCheckBox();
			VerifyPageTitle(orderAccessoriesActionTitle);		
			EnterShippingInfoPage.clickNextBtn();

			VerifyOrderPage.WaitForPageToLoad();
			//VerifyOrderPage.verifyAdditionalInformationBlock(); 
			//VerifyOrderPage.VerifyAdditionalInformationOderAccessories();
			CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories();

			VerifyOrderPage.VerifyOrderAccessoriesAction(); 
			VerifyOrderPage.VerifyShippingInformationOrderAccessoriesAction();	
			VerifyCostOrderAccessoriesAction();
			
			VerifyOrderPage.clickSubmitBtn(); // submit order.
			VerifyOrderPage.WaitForOrderComplete();
			
			StoreOrderNumberToVariable(); // in order accessories the order number is shown in the order submitted page.
			Thread.sleep(2000); // wait two seconds before selecting to view the order.
			
			OrderSubmittedPage.SelectViewOrder(); // view order.	
			OrderSubmittedPage.WaitForOrderDetailsPageToLoad();

			// create and setup order details expected object with order type, order id, and expected status. 
			CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
			SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for deactivate service. 
			
			// this verifies the order number in verify page matches the order number in order details 
			// page and verifies the correct order type at the top of the order details (submitted) page.
			VerifyOrderNumberAndOrderTypeBetweenPages(); 
			
			// more verifications here.
			VerifyFullServiceNumber();
			OrderSubmittedPage.VerifyTopSection(); // this also sets external order id in orderDetailsObjectExpected object that was setup further above.
			//OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationOrderAccessories();  	
			//OrderSubmittedPage.VerifyAdditionalInformationOrderAccessories();
			CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories();
			
			// go to 'my orders' main page to setup for the loop test below.
			CommonTestSteps.GoToMyOrders();
			
			// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
			// the 'orderActionBlock' variable is set here.   
			VerifyLimitedUserOrderMyOrdersMainPage();

			CommonTestSteps.GoToMyOrders();
			VerifyOrderDetailsPagePreApproval();
			
			// at this point the order type has to be changed so the order can be found in the order approvals list.
			// in the order approvals page the status is called  "Accessories Order";
			orderDetailsObjectExpected.orderType = "Accessories Order";

		}
		
		return accessoriesAvailable;
		
	}
	
	
	
	// For test SFD112968_OrderAccessories
	
	public static boolean runOrderAccessories_SFD112968() throws Exception
	{
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectOrderAccessoriesAction();
		
			
		ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
		// This test can be performed only if there are available accessories to be ordered for the selected device
		boolean accessoriesAvailable = ChooseAccessoriesPage.addRemoveAccessoryFromCart(); 
		
		// ** Following steps can be removed - not needed to verify defect **
		if (accessoriesAvailable) {
			
			ProvideAdditionalInfoPage.WaitForPageToLoadUnsuspend(); // use unsuspend wait. it's the same as order accessories.
			VerifyPageTitle(orderAccessoriesActionTitle);		
			ProvideAdditionalInfoPage.EnterMissingInfoOrderAccessories();
			monthlyCostOrderAccessories = ProvideAdditionalInfoPage.GetCostTotal(); // save the total cost of the accessories ordered.
			ProvideAdditionalInfoPage.clickNextBtn();

			EnterShippingInfoPage.WaitForPageLoad();
			EnterShippingInfoPage.selectExpeditedCheckBox();
			VerifyPageTitle(orderAccessoriesActionTitle);		
			EnterShippingInfoPage.clickNextBtn();

			VerifyOrderPage.WaitForPageToLoad();
			VerifyOrderPage.verifyAdditionalInformationBlock(); 
			CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories(); // 1/10/19
			
			VerifyOrderPage.VerifyOrderAccessoriesAction(); 
			VerifyOrderPage.VerifyShippingInformationOrderAccessoriesAction();	
			VerifyCostOrderAccessoriesAction();
			VerifyOrderPage.clickSubmitBtn(); 
			VerifyOrderPage.WaitForOrderComplete();
			
			StoreOrderNumberToVariable(); // in order accessories the order number is shown in the order submitted page.
			Thread.sleep(2000); // wait two seconds before selecting to view the order.
			
			OrderSubmittedPage.SelectViewOrder(); // view order.	
			OrderSubmittedPage.WaitForOrderDetailsPageToLoad();

			// create and setup order details expected object with order type, order id, and expected status. 
			CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
			SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for deactivate service. 
			
			// this verifies the order number in verify page matches the order number in order details 
			// page and verifies the correct order type at the top of the order details (submitted) page.
			VerifyOrderNumberAndOrderTypeBetweenPages(); 
			
			// more verifications here.
			VerifyFullServiceNumber();
			OrderSubmittedPage.VerifyTopSection(); // this also sets external order id in orderDetailsObjectExpected object that was setup further above.
			OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationOrderAccessories();  	
			CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories(); // 1/10/19
			
			// go to 'my orders' main page to setup for the loop test below.
			CommonTestSteps.GoToMyOrders();
			
			// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
			// the 'orderActionBlock' variable is set here.   
			VerifyLimitedUserOrderMyOrdersMainPage();

			CommonTestSteps.GoToMyOrders();
			VerifyOrderDetailsPagePreApproval();
			
			// at this point the order type has to be changed so the order can be found in the order approvals list.
			// in the order approvals page the status is called  "Accessories Order";
			orderDetailsObjectExpected.orderType = "Accessories Order";

		}
		
		return accessoriesAvailable;
		
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helper methods below.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	
	// change order type in orderDetailsObjectExpected for deactivate service.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Order Accessories"; 
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
		OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationOrderAccessories(); 
		CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories(); // 1/10/19		
		OrderSubmittedPage.VerifyApprovals();		
		OrderSubmittedPage.VerifyShippingInformation(); // VerifyShippingInformationOrderAccessoriesPreApproval();
		OrderSubmittedPage.VerifyOrderSegmentAccessoriesOrderAction();
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		//VerifyHistoryDetailsForOrderSubmittedOrderAccessories();
		VerifyHistoryDetailsForOrderSubmitted();
	}	
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePostApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.VerifyTopSectionLowerPart();
		//ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationOrderAccessories();
		//ServiceNow.OrderSubmittedPage.VerifyAdditionalInformationOrderAccessories();
		CommonVerifyPageSubmitPage.VerifyAdditionalInformationOrderAccessories();
		OrderSubmittedPage.VerifyShippingInformation();  //VerifyShippingInformationOrderAccessoriesPostApproval();
		OrderSubmittedPage.VerifyOrderSegmentAccessoriesOrderAction();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
	}
	
	public static void SetOrderTypeForApproval()
	{
		orderDetailsObjectExpected.orderType = "Accessories Order";		
	}

	public static void SetOrderTypeForPostApproval()
	{
		orderDetailsObjectExpected.orderType = "Order Accessories";
	}
	
	// the order details page is open. it has synced with command so now the history section can be verified.
	public static void VerifyOrderDetailsOrderAccessories(ApproverAction appAction) throws Exception
	{
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
	}
	
	
}
