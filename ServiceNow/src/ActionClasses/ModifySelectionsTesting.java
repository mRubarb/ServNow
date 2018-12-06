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
	
	public static void TestOne() throws Exception 
	{
		ModifyOptions.GoToDevicesPage(currentCarrierLocal, newCarrierLocal);
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (finalListOfDevices.get(0).index + 1 ) + "]"));
		ChooseDevicePage.clickNextButton();
		ChoosePlanPage.WaitForPageToLoadPlanOrig();
		ChoosePlanPage.SelectFirstPlan();
		// at first - in plan select to modify plan options and verify the result in plan options page.
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
        
        for(Device de : finalListOfDevices)
        {
        	de.Show();
        }
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
