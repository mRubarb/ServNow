package ActionClasses;

import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.base.Verify;

import ServiceNow.BaseClass;
import ServiceNow.ChooseAccessoriesPage;
import ServiceNow.ChooseDevicePage;
import ServiceNow.ChoosePlanPage;
import ServiceNow.ModifyOptions;

public class ModifySelectionsTesting extends BaseClass 
{

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE !!! - the carrier variables below are for this test only. This combination makes a large list of devices that can be added.
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static String currentCarrierLocal = "Sprint";
	public static String newCarrierLocal = "Verizon Wireless";
	public static String xpathToOptionsList = "//h3[text()='Optional Features']/following-sibling::ul/li";
	public static String xpathClickSecondModify = "(//a[text()='Modify'])[2]";
	public static String xpathPlanOptionOne = "(" + xpathToOptionsList + ")[1]";
	public static String lineFeed = "\r\n";
    
	public static List<Device> listOfDevices = new ArrayList<Device>(); 
	public static List<Device> finalListOfDevices = new ArrayList<Device>();

	int deviceListSize = -1;
	
	public static BufferedWriter output = null;
	
	// load the devices, that have plans, and their index onto a list
	// add info about plan options, and accessories to each.
	// create final list for testing.
	public static void LoadDeviceData() throws Exception
	{
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);

		ChooseDevicePage.SetupForDeviceSelection(); // setup list of objects that hold information for each device.

		// this goes through the list of devices and adds all devices that that have a plan to the local 'listOfDevices' list of device objects.
		for(int x = 0; x < ChooseDevicePage.deviceVendorList.size(); x++)
		{
			ChooseDevicePage.addDeviceToOrder(); // this adds the test device.
			ChooseDevicePage.clickNextButton(); //Click Next. You are in the Choose a Plan step. The available plans for the selected device are listed.
			if(ChoosePlanPage.waitForPageToLoadPlans())
			{
				ChoosePlanPage.clickBackButton();
				listOfDevices.add(new Device(ChooseDevicePage.deviceVendorList.get(x).device, x));
			}
			ChooseDevicePage.WaitForPageToLoad();
		}
		
		ClearListSelection();
		// for(Device dev : listOfDevices){dev.Show();} // show device list
		
		PopulatedDeviceList();
		
		CreateFinalDeviceList();
		// for(Device dev : finalListOfDevices){dev.Show();} // show final device list
		
		// output final device list to file.
		OutputFinalDeviceListToFile();
	}
	
	// select modify in choose your plan page, select the first option, go back to device page, and verify it shows up in the device page.
	public static void TestOne() throws Exception 
	{
		String actual =  "";
		String expected =  "";		
		
		// go to devices page, select first device in devices list and select the first plan.
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(0).index + 1 ) + "]")).click();
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanOrig();
		ChoosePlanPage.SelectFirstPlan();
		
		// click modify for plan optional features, add the first option, and go back to main plan page.
		WaitForElementClickable(By.xpath(xpathClickSecondModify), MediumTimeout, "");
		driver.findElement(By.xpath(xpathClickSecondModify)).click(); // select modify
		WaitForElementPresent(By.xpath(xpathPlanOptionOne), ShortTimeout);
		driver.findElement(By.xpath(xpathPlanOptionOne)).click(); // select first option
		ChoosePlanPage.clickBackButton(); // go back to plan page
		ChoosePlanPage.WaitForPageToLoadPlanAndOptionSelected();
		
		// verify the proper plan optional feature selected through the modify selection is present in the Plan Features section.
		// NOTE: the exact text is not tested for. the plan name is only verified to be in the text block of the  
		WaitForElementPresent(By.xpath("//div[@ng-if='showPlanFeatures()']"), ShortTimeout);
		actual = driver.findElement(By.xpath("//div[@ng-if='showPlanFeatures()']")).getText().replace("\n", "");
		expected = finalListOfDevices.get(0).optionsList.get(0).split("-")[0].trim();
		Assert.assertTrue("", actual.contains(expected));
		
		//Pause("");
		
		// TODO:
		// select next and select 'Modify' on the device to change the device.
		
		// now select a different device and select its plan. 
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(0).index + 2 ) + "]")).click();
		
		//Pause("");
	
	
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// go through all device objects that have a plan in 'listOfDevices' and add items to the devices.
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	public static void PopulatedDeviceList() throws Exception
	{
		List<WebElement> tempWebList = new ArrayList<WebElement>();
		
		ShowText(" *** Start PopulatedDeviceList");
		for(Device dv : listOfDevices)
		{
			// select a device in list of devices.
			WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]"), MediumTimeout, "");
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (dv.index + 1) + "]")).click();
			System.out.println(dv.name  + " " + dv.index);
			
			// go through plan, accessory options, to accessories and mark each device that has each of these.   
			ChooseDevicePage.clickNextButton(); 
			
			ChoosePlanPage.WaitForPageToLoadPlanOrig();
			ChoosePlanPage.SelectFirstPlanIndexOne(); // select first plan
			ChoosePlanPage.clickNextButton();

			if(WaitForPlanOptions() > 0)
			{
				tempWebList = driver.findElements(By.xpath(xpathToOptionsList));
				for(WebElement ele : tempWebList)
				{
					dv.optionsList.add(ele.getText());
				}
			}
			
			ChoosePlanPage.clickNextButton();
			
			if(WaitForAccessoriesPage()) // see if there are accessories
			{
				tempWebList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
				for(WebElement ele : tempWebList)
				{
					dv.accessoryList.add(ele.getText());
				}
			}
			
			// go back to devices page
			ChooseAccessoriesPage.clickBackBtn();
			ChoosePlanPage.WaitForPageToLoadPlanSelected();
			ChoosePlanPage.clickBackButton();
			
			// clear device page and pop-up
			ClearListSelection();
			CloseDevicePageWarningPopUp();
			Thread.sleep(2000); // need pause here to allow pop-up to go away. if no pause, there is a click conflict. 
		}
	}

	public static void PopulatedDeviceListFromFile() throws Exception
	{
		ReadInFinalDeviceList();
	}
	
	public static void ReadInFinalDeviceList() throws IOException
	{
		String projFile = System.getProperty("user.dir");
		projFile = projFile.replace("\\", "/");
		String inputFile = projFile + "/DevicesOutFile.txt";
		BufferedReader in = new BufferedReader(new FileReader(inputFile));
		
		String propertyName = "";
		String tempName = "";
		String tempOptions = "";
		String tempAccessories = "";		
        String str;
        String [] strArray;
		int tempIndex = -1;        
        
		finalListOfDevices.clear();

		while ((str = in.readLine()) != null) 
        {
        	propertyName = str.split(":::")[0];
        	
        	switch(propertyName)
        	{
	        	case "name":
	        		tempName = str.split(":::")[1];
	        		break;
	        	case "index":
	        		tempIndex = Integer.parseInt(str.split(":::")[1]);
	        		break;
	        	case "OptionsList":
	        		tempOptions = str.split(":::")[1];
	        		break;
	        	case "AccessoriesList": // this is the last item read in for one block of information for a device.
	        		tempAccessories = str.split(":::")[1];
	        		
	        		Device tempDevice = new Device(tempName, tempIndex);
	        		
	        		// build list for plan options and put in Device object	
	        		strArray = tempOptions.split(",");
	        		for(String strLoop : strArray){tempDevice.optionsList.add(strLoop);}
 
	        		// build list for accessories and put in Device object	
	        		strArray = tempAccessories.split(",");
	        		for(String strLoop : strArray){tempDevice.accessoryList.add(strLoop);}

	        		finalListOfDevices.add(tempDevice); // add the device that was setup.
	        		
	        		break;	        		
	        	default:
	        		Assert.fail("Method 'ReadInFinalDeviceList' in 'ModifySelectionsTesting' class has bad input in input file.");
        	}

        }
        in.close();
        
        //for(Device de : finalListOfDevices){de.Show();}
	}
	
	
	public static void CreateFinalDeviceList()
	{
		ShowText(" *** Start CreateFinalDeviceList");
		for(Device dev : listOfDevices)
		{
			if (dev.accessoryList.size() > 0 && dev.optionsList.size() > 0)
			{
				finalListOfDevices.add(dev);
			}
		}
		Assert.assertTrue("Final device list is incomplete. Test can not be run.", finalListOfDevices.size() > 1);
	}

	public static void OutputFinalDeviceListToFile() throws IOException
	{
		String projFile = System.getProperty("user.dir");
		projFile = projFile.replace("\\", "/");
		
		File file = new File(projFile +   "/DevicesOutFile.txt");
        output = new BufferedWriter(new FileWriter(file));
		
		for(Device dev : finalListOfDevices)
		{
			dev.OutputToFile(output);
		}
		output.close();		
	}
	
	
	public static void CloseDevicePageWarningPopUp()
	{
		WaitForElementClickable(By.xpath("//button[text()='OK']"), MediumTimeout, "");
		driver.findElement(By.xpath("//button[text()='OK']")).click();
	}
	
	public static void JumpToDevicesPage()
	{
		WaitForElementClickable(By.xpath("//div[text()='Choose a Device']"), MediumTimeout, "");
		driver.findElement(By.xpath("//div[text()='Choose a Device']")).click();
	}
	
	public static void ClearListSelection()
	{
		String buttonXpath = "//button[text()='Remove from Cart']";
		try
		{
			WaitForElementClickable(By.xpath(buttonXpath), MediumTimeout, "");
			driver.findElement(By.xpath(buttonXpath)).click();
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static boolean WaitForAccessoriesPage()
	{
		try
		{
			WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout - ShortTimeout, "");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public static int WaitForPlanOptions()
	{
		try
		{
			List<WebElement> eleList = driver.findElements(By.xpath(xpathToOptionsList));			
			return eleList.size();
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	// //////////////////////////////////////////////////////
	// 						objects
	// //////////////////////////////////////////////////////
	public static class Device
	{
		public String name = "";
		public String outFileName = ""; 
		public int index = -1;
		public boolean isFirstLoop = true;
		public List<String> optionsList = new ArrayList<String>();  
		public List<String> accessoryList = new ArrayList<String>();
		
		Device(String nm, int ind)
		{
			name = nm;
			index = ind;
		}
		
		public void Show()
		{
			System.out.println("------------------------ ");
			System.out.println("name =  " + name);
			System.out.println("index =  " + index);
			System.out.println("options list size =  " + optionsList.size());
			System.out.println(optionsList);
			System.out.println(accessoryList);
		}
		
		public void OutputToFile(BufferedWriter fileHandle) throws IOException
		{
		    String lineFeed = "\r\n";
		    String strOptionsList = "";
		    String strAccessoriesList = "";		    
			fileHandle.write("name:::" + name + lineFeed);
			fileHandle.write("index:::" + index + lineFeed);
			for(String str : optionsList)
			{
				if(isFirstLoop)
				{
					strOptionsList += str.trim();					
				}
				else
				{
					strOptionsList += "," + str.trim();					
				}
				isFirstLoop = false;
			}
			
			isFirstLoop = true;
			for(String str : accessoryList)
			{
				if(isFirstLoop)
				{
					strAccessoriesList += str.trim();
				}
				else
				{
					strAccessoriesList += "," + str.trim();					
				}
				isFirstLoop = false;
			}
			
			fileHandle.write("OptionsList:::" + strOptionsList + lineFeed);
			fileHandle.write("AccessoriesList:::" + strAccessoriesList + lineFeed);
			
		}
	}	
}
