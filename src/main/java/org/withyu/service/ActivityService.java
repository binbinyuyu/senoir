package org.withyu.service;

import org.withyu.domain.Activity;

import java.util.List;

public interface ActivityService {
    public boolean issueActivity(Activity activity);

    public boolean alterActivity(int activityID, String content, String filename);

    public boolean deleteActivity(int activityID);

    public List<Activity> searchActivity(String word, int currentPage);

    public Activity queryActivityByID(int activityID);

    public int querySearchNum(String word);

    public List<Activity> queryActivityByUser(int userID);

    public int queryPageNum();

    public int querySelectedPageNum(String type);

    public List<Activity> queryActivityByPageByType(String type, int currentPage);

    public List<Activity> queryActivityByPage(int currentPage);
}
