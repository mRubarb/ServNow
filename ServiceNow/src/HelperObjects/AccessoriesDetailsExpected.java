package HelperObjects;

import java.util.List;

import org.testng.Assert;
import ServiceNow.BaseClass;


public class AccessoriesDetailsExpected // extends BaseClass
{
	public String name = "";
	public String vendor = "";	
	public String cost = "";
	public static String finalCost = "";  
	public static String finalCostMonthly = "";	
	public String manufacturer = "";	 // this is used with AccessoriesDetailsExpected classes that are instantiated. didn't make static.
	
	public static boolean objOneFound = false; 
	public static boolean objTwoFound = false;
	
	// these store the 'accessoriesDetailsListExpected' indexes of the accessories found on pages.
	public static int indexOne = 0; 
	public static int indexTwo = 0;
	
	// these indexes are used in order accessories action test for selecting two accessories in the list of accessories.
	public static int orderAccessoriesActionSelectionOneArrayIndex = 0;
	public static int orderAccessoriesActionSelectionTwoArrayIndex = 1;	
	
	
	public AccessoriesDetailsExpected() {}
	
	public AccessoriesDetailsExpected(String name, String vendor, String price)
	{
		this.name = name;
		this.vendor = vendor;
		this.cost = price;
	}
	
	public String GetCostNoDollarSign()
	{
		return cost.substring(1);
	}
	
	public String GetCostNoDollarSignNoComma()
	{
		return cost.substring(1).replace(",","");
	}
	
	// this assumes manufacturer name is first word in accessory name.
	public String GetManufacturer()
	{
		return  name.split(" ")[0];
	}
	
	// this assumes manufacturer name is first word in accessory name.
	public String GetManufacturerTwoWords()
	{
		return  name.split(" ")[0] + " " + name.split(" ")[1]; 
	}	
	
	
	// this looks for the name and the cost to be equal in the order details page. the price is not shown in the order details page.
	public boolean equals(AccessoriesDetailsExpected obj) 
	{
		return obj.name.equals(this.name) && obj.cost.equals(this.cost);		
	}
	
	// this looks for the name to be equal in the approval page description section.
	public boolean equalsName(AccessoriesDetailsExpected obj) 
	{
		return obj.name.equals(this.name);		
	}
	
	// this is for verifying the accessories on a page.
	public static void verifyOrderAccessories(AccessoriesDetailsExpected actualObjOne, AccessoriesDetailsExpected actualObjTwo)
	{
		String errMessage = "Failure in AccessoriesDetailsExpected.VerifyOderAccessories.";
		
		// these are set when the corresponding object is found in the accessory list.  
		objOneFound = false; 
		objTwoFound = false;
		
		// 1) go through the accessoriesDetailsListExpected list.
		// 2) use the accessoriesDetailsListExpected 'equals' operator to find the two expected accessories on the approval page.
		//    the 'equals' operator verifies the name and price of an accessory in accessoriesDetailsListExpected list equals the 
		//    name and price of one of the two accessories passed in. 
		// 3) verify their list index is one of the two indexes used when the accessories were selected on the add accessories page.
		for(int x = 0; x < BaseClass.accessoriesDetailsListExpected.size(); x++)
		{
			if(BaseClass.accessoriesDetailsListExpected.get(x).equals(actualObjOne))
			{
				Assert.assertTrue(x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionOneArrayIndex || x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionTwoArrayIndex,
						errMessage + " Incorrect index found.");
				objOneFound = true;
				indexOne = x;
			}
			if(BaseClass.accessoriesDetailsListExpected.get(x).equals(actualObjTwo))
			{
				Assert.assertTrue(x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionOneArrayIndex || x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionTwoArrayIndex,
						errMessage + " Incorrect index found.");
				objTwoFound = true;
				indexTwo = x;
			}
		}
		
		 // System.out.println(indexOne + " " +  indexTwo); 
		
		// verify the two indexes found are not the same.
		Assert.assertTrue(indexOne !=  indexTwo, errMessage + " The two indexes found should not be the same.");
		
		// verify both objects passed in are found (extra sanity check).		 
		Assert.assertTrue(objOneFound && objTwoFound, "One or both of the objects passed in were not found.");
	}	

	public static void verifyOrderAccessories(AccessoriesDetailsExpected actualObjOne)
	{
		String errMessage = "Failure in AccessoriesDetailsExpected.VerifyOderAccessories.";
		
		// these are set when the corresponding object is found in the accessory list.  
		objOneFound = false; 
	
		
		// 1) go through the accessoriesDetailsListExpected list.
		// 2) use the accessoriesDetailsListExpected 'equals' operator to find the two expected accessories on the approval page.
		//    the 'equals' operator verifies the name and price of an accessory in accessoriesDetailsListExpected list equals the 
		//    name and price of one of the two accessories passed in. 
		// 3) verify their list index is one of the two indexes used when the accessories were selected on the add accessories page.
		for(int x = 0; x < BaseClass.accessoriesDetailsListExpected.size(); x++)
		{
			if(BaseClass.accessoriesDetailsListExpected.get(x).equals(actualObjOne))
			{
				Assert.assertTrue(x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionOneArrayIndex || x == AccessoriesDetailsExpected.orderAccessoriesActionSelectionTwoArrayIndex,
						errMessage + " Incorrect index found.");
				objOneFound = true;
				indexOne = x;
			}
			
		}
		
	}	
	
	
	// this is for verifying the accessory manufacturers on a page.
	public static void verifyOrderAccessoriesManufacturers(List<AccessoriesDetailsExpected> listAccessoriesDetails)
	{
		
		for (int i = 0; i < listAccessoriesDetails.size(); i++) {
		
			Assert.assertTrue(listAccessoriesDetails.get(i).manufacturer.trim().contains(BaseClass.accessoriesDetailsListExpected.get(i).GetManufacturer().trim()), "");
			// Uses contains instead of equals for the case where the manufacturer's name is composed of two words (e.g.: Verizon Wireless).
			// The GetManufacturer() method returns only the first word (Verizon)
		}
	}
	
	
	/*
	 * // this is for verifying the accessory manufacturers on a page.
	public static void verifyOrderAccessoriesManufacturers(AccessoriesDetailsExpected actualObjOne, AccessoriesDetailsExpected actualObjTwo)
	{
		// index one goes with the object 'accessoryOne' 
		Assert.assertEquals(actualObjOne.manufacturer, BaseClass.accessoriesDetailsListExpected.get(AccessoriesDetailsExpected.indexOne).GetManufacturer(), "");
		
		// index two goes with the object 'accessoryTwo' 
		Assert.assertEquals(actualObjTwo.manufacturer, BaseClass.accessoriesDetailsListExpected.get(AccessoriesDetailsExpected.indexTwo).GetManufacturer(), "");
	}*/
	
	
	
	
	public void Show()
	{
		System.out.println("------------------------------");
		System.out.println(name);
		System.out.println(vendor);
		System.out.println(cost);
	}
	
	
}
