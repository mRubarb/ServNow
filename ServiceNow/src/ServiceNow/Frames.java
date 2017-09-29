package ServiceNow;

import org.openqa.selenium.By;

public class Frames extends BaseClass{
	public static void switchToGsftMainFrame(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.id("gsft_main")));
	}
	
	public static void switchToGsftNavFrame(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.id("gsft_nav")));
	}
	
	public static void switchToDefaultFrame(){
		driver.switchTo().defaultContent();
	}
}
