package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BaseClass 
{
	public static WebElement element = null;
	public static String userName = "";
	public static String password = "";	
	
	public static void Login() throws InterruptedException
	{
		switch(userLoginMode)
		{
			case Admin:
			{
				userName = userAdmin;
				password = userAdminPasswd;
				break;
			}
			case Approver:
			{
				userName = userApprover;
				password = userApproverPasswd;
				break;
			}
			case Limited:
			{
				userName = userLimited;
				password = userLimitedPasswd;
				break;
			}
		}
		
		WaitForElementClickable(By.xpath("//input[@id='user_name']"), MediumTimeout, "Login name field not found in login page.");
		driver.findElement(By.xpath("//input[@id='user_name']")).clear();		
		driver.findElement(By.xpath("//input[@id='user_name']")).sendKeys(userName);		
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys(password);		
		WaitForElementClickable(By.id("sysverb_login"), MediumTimeout, "Login button not found in login page.");
		driver.findElement(By.id("sysverb_login")).click();
		
	}
	
	
	// loginButton
	public static WebElement loginButton() 
	{
		element = driver.findElement(By.id("sysverb_login"));
		return element;
	}
		
	
	// userNameTextBox
	public static WebElement userNameTextBox() {
		element = driver.findElement(By.xpath("//input[@id='user_name']"));
		return element;
	}

	// passwordTextBox
	public static WebElement passwordTextBox() {
		element = driver.findElement(By.id("user_password"));
		return element;
	}
		
	//Fill in UserName text box
	public static void fillUserNameTextBox (String userName) {
		element = userNameTextBox();
		element.sendKeys(userName);
	}
		
	//Fill in password Text box
	public static void fillPasswordTextBox(String password) {
		element = passwordTextBox();
		element.sendKeys(tcAllSN_Password);
	}
	
	//click login button
	public static void clickLoginButton() {
		element = loginButton();
		element.click();
	}
		
	
}
