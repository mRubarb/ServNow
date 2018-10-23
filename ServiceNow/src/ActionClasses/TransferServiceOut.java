package ActionClasses;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.MyDevicesPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.VerifyOrderPage;

public class TransferServiceOut extends ActionsBase 
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a transfer out service
	// and does verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunTransferServiceOut() throws Exception
	{
		// go through the transfer service out pages.
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectTransferOutAction();		

		// verify title and expected service number.
		ServiceNow.ProvideAdditionalInfoPage.WaitForPageToLoad();
		VerifyPageTitle(transferServiceOutActionTitle);
		VerifyServiceNumberShownProvideInfoPage();

		// below - can't do anymore - dBase refresh changed everything to required.
		// got to next page and verify expected service number.
		//ServiceNow.ProvideAdditionalInfoPage.clickNextBtn();
		//ServiceNow.VerifyOrderPage.WaitForPageToLoad();
		//VerifyServiceNumberShownVerifyOrderPage();
		
		// populate provide additional info page.
		//ServiceNow.VerifyOrderPage.ClickBackButton();
		ServiceNow.ProvideAdditionalInfoPage.WaitForPageToLoad();
		ServiceNow.ProvideAdditionalInfoPage.PopulateFieldsTransferServiceOut();
		ServiceNow.ProvideAdditionalInfoPage.clickNextBtn();

		// verify additional information in verify order page. 
		VerifyOrderPage.WaitForPageToLoad();
		//VerifyOrderPage.verifyAdditionalInformationBlock();
		VerifyOrderPage.VerifyAdditionalInformationTransferServiceOut();
		
		// SUBMIT
		VerifyOrderPage.clickSubmitBtn();
		VerifyOrderPage.WaitForOrderComplete(); // added this Wednesday

		StoreOrderNumberToVariable(); // in deactivate the order number is shown in the order submitted page.
		Thread.sleep(2000); // wait two seconds before selecting to view the order.
		
		OrderSubmittedPage.SelectViewOrder();	
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		
		// create and setup order details expected object with order type, order id, and expected status. 
		CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
		SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for deactivate service. LOOK UP 

		// this verifies the order number in verify page matches the order number in order details 
		// page and verifies the correct order type at the top of the order details (submitted) page.
		VerifyOrderNumberAndOrderTypeBetweenPages();

		// more verifications here.
		OrderSubmittedPage.VerifyTopSection(); // this also sets external order id in orderDetailsObjectExpected object that was setup further above.
		OrderSubmittedPage.VerifyTopSectionLowerPart();
		OrderSubmittedPage.VerifyAdditionalInformationTransferServiceOut();
		//OrderSubmittedPage.verifyAdditionalInformationBlock(); // ana's new method
		
		// go to 'my orders' main page to setup for the loop test below.
		CommonTestSteps.GoToMyOrders();
		
		// this loops on going into the 'my orders' page until all verifications on the 'my orders' page pass.  
		// the 'orderActionBlock' variable is set here.
		VerifyLimitedUserOrderMyOrdersMainPage();
		
		CommonTestSteps.GoToMyOrders();
		VerifyOrderDetailsPagePreApproval();
	}
	
	public static void SetOrderTypeForPostApproval()
	{
		orderDetailsObjectExpected.orderType = "Transfer Service Out";
	}

	// this is assumes the user's 'my orders' page is open and loaded. this is done after the approval has been one.
	public static void VerifyOrderDetailsPagePostApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.VerifyTopSectionLowerPart();
		ServiceNow.OrderSubmittedPage.VerifyAdditionalInformationTransferServiceOut();
		//ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock();		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyOrderDetailsHistoryAfterApproval();
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////
	//									Helper Methods Below
	////////////////////////////////////////////////////////////////////////////////////////////////	
	// //////////////////////////////////////////////////////////////////////////////////////////////			
	
	// change order type in orderDetailsObjectExpected for transfer service out
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Transfer Service Out"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}
	
	
	public static void VerifyServiceNumberShownProvideInfoPage()
	{
		Assert.assertEquals(driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).getAttribute("value"), BaseClass.serviceNumber);
	}
	
	public static void VerifyServiceNumberShownVerifyOrderPage()
	{
		Assert.assertEquals(driver.findElement(By.xpath("//td/span")).getText(), BaseClass.serviceNumber);
	}

	// this loops on the test that checks the order block in the 'my orders' main page. it sometimes doesn't sync correctly 
	// so it is looped on.
	public static void VerifyLimitedUserOrderMyOrdersMainPage() throws Exception
	{
		if(!RunTimedCall_VerifyLimitedUserOrdersPage(loopTime))
		{
			Assert.fail("Failed to find correct users information in users order information in TransferServiceOut.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
	}
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePreApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		ServiceNow.OrderSubmittedPage.VerifyTopSectionActionsAfterCommandSync();		
		//ServiceNow.OrderSubmittedPage.VerifyAdditionalInformation(); 
		ServiceNow.OrderSubmittedPage.verifyAdditionalInformationBlock();
		ServiceNow.OrderSubmittedPage.VerifyApprovals();		
		
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyHistoryDetailsForOrderSubmitted();
	}			
	
	
}
