package com.pulse.hawkeye.depw.core.data;

import org.json.JSONException;
import org.json.JSONObject;

public interface IJsonParser 
{
	
	/**
	 * 
	 *
	 * @see         
	 */
	void fromJson(JSONObject jsonObj, String tag) throws JSONException, InstantiationException, IllegalAccessException;
	
	/**
	 * 
	 *
	 * @see         
	 */
	void toJson(JSONObject jsonObj, String tag) throws JSONException, InstantiationException, IllegalAccessException;
	
	/**
	 * 
	 *
	 * @see         
	 */
	String  toJsonString();
}
