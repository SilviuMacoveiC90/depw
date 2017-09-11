package com.pulse.hawkeye.depw.core.common;

import com.pulse.hawkeye.depw.core.data.IJsonParser;
import com.pulse.hawkeye.depw.core.data.MvpError;
import com.pulse.hawkeye.depw.utils.Log;

import org.json.JSONObject;

public class TaskJsonParser extends Task {

	public static boolean DEBUG = Log.DEBUG;
	public static final String TAG = TaskJsonParser.class.getSimpleName();	


    private String mJSONTag = "";
	private String sJSON;
	private IJsonParser object;
	
	/**
	 * 
	 *
	 * @see         
	 */		
	public TaskJsonParser(IJsonParser object, String sJSON) 
	{		
		this.object = object;
		this.sJSON = sJSON;
	}
    public TaskJsonParser(IJsonParser object, String sJSON, String JSONTag)
    {
        this.object = object;
        this.sJSON = sJSON;
        mJSONTag = JSONTag;
    }

	public String getsJSON(){
		return sJSON;
	}

	
	/**
	 * 
	 *
	 * @see         
	 */		
	@Override
	public Object getReturnData()
	{		
		return object;
	}

	

	/**
	 * 
	 *
	 * @see         
	 */		
	@Override
	protected void onExecute() {
		if (DEBUG) Log.m(TAG,this,"onExecute");
		
		try 
		{


			JSONObject jsonObj = new JSONObject(sJSON);
            if ( mJSONTag.equals("") ) {
                object.fromJson(jsonObj, null);
            }else{
                object.fromJson(jsonObj, mJSONTag);
            }
		} 
		catch (Exception e) 
		{
			Log.d(TAG, "::onExecute error caught when parsing : "+sJSON);
	         e.printStackTrace();
	         setError(new MvpError(MvpError.ErrorType.Parser, e));
	    }
		
		taskCompleted();
	}
}
