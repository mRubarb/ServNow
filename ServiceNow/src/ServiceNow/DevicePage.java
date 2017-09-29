package ServiceNow;

import org.openqa.selenium.By;

import HelperObjects.DeviceInfoActions;

public class DevicePage  extends BaseClass
{
	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementPresent(By.xpath("//h3[text()='Contract Dates']"), MainTimeout);
		WaitForElementPresent(By.xpath("//label[text()='Billing Vendor']"), MainTimeout);	

		DeviceInfoActions.currentVendorPortNumber = driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText();
	}

	public static void setCurrentVendorToDeviceInfoActions() throws Exception
	{
		DeviceInfoActions.currentVendorPortNumber = driver.findElement(By.xpath("//label[text()='Vendor']/following ::span[1]")).getText();
		System.out.println("Current Vendor Port Number : " + DeviceInfoActions.currentVendorPortNumber );
	}
}
