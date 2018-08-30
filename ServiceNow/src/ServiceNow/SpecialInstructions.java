package ServiceNow;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpecialInstructions extends BaseClass {

	public static void checkAllOptions() {
		
		String xpath = "//form[@id='acknowledgementsForm']/div/div/label/input";
		
		List<WebElement> checkBoxes = driver.findElements(By.xpath(xpath));
				
		// check each checkbox
		for (WebElement c: checkBoxes) {
			
			c.click();
			
		}
		
		/*
		 * String xpath = "//form[@id='acknowledgementsForm']/div";
		
		int amountCheckBoxes = driver.findElements(By.xpath(xpath)).size();
				
		// check each checkbox
		for (int i = 0; i < amountCheckBoxes; i++) {
			
			String xpathCheckbox = xpath + "[" + (i+1) + "]/div/label/input";
			driver.findElement(By.xpath(xpathCheckbox)).click();
			
		}*/
		
		
	}

	public static void clickNextButton() {
		
		driver.findElement(By.xpath("//button[text()='Next']")).click();
		
	}

	
}
