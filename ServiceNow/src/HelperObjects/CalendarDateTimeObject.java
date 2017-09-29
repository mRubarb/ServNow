package HelperObjects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarDateTimeObject 
{
	public static String year = ""; 	
	public static String month = "";	
	
	static String [] strArray;
	static String yearMonthDay;
	static String [] strArrayForTesting;
	
	// use as static. this gets the year and month separated by a space.
	static public void StoreYearMonth(String strIn)
	{
		year = strIn.split(" ")[0];
		month = strIn.split(" ")[1];		
	}
	
	// not in use - selecting day consistently is not easy.   
	static public String DateFormattedForVerifyOrder(String dateSelected) 
	{
		return month + " " + dateSelected.substring(1) + "nd " + year;
	}
	
	// this verifies the full date passed in contains the month and year stored in this object.
	static public boolean VerifyMonthAndYear(String fullDate) 
	{
		return fullDate.contains(month) && fullDate.contains(year);
	}

	// this received the preferred suspension date in the format found in the approval page and 
	// verifies it. format is 2016-09-13 00:00:00
	static public boolean VerifyPreferredSuspendionDateForApproval(String str) throws ParseException
	{
		strArray= str.split(" "); // get items in input separated by a space into two element array. 
		yearMonthDay = strArray[0]; // get the first one with year, month, date.
		strArrayForTesting = yearMonthDay.split("-"); // put into three elements.
		
		// send result for comparing year and month.
		return strArrayForTesting[0].equals(year) && strArrayForTesting[1].equals("0" + Integer.toString(GetMonthInt(month)));
	}
	
	// return the integer value of the month string (i.e. 'august') sent in.  
	static public int  GetMonthInt(String month) throws ParseException
	{
	    Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int intMonth = cal.get(Calendar.MONTH);
	    return (intMonth + 1);
	}	
	
	public static void Show()
	{
		System.out.println("Year: " + year);
		System.out.println("Month: " + month);		
	}
	
	// this returns the current date in a format that 
	// matches the format in the order details page.
	public static String BuildDateForHistory()
	{
		String [] strArray;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		strArray = dateFormat.format(date).split("-"); // put each part of the complete date into an array. 
		
		strArray[1] = GetMonth();
		strArray[1] = strArray[1].substring(0,3); // trim month
		strArray[2] = strArray[2].substring(2,4); // trim year		
		return strArray[0] + "-" + 	strArray[1] + "-" + strArray[2]; // return final string.
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
}
