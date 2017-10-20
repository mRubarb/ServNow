package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;


public class IdentifyDevices extends BaseClass 
{
	public static String strTemp = "";
	
	public static String newManufacturer = "Apple";  // "AT&T";
	public static String newModel = "iPhone 7 (32GB) - Silver";  // "Apple iPad with Wi-Fi + 4G 16GB (Black)";
	public static String newSerialNumberType = "IMEI"; // "CAP CODE";
	public static String newSerialNumberTypeForVerify = "IMEI"; // "CAP_CODE";	
	public static String newSerialNumber = "121527071";	
	public static String authorizationCode = "1478527"; // "20161952";	
	
	public static String oldManufacturer = "";	
	public static String oldModel = "";
	public static String oldSerialNumber = "1969HC";	
	public static String oldSerialNumberType = "";
	public static String simId = "";
	
	
	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), MainTimeout, "Failed waiting for 'Identify Devices' page to load.");
		WaitForElementPresent(By.id("swap-new-manufacturer"), MediumTimeout);
		//WaitForElementVisible(By.xpath("//span[contains(text(),'Activity ID')]"), MediumTimeout);
	}
	
	public static void ClickNext() throws Exception
	{
		WaitForElementClickable(By.xpath("//button[text()='Next']"), MediumTimeout, "");  
		driver.findElement(By.xpath("//button[text()='Next']")).click();
	}
	
	// this populates the new device section.
	public static void PopulateNewDevice() throws Exception 
	{
		// first store the old device information.
		
		// no such element: Unable to locate element: {"method":"id","selector":"swap-exist-manufacturer"}
		
		WaitForElementClickable(By.id("swap-exist-manufacturer"), MediumTimeout, "");
		
		oldManufacturer = new Select(driver.findElement(By.id("swap-exist-manufacturer"))).getFirstSelectedOption().getText(); 
		oldModel = new Select(driver.findElement(By.id("swap-exist-model"))).getFirstSelectedOption().getText();		
		oldSerialNumberType = new Select(driver.findElement(By.id("swap-exist-serial-number-type"))).getFirstSelectedOption().getText();
		simId = driver.findElement(By.id("swap-exist-sim-id")).getText();
		
		// couldn't get serial number. set it to already set variable  
		driver.findElement(By.xpath("//input[@ng-model='existingDevice.serialNumber.value']")).clear();		
		driver.findElement(By.xpath("//input[@ng-model='existingDevice.serialNumber.value']")).sendKeys(oldSerialNumber);
		
		// set new device info.
		new Select(driver.findElement(By.id("swap-new-manufacturer"))).selectByVisibleText(newManufacturer); // new manufacturer
		
		// after selecting manufacturer a call is made to API. have to wait for next pull down to be available.
		WaitForElementClickable(By.id("swap-new-model"), MediumTimeout, "Wait for Model pulldown failed in PopulateNewDevice.PopulateNewDevice"); 
		
		new Select(driver.findElement(By.id("swap-new-model"))).selectByVisibleText(newModel);		
		new Select(driver.findElement(By.id("swap-new-serial-number-type"))).selectByVisibleText(newSerialNumberType);
		driver.findElement(By.id("swap-new-serial-number")).sendKeys(newSerialNumber);
	}
	
	public static void ShowIdentityData() throws InterruptedException
	{
		DebugTimeout(0, " --------------- Identity class stored data. ------------------------");
		DebugTimeout(0, "oldManufactuer: " + oldManufacturer);
		DebugTimeout(0, "oldModel: " + oldModel);
		DebugTimeout(0, "oldSerialNumber: " + oldSerialNumber);
		DebugTimeout(0, "oldSerialNumberType: " + oldSerialNumberType);
		DebugTimeout(0, "newManufacturer: " + newManufacturer);
		DebugTimeout(0, "newModel: " + newModel);
		DebugTimeout(0, "newSerialNumberType: " + newSerialNumberType);
		DebugTimeout(0, "newSerialNumber: " + newSerialNumber);
	}
}
