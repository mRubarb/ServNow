package HelperObjects;

import junit.framework.Assert;

public class AccessoriesDetailsShopCart 
{
	
	public String name = "";
	public String cost = "";
	public String fullCostString = "";	
	public int expectedArraySize = 2;
	
	
	public AccessoriesDetailsShopCart(String nam, String cst)
	{
		name = nam;
		fullCostString = cst;
		cost = SetupCost(cst); 
	}
	
	private String SetupCost(String cst)
	{
		cst = cst.trim();
		String [] tmpArray = cst.split(" ");
		Assert.assertEquals(expectedArraySize, tmpArray.length); // expecting 2 elements.
		return tmpArray[1].trim();
	}
	
	public void Show()
	{
		System.out.println(name);
		System.out.println(fullCostString);		
		System.out.println(cost);		
	}
}

