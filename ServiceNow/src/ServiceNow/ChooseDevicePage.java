package ServiceNow;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.DeviceInfoActions;
import HelperObjects.DevicePortNumber;


// http://www.tutorialspoint.com/guava/guava_multimap.htm
//these are for multimap.

/*
import org.testng.collections.MultiMap;
import com.google.common.collect.Multimap;
*/


public class ChooseDevicePage extends BaseClass
{
	public static WebElement element = null;

	// these are for selecting a device to add to the cart. they are setup in 'SetupForDeviceSelection()'.
	static int cartButtonIndexToSelectForAdd = 0;
	static String deviceTextForSelectedDevice = "";	
	static int numAddCartButtons = 0;

	public static String errMessage = "";
	public static String [] strArray;	
		
	public static List<DeviceAndVendor> deviceVendorList = new ArrayList<DeviceAndVendor>(); // this holds devices and their vendor/price info.
	public static List<WebElement> webEleListDevices = new ArrayList<WebElement>(); // devices
	public static List<WebElement> webEleListVendor = new ArrayList<WebElement>(); // vendors and their cost 

	// Store away each device with its vendor name and cost.
	public static void SetupForDeviceSelection() throws Exception 
	{
		// clear lists from any previous use. 
		webEleListDevices.clear();
		webEleListVendor.clear();		
		deviceVendorList.clear();
		
		webEleListDevices = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding")); // list of devices in web elements.
		webEleListVendor = driver.findElements(By.cssSelector(".tg-valign--top.ng-binding")); // list of vendors and their cost in web elements.
		
		Assert.assertTrue((webEleListDevices.size() * 2) == webEleListVendor.size(), "Device and vendor lists sizes ahould be the same in 'ChooseDevicePage.SetupForDeviceSelectionBetter()'");
		
		// create a list of objects, each holding the device and the device's vendor name and price.
		for( int x = 0; x < webEleListDevices.size(); x++ )
		{
			DeviceAndVendor tempObj = new DeviceAndVendor(webEleListDevices.get(x).getText(), webEleListVendor.get(x * 2).getText(), webEleListVendor.get((x * 2) + 1).getText());
			deviceVendorList.add(tempObj);
		}		
		
		//for(int x = 0; x < deviceVendorList.size(); x++) {deviceVendorList.get(x).Show();} // DEBUG. show each object.
	}			
	
	//
	// NOTE: this uses the list that was setup above in method SetupForDeviceSelection()
	//
	// 1) selects a device from the device list and stores the index of the device in the deviceVendorList.
	// 2) populates the deviceInfoActions class.
	public static void SelectUpgradeDeviceAndStoreDeviceInfoIndex() throws Exception 
	{
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		int offset = 0;
		
		//go through the list that was populated in the SetupForDeviceSelection() method further above.
		for(int i = 0; i < deviceVendorList.size(); i++)
		{
			
			WaitForElementClickable(By.xpath("//div/div[" + (i + 1) + "]/div/div/div/div/div/button[text()='Add to Cart']"), MediumTimeout, "");
			
			WebElement buttonAddToCart = driver.findElement(By.xpath("//div/div[" + (i + 1) + "]/div/div/div/div/div/button[text()='Add to Cart']"));
			
			offset = i * 100;
			jse.executeScript("window.scrollBy(0, " + offset + ")", "");		
			
			buttonAddToCart.click();
			
			DeviceInfoActions.upgradeDeviceIndex = i; // use static property in DeviceInfoActions class.
			
			// use instantiated deviceInfoActions class that was instantiated in the base class.
			// populate it with info from the list element that contains the static hard-coded device name in DeviceInfoActions.    
			deviceInfoActions.name = deviceVendorList.get(i).device;
			deviceInfoActions.cost = deviceVendorList.get(i).cost;
			deviceInfoActions.vendor = deviceVendorList.get(i).vendor;
			
			System.out.println("Device Selected: " +  deviceInfoActions.name);
			
		}
		
		jse.executeScript("window.scrollBy(0, " + (-offset) + ")", "");		
		
	}
		
	// this adds and removes each device. it verifies the device and cost in shopping cart is added and removed. 
	// then it adds the test device to the shopping cart.
	public static void AddRemoveAllDevices() throws Exception 
	{
		WaitForElementVisible(By.xpath("//h3[text()='Shopping Cart']"), MediumTimeout); // make sure cart is showing.  
		
		// wait for 10 add to cart buttons.
		WaitForElements(By.xpath("//button[text()='Add to Cart']"), MainTimeout, 10);

		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		int offset = 0;
		
		for(int x = 0; x < deviceVendorList.size(); x++)
		{
			
			offset = x * 100;
			jse.executeScript("window.scrollBy(0, " + offset + ")", "");		
			
			// select check box with value 'x' and wait for the devices section. 
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (x + 1) + "]")).click();
			
			WaitForElementVisible(By.xpath(shopCartDev), MediumTimeout);			

			// get the device text and compare to expected in the device/vendor object cost.
			Assert.assertEquals(driver.findElement(By.xpath(shopCartDev + "/div/div/div[1]")).getText(), deviceVendorList.get(x).device
					,"Fail in ChooseDevicePage.AddRemoveAllDevices(). Expected device name doesn't equal actual.");

			// get the cost text and compare to expected in the device/vendor object cost.
			Assert.assertEquals(driver.findElement(By.xpath(shopCartDev + "/div/descendant ::div[text()='" + deviceVendorList.get(x).device + "']/following-sibling:: div")).getText()
					,deviceVendorList.get(x).cost, "Fail in ChooseDevicePage.AddRemoveAllDevices(). Expected cost value doesn't equal actual.");
			
			// verify correct number of add/remove buttons.
			Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Remove from Cart']")).size() == 1
					, "Number of remove from cart buttons in ChooseDevicePage.AddRemoveAllDevices() is incorrect.");
			
			Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() == deviceVendorList.size() - 1
					, "Number of add to cart buttons in ChooseDevicePage.AddRemoveAllDevices() is incorrect.");			
			
			// remove device from cart and verify there is no "device" section in the shopping cart.
			driver.findElement(By.xpath("//button[text()='Remove from Cart']")).click();		
			// NOTE: this is slow.
			Assert.assertTrue(WaitForElementNotPresentNoThrow(By.xpath(shopCartDev), MiniTimeout),
					"Found element that is not supposed to be visible in 'ChooseDevicePage.SetupForDeviceSelection'");
		} 	
		
		jse.executeScript("window.scrollBy(0, " + (-offset) + ")", "");	
		
	}		
	

	public static void addDeviceToOrder()
	{
		// find the device to be used for the rest of the testing and add it. also store its cost.
				
		int i = indexDeviceSelectedNewActivation;
		//System.out.println("indexDeviceSelectedNewActivation: " + indexDeviceSelectedNewActivation);
		
		deviceInfoActions.cost = deviceVendorList.get(i).cost; // put its cost into DeviceInfoActions object for later use.
		deviceInfoActions.name = deviceVendorList.get(i).device;  // added by Ana - 9/8/17 - to replace the hardcoded value assigned on the constructor 
		deviceInfoActions.vendor = deviceVendorList.get(i).vendor;  // added by Ana - 9/8/17 - to replace the hardcoded value assigned on the constructor
		
		int index = i + 1;
		
		System.out.println("Device Selected: " + driver.findElement(By.cssSelector("div.tg-display--table.ng-scope:nth-of-type(" + index + ")>div>div>div")).getText());
		//System.out.println("Vendor: " + driver.findElement(By.xpath("(//div/div/table/tbody/tr[1]/td[2])[" + index + "]")).getText());
		//System.out.println("Cost: " + driver.findElement(By.xpath("(//div/div/table/tbody/tr[2]/td[2])[" + index + "]")).getText());
		
		// Scroll down based on the device's index. This is to make sure that the 'Add to cart' button that needs to be clicked is visible.
		// If button is not visible, it is not clickable. 
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0, " + (i * 110) + ")", "");				
		
		int buttonIndex = index;
		
		if (!isFirstPass) {
			buttonIndex--;
		}
				
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + buttonIndex + "]")).click(); // add device to be used in future tests.
		indexDeviceSelectedNewActivation++;
		isFirstPass = false;
		
		AccessoriesDetailsExpected.finalCost = deviceInfoActions.DeviceCostRemoveDollarSign(); 
				
	}
	
	// ** NOT USED **
	/*public static void clickAddToCartButtonDevice() 
	{
		WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[" + cartButtonIndexToSelectForAdd + "]"), MainTimeout, "Failed in clickAddToCartButtonDevice1.");
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + cartButtonIndexToSelectForAdd + "]")).click();
	}*/	
	
	// this selects the first device.  
	// rather than the list in this class, so it can be accessed everywhere.
	public static void AddPortDevice() throws InterruptedException 
	{
		// temp list  for holding all devices in list. 
		ArrayList<DevicePortNumber> localList = new ArrayList<DevicePortNumber>();	
		
		// this verifies there is at least one device in the list.
		List<WebElement> testList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
		Assert.assertFalse(testList.isEmpty(),"Error in ChooseDevicePage.LoadPortDevices. There are no devices in device list.");

		// store all the device and select the first one.
		// store away devices.
		List<WebElement> listNames = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
		List<WebElement> listVendorPrice = driver.findElements(By.cssSelector(".tg-valign--top.ng-binding"));
		
		for(int x = 0, y = 0; x <  listNames.size(); x++, y +=2)
		{
			DevicePortNumber tempDev =  new DevicePortNumber(listNames.get(x).getText(), listVendorPrice.get(y).getText(), listVendorPrice.get(y + 1).getText());
			localList.add(tempDev);
		}

		// select first device and store its info. 
		DevicePortNumber.selectedDeviceName = listNames.get(0).getText();
		DevicePortNumber.selectedDeviceVendor = listVendorPrice.get(0).getText();
		DevicePortNumber.selectedDevicePrice = listVendorPrice.get(1).getText();		
		
		deviceInfoActions.name = DevicePortNumber.selectedDeviceName;
		deviceInfoActions.vendor = DevicePortNumber.selectedDeviceVendor;
		deviceInfoActions.cost = DevicePortNumber.selectedDevicePrice;
		
		AccessoriesDetailsExpected.finalCost = DevicePortNumber.selectedDevicePrice.replace("$", ""); 
		
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();		
	}		
	
	public static void verifyDeviceInShoppingCart() throws Exception 
	{
		// check if element is visible in shopping cart.
		Assert.assertTrue(WaitForElementVisible(By.xpath("//div[text()='Device']/following-sibling :: div[1]/descendant :: div[text()='" + deviceTextForSelectedDevice + "']"), 
				          ShortTimeout), "Failed to find expected device in cart - method is 'ChooseDevicePage.verifyDevice1InShoppingCart()'");
	}

	public static void WaitForPageToLoad() throws Exception
	{
		errMessage = "Failed wait in ChooseDevicePage.WaitForPageToLoad";
		
		// wait for next button at bottom of page and wait for button #5.
		WaitForElementClickable(By.xpath("(//div/button[text()='Next'])[2]"), ExtremeTimeout, errMessage);
		WaitForElementPresent(By.xpath("(//div/button[text()='Add to Cart'])[5]"), MainTimeout); //		
	}
	
	
	public static void WaitForPageToLoadUpgradeDevice() throws Exception
	{
		errMessage = "Failed wait in ChooseDevicePage.WaitForPageToLoadUpgradeDevice";		
		
		// wait for next button at bottom of page and wait for button #3.
		WaitForElementClickable(By.xpath("(//div/button[text()='Next'])[2]"), ExtremeTimeout, "Failed in wait for Device Page to load"); //
		WaitForElementPresent(By.xpath("(//div/button[text()='Add to Cart'])[1]"), MainTimeout); //		
	}	
	
	public static void WaitForPageToLoadPortNumber() throws Exception
	{
		errMessage = "Failed wait in ChooseCarrierPage.WaitForPageToLoadPortNumber";		
		
		// wait for next button at bottom of page and wait for other items.
		WaitForElementClickable(By.xpath("(//div/button[text()='Next'])[2]"), ExtremeTimeout, errMessage); // the second 'next' button.
		WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout, errMessage); // first button
		WaitForElementPresent(By.cssSelector(".timing_span > a"), MainTimeout); // some text at the bottom of the page.		
	}			
	
	public static void WaitForPageToLoadUpgradeService() throws Exception
	{
		errMessage = "Failed wait in ChooseDevicePage.WaitForPageToLoadUpgradeDevice";		
		
		// wait for next button at bottom of page and wait for button #3.
		WaitForElementClickable(By.xpath("(//div/button[text()='Next'])[2]"), ExtremeTimeout, "Failed in wait for Device Page to load"); //
		WaitForElementClickable(By.cssSelector("#existing-manufacturer"), ExtremeTimeout, "Failed in wait for Device Page to load"); //
	}	
	
	// *** Methods commented out since they are not used *** 
			/*
	 // Returns Postal Code TextBox element
		public static WebElement nextButton() 
		{
			element = driver.findElement(By.cssSelector(".tg-button.tg-button--primary.ng-binding.ng-scope"));
			return element;
		}
	 
		// ErrorNoPostalCode
		public static WebElement errorNoDeviceSelected() throws Exception 
		{
			element = driver.findElement(By.xpath("//div//ul//li"));
			return element;
		}
		
		// Returns Samsung Galaxy S4 (16GB) - BlackMist in Shopping Cart Element
		public static WebElement device1InCart(WebDriver driver) {
			element = driver.findElement(By.xpath("//div[@class='tg-width--one-whole']"));
			return element;
		}
		//Nokia Lumina 822
		public static WebElement nokiaLumina882(WebDriver driver){
			element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[9]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div/div[1]"));
			return element;
		}*/

	// it is expected that there is no device added to cart. previously one was added, verified as added to cart, them removed from the cart.		
	public static void VerifyDevice1RemoveFromCartButtonTextChanged() throws Exception 
	{
		// wait for the last button on page.
		WaitForElementPresent(By.xpath("(//button[text()='Add to Cart'])[" + numAddCartButtons + "]"), ShortTimeout); 
		
		// now check that all the buttons have 'Add to Cart' text.
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() == numAddCartButtons, "Expected to have no devices added to cart in 'ChooseDevicePage.VerifyDevice1RemoveFromCartButtonTextChanged'.");

		// verify no device text found in shopping cart.
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath("//div[text()='Device']"), MiniTimeout), 
		        "Device text was found where shopping cart is supposed to be empty. Method is 'ChooseDevicePage.VerifyDevice1RemoveFromCartButtonTextChanged()'");
	}
	
	
	// #1) verify there are (numAddCartButtons - 1) buttons with "Add to Cart" text.
	// #2) verify there is only one button with "Remove from Cart" text.
	// #3) verify the device name above the button that says "Remove from Cart" is the text of the device that was added to the cart
	public static void verifyRemoveDevice1FromCartButtonText() throws Exception 
	{
		int numButtonsWithAddToCart = numAddCartButtons - 1;
		int numItemsNotSelected = 1;		

		// verify #1 
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() == numButtonsWithAddToCart,
					"The number of 'Add To Cart' buttons found in 'ChooseDevicePage.verifyRemoveDevice1FromCartButtonText' method are incorrect.");
		
		// verify #2
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Remove from Cart']")).size() == numItemsNotSelected,
					"The number of 'Remove from Cart' buttons found in 'ChooseDevicePage.verifyRemoveDevice1FromCartButtonText' method are incorrect.");		
	
		// verify #3
		Assert.assertEquals(driver.findElement(By.xpath("//button[text()='Remove from Cart']/../../../preceding-sibling :: div[1]")).getText(),deviceTextForSelectedDevice,
					"Device name that goes with 'Remove from Cart' button is incorrect in ChooseDevicePage.verifyRemoveDevice1FromCartButtonText().");
	}
	
	
	// ** NOT USED ** TO BE REMOVED *** 9/25/17
	/*public static boolean verifyDevice2InShoppingCart() {
		
		try
		{
			driver.findElement(By.xpath("(//div/div[text()='Nokia Lumia 822'])[2]"));
			return true;
			
		}
		catch(NoSuchElementException e){
			return false;
		}
	}*/
	
	public static void VerifyNoDeviceSelected() throws Exception 
	{
		
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath(mainErrorMessage), MediumTimeout), 
				          "Error message appears to be missing in 'ChooseDevicePage.VerifyNoDeviceSelected.");
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath(mainErrorMessage), MediumTimeout), 
					       "Error message appears to be missing in 'ChooseDevicePage.VerifyNoDeviceSelected.");
	}

	// this verifies there is only one button with 'Remove from Cart', this is expected. If there is only one, then click it.
	public static void clickRemoveDevice1FromCartButton() 
	{
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Remove from Cart']")).size() == 1, "There should be only one button in ChooseDevicePage.VerifyNoDeviceSelected()");
		driver.findElement(By.xpath("//button[text()='Remove from Cart']")).click();
	}

	public static void clickNextButton()  
	{
		
		WaitForElementClickable(By.cssSelector(".tg-button.tg-button--primary.ng-binding.ng-scope"), MainTimeout, "Next button in method 'clickNextButton()' not found.");   
		//driver.findElement(By.cssSelector(".tg-button.tg-button--primary.ng-binding.ng-scope")).click();		
		
		try { // Try clicking Next button at the top 
			
			driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div/button[text()='Next']")).click();
			
		} catch (Exception e) {
			
			// Scroll down to make 'Next' button visible and click on it
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0, 2000)", "");		
			driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div/div/button[text()='Next']")).click();
		}
		
	}

	public static void SelectFirstDevice() throws Exception 
	{
		// WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout, "");
		if(!WaitForElementClickableBoolean(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout))
		{
			Assert.fail("No device found in device list in method 'SelectFirstDevice()'");
		}
		
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();
		WaitForElementVisible(By.cssSelector(".tg-float--left"), MediumTimeout);
	}

	
	public static void getExistingDeviceInfo() {
		
		String existingDeviceManufacturer = new Select(driver.findElement(By.id("existing-manufacturer"))).getFirstSelectedOption().getText().trim();
		String existingDeviceModel = new Select(driver.findElement(By.id("existing-model"))).getFirstSelectedOption().getText().trim();
		String existingDeviceSerialNumber = driver.findElement(By.id("existing-serial-number")).getText().trim();
		String existingDeviceSerialNumberType = new Select(driver.findElement(By.id("existing-serial-number-type"))).getFirstSelectedOption().getText().trim();
		
		IdentifyDevices.oldManufacturer = existingDeviceManufacturer;
		IdentifyDevices.oldModel = existingDeviceModel;
		IdentifyDevices.oldSerialNumber = existingDeviceSerialNumber;
		IdentifyDevices.oldSerialNumberType = existingDeviceSerialNumberType;
		
	}


}
