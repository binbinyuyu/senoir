package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.IQuestionDao;
import org.withyu.domain.Question;
import org.withyu.service.QuestionService;

import java.util.List;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private IQuestionDao iQuestionDao;
    int count = 10;

    /**
     * 发布询问
     *
     * @param question
     * @return
     */
    @Override
    public boolean issueQuestion(Question question) {
        int num = iQuestionDao.issueQuestion(question);
        return num == 1;
    }

    /**
     * 修改询问
     *
     * @param questionID
     * @param content
     * @return
     */
    @Override
    public boolean alterQuestion(int questionID, String content) {
        int num = iQuestionDao.alterQuestion(questionID, content);
        return num == 1;
    }

    /**
     * 删除询问
     *
     * @param questionID
     * @return
     */
    @Override
    public boolean deleteQuestion(int questionID) {
        int num = iQuestionDao.deleteQuestion(questionID);
        return num == 1;
    }

    /**
     * 查询全部页数
     *
     * @return
     */
    @Override
    public int queryAllNum() {
        int totalPage = iQuestionDao.queryAllNum();
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 分页查看全部
     *
     * @param currentPage
     * @return
     */
    @Override
    public List<Question> queryAllByPage(int currentPage) {
        return iQuestionDao.queryAllByPage((currentPage - 1) * 10);
    }

    /**
     * 查询某类页数
     *
     * @param type
     * @return
     */
    @Override
    public int querySelectedNum(int type) {
        int totalPage = iQuestionDao.querySelectedNum(type);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 分页查看某类
     *
     * @param type
     * @param currentPage
     * @return
     */
    @Override
    public List<Question> querySelectedByPage(int type, int currentPage) {
        return iQuestionDao.querySelectedByPage(type, (currentPage - 1) * count);
    }


    /**
     * 查询相关询问数量
     *
     * @param word
     * @return
     */
    @Override
    public int querySearchNum(String word) {
        int totalPage = iQuestionDao.querySearchNum(word);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 分页查询相关活动
     *
     * @param word
     * @param startPos
     * @return
     */
    @Override
    public List<Question> querySearchByPage(String word, int startPos) {
        return iQuestionDao.querySearchByPage(word, (startPos - 1) * count);
    }

    /**
     * 其他用户也有疑问
     *
     * @param questionID
     * @param userID
     * @return
     */
    @Override
    public boolean haveQuestionToo(int questionID, int userID) {
        int num = iQuestionDao.haveQuestionToo(questionID, userID);
        return num == 1;
    }

    /**
     * 取消其他用户的疑问
     *
     * @param questionID
     * @param userID
     * @return
     */
    @Override
    public boolean cancelhaveQuestionToo(int questionID, int userID) {
        int num = iQuestionDao.cancelhaveQuestionToo(questionID, userID);
        return num == 1;
    }


    /**
     * 查询question详细信息
     *
     * @param questionID
     * @return
     */
    @Override
    public Question queryDetail(int questionID) {
        return iQuestionDao.queryDetail(questionID);
    }

    /**
     * 查询某个用户发布的question
     *
     * @param userID
     * @return
     */
    @Override
    public List<Question> queryCertainUserQuestion(int userID) {
        return iQuestionDao.queryCertainUserQuestion(userID);
    }

    /**
     * 查询某个用户发布的社团活动
     *
     * @param userID
     * @return
     */
    @Override
    public List<Question> queryCertainUserActivity(int userID) {
        return iQuestionDao.queryCertainUserActivity(userID);
    }

    /**
     * 查询用户是否疑问过了
     *
     * @param userID
     * @param questionID
     * @return
     */
    @Override
    public boolean queryHaveQuestion(int userID, int questionID) {
        int num = iQuestionDao.queryHaveQuestion(userID, questionID);
        return num == 1;
    }

    @Override
    public List<Question> queryAllByPageByHot(int startPos) {
        return iQuestionDao.queryAllByPageByHot((startPos - 1) * count);
    }

    @Override
    public List<Question> querySelectedByPageByHot(int typeID, int currentPage) {
        return iQuestionDao.querySelectedByPageByHot(typeID, (currentPage - 1) * count);
    }

    @Override
    public List<Question> queryAllByPageByNew(int startPos) {
        return iQuestionDao.queryAllByPageByNew((startPos - 1) * count);
    }

    @Override
    public List<Question> querySelectedByPageByNew(int typeID, int currentPage) {
        return iQuestionDao.querySelectedByPageByNew(typeID, (currentPage - 1) * count);
    }
}
