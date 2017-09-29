package ServiceNow;

public class DeviceAndVendor 
{
	public String device = "";
	public String vendor = "";
	public String cost = "";
	
	public DeviceAndVendor(String dev, String ven, String cst)
	{
		device = dev;
		vendor = ven;
		cost = cst;
	}
	
	//public boolean equals(DeviceAndVendor devObj)
	//{
	//	return this.device == devObj.device && this.vendor == devObj.vendor && this.cost == devObj.cost;
	//}
	
	//public boolean deviceNameEquals(DeviceAndVendor devObj)
	//{
	//	return this.device == devObj.device;
	//}
	
	public void Show()
	{
		System.out.println("============");
		System.out.println(device);
		System.out.println(vendor);
		System.out.println(cost);	
	}
	
}
