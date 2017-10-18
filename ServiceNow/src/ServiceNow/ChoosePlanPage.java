package ServiceNow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import HelperObjects.FeatureShoppingCart;
import HelperObjects.PlanInfoActions;
import HelperObjects.PlanOptionalFeatures;

import org.openqa.selenium.JavascriptExecutor;

public class ChoosePlanPage extends BaseClass
{
	public static WebElement element = null;


	public enum Action 
	{
		Add,
		Remove
	}

	// TODO - add to web element return.
	static String xpathToFeaturesList = "//div[text()='Plan Features']/following-sibling:: div[1]/descendant ::div[@class='tg-space--eighth--bottom sn-cart__section--fade ng-scope']/descendant ::span[1]";
	
	static String xpathToFullList =	shopCartPlanFeatures;

	
	// these are expected error  messages. 
	static String PlanNeedsSelectedErrorOne = "Please fix the following validation errors:";
	static String PlanNeedsSelectedErrorTwo = "Please select a Plan";

	static String noOptionalFeatures = "No optional features have been selected";
	
	// these are string values that are found in the 'Plan Features' section for cost and cost monthly. 
	//static String planFeatureCost ="";
	//static String planFeatureCostMonthly ="";	

	// these are string values that are found in the 'Device' and 'Plan' section for cost and cost monthly. 
	static String deviceCost ="";
	static String planCostMonthly ="";	
	
	static float planMonthlyCostFloat = 0;
	// static float planFeaturesMonthlyCost = 0;	
	
	// this is for storing the name of the first plan in the list of plans. this is done in StoreNameOfFirstPlanInList().
	static String planNameFirstInList  = "";

	static String planSelectionText = "Select a plan below to add to your shopping cart."; // string that's used in Xpath.
	
	static String totalString = ""; // this is used when testing plan page optional features.
	

	static Map<String, String> mapOptionalFeatures = new LinkedHashMap<String, String>(); // this will hold the plan name as key and monthly cost as the key value.	
	
	static String [] includedFeatures; 
	
	
	// store away names and monthly costs of all optional features listed on page.
	// store away the initial cost and CostMonthly in plan features section.
	// store away the initial cost and CostMonthly in device and plan section..
	// ******** COMMENTED SINCE IT IS NOT USED *************
	/*public static void StoreOptionalFeaturesNamesAndInfo() throws Exception
	{
		// List<String> optionInfoList = new ArrayList<>();
		List<WebElement> webElementList = driver.findElements(By.xpath("//li/label")); // this will hold all of the optional features text information.
 		
		String [] strTempArray; 
		
		// go through each optional feature element and get the associated text.
		for(WebElement ele : webElementList) 
		{
			strTempArray = SetupTwoElementStringArray(ele.getText(), "-");
			mapOptionalFeatures.put(strTempArray[0], strTempArray[1]);
		}

		// DEBUG
		//for (String key : mapOptionalFeatures.keySet()){System.out.println("Key:" + key + " Value:" + mapOptionalFeatures.get(key));} // debug.    					

	}*/
	
	// this gets the list of "Optional Features" and stores the text for each optional feature into a PlanOptionalFeatures object that is put on a list. 
	public static boolean storeOptionalFeaturesNamesAndInfoNew() throws Exception
	{
		optionalFeaturesList.clear();
		
		List<WebElement> featuresList = driver.findElements(By.xpath("//li/label")); // this will hold all of the optional features text information.
		
		// go through each optional feature element and end the associated text for each object.
		for(WebElement feature : featuresList) 
		{
			optionalFeaturesList.add(new PlanOptionalFeatures(feature.getText()));
			
		}
		
		// PlanOptionalFeatures.ShowOptionalFeaturesList(); // DEBUG
		
		if (optionalFeaturesList.size() > 0) {
			return true;
		} else {
			return false;
		}
		
		
	}
		
	// 1) this verifies the device/plan names and cost are correct per the previous values that were stored off when the device and plan were added.
	// 2) this verifies the cost and cost monthly values at the bottom of the shopping cart are correct per the previous values that were stored off 
	//    when the device and plan were added.
	public static void VerifyStartingShoppingCartValuesAlign() throws Exception
	{
		WaitForElementVisible(By.xpath("//h3[text()='Optional Features']"), MainTimeout);
		
		// get the text for cost and cost monthly in plan features at the very bottom of the shopping cart
		// these are considered the actual values. 
		String planFeatureCost = driver.findElement(By.xpath("//span[text()='Cost']/following-sibling::span[1]")).getText();
		String planFeatureCostMonthly = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following-sibling::span[1]")).getText();
		
		VerifyDeviceAndPlanSections();
		
		// now compare the actual values in plan features section with the stored off values.
		Assert.assertEquals(planFeatureCost, deviceInfoActions.cost, "Error in plan page 'StoreOptionalFeaturesNamesAndInfo()'. Cost value doesn't match between plan features and device cost.");
		Assert.assertEquals(planFeatureCostMonthly, planInfoActions.PlanTextCost(), "Error in plan page 'StoreOptionalFeaturesNamesAndInfo()'. Cost monthly value doesn't match between plan features and device cost.");
		
		// verify no optional features have been added.
		// Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//div[text()='Plan Features']//following-sibling ::div[1]/descendant ::div[text()='" + noOptionalFeatures +"']"), MediumTimeout));		
		
		boolean planHasOptionalFeatures = false;
		
		// If the 'Plan Features' section is displayed in Shopping cart, verify that there's a legend below indicating that no optional features have been selected. 
		try {
			driver.findElement(By.xpath("//div[text()='Plan Features']"));
			planHasOptionalFeatures = true;
			
		} catch (Exception e) { }
		
		if (planHasOptionalFeatures) {
			Assert.assertEquals(driver.findElement(By.xpath("//div[@data-qa='Plan Features']")).getText(), noOptionalFeatures, "");
		}
		
	}

	// this adds and removes all of the Optional Features listed on the plan page. it verifies what's in the shopping cart for each check-box (checked and un-checked). 
	// it also verifies the 'Cost Monthly' value as the check boxes are selected and not selected. adding starts from the top and removing starts from the bottom.   
	public static void VerifyAddRemoveCostMonthly() throws Exception
	{
		// get number of check boxes on the page and verify the size of the list created previously is the same.
		int numCheckboxes = driver.findElements(By.xpath("//h3[text()='Optional Features']/following-sibling ::ul/li")).size();
		Assert.assertTrue(numCheckboxes == optionalFeaturesList.size(), "Map named 'optionalFeaturesList' size should equal number of checkboxes in plan page Optional Features. Method is 'VerifyAddRemoveCostMonthly()'");

		// verify no optional features have been added at this point by looking for 'No optional features have been selected' text.
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//div[text()='Plan Features']//following-sibling ::div[1]/descendant ::div[text()='" + noOptionalFeatures +"']"), MediumTimeout));

		// get the cost that was saved off in the PlanInfoActions object. the complete field was saved off in the object so call the method that returns the cost only, i.e. "34.76".
		// this value was saved off when the plan was first added.
		String totalString = planInfoActions.PlanDecimalCost();		
		
		// select each check box, one at a time starting from the top and, 
		// verify the optional features in the cart using the stored away values  in optionalFeaturesList. 
		// verify the 'Cost Monthly" value at bottom of plan features is correct on each 
		for(int x = 0; x < numCheckboxes; x++)
		{
			driver.findElement(By.xpath("(//label[@class='sn-callout__label ng-binding']/input)[" + (x + 1) + "]")).click();	// click the indexed check box.			
			
			// below waits for the correct count of items in the shopping cart section Plan Features.
			Assert.assertTrue(WaitForElements(By.xpath("(//div[@class='sn-cart__section'])[3]/div/div"), MiniTimeout, x + 1), 
					"Expected number of elements not found in ChoosePlanPage.VerifyAddRemoveCostMonthly. ");		
			
			// send all of the text under the Plan Features section to this method. it verifies the items under plan features are correct per the stored away items in optionalFeaturesList.
			VerifyItemsInCartAdd(driver.findElement(By.xpath(shopCartPlanFeatures + "/div")).getText()); // verify shopping cart value(s) (actual) to stored value(s) (expected).
			
			// add the cost of the just-added optional feature to the total cost and return string of total. this is the expected   
			totalString = GetNewTotal(totalString, optionalFeaturesList.get(x).CostWithNoDollarSignNoComma(), Action.Add); 

			// verify the actual "cost monthly" value at the bottom of the shopping cart is correct per the expected (calculated) value.
			double actualCostMonthly = Double.parseDouble(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText().replace("$", "").replace(",", ""));
			double expectedCostMonthly = Double.parseDouble(CostMonthlyCalculatedConvertToFullText(totalString).replace("$", "").replace(",", ""));
			
			FeatureShoppingCart.verifyCostMonthly(actualCostMonthly, expectedCostMonthly);
			
			//Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText(), 
				//	CostMonthlyCalculatedConvertToFullText(totalString), "Cost Monthly value in shopping cart is not correct in ChoosePlanPage.MakeDesiredSelections");
			
			// make sure the cost at the bottom of the shopping cart remains at the device cost. 
			Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost']/following-sibling::span[1]")).getText(), deviceInfoActions.cost);

		}


		
		// un-check each check box, one at a time starting from the top of check boxes, and verify the optional features in the shopping cart, using the stored away values in optionalFeaturesList.
		for(int x = numCheckboxes; x > 0; x--)
		{
			driver.findElement(By.xpath("//h3[text()='Optional Features']/following-sibling ::ul/li[" + x + "]/label/input")).click();	// click the indexed check box. 
			
			// when x is 1, the button click made to the last check box showing at the top of the optional features list has been checked.  
			// with the last check box checked there will be 'No optional features have been selected' text shown in the "Plan Features" section.
			// do the else section.
			if(x != 1)    
			{	
				// below waits for the correct count of items in the shopping cart section Plan Features.
				Assert.assertTrue(WaitForElements(By.xpath("(//div[@class='sn-cart__section'])[3]/div/div"), MiniTimeout, x - 1), 
						"Expected number of elements not found in ChoosePlanPage.VerifyAddRemoveCostMonthly. ");		
	
				// send all of the text under the Plan Features section to this method. it verifies the items under plan features are correct per the stored away items in optionalFeaturesList.
				VerifyItemsInCartRemove(driver.findElement(By.xpath(shopCartPlanFeatures + "/div")).getText()); // verify shopping cart value(s) (actual) to stored value(s) (expected).
				
				totalString = GetNewTotal(totalString, optionalFeaturesList.get(x - 1).CostWithNoDollarSignNoComma(), Action.Remove);
				// DebugTimeout(0, totalString); // DEBUG
				
				double actualCostMonthly = Double.parseDouble(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText().replace("$", "").replace(",", ""));
				double expectedCostMonthly = Double.parseDouble(CostMonthlyCalculatedConvertToFullText(totalString).replace("$", "").replace(",", ""));
				
				FeatureShoppingCart.verifyCostMonthly(actualCostMonthly, expectedCostMonthly);
				
				//Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following-sibling ::span[1]")).getText().substring(1).replaceAll(",",""), totalString);				

				// make sure the cost at the bottom of the shopping cart remains at the device cost. 
				Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost']/following-sibling::span[1]")).getText(), deviceInfoActions.cost);
			}
			else
			{
				// verify no optional features have been added at this point by looking for 'No optional features have been selected' text.
				Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//div[text()='Plan Features']//following-sibling ::div[1]/descendant ::div[text()='" + noOptionalFeatures +"']"), MediumTimeout));
				
				totalString = GetNewTotal(totalString, optionalFeaturesList.get(x - 1).CostWithNoDollarSignNoComma(), Action.Remove);
				// DebugTimeout(0, totalString); // DEBUG
				
				Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following-sibling ::span[1]")).getText().substring(1).replaceAll(",",""), 
						totalString);
				
				// make sure the cost at the bottom of the shopping cart remains at the device cost. 
				Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost']/following-sibling::span[1]")).getText(), deviceInfoActions.cost);
			}
		}
	}
	
	// assume all of the optional features are not checked. select the first and the last optional feature. verify the cost monthly shown at the bottom of the 
	// shopping cart for each selection and store the final value for the cost monthly in the shopping cart.
	public static void addOptionalFeaturesToShoppingCart() throws Exception
	{		
		// get the cost that was saved off in the PlanInfoActions object. the complete field was saved off in the object so call the method that returns the cost only, i.e. "34.76".
		// this value was saved off when the plan was first added.
		String addUpCost = planInfoActions.PlanDecimalCost();		
		
		WaitForElementPresent(By.xpath("//h3[text()='Optional Features']"), MiniTimeout);
		driver.findElement(By.xpath("//h3[text()='Optional Features']/following-sibling ::ul/li[1]/label/input")).click();	// click the first indexed check box.		
		
		// call method that adds cost in list of plan options element to current cost.  
		addUpCost = GetNewTotal(addUpCost, optionalFeaturesList.get(0).CostWithNoDollarSignNoComma(), Action.Add);
		
		
		// verify the actual "cost monthly" value at the bottom of the shopping cart is correct per the expected (calculated) value.
		Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText(), 
				CostMonthlyCalculatedConvertToFullText(addUpCost), "Cost Monthly value in shopping cart is not correct in ChoosePlanPage.MakeDesiredSelections");		
		
		driver.findElement(By.xpath("//h3[text()='Optional Features']/following-sibling ::ul/li[" + optionalFeaturesList.size() + "]/label/input")).click();	// click the last indexed check box.

		// call method that adds cost in list of plan options element to current cost.		
		addUpCost = GetNewTotal(addUpCost, optionalFeaturesList.get(optionalFeaturesList.size() - 1).CostWithNoDollarSignNoComma(), Action.Add);
		
		// below waits for the correct count of items in the shopping cart section Plan Features.
		Assert.assertTrue(WaitForElements(By.xpath("(//div[@class='sn-cart__section'])[3]/div/div"), MiniTimeout, 2), 
				"Expected number of elements not found in ChoosePlanPage.MakeDesiredSelections. ");
		
		// verify the actual "cost monthly" value at the bottom of the shopping cart is correct per the expected (calculated) value.
		Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText(), 
				CostMonthlyCalculatedConvertToFullText(addUpCost), "Cost Monthly value in shopping cart is not correct in ChoosePlanPage.MakeDesiredSelections");
		
		// make sure the cost at the bottom of the shopping cart remains at the device cost. 
		Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Cost']/following-sibling::span[1]")).getText(), deviceInfoActions.cost);
		
		// store away the expected (calculated) final cost monthly at bottom of shopping cart for tests made in future		
		planInfoActions.costMonthlyTotal = addUpCost;
			
		PlanInfoActions.optionalFeatures = new ArrayList<>();
		PlanInfoActions.optionalFeatures.add(optionalFeaturesList.get(0).name);
		PlanInfoActions.optionalFeatures.add(optionalFeaturesList.get(optionalFeaturesList.size()-1).name);
		
	}	
	
	// this checks for options being present in the plan page after one click of the next button.
	public static void VerifyOptionalFeaturesPresent() throws Exception
	{
		WaitForElementVisible(By.xpath("//h3[text()='Optional Features']"),MainTimeout); 
		Assert.assertTrue(WaitForElementVisibleNoThrow(By.xpath("//h3[text()='Optional Features']"),MainTimeout), "Error in VerifVerifyOptionsPresent(). Plan page is expected to have optins.");
	}
	
	// the next button in the device page has been selected. get the name of the first plan  
	public static void StoreNameOfFirstPlanInList() throws Exception
	{
		 		
		// Ana add - To avoid the hardcoded plan name 
		planInfoActions.planSelectedName = driver.findElement(By.cssSelector("div.sn-section-heading.ng-binding")).getText();
		
		// Store the complete plan's price. E.g: '(Monthly) $114.99'
		List<WebElement> tempWebElementList =  driver.findElements(By.cssSelector(".tg-valign--top.ng-binding")); 
		planInfoActions.planCostCompleteField = tempWebElementList.get(1).getText();
		
		System.out.println("Plan Name Selected: " + planInfoActions.planSelectedName);
		
	}

  
	// store first plan on list to PlanInfoAction port number variables. 
	public static void AddPortPlan() throws InterruptedException 
	{
		// local list for holding all devices in list. 
		ArrayList<PlanInfoActions> localList = new ArrayList<PlanInfoActions>();	
		
		
		// this verifies there is at least one device in the list.
		List<WebElement> testList = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
		Assert.assertFalse(testList.isEmpty(),"Error in ChooseDevicePage.LoadPortDevices. There are no devices in device list.");

		// store all the device and select the first one.
		List<WebElement> listNames = driver.findElements(By.cssSelector(".sn-section-heading.ng-binding"));
		List<WebElement> listVendorPrice = driver.findElements(By.cssSelector(".tg-valign--top.ng-binding"));
		
		for(int x = 0, y = 0; x <  listNames.size(); x++, y +=2)
		{
			PlanInfoActions tempPlan =  new PlanInfoActions(listNames.get(x).getText(), listVendorPrice.get(y).getText(), listVendorPrice.get(y + 1).getText());
			localList.add(tempPlan);
		}

		// store first plan info to PlanInfoActions.
		PlanInfoActions.portPlanName = listNames.get(0).getText();
		PlanInfoActions.portVendorName = listVendorPrice.get(0).getText();
		PlanInfoActions.portPlanCost = listVendorPrice.get(1).getText();		
	
		// select the first plan
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();
		
		// Store the complete plan's price. E.g: '(Monthly) $114.99'
		List<WebElement> tempWebElementList =  driver.findElements(By.cssSelector(".tg-valign--top.ng-binding")); 
		planInfoActions.planCostCompleteField = tempWebElementList.get(1).getText();
		
		planInfoActions.costMonthlyTotal = planInfoActions.PlanDecimalCost();
		
		planInfoActions.planSelectedName = driver.findElement(By.cssSelector("div.sn-section-heading.ng-binding")).getText();
		System.out.println("Plan Name Selected: " + planInfoActions.planSelectedName);
		
	}
	
    public static void WaitForPageToLoadPlanSelected() throws Exception
    {
    	WaitForElementVisible(By.xpath("//div[text()='" + planSelectionText + "']"), MediumTimeout);
    	WaitForElementVisible(By.xpath("//button[text()='Remove from Cart']"), MediumTimeout);    	
    	WaitForElementVisible(By.xpath("(//button[text()='Next'])[1]"), MediumTimeout);    	
    	WaitForElementVisible(By.xpath("(//button[text()='Next'])[2]"), MediumTimeout);
    }
	
	public static boolean waitForPageToLoadPlans() throws Exception
	{
		//WaitForElementVisible(By.xpath("//div[text()='" +  planSelectionText + "']"), MainTimeout);  // <-- commented by Ana 9/8/17
		
		// try - catch added by Ana 9/8/17
		
		WaitForElementVisible(By.cssSelector("span.sn-flow__heading.ng-binding"), MediumTimeout);
		
		try {
			
			WaitForElementVisible(By.xpath("(//button[text()='Add to Cart'])[1]"), MediumTimeout);
			return true;
			
		} catch (Exception e) {
			// If there are no plans for the selected device, click Back, and select a different device
			
			WaitForElementVisible(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message"), MediumTimeout);
			String message = driver.findElement(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message")).getText();
			
			if (message.equals("No Plans Found")) {
				driver.findElement(By.xpath("//button[text()='Back']")).click();
				WaitForElementVisible(By.xpath("//span[text()='Choose your new device.']"), MediumTimeout);
			}
			return false;
		}
		
	}
	
	
	static public void WaitForPageToLoadNoPlanSelected() throws Exception
	{
		//WaitForElementVisible(By.xpath("//div[text()='" +  planSelectionText + "']"), MainTimeout);  // <-- commented by Ana 9/8/17
		
		// try - catch added by Ana 9/8/17
		
		try {
			
			WaitForElementVisible(By.xpath("(//button[text()='Add to Cart'])[1]"), ExtremeTimeout);
			
		} catch (Exception e) {
			// If there are no plans for the selected device, click Back, and select a different device
			
			WaitForElementVisible(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message"), MediumTimeout);
			String message = driver.findElement(By.cssSelector("span.sn-notifyBlock.sn-notifyBlock--message")).getText();
			
			if (message.equals("No Plans Found")) {
				driver.findElement(By.xpath("//button[text()='Back']")).click();
				WaitForElementVisible(By.xpath("//span[text()='Choose your new device.']"), MediumTimeout);
			}
		}
		
	}
	
	// this is for features test.  
	static public void SelectFirstPlan()
	{
		if(!WaitForElementClickableBoolean(By.xpath("//button[text()='Add to Cart']"), MediumTimeout))
		{
			Assert.fail("No plan found in plan list in method 'SelectFirstPlan()'");			
		}
		
		driver.findElement(By.xpath("//button[text()='Add to Cart']")).click();
		
		WaitForElementClickable(By.xpath("//button[text()='Remove from Cart']"), MediumTimeout, "");
		
	}
	
	public static WebElement backButton() {
		element = driver.findElement(By.xpath("//div[@id='_create_order_page_content_']/div[4]/button[2]"));
		return element;
	}
	
	public static WebElement featuresCheckBox1() {
		element = driver.findElement(By.xpath(".//*[@id='optionalFeature_951']"));
		return element;
	}
	public static WebElement featuresCheckBox2() {
		element = driver.findElement(By.xpath(".//*[@id='optionalFeature_88']"));
		return element;
	}
		
	public static WebElement errorNoPlanSelected() {
		element = driver.findElement(By.xpath("//div/ul/li"));
		return element;
	}
	
	// Returns Nationwide for Business Talk Share Plan in Shopping cart
	public static WebElement plan1InCart() {
		element = driver.findElement(By.xpath("//div[2]/div[9]/div[2]/div[2]/div[2]/div/div/div[1]"));
		return element;
	}
	
	//Gets CostMonthly to add to string and compare
	public static WebElement costMonthly(){
		element = driver.findElement(By.xpath(""));
		return element;
	}
	

	public static void VerifyNoPlanSelectedError() throws Exception 
	{
		// WaitForElementVisible(By.xpath("//div/ul/li"), MainTimeout);
		WaitForElementVisible(By.xpath("(//ul/li)[3]"), MainTimeout);
		
		Assert.assertEquals(driver.findElement(By.xpath("//span[@class='ng-binding']")).getText(),PlanNeedsSelectedErrorOne,
				            "Failed first error test in method 'verifyNoPlanSelected()'");
		Assert.assertEquals(driver.findElement(By.xpath("(//ul/li)[3]")).getText(),PlanNeedsSelectedErrorTwo,
				             "Failed second error test in method 'verifyNoPlanSelected()'");
	}
	
	// verify plan and device are in the shopping cart.
	public static void VerifyPlanAndDeviceInShoppingCart() throws Exception 
	{
		WaitForElementPresent(By.xpath("//div[text()='Plan']"), MainTimeout); // this is bar above the plan in the cart.
		VerifyDeviceAndPlanSections();
	}

	public static void VerifyRemoveFromCartButtonText() 
	{
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Remove from Cart']")).size() == 1 , 
				          "Failed in verifyRemoveFromCartButtonText(). There should be one button with 'Remove from cart.' text.");
	}
	
	// make sure first plan in list can be selected then make sure it's not in the shopping cart.
	public static void VerifyPlanIsNotInShoppingCart() throws Exception 
	{
		WaitForElementClickable(By.xpath("(//button[text()='Add to Cart'])[1]"), MainTimeout, "'Add to Cart' not found in method 'VerifyPlanIsNotInShoppingCart()'");
		WaitForElements(By.xpath(shopCartCount), MiniTimeout, 2);
	}
	
	
	// *** Methods commented out since they are not used *** 
	/*
	public static void verifyCostMonthlyChanges(){
		String startingCost = driver.findElement(By.xpath("//div//span[contains(text(),'$3')]")).getText();
		checkFeaturesCheckBox1();
		checkFeaturesCheckBox2();
		String newCost = driver.findElement(By.xpath("//div//span[contains(text(),'$53')]")).getText();
		//System.out.println(startingCost);
		//System.out.println(newCost);
		boolean	priceChanged;	
		if (startingCost != newCost){
				priceChanged = true;
			}
		else{
			priceChanged = false;
		}
		//System.out.println(priceChanged);
		Assert.assertEquals(priceChanged, true);
	}
	public static void verifyCostMonthlyChanges2()throws Exception{ //After removing from cart
		String startingCost = driver.findElement(By.xpath("//div//span[contains(text(),'$53')]")).getText();
		checkFeaturesCheckBox1();
		checkFeaturesCheckBox2();
		String newCost = driver.findElement(By.xpath("//div//span[contains(text(),'$3')]")).getText();
		boolean	priceChanged;	
		if(startingCost != newCost){
		priceChanged = true;
			}
		else{
			priceChanged = false;
		}
		Assert.assertEquals(priceChanged, true);
	}*/
	
	public static void clickNextButton() throws Exception 
	{
		WaitForElementVisible(By.xpath("//div[text()='" + planSelectionText + "']"), MainTimeout);
		WaitForElementClickable(By.xpath("(//button[text()='Next'])[1]"), MainTimeout, "Next button on plan page is not clickable in method 'clickNextButton()'");
		
		WebElement buttonNext = driver.findElement(By.xpath("//button[text()='Next']"));
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0," + buttonNext.getLocation().y + ")");
		buttonNext.click();
	}


	// page has settled. select the add to cart for first plan on list.
	public static void clickAddToCartButtonPlan()  
	{
		driver.findElement(By.xpath("(//button[text()='Add to Cart'])[1]")).click();
		planInfoActions.costMonthlyTotal = planInfoActions.PlanDecimalCost();
		
	}
	
	// store the included features into list in planInfoActions object.
	public static void storeIncludedFeatures() 
	{
		
		List<WebElement> includedFeaturesElements = driver.findElements(By.xpath("//h3[text()='Included Features']/following-sibling::ul[1]/li")); 
				
		for (int i = 0; i < includedFeaturesElements.size(); i++) {
			
			// System.out.println(i + " " + includedFeaturesElements.get(i).getText()); // DEBUG
			PlanInfoActions.AddIncludedFeature(includedFeaturesElements.get(i).getText());
		}
		
	}

	// store the included features into list in planInfoActions object.
	public static void StoreIncludedFeaturesCss() 
	{
		includedFeatures = driver.findElement(By.cssSelector(".sn-list--noBullets:nth-of-type(1)")).getText().split("\n");
		// ShowArray(strArray); // DEBUG
		
		// css=.sn-list--noBullets:nth-of-type(1) // this is same as (//button[text()='Add to Cart'])[2]
		
		PlanInfoActions.ClearIncludedFeature(); // make sure starting with empty list. 
		
		for(int x  = 0; x < includedFeatures.length; x++)
		{
			PlanInfoActions.AddIncludedFeature(includedFeatures[x]);
		}
	}	
	
	// select the first and last item in the 'optional features' list. 
	public static void selectFirstLastOptionalFeature()
	{
		WaitForElementClickable(By.xpath("(//li/label)[" +  optionalFeaturesList.size()  + "]"), MediumTimeout, "Failed wait in choose plan page.");
		WaitForElementClickable(By.xpath("(//li/label)[1]"), MediumTimeout, "Failed wait in choose plan page.");
		
		// Ana add
		selectedOptionalFeatures.add(driver.findElement(By.xpath("(//li/label)[1]")).getText().trim());
		selectedOptionalFeatures.add(driver.findElement(By.xpath("(//li/label)[" +  optionalFeaturesList.size()  + "]")).getText().trim());
		
		// ** NOTE: Added the following lines since the click on the feature started to fail, and was getting:
		// org.openqa.selenium.WebDriverException: unknown error: Element is not clickable at point (xx, yy)
		
		// Find the first feature 
		WebElement firstFeature = driver.findElement(By.xpath("(//li/label)[1]"));
		 
		// Scroll the browser to the element's Y position
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0," + firstFeature.getLocation().y + ")");
		 
		// Click the element
		firstFeature.click();
		
		// Find the last feature 
		WebElement lastFeature = driver.findElement(By.xpath("(//li/label)[" +  optionalFeaturesList.size()  + "]"));
				 
		// Scroll the browser to the element's Y position
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0," + lastFeature.getLocation().y + ")");
		 
		// Click the element
		lastFeature.click();
				
		
		
		//new Actions(driver).moveToElement(driver.findElement(By.xpath("(//li/label)[1]"))).perform();
		//driver.findElement(By.xpath("(//li/label)[1]")).click();
		//driver.findElement(By.id("optionalFeature_398")).click();
		
		//new Actions(driver).moveToElement(driver.findElement(By.xpath("(//li/label)[" +  optionalFeaturesList.size()  + "]"))).perform();
		//driver.findElement(By.xpath("(//li/label)[" +  optionalFeaturesList.size()  + "]")).click();
		
		// ******** TEST ************
		// Ana Oct 13
		planInfoActions.costMonthlyTotal = driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText();		
		
		
	}
	
	
	// page has settled because item checked in cart. select the only remove from cart button. 
	public static void clickRemoveFromCartButtonPlan() 
	{
		driver.findElement(By.xpath("(//button[text()='Remove from Cart'])[1]")).click();
	}
	
	public static void clickBackButton() {
		element = backButton ();
		element.click();
	}
	
	
	// *** Methods commented out since they are not used *** 
	/*
	public static void checkFeaturesCheckBox1(){
		element = featuresCheckBox1();
		element.click();
	}
	public static void checkFeaturesCheckBox2(){
		element = featuresCheckBox2();
		element.click();
	}*/

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 															helper methods 
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	
	
	// 1) this receives a string that has all the text under the "Plan Features" section in the shopping cart. 
	// 2) it removes unnecessary text.
	// 3) the string is broken apart using a split on "\n" into a string array. the elements in the array are trimmed\
	// 4) In the array, there are two elements for each Optional Feature in the Plan page.
	// 5) the for loop in this method goes through the strings under  the "Plan Features" section and compares them to expected values 
	//    in the optionalFeaturesList list of objects. 
	public static void VerifyItemsInCartAdd(String items) throws Exception
	{
		String []  tempStrArray;
		
		// remove not-needed text.
		items =items.replace("ADD:", "");
		items =items.replace("Remove", "");
		
		tempStrArray = items.split("\n"); // split into an array
		//for(String str : tempStrArray){DebugTimeout(0, str);} // DEBUG
		
		for(int x = 0; x < tempStrArray.length; x++) // trim each element - remove leading/trailing spaces.
		{
			tempStrArray[x] = tempStrArray[x].trim(); 
		}
		
		// index y goes through the list of optional features objects. index x goes through the array of strings. index x is incremented by 2 
		// because it selects two strings in the list, plan feature name and billing period type/name, in each loop. these strings are the
		// actual information. y gets information from the selected optional features object that has information that is used for the 
		// expected values. 
		for(int x = 0,  y = 0; x <  tempStrArray.length; x += 2, y++)
		{
			Assert.assertEquals(tempStrArray[x], optionalFeaturesList.get(y).name, "Expected name failed in ChoosePlanPage.VerifyItemsInCart."); // plan names
			Assert.assertEquals(tempStrArray[x + 1], optionalFeaturesList.get(y).CombineBillingIntervalAndCost(), "Expected billing and cost failed in ChoosePlanPage.VerifyItemsInCart."); // billing/cost
		}
	}
	
	// this 
	public static void VerifyItemsInCartRemove(String items) throws Exception
	{
		String []  tempStrArray;
		
		// remove not-needed text.
		items =items.replace("ADD:", "");
		items =items.replace("Remove", "");
		
		tempStrArray = items.split("\n"); // split into an array

		//for(String str : tempStrArray){DebugTimeout(0, str);} // DEBUG
		
		for(int x = 0; x < tempStrArray.length; x++) // remove leading/trailing spaces.
		{
			tempStrArray[x] = tempStrArray[x].trim(); 
		}
		
		for(int x = 0,  y = 0; x <  tempStrArray.length; x += 2, y++)
		{
			Assert.assertEquals(tempStrArray[x], optionalFeaturesList.get(y).name, "Expected name failed in ChoosePlanPage.VerifyItemsInCart.");
			Assert.assertEquals(tempStrArray[x + 1], optionalFeaturesList.get(y).CombineBillingIntervalAndCost(), "Expected billing and cost failed in ChoosePlanPage.VerifyItemsInCart.");
		}
	}	
	
	// this takes in a string that is a running total called 'runningTotal' and a string that is 
	// a value to add or subtract to/from the running total called 'valueToChange'.
	// 1) create doubles that hold the running total and the value to add.
	// 2) setup a decimal format.
	// 3) add running total and value to add.
	// 4) return string that has new running total
	public static String GetNewTotal(String runningTotal, String valueToChange, Action actionType)
	{
		// convert to double and create a decimal format.
		double tempTotalIn  = Double.valueOf(runningTotal).doubleValue();
        double tempValueForAction  = Double.valueOf(valueToChange).doubleValue(); 
	    DecimalFormat decFormat = new DecimalFormat("#.00");
        
        // add value to add sent in to running total and return.
	    if(actionType == Action.Add)
	    {
	    	tempTotalIn += tempValueForAction;
	    }
	    else
	    {	
	    	tempTotalIn -= tempValueForAction;	    
	    }

	    return decFormat.format(tempTotalIn);
	}	

	
	// this helper method for several methods. it takes a string like " abcdef - ksndhy", splits it into two array
    // elements, trims each array element, and returns the final array of two string elements. 
	// The split character to split by is passed in as 'splitChar'.
    public static String[] SetupTwoElementStringArray(String optionToSplit, String splitChar)
    {
    	String strTrimmed = optionToSplit.trim();
    	String [] strSlipt = strTrimmed.split(splitChar);
    	String [] strFinal = new String[2];
    	
    	strFinal[0] = strSlipt[0].trim();
    	strFinal[1] = strSlipt[1].trim();    	
    	
    	// for(String str : strFinal){System.out.println(str);} // debug
    	
    	return strFinal;
    }
	
    // this helper method receives a string with a ':' separator. The item on the right
    // of the separator is what is needed as a return.
    public static String GetPlanFeatureName(String fullString) throws Exception
    {
    	String [] retString = SetupTwoElementStringArray(fullString, ":") ;
    	return retString[1];
    }

  
}
