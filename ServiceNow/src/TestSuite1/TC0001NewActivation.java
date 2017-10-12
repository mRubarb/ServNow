package TestSuite1;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.OrderNewServicePage;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.SideBar;
import ServiceNow.VerifyOrderPage;


public class TC0001NewActivation extends BaseClass
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		//userLoginMode = UserLoginMode.Limited;  //UserLoginMode.Admin; // setup login for admin user.
		//setUpDriver(DriverSetup.ServiceNow);
	}
	
	@Test
	public static void tc0001NewActivation() throws Exception
	{
	
		// **** This is to verify the merge . *****
		approvalActionType = ApprovalActionType.newActivation; 
		
		// WaitForElementClickable(By.xpath("//div[text()='Service Automation']"), MainTimeout , "Failed wait in step 1."); // this waits for an item in the top window.
	
		// 1. Log into ServiceNow as test user.
		//Frames.switchToGsftMainFrame();
		//LoginPage.Login();
		
		CommonTestSteps.LoginLimitedUser();
		BaseClass.stepComplete("TC:0001", "TS:01");

		 // 2. On the left pane, under Tangoe Mobility, click Home. There are four sections displayed.

		// switch to main frame and wait for text in one of the displayed selections.
		// switch to side bar (navigation) frame and select home.		
		
		Frames.switchToGsftNavFrame();		
		SideBar.clickHomeButton();
		BaseClass.stepComplete("TC:0001", "TS:02");
		 
		// 3. Click Create an Order on the Order a New Device and Service section. You are in the first step of the process: Select Region.
		Frames.switchToGsftMainFrame(); // switch frame so waits can work. 
		
		HomePage.WaitForPageToLoad();
		
		HomePage.clickCreateAnOrderButton();
		BaseClass.stepComplete("TC:0001", "TS:03");
		
		// 4. There’s a drop down list to select a country. Click “Next” without choosing Country. – There’s an error message for the required field (“Country”).
		// 5. Set Country to ‘United States’. – There’s a field displayed to enter Postal Code.
	
		// TODO -----------------------------------------------  Click “Next” without choosing Country.
		OrderNewServicePage.selectCountryFromDropDown();
		BaseClass.stepComplete("TC:0001", "TS:04");
	
		// 6. Click Next leaving Postal Code blank (or fill it using an incorrect format). There's an error message for the required field Postal Code.
	
		OrderNewServicePage.clickNextButtonSelectRegion(); // added waitPresent
		OrderNewServicePage.getErrorNoPostalCode();
		OrderNewServicePage.verifyNoPostalCodeErrorPresent();
		BaseClass.stepComplete("TC:0001", "TS:06");
		
		// 7. Fill in Postal Code and click Next. You are in the Choose a Device step. The available Devices for the selected country are listed.
	
		OrderNewServicePage.fillPostalCodeTextBox("02451");
		OrderNewServicePage.clickNextButtonSelectRegion(); // move to device select page.
		ChooseDevicePage.WaitForPageToLoad();
		BaseClass.stepComplete("TC:0001", "TS:07");

		ChooseDevicePage.SetupForDeviceSelection(); // setup list of objects that hold information for each device.
		
		// 8. Click Next without selecting any device. There's an error message for the required selection.
		ChooseDevicePage.clickNextButton();
		ChooseDevicePage.VerifyNoDeviceSelected(); 
		BaseClass.stepComplete("TC:0001", "TS:08");
		
		// 9. Click on Add to Cart button for any of the devices listed. Verify that the device is added to Shopping cart, 
		// and that the Add to Cart button, for the selected device, changes to Remove from Cart.
		// 10. Click on Remove from Cart. Verify that the device is removed from Shopping cart. Add device back to shopping cart 
		// by clicking on Add to Cart button.
		// 11. Now click on 'Add to Cart' button for a different device. Verify that the new device is added to 
		// Shopping cart, and that the device selected in previous step has been removed from Shopping cart.  

		ChooseDevicePage.AddRemoveAllDevices(); //<-- *** UNCOMMENT LATER *****
		
		boolean planForDevice = false; 
		
		while (!planForDevice) {
		
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			
			BaseClass.stepComplete("TC:0001", "TS:09, TS:10, TS:11");

			// 12.	Click Next. You are in the Choose a Plan step. The available plans for the selected device are listed. 
			ChooseDevicePage.clickNextButton();
			planForDevice = ChoosePlanPage.waitForPageToLoadPlans();
			
		}
				
		// this gets the name of the first plan in the list and saves it off.
		ChoosePlanPage.StoreNameOfFirstPlanInList(); 
		
		BaseClass.stepComplete("TC:0001", "TS:12");
	
		// 13.	Click Next without choosing a plan – There’s an error message for the required selection.
		ChoosePlanPage.clickNextButton(); 
		ChoosePlanPage.VerifyNoPlanSelectedError();
		BaseClass.stepComplete("TC:0001", "TS:13");

		// 14.	Click on Add to Cart button for any of the plans listed. Verify that the plan is added to Shopping cart, and that 
		// the Add to Cart button for the selected plan changes to Remove from Cart.  
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();
		ChoosePlanPage.VerifyRemoveFromCartButtonText();
		BaseClass.stepComplete("TC:0001", "TS:14");
		
	    // 15.	Click on Remove from Cart button. Verify that the device is removed from Shopping cart. Add device back to 
		// shopping cart by clicking on Add to Cart button.  

		ChoosePlanPage.clickRemoveFromCartButtonPlan();
		ChoosePlanPage.VerifyPlanIsNotInShoppingCart();
		ChoosePlanPage.clickAddToCartButtonPlan();
		ChoosePlanPage.VerifyPlanAndDeviceInShoppingCart();	
		BaseClass.stepComplete("TC:0001", "TS:15");
		
		// TODO #16 --- NOT doing this. only use one plan. Only click next.
		// 16.	If there's more than one plan repeat step 11 for plan). Click Next. 
		// 17.	If there are Optional Features displayed for the plan selected, verify that you can click Next without adding any of them.
		
		// these two clicks get you to the accessories page. 
		// first click will show optional features because specific device is added. the specific device is known to have features.
		// second click gets you to the accessories page. 
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.storeIncludedFeatures();
		ChoosePlanPage.VerifyOptionalFeaturesPresent(); // expecting device being used to have optional features. verify this. 
		ChoosePlanPage.clickNextButton(); // now at accessories page.
		BaseClass.stepComplete("TC:0001", "TS:16");
		BaseClass.stepComplete("TC:0001", "TS:17");
		
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
			
		BaseClass.stepComplete("TC:0001", "TS:18");

		 // 19. Click Next. You are in the Choose Accessories step. The available accessories for the selected device are listed. 
		ChoosePlanPage.clickNextButton();		
		BaseClass.stepComplete("TC:0001", "TS:19");
		ChooseAccessoriesPage.waitForPageToLoadAccessories();
		
		// 20. Verify that you can click Next without adding any accessory.
		ChooseAccessoriesPage.clickNextBtn();
		BaseClass.stepComplete("TC:0001", "TS:20");
		
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
		
		BaseClass.stepComplete("TC:0001", "TS:21");
			
		 // 22.	Click Next. You are in the Provide Additional Info step. 
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		
		BaseClass.stepComplete("TC:0001", "TS:22");
		
		// 23.	Click Next leaving required fields blank. There's an error message for the required fields. Verify there is an error message
		// 24.	Fill in all fields and set Reason to Other. Hit next and verify error.
		ProvideAdditionalInfoPage.clickNextBtn();
		ProvideAdditionalInfoPage.VerifyErrorsAndCompletePage();
		ProvideAdditionalInfoPage.VerifyDeviceAndPlanSectionsCorrect();
		BaseClass.stepComplete("TC:0001", "TS:23, 24");

		 // 25.	Fill in Reason and click Next.  You are in the Enter Shipping Info step. 
		ProvideAdditionalInfoPage.clickNextBtn();
		BaseClass.stepComplete("TC:0001", "TS:25");
		
		 // 26.	Name and Shipping Address info for the employee should be filled out by default. This info can be added/edited on CMD instance.

		//Just need to click next on this step, will fail if info required info is not present.
		EnterShippingInfoPage.WaitForPageLoad();
		
		// verify all data expected to be filled in.
		EnterShippingInfoPage.VerifyCorrectData();
		BaseClass.stepComplete("TC:0001", "TS:26");
		
		 // 27.	Select a different Country. The Address fields corresponding to the selected country are loaded. 
		 //     Click Next leaving the address fields blank. There's an error message for the required fields.
		EnterShippingInfoPage.selectGermanyDropDown();
		EnterShippingInfoPage.clickNextBtn();
		
		EnterShippingInfoPage.VerifyErrorsGermany();
		BaseClass.stepComplete("TC:0001", "TS:27");

		 // 28.	Fill in required fields. 
		EnterShippingInfoPage.PopulateFieldsForGermany();
		
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
		VerifyOrderPage.VerifySelectedDeviceDetails();
		VerifyOrderPage.verifySelectedPlanAndOptionalFeaturesDetails();
		VerifyOrderPage.verifyAccessoriesDetails();
		VerifyOrderPage.verifyAdditionalInformationBlock();		
		VerifyOrderPage.VerifyShippingInformation();		
		VerifyOrderPage.VerifyCostAndCostMonthly();
		BaseClass.stepComplete("TC:0001", "TS:30");
		
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
		OrderSubmittedPage.verifyOrderSegments();

		BaseClass.stepComplete("TC:0001", "TS:31");

		
/*		// Section commented since the last step in CMD is not working. Needs to be fixed.	
		// close service now browser.
		driver.close();
		driver.quit();

		// setup actual object that will be populated with info from command. 
		CreateOrderDetailsActualObject();
		
		// start command browser and get to users orders on order status page. 
		setUpDriver(DriverSetup.Command);
		Command.MainLoginPage.Login();
		Command.OrderStatusPage.GoToOrderStatus();
		Command.OrderStatusPage.SearchUserByLastFirstName();
		
		// populate the actual order details object with order info for user's latest order. 
		Command.OrderStatusPage.GetOrderDetailsActual();
		
		// compare actual order object to expected order object.
		CompareActualDetailsObjects();
		BaseClass.stepComplete("TC:0001", "Verify order information with commnd info.");
*/
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}
}
