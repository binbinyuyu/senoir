package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.IUser_ActivityDao;
import org.withyu.domain.Question;
import org.withyu.domain.User;
import org.withyu.service.User_ActivityService;
import org.withyu.util.CurrentTime;

import java.util.List;

@Service("user_activityService")
public class User_ActivityServiceImpl implements User_ActivityService {
    @Autowired
    private IUser_ActivityDao iUser_activityDao;

    /**
     * 删除活动
     * @param questionID
     * @return
     */
    @Override
    public boolean deleteActivity(int questionID) {
        int num = iUser_activityDao.deleteActivity(questionID);
        return num==1;
    }

    /**
     * 用户参加活动
     *
     * @param userID
     * @param questionID
     * @return
     */
    @Override
    public boolean joinActivity(int userID, int questionID) {
        String time = CurrentTime.getCurrentTime();
        // System.out.println("userID:"+userID+"|activityID:"+activityID+"|time:"+time);
        int num = iUser_activityDao.joinActivity(userID, questionID, time);

        return num == 1;
    }

    /**
     * 查询用户是否参与该活动
     *
     * @param userID
     * @param questionID
     * @return
     */
    @Override
    public boolean isJoined(int userID, int questionID) {
        int num = iUser_activityDao.isJoined(userID, questionID);
        return num == 1;
    }

    /**
     * 查询用户参加的所有活动信息
     *
     * @param userID
     * @return
     */
    @Override
    public List<Question> queryJoinedActivity(int userID) {
        return iUser_activityDao.queryJoinedActivity(userID);
    }

    /**
     * 用户退出活动
     *
     * @param userID
     * @param questionID
     * @return
     */
    @Override
    public boolean quitActivity(int userID, int questionID) {
        int num = iUser_activityDao.quitActivity(userID, questionID);
        return num == 1;
    }

    /**
     * 查询活动的所有参与者
     *
     * @param questionID
     * @return
     */
    @Override
    public List<User> queryParticipantByActivityID(int questionID) {
        return iUser_activityDao.queryParticipant(questionID);
    }

    /**
     * 查询用户发布的所有活动
     * @param userID
     * @return
     */
    @Override
    public List<Question> queryQuestionByUser(int userID) {
        return iUser_activityDao.queryQuestionByUser(userID);
    }
}
