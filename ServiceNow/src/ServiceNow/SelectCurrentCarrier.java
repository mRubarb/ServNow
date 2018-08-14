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

	public static void selectNewCarrier(String newCarrier) {
		
		String xpathButtons = "//div/button[text()='Select']"; //"//form/div/div/div/div/div/button[text()='Select']";
		
		int amountCarriers = driver.findElements(By.xpath(xpathButtons)).size() + 1; // +1 because there's a carrier already selected and button's text is 'Unselect'
		//System.out.println("amountCarriers: " + amountCarriers);
		
		boolean buttonClicked = false;
		int i=0;
				
		while (i < amountCarriers && !buttonClicked) {
			
			String carrierFound = driver.findElement(By.xpath("//form/div[2]/div[" + (3 + i) + "]/div/div[2]/div/div")).getText();
															//  form/div[2]/div[3]/div/div[2]/div/div[1]
			//System.out.println("carrierFound: " + carrierFound);
			
			if (carrierFound.equals(newCarrier)) {
				
				String xpathButton = "//form/div[2]/div[" + (3 + i) + "]/div/div[3]/button[text()='Select']";
				driver.findElement(By.xpath(xpathButton)).click();
				buttonClicked = true;
				
			}
			
			i++;
				
		}
		
	}

}
