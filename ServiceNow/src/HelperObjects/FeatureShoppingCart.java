package HelperObjects;

import org.openqa.selenium.By;
import org.testng.Assert;

import ServiceNow.BaseClass;
import ServiceNow.BaseClass.Action;

public class FeatureShoppingCart 
{
	public String fullName = "";	
	public String fullCost = "";
	public String decimalCost = "";	
	public String dollarCost = "";
	public boolean costIsZero = false;
	public static String monthlyTag = "(Monthly)";
	
	
	public static String costMonthly = "0";	
	public static String saveMonthly = "0";	
	public static int maxItemsToAdd = 3;
	public static String removeText = "REMOVE";
	
	public FeatureShoppingCart(String fullName, String fullCost)
	{
		this.fullName = fullName;

		if(fullCost.trim().equals(monthlyTag))
		{
			this.fullCost = monthlyTag;
			dollarCost = "$0.00";
			decimalCost = "0.00";
			costIsZero = true;
		}
		else
		{
			this.fullCost = fullCost;
			dollarCost = fullCost.replace(monthlyTag,"").trim();
			decimalCost = dollarCost.replace("$","").trim();
		}
	}
	
	// the method that calculates change in costMonthly and saveMonthly returns '.00' 
	// for a zero value. shopping cart test needs zero to look like '0.00'.  
	public static void FixZerovalues()
	{
		if(costMonthly.equals(".00"))
		{
			costMonthly = "0.00";
		}
		if(saveMonthly.equals(".00"))
		{
			saveMonthly = "0.00";
		}
	}
	
	// this goes through the list holding items added to the shopping cart and compares each one to
	// the expected list. both lists are expected to be in the same order. 
	// this also totals up the expected monthly cost as it goes through the shopping cart list.
	// featuresList is the expected list and featuresShoppingCartList is the actual list. 
	public static void VerifyAddedFeatures() throws Exception
	{
		costMonthly = "0"; // need to reset on each call. this is called more than once with different sized shopping cart lists. 
		
		for(int x = 0; x < BaseClass.featuresShoppingCartList.size(); x++)
		{
			costMonthly = BaseClass.GetNewTotal(costMonthly, BaseClass.featuresList.get(x).decimalCost, Action.Add);
			Feature.VerifyFeaturesMatchExpectedActual(BaseClass.featuresList.get(x), BaseClass.featuresShoppingCartList.get(x)); 
		}

		// verify 'Cost Monthly' in shopping cart to cost calculated from for loop above.
		Assert.assertEquals(BaseClass.driver.findElement(By.xpath("//span[text()='Cost Monthly']/following ::span[1]")).getText(), "$" + costMonthly, "");
	}
	
	public void Show()
	{
		System.out.println(" ---------------------------------------- ");
		System.out.println("fullName: " + fullName);
		System.out.println("fullCost: " + fullCost);
		System.out.println("decimalCost: " + decimalCost);
		System.out.println("dollarCost: " + dollarCost);
		System.out.println("monthlyTag: " + monthlyTag);
		System.out.println("costIsZero: " + costIsZero);		
	}
	
	public static void ShowList()
	{
		for(FeatureShoppingCart fr : BaseClass.featuresShoppingCartList)
		{
			fr.Show();
		}
	}
	
	
	// It verifies that the Cost Monthly found and the Cost Monthly calculated are equal or their difference is less than 1.0
	public static void verifyCostMonthly(double actualCostMonthly, double expectedCostMonthly) {
	
		double difference = Math.abs(actualCostMonthly - expectedCostMonthly);
				
		Assert.assertTrue((difference < 1.0), "Cost Monthly value in shopping cart is not correct in ChoosePlanPage.MakeDesiredSelections");
		
	}
	
	
}
