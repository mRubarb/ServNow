package HelperObjects;

import org.testng.Assert;
import ServiceNow.BaseClass;


// this is for hold all the optional features listed in the plan page.
// it also  
public class PlanOptionalFeatures 
{
	public String name = "";
	public String completeName = "";	
	public String cost = "";	
	public String billingInterval = "";	
	public static String optionalFeaturesTitle = "Optional Features";  
	
	public PlanOptionalFeatures(String completeLine) 
	{
		// completeLine --> e.g.: Sprint Equipment Service and Repair Program - $4.00 (Monthly)
		
		completeName = completeLine.trim();
		//String[] splitOne = completeLine.split("-");  
		
		// Line above modified by Ana 9/11/17
		// If the name of the feature has a "-", the name is cut off. 
		// Instead, the $ sign is used to obtain feature name and cost + billing interval separately  
		
		
		int indexBegin = completeLine.indexOf("- $"); // Get the index where the feature name ends
		int indexEnd = indexBegin + 3;  // Get the index where the price begins
		
		name = completeLine.substring(0, indexBegin);
		name = name.trim();
		
		cost = "$" + completeLine.substring(indexEnd).split(" ")[0];
		billingInterval = completeLine.substring(indexEnd).split(" ")[1];
		
		
	//	System.out.println("name: " + name);
	//	System.out.println("cost: " + cost);
	//	System.out.println("billingInterval: " + billingInterval);	
		
	}
	
	
	// this is for areas that want to populate field(s) from outside the constructor
	public PlanOptionalFeatures()
	{
	}
	
	public String CombineBillingIntervalAndCost()
	{
		return billingInterval + " " + cost;
	}
	
	public String CostWithNoDollarSignNoComma()
	{
		return cost.substring(1).replace(",","");
	}
	
	public String CompleteNameForOrderDetails()
	{
		return completeName.replace("(", "").replace(")", "").toLowerCase();
	}
	
	public String RemoveCurlBrackets()
	{
		return completeName.replace("(","").replace(")","");
	}
	
	// this takes two optional features names and verifies one is the first item in the 
	// optionalFeaturesList and the other is the last item in the optionalFeaturesList.  
	public static void VerifyPlanOptionsFirstLast(String optionOneName, String optionTwoName)	
	{
		String errMessage = "Failed verification of optional plans in PlanOptionalFeatures.VerifyPlanOptionsFirstLast.";
		
		int firstIndex = 0;
		int lastIndex = BaseClass.optionalFeaturesList.size() - 1;
		
		// variables below are used in verification of what was found in the for loop below. 
		int indexOne = 0;
		int indexTwo = 0;		
		boolean foundFirstName = false;
		boolean foundLastName = false;		
		
		// go through the optionalFeaturesList and look for the two options passed in. verify the indexes of the options found 
		// are the first and last items in list. curly brackets are removed because the verification and order details pages 
		// don't have them.
		for(int x = 0; x < BaseClass.optionalFeaturesList.size(); x++)
		{
			if(BaseClass.optionalFeaturesList.get(x).RemoveCurlBrackets().toLowerCase().equals(optionOneName.toLowerCase()))
			{
				Assert.assertTrue(x == firstIndex || x == lastIndex, errMessage);
				foundFirstName = true;
				indexOne = x;
			}
			if(BaseClass.optionalFeaturesList.get(x).RemoveCurlBrackets().toLowerCase().equals(optionTwoName.toLowerCase()))
			{
				Assert.assertTrue(x == firstIndex || x == lastIndex, errMessage);
				foundLastName = true;
				indexTwo = x;
			}
		}

		Assert.assertTrue(indexOne != indexTwo, errMessage); // verify two different names (indexes) were found.
		Assert.assertTrue(foundFirstName && foundLastName, errMessage); // extra sanity check		
	}
	
	public void Show()
	{
		System.out.println("----------------------");
		System.out.println("Name:" + name);	
		System.out.println("Cost:" + cost);
		System.out.println("Billing Interval:" + billingInterval);
	}
	
	public static void ShowOptionalFeaturesList()
	{
		for(PlanOptionalFeatures pOpFeature : BaseClass.optionalFeaturesList)
		{
			pOpFeature.Show();
		}
	}
	
}
