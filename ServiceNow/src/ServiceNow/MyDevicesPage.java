package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import HelperObjects.DeviceInfoActions;


public class MyDevicesPage extends BaseClass
{
	
	
	// store away full service number and numbers-only service number.
	public static void StoreServiceNumberFormats() throws Exception
	{
		int index = indexMyDevices;
		
		if (actionOnlyOnService) {
			
			index = indexMyServices;
			
		}
		
		// get the complete service number and device
		String fullServiceNumberFromUI = driver.findElement(By.xpath("//div/div[" + index +"]/div[@class='tg-display--table-cell']/div[2]")).getText();
				
		//System.out.println("fullServiceNumberFromUI: " + fullServiceNumberFromUI);
		
		String [] strArray = fullServiceNumberFromUI.split(" ");  //driver.findElement(By.cssSelector(".tg-pad--quarter--top")).getText().split( " "); // get service number parts	
		
		serviceNumber = strArray[1].replace("(","").replace(")", "") + strArray[2].replace("-","");  // get numbers-only service number. 
		
		serviceNumber = serviceNumber.replaceAll("[^0-9]", "");
		
		fullServiceNumber = fullServiceNumberFromUI.replaceAll("[A-Z]", "");
		
		System.out.println("serviceNumber: " + serviceNumber);
		System.out.println("fullServiceNumber: " + fullServiceNumber);
		
	}
	
	
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
	
	
}
