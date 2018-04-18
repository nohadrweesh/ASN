package com.example.a.myapplication.utils;

/**
 * Created by ecs on 11/04/2018.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;

import com.example.a.myapplication.Main2Activity;
import com.example.a.myapplication.MainActivity;
import com.example.a.myapplication.R;
import com.example.a.myapplication.SecondActivity;
import com.example.a.myapplication.sync.HelpIntentService;
import com.example.a.myapplication.sync.HelpTasks;
import com.example.a.myapplication.vo.NotificationVo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    private static final String TAG = "NotificationUtils";

    private static final int NOTIFICATION_ID = 200;
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "myChannel";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;

    private static final int ACTION_ACCEPT_HELP_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    private static final int HELP_PENDING_INTENT_ID = 3417;

    public static int toCarID;
    public  static int toDriverID;


    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("OBDActivity", Main2Activity.class);
        activityMap.put("SecondActivity", SecondActivity.class);
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void displayNotification1(NotificationVo notificationVO, Intent resultIntent) {
        {
            String message = notificationVO.getMessage();
            String title = notificationVO.getTitle();
            String iconUrl = notificationVO.getIconUrl();
            String action = notificationVO.getAction();
            String destination = notificationVO.getActionDestination();
            Bitmap iconBitMap = null;
            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl);
            }
            final int icon = R.mipmap.ic_launcher;

            PendingIntent resultPendingIntent;

            if (URL.equals(action)) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
                resultIntent = new Intent(mContext, activityMap.get(destination));

                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            } else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            }


            Notification.Builder mBuilder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder = new Notification.Builder(mContext,
                        CHANNEL_ID);
            } else {
                mBuilder = new Notification.Builder(mContext);
            }




            Notification notification;

            if (iconBitMap == null) {
                //When Inbox Style is applied, user can expand the notification
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                //NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.MediaStyle().setMediaSession(sessionCompat.getSessionToken())

                inboxStyle.addLine(message);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        //.setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(message)
                        //.addAction(acceptUserHelp(mContext))
                        //.addAction(ignoreAction(mContext))
                        .build();

            } else {
                //If Bitmap is created from URL, show big icon
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(iconBitMap);
                notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        //.setStyle(bigPictureStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(message)
                        .build();
            }

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static android.support.v4.app.NotificationCompat.Action ignoreAction(Context context) {

        Intent ignoreActionIntent = new Intent(context, HelpIntentService.class);

        ignoreActionIntent.setAction(HelpTasks.ACTION_DISMISS_HELP_REQUEST);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreActionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.Action ignoreReminderAction = new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
                "No, Sorry I can't help.",
                ignoreReminderPendingIntent);

        return ignoreReminderAction;
    }


    private static NotificationCompat.Action acceptUserHelp(Context context) {

        Intent incrementWaterCountIntent = new Intent(context, HelpIntentService.class);

        incrementWaterCountIntent.setAction(HelpTasks.ACTION_ACCEPT_HELP_REQUEST);

        PendingIntent acceptHelpPendingIntent = PendingIntent.getService(
                context,
                ACTION_ACCEPT_HELP_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action acceptHelpAction = new NotificationCompat.Action(R.drawable.ic_local_drink_black_24px,
                "I can help him,Provide with more info",
                acceptHelpPendingIntent);

        return acceptHelpAction;
    }


    public  void displayNotification(NotificationVo notificationVO, Intent resultIntent) {
        String message = notificationVO.getMessage();
        String title = notificationVO.getTitle();
        String iconUrl = notificationVO.getIconUrl();
        String action = notificationVO.getAction();
        String destination = notificationVO.getActionDestination();
        toCarID=notificationVO.getToCarID();
        toDriverID=notificationVO.getToDriverID();

        Log.d(TAG, "displayNotification: toDriverID "+String.valueOf(toDriverID));
        Log.d(TAG, "displayNotification: toCarID "+String.valueOf(toCarID));
        Bitmap iconBitMap = null;
        if (iconUrl != null) {
            iconBitMap = getBitmapFromURL(iconUrl);
        }
        final int icon = R.mipmap.ic_launcher;

        PendingIntent resultPendingIntent;

        if (URL.equals(action)) {
            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

            resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
            resultIntent = new Intent(mContext, activityMap.get(destination));

            resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        } else {
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        }

        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        message))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(resultPendingIntent)

                .addAction(acceptUserHelp(mContext))
                .addAction(ignoreAction(mContext))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                HELP_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
