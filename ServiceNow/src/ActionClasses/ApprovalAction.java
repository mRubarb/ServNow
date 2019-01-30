package ActionClasses;

import ServiceNow.Approvals;
import ServiceNow.BaseClass;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


public class ApprovalAction extends BaseClass {

	
	// List<OrderDetailsObjectExpected> orders
	
	// this is for rejecting two orders. 
	public static void approveOrRejectOrders(ApproverAction approverAction) throws Exception {
	
		boolean foundTwoOrders = true;
		
		// see if both orders are at requested state.
		if(!Approvals.LookForOrdersAtRequestedStateTwoOrders(30000)) // parameter in milliseconds.
		{
			if(!Approvals.LookForOrdersAtRequestedStateTwoOrders(70000))
			{
				foundTwoOrders = false;
			}
		}

		System.out.println(foundTwoOrders);
		
		if(!foundTwoOrders)
		{
			Pause("Didn't find two orders marked requested !!!! Select OK and program will fail.");
			Assert.fail("expected orders in approval page not found.");
		}
		
		// 1. Search for first order and store row order number 
		Approvals.openOrderDetails(orderDetailsObjectExpected.orderId, orderDetailsObjectExpected.externalOrderId);
		int rowOrder1 = Approvals.rowNumContainingOrder;
		System.out.println("rowOrder1: " + rowOrder1);
		
		// 2. Search for second order and store row order number
		Approvals.openOrderDetails(orderDetailsObjectExpected.orderIdTwo, orderDetailsObjectExpected.externalOrderIdTwo);
		int rowOrder2 = Approvals.rowNumContainingOrder;
		System.out.println("rowOrder2: " + rowOrder2);
		
		// 3. Being on the list of orders click on check-boxes
		selectOrderCheckbox(rowOrder1);
		selectOrderCheckbox(rowOrder2);
		
		Pause("The two orders have been found. Select a selection in the pulldoen at bottom of this page = approve/reject");
		
		// 4. Go to the bottom of the list and select either Approve or Reject, from the drop-down list. 
		
		int[] rowNumbers = {rowOrder1, rowOrder2};
		
		if (approverAction.equals(ApproverAction.approve)){
			
			approveOrdersSelected(rowNumbers);
			
		} else if (approverAction.equals(ApproverAction.reject)) {
			
			rejectOrdersSelected(rowNumbers);
			
		}
		
		
	}

	
	public static void selectOrderCheckbox(int index) {
		
		driver.findElement(By.xpath("//tbody/tr[" + index + "]/td/span[@class='input-group-checkbox']")).click();  
		
	}
	
	
	private static void approveOrdersSelected(int[] rowNumbers) throws InterruptedException {
		
		// The 'Approve' button is not listed anymore. --> So the Approve action must be selected from a dropdown list 
		
		// 3. The action ('Approve' in this case) must be selected from a dropdown list located below 
		
		new Select(driver.findElement(By.xpath("//span[@id='sysapproval_approver_choice_actions']/select"))).selectByVisibleText("Approve");
		
		Thread.sleep(3000);
		
		// 4. Verify that the State changes to 'Approved'

		Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumbers[0] + "]/td[4]")).getText(), "Approved", "Failed. State is not Approved.");	
		Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumbers[1] + "]/td[4]")).getText(), "Approved", "Failed. State is not Approved.");
		
		// now set the order details expected status to 'In Fulfillment'
		orderDetailsObjectExpected.status = "In Fulfillment";			
		
		System.out.println("Orders have been approved.");
		
		
	}
	
	// bladd this is used by the test that deleting more than one 
	private static void rejectOrdersSelected(int[] rowNumbers) throws Exception {
		
		//
		
		List<WebElement> options = driver.findElements(By.xpath("//span[@id='sysapproval_approver_choice_actions']/select/option"));  //cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select>option")

		ShowText("---------------------------");
		
		for(WebElement ele : options)
		{
			ShowText(ele.getText());
		}
		
		ShowText("---------------------------");
		
		int indexReject = 0;
		
		// below should be the third one
		for (int i = 0; i < options.size(); i++) {
			
			if (options.get(i).getText().trim().equals("Reject")) {
				indexReject = i;
			}
			
		}
		
		System.out.println("Index for delete is " + indexReject);
		
		//driver.findElement(By.xpath("//select/option[text()='Reject']")).click(); // <-- it doesn't work, replaced by line below
		
		
		Pause("Time for pulldown.");
		
		//new Select(driver.findElement(By.cssSelector("div.custom-form-group>div>table>tbody>tr>td>span>select"))).selectByIndex(indexReject);
		///* TEST ---> */  new Select(driver.findElement(By.xpath("//span[@id='sysapproval_approver_choice_actions']/select"))).selectByIndex(indexReject);
		new Select(driver.findElement(By.xpath("//span[@id='sysapproval_approver_choice_actions']/select"))).selectByVisibleText("Reject");
		
		Pause("Result?");
		
		
		Thread.sleep(3000);
		
		// 4. Verify that the State changes to 'Rejected'
		
		Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumbers[0] + "]/td[4]")).getText(), "Rejected", "Failed. State is not Rejected.");	
		Assert.assertEquals(driver.findElement(By.xpath("//tbody[@class='list2_body']/tr[" + rowNumbers[1] + "]/td[4]")).getText(), "Rejected", "Failed. State is not Rejected.");
		
		// now set the order details expected status to 'Approval Rejected'
		orderDetailsObjectExpected.status = "Approval Rejected";				
		
		System.out.println("Order has been rejected.");
		
		
	}
	
		
	
}
