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

	public static int numberExpectedAddToCartButtons = 9;
	public static String featureTenExpectedText = "Select a device below to add to your shopping cart.";
		
	public static void RunFeature10() throws Exception
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

		// wait for expected number of add to cart buttons.		
		if(!WaitForElements(By.xpath("//button[text()='Add to Cart']"), MainTimeout, numberExpectedAddToCartButtons))
		{
			Assert.fail("Error in 'RunFeature10()' method. Expected to find " + numberExpectedAddToCartButtons + " devices in the devices list. " +
					    "May need to change expected number of devices variable 'numberExpectedAddToCartButtons' in 'RunFeature10()' method.");
		}
		
		// ShowText(driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText()); 
		Assert.assertEquals(driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText(), featureTenExpectedText,
		                                       "Expected text for Feature 10 is incorrect in method 'RunFeature10()'.");
		
		// driver.findElement(By.cssSelector(".sn-instructions.tg-pad--quarter--top.tg-pad--bottom.ng-binding")).getText();
		// Select a device below to add to your shopping cart.

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS); // this keeps the wait below to 3 seconds.		

		// if the radio button for selecting an existing device exists, this is an error. 
		if(WaitForElementNotClickableNoThrow(By.cssSelector(".tg-grid__cell.tg-one-half.tg-pad--right~div>label>input"), MiniTimeout, ""))
		{
			Assert.fail("Radion button on Devices page appears to be visible. It should not be visible.");
		}
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // set back to original.
	}
	
	// this "tries" to get to the admin settings page and make sure feature 10 to 13 check-boxes are checked. 
	public static void CloseClean() throws Exception
	{
		Frames.switchToDefaultFrame();
		Thread.sleep(3000);		
		System.out.println(WaitForElementNotClickableNoThrow(By.xpath("//button[text()='Logout']"), MediumTimeout, ""));
		Frames.switchToGsftMainFrame();
		Thread.sleep(3000);
		System.out.println(WaitForElementNotClickableNoThrow(By.xpath("//input[@id='user_name']"), MediumTimeout, ""));
		
		
		/*
		// see if can logout
		if(canClickLogout())
		{
			ShowText("can click logout clean");
			//CommonTestSteps.Logout();
			//CommonTestSteps.LoginAdminBrowserOpen();
			//CommonTestSteps.GoToAdminSettings();
			//SettingsPage.SetCheckboxesTrue();			
		}
		
		// see if can login.
		if(canLogin())
		{
			ShowText("can login clean");
			//CommonTestSteps.Logout();
			//CommonTestSteps.LoginAdminBrowserOpen();
			//CommonTestSteps.GoToAdminSettings();
			//SettingsPage.SetCheckboxesTrue();			
		}
		*/		
	}
	

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 														HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// see if can click the logout button
	public static boolean canClickLogout() throws InterruptedException
	{
		Frames.switchToDefaultFrame();
		Thread.sleep(1000);
		if(WaitForElementNotClickableNoThrow(By.xpath("//button[text()='Logout']"), MediumTimeout, ""))
		{
			return false;			
		}
		return true;
	}

	
	public static boolean canLogin()
	{
		Frames.switchToDefaultFrame();
		if(WaitForElementNotClickableNoThrow(By.xpath("//input[@id='user_name']"), MediumTimeout, ""))
		{
			return false;			
		}
		return true;
	}


}
