package org.withyu.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.withyu.domain.Question;
import org.withyu.domain.User;

import java.util.List;

@Repository
public interface IUser_ActivityDao {

    /**
     * 删除活动
     *
     * @param questionID
     * @return
     */
    @Delete("delete from question " +
            "where questionID = #{questionID}")
    int deleteActivity(@Param("questionID") int questionID);

    /**
     * 参与活动
     * @param userID
     * @param questionID
     * @param time
     * @return
     */
    @Insert("insert into user_activity " +
            "values( #{questionID},#{userID}, #{joinTime})")
    int joinActivity(@Param("userID") int userID,
                     @Param("questionID") int questionID,
                     @Param("joinTime") String time);

    /**
     * 退出活动
     * @param userID
     * @param questionID
     * @return
     */
    @Delete("delete from user_activity " +
            "where questionID = #{questionID} and userID = #{userID}")
    int quitActivity(@Param("userID") int userID,
                     @Param("questionID") int questionID);

    /**
     * 查询用户是否参与该活动
     * @param userID
     * @param questionID
     * @return
     */
    @Select("select count(*) from user_activity " +
        "where userID = #{userID} and questionID = #{questionID}")
    int isJoined(@Param("userID") int userID,
                 @Param("questionID") int questionID);

    /**
     * 查询用户参加的所有活动信息
     * @param userID
     * @return
     */
    @Select("select * from question " +
            "where questionID in " +
            "( select questionID from user_activity where userID = #{userID} ) " +
            "and userID != #{userID} " +
            "order by issueTime desc")
    List<Question> queryJoinedActivity(@Param("userID") int userID);

    /**
     * 查询活动的所有参与者信息
     * @param questionID
     * @return
     */
    @Select("select user.userID,nickname,avator,school,email ,jointime from user ,user_activity " +
            "where user.userID = user_activity.userID and questionID = #{questionID} and user.userID in ( select userID from user_activity as a  where userID not in   (select userID from question as b where a.questionID=b.questionID) and questionID = #{questionID} )")
    List<User> queryParticipant(@Param("questionID") int questionID);

    /**
     * 查询用户发布的所有活动
     * @param userID
     * @return
     */
    @Select("select * from question where userID={userID} and typeID=2")
    List<Question> queryQuestionByUser(@Param("userID") int userID);
}
