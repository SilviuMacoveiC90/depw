package com.pulse.hawkeye.depw.core.common;

import android.os.Handler;
import android.os.Looper;


import com.pulse.hawkeye.depw.core.data.MvpError;
import com.pulse.hawkeye.depw.utils.IConst;
import com.pulse.hawkeye.depw.utils.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;


public class Task implements Runnable, ITaskReturn {

    public static boolean DEBUG = Log.DEBUG;
    public static boolean VERBOUSE = Log.VERBOUSE;
    public static boolean INFO = Log.INFO;
    public static final String TAG = Task.class.getSimpleName();


    public static Long idCounter = Long.valueOf(0);

    protected Object taskId = null;

    private MvpError error = null;
    protected Handler handlerParent = null;

    protected Future taskHandler;

    public enum State {Created, Pooled, Running, Completed, Canceled, CanceledWithNotif}

    protected State executionState = State.Created;

    //	protected WeakReference<ITaskReturn> WRCallback = new WeakReference<ITaskReturn>(null);
    protected ITaskReturn callback = null;
    protected List<Task> listSubTasks = Collections.synchronizedList(new ArrayList<Task>());

    /**
     * @param
     * @return
     * @see
     */
    public Task() {
        if (DEBUG) Log.m(TAG, this, "ctor");
//		this.WRCallback = new WeakReference<>(null);
        setState(State.Created);
    }


    /**
     * @param
     * @return
     * @see
     */
    public Task(ITaskReturn passedCallback) {
        if (DEBUG) Log.m(TAG, this, "ctor(parent)");
//		this.WRCallback = new WeakReference<>(callback);
        callback = passedCallback;
        setState(State.Created);
    }


    /**
     * Will clear the callback instance. Used to eliminate the double instantiation problem.
     * Whould be called when the task is no longer needed
     */
    public void clearCallback() {
        callback = null;
    }

    /**
     * will reset the task to be ready to restart
     */
    public void reset() {
        error = null;
        setState(State.Created);
    }

    /**
     * @param
     * @return
     * @see
     */
    @Override
    public void run() {
        if (DEBUG) Log.m(TAG, this, "run");

        if (isCanceled()) {
            return;
        }

        setState(State.Running);

        if (INFO) Log.m(TAG, this, "run: ");

        synchronized (this) {
            try {
                onResetData();
                onExecute();
            } catch (Exception e) {
                if (DEBUG) e.printStackTrace();
                Log.e(TAG, "::run error caught while executing task");
            }

        }
    }


    /**
     * @param
     * @return
     * @see
     */
    protected void onExecute() {
        if (DEBUG) Log.m(TAG, this, "onExecute");
        taskCompleted();
    }

    /**
     * @param
     * @return
     * @see
     */
    protected void onResetData() {
        if (DEBUG) Log.m(TAG, this, "onResetData");

    }


    /**
     * @param
     * @return
     * @see
     */
    protected void taskCompleted(MvpError error) {
        if (DEBUG) Log.m(TAG, this, "taskCompleted(e)");

        if (DEBUG)
            Log.d(TAG, "	**Error** " + (error != null ? error.toString() : IConst.EMPTY_STRING));
        this.error = error;
        taskCompleted();
    }

    /**
     * @param
     * @return
     * @see
     */
    protected void taskCompleted() {

        if (callback != null) {
            if (DEBUG) Log.m(TAG, this, "taskCompleted");


            if (isCanceled()) {
                return;
            }

            if (executionState == State.Running) {
                setState(State.Completed);
            }

            final boolean canceled = executionState == State.CanceledWithNotif || executionState == State.Canceled;
            if (null == handlerParent) {
                handlerParent = new Handler(Looper.getMainLooper());
            }
            handlerParent.post(new CallbackAnnouncer(callback, canceled));
        } else {
            Log.d(TAG, "::taskCompleted Lost reference to callback");
        }
        onResetData();
    }

    /**
     * Class used to announce the taskReturn but not be touched by any changes in the
     * Callback reference
     */
    protected class CallbackAnnouncer implements Runnable {
        private ITaskReturn mCallback;
        private Boolean mCanceled;

        public CallbackAnnouncer(ITaskReturn callback, boolean canceled) {
            mCallback = callback;
            mCanceled = canceled;
        }

        @Override
        public void run() {
            if (mCallback != null) {
                mCallback.onReturnFromTask(Task.this, mCanceled);
            }
            mCallback = null;
        }
    }

    /**
     * @param
     * @return
     * @see
     */
    public MvpError getError() {
        if (DEBUG) Log.m(TAG, this, "getError");

        return error;
    }

    /**
     * @param
     * @return
     * @see
     */
    public boolean isError() {
        Boolean isError = error != null;
        if (isError) {
            if (VERBOUSE) Log.m(TAG, this, "isError");
        }

        return isError;
    }

    /**
     * @param
     * @return
     * @see
     */
    protected void setError(MvpError error) {
        if (DEBUG) Log.m(TAG, this, "setError");

        this.error = error;
    }


    /**
     * @param
     * @return
     * @see
     */
    protected void executeSubTask(Task subTask) {
        if (DEBUG) Log.m(TAG, this, "executeSubTask");

        if (isCanceled()) {
            return;
        }

        synchronized (listSubTasks) {
            listSubTasks.add(subTask);
        }

        if (DEBUG) Log.d(TAG, "worker id = " + taskId);

//		subTask.WRCallback = new WeakReference<>( (ITaskReturn)this );
        subTask.callback = this;
        subTask.execute();
    }

    /**
     * @param
     * @return
     * @see
     */
    public void execute() {
        if (DEBUG) Log.m(TAG, this, "execute");

        synchronized (idCounter) {
            ++idCounter;
            execute(idCounter, IConst.ASYNC_MODE);
        }
    }


    /**
     * @param
     * @return
     * @see
     */
    public void execute(Object taskId, boolean executeMode) {
        if (DEBUG) Log.m(TAG, this, "execute");


        if (Thread.currentThread().getId() == 1) {
            handlerParent = new Handler(Looper.getMainLooper()); // for UI thread only
        }

        if (null == taskId) {
            return;
        }

        this.taskId = taskId;

        if (DEBUG) Log.d(TAG, "worker id = " + taskId);

        setState(State.Pooled);

        if (executeMode) {
            taskHandler = TasksPoolExecutor.getInstance().submit(this);
        } else {
            onExecute();
        }
    }


    /**
     * @param
     * @return
     * @see
     */
    public Object getTaskId() {
        return taskId;
    }

    /**
     * @param
     * @return
     * @see
     */
    public void cancel(boolean noParentNotif) {
        if (DEBUG) Log.m(TAG, this, "cancel");

//		ITaskReturn tr = WRCallback.get();
        if (callback == null) {
            return;
        }

        setState(State.Canceled);


        boolean bCancel = TasksPoolExecutor.getInstance().remove(this);
        if (DEBUG) Log.d(TAG, "TasksPoolExecutor canceling " + taskId + ": " + bCancel);
        if (null != taskHandler) {
            bCancel = taskHandler.cancel(false);
            if (DEBUG) Log.d(TAG, "Future canceling " + taskId + ": " + bCancel);
        }

        synchronized (listSubTasks) {
            for (int i = 0; i < listSubTasks.size(); i++) {
                listSubTasks.get(i).cancel(noParentNotif);
            }
            listSubTasks.clear();
        }

        if (false == noParentNotif) {
            if (null != handlerParent) {
                handlerParent.post(new Runnable() {

                    @Override
                    public void run() {
//						ITaskReturn tr = WRCallback.get();
                        if (callback != null) {
                            callback.onReturnFromTask(Task.this, true);
                        }
                    }
                });
            } else {
                if (callback != null) {
                    callback.onReturnFromTask(Task.this, true);
                }
            }
        }

    }


    /**
     * @param
     * @return
     * @see
     */

    public Object getReturnData() {
        return null;
    }

    /**
     * @param
     * @return
     * @see
     */
    @Override
    public void onReturnFromTask(Task subTask, boolean canceled) {
        if (DEBUG) Log.m(TAG, this, "onReturnFromTask");

        if (DEBUG) Log.d(TAG, "return from worker id = " + subTask.taskId);

        synchronized (listSubTasks) {
            listSubTasks.remove(subTask);
        }
    }


    public boolean isCanceled() {
        boolean bState = true;
        synchronized (executionState) {
            bState = (executionState == State.Canceled);
        }
        return bState;
    }

    public State getState() {
        return executionState;
    }

    protected void setState(State state) {
        if (DEBUG) Log.m(TAG, this, "setState");

        synchronized (executionState) {
            executionState = state;
            if (Task.DEBUG) Log.d(TAG, "State: " + executionState);
        }
    }

}
