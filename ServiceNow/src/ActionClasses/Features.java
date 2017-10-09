package ActionClasses;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.BaseClass;
import ServiceNow.ChooseDevicePage;
import ServiceNow.Frames;
import ServiceNow.HomePage;
import ServiceNow.MyDevicesPage;
import ServiceNow.OrderNewServicePage;
import ServiceNow.SettingsPage;
import ServiceNow.SideBar;


public class Features extends BaseClass 
{

	static public enum checkBoxState
	{
		checked,
		notChecked
	}
	
	
	public static int numberExpectedAddToCartButtons = 9;
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
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 														HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
