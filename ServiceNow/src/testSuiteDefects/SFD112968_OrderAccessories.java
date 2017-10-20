package testSuiteDefects;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ActionClasses.OrderAccessories;
import ActionsBaseClasses.CommonTestSteps;

import ServiceNow.BaseClass;


public class SFD112968_OrderAccessories extends BaseClass {

	@BeforeClass
	public static void setUp() throws Exception
	{
		
	}

	@Test
	public static void tc00018OrderAccessoriesApprove() throws Exception
	{
		BaseClass.stepComplete("------------------ Starting Order Accessories - SFD112968 -----------------------", "");		

				
		// login as limited user.
		CommonTestSteps.LoginLimitedUser();
		
		// got to the devices page through the home page. 
		CommonTestSteps.GoToDevicesPage();

		boolean accessoriesAvailable = OrderAccessories.runOrderAccessories_SFD112968();
				
		CommonTestSteps.Logout();

		if (!accessoriesAvailable) {
			
			JOptionPane.showMessageDialog(frame, "No accessories available for selected device. Select a different device to verify defect.");
			
		}
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser.");
		driver.close();
		driver.quit();
	}		
	
	
}
