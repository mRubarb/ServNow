package ServiceNow;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ActionClasses.ModifySelectionsTesting.Device;

public class ModifySelectionsTesting extends BaseClass 
{

	public static String currentCarrierLocal = "Sprint";
	public static String newCarrierLocal = "Verizon Wireless";
	public static String xpathToOptionsList = "//h3[text()='Optional Features']/following-sibling::ul/li";
	public static String xpathClickFirstModify = "(//a[text()='Modify'])[1]";
	public static String xpathClickSecondModify = "(//a[text()='Modify'])[2]";
	public static String xpathClickThirdModify = "(//a[text()='Modify'])[3]";
	public static String xpathPlanOptionOne = "(" + xpathToOptionsList + ")[1]";
	public static String xpathPlanOptionTwo = "(" + xpathToOptionsList + ")[2]";
	public static String xpathAddToCartForIndexing = "(//button[text()='Add to Cart'])";
	public static String lineOne = "Line One";
	public static String lineTwo = "Line Two";
	public static String lineThree = "Line Three";	
	public static String detailedRemoveDeviceError = "Plans, optional features, and/or accessories must be compatible with your selected device. "
												   + "Removing this device requires that we remove the following items from your shopping cart:";

	public static String xpathClickFirstModifyVerify = "(//button[text()='Modify'])[1]";
	public static String xpathClickSecondModifyVerify = "(//button[text()='Modify'])[2]";	
	public static String xpathClickThirddModifyVerify = "(//button[text()='Modify'])[3]";
	public static String xpathClickFourthdModifyVerify = "(//button[text()='Modify'])[4]";
	public static String xpathClickFivedModifyVerify = "(//button[text()='Modify'])[5]";
	
	public static int verifyPageTempIndex = -1;
	
	public static List<Device> finalListOfDevices = new ArrayList<Device>();
	public static List<WebElement> tempWebEleList; 
	
	public static String [] messageArray = 
	{
		"Removing device will empty your shopping cart.", // device
		"Removing your plan will also remove any related additional info you entered previously.", // plan
		"Changing devices will empty your shopping cart.", // change device
		"Choosing a different plan will also remove any related additional info you entered previously." // remove and add plan back.
	};

	public static String [] detailedMessageArray =
	{
			detailedRemoveDeviceError		
	};
	
	public static String [] detailedMessageItems =
	{
			"Plan",
			"Accessories",
			"Additional order info"
	};
	
	public static void UnSelectOptionOne() throws Exception
	{
		// click third modify to get back to option page, remove selected option.    
		driver.findElement(By.xpath(xpathClickThirdModify)).click();
		WaitForElementPresent(By.xpath(xpathPlanOptionOne), ShortTimeout);
		driver.findElement(By.xpath(xpathPlanOptionOne)).click(); // select first option to remove selection
	}
	
	public static boolean CatchFail(String xpath)
	{
		try
		{
			ShowText(driver.findElement(By.xpath(xpath)).getText());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static void ClearListSelection()
	{
		String buttonXpath =  xpathRemoveFromCart;   // "//button[text()='Remove from Cart']";
		try
		{
			WaitForElementClickable(By.xpath(buttonXpath), MediumTimeout, "");
			driver.findElement(By.xpath(buttonXpath)).click();
		}
		catch(Exception e)
		{
			
		}
	}

	// this starts at home page and sets up everything to verify orders.   
	public static void SetupAllToVerifyOrder() throws Exception
	{
		PopulatedDevicePlanPages();
		ChoosePlanPage.clickNextButton();
		assertTrue(ChooseAccessoriesPage.waitForPageToLoadAccessories());
		driver.findElement(By.xpath(xpathAddToCartForIndexing + "[1]")).click(); // select an accessory.
		ChooseAccessoriesPage.clickNextBtn();
		ProvideAdditionalInfoPage.WaitForPageToLoad();
		ProvideAdditionalInfoPage.enterMissingInfoTransferServiceIn();
		ProvideAdditionalInfoPage.clickNextBtn();
		EnterShippingInfoPage.WaitForPageLoad();
		EnterShippingInfoPage.clickNextBtn();
		VerifyOrderPage.WaitForPageToLoad();
		//WaitForElementVisible(By.xpath("//div[text()='Device']/following-sibling::div[2]/div"), MediumTimeout);
		WaitForElementVisible(By.xpath("//div[text()='Additional Information']"), MainTimeout);
		WaitForElementVisible(By.xpath("//div[text()='Shipping Information']"), MediumTimeout);
	}

	// go to devices page, select first device in devices list and select the first plan.
	// click modify for plan optional features and add the first option.
	public static void PopulatedDevicePlanPages() throws Exception
	{
		// go to devices page, select first device in devices list and select the first plan.
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(0).index + 1 ) + "]")).click();
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanOrig();
		ChoosePlanPage.SelectFirstPlan();
		
		// click modify for plan optional features and add the first option.
		WaitForElementClickable(By.xpath(xpathClickSecondModify), MediumTimeout, "");
		driver.findElement(By.xpath(xpathClickSecondModify)).click(); // select modify
		WaitForElementPresent(By.xpath(xpathPlanOptionOne), ShortTimeout);
		driver.findElement(By.xpath(xpathPlanOptionOne)).click(); // select first option
	}
	
	// verify actual and expected sent in are equal, after filtering out labels. 
	// example 'actual' input - "Address Line1 Line One". use split to get actual from this
	public static void VerifyShippingInfo(String actual, String expected)
	{
		actual =  actual.split(" ")[2] + " " + actual.split(" ")[3];    
		Assert.assertEquals("Failed compare in 'VerifyShippingInfo' method.", expected, actual);
	}
	
	
	// verify test in remove device warning and close the warning 
	public static void VerifyWarnigIsPresentAndClose(boolean trueClose, int messageIndex) throws Exception
	{
		WaitForElementVisible(By.xpath("//p[text()='" + messageArray[messageIndex]  + "']"), ShortTimeout);
		//WaitForElementVisible(By.xpath("//p[text()='Removing device will empty your shopping cart.']"), ShortTimeout);	
		if(trueClose)
		{
			CloseDevicePageWarningPopUp(true);
		}
		else
		{
			CloseDevicePageWarningPopUp(false);
		}
	}
	
	public static void CloseDevicePageWarningPopUp(boolean trueOk) throws Exception
	{
		if(trueOk)
		{
			WaitForElementClickable(By.xpath("//button[text()='OK']"), MediumTimeout, "");
			driver.findElement(By.xpath("//button[text()='OK']")).click();
		}
		else
		{
			WaitForElementClickable(By.xpath("(//button[text()='Cancel'])[3]"), MediumTimeout, "");
			driver.findElement(By.xpath("(//button[text()='Cancel'])[3]")).click();
		}
	}

	
	// click modify, defined by xpath passed in, to get back to selected page. select to click the selected item in the 
	// page list, verify and cancel message pop-up.	
	public static void VerifyRemoveMessage(String modifyXpath, int errorMessageIndex) throws Exception
	{
		driver.findElement(By.xpath(modifyXpath)).click();
		ClearListSelection();
		VerifyWarnigIsPresentAndClose(false, errorMessageIndex);
		Thread.sleep(1500); // wait for pop-up to go away to avoid click conflict 
	}
}
