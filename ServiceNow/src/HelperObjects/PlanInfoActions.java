package HelperObjects;

import java.util.ArrayList;


// store the information for the selected plan.
// provide data manipulations for the plan information.
public class PlanInfoActions 
{
	public String costMonthlyTotal = ""; 
	public String planSelectedName = "";
	public static String planCostCompleteField = ""; // this is set in ChoosePlanPage.StoreNameOfFirstPlanInList()
	public static String costValueInShoppingCart = ""; // this is used in new activation
	//public static String planstartingCost = "";  // ** NOT USED - TO BE REMOVED	
	public static String planVendor = "";
	public static ArrayList<String> includedFeatures = new ArrayList<>(); // this holds the included features that are in plan page.
	public static ArrayList<String> optionalFeatures = new ArrayList<>(); // this holds the optional features added to shopping cart.  // Ana add - 9/12/17
	public static String includedFeaturesTitle = "Included Features";	
	public static String optionalFeaturesTitle = "Optional Features";	
	public static String carrierAccountNumber = "67234957"; // for additional info 
	public static String otherInfo = "This is other info test."; // for additional info	

	// storage for port number
	public static String portPlanName = "";
	public static String portVendorName = "";
	public static String portPlanCost = "";	
	public static String portCostMonthly = "";	
	
	// ctor
	public PlanInfoActions(String plnSelectedName)
	{
		planSelectedName = plnSelectedName;
	}
	
	// ctor
	public PlanInfoActions(String plnSelectedName, String plnVendor, String planCost)
	{
		portPlanName = plnSelectedName;
		portVendorName = plnVendor;
		portPlanCost = planCost;		
		
	}
	
	
	public PlanInfoActions() { }
	

	// this returns the numeric cost. it finds the cost section in planCostCompleteField, removes the dollar sign, and removes commas. 
	public String PlanDecimalCost()
	{
		//return  planCostCompleteField.split(" ")[1].replace(",",".").substring(1); // modified by ana - 8/10/18
		return  planCostCompleteField.split(" ")[1].replace(",","").substring(1);
	}
	
	public String PlanTextCost()
	{
		return  planCostCompleteField.split(" ")[1];
	}	
	
	public String SetupCostForOrderDetails()
	{
		String [] strArray;
		strArray = planCostCompleteField.split(" ");
		return strArray[1] + " " + strArray[0].replace("(","").replace(")","").toLowerCase();
	}
	
	public static void AddIncludedFeature(String feature)
	{
		includedFeatures.add(feature);
	}
	
	public static void ClearIncludedFeature()
	{
		includedFeatures.clear();
	}
	
	public void Show()
	{
		System.out.println(" -------------------------------------- ");
		System.out.println("planSelectedName: " + planSelectedName);
		System.out.println("planCostCompleteField: " + planCostCompleteField);
	}
	
	
}
