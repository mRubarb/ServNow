package HelperObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;

import ServiceNow.BaseClass;


public class Feature 
{
	public String fullName = "";	
	public String fullCost = "";
	public String decimalCost = "";	
	public String dollarCost = "";
	public String  action = ""; // this is set externally when a feature is added to shopping cart.	
	public final String monthlyTag = "(Monthly)";
	
	// selections for adding items to cart before going to verify order using zero index.
	public static int selectionOne = 0;
	public static int selectionTwo = 1;
	public static int selectionThree = 2;
	public static String finalCost = "0.00"; // this is the final cost that shows up in the approval section. it is calculated in 'UpdateFeature.SetupAddedFeaturesForNextPages'. 
	
	public Feature(String fullName, String fullCost)
	{
		this.fullName = fullName;
		this.fullCost = fullCost;
		decimalCost = fullCost.split(" ")[0].replace("$","");
		dollarCost = fullCost.split(" ")[0];
	}

	// this verifies feature one equals feature two. feature one is the expected from the list of features and featureTwo is the actual from the shopping cart. 
	public static void VerifyFeaturesMatchExpectedActual(Feature featureOne , FeatureShoppingCart featureTwo) 
	{
		String errMessage  = "Fail in Feature class when comparing teo feature objects in method 'VerifyFeaturesMatch'"; 
		
		Assert.assertEquals(featureOne.fullName, featureTwo.fullName, errMessage);
		Assert.assertEquals(featureOne.decimalCost, featureTwo.decimalCost, errMessage);	
		Assert.assertEquals(featureOne.dollarCost, featureTwo.dollarCost, errMessage);	
	}

	
	// this sets up the action information for a feature. the dollar cost is set to negative 
	// when remove action is passed in. this is because a remove action negates the dollar cost
	// in the 'verify order', 'order details', and approval pages.
	// 
	// This is done in 'UpdateFeature.SetupAddedFeaturesForNextPages'. 
	// 
	public void SetActions(BaseClass.UpdateFeatureActions actionType) 
	{
		switch (actionType)
		{
		
			case add:
			{
				action = "ADD:";
				break;
			}
			case remove:
			{
				action = "REMOVE:";	
				dollarCost = "-" + dollarCost;
				break;				
			}
			case noChange:
			{
				action = "NoChange";				
				break;				
			}
			default:
			{
				Assert.fail("Bad enum parametr sent to Feature.SetActions");
				break;
			}	
		}
	}
	
	// this is for verifying the update features on a page.
	public static void VerifyUpdateFeatures(Feature objOne, Feature objTwo, Feature objThree)
	{
		String errMessage = "Indexing failure in AccessoriesDetailsExpected.VerifyOderAccessories.";
		
		// this list will hold the index of each feature found to be on the feature list. 
		List<Integer> intList = new ArrayList<Integer>();
		
		for(int x = 0; x < BaseClass.featuresList.size(); x++)
		{
			if(BaseClass.featuresList.get(x).equals(objOne))
			{
				Assert.assertTrue(x == selectionOne || x ==  selectionTwo || x == selectionThree, errMessage);
				intList.add(x);
			}
			if(BaseClass.featuresList.get(x).equals(objTwo))
			{
				Assert.assertTrue(x == selectionOne || x ==  selectionTwo || x == selectionThree, errMessage);
				intList.add(x);
			}
			if(BaseClass.featuresList.get(x).equals(objThree))
			{
				Assert.assertTrue(x == selectionOne || x ==  selectionTwo || x == selectionThree, errMessage);
				intList.add(x);
			}
		
			// verify all the indexes found are unique.
			Set<Integer> set = new HashSet<Integer>(intList);
			Assert.assertTrue(intList.size() == set.size(), errMessage);
		}
	}
	
	// this looks for the name and the cost to be equal in the order details page. the price is not shown in the order details page.
	public boolean equals(Feature obj) 
	{
		return obj.fullName.equals(this.fullName) && obj.fullCost.equals(this.fullCost) && obj.action.equals(this.action);		
	}
	
	public void Show()
	{
		System.out.println(" ---------------------------------------- ");
		System.out.println("fullName: " + fullName);
		System.out.println("fullCost: " + fullCost);
		System.out.println("decimalCost: " + decimalCost);
		System.out.println("dollarCost: " + dollarCost);
		System.out.println("monthlyTag: " + monthlyTag);
		System.out.println("action: " + action);		
	}
	
	public static void ShowList()
	{
		for(Feature fr : BaseClass.featuresList)
		{
			fr.Show();
		}
	}
	
	
}
