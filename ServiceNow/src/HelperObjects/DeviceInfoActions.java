package HelperObjects;

//store the information for the selected device.
//provide data manipulations for the device information.
public class DeviceInfoActions 
{
	public String name = "";
	public String cost = "";	
	public String vendor = "";
	
	// device actions that can be selected in 'My Devices' page.
	public static String deactivate = "Deactivate Service"; 
	public static String suspend = "Suspend Service";	
	public static String unsuspend = "Unsuspend Service";	
	public static String orderAccessories = "Order Accessories";
	public static String swapDevices = "Swap Devices";
	public static String upgradeDevice = "Upgrade Device";
	public static String upgradeService = "Upgrade Service";
	public static String updateFeatures = "Update Features";	
	public static String portNumber = "Port Number";	
	
	public static int upgradeDeviceIndex = 0;
	
	public static String reasonUpgradeAction = "warrant repair";
	public static String currentVendorPortNumber = "";	
	
	
	public DeviceInfoActions() { }
	
	public DeviceInfoActions(String deviceName, String deviceVendor) 
	{
		name = deviceName;
		vendor = deviceVendor;
	}
	
	public String DeviceCostRemoveDollarSign() 
	{
		return cost.replace("$", "");
	}

	// this assumes manufacturer name is first word in device name.
	public String GetDeviceManufacturer() 
	{
		return  name.split(" ")[0];
	}
}
