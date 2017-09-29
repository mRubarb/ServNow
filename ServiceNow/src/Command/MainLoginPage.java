package Command;

import org.openqa.selenium.By;

import ServiceNow.BaseClass;

public class MainLoginPage extends BaseClass
{

	public static void Login() throws Exception  
	{
	    // WaitForElementClickable(By.xpath("//input[@class='login-button']"), MainTimeout, "Error waiting for Login Button element on login page.");		
		WaitForElementClickable(By.name("Login"), MainTimeout, "Error waiting for Login Button element on login page.");
	    driver.findElement(By.name("userName")).clear();
	    driver.findElement(By.name("userName")).sendKeys(commandUserName);	    
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys(commandPassword);	    
	    driver.findElement(By.name("Login")).click();

	    WaitForElementClickable(By.id("menuMainProcure"), ExtremeTimeout, "Failed wait in command login.");	    
	}
}




