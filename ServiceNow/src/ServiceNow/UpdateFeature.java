package ServiceNow;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import HelperObjects.Feature;
import HelperObjects.FeatureShoppingCart;

public class UpdateFeature extends ActionsBase
{

	public static String [] strArray;
	public static String errMessage = "";
	public static String strTemp = "";
	public static String strTempTwo = "";
	
	static public void ClickNext() 
	{
		driver.findElement(By.xpath("(//button[text()= 'Next'])[1]")).click();		
	}
	
	static public void WaitForPageToLoad() throws Exception
	{
		WaitForElementVisible(By.xpath("//span[text()='Update your features.']"), MainTimeout);
		WaitForElementClickable(By.xpath("(//button[text()='NO CHANGE'])[11]"), MainTimeout, "Failed in wait for UpdateFeatures.WaitForPageToLoad.");		
	}
	
	// this verifies adding and completely removing  two features.
	static public void VerifyAddAndNoChangeSelections() throws Exception
	{
		errMessage = "Fail in UpdateFeature.VerifySelections";
		
		int noItem = 0;
		int oneItem = 1;
		int twoItem = 2;		
		
		// set this below the TinyTimeout time or it will override the TinyTimeout wait time.
		SetImplicitWait(1);
		
		// verify there are no items in shopping cart.
		Assert.assertTrue(WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TeenyTinyTimeout, noItem));
		
		// click add on first item, wait for the shopping cart item to show up and make sure there is only one.
		driver.findElement(By.xpath("(//button[text()='ADD'])[1]")).click();
		WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TinyTimeout, oneItem);

		// click add, wait for the shopping cart item to show up and make sure there are two.
		driver.findElement(By.xpath("(//button[text()='ADD'])[2]")).click();
		WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TinyTimeout, twoItem);
		
		// click 'no change', wait for the shopping cart item to show up and make sure there are one.
		driver.findElement(By.xpath("(//button[text()='NO CHANGE'])[2]")).click();
		WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TinyTimeout, oneItem); 
		
		// click 'no change' and make sure there is nothing in the shopping cart.
		driver.findElement(By.xpath("(//button[text()='NO CHANGE'])[1]")).click();
		Assert.assertTrue(WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TeenyTinyTimeout, noItem));		

		// return this to default.
		SetImplicitWait(5);

		// css=.sn-cart__heading.tg-space--half--top~div>div>div:first-of-type // one item in cart.
		// css=.sn-cart__heading.tg-space--half--top~div>div>div:nth-of-type // many items in cart.
	}
	
	
	// this adds maxItems (see variable below) to the shopping cart. after each is added there is a check to verify the number of items in the cart is correct.
	// then the added items are compared to the expected items list called featuresList including the cost monthly.	
	static public void VerifyAddFeatures() throws Exception
	{
		int noItemsInCart = 0; 

		featuresShoppingCartList.clear(); // starting from empty shopping cart.
		
		// set this below or it will override the TinyTimeout wait time.
		SetImplicitWait(1);
		
		// verify there are no items in shopping cart.
		Assert.assertTrue(WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TinyTimeout, noItemsInCart));
		
		for(int x = 0; x < FeatureShoppingCart.maxItemsToAdd; x++)
		{
			// click add on first item, wait for the shopping cart item to show up and make sure there is only one.
			driver.findElement(By.xpath("(//button[text()='ADD'])[" + (x + 1) + "]")).click();
			
			// verify items in cart.
			Assert.assertTrue(WaitForElements(By.cssSelector(".sn-cart__item.tg-pad--eighth--top.ng-binding"), TinyTimeout, x + 1));
			
			// get the information from the latest item added to the shopping cart and add to featuresShoppingCartList
			strArray = driver.findElement(By.cssSelector(".sn-cart__heading.tg-space--half--top ~ div > div > div:nth-child(" + (x + 1) + ")")).getText().split("\n");
			featuresShoppingCartList.add(new FeatureShoppingCart(strArray[0].replace("ADD:","").trim(), strArray[1].replace("Remove","").trim()));

			FeatureShoppingCart.VerifyAddedFeatures(); 
		}
		
		// set back to default.
		SetImplicitWait(5);
		
		// store away the full cost of the additions.
		costMonthlyAfterAddingFeatures = FeatureShoppingCart.costMonthly;
	}
	
	// this assumes FeatureShoppingCart.maxItemsToAdd number of items are in the shopping cart and they have been verified when they were added.
	// this goes through FeatureShoppingCart.maxItemsToAdd number of items in the update features page shopping cart and selects 'remove', one at a time.
	// after each selection is made, 'VerifyRemovedItemInShoppingCart' is called passing the string array of the item in the shopping cart that is
	// affected by the 'remove' selection. 'VerifyRemovedItemInShoppingCart' verifies expected results in the affected shopping cart item. 
	static public void VerifyRemoveActions() throws Exception 
	{
		for(int x = 0; x < FeatureShoppingCart.maxItemsToAdd; x++)
		{
			driver.findElement(By.xpath("(//button[text()='REMOVE'])[" + (x + 1) + "]")).click(); // click 'remove' button.
			
			if (x == FeatureShoppingCart.maxItemsToAdd) {
			
				// If the last item has been removed from shopping cart, 
				// wait for 'Cost Monthly' label and value to not be visible 
				// so there's no error --> added by Ana 9/7/17
				WaitForElementNotPresentNoThrow(By.xpath("//span[@text='Cost Monthly']"), MediumTimeout); 
				
			}						
			VerifyRemovedItemInShoppingCart(driver.findElement(By.cssSelector(".sn-cart__heading.tg-space--half--top ~ div > div > div:nth-child(" + (x + 1) + ")")).getText().split("\n"),	x);
		}
	}	

	// this gets an array of info that is one item in the shopping cart that has had the 'remove' button selected on it.
	// this uses the expected data in 'featuresList' list to verify the info in the shopping cart item. 
	static public void VerifyRemovedItemInShoppingCart(String [] strArray, int index) throws Exception
	{
		errMessage ="Failure in UpdateFeatures.VerifyRemovedItemInShoppingCart";
		
		//System.out.println("Cost monthly " + FeatureShoppingCart.costMonthly); // DEBUG
		//System.out.println("Save monthly " + FeatureShoppingCart.saveMonthly); // DEBUG
		
		Assert.assertEquals(strArray[0].split(":")[1].trim(), featuresList.get(index).fullName, errMessage);  // verify full name
		Assert.assertEquals(strArray[0].split(":")[0].trim(), FeatureShoppingCart.removeText, errMessage);	// verify leading text says "REMOVE"
	
		// NOTE: this is here because of DE# 108239
		if(!featuresShoppingCartList.get(index).costIsZero) // if the feature cost is not '$0.00', verify the dollar cost.
		{
			
			String valueFound = strArray[1].trim().split(" ")[2].trim();
			String valueExpected = featuresList.get(index).dollarCost; 
			
			if (!featuresList.get(index).dollarCost.equals("$0.00")) { 
			
				valueExpected = "-" + valueExpected; 
			}  
			
			Assert.assertEquals(valueFound, valueExpected, errMessage); // dollar cost should have a "-" in front of it.
			
			// calculate/verify shopping cart 'cost monthly'. 
			FeatureShoppingCart.costMonthly = GetNewTotal(FeatureShoppingCart.costMonthly, featuresList.get(index).decimalCost, Action.Remove);
		}
		
		// Calculate shopping cart 'save monthly'.
		FeatureShoppingCart.saveMonthly = GetNewTotal(FeatureShoppingCart.saveMonthly, featuresList.get(index).decimalCost, Action.Add);
		
		FeatureShoppingCart.FixZerovalues(); // this changes the format of FeatureShoppingCart 'saveMonthly' and 'costMonthly' when they are equal to '.00'.  
		
		// verify correct 'Save Monthly' value.
		Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Save Monthly']/following ::span[1]")).getText(), "$" + FeatureShoppingCart.saveMonthly, errMessage);
		
		SetImplicitWait(1);
		// NOTE: this is to compensate for defect  DE# 108242. have to ensure that the 'cost monthly' field hasn't gone away. 
		if(!WaitForElements(By.xpath("//span[text()='Cost Monthly']/following ::span[1]"), TeenyTinyTimeout, 0))
		{
			// verify correct 'Cost Monthly' value.
			Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText(), "$" + FeatureShoppingCart.costMonthly , errMessage);
		}
		SetImplicitWait(5);
	}

	// this sets up the 'update features' page to load items one, two, and five to the shopping cart. The first and fifth 
	// items are add actions and the second is remove.
	static public void SetupAddedFeaturesForNextPages(int numFeaturesToLoad) throws InterruptedException
	{
		Assert.assertTrue(numFeaturesToLoad >= 5);
		
		Feature.finalCost = "0.00";
		
		// first remove all items in the shopping cart.
		for(int x = 0; x < FeatureShoppingCart.maxItemsToAdd; x++)  // x < numFeaturesToLoad
		{
			driver.findElement(By.xpath("(//button[text()='NO CHANGE'])[" + (x + 1) + "]")).click();
		}

		// select item 1, 2, and 5. this will give two features that have a non-zero cost and one that has a zero cost.
		driver.findElement(By.xpath("(//button[text()='ADD'])[" + (Feature.selectionOne + 1) + "]")).click(); // item one (index 1).
		featuresList.get(Feature.selectionOne).SetActions(UpdateFeatureActions.add);

		driver.findElement(By.xpath("(//button[text()='REMOVE'])[" + (Feature.selectionTwo + 1) + "]")).click(); // item two (index 2).
		featuresList.get(Feature.selectionTwo).SetActions(UpdateFeatureActions.remove);	
		
		driver.findElement(By.xpath("(//button[text()='ADD'])[" + (Feature.selectionThree + 1) + "]")).click(); // item three  (index 5).
		featuresList.get(Feature.selectionThree).SetActions(UpdateFeatureActions.add);

		// store monthly cost
		FeatureShoppingCart.costMonthly =  driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();
		
		// calculate final cost and add '$' sign in front of it.
		Feature.finalCost = GetNewTotal(Feature.finalCost, featuresList.get(Feature.selectionOne).decimalCost, Action.Add);
		Feature.finalCost = GetNewTotal(Feature.finalCost, featuresList.get(Feature.selectionTwo).decimalCost, Action.Remove);
		Feature.finalCost = GetNewTotal(Feature.finalCost, featuresList.get(Feature.selectionThree).decimalCost, Action.Add);		

		if(Feature.finalCost.contains("-")) // if value is negative. need to setup with dollar sign.
		{
			Feature.finalCost = Feature.finalCost.replace("-" ,"-$");
		}
	}
	
	// create a list of features.  
	static public void LoadFeatures(int numFeaturesToLoad) throws Exception
	{
		featuresList.clear(); // list may have items from previous test.
		
		List<WebElement> wbListCost = driver.findElements(By.cssSelector(".sn-section-subheading.tg-space--quarter--ends.ng-binding"));
		List<WebElement> wbListname = driver.findElements(By.cssSelector(".sn-list-heading.ng-binding"));
		
		for(int x = 0; x < numFeaturesToLoad; x++)
		{
			strArray = wbListCost.get(x).getText().split("\n");
			featuresList.add(new Feature(wbListname.get(x).getText(), strArray[0].split(":")[1].trim()));	
		}
		// Feature.ShowList(); // DEBUG
	}
}
