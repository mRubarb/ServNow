package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class SideBar extends BaseClass 
{
	public static WebElement element = null;

	// click login button
	public static void clickHomeButton() 
	{
		WebElement homeButton = driver.findElement(By.xpath(".//*[@id='865b078b0fa74200e5680cbce1050e25']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(homeButton).click().perform();
	}

	public static void clickApprovalButton() 
	{
		WebElement homeButton = driver.findElement(By.xpath(".//*[@id='e0f011dbff02110053ccffffffffff9b']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(homeButton).click().perform();
	}
	
	
	public static void adminClickTangoeMobilityOrderRequests() 
	{
		WebElement homeButton = driver.findElement(By.xpath(".//*[@id='6fa06ea20feb1600e5680cbce1050e72']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(homeButton).click().perform();
	}	
	
	// .//*[@id='865b078b0fa74200e5680cbce1050e25']
	
	// click users button
	public static void clickUsersBtn() 
	{
		WebElement usersBtn = driver.findElement(By.xpath("//a[@id='555aed3fc0a8016600657c7b0ddc6e97']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(usersBtn).click().perform();
	}

	// click my devices button
	public static void clickMyDevicesBtn() 
	{
		WebElement myDevicesBtn = driver.findElement(By.xpath("//a[@id='77755f4f0fa74200e5680cbce1050e77']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(myDevicesBtn).click().perform();
	}
	
	// click all my orders button
	public static void clickAllMyOrders() 
	{
		WebElement myDevicesBtn = driver.findElement(By.xpath(".//*[@id='d41a5bcf0fa74200e5680cbce1050edf']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(myDevicesBtn).click().perform();
	}
	
	// jnupp
	// click settings selection when in admin mode.
	public static void clickAdminSettings() throws Exception 
	{
		WebElement settingsBtn = driver.findElement(By.xpath(".//*[@id='ae4b53030fe74200e5680cbce1050e1f']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(settingsBtn).click().perform();
	}	
}
