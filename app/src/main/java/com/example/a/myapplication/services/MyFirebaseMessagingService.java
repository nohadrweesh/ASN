package com.example.a.myapplication.services;

/**
 * Created by ecs on 11/04/2018.
 */

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a.myapplication.MainActivity;
import com.example.a.myapplication.SingleAdActivity;
import com.example.a.myapplication.SingleAdvertiserAdsActivity;
import com.example.a.myapplication.utils.NotificationUtils;
import com.example.a.myapplication.vo.NotificationVo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";
    private static final String TO_CAR_ID="toCarID";
    private static final String TO_USER_ID="toUserID";
    private static final String PROBLEM_ID="problemID";
    private static final String NOTIFICATION_TYPE="notificationType";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVo notificationVO = new NotificationVo();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        String notificationType=data.get(NOTIFICATION_TYPE);

        if(notificationType.equals("ADV")){
            int ownerID = Integer.valueOf(data.get("ownerID"));
            String ownerName = data.get("ownerName");
            String ownerIconURL = data.get("iconURL");

            Intent intent = new Intent(getApplicationContext(),SingleAdvertiserAdsActivity.class);
            intent.putExtra("ownerID", ownerID);
            intent.putExtra("ownerID", ownerID);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    intent,0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(ownerName)
                    .setContentText(ownerName + " has new advertisements")
                    .setContentIntent(pendingIntent);
        }
        else {
            String title = data.get(TITLE);
            String message = data.get(MESSAGE);
            String iconUrl = data.get(IMAGE);
            String action = data.get(ACTION);
            String actionDestination = data.get(ACTION_DESTINATION);
            String toCarID = data.get(TO_CAR_ID);
            String toUserID = data.get(TO_USER_ID);
            String problemID = data.get(PROBLEM_ID);
            NotificationVo notificationVO = new NotificationVo();
            notificationVO.setTitle(title);
            notificationVO.setMessage(message);

            notificationVO.setIconUrl(iconUrl);
            notificationVO.setAction(action);
            notificationVO.setActionDestination(actionDestination);
            notificationVO.setNotificationType(notificationType);
            if (!notificationType.equals("SA")) {

                notificationVO.setToCarID(Integer.parseInt(toCarID));
                notificationVO.setToDriverID(Integer.parseInt(toUserID));
                notificationVO.setProblemID(Integer.parseInt(problemID));
            } else {
                Log.d(TAG, "handleData: notificationTYPE SA");
            }

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.displayNotification(notificationVO, resultIntent);
            notificationUtils.playNotificationSound();
        }
    }

}
