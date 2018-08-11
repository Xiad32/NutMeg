package com.example.nutmeg;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource{

    @Nullable private ResourceCallback mCallback;
    private AtomicBoolean mIsIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleResourse(boolean idleStatue){
        mIsIdle.set(idleStatue);
        if(isIdleNow() & mCallback != null)
            mCallback.onTransitionToIdle();
    }

}
