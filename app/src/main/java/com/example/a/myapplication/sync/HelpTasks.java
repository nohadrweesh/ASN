package com.example.a.myapplication.sync;

import android.content.Context;
import android.util.Log;

import com.example.a.myapplication.HelpUtils;
import com.example.a.myapplication.utils.NotificationUtils;

/**
 * Created by Speed on 14/04/2018.
 */

public class HelpTasks {
    public static final String ACTION_ACCEPT_HELP_REQUEST = "accept-help-request";

    public static final String ACTION_DISMISS_HELP_REQUEST = "dismiss-help-request";

    private static final String TAG = "HelpTasks";

    public static void executeTask(Context context, String action) {
        if (ACTION_ACCEPT_HELP_REQUEST.equals(action)) {
            Log.d(TAG, "executeTask: action "+action);
            HelpUtils.getInstance(context).sendHelpTo(NotificationUtils.toDriverID,NotificationUtils.toCarID,NotificationUtils.problemID);


        } else if (ACTION_DISMISS_HELP_REQUEST.equals(action)) {
            Log.d(TAG, "executeTask: action "+action);
        }

    }
}
