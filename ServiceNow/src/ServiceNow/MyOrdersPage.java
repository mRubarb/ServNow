package ServiceNow;

import java.time.LocalDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


public class MyOrdersPage extends BaseClass 
{

	// public static int orderBlockIndex = 0;
	public static String [] strTempArray;
	public static String tmpString = "";
	static LocalDateTime ldt = LocalDateTime.now();
	
	public static void WaitForPageToLoad() throws Exception
	{
		// WaitForElementVisible(By.xpath("//label[text()='Page']"), MainTimeout); // this isn't visible.
		WaitForElementVisible(By.xpath("(//button[text()='View Order'])[3]"), MainTimeout);		
		WaitForElementVisible(By.xpath("(//div[contains(text(),'#')])[3]"), MainTimeout);	
	}		
	
	// this only checks the order info block on the 'my orders page'.  
	public static void VerifyOrderInfoMyOrdersPageMainPage() throws Exception
	{
		String errMessage = "Failure in checking order block in My Orders Page in MyOrdersPage.VerifyOrderInfoInMyOrdersPage.";
		
		// this verifies the very top section that has action and order Id.
		Assert.assertEquals(FormatTopSectionOfOrderBlock()[0], orderDetailsObjectExpected.orderId, errMessage);
		Assert.assertEquals(FormatTopSectionOfOrderBlock()[1], orderDetailsObjectExpected.orderType, errMessage);
		
		// this gets the three strings in the order block that are below the order number.
		strTempArray = driver.findElement(By.xpath("(.//*[@id='externalOrderNumber'])[" + orderBlockIndex + "]/../..")).getText().split("\n");

		// this gets order status. need to put this in a string because it is used later for string filtering.
		tmpString = strTempArray[0].split(String.valueOf(ldt.getYear()))[1];
		
		Assert.assertEquals(tmpString, orderDetailsObjectExpected.status.toUpperCase(), errMessage); // verify order status in first line.
		Assert.assertEquals(strTempArray[0].replace(tmpString, "").replace("Date Submitted  ", ""),BuildDateString(), errMessage); // verify date in first line. 
		
		if (approvalActionType.equals(ApprovalActionType.transferServiceIn)
				|| approvalActionType.equals(ApprovalActionType.transferServiceInAndPort)) {
		
			Assert.assertEquals(strTempArray[1].replace("Service Number  ", ""), formatServiceNumber(newServiceNumber), errMessage); // verify full service number in second line
			
		} else {
			
			Assert.assertEquals(strTempArray[1].replace("Service Number  ", ""), fullServiceNumber, errMessage); // verify full service number in second line
		}
		
		Assert.assertEquals(strTempArray[2].replace("External Order #  ", ""), orderDetailsObjectExpected.externalOrderId, errMessage); // verify external order id in third line.
		
	}	
	
	
	private static String formatServiceNumber(String serviceNumber) {
		
		// from 5669879877 to +1 (566) 987-9877
		
		String part1 = serviceNumber.substring(0, 3);
		String part2 = serviceNumber.substring(3, 6);
		String part3 = serviceNumber.substring(6, 10);
		
		//System.out.println("Part 1: " + part1);
		//System.out.println("Part 2: " + part2);
		//System.out.println("Part 3: " + part3);
		
		String formattedServiceNumber = "+1" + " (" + part1 + ") " + part2 + "-" + part3 ;
		
		return formattedServiceNumber;
		
	}
	

	// this only checks the order info block on the order details page'.  
	public static void VerifyOrderInfoStatusMyOrdersPageMainPage() throws Exception
	{
		WaitForElementVisible(By.xpath("(//span[@id='dateSubmitted']/span)[" + orderBlockIndex  + "]"), MediumTimeout);
		Assert.assertEquals(driver.findElement(By.xpath("(//span[@id='dateSubmitted']/span)[" + orderBlockIndex  + "]")).getText(), orderDetailsObjectExpected.status.toUpperCase(), "");
	}
	
	// this gets the list of external order id's on the My Orders page, goes through them, and sees if the external order id is on the page
	// if found, the one based index of the external order id is stored into a local variable. if not found, throw error.
	public static void LocateOrderActionBlock()  // --> modified by Ana - 9/19/17
	{
		List<WebElement> externalNumbersList = driver.findElements(By.id("externalOrderNumber"));

		int i = 0;
		
		boolean orderNumberMatches = false;
		
		while (!orderNumberMatches && i < externalNumbersList.size())
		{
			String externalOrderNumberFound = externalNumbersList.get(i).getText().trim();
			//System.out.println("externalOrderNumberFound: " + externalOrderNumberFound);
			//System.out.println("orderDetailsObjectExpected.externalOrderId: " + orderDetailsObjectExpected.externalOrderId);
			
			if(externalOrderNumberFound.equals(orderDetailsObjectExpected.externalOrderId)) { orderNumberMatches = true; }
						
			i++;
			orderBlockIndex = i;
		}
		
		// if the external order id wasn't found the orderBlockIndex will be 0.
		Assert.assertTrue(orderNumberMatches, "Failed to find the External Order Id in users My Orders list. Method is in ActionsBase.LocateOrderAction.");	
		
	}	

	// this assumes the 'my orders' page is loaded. the 'orderBlockIndex' variable was set in 'LocateOrderActionBlock()'.
	public static void SelectOrderActionBlock()
	{
		driver.findElement(By.xpath("(//button[text()='View Order'])[" + orderBlockIndex +"]")).click(); // click the order
	}
	
	
	public static void VerifyApprovals()  
	{
		String errorMessage = "Incorrect information found in Order Details page in MyOrdersPage.VerifyApprovals";

		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[5]")).getText(), approverBob, errorMessage);
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[7]")).getText(), approverServiceNow, errorMessage);
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[9]")).getText(), approverAdmin, errorMessage);		
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-binding'])[11]")).getText(), approverAna, errorMessage);		
	}		
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												HELPERS
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	public static String [] FormatTopSectionOfOrderBlock()
	{
		strTempArray = driver.findElement(By.xpath("(.//*[@id='externalOrderNumber'])[" + orderBlockIndex + "]/../preceding ::div[3]")).getText().split(":");
		return new String [] {strTempArray[0].replace("#", ""), strTempArray[1].trim()};
	}
}
