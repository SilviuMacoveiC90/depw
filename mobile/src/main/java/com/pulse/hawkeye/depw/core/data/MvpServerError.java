package com.pulse.hawkeye.depw.core.data;


import com.pulse.hawkeye.depw.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;


public class MvpServerError extends MvpError implements IJsonParser{
	

	public static final String TAG_RESPONSECODE = "responseCode";
	public static final String TAG_TITLE = "title";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_CORRELATIONID = "correlationId";
	public static final String TAG_MAXDEVICES= "maxDevices";

	
	public enum ErrorType {Http, Parser, System, Processing}

	private int responseCode = -1;
	private String title;
	private String description;
	private String correlationId;
	private int maxDevices;
	private String jsonData;
	

	public MvpServerError(int responseCode, String description) 
	{
		super(MvpError.ErrorType.Processing);
	
		this.responseCode = responseCode;
		this.description = description;
	}
	
	public MvpServerError(HttpURLConnection connection) 
	{
		super(MvpError.ErrorType.Http);
		
		try 
		{
			responseCode = connection.getResponseCode();
			title = connection.getResponseMessage();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if (responseCode == -1)
		{
//			description = MainApp.getRes().getString(R.string.internet_no_connection);
//			if (Utils.isEmpty(title))
//			{
//				title = description;
//			}
		}
	}
	
	public MvpServerError(String jsonData) 
	{
		super(MvpError.ErrorType.Http);
		this.jsonData = jsonData;

		responseCode = 404;
		title = "";
		
		if (jsonData == null)
		{

			return;
		}
		
		if (false == jsonData.contains("<html"))
		{
			checkAndDecodeJson();
		}
	}	
	
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	
	public int getMaxDevices() {
		return maxDevices;
	}
	public void setMaxDevices(int maxDevices) {
		this.maxDevices = maxDevices;

	}
	
	public void checkAndDecodeJson()
	{		
		if (jsonData.isEmpty())
		{
			responseCode = 408;
			return;
		}
		
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jsonData);
	
			fromJson(jsonObj, null);
	
		} catch (JSONException e) {

			e.printStackTrace();
		}
		 catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
	}


	@Override
	public void fromJson(JSONObject jsonObj, String tag) throws JSONException,
			InstantiationException, IllegalAccessException {
		
		responseCode = Utils.getJSONAsInt(jsonObj, TAG_RESPONSECODE);
		title = Utils.getJSONAsString(jsonObj,TAG_TITLE);
		description = Utils.getJSONAsString(jsonObj,TAG_DESCRIPTION);	
		correlationId = Utils.getJSONAsString(jsonObj,TAG_CORRELATIONID);	
		maxDevices = Utils.getJSONAsInt(jsonObj, TAG_MAXDEVICES);		
	}


	@Override
	public void toJson(JSONObject jsonObj, String tag) throws JSONException,
			InstantiationException, IllegalAccessException {

		Utils.setJSONAsInt(jsonObj, TAG_RESPONSECODE, responseCode);
		Utils.setJSONAsString(jsonObj, TAG_TITLE, title);
		Utils.setJSONAsString(jsonObj, TAG_DESCRIPTION, description);
		Utils.setJSONAsString(jsonObj, TAG_DESCRIPTION, correlationId);	
		Utils.setJSONAsInt(jsonObj, TAG_RESPONSECODE, maxDevices);
		
	}


	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	@Override
	public String toJsonString() {
		
		JSONObject jsonObj = new JSONObject();
		
		try 
		{
			toJson(jsonObj, null);
			
		} catch (Exception e) {
			
			return "{error}";
		}
		
		return jsonObj.toString();
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	@Override
	public String toString() {

		return super.toString() + " \n " + jsonData;
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	@Override
	public String displayError() 
	{
		int code = getResponseCode();


		if (code <= 0 || code == 408)
		{
			return "An error has occured";
		}
		
		if (getDescription() != null)
		{
			return getDescription();
		}

		return super.displayError();
	}
}
