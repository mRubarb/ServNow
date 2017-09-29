package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomePage extends BaseClass
{
	public static WebElement element = null;
	
	public static void WaitForPageToLoad() throws Exception
	{
		WaitForElementVisible(By.xpath("//div[text()='Order a New Device and Service']"), ExtremeTimeout);
		WaitForElementVisible(By.xpath("//div/a[text()='Create an Order']"), MainTimeout);
	}

	public static void SelectViewDevices() throws Exception
	{
		WaitForElementVisible(By.xpath("//a[text()='View your Devices']"), MediumTimeout);
		driver.findElement(By.xpath("//a[text()='View your Devices']")).click();
	}
	
	//Create an Order Button
	public static WebElement createAnOrderButton(){
		element  = driver.findElement(By.cssSelector(".tg-button.tg-button--primary.tg-button--large"));
		return element;
	}
	
	
	public static WebElement myDevicesLink(){
	element = driver.findElement(By.xpath("//a[@id='77755f4f0fa74200e5680cbce1050e77']"));
	return element;
	}
		
	
	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		Methods/Actions			 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This section Is for all Methods and Actions
	public static void clickMyDevicesLink(){
		element = myDevicesLink();
		element.click();
	}
	
	
	

	/*###################################################################################################
	 * #######################                       	     ############################################
	 * #######################		 Fills and Clicks		 ############################################
	 * #######################                               ############################################
	 * ##################################################################################################
	 */ //This Section is for all clickable actions
	public static void clickCreateAnOrderButton()
	{
		WaitForElementClickable(By.cssSelector(".tg-button.tg-button--primary.tg-button--large"), ShortTimeout, "Failed to find home page button in 'clickCreateAnOrderButton'.");
		element = createAnOrderButton();
		element.click();
	}
}
