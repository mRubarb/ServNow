package ActionClasses;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseCarrierPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.DevicePage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyOrdersPage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.VerifyOrderPage;
 

public class PortNumber extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// This is the main method for this class. It goes through the pages needed to do Port Number
	// and does verification. 
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunPortNumber() throws Exception
	{
		// go through the action pages.
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.StoreServiceNumberFormats();
		MyDevicesPage.SelectPorNumberAction();
		
		ChooseCarrierPage.WaitForPageToLoadPortNumber();
		VerifyPageTitle(portNumberTitle);
		ChooseCarrierPage.verifyErrorMessageNoCarrierSelected();
		ChooseCarrierPage.VerifyButtonSelectsAndContinue();
		
		ChooseDevicePage.WaitForPageToLoadPortNumber();	
		VerifyPageTitle(portNumberTitle);
		ChooseDevicePage.AddPortDevice();
		ChooseDevicePage.clickNextButton();
		
		ChoosePlanPage.WaitForPageToLoadNoPlanSelected();
		VerifyPageTitle(portNumberTitle);		
		ChoosePlanPage.AddPortPlan();
		ChoosePlanPage.clickNextButton();

		ChoosePlanPage.StoreIncludedFeaturesCss();

		boolean optionalFeaturesAvailable = ChoosePlanPage.storeOptionalFeaturesNamesAndInfoNew();
		if (optionalFeaturesAvailable) {
			ChoosePlanPage.selectFirstLastOptionalFeature();
		}
		ChoosePlanPage.clickNextButton();

		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		boolean accessoriesAvailable = ChooseAccessoriesPage.waitForPageToLoadAccessories();
		//ChooseAccessoriesPage.VerifyDeviceAndPlanCorrect();
				
		if (accessoriesAvailable) {
			ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
			ChooseAccessoriesPage.VerifyAddRemoveItemsInAccessories();
			ChooseAccessoriesPage.addAccessoriesToShoppingCart(false);
		}
		
		ChooseAccessoriesPage.clickNextBtn();
		
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.clickNextBtn();

		ProvideAdditionalInfoPage.EnterMissingInfoPortNumber();
		//ProvideAdditionalInfoPage.VerifyDeviceAndPlanSectionsCorrect();
		ProvideAdditionalInfoPage.clickNextBtn();

		//Just need to click next on this step, will fail if required info is not present.
		EnterShippingInfoPage.WaitForPageLoad();
		
		// verify all data expected to be filled in.
		EnterShippingInfoPage.VerifyCorrectData();
		
		EnterShippingInfoPage.SelectDropdownToUnitedStates();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.VerifyErrorsUnitedStates();
		BaseClass.stepComplete("TC:0001", "TS:28");
		
		 // 29.	Check box "Please expedite order".  Click "Next". You are in the "Verify Order" step
		EnterShippingInfoPage.checkExpediteOrder();
		EnterShippingInfoPage.clickNextBtn();
		BaseClass.stepComplete("TC:0001", "TS:29");
		
		// create a helper object that stores details about a New Service order.
		CreateOrderDetailsExpectedObject();
		
		 // 30.	The data entered/selected on each step is displayed. Verify that the data displayed matches the selections made on previous steps.
		VerifyOrderPage.WaitForPageToLoad();
		VerifyOrderPage.verifySelectedDeviceDetailsPortNumber();  	
		VerifyOrderPage.verifySelectedPlanAndOptionalFeaturesDetails(); 
		VerifyOrderPage.verifyAccessoriesDetails();
		// VerifyOrderPage.verifyAdditionalInformationBlock();  //VerifyAdditionalInformation();
		VerifyOrderPage.VerifyAdditionalInformationPortNumber();
		VerifyOrderPage.VerifyShippingInformation();		
		VerifyOrderPage.VerifyCostAndCostMonthly();
		BaseClass.stepComplete("TC:0001", "TS:30");
		
		// 31. Click Submit Order.  You are in the Order Submitted step.  
		VerifyOrderPage.clickSubmitBtn();
		VerifyOrderPage.WaitForOrderComplete();
		VerifyOrderPage.VerifyOrderSubmittedInfo();
		DebugTimeout(2, ""); // wait for SN to hopefully sync with command.
		
		StoreOrderNumberToVariable(); // The order number is shown in the order submitted page.
		
		Thread.sleep(2000); // wait two seconds before selecting to view the order.
		
		OrderSubmittedPage.SelectViewOrder();		
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		OrderSubmittedPage.VerifyTopSection();
		//OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationPortNumber();	
		OrderSubmittedPage.VerifyAdditionalInformationPortNumber();
		OrderSubmittedPage.VerifyAccountHolderInformation(); 
		OrderSubmittedPage.VerifyApprovals();		
		OrderSubmittedPage.VerifyShippingInformation();
		OrderSubmittedPage.verifyOrderSegments();
		
				
		// create and setup order details expected object with order type, order id, and expected status. 
		//CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
		SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for deactivate service. 
		
		// this verifies the order number in verify page matches the order number in order details 
		// page and verifies the correct order type at the top of the order details (submitted) page.
		VerifyOrderNumberAndOrderTypeBetweenPages(); 

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
		OrderSubmittedPage.VerifyApprovals();	
		OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationPortNumber();
		OrderSubmittedPage.VerifyShippingInformation(); // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.verifyOrderSegmentDevice();
		OrderSubmittedPage.verifyOrderSegmentPlan();
				
		// the order details page is open. it has synced with command so now the history section can be verified. 
		// use VerifyHistoryDetailsForOrderSubmittedOrderAccessories. it doesn't use tbody to find the history section. 
		//VerifyHistoryDetailsForOrderSubmittedOrderAccessories();
	}
	
	// this is assumes the user's 'my orders' page is open and loaded. 
	public static void VerifyOrderDetailsPagePostApproval(ApproverAction action) throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		//OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformationPortNumber();
		OrderSubmittedPage.VerifyAdditionalInformationPortNumber();		
		OrderSubmittedPage.VerifyShippingInformation(); // VerifyShippingInformationOrderAccessoriesPostApproval();	// re-use accessories method.
		OrderSubmittedPage.verifyStatusAndVendor();
		OrderSubmittedPage.verifyOrderSegmentDevice();
		OrderSubmittedPage.verifyOrderSegmentPlan();
			
		// the order details page is open. it has synced with command so now the history section can be verified. 
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(action);
	}
	

	
	public static void VerifyOrderDetailsHistoryPostApproval(ApproverAction appAction) throws Exception
	{
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
	}
	
	public static void GetCurrentVendor() throws Exception 
	{
		CommonTestSteps.GoToDevicesPage();
		MyDevicesPage.WaitForPageToLoad();
		
		// Click 'View Device' button
		driver.findElement(By.xpath("//div/div[" + indexMyDevices + "]/div/div/button[text()='View Device']")).click();
		
		DevicePage.WaitForPageToLoad();
		DevicePage.setCurrentVendorToDeviceInfoActions();
		
		// Duplicated line, it's included in the method called in line above
		//DeviceInfoActions.currentVendorPortNumber = driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText();
		
		// store the logged in user's carrier for port number transaction. for port number, the user's carrier is what becomes the current carrier.
		// verified this in the command port number transaction.
		if(approvalActionType.equals(approvalActionType.portNumber))
		{
			WaitForElementVisible(By.xpath("(//label[text()='Vendor']/following ::span)[1]"), ShortTimeout);
			currentCarrierPortNumber = driver.findElement(By.xpath("(//label[text()='Vendor']/following ::span)[1]")).getText(); 
			System.out.println("currentCarrierPortNumber for Port Number transaction: " + currentCarrierPortNumber);
		}
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helper methods below.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	

	// change order type in orderDetailsObjectExpected for Port Number.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Port Number"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
				
	}
	
	public static void verifyOrderDetailsHistoryPostApproval(ApproverAction appAction) throws Exception
	{
		VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
	}
}

