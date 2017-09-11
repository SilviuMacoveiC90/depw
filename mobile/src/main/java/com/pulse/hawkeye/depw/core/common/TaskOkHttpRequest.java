package com.pulse.hawkeye.depw.core.common;


import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cosmin on 12/20/2016.
 * A task that will send http requests using okHttp library
 */

public class TaskOkHttpRequest extends TaskHttpRequest {


    public static final String TAG = TaskOkHttpRequest.class.getSimpleName();
    private static final OkHttpClient ohHttpClient = new OkHttpClient.Builder()
//            .cache(new Cache(cacheDir, cacheSize))
            .readTimeout(requestTimeout, TimeUnit.MILLISECONDS)
            .build();


    public TaskOkHttpRequest(String requestType, String sURL, HttpReqParams httpParams, DataType dataType) {
        super(requestType, sURL, httpParams, dataType);
    }

    public TaskOkHttpRequest(ITaskReturn callback, String requestType, String sURL, HttpReqParams httpParams, DataType dataType) {
        super(callback, requestType, sURL, httpParams, dataType);
    }


    @Override
    public void onExecute()
    {
        if (DEBUG) Log.m(TAG,this,"onExecute");
        Chrono crono = new Chrono();

        String tempURL = this.sURL;
        if(!MainApp.isNetworkAvailable())
        {
            setError(new MvpError(MvpError.ErrorType.Http));
            taskCompleted();
            return;
        }

        if ( isCanceled() ){
            terminateWithCancel(tempURL);
            return;
        }

        try{
            HttpUrl.Builder urlBuilder = HttpUrl.parse(sURL).newBuilder();
            if ( httpParams != null ){
                httpParams.updateUrlBuilder(urlBuilder);
            }
            if ( isCanceled() ){
                terminateWithCancel(tempURL);
                return;
            }
             tempURL = urlBuilder.build().toString();
            if (DEBUG) Log.d(TAG,">>>>>>>>");
            if (DEBUG) Log.d(TAG,tempURL);
            if (DEBUG) Log.d(TAG,">>>>>>>>");
            if ( isCanceled() ){
                terminateWithCancel(tempURL);
                return;
            }
            Request request = new Request.Builder()
                    .url(tempURL)
                    .build();
            if ( isCanceled() ){

                terminateWithCancel(tempURL);
                return;
            }
            Response response = ohHttpClient.newCall(request).execute();
            if ( isCanceled() ){

                terminateWithCancel(tempURL);
                return;
            }

            responseCode = response.code();
            if (responseCode < 400 /*any response code > 400 is an error code*/)
            {
                if (dataType == DataType.String)
                {
                    sData = response.body().string();
                }
                else
                {
                    byteData = Utils.convertBufferToByteArray(response.body().byteStream());
                }
            }
            else
            {

                sData = response.body().string();

                setError(new MvpError(MvpError.ErrorType.Http, responseCode));
            }

        }catch (Exception ex){
            Log.d(TAG,"::onExecute Some exception caught while doing the request");
            ex.printStackTrace();
            setError(new MvpError(MvpError.ErrorType.Http, ex));
        }



        if (DEBUG) Log.d(TAG,"[URL]: " + tempURL);
        if (DEBUG) Log.d(TAG,"data: " + sData);

        if (DEBUG) Log.d(TAG,"Http req: " + crono.print());

        taskCompleted();
    }

}
