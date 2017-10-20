package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import HelperObjects.DeviceInfoActions;


public class MyServicesPage extends BaseClass
{
	public static void SelectUpgradeServiceAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServicesPage))).selectByVisibleText("Upgrade Service"); // need to create Serviceinfo class or rename DeviceInfo class
	}

	public static void WaitForPageToLoad()
	{
		if(!WaitForElementClickableBoolean(By.xpath("(//select)[1]"), ExtremeTimeout - MainTimeout))
		{
			Assert.fail("There are no services that can have an action applied in 'MyServicesPage.WaitForPageToLoad'.");
		}
		
		if(!WaitForElementClickableBoolean(By.xpath("(//button[text()='View Service'])[1]"), MediumTimeout))
		{
			Assert.fail("There are no service that can be viewed in 'MyServicesPage.WaitForPageToLoad'.");
		}
	}


	public static void selectUpgradeServiceAction()
	{
		new Select(driver.findElement(By.xpath(chooseActionsPullDownServicesPage))).selectByVisibleText(DeviceInfoActions.upgradeService);
	}	


}
