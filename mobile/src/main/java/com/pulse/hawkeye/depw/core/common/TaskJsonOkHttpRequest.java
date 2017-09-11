package com.pulse.hawkeye.depw.core.common;


import com.pulse.hawkeye.depw.utils.IConst;
import com.pulse.hawkeye.depw.utils.Log;

/**
 * Created by Cosmin on 12/20/2016.
 * a {@link TaskJsonRequest} that will use {@link TaskOkHttpRequest}
 */

public class TaskJsonOkHttpRequest extends TaskOkHttpRequest {
    public TaskJsonOkHttpRequest(String sURL,
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
    public TaskJsonOkHttpRequest(ITaskReturn callback, String sURL,
                           HttpReqParams httpParams)
    {
        super(callback, IConst.HTTP_GET, sURL, httpParams, DataType.String);

        if (DEBUG) Log.m(TAG,this,"ctor()");
    }
}
