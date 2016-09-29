package com.uscc.ncku.androiditri;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyFeedbackService extends Service {
    public MyFeedbackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
