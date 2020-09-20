package org.withyu.service;

import org.withyu.domain.Message;

import java.util.List;

public interface MessageService {
    public List<Message> queryMessage(int userID);

    public boolean sendMessage(Message message);

    public boolean setMessageRead(int userID);
}
