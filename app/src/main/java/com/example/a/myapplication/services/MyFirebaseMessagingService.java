package com.example.a.myapplication.services;

/**
 * Created by ecs on 11/04/2018.
 */

import android.content.Intent;
import android.util.Log;

import com.example.a.myapplication.MainActivity;
import com.example.a.myapplication.OffersActivity;
import com.example.a.myapplication.SingleAdvertiserAdsActivity;
import com.example.a.myapplication.database.DatabaseHelper;
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
    private static final String NOTIFICATION_CENTER_ID="centerId";
    private static final String NOTIFICATION_OFFER_ID="offerId";
    private static final String NOTIFICATION_EXPIRY_DATE="expDate";
    private static final String NOTIFICATION_CENTER_NAME="centerName";

    private DatabaseHelper db;

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
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        String toCarID=data.get(TO_CAR_ID);
        String toUserID=data.get(TO_USER_ID);
        String problemID=data.get(PROBLEM_ID);
        String notificationType=data.get(NOTIFICATION_TYPE);
        NotificationVo notificationVO = new NotificationVo();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        notificationVO.setIconUrl(iconUrl);
        notificationVO.setAction(action);
        notificationVO.setActionDestination(actionDestination);
        notificationVO.setNotificationType(notificationType);
        Intent resultIntent;
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        if(notificationType.equals("HELP")) {

            notificationVO.setToCarID(Integer.parseInt(toCarID));
            notificationVO.setToDriverID(Integer.parseInt(toUserID));
            notificationVO.setProblemID(Integer.parseInt(problemID));

            resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            //TODO: THIS INTENT DETERMINES WHERE TO GO WHEN YOU PRESS IT(NOT ITS ACTIONS)
            // TODO:SO YOU CAN CUSTOMIZE IT WITH THE ACTIVITY YOU WANT,PUT EXTRAS TO IT

        }else if(notificationType.equals("SC-OFFER")){
            db = new DatabaseHelper(this);
            db.insertOffer(Integer.parseInt(data.get(NOTIFICATION_OFFER_ID)),Integer.parseInt(data.get(NOTIFICATION_CENTER_ID)),
                    data.get(TITLE),data.get(MESSAGE),data.get(NOTIFICATION_EXPIRY_DATE),data.get(NOTIFICATION_CENTER_NAME));

            resultIntent = new Intent(getApplicationContext(), OffersActivity.class);
            //TODO: THIS INTENT DETERMINES WHERE TO GO WHEN YOU PRESS IT(NOT ITS ACTIONS)
            // TODO:SO YOU CAN CUSTOMIZE IT WITH THE ACTIVITY YOU WANT,PUT EXTRAS TO IT

        }else if(notificationType.equals("ADV")) {

            //TODO:ADD  ADV APP NOTIFICATION HANDLING HERE

            Log.d("HandleAdvNotifData :", "Adv Notification Received");

            int ownerID = Integer.valueOf(data.get("ownerID"));
            String ownerName = data.get("ownerName");

            notificationVO.setTitle(ownerName);
            notificationVO.setMessage(ownerName + " has new advertisements");
            notificationVO.setNotificationType(notificationType);

            ///// These Attributes are set to NULL as they aren't needed //////
            notificationVO.setIconUrl("");
            notificationVO.setAction("");
            notificationVO.setActionDestination("");


            resultIntent = new Intent(getApplicationContext(), SingleAdvertiserAdsActivity.class);
            resultIntent.putExtra("ownerID", ownerID);
            resultIntent.putExtra("ownerName", ownerName);

            //TODO: THIS INTENT DETERMINES WHERE TO GO WHEN YOU PRESS IT(NOT ITS ACTIONS)
            // TODO:SO YOU CAN CUSTOMIZE IT WITH THE ACTIVITY YOU WANT,PUT EXTRAS TO IT
        }
        else {

            resultIntent = new Intent(getApplicationContext(), MainActivity.class);

            //TODO: THIS INTENT DETERMINES WHERE TO GO WHEN YOU PRESS IT(NOT ITS ACTIONS)
            // TODO:SO YOU CAN CUSTOMIZE IT WITH THE ACTIVITY YOU WANT,PUT EXTRAS TO IT
            Log.d(TAG, "handleData: notificationTYPE "+notificationType);
        }




        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

    }



}
