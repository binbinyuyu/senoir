package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.IMessageDao;
import org.withyu.domain.Message;
import org.withyu.service.MessageService;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private IMessageDao iMessageDao;

    /**
     * 查询发送给用户的所有消息
     *
     * @param userID
     * @return
     */
    @Override
    public List<Message> queryMessage(int userID) {
        return iMessageDao.queryMessage(userID);
    }

    /**
     * 朝用户发送消息
     *
     * @param message
     * @return
     */
    @Override
    public boolean sendMessage(Message message) {
        int num = iMessageDao.sendMessage(message);
        return num == 1;
    }

    /**
     * 将用户的所有message设置为已读
     *
     * @param userID
     * @return
     */
    @Override
    public boolean setMessageRead(int userID) {
        int num = iMessageDao.setMessageRead(userID);
        return num == 1;
    }
}
