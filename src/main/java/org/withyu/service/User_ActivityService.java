package org.withyu.service;

import org.withyu.domain.Question;
import org.withyu.domain.User;

import java.util.List;

public interface User_ActivityService {
    boolean deleteActivity(int questionID);

    boolean joinActivity(int userID, int questionID);

    boolean isJoined(int userID, int questionID);

    List<Question> queryJoinedActivity(int userID);

    boolean quitActivity(int userID, int questionID);

    List<User> queryParticipantByActivityID(int questionID);

    List<Question> queryQuestionByUser(int userID);
}
