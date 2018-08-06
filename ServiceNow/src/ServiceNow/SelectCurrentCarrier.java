package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class SelectCurrentCarrier extends BaseClass {

	public static void selectCurrentCarrier(String carrier) {
		
		WaitForElementClickable(By.id("currentXferInCurrentCarrier"), 5, "Dropdown not clickable");
		
		new Select(driver.findElement(By.id("currentXferInCurrentCarrier"))).selectByVisibleText(carrier);
		
	}

	public static void clickNextButton() {
		
		driver.findElement(By.xpath("//button[text()='Next']")).click();
		
	}

}
