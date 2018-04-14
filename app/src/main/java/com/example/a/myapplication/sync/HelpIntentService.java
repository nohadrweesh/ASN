package com.example.a.myapplication.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Speed on 14/04/2018.
 */

public class HelpIntentService extends IntentService {
    private static final String TAG = "HelpIntentService";
    public HelpIntentService() {
        super("HelpIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action=intent.getAction();
        Log.d(TAG, "onHandleIntent: Action = "+action);
        HelpTasks.executeTask(this,action);


    }
}
