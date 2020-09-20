package org.withyu.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Question {
    private int questionID;
    private int userID;
    private String content;
    private String image;
    private String issueTime;
    private int typeID;
    private int number;

    public Question(int questionID, int userID, String content, String image, String issueTime, int typeID, int number) {
        this.questionID = questionID;
        this.userID = userID;
        this.content = content;
        this.image = image;
        this.issueTime = issueTime;
        this.typeID = typeID;
        this.number = number;
    }

    public Question(int questionID) {
        this.questionID = questionID;
    }

    public Question(int questionID, String content) {
        this.questionID = questionID;
        this.content = content;
    }

    public Question(int userID, String content, String image, String issueTime, int typeID) {
        this.userID = userID;
        this.content = content;
        this.image = image;
        this.issueTime = issueTime;
        this.typeID = typeID;
        this.number = 1;
    }

    public Question(long questionID, int userID, String content, String image, Timestamp issueTime, int typeID, int number) {
        this.questionID = (int) questionID;
        this.userID = (int) userID;
        this.content = content;
        this.image = image;
        this.issueTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(issueTime);
        this.typeID = typeID;
        this.number = number;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
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

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", userID=" + userID +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", issueTime='" + issueTime + '\'' +
                ", typeID=" + typeID +
                ", number=" + number +
                '}';
    }
}
