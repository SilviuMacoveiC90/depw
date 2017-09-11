package com.pulse.hawkeye.depw.core.common;

import com.pulse.hawkeye.depw.utils.IConst;
import com.pulse.hawkeye.depw.utils.Log;

public class TaskJsonRequest extends TaskHttpRequest {
	public static boolean DEBUG = Log.DEBUG;
	public static final String TAG = TaskJsonRequest.class.getSimpleName();	
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	public TaskJsonRequest(String sURL,
			HttpReqParams httpParams) 
	{
		super(IConst.HTTP_GET, sURL, httpParams, DataType.String);
		
		if (DEBUG) Log.m(TAG,this,"ctor()");
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	public TaskJsonRequest(ITaskReturn callback, String sURL,
			HttpReqParams httpParams) 
	{
		super(callback, IConst.HTTP_GET, sURL, httpParams, DataType.String);
		
		if (DEBUG) Log.m(TAG,this,"ctor()");
	}
}
