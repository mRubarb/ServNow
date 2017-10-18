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
		ProvideAdditionalInfoPage.EnterMissingInfoFeatures();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		
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
		ChoosePlanPage.WaitForPageToLoadNoPlanSelected();
		
		
		
		/*
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
		ProvideAdditionalInfoPage.EnterMissingInfoFeatures();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		*/
		
		// VerifyFeature12(state);
	}
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 														HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// this verifies a device exists in the Choose a Device Page for Upgrade Service. It's assumed the page is already open.
	public static void VerifyDeviceExistsInUpgradeService() throws Exception
	{
 
		String errMessageInstructions = " field in 'Features.VerifyDeviceExistsInUpgradeService' method is empty. Need to select a proper value for 'indexMyServices' in the Base Class "
									   + " that will select a service that has an associated device when 'MyServicesPage.SelectUpgradeServiceAction()' is called in this test.";

		
		ShowText(driver.findElement(By.cssSelector("#existing-manufacturer")).getAttribute("label"));
		
		
		// verify manufacturer
		Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-manufacturer"))).getFirstSelectedOption().getText().equals("")
						   ,"Maunufacturer" + errMessageInstructions);
		
		// verify model
		Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-model"))).getFirstSelectedOption().getText().equals("")
				   ,"Model" + errMessageInstructions);
		
		// verify serial number
		Assert.assertFalse(driver.findElement(By.cssSelector("#existing-serial-number")).getAttribute("value").equals("")
				   ,"Serial Number" + errMessageInstructions);
		
		// verify serial number type 
		Assert.assertFalse(new Select(driver.findElement(By.cssSelector("#existing-serial-number-type"))).getFirstSelectedOption().getText().equals("")
				   ,"Maunufacturer" + errMessageInstructions);
		

		
		
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
			Assert.fail("Error in 'RunFeature10()' method. Expected to find " + numberExpectedAddToCartButtons + " devices in the devices list. " +
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
			if(!WaitForElementClickableBoolean(By.cssSelector(".tg-space--half--bottom.ng-scope>label>input"), ShortTimeout))
			{
				Assert.fail("Checkbox for expediting order is not present in 'VerifyFeature12' method.");
			}
			
			// ShowText("Text: " + driver.findElement(By.cssSelector(".tg-space--half--bottom.ng-scope>label")).getText()); // DEBUG
			
			// verify correct text message.
			String expected = " Please expedite this order. (Additional charges may apply.)";
			Assert.assertEquals(driver.findElement(By.cssSelector(".tg-space--half--bottom.ng-scope>label")).getText(), expected);
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
	
	//  this verifies presence or no presence of check-boxes, depending on state passed in, and verifies expected text.  
	public static void VerifyRadioButtonAndText(checkBoxState state) throws Exception
	{

		if(state.equals(checkBoxState.notChecked)) // feature not checked.
		{
			ShowText("Verifying Feature Unchecked.");
			
			// wait for expected number of add to cart buttons.
			WaitForDeviceList();
			
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
		}
		else // feature checked.
		{
			ShowText("Verifying Feature Checked.");
			
			// wait for expected number of add to cart buttons.
			WaitForDeviceList();

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

		}
		
		ShowText("Feature verification passed.");
	}	
}
