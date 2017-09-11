package com.pulse.hawkeye.depw.core.common;

import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock extends ReentrantLock {

    private static final String TAG = "MyReentrantLock";


    @Override
    public void  lock(){
        super.lock();
    }

    @Override
    public void unlock(){
        super.unlock();
    }

}
