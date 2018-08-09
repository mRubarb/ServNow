package ActionClasses;

import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.SelectCurrentCarrier;
import ServiceNow.SelectRegionPage;
import ServiceNow.SideBar;
import ServiceNow.SpecialInstructions;
import ServiceNow.VerifyOrderPage;

import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;


public class TransferServiceIn extends BaseClass {

	public static void runTransferServiceInPhoneNumber() throws Exception {
		
		// 2. On the left pane, under Tangoe Mobility, click Home. There are four sections displayed.

		// switch to main frame and wait for text in one of the displayed selections.
		// switch to side bar (navigation) frame and select home.		
		
		Frames.switchToGsftNavFrame();		
		SideBar.clickHomeButton();
		
		// 3. Click 'Transfer my phone number'. You are in the first step of the process: Select Region.
		Frames.switchToGsftMainFrame(); // switch frame so waits can work. 
		
		HomePage.WaitForPageToLoad();
		
		HomePage.clickTransferServiceInButton();
		
		// STEP 1 - SELECT REGION
		
		// 4. There’s a drop down list to select a country. Click “Next” without choosing Country. – There’s an error message for the required field (“Country”).
		// 5. Set Country to ‘United States’. – There’s a field displayed to enter Postal Code.
	
		// TODO -----------------------------------------------  Click “Next” without choosing Country.
		SelectRegionPage.selectCountryFromDropDown();
			
		// 6. Click Next leaving Postal Code blank (or fill it using an incorrect format). There's an error message for the required field Postal Code.
	
		SelectRegionPage.clickNextButtonSelectRegion(); // added waitPresent
		SelectRegionPage.getErrorNoPostalCode();
		SelectRegionPage.verifyNoPostalCodeErrorPresent();
				
		// 7. Fill in Postal Code and click Next. You are in the Choose a Device step. The available Devices for the selected country are listed.
	
		SelectRegionPage.fillPostalCodeTextBox("02451");
		SelectRegionPage.clickNextButtonSelectRegion(); // move to device select page.
		
		
		// STEP 2 - SELECT YOUR CURRENT CARRIER
		
		// Select current carrier from dropdown list
		SelectCurrentCarrier.selectCurrentCarrier("Verizon Wireless");
		
		// Keep the carrier selected - service won't be moved to a different carrier. 
		SelectCurrentCarrier.clickNextButton();
		
		
		// STEP 3 - SPECIAL INSTRUCTIONS
		
		SpecialInstructions.checkAllOptions();
		SpecialInstructions.clickNextButton();
		
		
		// STEP 4 - CHOOSE A DEVICE		*************************
		
		ChooseDevicePage.WaitForPageToLoad();
		ChooseDevicePage.SetupForDeviceSelection(); // setup list of objects that hold information for each device.
		
		// 8. Click Next without selecting any device. There's an error message for the required selection.
		ChooseDevicePage.clickNextButton();
		ChooseDevicePage.VerifyNoDeviceSelected(); 
		
		
		// 9. Click on Add to Cart button for any of the devices listed. Verify that the device is added to Shopping cart, 
		// and that the Add to Cart button, for the selected device, changes to Remove from Cart.
		// 10. Click on Remove from Cart. Verify that the device is removed from Shopping cart. Add device back to shopping cart 
		// by clicking on Add to Cart button.
		// 11. Now click on 'Add to Cart' button for a different device. Verify that the new device is added to 
		// Shopping cart, and that the device selected in previous step has been removed from Shopping cart.  

				
		boolean planForDevice = false; 
		
		while (!planForDevice) {
		
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			
			// 12.	Click Next. You are in the Choose a Plan step. The available plans for the selected device are listed. 
			ChooseDevicePage.clickNextButton();
			planForDevice = ChoosePlanPage.waitForPageToLoadPlans();
			
		}
		
		// STEP 5 - CHOOSE A PLAN
		
		// this gets the name of the first plan in the list and saves it off.
		ChoosePlanPage.StoreNameOfFirstPlanInList(); 
		
			
		// 13.	Click Next without choosing a plan – There’s an error message for the required selection.
		ChoosePlanPage.clickNextButton(); 
		ChoosePlanPage.VerifyNoPlanSelectedError();
		
		// 14.	Click on Add to Cart button for any of the plans listed. Verify that the plan is added to Shopping cart, and that 
		// the Add to Cart button for the selected plan changes to Remove from Cart.  
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();
		ChoosePlanPage.VerifyRemoveFromCartButtonText();
		
		
	    // 15.	Click on Remove from Cart button. Verify that the device is removed from Shopping cart. Add device back to 
		// shopping cart by clicking on Add to Cart button.  

		ChoosePlanPage.clickRemoveFromCartButtonPlan();
		ChoosePlanPage.VerifyPlanIsNotInShoppingCart();
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();	
		
		
		// 16.	If there's more than one plan repeat step 11 for plan). Click Next. 
		// 17.	If there are Optional Features displayed for the plan selected, verify that you can click Next without adding any of them.
		
		// these two clicks get you to the accessories page. 
		// first click will show optional features because specific device is added. the specific device is known to have features.
		// second click gets you to the accessories page. 
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.storeIncludedFeatures();
		ChoosePlanPage.VerifyOptionalFeaturesPresent(); // expecting device being used to have optional features. verify this. 
		ChoosePlanPage.clickNextButton(); // now at accessories page.
		
		
		// STEP 6 - CHOOSE ACCESSORIES
		
		// 18.	Go Back and make sure you can add (and remove) one or more features to the Shopping Cart.  
		// Verify that as long as you add/remove features to/from Shopping Cart, the Cost Monthly item on 
		// the Shopping Cart gets updated.  
		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		ChooseAccessoriesPage.VerifyDeviceAndPlanCorrect();
		ChooseAccessoriesPage.clickBackBtn(); // go back to plan page.
		ChoosePlanPage.WaitForPageToLoadPlanSelected();  
		
		// now test rest of step 18.
		ChoosePlanPage.clickNextButton(); // get to optional features display in plan page.
		boolean optionalFeaturesAvailable = ChoosePlanPage.storeOptionalFeaturesNamesAndInfoNew();  
		ChoosePlanPage.VerifyStartingShoppingCartValuesAlign();
		
		//  this adds/removes all of the plan options and verifies everything in the shopping cart.
		if (optionalFeaturesAvailable) {
			
			ChoosePlanPage.VerifyAddRemoveCostMonthly();
			ChoosePlanPage.addOptionalFeaturesToShoppingCart(); // this add the first and last optional features to be used at end of this test case.
		}
			

		 // 19. Click Next. You are in the Choose Accessories step. The available accessories for the selected device are listed. 
		ChoosePlanPage.clickNextButton();		
		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		
		// 20. Verify that you can click Next without adding any accessory.
		ChooseAccessoriesPage.clickNextBtn();
		
		// 21.	Go Back and make sure you can add (and remove) one or more accessories to/from the Shopping Cart. 
		//      Verify that as long as you add/remove accessories, the Cost item on the Shopping Cart gets updated.  
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.clickBackButton();
		boolean accessoriesAvailable = ChooseAccessoriesPage.waitForPageToLoadAccessories();
		ChooseAccessoriesPage.VerifyDeviceAndPlanCorrect();
				
		if (accessoriesAvailable) {
			ChooseAccessoriesPage.loadAvailableAccessoriesIntoExpectedList();
			ChooseAccessoriesPage.VerifyAddRemoveItemsInAccessories();
			ChooseAccessoriesPage.addAccessoriesToShoppingCart(false);
		}
		
			
		 // 22.	Click Next. You are in the Provide Additional Info step. 
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		
		
		// 23.	Click Next leaving required fields blank. There's an error message for the required fields. Verify there is an error message
		// 24.	Fill in all fields and set Reason to Other. Hit next and verify error.
		ProvideAdditionalInfoPage.clickNextBtn();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		//ProvideAdditionalInfoPage.VerifyErrorsAndCompletePage();
		ProvideAdditionalInfoPage.VerifyDeviceAndPlanSectionsCorrect();

		 // 25.	Fill in Reason and click Next.  You are in the Enter Shipping Info step. 
		ProvideAdditionalInfoPage.clickNextBtn();
		
		 // 26.	Name and Shipping Address info for the employee should be filled out by default. This info can be added/edited on CMD instance.

		//Just need to click next on this step, will fail if info required info is not present.
		EnterShippingInfoPage.WaitForPageLoad();
		
		// verify all data expected to be filled in.
		EnterShippingInfoPage.VerifyCorrectData();
		
				
		EnterShippingInfoPage.SelectDropdownToUnitedStates();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.VerifyErrorsUnitedStates();
				
		 // 29.	Check box "Please expedite order".  Click "Next". You are in the "Verify Order" step
		EnterShippingInfoPage.checkExpediteOrder();
		EnterShippingInfoPage.clickNextBtn();
		
		// create a helper object that stores details about a New Service order.
		CreateOrderDetailsExpectedObject();
		
		 // 30.	The data entered/selected on each step is displayed. Verify that the data displayed matches the selections made on previous steps.
		VerifyOrderPage.WaitForPageToLoad();
		VerifyOrderPage.VerifySelectedDeviceDetails();
		VerifyOrderPage.verifySelectedPlanAndOptionalFeaturesDetails();
		VerifyOrderPage.verifyAccessoriesDetails();
		VerifyOrderPage.verifyAdditionalInformationBlock();		
		VerifyOrderPage.VerifyShippingInformation();		
		VerifyOrderPage.VerifyCostAndCostMonthly();
		
		
		// 31. Click Submit Order.  You are in the Order Submitted step.  
		VerifyOrderPage.clickSubmitBtn();
		VerifyOrderPage.WaitForOrderComplete();
		VerifyOrderPage.VerifyOrderSubmittedInfo();
		DebugTimeout(2, ""); // wait for SN to hopefully sync with command.
		OrderSubmittedPage.SelectViewOrder();		
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		OrderSubmittedPage.VerifyTopSection();
		OrderSubmittedPage.VerifyAdditionalInformation();	
		OrderSubmittedPage.VerifyAccountHolderInformation(); 
		OrderSubmittedPage.VerifyApprovals();		
		OrderSubmittedPage.VerifyShippingInformation();
		 	
	}

	public static void setOrderTypeForPostApproval() {

		orderDetailsObjectExpected.orderType = "Transfer Service In";
	}

	public static void verifyLimitedUserOrderMyOrdersMainPage() throws Exception {

		if(!ActionsBaseClasses.ActionsBase.RunTimedCall_VerifyLimitedUserOrdersPage(ActionsBaseClasses.ActionsBase.loopTime))
		{
			Assert.fail("Failed to find correct users information in users order information in TransferServiceIn.VerifyLimitedUserOrderMyOrdersMainPage.");			
		}
		
	}

	public static void verifyOrderDetailsPagePostApproval() throws Exception {

		ServiceNow.MyOrdersPage.WaitForPageToLoad(); // sanity check.
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();	
		ServiceNow.OrderSubmittedPage.WaitForOrderDetailsPageToLoad();
		ServiceNow.OrderSubmittedPage.VerifyTopSection();
		
		// need this here because post approval order details page can't be checked with 'VerifyTopSectionActionsAfterCommandSync()'. the top section is different in post approval order.		
		ServiceNow.OrderSubmittedPage.VerifyOrderStatus();    
		
		OrderSubmittedPage.verifyAdditionalInformationTransferServiceIn();
		OrderSubmittedPage.VerifyShippingInformation();
		OrderSubmittedPage.verifyStatusAndVendor();
		//OrderSubmittedPage.VerifyDeviceSectionUpgradeDevice();
		//OrderSubmittedPage.VerifyPlanSectionUpgradeDevice();
		
		
	}

	public static void verifyOrderDetailsHistoryPostApproval(ApproverAction appAction) throws Exception {

		ActionsBaseClasses.ActionsBase.VerifyOrderDetailsHistoryAccessoriesAfterApproval(appAction);
	}

	// change order type in orderDetailsObjectExpected for Transfer Service In.
	public static void SetupOrderDetailsExpectedObject()
	{
		orderDetailsObjectExpected.orderType = "Transfer Service In"; 
		orderDetailsObjectExpected.orderId = ActionsBase.orderSubmittedPageOrderNumber;
		orderDetailsObjectExpected.status = ActionsBase.awaitingApprovalStatus;
		
	}
	
	
}
