package org.withyu.domain;

import org.withyu.util.CurrentTime;

public class Comment {
    private int commentID;
    private int questionID;
    private int userID;
    private String content;
    private int isToOwner;
    private int targetCommentID;
    private String issueTime;

    public Comment() {
    }

    public Comment(int commentID, int questionID, int userID, String content, int isToOwner, int targetCommentID,String issueTime) {
        this.commentID = commentID;
        this.questionID = questionID;
        this.userID = userID;
        this.content = content;
        this.isToOwner = isToOwner;
        this.targetCommentID = targetCommentID;
        this.issueTime = issueTime;
    }

    public Comment(int questionID, int userID, String content) {
        this.questionID = questionID;
        this.userID = userID;
        this.content = content;
        this.isToOwner = 0;
        this.targetCommentID = 0;
        this.issueTime = CurrentTime.getCurrentTime();
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
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

    public int getIsToOwner() {
        return isToOwner;
    }

    public void setIsToOwner(int isToOwner) {
        this.isToOwner = isToOwner;
    }

    public int getTargetCommentID() {
        return targetCommentID;
    }

    public void setTargetCommentID(int targetCommentID) {
        this.targetCommentID = targetCommentID;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", questionID=" + questionID +
                ", userID=" + userID +
                ", content='" + content + '\'' +
                ", isToOwner=" + isToOwner +
                ", targetCommentID=" + targetCommentID +
                ", issueTime='" + issueTime + '\'' +
                '}';
    }
}
