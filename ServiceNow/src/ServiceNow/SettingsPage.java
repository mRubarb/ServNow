package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class SettingsPage extends BaseClass 
{

	public static int checkboxCounterInit = 55;
	public static int checkboxCounter = 0;
	public static int numCheckboxesToCheck = 4;
	public static boolean hadToCheckBox = false;
	
	
	public static void MoveToBottomOfPage() throws Exception // jnupp
	{
		WebElement ele = driver.findElement(By.cssSelector(".container-fluid>button"));
		new Actions(driver).moveToElement(ele).perform();
		WaitForElementClickable(By.cssSelector(".container-fluid>button"), ShortTimeout, "");
	}

	public static void SavePage() throws Exception
	{
		driver.findElement(By.cssSelector(".container-fluid>button")).click();		
		Thread.sleep(1000); // wait for clicked element to go disabled. 
		WaitForElementClickable(By.cssSelector(".container-fluid>button"), MediumTimeout, "");
	}
	
	// set check boxes for features 10. 11. 12, and 13 true and save
	public static void SetCheckboxesTrue() throws Exception
	{
		MoveToBottomOfPage();
		
		checkboxCounter = checkboxCounterInit;
		hadToCheckBox = false;
		
		for(int x = 0; x < numCheckboxesToCheck; x++)
		{
			if(!driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + checkboxCounter + ")>td>input:nth-of-type(1)")).isSelected())
			{
				driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + checkboxCounter + ")>td>input:nth-of-type(1)")).click();
				hadToCheckBox = true;
			}			
			checkboxCounter+=2;
		}
		
		// if a check box was selected, have to save the new settings. 
		if(hadToCheckBox) 
		{
			ShowText("Had to set unchecked checbox(es).");
			SavePage();
		}
	}
	
	public static void SelectFeature10() throws Exception
	{
		MoveToBottomOfPage();
		WaitForElementClickable(By.cssSelector("tbody>tr:nth-of-type(" + checkboxCounterInit + ")>td>input:nth-of-type(1)"), MediumTimeout, "");
		driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + checkboxCounterInit + ")>td>input:nth-of-type(1)")).click();
		SavePage();
	}
}
