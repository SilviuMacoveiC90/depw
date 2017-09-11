package com.pulse.hawkeye.depw.core.common;

import com.pulse.hawkeye.depw.R;
import com.pulse.hawkeye.depw.core.data.MvpError;
import com.pulse.hawkeye.depw.core.data.MvpServerError;
import com.pulse.hawkeye.depw.utils.IConst;
import com.pulse.hawkeye.depw.utils.Log;
import com.pulse.hawkeye.depw.utils.Utils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;


public class TaskHttpRequest extends Task
{	
	public static boolean DEBUG = Log.DEBUG;
	public static boolean IS_RELEASE = Log.IS_RELEASE;
	public static final String TAG = TaskHttpRequest.class.getSimpleName();

	protected HttpReqParams httpParams = null;




	protected String requestType = IConst.HTTP_GET;
	protected String sURL = null;
	protected InputStream requestStream = null;
	protected DataType dataType;

	protected int responseCode = -1;
	protected static int requestTimeout = 30 * 1000; // msecs
	protected HttpURLConnection connection = null;

	protected byte[] byteData = null;
	protected String sData = "";


	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	public TaskHttpRequest(String requestType, String sURL, HttpReqParams httpParams, DataType dataType)
	{
		if (DEBUG) Log.m(TAG,this,"ctor()");

		this.requestType = requestType;
		this.httpParams = httpParams;
		this.sURL = sURL;
		this.dataType = dataType;
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	public TaskHttpRequest(ITaskReturn callback, String requestType, String sURL, HttpReqParams httpParams, DataType dataType)
	{
		super(callback);
		if (DEBUG) Log.m(TAG,this,"ctor()");

		this.requestType = requestType;
		this.httpParams = httpParams;
		this.sURL = sURL;
		this.dataType = dataType;
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	public int getResponseCode()
	{
		return responseCode;
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	public byte[] getByteData()
	{
		return byteData;
	}

	public String getStringData()
	{
		return sData;
	}


	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	@Override
	public void onExecute()
	{
		if (DEBUG) Log.m(TAG,this,"onExecute");

		String tempURL = this.sURL;

		if ( isCanceled() ){
			terminateWithCancel(tempURL);
			return;
		}
		Chrono crono = new Chrono();
		
		MvpError error = null;
		try
		{
			boolean hasPostData = false;
			if (httpParams != null)
			{
				tempURL = sURL+httpParams.asString();
				hasPostData = (false == Utils.isEmpty(httpParams.getPostData()));
			}			
			URI uri = new URI(tempURL);

			if ( isCanceled() ){
				terminateWithCancel(tempURL);
				return;
			}
			URL requestURL = uri.toURL();
			if (DEBUG) Log.d(TAG,">>>>>>>>");
			if (DEBUG) Log.d(TAG,tempURL);
			if (DEBUG) Log.d(TAG,">>>>>>>>");

			if (false == IS_RELEASE) 
			{
				Log.i(TAG,tempURL);
				//MainApp.getInstance().writetoLogFile("\n" + tempURL);
			}

			if ( isCanceled() ){
				terminateWithCancel(tempURL);
				return;
			}
			connection = (HttpURLConnection) requestURL.openConnection();

			if ( isCanceled() ){
				terminateWithCancel(tempURL);
				return;
			}
			connection.setRequestMethod(requestType);
//			connection.setRequestProperty("Accept-Encoding", "gzip");
			if ( isCanceled() ){

				terminateWithCancel(tempURL);
				return;
			}
			connection.setConnectTimeout(requestTimeout);

			if ( isCanceled() ){

				terminateWithCancel(tempURL);
				return;
			}
			if (null != httpParams)
			{
				httpParams.applyProperties(connection);
			}

			if ( isCanceled() ){

				terminateWithCancel(tempURL);
				return;
			}
			connection.setDoOutput(hasPostData);

			if ( isCanceled() ){

				terminateWithCancel(tempURL);
				return;
			}
			connection.connect();
			
			if (hasPostData)
			{
				DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			      wr.writeBytes (httpParams.getPostData());
			      wr.flush ();
			      wr.close ();								
			}

			if ( isCanceled() ){
				terminateWithCancel(tempURL);
				return;
			}
			responseCode = connection.getResponseCode();
			if ( isCanceled() ){
				terminateWithCancel(tempURL);
				return;
			}
			tempURL = connection.getURL().toString();
			if (responseCode < 400 /*any response code > 400 is an error code*/)
			{
					if (dataType == DataType.String)
					{				
				        sData = Utils.convertBufferToString(connection.getInputStream());
				    }
					else
					{
						byteData = Utils.convertBufferToByteArray(connection.getInputStream());
					}
			}
			else
			{
					
				sData = Utils.convertBufferToString(connection.getErrorStream());
				
		        setError(new MvpError(MvpError.ErrorType.Http, responseCode));
			}
	
		}
		catch (SocketTimeoutException e)
		{
			Log.e(TAG,"::onExecute SocketTimeoutException caught wile running "+tempURL);
			e.printStackTrace();
			setError(new MvpServerError(connection));			
		}
		catch (UnknownHostException e)
		{
			Log.e(TAG,"::onExecute UnknownHostException caught wile running "+tempURL);
			e.printStackTrace();
			setError(new MvpServerError(connection));
		}
		catch (Exception e)
		{
			Log.e(TAG,"::onExecute Exception caught wile running "+tempURL);
			e.printStackTrace();
			setError(new MvpError(MvpError.ErrorType.Http, e));
		}

		
		if (null != connection)
		{
			connection.disconnect();
			connection = null;
		}

		if (DEBUG) Log.d(TAG,"[URL]: " + tempURL);
		if (DEBUG) Log.d(TAG,"data: " + sData);
		
		if (DEBUG) Log.d(TAG,"Http req: " + crono.print());
		
		taskCompleted();
	}

	protected void terminateWithCancel(String tempURL){

		Log.w(TAG,"::onExecute terminateWithCancel caught wile running "+tempURL);

		if ( connection != null ) {
			try {
				connection.disconnect();
			}catch (Exception ex) {
			}
			connection = null;
		}
		taskCompleted();
	}

}

