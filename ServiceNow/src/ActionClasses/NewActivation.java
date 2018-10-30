package ActionClasses;

import org.openqa.selenium.By;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;
import ServiceNow.Frames;
import ServiceNow.SelectRegionPage;
import ServiceNow.SideBar;

public class NewActivation extends ActionsBase
{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// this is the main method for this class. it goes through the pages needed to do a new activation. 
	// and does verification. It uses as much of the existing page object methods it can. In cases where 
	// it can't use the existing page object methods it uses helper methods found in this class. 
	// The helper methods are further below.
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void RunNewActivation() throws Exception
	{

		CommonTestSteps.GoToHomePage();
		
		// select to create a new order. //a[text()='Create an order']
		driver.findElement(By.xpath("//a[text()='Create an order']")).click();
		
		SelectRegionPage.selectCountryFromDropDown();
		SelectRegionPage.fillPostalCodeTextBox("01803");
		SelectRegionPage.clickNextButtonSelectRegion();
	
	
	}
	
	
	
	
}
