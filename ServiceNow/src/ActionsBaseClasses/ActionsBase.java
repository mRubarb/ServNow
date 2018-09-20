package ActionsBaseClasses;

import org.openqa.selenium.By;
import org.testng.Assert;

import HelperObjects.CalendarDateTimeObject;
import ServiceNow.BaseClass;
import ServiceNow.Frames;
import ServiceNow.OrderSubmittedPage;
import ServiceNow.SideBar;

// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// this is the base class for actions (deactivate, suspend, etc) classes. all of the actions classes are in the package
// called 'action classes approve' and 'action classes reject'.
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActionsBase extends BaseClass 
{
	public static String [] historyDataArray;
	
	// this is set in the order submitted page. it holds the order id. it is used to populate the orderDetailsObjectExpected object at a later time.
	public static String orderSubmittedPageOrderNumber = ""; 
	
	// this is the amount of time for test loops. 
	public static int loopTime = 660;
	
	// this is how many of the items in the update features page should be stored into a list of items.
	public static int numUpdateFeaturesToLoad = 10;
	
	// order types titles 
	public static String deactivateActionTitle = "Deactivate Service for your Device";
	public static String suspendActionTitle = "Suspend your Service";	
	public static String unsuspendActionTitle = "Unsuspend your Service"; 
	public static String orderAccessoriesActionTitle = "Order Accessories";	
	public static String swapDevice = "Swap a Device";	
	public static String upgradeDevice = "Upgrade a Device";
	public static String upgradeService = "Upgrade a Service";	
	public static String updateServiceTitle = "Update Service Features for Device";
	public static String portNumberTitle = "Port your Number";
	public static String transferServiceOutActionTitle = "Transfer Service for your Device to be personal"; // transfer service out.	
	
	// this holds the monthly cost after doing the 'VerifyAddFeatures' method.
	public static String costMonthlyAfterAddingFeatures = "0";
	
	// action status types.
	public static String awaitingApprovalStatus = "Awaiting Approval in ServiceNow";	
	public static String inFullfillmentStatus = "In Fulfillment";
	
	public static String monthlyCostOrderAccessories = ""; // this is set in the order accessories action.
	
	// general variables
	public static String reasonUpgradeDevice = "warrant repair";
	
	private static String admins = "SN 2 Admin (Unknown)";  //", sn2.admin.xx1@example.com";
	private static String approverText = "Approvers: not_required:ServiceNow Certifier - Approver , not_required:, approved:Bob Lichtenfels-Approver, not_required:ServiceNow Certifier - Admin, not_required:Ana Pace-Approver";
	
	// this verifies the cost listed in the 'verify orders' page is the same as the cost shown when the accessories that were 
	// added in order accessories page. this check is done in the verify orders page.
	public static void VerifyCostOrderAccessoriesAction()
	{
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Cost']/../following ::td/span")).getText(), monthlyCostOrderAccessories, 
                 "Cost listed in Verify Orders page does not match the cost shown when accessroies were added in  ActionsBase VerifyCostOrderAccessoriesAction."  );  
	}
	
	// this verifies the title at the top of each page that is stepped through.
	public static void VerifyPageTitle(String actionType) throws Exception
	{
		Assert.assertEquals(driver.findElement(By.cssSelector(".tg-pageTitle")).getText(), actionType, "Failed page title check in ActionsBase.VerifyPageTitle.");
	}
	
	// this sets up the order number that is shown in the order submitted page when a service is done.  
	public static void StoreOrderNumberToVariable() throws Exception
	{
		orderSubmittedPageOrderNumber = driver.findElement(By.xpath("//div[@class='sn-notifyBlock__body']/p")).getText().replace("Order #","");
		//orderSubmittedPageOrderNumber = driver.findElement(By.cssSelector("h1.tg-pageTitle.ng-binding")).getText().replace("Order #","").trim();
		
	}
	
	// jnupp below
	//   
	public static void StoreOrderNumberTwoObjectVariable() throws Exception
	{
		// orderSubmittedPageOrderNumber = driver.findElement(By.xpath("//div[@class='sn-notifyBlock__body']/p")).getText().replace("Order #",""); // orig
		//orderSubmittedPageOrderNumber = driver.findElement(By.cssSelector("h1.tg-pageTitle.ng-binding")).getText().replace("Order #","").trim();
		
		if(orderDetailsObjectExpected.equals(null))
		{
			Assert.fail();
		}
		
		orderDetailsObjectExpected.orderIdTwo = driver.findElement(By.xpath("//div[@class='sn-notifyBlock__body']/p")).getText().replace("Order #","");
	}
	// jnupp above
		
		
	// this compares the order number on the verify order page to what is displayed on the order submitted page.
	public static void VerifyOrderNumberAndOrderTypeBetweenPages() throws Exception
	{
		Assert.assertEquals(GetTopSectionItems()[0], orderSubmittedPageOrderNumber, "Order number in order submitted  doesn't match order number in Order Submitted Page. Method is OrderSubmittedPage.VerifyOrderNumber");
		Assert.assertEquals(GetTopSectionItems()[1], orderDetailsObjectExpected.orderType);		
	}	

	// this returns the order number and the type of action found near the top order details (submitted) page. 
	static public String [] GetTopSectionItems()
	{
		String fullTitle = driver.findElement(By.cssSelector(".sn-section-heading.ng-binding")).getText();		
		String [] strArray = fullTitle.split(":");
		return new String [] {strArray[0].replace("#",""),strArray[1].trim()};
	}		
	
	// this checks the full service number shown on the order details (submitted) page. the 'fullServiceNumber' variable
	// was set when the deactivate test was on the 'MyDevices' page.
	static public void VerifyFullServiceNumber()
	{
		Assert.assertEquals(driver.findElement(By.xpath("//label[text()='Service Number']/following ::span[1]")).getText(), fullServiceNumber,
				            "Full service number is wrong in order subbmitted page in RunDeactivateService.VerifyFullServiceNumber.");
	}
	
	public static void GoOutAndBack() throws Exception
	{
		Frames.switchToGsftNavFrame(); 		
		SideBar.clickAllMyOrders();
		Frames.switchToGsftMainFrame();
		ServiceNow.MyOrdersPage.WaitForPageToLoad();
		ServiceNow.MyOrdersPage.SelectOrderActionBlock();
		OrderSubmittedPage.WaitForOrderDetailsPageToLoad(); 
	}	
	
	public static boolean RunTimedCall_VerifyLimitedUserOrdersPage(long waitDuration) throws Exception 
	{
		long waitTime = waitDuration;
		boolean isGood = true;
		
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis()-startTime) < waitTime *1000)
		{
			isGood = true;
			try
			{
				VerifyLimitedUserOrder();
			}
			catch (AssertionError e)
			{
				System.out.println("Assert error");
				System.out.println(e.getMessage());
				isGood = false;
				GoOutAndBackMyOrders();
			}
			catch(org.openqa.selenium.NoSuchElementException e)
			{
				System.out.println("selection error");
				System.out.println(e.getMessage());
				isGood = false;
				GoOutAndBackMyOrders();
			}
			finally
			{
				// DebugTimeout(0, "Finally Has been reached.");
				if(isGood)
				{
					return true;
				}
			}
			Thread.sleep(2000); // pause before next call.
		}
		return false;
	}
	
	public static void GoOutAndBackMyOrders() throws Exception
	{
		Frames.switchToGsftNavFrame(); 		
		SideBar.clickAllMyOrders();
		Frames.switchToGsftMainFrame();		
		ServiceNow.MyOrdersPage.WaitForPageToLoad();
	}	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// this finds and verifies the order block that is associated with the order just placed.
	// the order block is on the limited user's 'my orders' page. the order details under this 
	// page were already verified when the deactivate action was done.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	public static void VerifyLimitedUserOrder() throws Exception
	{
		ServiceNow.MyOrdersPage.WaitForPageToLoad();
		ServiceNow.MyOrdersPage.LocateOrderActionBlock();
		ServiceNow.MyOrdersPage.VerifyOrderInfoMyOrdersPageMainPage(); // bladdzzz
	}		
	
	// this verifies each row in the order details page. this is for the order details page after an order action 
	// has been submitted and the order details page has synced with command.
	public static void VerifyHistoryDetailsForOrderSubmitted() throws Exception
	{
		String errMessage = "Failed to verify History Details in order details page with method ActionBase.VerifyHistoryDetails.";
		
		WaitForElementVisible(By.xpath("(//label[text()='Event'])[3]"), MainTimeout);
		
		historyDataArray = driver.findElement(By.xpath("//div[@id='panel-history']/div/div/table/tbody")).getText().split("\n");
		
		Assert.assertTrue(historyDataArray[0].contains("Entered Order Processing"), errMessage); // row 1
		Assert.assertTrue(historyDataArray[0].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 1 		
		
		Assert.assertEquals(historyDataArray[1].trim(), "Workflow",errMessage); // row 2	
		
		Assert.assertEquals(historyDataArray[2].replace("EventAwaiting","Awaiting"), "Awaiting Approval Event", errMessage); // row 3
		
		Assert.assertTrue(historyDataArray[3].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 4 
		
		Assert.assertEquals(historyDataArray[4].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 5
		
		Assert.assertEquals(historyDataArray[5].replace("EventOrder","Order"), "Order Submitted Event", errMessage); // row 6		
		
		Assert.assertTrue(historyDataArray[6].contains("Test Additional Instructions"), errMessage); // row 7 
		Assert.assertTrue(historyDataArray[6].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 7
		
		Assert.assertEquals(historyDataArray[7].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 8		
		
		Assert.assertEquals(historyDataArray[8].replace("Event","").trim(), "Initiator-Note", errMessage); // row 9
	}

	
	
	// this verifies he history section depending on the order approval type. 
	public static void VerifyOrderDetailsHistoryAfterApproval() throws Exception
	{
		String errMessage = "Failed to verify History Details in order details page with method ActionBase.VerifyOrderDetailsHistoryAfterApproval.";
		//String admins = "SN 2 Admin (Unknown)";  //", sn2.admin.xx1@example.com";
		//String approverText = "Approvers: not_required:ServiceNow Certifier - Approver , not_required:, approved:Bob Lichtenfels-Approver, not_required:ServiceNow Certifier - Admin, not_required:Ana Pace-Approver";
		
		switch (approverAction)
		{
			case approve:
			{
				WaitForElementVisible(By.xpath("(//label[text()='Event'])[5]"), MainTimeout);

				historyDataArray = driver.findElement(By.xpath("(//table/tbody)[2]")).getText().split("\n");
				//for(String str : historyDataArray){DebugTimeout(0, str.trim());} // DEBUG
				
				Assert.assertTrue(historyDataArray[0].contains("Entered Order Processing"), errMessage); // row 1
				Assert.assertTrue(historyDataArray[0].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 1				
				
				Assert.assertEquals(historyDataArray[1].trim(),"Workflow",errMessage); // row 2
				
				Assert.assertEquals(historyDataArray[2].replace("EventIn", "In"),"In Fulfillment Event", errMessage); // row 3
				
				String [] strTempArray = historyDataArray[3].split("\\."); // row 4
				Assert.assertTrue(strTempArray[0].contains("Order was approved in ServiceNow"), errMessage); // row 4 
				Assert.assertTrue(strTempArray[0].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 4;				
				// Assert.assertTrue(strTempArray[1].contains(approverText), errMessage); // row 4 // THIS TEXT IS NOT IN CONSISTENT ORDER. TODO
				
				Assert.assertTrue(historyDataArray[4].contains(admins), errMessage); // row 5		
				
				Assert.assertEquals(historyDataArray[5].replace("Event", ""), "Approver-Note", errMessage); // row 6		
				
				Assert.assertTrue(historyDataArray[6].contains("Entered Order Processing"), errMessage); // row 7
				Assert.assertTrue(historyDataArray[6].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 7
				
				Assert.assertEquals(historyDataArray[7].trim(), "Workflow", errMessage); // row 8
				
				Assert.assertEquals(historyDataArray[8].replace("EventAwaiting", "Awaiting"), "Awaiting Approval Event", errMessage); // row 9	
				
				Assert.assertTrue(historyDataArray[9].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 10
				
				Assert.assertEquals(historyDataArray[10].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 11 
				
				Assert.assertEquals(historyDataArray[11].replace("EventOrder", "Order"), "Order Submitted Event", errMessage); // row 12
				
				Assert.assertTrue(historyDataArray[12].contains("Test Additional Instructions"), errMessage); // row 13	
				Assert.assertTrue(historyDataArray[12].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 13
				
				Assert.assertEquals(historyDataArray[13].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 14		
				
				Assert.assertEquals(historyDataArray[14].replace("EventInitiator", "Initiator"), "Initiator-Note", errMessage); // row 15 		
				
				break;
				
			}
			case reject:
			{
				WaitForElementVisible(By.xpath("(//label[text()='Event'])[4]"), MediumTimeout);
				historyDataArray = driver.findElement(By.xpath("(//table/tbody)[2]")).getText().split("\n");				
				// for(String str : historyDataArray){DebugTimeout(0, str.trim());} // DEBUG
 
				// Assert.assertTrue(strTempArray[0].contains(approverText), errMessage); // row 4 // THIS TEXT IS NOT IN CONSISTENT ORDER. TODO // row 1				
				
				Assert.assertTrue(historyDataArray[1].trim().contains(admins), errMessage); // row 2
				Assert.assertEquals(historyDataArray[2].replace("EventApproval", "Approval"), "Approval Rejected Event", errMessage); // row 3
				
				Assert.assertTrue(historyDataArray[3].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 4
				Assert.assertTrue(historyDataArray[3].contains("Entered Order Processing"), errMessage); // row 4
				
				Assert.assertEquals(historyDataArray[4].trim(), "Workflow", errMessage); // row 5
				
				Assert.assertEquals(historyDataArray[5].replace("EventAwaiting","Awaiting"), "Awaiting Approval Event", errMessage); // row 6	
				
				Assert.assertTrue(historyDataArray[6].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 7				
				
				Assert.assertEquals(historyDataArray[7].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 8				
				
				Assert.assertEquals(historyDataArray[8].replace("EventOrder","Order"), "Order Submitted Event", errMessage); // row 9				
				
				Assert.assertTrue(historyDataArray[9].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 10				
				
				Assert.assertTrue(historyDataArray[9].contains("Test Additional Instructions"), errMessage); // row 11
				
				Assert.assertEquals(historyDataArray[10].trim(), userLimitedShorterNameOrderHistory, errMessage); // row 12				
				Assert.assertEquals(historyDataArray[11].replace("EventInitiator","Initiator"), "Initiator-Note", errMessage); // row 12				
				break;
			}
			case none:
			{
				Assert.fail("FAILURE: Order History Post Approval Not Being Checked.");
			}
		}
	}

	// this verifies the history section depending on the order approval type. 
	public static void VerifyOrderDetailsHistoryAccessoriesAfterApproval(ApproverAction action) throws Exception
	{
		String errMessage = "Failed to verify History Details in order details page with method ActionBase.VerifyOrderDetailsHistoryAfterApproval.";
		//String admins = "SN 2 Admin (Unknown), sn2.admin.xx1@example.com";
		//String approverText = "Approvers: not_required:ServiceNow Certifier - Approver , not_required:, approved:Bob Lichtenfels-Approver, not_required:ServiceNow Certifier - Admin, not_required:Ana Pace-Approver";
		
		switch(action)
		{
			case approve:
			{
				WaitForElementVisible(By.xpath("(//label[text()='Event'])[5]"), MainTimeout);
				
				historyDataArray = driver.findElement(By.xpath("//div[text()='History']/following ::div[1]/table/tbody")).getText().split("\n");				
				
				Assert.assertTrue(historyDataArray[0].contains("Entered Order Processing"), errMessage); // row 1
				Assert.assertTrue(historyDataArray[0].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 1				
				Assert.assertEquals(historyDataArray[1].trim(),"Workflow",errMessage); // row 2
				Assert.assertEquals(historyDataArray[2].replace("EventIn", "In"),"In Fulfillment Event", errMessage); // row 3
				String [] strTempArray = historyDataArray[3].split("\\."); // row 4
				Assert.assertTrue(strTempArray[0].contains("Order was approved in ServiceNow"), errMessage); // row 4 
				Assert.assertTrue(strTempArray[0].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 4;				
				// Assert.assertTrue(strTempArray[1].contains(approverText), errMessage); // row 4 // THIS TEXT IS NOT IN CONSISTENT ORDER. TODO
				Assert.assertTrue(historyDataArray[4].contains(admins), errMessage); // row 5		
				Assert.assertEquals(historyDataArray[5].replace("Event", ""), "Approver-Note", errMessage); // row 6		
				Assert.assertTrue(historyDataArray[6].contains("Entered Order Processing"), errMessage); // row 7
				Assert.assertTrue(historyDataArray[6].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 7
				Assert.assertEquals(historyDataArray[7].trim(), "Workflow", errMessage); // row 8
				Assert.assertEquals(historyDataArray[8].replace("EventAwaiting", "Awaiting"), "Awaiting Approval Event", errMessage); // row 9	
				Assert.assertTrue(historyDataArray[9].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 10
				Assert.assertEquals(historyDataArray[10].replace(" (Unknown),", "").trim(), userLimitedShorterNameOrderHistory, errMessage); // row 11 
				Assert.assertEquals(historyDataArray[11].replace("EventOrder", "Order"), "Order Submitted Event", errMessage); // row 12
				Assert.assertTrue(historyDataArray[12].contains("Test Additional Instructions"), errMessage); // row 13	
				Assert.assertTrue(historyDataArray[12].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 13
				Assert.assertEquals(historyDataArray[13].replace(" (Unknown),", "").trim(), userLimitedShorterNameOrderHistory, errMessage); // row 14		
				Assert.assertEquals(historyDataArray[14].replace("EventInitiator", "Initiator"), "Initiator-Note", errMessage); // row 15 		
				break;
			}
			case reject:
			{
				WaitForElementVisible(By.xpath("(//label[text()='Event'])[4]"), MediumTimeout);
				historyDataArray = driver.findElement(By.xpath("//div[text()='History']/following ::div[1]/table/tbody")).getText().split("\n");

				// Assert.assertTrue(strTempArray[0].contains(approverText), errMessage); // row 4 // THIS TEXT IS NOT IN CONSISTENT ORDER. TODO // row 1				
				Assert.assertTrue(historyDataArray[1].trim().contains(admins), errMessage); // row 2
				Assert.assertEquals(historyDataArray[2].replace("EventApproval", "Approval"), "Approval Rejected Event", errMessage); // row 3
				Assert.assertTrue(historyDataArray[3].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 4
				Assert.assertTrue(historyDataArray[3].contains("Entered Order Processing"), errMessage); // row 4
				Assert.assertEquals(historyDataArray[4].trim(), "Workflow", errMessage); // row 5
				Assert.assertEquals(historyDataArray[5].replace("EventAwaiting","Awaiting"), "Awaiting Approval Event", errMessage); // row 6	
				Assert.assertTrue(historyDataArray[6].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 7				
				Assert.assertEquals(historyDataArray[7].replace(" (Unknown),", "").trim(), userLimitedShorterNameOrderHistory, errMessage); // row 8				
				Assert.assertEquals(historyDataArray[8].replace("EventOrder","Order"), "Order Submitted Event", errMessage); // row 9				
				Assert.assertTrue(historyDataArray[9].contains(CalendarDateTimeObject.BuildDateForHistory()), errMessage); // row 10				
				Assert.assertTrue(historyDataArray[9].contains("Test Additional Instructions"), errMessage); // row 11
				Assert.assertEquals(historyDataArray[10].replace(" (Unknown),","").trim(), userLimitedShorterNameOrderHistory, errMessage); // row 12				
				Assert.assertEquals(historyDataArray[11].replace("EventInitiator","Initiator"), "Initiator-Note", errMessage); // row 12
				break;
			}
			case none:
			{
				Assert.fail("FAILURE: Order History Post Approval Not Being Checked in ActionsBase.VerifyOrderDetailsHistoryAccessoriesAfterApproval.");
			}
		}
	}



}
