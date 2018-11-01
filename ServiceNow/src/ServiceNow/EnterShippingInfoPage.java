  package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class EnterShippingInfoPage extends BaseClass
{
	public static WebElement element = null;
	public static String germanStAddress = "Rambertweg";
	public static String germanCity = "Munich";

	public static void clickNextBtn()
	{
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ShortTimeout, "Next button not found in method 'clickNextButton()'");
		driver.findElement(By.xpath("(//button[text()='Next'])[1]")).click();
	}

	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementVisible(By.xpath("//label[text()='Postal Code']"), MainTimeout);		
	}
	
	// select the expedite check box.
	public static void selectExpeditedCheckBox() throws Exception
	{
		driver.findElement(By.id("expedited")).click();
	}
	
	public static void WaitForPageLoad() throws Exception
	{
		WaitForElementVisible(By.xpath("//label[text()='Postal Code']"), MainTimeout);
		WaitForElementVisible(By.xpath("(//button[text()='Next'])[1]"), MainTimeout);
		WaitForElementVisible(By.xpath("(//button[text()='Next'])[2]"), MainTimeout);
	}
	
	public static void WaitForPageLoadExtended() throws Exception
	{
		WaitForElementVisible(By.xpath("//label[text()='Postal Code']"), ExtremeTimeout - MainTimeout);
		WaitForElementVisible(By.xpath("(//button[text()='Next'])[1]"), MainTimeout);
		WaitForElementVisible(By.xpath("(//button[text()='Next'])[2]"), MainTimeout);
	}
	
	public static void VerifyCorrectData() throws Exception
	{
		String temp  = new Select(driver.findElement(By.xpath("(//tr/td/select)[1]"))).getFirstSelectedOption().getText();
		Assert.assertEquals(temp, "United States" ,"");
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_NAME")).getAttribute("value"), userLimitedShorterName,"");
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_LINE_1")).getAttribute("value"), addressLineOneOrderActions , "");
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_CITY")).getAttribute("value"), cityOrderActions , "");		
		temp  = new Select(driver.findElement(By.xpath("(//tr/td/select)[2]"))).getFirstSelectedOption().getText();
		Assert.assertEquals(temp, userState ,"");		
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_POSTAL_CODE")).getAttribute("value"), zipCodeOrderActions,"");
	}
	
	public static void VerifyCorrectDataDeviceAction() throws Exception
	{
		Assert.assertEquals(new Select(driver.findElement(By.xpath("//select[@ng-model='shipping.selectedCountry']"))).getFirstSelectedOption().getText(),
				            userCountry, "Pulldon should be set to 'United States'");
		
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_NAME")).getAttribute("value"), userLimitedShorterName, "Name is wrong in 'VerifyCorrectData()'");
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_LINE_1")).getAttribute("value"), addressLineOne , "Address is wrong in 'VerifyCorrectData()'.");	
		
		Assert.assertEquals(new Select(driver.findElement(By.id("ADDRESS_FORMAT_STATE"))).getFirstSelectedOption().getText(),
	            userState, "Pulldon should be set to 'Massachusetts'");		
		
		Assert.assertEquals(driver.findElement(By.id("ADDRESS_FORMAT_POSTAL_CODE")).getAttribute("value"), zipCodeOrderActions, "Postal code is wrong in 'VerifyCorrectData()'.");		
	}	
	
	public static void EnterNonRequiredFieldsAndExpedite() throws Exception
	{
		driver.findElement(By.cssSelector("#ADDRESS_FORMAT_LINE_2")).sendKeys(addressLineTwoDummyInfo);
		driver.findElement(By.cssSelector("#ADDRESS_FORMAT_LINE_3")).sendKeys(addressLineThreeDummyInfo);
		driver.findElement(By.cssSelector("#expedited")).click();		
	}
	
	public static void selectGermanyDropDown() throws Exception
	{
		// comment not needed. creates long delay.
		// WaitForElementNotClickableNoThrow(By.id("ADDRESS_FORMAT_OTHER_ADDRESS_INFORMATION"), MainTimeout, "Expected text field not found in 'selectGermanyDropDown'");		
		new Select(driver.findElement(By.xpath("//select[@ng-model='shipping.selectedCountry']"))).selectByVisibleText("Germany");
		WaitForElementVisible(By.xpath("//label[text()='Other Address Information']"), MainTimeout);
		WaitForElementClickable(By.id("ADDRESS_FORMAT_OTHER_ADDRESS_INFORMATION"), MainTimeout, "Expected text field not found in 'selectGermanyDropDown'");	
	}	

	public static void PopulateGermanFields()
	{
		element = addressLine1TextBox();
		element.sendKeys(germanStAddress);
	}	
	
	// 10/31/18 - changed name.
	// waiting for this error to be visible verifies it is visible.
	public static void VerifyErrorsShippingInfo(Country country) throws Exception
	{
		WaitForElementVisible(By.xpath(mainErrorMessage), MainTimeout);		
		WaitForElementVisible(By.xpath("//li[text()='Line 1 is required.']"), MediumTimeout);
		WaitForElementVisible(By.xpath("//li[text()='City is required.']"), MediumTimeout);
		if(country.equals(Country.UnitedStates))
		{
			WaitForElementVisible(By.xpath("//li[text()='State is required.']"), MediumTimeout);			
		}
		else
		{
			if(WaitForElementVisibleNoThrow(By.xpath("//li[text()='State is required.']"), ShortTimeout))
			{
				Assert.fail("Found error message that shouldn't exist in 'VerifyErrorsShippingInfo");
			}
		}
	}	
	
	public static void PopulateFieldsForGermany()
	{
		driver.findElement(By.id("ADDRESS_FORMAT_LINE_1")).sendKeys("Bob Lichtenfels");
		driver.findElement(By.id("ADDRESS_FORMAT_CITY")).sendKeys("Erie");	
	}
	
	public static void SelectDropdownToUnitedStates() throws Exception
	{
		WaitForElementNotClickableNoThrow(By.id("ADDRESS_FORMAT_OTHER_ADDRESS_INFORMATION"), MainTimeout, "Expected text field not found in 'selectGermanyDropDown'");		
		WaitForElementVisible(By.xpath("//label[text()='Postal Code']"), MainTimeout);
		WaitForElementClickable(By.xpath("//select[@ng-model='shipping.selectedCountry']"), MediumTimeout, "");
		new Select(driver.findElement(By.xpath("//select[@ng-model='shipping.selectedCountry']"))).selectByVisibleText("United States");		
	}
	
	public static void VerifyErrorsUnitedStates() throws Exception
	{
		driver.findElement(By.id("ADDRESS_FORMAT_POSTAL_CODE")).clear();
		driver.findElement(By.id("ADDRESS_FORMAT_CITY")).clear();
		driver.findElement(By.id("ADDRESS_FORMAT_LINE_1")).clear();
		driver.findElement(By.id("ADDRESS_FORMAT_NAME")).clear();		

		clickNextBtn();
		WaitForPageToLoad();

		
		Assert.assertTrue(driver.findElements(By.xpath(mainErrorMessage + "/../following-sibling::div[1]/ul")).size() == 4 , 
				"Expected number of error messages is not correct in 'EnterShippingPageInfo.VerifyErrorsUnitedStates'.");

		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath(mainErrorMessage), MediumTimeout), 
				  			"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Name is required.']"), MediumTimeout), 
							"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Line 1 is required.']"), MediumTimeout), 
							"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='City is required.']"), MediumTimeout), 
							"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Postal Code is required.']"), MediumTimeout), 
							"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");

		driver.findElement(By.id("ADDRESS_FORMAT_NAME")).sendKeys(userName);  // "Bob Lichtenfels"

		clickNextBtn();
		WaitForPageToLoad();
		
		Assert.assertTrue(driver.findElements(By.xpath(mainErrorMessage + "/../following-sibling::div[1]/ul")).size() == 3 , 
				"Expected number of error messages is not correct in 'EnterShippingPageInfo.VerifyErrorsUnitedStates'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Line 1 is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='City is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Postal Code is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");

		driver.findElement(By.id("ADDRESS_FORMAT_LINE_1")).sendKeys(addressLineOne); // "9847 Main"		

		clickNextBtn();
		WaitForPageToLoad();
		
		Assert.assertTrue(driver.findElements(By.xpath(mainErrorMessage + "/../following-sibling::div[1]/ul")).size() == 2 , 
				"Expected number of error messages is not correct in 'EnterShippingPageInfo.VerifyErrorsUnitedStates'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='City is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Postal Code is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		driver.findElement(By.id("ADDRESS_FORMAT_CITY")).sendKeys(userCity);  // "Boston"		

		clickNextBtn();
		WaitForPageToLoad();
		
		Assert.assertTrue(driver.findElements(By.xpath(mainErrorMessage + "/../following-sibling::div[1]/ul")).size() == 1 , 
				"Expected number of error messages is not correct in 'EnterShippingPageInfo.VerifyErrorsUnitedStates'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Postal Code is required.']"), MediumTimeout), 
				"Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		

		driver.findElement(By.id("ADDRESS_FORMAT_POSTAL_CODE")).sendKeys(userPostalCode);  // "02334"		
	}
	
	public static void checkExpediteOrder()
	{
		driver.findElement(By.id("expedited")).click();
	}
	
	/*
	public static WebElement nextBtn()
	{
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout, "");
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout, "");		
		driver.findElement(By.xpath("(//button[text()='Next'])[1]"));
	}
	*/
	
	public static WebElement addressLine1TextBox(){
		element = driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_LINE_1']"));
		return element;
	}
	public static WebElement germanCityTextBox (){
		element = driver.findElement(By.xpath("//input[@id='ADDRESS_FORMAT_CITY']"));
		return element;
	}
	

	
	
	
	
	
	/*###################################################################################################
	 * ##########################                        ################################################
	 * ##########################		Check Boxes		 ################################################
	 * ##########################                        ################################################
	 * ##################################################################################################
	 */ //This section is for all Check boxes
	
	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		Other Page Elements		 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This section is for uncategorized pages elements
	
	public static WebElement errorMessage(){
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[9]/div[1]/div[1]/div[1]/span"));
		return element;
	}
	public static WebElement expediteOrderChkBox(){
		element = driver.findElement(By.xpath("//input[@id='expedited']"));
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
	

	
	
		public static void fillLine1TextBox()
		{
			element = addressLine1TextBox();
			element.sendKeys(germanStAddress);
		}
		public static void fillGermanCityTxtBox(){
			element = germanCityTextBox();
			element.sendKeys(germanCity);
		}
	
		
		
		
		
}
