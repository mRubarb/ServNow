package ActionClasses;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.DevicePage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.Frames;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.SelectRegionPage;
import ServiceNow.SideBar;
import ServiceNow.VerifyOrderPage;

public class NewActivation extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a new activation. 
	// and does verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunNewActivation() throws Exception
	{
		CommonTestSteps.GoToHomePage();
		
		// select to create a new order. //a[text()='Create an order']
		driver.findElement(By.xpath("//a[text()='Create an Order']")).click();
		
		// There’s a drop down list to select a country. Click “Next” without choosing Country. – There’s an error message for the required postal code.
		SelectRegionPage.selectCountryFromDropDown();
		SelectRegionPage.clickNextButtonSelectRegion();
		SelectRegionPage.VerifyPostalCodeError();
		
		// enter postal code and  click next.
		SelectRegionPage.fillPostalCodeTextBox("01803");
		SelectRegionPage.clickNextButtonSelectRegion();
		ChooseDevicePage.WaitForPageToLoad();
		
		ChooseDevicePage.SetupForDeviceSelection(); // setup list of objects that hold information for each device.
		ChooseDevicePage.clickNextButton();
		ChooseDevicePage.VerifyNoDeviceSelected();  
		
		// verify can add/delete all devices and verify their shopping cart information.
		ChooseDevicePage.AddRemoveAllDevices();
		
		ShowText("Running loop to find device that has an available plan .............................................................");
		
		// add  device that has a plan in the plans list. 
		boolean planForDevice = false; 
		while (!planForDevice)
		{
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			ChooseDevicePage.clickNextButton();
			planForDevice = ChoosePlanPage.waitForPageToLoadPlans();
		}
		
		ShowText("Found a device that has an available plan."); 
		
		// this gets the name of the first plan in the list and saves it off.
		ChoosePlanPage.StoreNameOfFirstPlanInList();
		
		// Click Next without choosing a plan – There’s an error message for the required selection.
		ChoosePlanPage.clickNextButton(); 
		ChoosePlanPage.VerifyNoPlanSelectedError();
		
		// Click on Add to Cart button for any of the plans listed. Verify that the plan is added to Shopping cart, and that 
		// the Add to Cart button for the selected plan changes to Remove from Cart.  
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();
		ChoosePlanPage.VerifyRemoveFromCartButtonText();

		// Click on Remove from Cart button. Verify that the device is removed from Shopping cart. Add device back to 
		// shopping cart by clicking on Add to Cart button.  
		ChoosePlanPage.clickRemoveFromCartButtonPlan();
		ChoosePlanPage.VerifyPlanIsNotInShoppingCart();
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();	

		// these two clicks get you to the accessories page. 
		// first click will show optional features because specific device is added. the specific device is known to have features.
		// second click gets you to the accessories page. 
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.storeIncludedFeatures();
		ChoosePlanPage.VerifyOptionalFeaturesPresent(); // expecting device being used to have optional features. verify this. 
		ChoosePlanPage.clickNextButton(); // now at accessories page.
		
		// Go Back and make sure you can add (and remove) one or more features to the Shopping Cart.  
		// Verify that as long as you add/remove features to/from Shopping Cart, the Cost Monthly item on 
		// the Shopping Cart gets updated.  
		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		ChooseAccessoriesPage.VerifyDeviceAndPlanCorrect();
		ChooseAccessoriesPage.clickBackBtn(); // go back to plan page.
		ChoosePlanPage.WaitForPageToLoadPlanSelected();  		
		
		// now test rest of step above.
		ChoosePlanPage.clickNextButton(); // get to optional features display in plan page.
		boolean optionalFeaturesAvailable = ChoosePlanPage.storeOptionalFeaturesNamesAndInfoNew();  
		ChoosePlanPage.VerifyStartingShoppingCartValuesAlign();
		
		//  this adds/removes all of the plan options and verifies everything in the shopping cart.
		if (optionalFeaturesAvailable) 
		{
			ShowText("Options found");
			ChoosePlanPage.VerifyAddRemoveCostMonthly();
			ChoosePlanPage.addOptionalFeaturesToShoppingCart(); // this add the first and last optional features to be used at end of this test case.
		}
		else
		{
			ShowText("No Options found");			
		}
		
		// Click Next. You are in the Choose Accessories step. The available accessories for the selected device are listed. 
		ChoosePlanPage.clickNextButton();		
		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		
		// Verify that you can click Next without adding any accessory.
		ChooseAccessoriesPage.clickNextBtn();
		
		// Go Back and make sure you can add (and remove) one or more accessories to/from the Shopping Cart. 
		// Verify that as long as you add/remove accessories, the Cost item on the Shopping Cart gets updated.  
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.clickBackButton();
		boolean accessoriesAvailable = ChooseAccessoriesPage.waitForPageToLoadAccessories();
		ChooseAccessoriesPage.VerifyDeviceAndPlanCorrect();
				
		if (accessoriesAvailable) {
			ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
			ChooseAccessoriesPage.VerifyAddRemoveItemsInAccessories();
			ChooseAccessoriesPage.addAccessoriesToShoppingCart(false);
		}

		// Click Next. You are in the Provide Additional Info step. 
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		
		// Click Next leaving required fields blank. There's an error message for the required fields. Verify there is an error message
		// Fill in all fields and set Reason to New employee. Hit next.
		ProvideAdditionalInfoPage.clickNextBtn();
		//ProvideAdditionalInfoPage.VerifyErrorsAndCompletePage(); // keeping this - says something about business unit only needed for AT&T ?
		ProvideAdditionalInfoPage.VerifyErrorsNewActivation();
		ProvideAdditionalInfoPage.EnterMissingInfoNewActivation();
		ProvideAdditionalInfoPage.clickNextBtn();

		 // Name and Shipping Address info for the employee should be filled out by default. This info can be added/edited on CMD instance.
		//Just need to click next on this step, will fail if info required info is not present.
		EnterShippingInfoPage.WaitForPageLoad();
		
		// verify all data expected to be filled in.
		EnterShippingInfoPage.VerifyCorrectData();

		 // 27.	Select a different Country. The Address fields corresponding to the selected country are loaded. 
		 //     Click Next leaving the address fields blank. There's an error message for the required fields.
		EnterShippingInfoPage.selectGermanyDropDown();
		EnterShippingInfoPage.clickNextBtn();
		EnterShippingInfoPage.VerifyErrorsShippingInfo(Country.Germany);
		EnterShippingInfoPage.SelectDropdownToUnitedStates();
		EnterShippingInfoPage.WaitForPageLoad();

		 // 29.	Check box "Please expedite order".  Click "Next". You are in the "Verify Order" step
		EnterShippingInfoPage.checkExpediteOrder();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();

		VerifyOrderPage.VerifySelectedDeviceDetails();
		VerifyOrderPage.verifySelectedPlanAndOptionalFeaturesDetails();
		VerifyOrderPage.verifyAccessoriesDetails();
		//VerifyOrderPage.verifyAdditionalInformationBlock();
		VerifyOrderPage.VerifyAdditionalInformationNewActivation();
		VerifyOrderPage.VerifyShippingInformation();
		VerifyOrderPage.VerifyShippingInformationExpediteSelected();
		VerifyOrderPage.VerifyCostAndCostMonthly();
		
		// 31. Click Submit Order.  You are in the Order Submitted step.  
		VerifyOrderPage.clickSubmitBtn();
		VerifyOrderPage.WaitForOrderComplete();

		// create and setup order details expected object with order type, order id, and expected status. 
		CreateOrderDetailsExpectedObject(); // this is instantiated in base class. it is setup for Order new device and service.
		SetupOrderDetailsExpectedObject(); // this changes the object properties in the object created above for New Activation. 

		StoreOrderNumberToVariable();
		VerifyOrderPage.VerifyOrderSubmittedInfo();
		
		
		DebugTimeout(2, ""); // wait for SN to hopefully sync with command.
		OrderSubmittedPage.SelectViewOrder();		
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		OrderSubmittedPage.VerifyTopSection();
		// OrderSubmittedPage.verifyAdditionalInformationBlock(); // VerifyAdditionalInformation();	
		OrderSubmittedPage.VerifyAdditionalInformationNewActivation();
		OrderSubmittedPage.VerifyAccountHolderInformation(); 
		OrderSubmittedPage.VerifyApprovals();		
		OrderSubmittedPage.VerifyShippingInformation();
		OrderSubmittedPage.verifyOrderSegments();
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
			Assert.fail("Failed to find correct users information in users order information in NewActivation.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
	}	
	
	public static void VerifyOrderDetailsPostApproval() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		OrderSubmittedPage.VerifyTopSection();
		OrderSubmittedPage.VerifyAdditionalInformationNewActivation();
		OrderSubmittedPage.VerifyAccountHolderInformation(); 
		OrderSubmittedPage.VerifyApprovals();		
		OrderSubmittedPage.VerifyShippingInformation();
		OrderSubmittedPage.verifyOrderSegments();
		VerifyOrderDetailsHistoryAfterApproval();
	}

	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Order New Service"; 
		orderDetailsObjectExpected.orderId = orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = awaitingApprovalStatus;
	}
}

