package ActionClasses;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.EnterShippingInfoPage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.MyDevicesPage;
import ServiceNow.MyServicesPage;
import ServiceNow.OrderNewServicePage;
import ServiceNow.ProvideAdditionalInfoPage;
import ServiceNow.SettingsPage;
import ServiceNow.SideBar;
import ServiceNow.VerifyOrderPage;


public class Features extends BaseClass 
{

	static public enum checkBoxState
	{
		checked,
		notChecked
	}
	
	
	public static int numberExpectedAddToCartButtons = 0;
	public static String featureTenExpectedText = "Select a device below to add to your shopping cart.";
		
	public static void RunFeature10(checkBoxState state) throws Exception
	{
		// 
		Frames.switchToGsftNavFrame();
		SideBar.clickHomeButton();
		Frames.switchToGsftMainFrame();
		HomePage.WaitForPageToLoad();
		HomePage.clickCreateAnOrderButton();
		OrderNewServicePage.selectCountryFromDropDown();
		OrderNewServicePage.fillPostalCodeTextBox("02451");
		OrderNewServicePage.clickNextButtonSelectRegion(); // move to device select page.
		ChooseDevicePage.WaitForPageToLoad();

		numberExpectedAddToCartButtons = 9; // setup for expected number of devices in device list.
		
		VerifyRadioButtonAndText(state);

	}

	public static void RunFeature11(checkBoxState state) throws Exception
	{
		// get to the upgrade device page.
		Frames.switchToGsftNavFrame();
		CommonTestSteps.GoToDevicesPage();
		MyDevicesPage.WaitForPageToLoad();
		MyDevicesPage.SelectUpgradeDeviceAction();
		ChooseDevicePage.WaitForPageToLoadUpgradeDevice();

		numberExpectedAddToCartButtons = 5; // setup for expected number of devices in device list.
		
		VerifyRadioButtonAndText(state); // verify.
	}
	
	// this goes through create order to the shipping info page and then verifies expected result.
	public static void RunFeature12(checkBoxState state) throws Exception
	{
		Frames.switchToGsftNavFrame();
		SideBar.clickHomeButton();
		Frames.switchToGsftMainFrame();
		HomePage.WaitForPageToLoad();
		HomePage.clickCreateAnOrderButton();
		OrderNewServicePage.selectCountryFromDropDown();
		OrderNewServicePage.fillPostalCodeTextBox("02451");
		OrderNewServicePage.clickNextButtonSelectRegion(); // move to device select page.
		ChooseDevicePage.SelectFirstDevice(); // this waits for first device and clicks it if found else error.
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.SelectFirstPlan(); // this waits for first plan and clicks it if found else error.
		ChoosePlanPage.clickNextButton();
		ChoosePlanPage.clickNextButton(); // this gets to accessories page.		
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.EnterMissingInfoFeatures(ProvideAdditionalInfoPage.selectedFeature = ProvideAdditionalInfoPage.SelectedFeature.feature12);
		ProvideAdditionalInfoPage.clickNextBtn();

		
		VerifyFeature12(state);
	}
	
	// this goes through create order to the shipping info page and then verifies expected result.
	public static void RunFeature13(checkBoxState state) throws Exception
	{
		Frames.switchToGsftNavFrame();
		SideBar.clickMyServicesBtn();
		Frames.switchToGsftMainFrame();
		MyServicesPage.WaitForPageToLoad();
		MyServicesPage.SelectUpgradeServiceAction();
		ChooseDevicePage.WaitForPageToLoadUpgradeService();
		VerifyDeviceExistsInUpgradeService(); // make sue a device is shown in Upgrade Service page. 
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanOrig();
		ChoosePlanPage.SelectFirstPlan();
		ChoosePlanPage.clickNextButtonSimple();
		ChoosePlanPage.clickNextButtonSimple();
		ProvideAdditionalInfoPage.WaitForPageToLoadSuspendLongWait();
		ProvideAdditionalInfoPage.EnterMissingInfoFeatures(ProvideAdditionalInfoPage.selectedFeature = ProvideAdditionalInfoPage.SelectedFeature.feature13);
		
		VerifyFeature13(state);
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 														HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void VerifyFeature13(checkBoxState state) throws Exception // bladd
	{
		 // with check-box checked there should be a shipping info page.
		if(state.equals(checkBoxState.checked)) 
		{
			WaitForElementVisible(By.xpath("//div[text()='Enter Shipping Info']"), MediumTimeout); // verify the radio button for shipping info is visible.
			ProvideAdditionalInfoPage.clickNextBtn();
			EnterShippingInfoPage.WaitForPageLoadExtended();
			
			// verify expedited section, text in main title bar, and the postal code entry are shown.
			VerifyExpediteSectionInShippingInfoExists(); 
			VerifyShippingInfoOtherItems();
			
			ShowText("Feature 13 checked test passed.");
		}
		else
		{
			// at this point information has been entered into the provide additional information page.
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the MiniTimeout wait below to 3 seconds.
			if(WaitForElementVisibleNoThrow(By.xpath("//div[text()='Enter Shipping Info']"), MiniTimeout)) // verify the radio button for shipping info is not visible.
			{
				Assert.fail("Fail in Features 'VerifyFeature13' method. The radio button for shipping info appears to be visible.");
			}
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // back to original.  

			ProvideAdditionalInfoPage.clickNextBtn(); // go to verify order page. 
			VerifyOrderPage.WaitForPageToLoad();
			
			// verify this is order page
			String expected = "Verify your order.";
			Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Verify your order.']")).getText(), expected, "");
			
			String actual = driver.findElement(By.xpath("//div/span[text()='This request contains errors.']")).getText();
			expected = "This request contains errors.";
			Assert.assertEquals(actual, expected, "");
			
			actual = driver.findElement(By.xpath("//div/span[text()='This request contains errors.']/..")).getText();
			expected = "This request contains errors. Please note that making corrections to one part of the order could affect subsequent steps in the order process."; 
			Assert.assertEquals(actual, expected, "");
			
			//Pause("b4 click");

			driver.findElement(By.xpath("//button[text()='Begin Fixing Errors']")).click();
			
			// Pause("after click");

			String expectedText = "";
			String errMess = "Expected text " +  expectedText + "is not present";
			
			// //////////////////////////////////////////////////////////////////////////////////////
			// verify the device page is present after clicking on 'Begin Fixing Errors' above.
			// //////////////////////////////////////////////////////////////////////////////////////
			ChooseDevicePage.WaitForPageToLoadUpgradeService();
			
			expectedText = "Choose your new device.";			
			//ShowText(driver.findElement(By.cssSelector(".sn-flow__heading.ng-binding")).getText());
			Assert.assertEquals(driver.findElement(By.cssSelector(".sn-flow__heading.ng-binding")).getText(), 
								expectedText, errMess);
			
			expectedText = "Error: SHIPPING_ADDRESS is required.";
			//ShowText(driver.findElement(By.cssSelector(".sn-notifyBlockList>li")).getText());
			Assert.assertEquals(driver.findElement(By.cssSelector(".sn-notifyBlockList>li")).getText(), expectedText, errMess);
			
			ShowText("Feature 13 not checked test passed.");
		}
	}
	
	// this verifies a device exists in the Choose a Device Page for Upgrade Service. It's assumed the page is already open.
	public static void VerifyDeviceExistsInUpgradeService() throws Exception
	{
		String errMessageInstructions = " field in 'Features.VerifyDeviceExistsInUpgradeService' method is empty. Need to select a proper value for 'indexMyServices' in the Base Class "
									   + " that will select a service that has an associated device when 'MyServicesPage.SelectUpgradeServiceAction()' is called in this test.";

		//driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); // this keeps the MiniTimeout wait below to 3 seconds.
		
		// /////////////////////////////////////////////////////////////////////////////////
		// leaving many of these uncommented because they makes this section  real slow.
		// /////////////////////////////////////////////////////////////////////////////////
		
		// verify manufacturer
		Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-manufacturer"))).getFirstSelectedOption().getText().equals("")
						   ,"Maunufacturer" + errMessageInstructions);
		
		// verify model
		//Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-model"))).getFirstSelectedOption().getText().equals("")
		//		   ,"Model" + errMessageInstructions);
		
		// verify serial number
		Assert.assertFalse(driver.findElement(By.cssSelector("#existing-serial-number")).getAttribute("value").equals("")
				   ,"Serial Number" + errMessageInstructions);
		
		// verify serial number type 
		//Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-serial-number-type"))).getFirstSelectedOption().getText().equals("")
		//		   ,"Existing Serial Number" + errMessageInstructions);
		
		//driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the MiniTimeout wait below to 3 seconds.
	}	
	
	public static void CleanUpFailedTest() throws Exception
	{
		ShowText("Running Clean Up method beccause complete test has not been run.");
		CommonTestSteps.Logout();
		CommonTestSteps.LoginAdminBrowserOpen();
		CommonTestSteps.GoToAdminSettings();
		SettingsPage.SetCheckboxesTrue();			
	}
	
	public static void WaitForDeviceList() throws Exception
	{
		// wait for expected number of add to cart buttons.		
		if(!WaitForElements(By.xpath("//button[text()='Add to Cart']"), MainTimeout, numberExpectedAddToCartButtons))
		{
			Assert.fail("Error in 'RunFeature' test. Expected to find " + numberExpectedAddToCartButtons + " devices in the devices list. " +
					    "May need to change expected number of devices variable 'numberExpectedAddToCartButtons' in 'RunFeature10()' method.");
		}
	}
	
	// see if can click the logout button
	public static boolean canClickLogout() throws InterruptedException
	{
		Frames.switchToDefaultFrame();
		Thread.sleep(1000);
		if(WaitForElementClickableBoolean(By.xpath("//button[text()='Logout']"), MediumTimeout))
		{
			return true;			
		}
		return false;
	}

	// see if can find the login text box.
	public static boolean canLogin()
	{
		Frames.switchToGsftMainFrame();
		if(WaitForElementClickableBoolean(By.xpath("//input[@id='user_name']"), MediumTimeout))
		{
			return true;
		}
		return false;
	}


	// this "tries" to get to the admin settings page and make sure feature 10 to 13 check-boxes are checked. 
	public static void CloseClean() throws Exception
	{
		// see if can logout
		if(canClickLogout())
		{
			ShowText("Clicking logout and making sure Feature 10 through 13 are selected.");
			CommonTestSteps.Logout();
			CommonTestSteps.LoginAdminBrowserOpen();
			CommonTestSteps.GoToAdminSettings();
			SettingsPage.SetCheckboxesTrue();	
			return;
		}
		
		// see if can login.
		if(canLogin())
		{
			ShowText("Logging in as admin and making sure Feature 10 through 13 are selected.");
			CommonTestSteps.LoginAdminBrowserOpen();
			CommonTestSteps.GoToAdminSettings();
			SettingsPage.SetCheckboxesTrue();			
		}
	}
	

	// this is used to verify feature 12 with the check-box checked and not checked.
	public static void VerifyFeature12(checkBoxState state) throws Exception
	{
		// check that expedite check-box can be clicked and the text message is correct.
		if(state.equals(checkBoxState.checked))
		{
			VerifyExpediteSectionInShippingInfoExists(); // verify expedited section is shown. 
			ShowText("Verify feature 12 checked test passed.");
		}
		else
		{
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the MiniTimeout wait below to 3 seconds.
			
			// check there is no expedite check-box to be clicked and the text message is not there.  
			if(WaitForElementClickableBoolean(By.cssSelector(".tg-space--half--bottom.ng-scope>label>input"), ShortTimeout)) // no check-box.
			{
				Assert.fail("Checkbox for expediting order should not be present in 'VerifyFeature12' method.");
			}
			
			if(WaitForElementClickableBoolean(By.cssSelector(".tg-space--half--bottom.ng-scope>label"), MiniTimeout)) // no text message.
			{
				Assert.fail("Text for expediting order should not be present in 'VerifyFeature12' method.");
			}
			
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // back to original.
			
			ShowText("Verify feature 12 un-checked test passed.");
		}
	}
	
	// verify expedite order section in shipping info.
	public static void VerifyExpediteSectionInShippingInfoExists() throws Exception
	{
		if(!WaitForElementClickableBoolean(By.cssSelector(".tg-space--half--bottom.ng-scope>label>input"), ShortTimeout)) // checkbox
		{
			Assert.fail("Checkbox for expediting order is not present in 'VerifyFeature12' method.");
		}
		
		// ShowText("Text: " + driver.findElement(By.cssSelector(".tg-space--half--bottom.ng-scope>la;bel")).getText()); // DEBUG
		
		// verify correct text message.
		String expected = " Please expedite this order. (Additional charges may apply.)";
		String errMessage = "";
		Assert.assertEquals(driver.findElement(By.cssSelector(".tg-space--half--bottom.ng-scope>label")).getText(), expected, errMessage); // text
	}
	
	// this verifies the title bar is presnt and the postal code text box is clickable.  
	public static void VerifyShippingInfoOtherItems() throws Exception
	{
		WaitForElementVisible(By.cssSelector(".sn-flow__heading.ng-binding"), MediumTimeout);
		
		// ShowText(driver.findElement(By.cssSelector(".sn-flow__heading.ng-binding")).getText());

		String expected = "Where should we ship this order?";
		Assert.assertEquals(driver.findElement(By.cssSelector(".sn-flow__heading.ng-binding")).getText(), expected,"");
		
		WaitForElementClickable(By.id("ADDRESS_FORMAT_POSTAL_CODE"), MainTimeout, "Failed to find the Postal Code field in Enter Shipping Info page.");

	}
	
	
	//  this verifies presence or no presence of check-boxes, depending on state passed in, and verifies expected text.  
	public static void VerifyRadioButtonAndText(checkBoxState state) throws Exception
	{

		if(state.equals(checkBoxState.notChecked)) // feature not checked.
		{
			ShowText("Verifying Feature Unchecked.");
			
			// wait for expected number of add to cart buttons.
			// WaitForDeviceList(); // commented 10/23/17 - non needed.
			
			// verify expected text.
			Assert.assertEquals(driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText(), featureTenExpectedText,
			                                       "Expected text for Feature 10 is incorrect in method 'RunFeature10()'.");
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the wait below to 3 seconds.		

			//System.out.println(WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right~div>label>input"), ShortTimeout)); // DEBUG
			
			// if the radio button for selecting an existing device exists, this is an error. it should not be there 
			if(WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right~div>label>input"), ShortTimeout))
			{
				Assert.fail("Radio button on Devices page for existing device appears to be visible. It should not be visible.");
			}
			
			// if the radio button for selecting a new device does not exist, this is an error. 
			if(WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right>label>input"), ShortTimeout))
			{
				Assert.fail("Radio button on Devices page for new device appears to NOT be visible. It should be visible.");
			}
			
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // set back to original.
			
			ShowText("Verifying Feature Unchecked Passed.");
		}
		else // feature checked.
		{
			ShowText("Verifying Feature Checked.");
			
			// wait for expected number of add to cart buttons.
			// WaitForDeviceList(); // commented 10/23/17 - non needed.

			// System.out.println(WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right~div>label>input"), ShortTimeout)); // DEBUG
			
			// verify expected text.
			Assert.assertEquals(driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText(), featureTenExpectedText,
			                                       "Expected text for Feature 10 is incorrect in method 'RunFeature10()'.");

			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the wait below to 3 seconds.
			
			// if the radio button for selecting an existing device doesn't exist, this is an error. 
			if(!WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right>label>input"), ShortTimeout))
			{
				Assert.fail("Radion button on Devices page appears NOT to be visible. It should be visible.");
			}

			// if the radio button for selecting a new device does not exist, this is an error. 
			if(!WaitForElementClickableBoolean(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right~div>label>input"), ShortTimeout))
			{
				Assert.fail("Radion button on Devices page for new device appears to NOT be visible. It should be visible.");
			}
			
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // set back to original.
			
			ShowText("Verifying Feature Checked Passed.");

		}
		
		ShowText("Feature verification passed.");
	}	
}
