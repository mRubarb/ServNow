package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import HelperObjects.DeviceInfoActions;


public class MyDevicesPage extends BaseClass
{
	
	
	// store away full service number and numbers-only service number.
	public static void StoreServiceNumberFormats() throws Exception
	{
		
		// get the complete service number and device
		String fullServiceNumberFromUI = driver.findElement(By.xpath("//div/div[" + indexMyDevices +"]/div[@class='tg-display--table-cell']/div[2]")).getText();
				
		//System.out.println("fullServiceNumberFromUI: " + fullServiceNumberFromUI);
		
		String [] strArray = fullServiceNumberFromUI.split(" ");  //driver.findElement(By.cssSelector(".tg-pad--quarter--top")).getText().split( " "); // get service number parts
		
		/*for (String s: strArray) {
			System.out.println("service number part: " + s);

		}*/
		
		fullServiceNumber = fullServiceNumberFromUI;
		serviceNumber = strArray[1].replace("(","").replace(")", "") + strArray[2].replace("-","");  // get numbers-only service number. 
		
		//System.out.println("serviceNumber: " + serviceNumber);
		
		//return new String [] {serviceNumber, fullServiceNumberFromUI};
		
		
		//System.out.println("serviceNumber: " + serviceNumber);
		//System.out.println("fullServiceNumber: " + fullServiceNumber);
	}

	// this returns the full service number and the number-only service number
	/*static public String [] SetupServiceNumber()
	{
		// get the complete service number and device
		String fullServiceNumberFromUI  = driver.findElement(By.xpath("//div/div[" + indexMyDevices +"]/div[@class='tg-display--table-cell']/div[2]")).getText();
				
		System.out.println("fullServiceNumberFromUI: " + fullServiceNumberFromUI);
		
		String [] strArray = fullServiceNumberFromUI.split(" ");  //driver.findElement(By.cssSelector(".tg-pad--quarter--top")).getText().split( " "); // get service number part
		
		for (String s: strArray) {
			System.out.println("service number part: " + s);

		}
		
		String serviceNumber = strArray[1].replace("(","").replace(")", "") + strArray[2].replace("-","");  // get numbers-only service number. 
		
		System.out.println("serviceNumber: " + serviceNumber);
		
		return new String [] {serviceNumber, fullServiceNumberFromUI};
		
	}*/
	
	
	
	public static void WaitForPageToLoad() throws Exception
	{
		String errMessage = "Failed waiting for My Devices Page to load in MyDevicesPage.WaitForPageToLoad.";
		WaitForElementClickable(By.xpath("//select[@ng-model='device.selectedAction']"), MainTimeout, errMessage);
		WaitForElementClickable(By.xpath("//button[text()='View Device']"), MediumTimeout, errMessage);
	}
	
	public static void SelectDeactivateAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.deactivate);
	}
	
	public static void SelectSuspendAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.suspend);
	}	
	
	public static void SelectUnsuspendAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.unsuspend);
	}		
	
	public static void SelectOrderAccessoriesAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.orderAccessories);
	}		
	
	public static void SelectSwapDevicesAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.swapDevices);
	}			

	public static void SelectUpgradeDeviceAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.upgradeDevice);
	}			
	
	public static void SelectUpdateFeaturesAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.updateFeatures);
	}			

	public static void SelectPorNumberAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServices))).selectByVisibleText(DeviceInfoActions.portNumber);
	}			
	
	// find first device that has no service and select the deactivate action on it.
	public static void SelectFirstDeviceWithNoService()
	{
		int cntr;
		boolean foundDeviceWithNoService = false;

		// get the number of available devices on the page.
		int numDevices =  driver.findElements(By.xpath("//button[text()='View Device']")).size();
		
		// go through each device until one with no service is found.
		for(cntr= 1; cntr <= numDevices; cntr++)
		{
			if(driver.findElement(By.xpath("(//button[text()='View Device'])[" + cntr + "]/../preceding-sibling::div[1]")).getText().equals("")) // device has no service number shown.
			{
				foundDeviceWithNoService = true;
				break;
			}
		}
		
		if(!foundDeviceWithNoService) // throw error if a devices with no service was not found.
		{ 
			Assert.fail("Failed to find a device with no service in MyDevicesPage.SelectFirstDeviceWithNoService method.");
		}

		new Select(driver.findElement(By.xpath("(//select)[" + cntr +"]"))).selectByVisibleText(DeviceInfoActions.deactivate); // select the deactivate action.
	}
}
