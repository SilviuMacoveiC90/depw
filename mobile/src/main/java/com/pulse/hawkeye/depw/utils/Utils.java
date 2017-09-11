package com.pulse.hawkeye.depw.utils;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;


import com.pulse.hawkeye.depw.core.data.IJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static final String TAG = "Utils";

	public static boolean DEBUG = false;

    public static String buildStringFromStringListWithSeparator(char separator, Object... args)
    {

    	if (null == args)
    	{
    		return "";
    	}
    		
    	int length = args.length;
    	if (args.length == 0)
    	{
    		return "";
    	}
    	
     	String out="";      	
    	for (int i=0; i< length; i++)
    	{
    		if (i > 0)
    		{
    			out += separator;
    		}
    		out += args[i].toString();
    	}
    	
    	
    	return out;
    }

    public static String convertBufferToString(InputStream inputStream) throws IOException
    {
    	
        InputStreamReader iStreamReader = new InputStreamReader(inputStream);
        BufferedReader bReader = new BufferedReader(iStreamReader);
        String line = null;
        StringBuilder builder = new StringBuilder();
        while((line = bReader.readLine()) != null) { 
            builder.append(line);
        }
        bReader.close();     
        iStreamReader.close();
        
//        byte[] buffer = new byte[4096];
//        String sData = "";
//        while(bInput.available() > 0) 
//        { 
//        	int read = bInput.read(buffer);
//        	sData += (new String(buffer, 0, read));
//        }
//        
        return builder.toString();
    }
    
    public static byte[] convertBufferToByteArray(InputStream inputStream) throws IOException
    {  	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	
        byte[] buffer = new byte[4096];

		int read = 0;
		while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
			baos.write(buffer, 0, read);
		}
		buffer = null;
		baos.flush();
		byte[] ret = baos.toByteArray();
		baos.close();

        return  ret;
    }
    
    
    
    
    
    public static int getJSONAsInt(JSONObject jsonObj, String sTag)
    {
    	int data = 0;
    	try 
    	{
			data = jsonObj.getInt(sTag);
		} 
    	catch (Exception e) {
			if (DEBUG) e.printStackTrace();
		}
        
        return data;
    }
    
    public static void setJSONAsLong(JSONObject jsonObj, String sTag, long value)
    {
    	try 
    	{

    			jsonObj.put(sTag,value);   		
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    
    public static long getJSONAsLong(JSONObject jsonObj, String sTag)
    {
    	long data = 0;
    	try 
    	{
			data = jsonObj.getLong(sTag);
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
        
        return data;
    }
    
    public static void setJSONAsInt(JSONObject jsonObj, String sTag, int value)
    {
    	try 
    	{

    			jsonObj.put(sTag,value);   		
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    
    
    public static double getJSONAsDouble(JSONObject jsonObj, String sTag)
    {
    	double data = 0;
    	try 
    	{
			data = jsonObj.getDouble(sTag);
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
        
        return data;
    }
    
    public static void setJSONAsDouble(JSONObject jsonObj, String sTag, double value)
    {
    	try 
    	{
    		jsonObj.put(sTag,value);   		
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    
    public static boolean getJSONAsBool(JSONObject jsonObj, String sTag)
    {
    	boolean data = false;
    	try 
    	{
			data = jsonObj.getBoolean(sTag);
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
        
        return data;
    }
    
    public static void setJSONAsBool(JSONObject jsonObj, String sTag, boolean value)
    {
    	try 
    	{

    			jsonObj.put(sTag,value);   		
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    
    
    public static String getJSONAsString(JSONObject jsonObj, String sTag)
    {
    	if (sTag == null)
    	{
    		return jsonObj.toString();
    	}
    	
    	String data = "";
    	try 
    	{
			data = jsonObj.getString(sTag);
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
        
        return data.trim();
    }
    
    public static void setJSONAsString(JSONObject jsonObj, String sTag, String value)
    {
    	if (sTag == null)
    	{
    		return;
    	}
    	
    	try 
    	{
    		if (null != value)
    		{
    			jsonObj.put(sTag,value);
    		}
    		else
    		{
    			jsonObj.put(sTag,"");	
    		}
   		
		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    
    public static void putJSONArrayItem(JSONArray jsonArray, IJsonParser elem, String tag)
    {
    	try 
    	{
    		if (null != elem)
    		{
    			JSONObject jsonItem = new JSONObject();
    			elem.toJson(jsonItem, tag);
    			jsonArray.put(jsonItem);
    		}

		} 
    	catch (Exception e) {
    		if (DEBUG) e.printStackTrace();
		}
    }
    

	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */			
    public static String formatLink(String sText)
    {
	
    	String cleantext = sText.replaceAll("http://", "");
    	cleantext = cleantext.replaceAll(".com", "C");
    	cleantext = cleantext.replaceAll(".jpg", "J");
    	cleantext = cleantext.replaceAll(".gif", "G");
    	cleantext = cleantext.replaceAll(".png", "P");
    	cleantext = cleantext.replaceAll(".ae", "A");  
    	String newText = "";
    	for (int i = 0; i < cleantext.length(); i++)
    	{
    	    char c = cleantext.charAt(i);        

    	    switch (c)
    	    {
    	    case  '/':
    	    case  '|':
    	    case  '\\':
    	    case  '?':
    	    case  '*':
    	    case  '>':
    	    case  '<':
    	    case  '"':
    	    case  ':':
    	    case  '+':
    	    case  '[':
    	    case  ']':
    	    case  '\'':
    	    	newText += "_";
    	    break;
    	    default:
    	    	newText += c;
    	    	break;
    	    
    	    }   	
    	}
    	return newText;
    }
    
    
    public static String createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(path);
        if (!file.exists()) 
        {
            if (!file.mkdirs()) {
                Log.e("Utils", "Problem creating  folder: " +  file.getAbsolutePath());
                return "";
            }
        }
        return file.getAbsolutePath();



    }





//
	
    public static String[] buildList(String... args)
    {
    	return args;
    }

    public static int[] buildList(int... args)
    {
    	return args;
    }

	public static String convertSecToMinutes(int valInSec, String m) {
		int minutes = valInSec / 60;
		
		String timeString = "";
		timeString = timeString + minutes + " " + m;
		return timeString;
	}
	
	public static String getDirectory(String foldername) {
//      if (!foldername.startsWith(".")) {
//          foldername = "." + foldername;
//      }
      File directory = null;
      directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
              + File.separator + foldername);
      if (!directory.exists()) {
          directory.mkdirs();
      }
      return directory.getAbsolutePath();
  }

  public static String getFileExtension(String filename) {
      String extension = "";
      try {
          extension = filename.substring(filename.lastIndexOf(".") + 1);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return extension;
  }

    public static final String FORMAT_DT_STRING = "yyyy-MM-dd HH:mm:ss";
	public static SimpleDateFormat FORMAT_DT = new SimpleDateFormat(FORMAT_DT_STRING);
	public static long stringToDate(String text, SimpleDateFormat format)
	{
		try 
		{
			Date date  = format.parse(text);	
			return date.getTime();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

    /**
     * method that will convert the passed string to milliseconds using the default format
     * {@value Utils#FORMAT_DT_STRING}
     * @param text the text to convert
     * @return the value in milliseconds ot 0 in case or error
     */
    public static long stringToDate(String text){
        return stringToDate(text, FORMAT_DT);
    }
	
	public static String dateToString(long dateTime, SimpleDateFormat format)
	{
		try 
		{
			Date date = new Date(dateTime);
			return format.format(date);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	
	public static boolean isPointWithinView(int screenX, int screenY, View view) {
		
		if (view == null)
		{
			return false;
		}
		
		Rect hitRect = new Rect();
		view.getLocalVisibleRect(hitRect);
		int width = hitRect.right - hitRect.left;
		int height = hitRect.bottom - hitRect.top;
		int hiddenDX = view.getLeft() < 0 ? -view.getLeft() : 0 ;
		int hiddenDY = view.getTop() < 0 ? -view.getTop() : 0 ;
		
		if (height==0 || width == 0)
		{
			return false;
		}
		

        int[] childPosition = new int[2];
        view.getLocationOnScreen(childPosition);
        int left = childPosition[0] + hiddenDX;
        int right = left + width;
        
        int top = childPosition[1] + hiddenDY;
        int bottom = top + height;
        
		Rect viewScreenRect = new Rect(left, top, right, bottom);
        
        return viewScreenRect.contains(screenX, screenY);
    }
	
	
	public static int roundToInt(double value)
	{
		if (value - Math.floor(value) >=0.5) 
		{ 
			return (int)Math.ceil(value); 
		}

		return (int)Math.floor(value); 
		
	}
	
	public static void getViewRectRelToOther(View parent, View child, Rect rect)
	{
		
		int[] parentAbsCoord =  {0,0};
		int[] childAbsCoord =  {0,0};
		
		parent.getLocationOnScreen(parentAbsCoord);
		child.getLocationOnScreen(childAbsCoord);
		
		rect.left = childAbsCoord[0] - parentAbsCoord[0];
		rect.top = childAbsCoord[1] - parentAbsCoord[1];	
		rect.right = rect.left + child.getWidth();
		rect.bottom = rect.top + child.getHeight();
	}
	
	public static void setViewBoundsInRelLayout(View view, Rect rect)
	{
		if (null == view)
		{
			return;
		}
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rect.width(), rect.height());	
		params.setMargins( rect.left,rect.top,0,0);
		view.setLayoutParams(params);
	}
	
	public static String decodeUrl(String url)
	{
		try 
		{
			return URLDecoder.decode(url, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		return url;
	}
	
	public static String printRect(Rect rect)
	{
		return "("+rect.left + ","+rect.top + "-("+rect.right + ","+rect.bottom+")  W=" + rect.width() + " H="+rect.height();
	}
	
	public static boolean isEmpty(String text)
	{
		if (null == text)
		{
			return true;
		}

		return text.isEmpty();

	}
	
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	public static void clickView(final View view, boolean post) 
	{
		if (post)
		{
		new Handler(Looper.getMainLooper()).post(() -> view.performClick());
		}
		else
		{
			view.performClick();
		}
	}
	
	public static Date getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		Date date = new Date(dateFormat.format(cal.getTime()));
		return date;
	}
	
	public static void deleteFolder(String path)
	{
	    File file = new File(path);

	    if (file.exists()) {
	        String deleteCmd = "rm -r " + path;
	        Runtime runtime = Runtime.getRuntime();
	        try {
	            runtime.exec(deleteCmd);
	        } catch (IOException e) { }
	    }
	}
	
	
	public static void deleteRecursive(File dir)
	{
	    Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
	    if (dir.isDirectory())
	    {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++)
	        {
	            File temp = new File(dir, children[i]);
	            if (temp.isDirectory())
	            {
	                Log.d("DeleteRecursive", "Recursive Call" + temp.getPath());
	                deleteRecursive(temp);
	            }
	            else
	            {
	                Log.d("DeleteRecursive", "Delete File" + temp.getPath());
	                boolean b = temp.delete();
	                if (b == false)
	                {
	                    Log.d("DeleteRecursive", "DELETE FAIL");
	                }
	            }
	        }

	    }
	    dir.delete();
	}
	



	
//	public static void clearMemory()
//	{
//		CustomObject.removeAllData();
//        ItemList.NextTimeToUpdate = 0;
//	}
	
	/**
	 * @desc will convert dp in pixels
	 * @param dp
	 * @return the pixel equivalent for the passed dp number
	 */
	public static float dpToPx(float dp)
	{
//	    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
		float px = (float) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP,
		        dp, 
		        Resources.getSystem().getDisplayMetrics()
		);
		return px;
	}

	/**
	 * @desc will convert pixels in dp
	 * @param px
	 * @return	dp equivalent for the passed pixel number
	 */
	public static int pxToDp(int px)
	{
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}
	
	
	/**
	 * @desc will check if the device has software or hardware navigation keys
	 * @return true if device has hardware navigation keys
	 */
	public static boolean hasHwKeys(){
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

		// no navigation bar, unless it is enabled in the settings
// 99% sure there's a navigation bar
		return hasBackKey && hasHomeKey;
	}


    /**
     * Thread that will test the internet conenctivity and will return it's status with
     * {@link InternetTestThread#haveInternet()}
     */
    private static class InternetTestThread extends Thread{

        private Boolean  mHaveInternet = false;
        public Boolean haveInternet(){return mHaveInternet;}
        @Override
        public void run() {
            try {
                Log.d(TAG, "::isInternetAvailable");
                InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

                if (ipAddr.equals("")) {
                    Log.d(TAG, "::isInternetAvailable no internet");

                } else {

                    Log.d(TAG, "::isInternetAvailable have internet");
                    mHaveInternet = true;
                }

            } catch (Exception e) {
                Log.d(TAG, "::isInternetAvailable exception caught");
                e.printStackTrace();
            }
        }
    }
    /**
     * will check if google.com can be reached
     * The internet conductivity will be tested on a parallel thread and the current
     * thread will wait for joining with it.
     * This is done to avoid {@link NetworkOnMainThreadException}
     * @return true if google.com can be reached
     */
    public static boolean isInternetAvailable() {
        /*will test the internet conductivity on a parallel thread and wait for it to finish*/

        InternetTestThread t = new InternetTestThread();
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t.haveInternet();


    }


    /**
     * method that will adjust the received time stamp with the timezone.
     * @param timeStamp the timestamp in "yyyy-MM-dd'T'HH:mm:ss" format
     * @return the adjusted timestamp
     */
    public static String adjustWithTimeZone(String timeStamp){
        String newTimeStamp = timeStamp;
        String FORMAT_DT_STRING = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DT_STRING);
        format.setTimeZone(TimeZone.getDefault());
        Date date;
        try
        {
            date  = format.parse(timeStamp);
            date.setTime(date.getTime() + TimeZone.getDefault().getOffset(date.getTime()));
            newTimeStamp = format.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTimeStamp;
    }

    /**
     * adjusting the passed timestamp in milliseconds with TimeZone
     * @param timeStamp - the timestamp in milliseconds that need adjusting
     * @return the adjusted timestamp
     */
    public static long adjustWithTimeZone( long timeStamp ){
        return timeStamp + TimeZone.getDefault().getOffset(timeStamp);
    }



	
}
