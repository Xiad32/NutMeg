package com.example.nutmeg;

import android.os.Handler;
import android.support.annotation.Nullable;

public class MessageDelayer {
    //Just a delay

    interface DelayerCallback {
        void onDone(String text);
    }

    private static final int DELAY_MILLIS = 3000;

    static void processMessage(final String message, final DelayerCallback callback,
                               @Nullable final SimpleIdlingResource idlingResource) {
        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleResourse(false);
        }

        // Delay the execution, return message via callback.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(message);
                    if (idlingResource != null) {
                        idlingResource.setIdleResourse(true);
                    }
                }
            }
        }, DELAY_MILLIS);
    }


}
