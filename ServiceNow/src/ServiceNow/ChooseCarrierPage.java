package ServiceNow;

import org.openqa.selenium.By;
import org.testng.Assert;

public class ChooseCarrierPage extends BaseClass 
{

	public static String errMessage = "";

	// these are expected error  messages. 
	static String carrierNeedsSelectedErrorOne = "Please fix the following validation errors:";
	static String carrierNeedsSelectedErrorTwo = "Please select a carrier";
	
	public static void WaitForPageToLoadPortNumber() throws Exception
	{
		errMessage = "Failed wait in ChooseCarrierPage.WaitForPageToLoadPortNumber";		
		
		// wait for next button at bottom of page and wait for other items.
		WaitForElementClickable(By.xpath("(//div/button[text()='Next'])[2]"), ExtremeTimeout, errMessage); // the second 'next' button.
		WaitForElementClickable(By.xpath("(//button[text()='Select'])[1]"), MediumTimeout, errMessage); // first button		
		WaitForElementPresent(By.cssSelector(".timing_span > a"), MainTimeout); // some text at the bottom of the page.		
	}		
 
	public static void verifyErrorMessageNoCarrierSelected() throws Exception
	{
		errMessage = "Failed error message check in ChoosecarrierPage.VerifyErrorMesage";
		
		driver.findElement(By.xpath("//button[text()='Next']")).click();
		WaitForElementVisible(By.cssSelector(".sn-notifyBlock__header.tg-pad--quarter>span"), MediumTimeout);

		Assert.assertEquals(driver.findElement(By.cssSelector(".sn-notifyBlock__header.tg-pad--quarter>span")).getText(), carrierNeedsSelectedErrorOne, errMessage);
		Assert.assertEquals(driver.findElement(By.cssSelector(".sn-notifyBlock__body.tg-pad--quarter>ul>li")).getText(), carrierNeedsSelectedErrorTwo, errMessage);	
		
	}				
	
	// assuming there is at least one carrier to select. this was verified in 'WaitForPageToLoadPortNumber', 
	public static void VerifyButtonSelectsAndContinue() throws Exception
	{
		errMessage = "Failed wait in ChooseCarrierPage.VerifyButtonSelects";
		
		int x = 0;
		
		int totalNumButtonSections = driver.findElements(By.xpath("//form/div")).size(); // get number of buttons.
		
		Assert.assertTrue(totalNumButtonSections > 0, errMessage); // sanity check - make sure there are some buttons that can be selected.
		Assert.assertTrue(driver.findElements(By.xpath("//div/button[text()='Select']")).size() == totalNumButtonSections);  // verify all buttons say 'select'. 

		// select each button one at a time and verify all other buttons not selected say 'select' 
		for (x = 1; x <= totalNumButtonSections; x++)
		{
			driver.findElement(By.xpath("//form/div[" + x + "]/div/div[3]/button")).click(); // this drills down into the button inside of a carrier block.	
			Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Select']")).size() == totalNumButtonSections - 1); // verify buttons that say 'select'.
		}
		
		// click the last button that was clicked to get all buttons in the 'select' state.
		driver.findElement(By.xpath("//form/div[" + (x - 1) + "]/div/div[3]/button")).click();
		
		// Select the first carrier listed and click 'Next'
		driver.findElement(By.xpath("//button[text()='Select'][1]")).click();   // By.xpath("//div[text()='Sprint']/../../following ::div/button")
		
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ShortTimeout, "Failed wait for click.");
		driver.findElement(By.xpath("(//button[text()='Next'])[1]")).click();
		
	}			
}
