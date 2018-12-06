package ServiceNow;

import ActionsBaseClasses.CommonTestSteps;

public class ModifyOptions extends BaseClass 
{

	public static void GoToDevicesPage(String currentCarrierLocal, String newCarrierLocal) throws Exception
	{
		CommonTestSteps.GoToHomePage();
		
		HomePage.clickTransferServiceInButton();

		SelectRegionPage.selectCountryFromDropDown(); // setup postal code
		SelectRegionPage.fillPostalCodeTextBox("01803");
		SelectRegionPage.clickNextButtonSelectRegion(); // move to device select page.
		
		SelectCurrentCarrier.selectCurrentCarrier(currentCarrierLocal); // Select current carrier from drop-down list
		SelectCurrentCarrier.selectNewCarrier(newCarrierLocal); // Select a new carrier - service will be moved to a different carrier.
		SelectCurrentCarrier.clickNextButton();
		
		SpecialInstructions.checkAllOptions(); // accept message 
		SpecialInstructions.clickNextButton();
		
		ChooseDevicePage.WaitForPageToLoad();

	}
	
	
}
