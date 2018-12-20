package ServiceNow;

import org.openqa.selenium.By;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 12/20/18
// these are methods found to be identical in VerifyOrder and OrderSubmitted pages- except for error message. 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class CommonVerifyPageSubmitPage extends BaseClass 
{

	// this verification is called when processing a transfer service out. 
	public static void VerifyAdditionalInformationTransferServiceOut() throws Exception
	{
		String errorMessage = "Failure in checking additional information for Transfer Service Out"; 		

		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[0], additionalInstructions, errorMessage);
		ActionsBase.VerifyDates(birthDate, GetAdditionalInfoTransferServiceOut()[1]);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[2], contactNumber, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[3], extension, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[4], approverAdminMail, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[5], socialSecurityNumber, errorMessage);		
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[6], licenseNumber, errorMessage);		
		ActionsBase.VerifyDates(licenseExpire, GetAdditionalInfoTransferServiceOut()[7]);		
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[8], userStateShort, errorMessage);
		Assert.assertEquals(GetAdditionalInfoTransferServiceOut()[9], serviceNumber, errorMessage);

	}	

	public static String [] GetAdditionalInfoTransferServiceOut()
	{
		String [] strArray = driver.findElement(By.xpath("//tbody")).getText().split("\n");
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
		String [] tmpStringArray = driver.findElement(By.xpath("(//table/tbody)[1]")).getText().split("\n");

		Assert.assertEquals(tmpStringArray[0].replace("Contact Phone Number ", ""), contactNumber, errorMessage);
		Assert.assertEquals(tmpStringArray[1].replace("Additional Instructions ",""), additionalInstructions, errorMessage);		
		Assert.assertEquals(tmpStringArray[2].replace("Service Number ",""), serviceNumber, errorMessage);		
		Assert.assertEquals(tmpStringArray[3].replace("Reason ",""), reasonAction, errorMessage);		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
