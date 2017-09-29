package HelperObjects;


public class DevicePortNumber 
{
	public String name = "";
	public String vendor = "";
	public String cost = "";
	public static int numItemsInDevicePortNumberList = 0;
	
	// selected device for port number
	public static String selectedDeviceName = "";
	public static String selectedDeviceVendor = "";
	public static String selectedDevicePrice = "";
	
	// selected plan for port number --> ** COMMENTING ATTRIBUTES SINCE THEY ARE NOT USED.. ** TO BE REMOVED **   
	//public static String selectedPlanName = "";
	//public static String selectedPlanVendor = "";
	//public static String selectedPlanPrice = "";
	
	public DevicePortNumber(String name, String vendor, String cost)
	{
		this.name = name;
		this.vendor = vendor;		
		this.cost = cost;		
	}
	
	public void Show()
	{
		System.out.println(" ------------------- ");
		System.out.println("name:" + name);
		System.out.println("vendor:" + vendor);
		System.out.println("cost:" + cost);		
	}
}
