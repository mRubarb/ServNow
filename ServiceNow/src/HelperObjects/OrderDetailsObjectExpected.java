package HelperObjects;

// store the info of a completed Order New Service order.
public class OrderDetailsObjectExpected 
{
	public String orderId = "";
	public String externalOrderId = "";	
	public String departmentName = "";
	public String orderType = "";	
	public String orderTypeNewActivation = "New Device"; // order type for new activation is different in My Orders list when compared to title in approvals. 	
	public String status = "";
	public String orderDate = "";	
	public String orderIdTwo = ""; // jnupp
	public String externalOrderIdTwo = ""; // jnupp
	
	public static String awaitApproval = "Awaiting Approval";
	
	
	// this ctor is foe ordering a new service and device
	// other users change these parameters as needed.
	public OrderDetailsObjectExpected(String dept, String stats)  //(String dept, String oType, String stats)  <-- Modified 9/18/17 - orderType was hardcoded  
	{
		departmentName = dept;
		//orderType = oType;		
		status = stats;
	}
	
	public OrderDetailsObjectExpected(String oType)
	{
		orderType = oType;	
	}	
	
	public void Show()
	{
		System.out.println("EXPECTED ORDER DETAILS: ---------------------------------");
		System.out.println("orderId:" + orderId);
		System.out.println("externalOrderId:" + externalOrderId);
		System.out.println("departmentName:" + departmentName);
		System.out.println("orderType:" + orderType);
		System.out.println("status:" + status);
		System.out.println("orderDate:" + orderDate);
	}
	
}


