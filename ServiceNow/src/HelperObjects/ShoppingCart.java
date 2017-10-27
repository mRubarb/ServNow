package HelperObjects;

public class ShoppingCart {

	// costOneTime = device price + accessory price
	public static String costOneTime;
	
	// costMonthly = plan price + optional features price
	public static String costMonthly;
	
	
	public ShoppingCart() {
		
		costOneTime = "0";
		costMonthly = "0";
	}
	
}
