package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import ActionClasses.UpgradeDevice;
import HelperObjects.CalendarDateTimeObject;
import HelperObjects.DeviceInfoActions;
import HelperObjects.PlanInfoActions;


public class ProvideAdditionalInfoPage extends BaseClass
{
	
	public static enum SelectedFeature
	{
		feature12,
		feature13
	}
	
	public static SelectedFeature selectedFeature;
	
	public static WebElement element = null;
	public static String errMessage = "";
	
	
	public static void clickBackButton()
	{
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[1]"), ShortTimeout, "Failed waiting for back button in Additional Info Page");
		driver.findElement(By.xpath("(//button[text()='Back'])[1]")).click();
	}	
	
	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementPresent(By.xpath("//label[text()='Additional Instructions']"), MainTimeout);
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout);		
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout);
	}		
	
	public static void WaitForPageToLoadPortNumber() throws Exception
	{
		WaitForElementPresent(By.cssSelector("#ORDER_PROPERTY_FIELD_NAME_ON_INVOICE"), MainTimeout);
		WaitForElementPresent(By.cssSelector("#ORDER_PROPERTY_FIELD_SERVICE_NUMBER"), MainTimeout);
	}			
	
	// items that should already be shown.
	public static void VerifyExistingFieldsPortNumber() throws Exception
	{
		errMessage = "fail in ProvideAdditinalInfoPage.VerifyExistingFieldsPortNumber";
		
		Assert.assertEquals(driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_NAME_ON_INVOICE")).getAttribute("value"), userLimitedFullName, errMessage);
		Assert.assertEquals(driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CURRENT_CARRIER")).getAttribute("value"), DeviceInfoActions.currentVendorPortNumber, errMessage);
		Assert.assertEquals(driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).getAttribute("value"), serviceNumber, errMessage);		
	}			
	
	public static void WaitForPageToLoadSuspend() throws Exception
	{
		WaitForElementPresent(By.xpath("//label[text()='Additional Instructions']"), MainTimeout);
		WaitForElementPresent(By.xpath("//label[text()='Hold Service']"), MediumTimeout);
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout);		
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout);
	}		
	
	public static void WaitForPageToLoadUnsuspend() throws Exception
	{
		WaitForElementPresent(By.xpath("//label[text()='Additional Instructions']"), MainTimeout);
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout);		
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout);
	}		
	
	public static void WaitForPageToLoadSuspendLongWait() throws Exception
	{
		WaitForElementPresent(By.xpath("//label[text()='Additional Instructions']"), ExtremeTimeout - MediumTimeout - MediumTimeout);
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout);		
		WaitForElementPresent(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout);
	}		
	
	
	public static void VerifyDeviceAndPlanSectionsCorrect()
	{
		VerifyDeviceAndPlanSections();
	}
	
	
	public static void EnterMissingInfo() throws Exception
	{
		if(approverAction != ApproverAction.none) // gets nothing ??
		{
			DebugTimeout(0, driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).getText());
		}
				
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		if(approverAction != ApproverAction.none) // this is true if order action isn't being done.
		{
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
		}
		
		// now select the reason in pull-down and set.
		WaitForElementClickable(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"),MiniTimeout , "Pulldown in 'ProvideAdditionalInfoPage.verifyErrorPresent()' can't be slected.");
		new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText(reasonAction);
	}
	
	public static void EnterMissingInfoFeatures(SelectedFeature feature) throws Exception
	{
		if(feature.equals(SelectedFeature.feature12))
		{
			// fill in preferred area code. 
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).sendKeys(preferredAreaCode);
		}
				
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);

		if(feature.equals(SelectedFeature.feature12)) // bladdzz - add
		{
			// ORDER_PROPERTY_FIELD_BUSINESS_UNIT
			// fill in the additional instructions
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_BUSINESS_UNIT")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_BUSINESS_UNIT")).sendKeys("buissness unit");
		}		
		
		// now select the reason in pull-down and set.		
		if(feature.equals(SelectedFeature.feature12))
		{
			WaitForElementClickable(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"),MiniTimeout , "Pulldown in 'ProvideAdditionalInfoPage.verifyErrorPresent()' can't be slected.");
			new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText(reasonAddDeviceAndService);
		}
		else
		{
			WaitForElementClickable(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"),MiniTimeout , "Pulldown in 'ProvideAdditionalInfoPage.verifyErrorPresent()' can't be slected.");
			new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText(reasonWarrantRepair);
		}
	}	
	
	public static void EnterMissingInfoPortNumber() throws Exception
	{

		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CARRIER_ACCT_NUMBER")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CARRIER_ACCT_NUMBER")).sendKeys(PlanInfoActions.carrierAccountNumber);
		
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_NAME_ON_INVOICE")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_NAME_ON_INVOICE")).sendKeys(userLimitedShorterName);
				
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);		
		
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).sendKeys(extension);
		
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
		
		//driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CURRENT_CARRIER")).clear();
		//driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_CURRENT_CARRIER")).sendKeys("");
		
		// ***Commenting since it's not present for AT&T -- Need to check other carriers
		//driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_OTHER")).clear();
		//driver.findElement(By.cssSelector("#ORDER_PROPERTY_FIELD_OTHER")).sendKeys(PlanInfoActions.otherInfo);
	}
	
	
	
	public static void EnterMissingInfoSwapDevice() throws Exception
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);

		// fill in extension
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).sendKeys(extension);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
	
		// fill service number
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
		
		// If field for 'Preferred Area Code' is present, fill Preferred Area Code (for some vendors it may not be present) 
		if (WaitForElementVisibleNoThrow(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE"), TinyTimeout)) {
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).sendKeys(preferredAreaCode);
		}
				
		// If field for 'Authorization Code' is present, fill Authorization Code (when vendor is Verizon, field is present)
		// *** UNCOMMENT WHEN SFD 113339 IS FIXED ***
		/*if (WaitForElementVisibleNoThrow(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE"), TinyTimeout)) {
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE")).sendKeys(IdentifyDevices.authorizationCode);
		}*/
		
	}	
	
	
	
	public static void enterMissingInfoSwapDevice_SFD113339() throws Exception
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);

		// fill in extension
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).sendKeys(extension);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
	
		// fill service number
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
		
		// If field for 'Preferred Area Code' is present, fill Preferred Area Code (for some vendors it may not be present) 
		if (WaitForElementVisibleNoThrow(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE"), TinyTimeout)) {
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).sendKeys(preferredAreaCode);
		}
				
		// If field for 'Authorization Code' is present, fill Authorization Code (when vendor is Verizon, field is present)
		if (WaitForElementVisibleNoThrow(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE"), TinyTimeout)) {
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE")).clear();
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_AUTHORIZATION_CODE")).sendKeys(IdentifyDevices.authorizationCode);
		}
		
	}	
	
	
	public static void EnterMissingInfoSuspend() throws InterruptedException
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
				
		// open the calendar picker, select the next month, and close it.
		driver.findElement(By.xpath("//tbody/tr[5]/td[2]/input")).click(); // calendar picker (id doesn't work - use xpath).

		
		WaitForElementClickable(By.xpath("//th[@ng-click='next()']"), MiniTimeout, 
                "Failed waiting for calendar picker 'next' selection in ProvideAdditionalInfoPage.EnterMissingInfoSuspend.");		
		driver.findElement(By.xpath("//th[@ng-click='next()']")).click();

		// store the year and month
		CalendarDateTimeObject.StoreYearMonth(driver.findElement(By.xpath("//th[@class='switch ng-binding']")).getText());
		
		
		// day of month select needs work. took out.
		// now that the next month is selected, select day 2.
		//WaitForElementClickable(By.xpath("//span[@class='ng-binding disabled' and text()='" + dayOfMonth + "']"), MiniTimeout, 
		//		"Failed waiting for calendar picker day two selection in ProvideAdditionalInfoPage.EnterMissingInfoSuspend.");
		//driver.findElement(By.xpath("//span[@class='ng-binding disabled' and text()='" + dayOfMonth + "']")).click(); 

		// this takes focus away from calendar pop-up. 
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).click();
		
		// select pull down with a user name in it.
		new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_HOLD_SERVICE"))).selectByVisibleText(limitedUserPulldownSelection);
	}	
	
	
	public static void EnterMissingInfoUnsuspend() throws InterruptedException
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
				
		// open the calendar picker, select the next month, and close it.
		driver.findElement(By.xpath("//tbody/tr[5]/td[2]/input")).click(); // calendar picker (id doesn't work - use xpath).

		
		WaitForElementClickable(By.xpath("//th[@ng-click='next()']"), MiniTimeout, 
                "Failed waiting for calendar picker 'next' selection in ProvideAdditionalInfoPage.EnterMissingInfoSuspend.");		
		driver.findElement(By.xpath("//th[@ng-click='next()']")).click();

		// store the year and month
		CalendarDateTimeObject.StoreYearMonth(driver.findElement(By.xpath("//th[@class='switch ng-binding']")).getText());
		
		// day of month select needs work. took out.
		// now that the next month is selected, select day 2.
		//WaitForElementClickable(By.xpath("//span[@class='ng-binding disabled' and text()='" + dayOfMonth + "']"), MiniTimeout, 
		//		"Failed waiting for calendar picker day two selection in ProvideAdditionalInfoPage.EnterMissingInfoSuspend.");
		//driver.findElement(By.xpath("//span[@class='ng-binding disabled' and text()='" + dayOfMonth + "']")).click(); 

		// this takes focus away from calendar pop-up. 
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).click();
	}		
	
	
	// this populates additional information when doing order accessories action.   
	public static void EnterMissingInfoOrderAccessories() throws InterruptedException
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the extension
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).sendKeys(extension);

		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_SERVICE_NUMBER")).sendKeys(serviceNumber);
		
	}		
	
	// this populates additional information when doing Upgrade Device action.   
	public static void EnterMissingInfoUpgradeDevice() throws InterruptedException
	{
		// fill in contact number.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		
		// fill in the extension
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER_EXT")).sendKeys(extension);

		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText(UpgradeDevice.reasonUpgradeDevice);
	
	
	}		
	
	
	public static String GetCostTotal() throws InterruptedException
	{
		return driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
	}
	
	
	
	public static void clickNextBtn()
	{
		driver.findElement(By.xpath("(//button[text()='Next'])[1]")).click();
	}
	
	public static WebElement backButton(){
		element = driver.findElement(By.xpath("//div[2]/div[4]/button[2]"));
		return element;
	}
	public static WebElement nextBtn(){
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[4]/button[3]"));
		return element;
	}

	public static void SelectReasonDropDownBox() throws Exception
	{
		new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText("Other");
		WaitForElementPresent(By.xpath("//input[@id='ORDER_PROPERTY_OTHER_CHOICE_FIELD_Reason']"), MediumTimeout);
	}	

	public static void VerifyReasonError() throws Exception
	{
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Reason is required']"), MediumTimeout), "Verify reason error text not found.");
	}	
	
	
	
	
	
	/*###################################################################################################
	 * ##########################                        ################################################
	 * ##########################		Text Boxes		 ################################################
	 * ##########################                        ################################################
	 * ##################################################################################################
	 */ //This section is for all text boxes
	// ***** NOT USED -- TO BE REMOVED *****
	/*
	public static WebElement preferredAreaCodeTxtBox(){
		element = driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE"));
		return element;
	}
	
	public static WebElement contactPhoneNumberTxtBox(){
		element = driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER"));
		return element;
	}
	public static WebElement otherTxtBox(){
		element = driver.findElement(By.xpath("//input[@id='ORDER_PROPERTY_OTHER_CHOICE_FIELD_Reason']"));
		return element;
	}*/
	
	
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
	
	// ***** NOT USED -- TO BE REMOVED *****
	//error message not filling required fields
	/*
	 * public static WebElement errorMessage(WebDriver driver){
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[9]/div[1]/div[1]/div[1]/span"));
		return element;
	}
	public static WebElement errorMessage2(WebDriver driver){
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[9]/div[1]/div[1]/div[2]/ul/li"));
		return element;
	}
	public static WebElement reasonDropDownBox(WebDriver driver){
		element = driver.findElement(By.id(""));
		return element;
	}
	*/
	
	// TODO : do rest of errors.
	public static void VerifyErrorsAndCompletePage() throws Exception
	{
		// verify number of errors under main error message.  -- COMMENTED SINCE SOMETIMES THE # OF REQUIRED FIELDS IS NOT 3 
		//Assert.assertTrue(driver.findElements(By.xpath(mainErrorMessage + "/../following-sibling ::div/ul")).size() == 3, 
			//	"Did not find three expected error messages in ProvideAdditionalInfoPage.VerifyErrors.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath(mainErrorMessage), MediumTimeout), 
						  "Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Preferred Area Code is required']"), MediumTimeout), 
				  "Expected Error message 'Preferred Area Code is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Contact Phone Number is required']"), MediumTimeout), 
				  "Expected Error message 'Contact Phone Number is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Reason is required']"), MediumTimeout), 
				  "Expected Error message 'Reason is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");

		// This will make the first message under the main error message change. 
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).clear();		
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).sendKeys("1234");
		clickNextBtn();
	
		//Assert.assertTrue(driver.findElements(By.xpath("//span[text()='Please fix the following validation errors:']/../following-sibling ::div/ul")).size() == 3, 
			//	"Did not find three expected error messages in ProvideAdditionalInfoPage.VerifyErrors.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath(mainErrorMessage), MediumTimeout), 
				  "Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Preferred Area Code must be 3 characters long']"), MediumTimeout), 
				  "Expected Error message 'Preferred Area Code must be 3 characters long' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		/*Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Preferred Area Code must have a length between 3 and 3 characters']"), MediumTimeout), 
				  "Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");*/
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Contact Phone Number is required']"), MediumTimeout), 
				  "Expected Error message not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Reason is required']"), MediumTimeout),
				  "Expected Error message 'Contact Phone Number is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		// This will remove the first message under the main error message.
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_PREFERRED_AREA_CODE")).sendKeys(preferredAreaCode);
		clickNextBtn();

		// number of messages should be two now.
		//Assert.assertTrue(driver.findElements(By.xpath("//span[text()='Please fix the following validation errors:']/../following-sibling ::div/ul")).size() == 2, 
			//	"Did not find three expected error messages in ProvideAdditionalInfoPage.VerifyErrors.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Contact Phone Number is required']"), MediumTimeout), 
				  "Expected Error message 'Contact Phone Number is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Reason is required']"), MediumTimeout),
				  "Expected Error message 'Reason is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");
		
		// Now enter a phone number, verify only one error below the main error message 
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_CONTACT_NUMBER")).sendKeys(contactNumber);
		clickNextBtn();
		
		// number of messages should be one now.
		//Assert.assertTrue(driver.findElements(By.xpath("//span[text()='Please fix the following validation errors:']/../following-sibling ::div/ul")).size() == 1, 
			//	"Did not find three expected error messages in ProvideAdditionalInfoPage.VerifyErrors.");
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//li[text()='Reason is required']"), MediumTimeout),
				  "Expected Error message 'Reason is required' not present in 'ProvideAdditionalInfoPage.verifyErrorPresent()'.");

		// now select the reason in pull-down.
		WaitForElementClickable(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"),MiniTimeout , "Pulldown in 'ProvideAdditionalInfoPage.verifyErrorPresent()' can't be slected.");
		new Select(driver.findElement(By.id("ORDER_PROPERTY_CHOICE_FIELD_Reason"))).selectByVisibleText(reasonAddDeviceAndService);
		
		// fill in the additional instructions
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).clear();
		driver.findElement(By.id("ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS")).sendKeys(additionalInstructions);
		
		//ORDER_PROPERTY_FIELD_ADDITIONAL_INSTRUCTIONS
		
		
		// For AT&T 'Business Unit' is required 
		try {
			
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_BUSINESS_UNIT"));
			driver.findElement(By.id("ORDER_PROPERTY_FIELD_BUSINESS_UNIT")).sendKeys(businessUnit);
						
		} catch (Exception e) { }
		
		
	}
	
	
	
	
	
		
	
	
	
	
	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		  Fills and Clicks		 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This Section is for all clickable  and fill actions
	
	
	
	
	
	// ***** NOT USED -- TO BE REMOVED *****
	/*
	public static void fillPreferredAreaCodeTxtBox(){
		element = preferredAreaCodeTxtBox();
		element.sendKeys("781");
	}
	public static void fillContactPhoneNumberTxtBox(){
		element = contactPhoneNumberTxtBox();
		element.sendKeys("7812739306");
	}

	public static void fillOtherTxtBox()
	{
		element = otherTxtBox();
		element.sendKeys("Other reason filled out");
	}
	*/
}
