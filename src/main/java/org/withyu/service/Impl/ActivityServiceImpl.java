package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.IActivityDao;
import org.withyu.domain.Activity;
import org.withyu.service.ActivityService;

import java.util.List;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private IActivityDao iActivityDao;
    int count = 10;

    /**
     * 用户发布活动
     *
     * @param activity
     * @return
     */
    @Override
    public boolean issueActivity(Activity activity) {
        int num = iActivityDao.issueActivity(activity);
        return num == 1;
    }

    /**
     * 用户修改活动内容与图片
     *
     * @param activityID
     * @param content
     * @param filename
     * @return
     */
    @Override
    public boolean alterActivity(int activityID, String content, String filename) {
        int num1 = iActivityDao.alterContent(activityID, content);
        int num2 = iActivityDao.saveImage(activityID, filename);
        return num1 == 1 && num2 == 1;
    }

    /**
     * 用户删除活动
     *
     * @param activityID
     * @return
     */
    @Override
    public boolean deleteActivity(int activityID) {
        int num = iActivityDao.deleteActivity(activityID);
        return num == 1;
    }

    /**
     * 分页搜索活动
     *
     * @param word
     * @param currentPage
     * @return
     */
    @Override
    public List<Activity> searchActivity(String word, int currentPage) {
        return iActivityDao.searchActivity(word, (currentPage-1)*10);
    }

    /**
     * 根据ID查询活动
     *
     * @param activityID
     * @return
     */
    @Override
    public Activity queryActivityByID(int activityID) {
        return iActivityDao.queryActivityByID(activityID);
    }

    /**
     * 查询相关活动结果的数量
     *
     * @param word
     * @return
     */
    @Override
    public int querySearchNum(String word) {
        int totalPage = iActivityDao.querySearchNum(word);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 查询用户发布的所有活动
     *
     * @param userID
     * @return
     */
    @Override
    public List<Activity> queryActivityByUser(int userID) {
        return iActivityDao.queryActivityByUser(userID);
    }

    /**
     * 查询所有活动总页数
     *
     * @return
     */
    @Override
    public int queryPageNum() {
        int totalPage = iActivityDao.queryPageNum();
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 查询某分类活动总数量
     *
     * @param type
     * @return
     */
    @Override
    public int querySelectedPageNum(String type) {
        int totalPage = iActivityDao.querySelectedPageNum(type);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 分页查看某个分类的活动
     *
     * @param type
     * @param currentPage
     * @return
     */
    @Override
    public List<Activity> queryActivityByPageByType(String type, int currentPage) {
        return iActivityDao.queryActivityByPageByType(type, (currentPage-1)*10);
    }

    /**
     * 分页查看所有活动
     *
     * @param currentPage
     * @return
     */
    @Override
    public List<Activity> queryActivityByPage(int currentPage) {
        return iActivityDao.queryActivityByPage((currentPage-1)*10);
    }
}
