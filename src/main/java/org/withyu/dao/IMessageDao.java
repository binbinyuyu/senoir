package org.withyu.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.withyu.domain.Message;

import java.util.List;

@Repository
public interface IMessageDao {
    /**
     * 查询发送给该用户的所有信息
     *
     * @param userID
     * @return
     */
    @Select("select * from message " +
            "where receiver = #{userID} " +
            "order by sendTime desc")
    List<Message> queryMessage(@Param("userID") int userID);

    /**
     * 朝用户发送消息
     *
     * @param message
     * @return
     */
    @Insert("insert into message(title,content,sendTime,receiver,isRead) " +
            "values(#{message.title}, #{message.content}, #{message.sendTime}, " +
            "#{message.receiver}, #{message.isRead})")
    int sendMessage(@Param("message") Message message);

    /**
     * 将发送给该用户的所有message设置为已读
     * @param userID
     * @return
     */
    @Update("update message set isRead = 1 " +
            "where receiver = #{userID}")
    int setMessageRead(@Param("userID") int userID);

}
