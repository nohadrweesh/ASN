package com.example.a.myapplication.vo;

/**
 * Created by ecs on 11/04/2018.
 */

public class NotificationVo {

    private String title;
    private String message;
    private String iconUrl;
    private String action;
    private String actionDestination;
    private String notificationType;
    private int toCarID;
    private int toDriverID;
    private int problemID;


    public int getProblemID() {
        return problemID;
    }

    public int getToCarID() {
        return toCarID;
    }


    public int getToDriverID() {
        return toDriverID;
    }

    public void setToCarID(int toCarID) {
        this.toCarID = toCarID;
    }

    public void setToDriverID(int toDriverID) {
        this.toDriverID = toDriverID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    public void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
