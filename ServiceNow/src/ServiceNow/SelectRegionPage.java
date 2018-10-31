package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SelectRegionPage extends BaseClass
{
	public static WebElement element = null;
	
	/*###################################################################################################
	 * ##########################                        ################################################
	 * ##########################		Page Elements	 ################################################
	 * ##########################                        ################################################
	 * ##################################################################################################
	 */ //This section defines all elements on page
	
	/*###################################################################################################
	 * ##########################                        ################################################
	 * ##########################		BUTTONS			 ################################################
	 * ##########################                        ################################################
	 * ##################################################################################################
	 */ //This section defines all Button elements
	
	//SelectRegion Next Button
		public static WebElement nextButtonSelectRegion() throws Exception
		{
			WaitForElementPresent(By.cssSelector(".tg-button.tg-button--primary.ng-binding.ng-scope"), MainTimeout); 
			// DebugTimeout(0, "Wait OK In 'nextButtonSelectRegion'");
			element = driver.findElement(By.cssSelector(".tg-button.tg-button--primary.ng-binding.ng-scope"));
			return element;
		}
	
	/*###################################################################################################
	 * ##########################                        ################################################
	 * ##########################		Text Boxes		 ################################################
	 * ##########################                        ################################################
	 * ##################################################################################################
	 */ //This section is for all text boxes
	
	//Returns Postal Code TextBox element
	public static WebElement postalCodeTextBox(){
		element  = driver.findElement(By.xpath(".//*[@id='POSTAL_CODE']"));
		return element;
	}
	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		Other Page Elements		 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This section is for uncategorized pages elements
	
	//ErrorNoPostalCode
		public static WebElement errorNoPostalCode(WebDriver driver){
			element = driver.findElement(By.xpath("//div//ul//li"));
			return element;
		}
		
		
	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		Methods/Actions			 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This section Is for all Methods and Actions

	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		      Verify		     ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This section is for all Methods that verify something	
		
		public static void verifyNoPostalCodeErrorPresent(){
			element = errorNoPostalCode(driver);
			String errorToText = element.getText();
			boolean isPreset;
				if(errorToText != null){
					isPreset = true;
				}
				else{
					isPreset = false;
				}
			Assert.assertEquals(isPreset, true);
			
		}
		
	public static void fillPostalCodeTextBox(String postalCode){
		element = postalCodeTextBox();
		element.sendKeys(postalCode);
	}
	
	public static void clickNextButtonSelectRegion() throws Exception
	{
		element = nextButtonSelectRegion();
		element.click();
	}
	
	public static void VerifyPostalCodeError() throws Exception
	{
		WaitForElementVisible(By.xpath(mainErrorMessage), MediumTimeout);		
		WaitForElementVisible(By.xpath("//li[text()='Please enter the Postal Code.']"), MediumTimeout);		
	}
	
	public static void getErrorNoPostalCode()
	{
		element = errorNoPostalCode(driver);
		@SuppressWarnings("unused")
		String errorToText = element.getText();
	}
	public static void selectCountryFromDropDown() throws Exception
	{
		WaitForElementVisible(By.xpath("//div/h3[text()='Shopping Cart']"), MainTimeout);
		driver.findElement(By.id("COUNTRY")).sendKeys("United States");
	}
	
	
	
	
}
