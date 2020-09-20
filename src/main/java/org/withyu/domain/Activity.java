package org.withyu.domain;

import org.withyu.util.CurrentTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


//-1表示未知
public class Activity {
    private int activityID;
    private int userID;
    private String content;
    private String image;
    private String issueTime;
    private int number;
    private String type;

    public Activity(int userID, String content, String image, int number, String type) {
        this.activityID = -1;
        this.userID = userID;
        this.content = content;
        this.image = image;
        this.issueTime = CurrentTime.getCurrentTime();
        this.number = number;
        this.type = type;
    }

    public Activity(int activityID, int userID, String content, String image, String issueTime, int number,
                    String type) {
        this.activityID = activityID;
        this.userID = userID;
        this.content = content;
        this.image = image;
        this.issueTime = issueTime;
        this.number = number;
        this.type = type;
    }

    public Activity(long activityID, long userID, String content, String image, Timestamp issueTime, int number,
                    String type) {
        this.activityID = (int) activityID;
        this.userID = (int) userID;
        this.content = content;
        this.image = image;
        this.issueTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(issueTime);;
        this.number = number;
        this.type = type;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityID=" + activityID +
                ", userID=" + userID +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", issueTime='" + issueTime + '\'' +
                ", number=" + number +
                ", type='" + type + '\'' +
                '}';
    }
}
