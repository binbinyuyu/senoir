package org.withyu.service;

import org.apache.ibatis.annotations.Param;
import org.withyu.domain.Question;

import java.util.List;

public interface QuestionService {
    boolean issueQuestion(Question question);

    boolean alterQuestion(int questionID, String content);

    boolean deleteQuestion(int questionID);

    int queryAllNum();

    List<Question> queryAllByPage(int currentPage);

    int querySelectedNum(int type);

    List<Question> querySelectedByPage(int type, int currentPage);

    int querySearchNum(String word);

    List<Question> querySearchByPage(String word, int startPos);

    boolean haveQuestionToo(int questionID, int userID);

    boolean cancelhaveQuestionToo(int questionID, int userID);

    Question queryDetail(int questionID);

    List<Question> queryCertainUserQuestion(int userID);

    List<Question> queryCertainUserActivity(int userID);

    boolean queryHaveQuestion(int userID, int questionID);

    List<Question> queryAllByPageByHot( int startPos);
    List<Question> querySelectedByPageByHot( int typeID,int currentPage);
    List<Question> queryAllByPageByNew( int startPos);
    List<Question> querySelectedByPageByNew( int typeID,int currentPage);
}
