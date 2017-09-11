package com.pulse.hawkeye.depw.utils;


import android.os.Environment;


import com.pulse.hawkeye.depw.core.common.TaskHttpRequest;
import com.pulse.hawkeye.depw.core.common.TaskJsonRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Log {
	public static final int LOG_VERBOSE = 0;
	public static final int LOG_DEBUG = 1;
	public static final int LOG_INFO = 2;
	public static final int LOG_WARN = 3;
	public static final int LOG_ERROR = 4;
	public static final int LOG_ASSERT = 5;
	
	public static final int LOG_LEVEL = LOG_DEBUG;
	
	// flag to deactivate all logs
	public static boolean DEBUG = true;
	public static boolean VERBOUSE = true;
	public static boolean INFO = false;
	public static boolean IS_TESTING = false;
	public static boolean IS_RELEASE = false;
 
	public static void setLogLevels()
	{
		if (DEBUG)
		{
			TaskJsonRequest.DEBUG = false;
			TaskHttpRequest.DEBUG = true;
		}

	}

	/**
	 * will write a debug log if app in debug mode
	 * @param tag - the log tag
	 * @param message - the log message
	 */
	public static void d(String tag, String message){
		if ( DEBUG ) {
			/*will log only if in debug mode*/
			if (LOG_LEVEL <= LOG_DEBUG) {

				android.util.Log.d(tag, "[" + Thread.currentThread().getId() + "] " + message);

			}
		}
	}

	
	public static void m(String tag, Object obj, String function){
		if (LOG_LEVEL <= LOG_DEBUG)
		{
			android.util.Log.d(tag, "[" + Thread.currentThread().getId()+"] ::" + function + "\t " + obj);
		}
	}
	
	public static void e(String tag, String message){
		if (LOG_LEVEL <= LOG_ERROR){

			android.util.Log.e(tag, "[" + Thread.currentThread().getId()+"] " + message);

		}
	}
	
	public static void e(String tag, String message, Exception e){
		if (LOG_LEVEL <= LOG_ERROR){
			android.util.Log.e(tag, "[" + Thread.currentThread().getId()+"] " + message, e);
		}
	}
	
	public static void w(String tag, String message){
		if (LOG_LEVEL <= LOG_WARN){

			android.util.Log.w(tag, "[" + Thread.currentThread().getId()+"] " + message);
		}
	}
	
	public static void w(String tag, String message, Exception e){
		if (LOG_LEVEL <= LOG_WARN)
		{
			android.util.Log.w(tag, "[" + Thread.currentThread().getId()+"] " + message, e);

		}
	}
	
	public static void i(String tag, String message){
		if (LOG_LEVEL <= LOG_INFO){

			android.util.Log.i(tag, "[" + Thread.currentThread().getId()+"] " + message);

		}
	}
	
	public static void v(String tag, String message){
		if (LOG_LEVEL <= LOG_VERBOSE){

			android.util.Log.v(tag, "[" + Thread.currentThread().getId()+"] " + message);

		}
	}
	
	private static void logLongString(String tag, String longMessage){
		int maxLogSize = 1000;
	    for(int i = 0; i <= longMessage.length() / maxLogSize; i++) {
	        int start = i * maxLogSize;
	        int end = (i+1) * maxLogSize;
	        end = end > longMessage.length() ? longMessage.length() : end;
	        android.util.Log.d(tag, longMessage.substring(start, end));
	    }
	}
	
	private static void appendLog(String text)
	{ 
	   String filePath = Environment.getExternalStorageDirectory() + "/depw" /*+SettingsConstants.defaultSettings.getClass().getSimpleName()*/ + "_log.file";
	   File logFile = new File(filePath);
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      e.printStackTrace();
	   }
	}
}
