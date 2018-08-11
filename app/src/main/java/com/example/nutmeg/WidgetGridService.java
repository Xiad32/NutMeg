package com.example.nutmeg;

import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

import com.example.nutmeg.Widget.GridRemoteViewsFactory;

public class WidgetGridService extends RemoteViewsService {
//    public WidgetGridService() {
//    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        //  Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}
