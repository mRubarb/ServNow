package ServiceNow;

import org.openqa.selenium.By;

public class SpecialInstructions extends BaseClass {

	public static void checkAllOptions() {
		
		String xpath = "//form[@id='acknowledgementsForm']/div";
		
		int amountCheckBoxes = driver.findElements(By.xpath(xpath)).size();
				
		// check each checkbox
		for (int i = 0; i < amountCheckBoxes; i++) {
			
			String xpathCheckbox = xpath + "[" + (i+1) + "]/div/label/input";
			driver.findElement(By.xpath(xpathCheckbox)).click();
			
		}
		
	}

	public static void clickNextButton() {
		
		driver.findElement(By.xpath("//button[text()='Next']")).click();
		
	}

	
}
