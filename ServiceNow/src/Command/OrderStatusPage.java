package Command;
import org.openqa.selenium.By;
//
import org.testng.annotations.BeforeClass;

import HelperObjects.OrderDetailsObjectActual;


public class OrderStatusPage extends ServiceNow.BaseClass
{

	public static void GoToOrderStatus() throws Exception  
	{
	    WaitForElementClickable(By.id("menuMainProcure"),MainTimeout, "Failed wait in GoToOrderStatus");	    
	    Thread.sleep(4000); // below frame click problem. have to wait.
	    driver.findElement(By.id("menuMainProcure")).click();
		
	    WaitForElementPresent(By.id("menuMainProcure_Order_Status"),MainTimeout);	    
	    driver.findElement(By.id("menuMainProcure_Order_Status")).click();
	    
	    // switch to the correct frame and wait for name text box.
	    // DebugTimeout(0, "Ready for frame switch");
	    driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
	    WaitForElementPresent(By.xpath("(.//*[@id='pad_rt_5']/input[1])[1]"), MainTimeout);
	    WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);	    
	    // DebugTimeout(0, "frame switch done");
	    WaitForElementClickable(By.xpath("//input[@value='Search']"),MainTimeout,"failed wait before selecting search button.");	    
	}	
	
	// this assumes oder status page is opened.
	public static void SearchUserByLastFirstName() throws Exception
	{
	    
		WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
	    
		driver.findElement(By.xpath("(//td/br)[1]/../input[@name='lastName']")).sendKeys(userLastName);	    
	    driver.findElement(By.xpath("(//td/br)[1]/../input[@name='firstName']")).sendKeys(userFirstName);
				
		WaitForElementClickable(By.xpath("//input[@value='Search']"), MainTimeout, "Failed wait before selecting search button.");
	    
	    driver.findElement(By.xpath("//input[@value='Search']")).click();	    
	    
	    WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
		
		
		// DebugTimeout(0,"starting SearchUserByLastName()");
	    // saw error again in here 12/10/15  
	    // unknown error: Element is not clickable at point (132, 711). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
		//WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
	    //driver.findElement(By.xpath("(//td/br)[1]/../input[@name='lastName']")).sendKeys(userLastName);	    
	    //driver.findElement(By.xpath("(//td/br)[1]/../input[@name='firstName']")).sendKeys(userFirstName);
	    // intermittent error here when try to click search: unknown error: Element is not clickable at point (1205, 499). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
	    //WaitForElementClickable(By.xpath("//input[@value='Search']"),MainTimeout,"failed wait before selecting search button.");
	    //driver.findElement(By.xpath("//input[@value='Search']")).click();	    
	    //WaitForElementPresent(By.xpath("(//td/br)[1]/../input[@name='lastName']"), MainTimeout);
	    // DebugTimeout(0,"search method done");
	}		


	// this assumes order page is opened and a search has been done for a user's orders.
	public static void GetOrderDetailsActual() throws Exception
	{
	    String[] tempArray;
	    String strTemp;

	    int rowNum = 4;	    
	    
	    WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[5]"), MainTimeout);
	    
	    WaitForElementClickable((By.xpath("(.//*[@id='forceShow'])[1]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    

	    int columnNum = 2;
	    
	    WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]"), MainTimeout);
	    WaitForElementClickable((By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    
    	strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")).getText();
	    tempArray = strTemp.split("\n");
	    
	    // DebugTimeout(0, tempArray[0] + " " + tempArray[1]); // DEBUG
	    orderDetailsObjectActual.departmentName = tempArray[1];
	    
	    columnNum = 5;
	    
    	WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]"), MainTimeout);
	    WaitForElementClickable((By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    
    	strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")).getText();
	    tempArray = strTemp.split("\n");

	    // DebugTimeout(0, tempArray[0] + " " + tempArray[1]); // DEBUG
	    orderDetailsObjectActual.orderId = tempArray[0];
	    orderDetailsObjectActual.externalOrderId = tempArray[1];
	    
	    columnNum = 7;
	    
    	WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]"), MainTimeout);
	    WaitForElementClickable((By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    
    	strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")).getText();
	    tempArray = strTemp.split("\n");	    
	    
	    // DebugTimeout(0, tempArray[0] + " " + tempArray[1]); // DEBUG
	    orderDetailsObjectActual.orderDate = tempArray[0];
	    orderDetailsObjectActual.orderType = tempArray[1];
	    
	    columnNum = 9;
	    
    	WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]"), MainTimeout);
	    WaitForElementClickable((By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    
    	strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + rowNum + "]/td))[" + columnNum + "]")).getText();
	    tempArray = strTemp.split("\n");	    
	    
	    // DebugTimeout(0, tempArray[0] + " " + tempArray[1]); // DEBUG
	    orderDetailsObjectActual.status = tempArray[1];
	}	
	
	// this assumes oder  page is opened and a search has been done for a user's orders.
	public static void GetOrderDetails() throws Exception
	{
	    String[] tempArray;
	    String strTemp;
		//List<String> listDates = new ArrayList<String>();	    
		String[] dateLabels = {"InitNote", "SubmitEvent", "FulfillEvent"};		
	    
	    //OrderInfoOutList = new FileWriter(OrderInfoOutListFile); // create file writer.
	    //OrderInfoOutList.write("Orders List:\r\n"); // output header.
	    
	    // /////////////////////////////////////////////////////////////////////////
	    // NOTE !!!!!NOTE !!!!! See "Empty" class for breakdown of Xpaths. ////////
	    // /////////////////////////////////////////////////////////////////////////

	    WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[4]/td))[5]"), MainTimeout);
	    WaitForElementClickable((By.xpath("(.//*[@id='forceShow'])[1]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    

	    for(int x = 4, z = 1; x < 13 ; x += 2, z++) 
		{
		    DebugTimeout(0,"Pass"); // frame click problem // removed 2/9/16 
	    	WaitForElementVisible(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[5]"), MainTimeout);
	    	WaitForElementClickable((By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[5]")), MainTimeout, "failed wait for clickable in method GetAndOutputOrderList_Details");	    
	    	
	    	strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[5]")).getText();
		    tempArray = strTemp.split("\n");
		    
		    DebugTimeout(0, tempArray[0] + " " + tempArray[1]);
		    
		    //OrderInfoOutList.write("OrderId:" + tempArray[0] + "\r\n"); 		    
		    //OrderInfoOutList.write("ExternalOrderId:" + tempArray[1] + "\r\n");	
		    
		    strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[7]")).getText();
		    tempArray = strTemp.split("\n");

		    DebugTimeout(0, tempArray[0] + " " + tempArray[1]);

		    
		    //OrderInfoOutList.write("OrderDate:" + tempArray[0] + "\r\n"); 		    
		    //OrderInfoOutList.write("OrderType:" + tempArray[1] + "\r\n");		    
		    
		    strTemp = driver.findElement(By.xpath("(((//tr/td[text()='USER INFO']/../../tr)[" + x + "]/td))[9]")).getText();
		    tempArray = strTemp.split("\n");		    

		    DebugTimeout(0, tempArray[0] + " " + tempArray[1]);

		    
		    //OrderInfoOutList.write("StatusDate:" + tempArray[0] + "\r\n"); 		    
		    //OrderInfoOutList.write("Status:" + tempArray[1] + "\r\n");		    

		    // BELOW NEW
		    //driver.findElement(By.xpath("(.//*[@id='forceShow'])[3]")).click();
		    //driver.findElement(By.xpath("(//b[text()='Doright, Dudley'])[" + z + "]")).click();		    

		   
		    //listDates = OrderDetails.GetOrderHistoryDatesAndReturn();
		    
		    //for(int y = 0; y < listDates.size(); y++)
		    //{
		    //	OrderInfoOutList.write("Date" + dateLabels[y]  + ":" + listDates.get(y) + "\r\n");
		    //	//System.out.println("Date" + dateLabels[y]  + ":" + listDates.get(y));
		    //}		    		    	

		}
		//OrderInfoOutList.close();
	}	


















}
