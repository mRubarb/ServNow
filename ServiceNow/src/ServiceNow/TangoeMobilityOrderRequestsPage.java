package ServiceNow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import ActionsBaseClasses.ActionsBase;
import ActionsBaseClasses.CommonTestSteps;

public class TangoeMobilityOrderRequestsPage extends ActionsBase
{
	// these are the column names to be used in the 'Tangoe Mobility Order Requests'
	public static String lastOrderStatus = "Tangoe Last Order Status";
	public static String orderStatus = "Tangoe Order Status";
	public static String tangoeOrderID = "Tangoe Order ID";
	public static String shortDescription = "Short description";

	// these will hold the indexes of the column names above.	
	public static int lastOrderStatusIndex = 0;
	public static int orderStatusIndex = 0;
	public static int orderIdIndex = 0;
	public static int shortDescritionIndex = 0;
	public static int orderIdRow = 0;
	
	public static String currentOrderID = ""; // this is the orderId of the deactivate order that is placed.
	
	// these are the expected states in 'Tangoe Last Order Status' and 'Tangoe Order Status' columns right after the deactivate order is submitted.
	public static String tangoeLastOrderStatusAfterDeactivate = "Order Submitted";
	public static String tangoeOrderStatusAfterDeactivate = "Order Submitted Awaiting Processing";
	
	// these are the expected states in 'Tangoe Last Order Status' and 'Tangoe Order Status' columns right after the deactivate order is updated by command.
	public static String awaitApproval = "Awaiting Approval";
	public static String awaitApprovalInServicenow = "Awaiting Approval in ServiceNow";
	
	// this is test that should be found in the orderId short description column.
	public static String orderType = "Deactivate Service Order";
	
	// this is set true if the order is already updated by command when the status of 'Tangoe Last Order Status' and 'Tangoe Order Status' columns are first checked. 
	public static boolean startingStatesAlreadyUpdated = false;
	
	public static String commonCssRowSelector = ".list_div_cell>div>table>tbody>tr:nth-of-type("; // 7/31/18 - this appears in many places.
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	// this makes sure the required columns needed by the test that uses this class are present.
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyNeededColumnsPresent() throws Exception
	{
		int cntr = 1;
		String expectedText = "Tangoe Mobility Order Requests";
		WaitForElementVisible(By.cssSelector(".navbar-title.list_title"), ExtremeTimeout - MainTimeout);
		Assert.assertEquals(driver.findElement(By.cssSelector(".navbar-title.list_title")).getText(), expectedText, "Failed to find the correct title in 'Tangoe Mobility Order Requests' page.");
		
		List<String> columnNames = GetColumnNames();
		
		for(String str : columnNames)
		{
			if(str.equals(""))
			{
				continue;
			}
			
			ShowText("* " + str);
			
			if(str.contains(lastOrderStatus))
			{
				lastOrderStatusIndex = cntr;
			}
			
			if(str.contains(orderStatus))
			{
				orderStatusIndex = cntr;
			}

			if(str.contains(tangoeOrderID))
			{
				orderIdIndex = cntr;
			}
			
			if(str.contains(shortDescription))
			{
				shortDescritionIndex = cntr;
			}
			
			cntr++;
		}

		// DEBUG - show index of needed columns.
		System.out.println("Column numbers = lastOrderStatusIndex: " + lastOrderStatusIndex + " orderStatusIndex: " + orderStatusIndex + " Tangoe Order Id: " + orderIdIndex);
		
		if(lastOrderStatusIndex == 0  || orderStatusIndex == 0 || orderIdIndex == 0 || shortDescritionIndex == 0)
		{
			Assert.fail("Verify  column names 'Tangoe Last Order Status', 'Tangoe Order Status', 'Tangoe Order Id', and 'Short description' "
					  + "are listed in the 'Tangoe Mobility Order Requests' page.");
		}
		
		// store the orderId that was saved in the deactivate order.
		currentOrderID = ActionsBase.orderSubmittedPageOrderNumber;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// this first verifies the the orderId of interest has 'Deactivate Service Order' text in its short description. then it
	// looks at the 'Tangoe Last Order Status' and 'Tangoe Order Status' columns to see if they are in completed or waiting states.
	// if both columns are in a completed state 'startingStatesAlreadyUpdated' boolean is set true. if a column is not in a completed state 
	// the expected 'order submitted' state for the column is verified.
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void ObserveStartingStates() throws Exception
	{
		String strActual = "";		
		boolean tangoeLastOrderStatusFinished = false; 
		boolean tangoeOrderStatusFinished = false;
		
		// currentOrderID = "13297008"; // debug
		
		ShowText("Order ID is: " + currentOrderID); // Debug
		
		orderIdRow = GetOrderIdRow();

		// verify the orderId of interest has 'Deactivate Service Order' text in its short description column.
		strActual = driver.findElement(By.cssSelector(commonCssRowSelector + orderIdRow + ")>td:nth-of-type(" + (shortDescritionIndex + 2) + ")")).getText();
		Assert.assertTrue(strActual.contains(orderType));
		
		// see if the 'Tangoe Last Order Status' column has the completed state. if it does, set its corresponding boolean true. if it doesn't, verify the expected text.  
		strActual = driver.findElement(By.cssSelector(commonCssRowSelector + orderIdRow + ")>td:nth-of-type(" + (lastOrderStatusIndex + 2) + ")")).getText();
		if (strActual.equals(awaitApproval))
		{
			tangoeLastOrderStatusFinished = true;
		}
		else
		{
			Assert.assertEquals(strActual, tangoeLastOrderStatusAfterDeactivate, "");			
		}
		
		// see if the 'Tangoe Order Status' column has the starting state. if it does, set its corresponding boolean true. if it doesn't, verify the expected text.
		strActual = driver.findElement(By.cssSelector(commonCssRowSelector + orderIdRow + ")>td:nth-of-type(" + (orderStatusIndex + 2) + ")")).getText();
		if (strActual.equals(awaitApprovalInServicenow))
		{
			tangoeOrderStatusFinished = true;
		}
		else
		{
			Assert.assertEquals(strActual, tangoeOrderStatusAfterDeactivate, "");			
		}
		
		if(tangoeLastOrderStatusFinished && tangoeOrderStatusFinished)
		{
			Pause("Starting states already Done before going into loop. test already passed..");
			startingStatesAlreadyUpdated = true;
		}

	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// keep refreshing the 'Tangoe Mobility Order Requests' page until the 'tangoe last order status' and the 'tangoe order status' columns are updated by command. 
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
	public static void StartPolling() throws Exception
	{
		String strActual = "";
		int rowNum = 0;
		int loopCntr = 1;
		boolean tangoeLastOrderStatusComplete = false;
		boolean tangoeOrderStatusComplete = false;
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + (1000 * 60 * 11); // wait for up to 11 minutes.

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		ShowText("Start polling --------------------- .");
		System.out.println(dateFormat.format(date));
		
		while(System.currentTimeMillis()<endTime)
		{
			rowNum = GetOrderIdRow();
			
			// get the 'tangoe last order status' and and see if it is gone to "Awaiting Approval".  
			strActual = driver.findElement(By.cssSelector(commonCssRowSelector + rowNum + ")>td:nth-of-type(" + (lastOrderStatusIndex + 2) + ")")).getText();

			if(strActual.equals(awaitApproval))
			{
				tangoeLastOrderStatusComplete = true;
			}
			
			// get the 'tangoe order status' and and see if it is gone to "Awaiting Approval in ServiceNow".
			strActual = driver.findElement(By.cssSelector(commonCssRowSelector + rowNum + ")>td:nth-of-type(" + (orderStatusIndex + 2) + ")")).getText();

			if(strActual.equals(awaitApprovalInServicenow))
			{
				tangoeOrderStatusComplete = true;
			}
			
			if(tangoeLastOrderStatusComplete && tangoeOrderStatusComplete) // if 'tangoe last order status' and 'tangoe order status' columns are in correct states - break. 
			{
				break;
			}
			
			ShowText("-------- Internal Test loop complete. Refreshing page and checking again.... --------");
			
			loopCntr++;
			CommonTestSteps.GoToTangoeMobilityOrderRequests(); // refresh page.
			Thread.sleep(3000);
		}
		
		date = new Date();
		ShowText("Polling done -------------------------------- .");
		System.out.println(dateFormat.format(date));
		
		boolean result = tangoeLastOrderStatusComplete && tangoeOrderStatusComplete;
	
		if(!result)
		{
			Assert.fail("Failed to see 'tangoe last order status' and 'tangoe order status' columns go to correct state in StartPolling() method.");
		}
		
		// don't need.
		//if(startingStatesAlreadyUpdated)
		//{
		//	Pause("Already Complete.");
		//	Assert.assertTrue(loopCntr == 1, "Completed statuses were found in ObserveStartingStates() method but StartPolling() method found an incomplete status. ");
		//}
		
		ShowText("Test Passed.");
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	//											HELPERS
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	// get the row number for the current order Id of interest. 
	public static int GetOrderIdRow()
	{
		int rowNum = 0;
		
		// wait for first row orderId to be click-able.
		WaitForElementClickable(By.cssSelector(commonCssRowSelector + 1 + ")>td:nth-of-type(" + (orderIdIndex + 2) + ")"), MediumTimeout, "");
		
		/// loop through 20 rows in 'Tangoe Mobility Order Requests' page and find what row the orderId is in. 
		for(int x = 1; x < 21; x++)
		{
			// get orderId row and save it into class global variable.
			if(driver.findElement(By.cssSelector(commonCssRowSelector + x + ")>td:nth-of-type(" + (orderIdIndex + 2) + ")")).getText().equals(currentOrderID))
			{
				rowNum = x;
				break;
			}
		}
		
		if(rowNum == 0)
		{
			Assert.fail("Falied to find the Order Id row number in 'TangoeMobilityOrderRequestsPage.GetOrderIdRow()' method.");
		}
				
		return rowNum;
	}
	
	// get the column names shown across the top of the 'Tangoe Mobility Order Requests' page.
	public static List<String> GetColumnNames()
	{
		// List<WebElement> columnList = driver.findElements(By.cssSelector(".list_div_cell>div>table>thead>tr>th>span"));
		List<WebElement> columnList = driver.findElements(By.xpath("//a[@class='column_head list_hdrcell table-col-header']")); 
		List<String> returnList = new ArrayList<String>();
		
		for(WebElement ele : columnList)
		{
			// ShowText(ele.getText());
			returnList.add(ele.getText());
		}
		return returnList;
	}
}
