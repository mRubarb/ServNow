package ServiceNow;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import HelperObjects.AccessoriesDetailsExpected;
import HelperObjects.DeviceInfoActions;
import HelperObjects.Feature;
import HelperObjects.FeatureShoppingCart;
import HelperObjects.OrderDetailsObjectActual;
import HelperObjects.OrderDetailsObjectExpected;
import HelperObjects.PlanInfoActions;
import HelperObjects.PlanOptionalFeatures;


public class BaseClass
{
	public static WebDriver driver;
	public static String baseUrl;
	//public static String CMD_baseUrl = "https://commcareqa.tangoe.com/manage/login";
	//public static String CMD_baseUrl = "http://dc1qacmdweb04:8080/manage/login/login.trq";
	public static String CMD_baseUrl = "https://qa1cmd.tangoe.com/manage/login/login.trq";
	
	public static boolean testCaseStatus = false;
	
	
	// these are values used throughout testing.
	public static String deviceSelectedForAllPages = "Apple iPhone 5 (16GB) - Black"; // this is the device selected in the device page class that is used in all pages after the device page.
	public static String deviceVendorSelectedForAllPages = "Verizon Wireless"; // this is the vendor selected in the device page class that will go with 'deviceSelectedForAllPages' above..
	public static String planSelectedForAllPages = "Nationwide for Business Talk Share Plan"; // this is the plan selected in the plan class that is used in all pages after the device page.
	
	// Index for device selected when doing New Activation
	// This index will be incremented and used to select a new device on New Activation when the device selected has no plans
	// Added by Ana 9/8/17
	public static int indexDeviceSelectedNewActivation = 4;
	// Added by Ana 9/11/17
	public static boolean isFirstPass = true; // this is needed in the case that the selected device has no plans. In that case, when test goes to the Choose Device
											  // page, the will be one less 'Add to Cart' button, and the 'Add to Cart' button clicked won't match with the desired
											  // device. This boolean variable will be used that there's one less button, to use the appropriate index for 
											  // 'Add to Cart' button.
	
	public static PlanInfoActions planInfoActions; // this is for storing the plan that is selected for testing, info related to it, and methods to manipulate the info.  
	public static DeviceInfoActions deviceInfoActions; // this is for storing the device that is selected for testing, info related to it, and methods to manipulate the info.
	
	// these are timeouts.
	public static int ExtremeTimeout = 120;
	public static int MainTimeout = 45;
	public static int MediumTimeout = 15;	
	public static int ShortTimeout = 5;
	public static int MiniTimeout = 3;	
	public static int TinyTimeout = 2;
	public static int TeenyTinyTimeout = 1;	
	
	// main text that shows up in many pages that have error messages. 
	public static String mainErrorMessage = "//span[text()='Please fix the following validation errors:']";
	
	// these are for the order details/verify pages.
	public static String userCountry = "United States";
	public static String userName = "Bob Lichtenfels";
	public static String userLastName = "Limited"; // "Lichtenfels";
	public static String userFirstName = "Bob";	
	public static String userDepartmentName = "Development";  //"124";	
	public static String userLineOne = "12 Main St"; //"9847 Main";
	public static String userCity = "Boston";	
	public static String userState = "Massachusetts";
	public static String userStateShort = "MA";
	public static String userPostalCode = "06125";  //"02334";
	public static String addressLineOne = "12 Main St";  // "40 Main";	
	public static String addressLineTwoDummyInfo = "LineTwoDummyInfo";
	public static String addressLineThreeDummyInfo = "LineThreeDummyInfo";
	public static String preferredAreaCode = "781";
	public static String contactNumber = "7812739306";	
	public static String serviceNumber = ""; // this is queried for in MyDevicesPage.	
	public static String fullServiceNumber = ""; // this is queried for in MyDevicesPage.
	public static String reasonAddDeviceAndService =  "Broken Device";
	public static String businessUnit =  "Unit1";
	public static String reasonAction = "Cancel service as employee is no longer with the company";
	public static String additionalInstructions = "Test Additional Instructions";	
	public static String userStateOrderDetails = "MA";
	public static String orderNewDeviceAndServiceOrderType = "Activate New Service";
	public static String orderNewDeviceAndServiceOrderStatus = "Awaiting Approval";
	public static String approverBob = "Bob Lichtenfels-Approver";	
	public static String approverAna = "Ana Pace (Approver)";	
	public static String approverAdmin = "SN 2 Admin";	
	public static String approverAdminMail = "sn2.admin.xx1@example.com";	
	public static String approverServiceNow = "ServiceNow Admin Certifier";	
	public static int orderBlockIndex = 0; // this is for holding a block location in 'my orders' page and is changed in various locations.
	public static ApproverAction approverAction; // this is set in test cases (at the very top) to indicate the type of approval. 
	public static ApprovalActionType approvalActionType = ApprovalActionType.none; // this is set in test cases (at the very top) to indicate the type of order action that will have it's content verified in the approval page.
	
	//approverAction
	
	public static String addressLineOneOrderActions = "12 Main St";  //"40 Main";	
	public static String cityOrderActions = "Boston";	
	public static String stateOrderActions = "MA";	
	public static String zipCodeOrderActions = "06125";  //"06234";	
	public static String orderDetailTangoeOrderStatus  = "Awaiting Approval";
	public static String extension = "781";
	
	// command variables
	public static String commandUserName = "tester.xx1";  // "bob.lichtenfels@tangoe.com XX1";
	public static String commandPassword = "tngo777";  //  "Jnupp@7071";	
	public static String commandURL = "https://qa1cmd.tangoe.com/manage/login/login.trq";
	
	// service now
	public static String serviceNowURL = "https://ven01270.service-now.com/";	
	public static UserLoginMode userLoginMode;
	
	
	// variables for actions and approvals
	public static String userLimitedFullName = "Bob Lichtenfels (Limited)";
	public static String userLimitedShorterName = "Bob Limited";
	public static String userApproverShorterName = "Bob Approver";
	public static String userLimitedFullNameOrderHistory = "Bob Lichtenfels (Limited) (Development),";
	public static String userLimitedShorterNameOrderHistory = "Bob Limited (Development),";
	public static String userLimitedFullNameExtended = "bob.lichtenfels.xx1.limited";
	public static String limitedUserPulldownSelection = "In pool for " + userLimitedShorterName; // this is for a pull down selection.
	public static String expeditedOrderText = "This order will be expedited. (Additional charges may apply.)"; // text for expedited order selected.
	
	public static JFrame frame;	
	
	// ////////////////////////////////////////////////////////
	// 			user names and passwords for SN.
	// ////////////////////////////////////////////////////////
	public static String tcAllSN_Password = "synergy#1";
	//public static String tcAllSN_Password = "tngo111";	
	
	//Admin_User
	public static String admin_userName = "bob.lichtenfels.xx1";
	// public static String admin_userName = "bob.lichtenfels.xx1.limited";	
	
	//TC0002 User
	//public static String tc0002SN_UserName = "rob.qa_tc0002";
	//public static String tc0002SN_UID = "rob.qa_tc0002";

	
	// logins for SN
	public static String userAdmin = "sn.cert.admin"; // jnupp
	public static String userAdminPasswd = "C3rt1fy!!"; // jnupp
	public static String userLimited  = "bob.lichtenfels.xx1.limited";
	public static String userLimitedPasswd = "tngo111";
	public static String userApprover = "bob.lichtenfels.xx1.approver"; 
	public static String userApproverPasswd = "tngo111";
	
	// xpaths.
	public static String shopCartDev = "//div[@data-qa='Device']";
	public static String shopCartPlan = "//div[@data-qa='Plan']";
	public static String shopCartPlanFeatures = "//div[@data-qa='Plan Features']";
	public static String shopCartAccessories = "//div[@data-qa='Accessories']";	
	public static String shopCartCount = "//h3[text()='Shopping Cart']/following-sibling ::div"; // use this to see how many sections in shopping cart.	
	//public static String chooseActionsPullDownServices  = "//select[@ng-model='device.selectedAction']"; // // this is the pull down on 'my devices' page.  	
	
	public static int indexMyDevices = 2;
	
	// Number '2' indicates the second device listed 
	public static String chooseActionsPullDownServices  = "//div/div[" + indexMyDevices + "]/div/select[@ng-model='device.selectedAction']"; // // this is the pull down on 'my devices' page.
	
	
	// these are for accessing current project folder.
	static File currentDirectory; 
	static String projectPath = ""; 
	
	// this holds a list of optional features objects.
	public static ArrayList<PlanOptionalFeatures> optionalFeaturesList = new ArrayList<PlanOptionalFeatures>();
	
	// this list holds objects of accessories that are found in the first page of the accessories list. 
	// each object has name, vendor, and cost. this list is the expected values for when accessories are added to the shopping cart. 
	// it is populated when the accessory page is opened.
	public static ArrayList<AccessoriesDetailsExpected> accessoriesDetailsListExpected = new ArrayList<AccessoriesDetailsExpected>();
	
	// this list holds objects of features found on the features page. 
	public static ArrayList<Feature> featuresList = new ArrayList<Feature>();

	// this list holds objects of features found in the shopping. 
	public static ArrayList<FeatureShoppingCart> featuresShoppingCartList = new ArrayList<FeatureShoppingCart>();

	// object that holds order details expected info.
	public static OrderDetailsObjectExpected orderDetailsObjectExpected;

	// object that holds order details actual info.
	public static OrderDetailsObjectActual orderDetailsObjectActual;

	// ana add - To store the optional features added to plan
	public static List<String> selectedOptionalFeatures;
	
	// ana add - 9/14/17 - To store the accessories added to shopping cart
	// A HashMap is used since the order in which the accessories are listed on the 'Verify Order' page 
	// is not always the same as the order in which they are displayed on 'Choose Accessories' page
	public static HashMap<String, AccessoriesDetailsExpected> accessoriesInCartHashMap  = new HashMap<String, AccessoriesDetailsExpected>(); 
 
	
	public enum Action 
	{
		Add,
		Remove
	}
	
	public enum DriverSetup 
	{
		ServiceNow,
		Command
	}
	
	// this is for different user log ins.  
	public enum UserLoginMode 
	{
		Admin,
		Limited,
		Approver
	}
	
	// this is for indicating what type of approval is being done. 
	public static enum ApproverAction
	{
		approve,
		reject,
		none
	}
	
	// this is for indicating what type of action is being done. used in approval page object.
	public static enum ApprovalActionType
	{
		deactivate,
		suspend,
		unsuspend,
		swapDevice,
		orderAccessories,
		upgradeDevice,
		updateFeatures,
		portNumber,
		none
	}	
	
	// this is for indicating what type of action is being done. used in approval page object.
	public static enum UpdateFeatureActions
	{
		add,
		remove,
		noChange		
	}	

	// ctor
	public BaseClass()  
	{
		System.out.println("BASE CLASS CONSTRUCTOR...");
		planInfoActions = new PlanInfoActions(planSelectedForAllPages); // setup plan name 
		deviceInfoActions  = new DeviceInfoActions(); // new DeviceInfoActions(deviceSelectedForAllPages, deviceVendorSelectedForAllPages);
		currentDirectory = new File(".");
		projectPath = currentDirectory.getAbsolutePath();
		approverAction = ApproverAction.none; 
		selectedOptionalFeatures = new ArrayList<String>();
		
	}
	
	public static void WaitForNonAdminMainPageLoad() throws Exception
	{
		// this waits for main page (page after login) to load.
		WaitForElementVisible(By.xpath("//td[text()='My Incidents by State']"), MainTimeout);
		WaitForElementVisible(By.xpath("//a[text()='Item Designer Category Request']"), MainTimeout);
		WaitForElementVisible(By.xpath("//a[text()='Request Developer Project Equipment']"), MainTimeout);
		WaitForElementVisible(By.xpath("//a[text()='Password Reset']"), MainTimeout);		
	}
	
	public static void WaitForAdminMainPageLoad() throws Exception // jnupp
	{
		// this waits for admin page (page after login) to load.
		WaitForElementVisible(By.xpath("//h2[contains(text(),'User Interface')]"), MainTimeout);
		WaitForElementVisible(By.xpath("//h2[contains(text(),'User Administration')]"), MainTimeout);
		WaitForElementVisible(By.xpath("//h2[contains(text(),'Reporting and Analytics')]"), MainTimeout);
		WaitForElementClickable(By.cssSelector(".pointerhand.icon-stop-watch.btn.btn-icon"), MediumTimeout, "");
	}	
	
	public static void ShowArray(String [] strArray)
	{
		System.out.println("Show Array ------------------------");
		for(int x = 0; x < strArray.length; x++)
		{
			System.out.println(x + " " + strArray[x]);
		}
	}
	
	
	
	public static void CreateOrderDetailsExpectedObject() 
	{
		orderDetailsObjectExpected = new OrderDetailsObjectExpected(userDepartmentName, orderNewDeviceAndServiceOrderStatus); 
		//orderDetailsObjectExpected = new OrderDetailsObjectExpected(userDepartmentName, orderNewDeviceAndServiceOrderType, orderNewDeviceAndServiceOrderStatus);
	}		
	
	public static void CreateOrderDetailsActualObject() 
	{
		orderDetailsObjectActual = new OrderDetailsObjectActual(); 
	}		
	
	public static void CompareActualDetailsObjects()
	{
		String errorMessage = "Compare of actual and expected order details objects failed in BaseClass.CompareActualDetailsObjects";
		Assert.assertEquals(orderDetailsObjectActual.orderId, orderDetailsObjectExpected.orderId, errorMessage);
		Assert.assertEquals(orderDetailsObjectActual.externalOrderId, orderDetailsObjectExpected.externalOrderId, errorMessage);		
		Assert.assertEquals(orderDetailsObjectActual.departmentName, orderDetailsObjectExpected.departmentName, errorMessage);
		Assert.assertEquals(orderDetailsObjectActual.orderType, orderDetailsObjectExpected.orderType, errorMessage);
		Assert.assertEquals(orderDetailsObjectActual.status, orderDetailsObjectExpected.status, errorMessage);		
	}
	
	public static void SetImplicitWait(int waitTime)
	{
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}
	
	// this takes in a string that is a running total called 'runningTotal' and a string that is 
	// a value to add or subtract to/from the running total called 'valueToChange'.
	// 1) create doubles that hold the running total and the value to add.
	// 2) setup a decimal format.
	// 3) add running total and value to add.
	// 4) return string that has new running total
	public static String GetNewTotal(String runningTotal, String valueToChange, Action actionType)
	{
		// convert to double and create a decimal format.
		double tempTotalIn  = Double.valueOf(runningTotal).doubleValue();
        double tempValueForAction  = Double.valueOf(valueToChange).doubleValue(); 
	    DecimalFormat decFormat = new DecimalFormat("#.00");
        
        // add value to add sent in to running total and return.
	    if(actionType == Action.Add)
	    {
	    	tempTotalIn += tempValueForAction;
	    }
	    else
	    {	
	    	tempTotalIn -= tempValueForAction;	    
	    }

	    return decFormat.format(tempTotalIn);
	}		

	
	
	// this is method that takes a decimal cost (2344.45) and converts it to full money text ($2,344.45).  
	public static String CostMonthlyCalculatedConvertToFullText(String costMonthlyTotal)
	{
		
		StringBuilder finalValue = new StringBuilder(); // this is the string that gets created. it is returned. 
		//System.out.println("costMonthlyTotal: " + costMonthlyTotal); // DEBUG.
		
		int threeCntr = 1;
		
		// trim the monthly total into two parts with dollar part in element 0.  
		String [] tempArray = costMonthlyTotal.split("\\.");
		
				
		// loop through the dollar part from right to left. starting at end, build the string to be returned with inserted "," where needed.   
		for(int x = tempArray[0].length(); x > 0; x--)
		{
			finalValue.insert(0,tempArray[0].charAt(x - 1));
			
			if(threeCntr == 3 && x != 1) // insert "," if the x counter is not at the first number in the dollar part.
			{
				finalValue.insert(0,",");
				threeCntr = 0;
			}
			threeCntr++;	
		}

		finalValue.append("." + tempArray[1]); // add the cents part to end 
		finalValue.insert(0, "$"); // add dollar sign to front.
		
		return finalValue.toString();
	}		
	
	public static void setUpDriver(DriverSetup setup) throws Exception
	{
		// driver = new FirefoxDriver();

		System.setProperty("webdriver.chrome.driver", projectPath + "\\chromedriver.exe");
		//System.setProperty("webdriver.ie.driver", path + "\\IEDriverServer.exe");	    
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		options.addArguments("disable-infobars");  // <-- Line added by Ana. Needed because with the chromedriver 2.28, there's an info bar that we don't want to have when browser is launched
		driver = new ChromeDriver(options);		
	    // driver = new ChromeDriver();
		
		switch(setup)
		{
			case ServiceNow:
			{
			    baseUrl = serviceNowURL;				
				break;
			}
			case Command:
			{
				baseUrl = commandURL; 
				break;
			}			
		}
	
		// maximize and configure timeouts
		driver.get(baseUrl);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	public static void loginAsAdmin()throws Exception
	{
		Frames.switchToGsftMainFrame();
		// Thread.sleep(5000);
		
		WaitForElementPresent(By.xpath("//input[@id='user_name']"), MainTimeout);
		LoginPage.fillUserNameTextBox(admin_userName);
		LoginPage.fillPasswordTextBox(tcAllSN_Password);
		LoginPage.clickLoginButton();
	}
		
	public static void ShowPopup(String message)		
	{
		JOptionPane.showMessageDialog(frame, message);
	}

	// this verifies the device and plan information in the shopping cart. 
	public static void VerifyDeviceAndPlanSections()
	{
		String [] strTempArray;
		
		// this is in the "device section" of the shopping cart. this gets the device name and cost (actual values).
		// array[0] is the device name. 
		// array[1] is the device cost in the device section. 
		strTempArray = driver.findElement(By.xpath(shopCartDev +  "/div")).getText().split("\n");
		
		// verify device name and device cost from above (actual values) with values that have been previously stored off.
		Assert.assertEquals(strTempArray[0], deviceInfoActions.name); // device
		Assert.assertEquals(strTempArray[1], deviceInfoActions.cost); // device cost		
		
		// this is in the "plan section" of the shopping cart. store these actual values.  
		// array[0] is the plan name. 
		// array[1] is the plan monthly cost in the plan section. 
		strTempArray = driver.findElement(By.xpath(shopCartPlan +  "/div")).getText().split("\n");		
		
		// verify the plan name previously stored off with the actual value.  
		Assert.assertEquals(strTempArray[0], planInfoActions.planSelectedName);
		
		// verify the plan cost monthly value previously stored off with the actual value. stored off value is the full text.
		//System.out.println("planInfoActions.planCostCompleteField: " + planInfoActions.planCostCompleteField);
		Assert.assertEquals(strTempArray[1], planInfoActions.planCostCompleteField);	
		
	}
	
	// /////////////////////////////////////////////////////////////////
	// The next two methods are need for date/time testing
	// /////////////////////////////////////////////////////////////////
	public static String BuildDateString()
	{
		LocalDateTime ldt = LocalDateTime.now();
        // System.out.println(ldt.getDayOfMonth() + " " + GetMonth() + " " + ldt.getYear());
		
		return ldt.getDayOfMonth() + " " + GetMonth() + " " + ldt.getYear();
	}
	
	// This returns the month in a string/calendar format with first char as Caps.
	public static String GetMonth()
	{
	   LocalDateTime ldt = LocalDateTime.now();
	   ldt.getMonth();
	   StringBuilder strBuilder = new StringBuilder();
	   String strMonth = ldt.getMonth().toString();
		   
	   for(int z = 0; z < strMonth.length(); z++)
	   {
		   if(z == 0)
		   {
			   strBuilder.append(strMonth.charAt(z));			   
		   }
		   else
		   {
			   strBuilder.append( strMonth.toLowerCase().charAt(z));			   
		   }
		}
		return strBuilder.toString();
	}
	
	
	// this takes a by object, a time duration in seconds, and an anticipated size to be found.
	// the by object is used in "driver.findelements(by)". the "driver.findelements(by)" 
	public static boolean WaitForElements(By by, long waitDuration, int anticipatedSize) throws Exception
	{
		long waitTime = waitDuration;
		
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis()-startTime) < waitTime *1000)
		{
			if(driver.findElements(by).size() == anticipatedSize)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void DebugTimeout(int TimeOut,  String... userInput) throws InterruptedException
	{
		if(userInput.length > 0)
		{
			System.out.println(userInput[0]);
		}
		Thread.sleep(TimeOut * 1000);
	}	
		
		public static void WaitForElementClickable(By by, int waitTime, String message)
		{
		    try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, waitTime);
		    	wait.until(ExpectedConditions.elementToBeClickable(by));
		    }
		    catch (WebDriverException e)
		    {
		        System.out.println("Error in WaitForElementClickable: " + e.getMessage());
		    	throw new WebDriverException(message);
		    }
		}	
	    
		public static boolean WaitForElementNotClickableNoThrow(By by, int waitTime, String message)
		{
		    try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, waitTime);
		    	ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)).apply(driver);
		    }
		    catch (WebDriverException e)
		    {
		    	return false;
		    }
		    return true;
		}	
		

		public static boolean WaitForElementClickableBoolean(By by, int waitTime)
		{
		    try
		    {
		    	WebDriverWait wait = new WebDriverWait(driver, waitTime);
		    	wait.until(ExpectedConditions.elementToBeClickable(by));
		    }
		    catch (WebDriverException e)
		    {
		        return false;
		    }
		    return true;
		}	
		
		
		public static boolean WaitForElementVisible(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
		        //System.out.println(e.toString());
		        throw new Exception(e.toString());
	        }	    
		    return true;
		}
		
		public static boolean WaitForElementVisibleNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
	        	return false;
	        }	    
		    return true;
		}		
		
		
		public static boolean WaitForElementNotVisibleNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
	        	return false;
	        }	    
		    return true;
		}				
		
		public static boolean WaitForElementPresent(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.presenceOfElementLocated(by));
		    }
	        catch (Exception e)
	        {
		        //System.out.println(e.toString());
		        throw new Exception(e.toString());
	        }	    
		    return true;
		}		

		public static boolean WaitForElementNotPresentNoThrow(By by, int timeOut) throws Exception 
		{
		    try
		    {
				WebDriverWait wait = new WebDriverWait(driver, timeOut);
			    wait.until(ExpectedConditions.presenceOfElementLocated(by));
			    ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(by)).apply(driver);			    
		    }
	        catch (Exception e)
	        {
		        return false;
	        	//System.out.println(e.toString());
		        //throw new Exception(e.toString());
	        }	    
		    return true;
		}		


		public static void stepComplete( String testCaseNumber, String testStepNumber)
		{
			//System.out.println(testCaseNumber + " " +testStepNumber + "");
		}
		
		// This shows a pop-up  message with added text passed in.  
		public static void Pause(String moreInfo) throws Exception // jnupp
		{
		    JOptionPane.showMessageDialog(frame, "PAUSE: " + moreInfo);
		}

		// jnupp below
		public static void ShowText(String str) 
		{
			System.out.println(str);
		}
		
		public static void ShowInt(int intIn) 
		{
			System.out.println(intIn);
		}
		// jnupp above.
		
		
		/*
	    public void WaitForUIBusyToDisappear()
	    {
	        int timeout = TestProperties.Instance.GetTimeoutSeconds();
	        WebDriverWait wait = new WebDriverWait(BrowserDriver.Instance.Driver, TimeSpan.FromSeconds(timeout));
	        wait.Until<bool>((d) =>
	        {
	            try
	            {
	                // If the find succeeds, the element exists, and
	                // we want the element to *not* exist, so we want
	                // to return true when the find throws an exception.
	                IWebElement element = d.FindElement(By.XPath("(//div[contains(@class, 'ui-busy')])[1]"));
	                return false;
	            }
	            catch (NoSuchElementException)
	            {
	                return true;
	            }
	        });
	    }	
		*/


}
