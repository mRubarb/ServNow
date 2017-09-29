package HelperObjects;

public class OrderDetailsObjectActual 
{
	public String orderId = "";
	public String externalOrderId = "";
	public String departmentName = "";
	public String orderType = "";
	public String status = "";	
	public String orderDate = "";	

	public void Show()
	{
		System.out.println("ACTUAL: ---------------------------------");
		System.out.println("orderId:" + orderId);
		System.out.println("externalOrderId:" + externalOrderId);
		System.out.println("departmentName:" + departmentName);
		System.out.println("orderType:" + orderType);
		System.out.println("status:" + status);
		System.out.println("orderDate:" + orderDate);
	}
}
