package ServiceNow;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.AccessoriesDetailsShopCart;
import HelperObjects.ShoppingCart;

import java.util.ArrayList;
import java.util.List;


public class ChooseAccessoriesPage extends BaseClass
{
	
	public static WebElement element = null;
	
	public static ArrayList<String> accessoryList = new ArrayList<String>();
	public static ArrayList<String> accessoryVendorPriceList = new ArrayList<String>();	
	public static String AccessoryListItemNotFound  = "Expected accessory list item is not found.";
	
	// this list holds objects of accessories that are found in the shopping cart. each object has name, vendor, and cost. 
	// this list is the actual values. it is populated as items are added to the shopping cart and is used for actual values.
	public static ArrayList<AccessoriesDetailsShopCart> accessoriesDetailsListActual = new ArrayList<AccessoriesDetailsShopCart>();	
	
	public static boolean waitForPageToLoadAccessories() throws Exception
	{
		// try - catch added by Ana 9/11/17
				
		WaitForElementVisible(By.cssSelector("span.sn-flow__heading.ng-binding"), MediumTimeout);
		
		try {
			
			WaitForElementVisible(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout);
			return true;
			
		} catch (Exception e) {
			
			// If there are no accessories for the selected device, a message stating 'No Accessories Found' will be displayed
						
			WaitForElementVisible(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message"), MediumTimeout);
			String message = driver.findElement(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message")).getText();
			
			Assert.assertTrue(message.equals("No Accessories Found"));
			
			return false;
		}
		
	}
	
	// *** NOT USED -- TO BE REMOVED *** 
	/*
	public static void WaitForPageToOpenThreeItems() throws Exception
	{
		WaitForElementVisible(By.xpath("//div[text()='Select one or more accessories below to add to your shopping cart.']"),MainTimeout );
		
		// assuming there are many accessories for the specific device selected, look for the fifth button. use the large timeout value because this page can load slow.
		WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[3]"), ExtremeTimeout, "failed in accessories page 'WaitForPageToOpen()'. Expected clickable button never showed up.");
	}
	
	
	// *** NOT USED -- TO BE REMOVED *** 
	public static void WaitForPageToOpenNoItems() throws Exception
	{
		WaitForElementVisible(By.xpath("//span[text()='No Accessories Found']"), MainTimeout);
		
		// assuming there are many accessories for the specific device selected, look for the fifth button. use the large timeout value because this page can load slow.
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ExtremeTimeout, "failed in accessories page 'WaitForPageToOpen()'. Expected clickable button never showed up.");
	}	
	
	
	// *** NOT USED -- TO BE REMOVED *** 
	// this stores cost monthly for port number. the cost for port number is the cost of the selected device.
	public static void StoreCostMonthlyPortNumber() throws Exception
	{
		PlanInfoActions.portCostMonthly = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();
		// css=.tg-display--table.sn-cart__total.tg-width--one-whole.sn-cart__section--fade.ng-scope:nth-of-type(1) > span:nth-of-type(2) // this uses index twice
	}	
	*/
	
	public static void clickBackBtn()
	{
		WaitForElementClickable(By.xpath("(//button[text()='Back'])[1]"), ShortTimeout, "Back button not found in method 'WaitForPageToOpen()'");
		driver.findElement(By.xpath("(//button[text()='Back'])[1]")).click();
	}

	public static void clickNextBtn()
	{
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ShortTimeout, "Next button not found in method 'WaitForPageToOpen()'");
		driver.findElement(By.xpath("(//button[text()='Next'])[1]")).click();
	}
	
	// put the accessories names on a list.  
	public static void loadAvailableAccessoriesIntoExpectedList() throws Exception
	{
		List<WebElement> nameElementList  = new ArrayList<WebElement>(); 
		List<WebElement> priceElementList = new ArrayList<WebElement>();	
		List<WebElement> vendorElementList = new ArrayList<WebElement>();	
		
		
		// make sure starting with empty lists.
		accessoriesDetailsListExpected.clear(); 
		accessoryList.clear();
		
		WaitForElementClickable(By.xpath("//button[text()='Add to Cart']"), MainTimeout, "Button 'Add to Cart' not clickable.");
		
		nameElementList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));

		for(WebElement name : nameElementList)
		{		
			accessoryList.add(name.getText());
		}
		
		priceElementList =  driver.findElements(By.cssSelector(".tg-valign--top.ng-binding"));

		for(WebElement price : priceElementList)
		{		
			accessoryVendorPriceList.add(price.getText());
		}
		
		List<String> vendorNamesList = new ArrayList<>();
		vendorElementList = driver.findElements(By.xpath("//td[@class='tg-valign--top']/label[text()='Vendor']/../following-sibling::td"));
		
		for(WebElement vendor: vendorElementList)
		{		
			vendorNamesList.add(vendor.getText());
		}
		
		for(int x = 0, y = 0; x < accessoryList.size(); x ++, y += 2)
		{
			accessoriesDetailsListExpected.add(new AccessoriesDetailsExpected(accessoryList.get(x), vendorNamesList.get(x), accessoryVendorPriceList.get(y+1)));
			//accessoriesDetailsListExpected.add(new AccessoriesDetailsExpected(accessoryList.get(x), accessoryVendorPriceList.get(y), accessoryVendorPriceList.get(y+1))); // OLD -- see if replacement works
			System.out.println("accessory name: " + accessoriesDetailsListExpected.get(x).name);
		}
		
		//System.out.println("accessoriesDetailsListExpected size: " + accessoriesDetailsListExpected.size());
		
	}
	
	
	public static void showAllAccessories() throws Exception
	{
		for(AccessoriesDetailsExpected accessDetails : accessoriesDetailsListExpected)
		{
			accessDetails.Show();
		}
	}

	// this assumes the accessory page has been opened.
	public static void VerifyAddRemoveItemsInAccessories() throws Exception
	{
		String [] strArray;
		
		String xpathForCostInCart = "//span[text()='Cost']/following-sibling ::span[1]";

		// get the cost before adding accessory items. the cost is shown at the bottom of the shopping cart. this removes the leading dollar sign and any commas.
		String cost = driver.findElement(By.xpath(xpathForCostInCart)).getText().substring(1).replace(",", "");
		
		// make sure there is no accessories section before anything is selected. 
		WaitForElements(By.xpath(shopCartCount), MiniTimeout, 4);
		
		// get number of buttons on the page and verify the size of the list expected values list created previously is the same.
		int numButtons = driver.findElements(By.xpath("//button[text()='Add to Cart']")).size();
		Assert.assertTrue(numButtons == accessoriesDetailsListExpected.size(), 
				    "List named 'accessoriesDetailsList size should equal number of checkboxes in Chose Accessories page. Method is 'VerifyAddRemoveItemsInAccessories()'");
		
		VerifyButtonStates(numButtons, 0);

		// click each 'Add to Cart' button starting from the top button. this will add items to the cart.
		for(int x = 0; x < numButtons; x++) 
		{
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();

			// verify button statuses.
			VerifyButtonStates(numButtons, x + 1);			
			
			// below waits for the correct count of items in the shopping cart section Plan Features.
			Assert.assertTrue(WaitForElements(By.xpath(shopCartAccessories + "/div"), MiniTimeout, x + 1), 
					"Expected number of elements not found in ChooseAccessoriesPage.VerifyAddRemoveItemsInAccessories.");
			
			// get the last accessory added to the shopping cart and add it to the list of accessories on the 
			// accessoriesDetailsListActual list.
			strArray =  driver.findElement(By.xpath(shopCartAccessories + "/div[" + (x + 1) + "]")).getText().split("\n");		
			accessoriesDetailsListActual.add(new AccessoriesDetailsShopCart(strArray[0], strArray[1]));

			// this will go through the items that have been added to the accessoriesDetailsListActual list and compare 
			// the items' values to the corresponding expected values. 
			VerifyShoppingCartValuesAdd(); 
			
			// this calculates the expected value for the "cost" that is at the bottom of the shopping cart.
			cost = GetNewTotal(cost, accessoriesDetailsListExpected.get(x).GetCostNoDollarSignNoComma(), Action.Add);
			
			// compare the actual value for "Cost" at the bottom of the shopping cart to the expected value.
			Assert.assertEquals(driver.findElement(By.xpath(xpathForCostInCart)).getText(), CostMonthlyCalculatedConvertToFullText(cost),
					"The actual value for Cost in Choose Accessories page does not match the expected value. The method is  'VerifyAddRemoveItemsInAccessories'.");			
		}
		
		// verify all buttons are selected. this will remove items from the cart.
		VerifyButtonStates(numButtons, numButtons);

		
		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// NOTE: the section below could be cleaned up in the future. will take more time. calls to driver.findelements,
		// when there are no elements, takes many seconds to finish   
		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// click each 'Remove from Cart' button starting from the top button. 
		for(int x = numButtons, y = 0; x > 0; x--, y ++)
		{
			driver.findElement(By.xpath("(//button[text()='Remove from Cart'])[1]")).click();
			
			// this calculates the expected value for the "cost" that is at the bottom of the shopping cart.
			cost = GetNewTotal(cost, accessoriesDetailsListExpected.get(y).GetCostNoDollarSign().replace(",",""), Action.Remove);
			
			// compare the actual value for "Cost" at the bottom of the shopping cart to the expected value.
			Assert.assertEquals(driver.findElement(By.xpath(xpathForCostInCart)).getText().substring(1).replace(",", ""), cost,
					"The actual value for Cost in Choose Accessories page does not match the expected value. The method is  'VerifyAddRemoveItemsInAccessories'.");
			
			if((x - 1) != 0) // the WaitForElements method below takes a long time when it receives a zero. For now, pausing to allow last item oin shopping cart to go away.
			{
				// below waits for the correct count of items in the shopping cart section Plan Features.
				Assert.assertTrue(WaitForElements(By.xpath(shopCartAccessories + "/div"), MiniTimeout, x - 1), 
						"Expected number of elements not found in ChooseAccessoriesPage.VerifyAddRemoveItemsInAccessories.");
				
			}
			else // no elements to be removed. just wait for shopping cart to catch up.
			{
				Thread.sleep(1000);
			}
			
			// verify button statuses.
			VerifyButtonStates(numButtons, x - 1);
			
			// remove item from actual list that was removed with button click above.
			accessoriesDetailsListActual.remove(0);

			// method 'VerifyShoppingCartValuesRemove' uses a driver.findelements to get how many elements are in the accessories section in the shopping cart.
			// if there are no accessories, the driver.findelements call takes many seconds. if (x - 1) is zero, just verify there are no items in the 
			// accessoriesDetailsListActual list. this doesn't check that there are zero accessories in the shopping cart. more work will be needed to come up with a way to do this. 
			if((x - 1) != 0)  
			{
				// verify the shopping cart values as they are removed using the actual list that was verified in the section above.
				VerifyShoppingCartValuesRemove();
			}
			else
			{
				Assert.assertTrue(accessoriesDetailsListActual.size() == 0);
			}

			// compare the actual value for "Cost" at the bottom of the shopping cart to the expected value.
			Assert.assertEquals(driver.findElement(By.xpath(xpathForCostInCart)).getText(), CostMonthlyCalculatedConvertToFullText(cost),
					"The actual value for Cost in Choose Accessories page does not match the expected value. The method is  'VerifyAddRemoveItemsInAccessories'.");
		}
		
		// verify all buttons are not selected.
		VerifyButtonStates(numButtons, 0);

	}	

	public static void VerifyDeviceAndPlanCorrect()
	{
		VerifyDeviceAndPlanSections();
	}

	// this makes the desired selections to be used and calculates the expected value for "cost" in the bottom of the shopping cart.
	// when accessories are added they add to the initial cost of the device. 
	public static void addAccessoriesToShoppingCart(boolean orderAccessoriesOrder) throws Exception
	{
				
		WaitForElementClickable(By.xpath("//button[text()='Add to Cart']"), MainTimeout, "Button 'Add to Cart' not clickable.");
		
		String initialCost;
		
		// If the order type is 'Order Accessories'
		if (orderAccessoriesOrder) {
			
			initialCost = "0";
			
		} else { // Else, if order type is 'New Activation' | 'Upgrade Device'
			
			initialCost = ShoppingCart.costOneTime.replace("$", "");   // deviceInfoActions.DeviceCostRemoveDollarSign();
		}
				
		// get the number of add to cart buttons. this is needed to index into the accessoriesDetailsListExpected list.
		int lastButtonIndex = driver.findElements(By.xpath("//button[text()='Add to Cart']")).size(); 
		//System.out.println("amount of accessories: " + lastButton);
		
		// click the first add to cart button.
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click(); // add first accessory on page.
		
		// add 1st accessory to HashMap
		accessoriesInCartHashMap.put(accessoriesDetailsListExpected.get(0).name, accessoriesDetailsListExpected.get(0));
				
		// add the initial cost to the cost of the first accessory added to the cart.
		String finalCost = GetNewTotal(initialCost, accessoriesDetailsListExpected.get(0).GetCostNoDollarSignNoComma(), Action.Add);
		
		if (lastButtonIndex > 1) { // added by Ana 9/4/17
		
			// click the last button. the last button location is one less because the first button was clicked.
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (lastButtonIndex - 1) + "]")).click();		

			// add last accessory to HashMap
			accessoriesInCartHashMap.put(accessoriesDetailsListExpected.get(lastButtonIndex - 1).name, accessoriesDetailsListExpected.get(lastButtonIndex - 1));
						
			// get the final cost by adding the last item on the accessoriesDetailsListExpected list to the total.
			finalCost = GetNewTotal(finalCost, accessoriesDetailsListExpected.get(lastButtonIndex - 1).GetCostNoDollarSignNoComma(), Action.Add);

		}
		
		// store away the final value for cost, at the bottom of the shopping cart, to be used in the future.
		
		// replaced by cost in ShoppingCart -- see line below
		//AccessoriesDetailsExpected.finalCost = finalCost;
		ShoppingCart.costOneTime = finalCost;
		
	}
	

	/* // added by Ana  // ** TO BE REMOVED *** 
	public static void makeDesiredSelectionsOrderAccessories() throws Exception
	{
		
		accessoriesInCartHashMap = new HashMap<String, AccessoriesDetailsExpected>();
		
		WaitForElementClickable(By.xpath("//button[text()='Add to Cart']"), MainTimeout, "Button 'Add to Cart' not clickable.");
		
		String initialCost = "0"; 
		System.out.println("initialCost: " + initialCost);
		
		// get the number of add to cart buttons. this is needed to index into the accessoriesDetailsListExpected list.
		int lastIndex = driver.findElements(By.xpath("//button[text()='Add to Cart']")).size(); 
		System.out.println("amount of accessories: " + lastIndex);
		
		// click the first add to cart button.
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click(); // add first accessory on page.
		
		// add 1st accessory to HashMap
		accessoriesInCartHashMap.put(accessoriesDetailsListExpected.get(0).name, accessoriesDetailsListExpected.get(0));
		
		// add the initial cost to the cost of the first accessory added to the cart.
		String finalCost = GetNewTotal(initialCost, accessoriesDetailsListExpected.get(0).GetCostNoDollarSignNoComma(), Action.Add);
		
		if (lastIndex > 1) { // added by Ana 9/4/17 - If there's more than one accessory available, add the last one listed to the shopping cart
		
			// click the last button. the last button location is one less because the first button was clicked.
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (lastIndex - 1) + "]")).click();		

			// add last accessory to HashMap
			accessoriesInCartHashMap.put(accessoriesDetailsListExpected.get(0).name, accessoriesDetailsListExpected.get(lastIndex - 1));
			
			// get the final cost by adding the last item on the accessoriesDetailsListExpected list to the total.
			finalCost = GetNewTotal(finalCost, accessoriesDetailsListExpected.get(lastIndex - 1).GetCostNoDollarSignNoComma(), Action.Add);

		}
		
		// store away the final value for cost, at the bottom of the shopping cart, to be used in the future.
		AccessoriesDetailsExpected.finalCost = CostMonthlyCalculatedConvertToFullText(finalCost);
	}*/

	
	
	
	// *** NOT USED -- TO BE REMOVED???? ***
	// this selects the first, third, and fifth accessory. 
	/*public static void makeDesiredSelectionsOrderAccessoriesAction() throws Exception
	{
		int maxRequiredSelections = 5;
		
		// first make sure there are five or more accessories. there should be because the tests are always run with the same device and service.  
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() >= maxRequiredSelections);
		
		// click the first add to cart button per the AccessoriesDetailsExpected index one.
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (AccessoriesDetailsExpected.orderAccessoriesActionSelectionOneArrayIndex + 1) + "]")).click(); // add first accessory on page.
		
		// click the first add to cart button per the AccessoriesDetailsExpected index one.
		// notice the element selected is one less because after selecting the first element the indexing for the non-selected buttons is reduced by one. 
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + (AccessoriesDetailsExpected.orderAccessoriesActionSelectionTwoArrayIndex) + "]")).click(); // add fifth accessory on page.
		
		// store cost
		AccessoriesDetailsExpected.finalCost = driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
	}
	
	
	// *** NOT USED -- TO BE REMOVED???? ***
	// this selects the last accessory and stores the cost and cost monthly 
	public static void MakeDesiredSelectionsUpgradeDeviceActionAndStoreCosts() throws Exception
	{
		int maxRequiredSelections = 1;
		
		// first make sure there are five or more accessories. there should be because the tests are always run with the same device and service.  
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() >= maxRequiredSelections);
		
		// click the last add to cart button per the AccessoriesDetailsExpected index.
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[" + accessoriesDetailsListExpected.size() + "]")).click(); // add last accessory on page.
		
		// store cost and monthly cost.
		AccessoriesDetailsExpected.finalCost = driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
		AccessoriesDetailsExpected.finalCostMonthly = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();		
	}
	
	// *** USED BY A NON USED METHOD.... -- TO BE REMOVED???? ***
	//Add Item 1 to cart
	public static WebElement addToCartBtnItem1(){
		element = driver.findElement(By.xpath("//div[2]/div[9]/div[1]/div[2]/div/div[1]/div[2]/div/div[2]/div/div/button"));
		return element;
	}
	
	// *** NOT USED -- TO BE REMOVED???? ***
	//Next Button
	public static WebElement nextButton() {
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[4]/button[3]"));
		return element;
	}
	
	
	// *** NOT USED -- TO BE REMOVED???? ***
	//Verify Price change by adding and removing accessory to and from shopping cart.
	public static void verifyCostChange(){
		String startingCost = driver.findElement(By.xpath("//div//span[contains(text(),'$149.99')]")).getText();
		clickAddToCartBtnItem1();
		String newCost = driver.findElement(By.xpath("//div//span[contains(text(),'$172.48')]")).getText();
		boolean costChange;	
		if (startingCost != newCost){
				costChange = true;
			}
		else{
			costChange = false;
		}
		Assert.assertEquals(costChange, true);
	}
	
	public static void clickAddToCartBtnItem1(){
		element = addToCartBtnItem1();
		element.click();
	}*/
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                       helper methods 
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	
	public static void VerifyShoppingCartValuesAdd()
	{
		for(int x = 0; x < accessoriesDetailsListActual.size(); x++)
		{
			Assert.assertEquals(accessoriesDetailsListActual.get(x).name, accessoriesDetailsListExpected.get(x).name, 
					"failed compare when comparing expected accessory name to actual name in  ChooseAccesoriesPage.VerifyShoppingCartValues");
			Assert.assertEquals(accessoriesDetailsListActual.get(x).cost, accessoriesDetailsListExpected.get(x).cost,
					"failed compare when comparing expected accessory cost to actual cost in ChooseAccesoriesPage.VerifyShoppingCartValues");			
		}
	}
	
	// this compares the actual values being deleted to the actual values that were verified in the accessoriesDetailsListActual list.
	// the actual values in the accessoriesDetailsListActual list were verified when the shopping cart values were added.
	// before each call to this method the accessoriesDetailsListActual list has the first element removed when an item is 
	// removed from the top of the shopping cart list.
	public static void VerifyShoppingCartValuesRemove() throws Exception
	{
		for (int x = 0; x < driver.findElements(By.xpath(shopCartAccessories + "/div")).size(); x++)
		{
			String [] strArray = driver.findElement(By.xpath("(" + shopCartAccessories + "/div)[" + (x + 1) + "]")).getText().split("\n");
			Assert.assertEquals(accessoriesDetailsListActual.get(x).name.trim(),strArray[0].trim(),"");
			Assert.assertEquals(accessoriesDetailsListActual.get(x).fullCostString.trim(),strArray[1].trim(),"");			
		}
	}
	
	// this verifies the correct number of 'Remove from Cart' and 'Add to Cart' buttons. 
	public static void VerifyButtonStates(int numButtons, int numSelected) throws Exception
	{
		// if no buttons selected, don't do this check because it takes many seconds to complete. the other check verifies no buttons are selected.
		if(numSelected != 0) 
		{
			Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Remove from Cart']")).size() == numSelected, 
					"Number of 'Remove from Cart' buttons is incorrect in 'ChooseAccesoriesPage.VerifyButtonStatesAddingItems'.");
		}
		
		// if all buttons selected, don't do this check because it takes many seconds to complete. the other check verifies all buttons are selected.		
		if(numSelected != numButtons)  
		{
			Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to Cart']")).size() == numButtons - numSelected, 
					"Number of 'Add to Cart' buttons is incorrect in 'ChooseAccesoriesPage.VerifyButtonStatesAddingItems'.");
		}
	}
	
	
	
	// Added by Ana - September 4, 2017
	// Accessories are optional. 
	// If there's at least one accessory, add it to shopping cart, 
	// else move to next step
	public static void addAccessoryToCart_Upgrade() throws Exception {

		// If there are accessories
		if (WaitForElementVisibleNoThrow(By.xpath("//div[text()='Select one or more accessories below to add to your shopping cart.']"), MainTimeout )) {
			
			// Add accessory to cart
			addAccessoriesToShoppingCart(false);
			//AccessoriesDetailsExpected.finalCost = driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
			//AccessoriesDetailsExpected.finalCostMonthly = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();		
			
		} else {
			
			// Wait for label 'No Accessories Found'
			WaitForElementVisibleNoThrow(By.xpath("//span[text()='No Accessories Found']"), MainTimeout);
			
		}
			
		// Click Next button	
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ExtremeTimeout, "failed in accessories page 'addAccessoryToCart()'. Expected clickable button never showed up.");
		clickNextBtn();	
		
		
	}
	
	// Added by Ana
	public static boolean addAccessoryToCart_OrderAccessories() throws Exception {

		// If there are accessories
		if (WaitForElementVisibleNoThrow(By.xpath("//div[text()='Select one or more accessories below to add to your shopping cart.']"), MainTimeout )) {
			
			// Add accessory to cart
			addAccessoriesToShoppingCart(true); 
			
			ShoppingCart.costOneTime = driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
			
			// AccessoriesDetailsExpected.finalCost = driver.findElement(By.xpath("//span[text()='Cost']/following ::span[1]")).getText();
			// AccessoriesDetailsExpected.finalCostMonthly = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();		
			
			// Click Next button	
			WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ExtremeTimeout, "failed in accessories page 'addAccessoryToCart()'. Expected clickable button never showed up.");
			clickNextBtn();	
			return true;
			
		} else {
			
			// Wait for label 'No Accessories Found'
			WaitForElementVisibleNoThrow(By.xpath("//span[text()='No Accessories Found']"), MainTimeout);
			return false;
		}
			
		
	}

	// ** SFD112968
	public static boolean addRemoveAccessoryFromCart() throws Exception {
		
		// If there are accessories
		if (WaitForElementVisibleNoThrow(By.xpath("//div[text()='Select one or more accessories below to add to your shopping cart.']"), MainTimeout )) {
			
			// Add accessory to cart
			WaitForElementClickable(By.xpath("//button[text()='Add to Cart']"), MainTimeout, "Button 'Add to Cart' not clickable.");
			
			String accessoryName = driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]/../../../preceding-sibling::div")).getText();
			
			System.out.println("** accessoryName:      " + accessoryName);
			
			// Click the first 'Add to Cart' button.
			driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();
			
			String accessoryNameInCart = driver.findElement(By.cssSelector("div.sn-cart__item")).getText();
			
			System.out.println("** accessoryNameInCart: " + accessoryNameInCart);
			
			// Verify the accessory has been added to cart
			Assert.assertEquals(accessoryNameInCart, accessoryName);
			
			
			// Remove the accessory added from cart 
			WaitForElementClickable(By.xpath("//button[text()='Remove from Cart']"), MainTimeout, "Button 'Remove from Cart' not clickable.");
			
			// Click 'Remove from Cart' button.
			driver.findElement(By.xpath("(//button[text()='Remove from Cart'])[1]")).click();
						
			// Verify that the shopping cart is empty
			try {
				
				driver.findElement(By.cssSelector("div.sn-cart__item"));
				
			} catch (NoSuchElementException e) { 
				
				System.out.println("** Shopping cart is empty --> OK **");
				
			}
			
			
			// Click Next button	
			WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), ExtremeTimeout, "failed in accessories page 'addAccessoryToCart()'. Expected clickable button never showed up.");
			clickNextBtn();	
			
			// Verify that user cannot move to 'Provide Additional Info' step if no accessory has been added to cart.
			String titleStepNameFound = driver.findElement(By.cssSelector("div.tg-infoBarCell>span")).getText();
			String titleStepNameExpected = "Choose your accessories.";
			
			// Verify that the current step is still 'Choose your accessories'
			Assert.assertEquals(titleStepNameFound, titleStepNameExpected);
			
			String titleStepNameNotExpected = "Provide additional information about your order.";
			
			Assert.assertNotEquals(titleStepNameFound, titleStepNameNotExpected);
			
			// Verify that there's an error message stating that at least one accessory needs to be added to cart
			String message = "Please select at least one accessory.";
			
			Assert.assertEquals(driver.findElement(By.cssSelector("div.sn-notifyBlock__header>span")).getText(), message);
			
			return true;
			
		} else {
			
			// Wait for label 'No Accessories Found'
			WaitForElementVisibleNoThrow(By.xpath("//span[text()='No Accessories Found']"), MainTimeout);
			return false;
			
		}
			
		
	}
	
	
	
	
	
	
}

