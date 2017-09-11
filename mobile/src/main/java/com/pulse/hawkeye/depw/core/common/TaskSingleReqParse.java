package com.pulse.hawkeye.depw.core.common;


import com.pulse.hawkeye.depw.core.data.IJsonParser;
import com.pulse.hawkeye.depw.core.data.MvpError;
import com.pulse.hawkeye.depw.core.data.MvpServerError;
import com.pulse.hawkeye.depw.utils.Log;

public class TaskSingleReqParse<TemplateType> extends Task{

	public static boolean DEBUG = Log.DEBUG;
	public final static String TAG = "TaskSingleReqCache";

	/**
	 * the class type of the object that will do the JSON parsing
	 * will have to extend {@link IJsonParser}
	 */
	protected Class classType;
	protected String sURL;
	protected HttpReqParams httpParams;
	public Object mvpData = null;

	protected TaskHttpRequest taskServerDataReq = null;
	protected TaskJsonParser taskServerDataParsing = null;

	/**
	 * The constructor of the task
	 * @param callback the callback with the task result
	 * @param classType the class type of the object that will do the JSON parsing
	 *                  will have to extend {@link IJsonParser}
	 * @param sURL the server base url
	 * @param httpParams the http params for the request
	 */
	public TaskSingleReqParse(ITaskReturn callback, Class classType, String sURL, HttpReqParams httpParams)
	{
		super(callback);

		if (DEBUG) Log.m(TAG,this,"ctor()");

		this.classType = classType;

		this.sURL = sURL;
		this.httpParams = httpParams;
	}


	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	@Override
	protected void onExecute()
	{
		if (DEBUG) Log.m(TAG,this,"onExecute");
		/*reset the task in case is refurbished and will be resent*/
		reset();

		taskServerDataReq = new TaskJsonRequest(
				sURL,
				httpParams);
		executeSubTask(taskServerDataReq);
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	@Override
	protected void onResetData()
	{
		super.onResetData();

		taskServerDataReq = null;
		taskServerDataParsing = null;
	}

	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */

	public Object getReturnData()
	{
		return  mvpData;
	}



	/**
	 *
	 *
	 * @param
	 * @return
	 * @see
	 */
	@Override
	public void onReturnFromTask(Task subTask, boolean canceled)
	{
		if (DEBUG) Log.m(TAG,this,"onReturnFromTask");

		super.onReturnFromTask(subTask, canceled);

		if (canceled || isCanceled())
		{
			return;
		}

		//----------------------------------------
		if (subTask ==  taskServerDataReq) // return from getting data from server
		{
			if (taskServerDataReq.isError())
			{
				if(null != taskServerDataReq.getError())
				{
					taskCompleted(taskServerDataReq.getError());
				}
				else
				{
					taskCompleted();
				}
				return;
			}

			if (taskServerDataReq.getStringData().contains(MvpServerError.TAG_RESPONSECODE))
			{
				taskCompleted(new MvpServerError(taskServerDataReq.getStringData()));
				return;
			}

			IJsonParser mvpData = null;
			try {
				mvpData= (IJsonParser) classType.newInstance();
			}
			catch (InstantiationException e)
			{
				setError(new MvpError(MvpError.ErrorType.Processing, e));

			}
			catch (IllegalAccessException e)
			{
				setError(new MvpError(MvpError.ErrorType.Processing, e));
			}


			taskServerDataParsing = new TaskJsonParser(mvpData, taskServerDataReq.getStringData());
			executeSubTask(taskServerDataParsing);

			return;
		}

		//----------------------------------------
		if (subTask == taskServerDataParsing) // return from parsing data from server
		{
			if (taskServerDataParsing.isError())
			{
				taskCompleted(new MvpError(MvpError.ErrorType.Processing, taskServerDataParsing.getError()));
				return;
			}

			mvpData = taskServerDataParsing.getReturnData();
			taskCompleted();

			return;
		}

	}



}
