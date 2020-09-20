package org.withyu.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Message {
    private int messageID;
    private String title;
    private String content;
    private String sendTime;
    private int receiver;
    private int isRead;// 0:未阅读，1：已阅读

    public Message(String title, String content, String sendTime, int receiver) {
        this.title = title;
        this.content = content;
        this.sendTime = sendTime;
        this.receiver = receiver;
        this.isRead = 0;
    }

    public Message(int messageID, String title, String content, String sendTime, int receiver, int isRead) {
        this.messageID = messageID;
        this.title = title;
        this.content = content;
        this.sendTime = sendTime;
        this.receiver = receiver;
        this.isRead = isRead;
    }
    public Message(long messageID, String title, String content, Timestamp sendTime, long receiver, int isRead) {
        this.messageID = (int) messageID;
        this.title = title;
        this.content = content;
        this.sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sendTime);
        this.receiver = (int) receiver;
        this.isRead = isRead;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "message{" +
                "messageID=" + messageID +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", receiver=" + receiver +
                ", isRead=" + isRead +
                '}';
    }
}
