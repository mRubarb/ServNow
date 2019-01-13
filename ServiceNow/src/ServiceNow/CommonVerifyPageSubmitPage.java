package ServiceNow;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionClasses.UpgradeDevice;
import ActionsBaseClasses.ActionsBase;
import HelperObjects.PlanInfoActions;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 12/20/18
// these are methods found to be identical in VerifyOrder and OrderSubmitted pages- except for error message. 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class CommonVerifyPageSubmitPage extends BaseClass 
{

	public static String [] strArray;
	
	// this verification is called when processing a transfer service out. 
	public static void VerifyAdditionalInformationTransferServiceOut() throws Exception
	{
		String errorMessage = "Failure in checking additional information for Transfer Service Out"; 		

		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[0], additionalInstructions, errorMessage);
		ActionsBase.VerifyDatesWithDifferentFormatsAreEqual(birthDate, GetAdditionalInfoTransferServiceOut()[1]);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[2], contactNumber, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[3], extension, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[4], approverAdminMail, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[5], socialSecurityNumber, errorMessage);		
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[6], licenseNumber, errorMessage);		
		ActionsBase.VerifyDatesWithDifferentFormatsAreEqual(licenseExpire, GetAdditionalInfoTransferServiceOut()[7]);		
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[8], userStateShort, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[9], serviceNumber, errorMessage);
	}	

	public static String [] GetAdditionalInfoTransferServiceOut()
	{
		strArray = driver.findElement(By.xpath("//tbody")).getText().split("\n");
		return new String [] {strArray[0].replace("Additional Instructions ", ""),
				  			  strArray[1].replace("Date of Birth ", ""),				
							  strArray[2].replace("Contact Phone Number ", ""),
				              strArray[3].replace("Ext ", ""),
							  strArray[4].replace("Personal E-mail Address ", ""), 
							  strArray[5].replace("Social Security Number ", ""),							  
							  strArray[6].replace("Driver's License Number ", ""),							  
							  strArray[7].replace("Driver's License Exp. Date ", ""),							  
							  strArray[8].replace("Driver's License State/ Province ", ""),							  
							  strArray[9].replace("Service Number ", "")};
	}
	
	public static void VerifyAdditionalInformationDeactivate() throws Exception
	{
		String errorMessage = "Incorrect information found in Order Details page in OrderSubmittedPage.VerifyAdditionalInformationDeactivate.";
		strArray = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");

		Assert.assertEquals(strArray[0].replace("Contact Phone Number ", ""), contactNumber, errorMessage);
		Assert.assertEquals(strArray[1].replace("Additional Instructions ",""), additionalInstructions, errorMessage);		
		Assert.assertEquals(strArray[2].replace("Service Number ",""), serviceNumber, errorMessage);		
		Assert.assertEquals(strArray[3].replace("Reason ",""), reasonAction, errorMessage);		
	}	

	public static void VerifyAdditionalInformationOrderAccessories() 
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationOrderAccessories.";		

		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		
		
		Assert.assertEquals(strArray[0].replace("Contact Phone Number ", ""), contactNumber, errMessage);
		Assert.assertEquals(strArray[1].replace("Ext ", ""), extension, errMessage);		
		Assert.assertEquals(strArray[2].replace("Additional Instructions ", ""), additionalInstructions, errMessage);
		Assert.assertEquals(strArray[3].replace("Service Number ", ""), serviceNumber, errMessage);
	}	
	
	public static void VerifyAdditionalInformationUpgradeDevice() throws Exception  
	{
		String errMessage = "Failure in verify Additional Information in VerifyOrderPage.VerifyAdditionalInformationOderAccessories.";		
		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div[1]")).getText().split("\n");		
		
		//VerifyAdditionalInformationCommon(strArray, errMessage);
		VerifyAdditionalInformationOrderAccessories();
		Assert.assertEquals(strArray[4].replace("Reason ", ""), UpgradeDevice.reasonUpgradeDevice, errMessage);		
	} 	
	
	public static void VerifyAdditionalInformationTransferServiceIn()
	{
		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div/table/tbody")).getText().split("\n");
		//for(String str : strArray){ShowText(str);}

		Assert.assertEquals(strArray[0].replace("Service Number ",""), newServiceNumber, "");
		Assert.assertEquals(strArray[1].replace("Carrier Account Number ",""), PlanInfoActions.carrierAccountNumber, "");
		Assert.assertEquals(strArray[2].replace("Name on Invoice ",""), userLimitedShorterName, "");
		Assert.assertEquals(strArray[3].replace("Contact Phone Number ",""), contactNumber, "");
		Assert.assertEquals(strArray[4].replace("Ext ",""), extension, "");
		Assert.assertEquals(strArray[5].replace("Additional Instructions ",""), additionalInstructions, "");

	}	

	public static void VerifyAdditionalInformationTransferServiceInAndPort()
	{
		strArray = driver.findElement(By.xpath("//div[text()='Additional Information']/following ::div/table/tbody")).getText().split("\n");
		//for(String str : strArray){ShowText(str);}
		
		Assert.assertEquals(strArray[0].replace("Carrier Account Number ",""), PlanInfoActions.carrierAccountNumber, "");
		Assert.assertEquals(strArray[1].replace("Name on Invoice ",""), userLimitedShorterName, "");
		Assert.assertEquals(strArray[2].replace("Contact Phone Number ",""), contactNumber, "");
		Assert.assertEquals(strArray[3].replace("Ext ",""), extension, "");
		Assert.assertEquals(strArray[4].replace("Additional Instructions ",""), additionalInstructions, "");
		Assert.assertEquals(strArray[5].replace("Service Number ",""), newServiceNumber, "");
	}
	
}
