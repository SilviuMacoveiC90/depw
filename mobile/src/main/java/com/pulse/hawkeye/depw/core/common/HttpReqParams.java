package com.pulse.hawkeye.depw.core.common;


import com.pulse.hawkeye.depw.utils.IConst;
import com.pulse.hawkeye.depw.utils.IHttpConst;
import com.pulse.hawkeye.depw.utils.Log;
import com.pulse.hawkeye.depw.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.HttpUrl;

public class HttpReqParams implements IHttpConst {

	private Map<String, String> paramsMap = new LinkedHashMap<String, String>();
	private Map<String, String> propertiesMap = new LinkedHashMap<String, String>();
	private String postData = "";
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */			
    public static String formatValue(String sText)
    {
	
    	String newText="";
    	
        	
    	for (int i = 0; i < sText.length(); i++)
    	{
    	    char c = sText.charAt(i);        

    	    switch (c)
    	    {
    	    case  '+':
    	    case  '-':
    	    case  '&':
    	    case  '|':
    	    case  '!':
    	    case  '(':
    	    case  ')':
    	    case  '{':
    	    case  '}':
    	    case  '[':
    	    case  ']':
    	    case  '^':
    	    case  '"':
    	    case  '~':
    	    case  '*':
    	    case  '?':
    	    case  ':':
    	    case  '\\':
    	    	newText += "\\";
    	    	break;
    	    
    	    }
    	    
        	newText += c;       	
    	}
    	

    	return "\""+newText+"\"";
    }


	public boolean equals(Object obj) {
		if ( obj instanceof HttpReqParams ){
			HttpReqParams params = (HttpReqParams) obj;
			try {
				return params.asString().equals(asString());
			} catch (UnsupportedEncodingException e) {
				return false;
			}
		}
		return (this == obj);
	}
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public HttpReqParams()
	{

	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public HttpReqParams(String schema)
	{
		paramsMap.put(PARAM_SCHEMA, schema);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public HttpReqParams(String schema, String json)
	{
		paramsMap.put(PARAM_SCHEMA, schema);
		paramsMap.put(PARAM_FORM, json);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setSchema(String schema)
	{
		paramsMap.put(PARAM_SCHEMA,schema);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setParam(String paramName, String paramValue)
	{
		paramsMap.put(paramName,paramValue);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setParam(String paramName, char separator, Object... items)
	{
		String value = Utils.buildStringFromStringListWithSeparator(separator, items);
		if (value.isEmpty())
		{
			return;
		}
		paramsMap.put(paramName,value);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public Map<String, String> getParams()
	{
		return paramsMap;
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	public void setToken()
	{

	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setRange(int startRange, int endRange)
	{
		if (endRange == 0) 
		{
			return;
		}
		
		paramsMap.put(PARAM_RANGE, ""+startRange + CHAR_DASH + endRange);
		paramsMap.put(PARAM_COUNT, VALUE_TRUE);
	}
	
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setSort(String field)
	{		
		if (field.isEmpty())
		{
			return;
		}
		paramsMap.put(PARAM_SORT, field);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setFilter(String filter)
	{
		if (Utils.isEmpty(filter))
		{
			return;
		}
		
		String query = CHAR_OPEN_BRACKET + filter.toLowerCase() + CHAR_CLOSE_BRACKET;
		paramsMap.put(PARAM_QUERY, formatValue(query));
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
//	public void setThubmnailFilter(String... assets)
//	{
//		//http://feed.theplatform.com/f/d-YTLC/xCZsFmbqvzUI?schema=1.2&form=cjson&byCategories=content%3AVoD&sort=title&range=1-5&fileFields=assetTypes,url,width,height&thumbnailFilter=byAssetTypes=Poster
//
//		String value = Utils.buildStringFromStringListWithSeparator(CHAR_COMMA, assets);
//		if (value.isEmpty())
//		{
//			return;
//		}
//
//		paramsMap.put(PARAM_THUMBNAILFILTER, PARAM_BY_ASSETTYPES +CHAR_EQ + value);
//	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
//	public void setFileFields(String... fields)
//	{
//		String value = Utils.buildStringFromStringListWithSeparator(CHAR_COMMA, fields);
//		if (value.isEmpty())
//		{
//			return;
//		}
//
//		paramsMap.put(PARAM_FILEFIELDS, value);
//	}
	
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	public String asString() throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

		Iterator it = paramsMap.entrySet().iterator();
		while (it.hasNext())
		{
			result.append((first) ? IHttpConst.CHAR_QUESTION : IHttpConst.CHAR_AND);

			Map.Entry<String,String> paramName = (Map.Entry<String,String>) it.next();

			if (! Utils.isEmpty(paramName.getKey()))
			{
				result.append(URLEncoder.encode(paramName.getKey(), "UTF-8"));
				if(null == paramName.getValue())
				{
					return "";
				}

				if(!paramName.getValue().equals(IConst.PARAM_NO_VALUE))
				{
					result.append("=");
				}
				else
				{
					paramName.setValue(IConst.EMPTY_STRING);
				}
			}
	        result.append(URLEncoder.encode(paramName.getValue(), "UTF-8"));

			if (first) {
				first = false;
			}
	    }

	    return result.toString();
	}


	public void updateUrlBuilder(HttpUrl.Builder builder){
		for (String val :
				paramsMap.keySet()) {
			if (!Utils.isEmpty(val) ){
				builder.addQueryParameter(val,paramsMap.get(val));
			}
		}
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void setProperty(String propName, String propValue)
	{
		propertiesMap.put(propName,propValue);
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		
	
	public void applyProperties(HttpURLConnection urlConnection)
	{
		Iterator it = propertiesMap.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String,String> property = (Map.Entry<String,String>) it.next();
			
			if (Log.DEBUG) Log.d("","\nProperty " + property.getKey() + "="+property.getValue());
			
			urlConnection.addRequestProperty(property.getKey(), property.getValue());
			
		}
	}
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	public HttpReqParams clone() {
		
		HttpReqParams params = new HttpReqParams("");
		params.paramsMap = new LinkedHashMap<String, String>();
		params.paramsMap.putAll(paramsMap);
		
		return params;
	}
	
	
	public String getPostData() {
		return postData;
	}

	public void setPostData(String postData) {
		this.postData = postData;
	}
	
}
