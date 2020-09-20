package org.withyu.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.withyu.domain.Question;

import java.util.List;

@Repository
public interface IQuestionDao {
    /**
     * 发布询问
     *
     * @param question
     * @return
     */
    @Insert("insert into question(userID,content,image,issueTime,typeID,number) " +
            "values (#{question.userID}, #{question.content}, " +
            "#{question.image}, #{question.issueTime}, #{question.typeID}, #{question.number})")
    int issueQuestion(@Param("question") Question question);

    /**
     * 修改询问
     *
     * @param questionID
     * @param content
     * @return
     */
    @Update("update question set content=#{content} " +
            "where questionID=#{questionID}")
    int alterQuestion(@Param("questionID") int questionID,
                      @Param("content") String content);

    /**
     * 删除询问
     *
     * @param questionID
     * @return
     */
    @Delete("delete from question " +
            "where questionID=#{questionID}")
    int deleteQuestion(@Param("questionID") int questionID);

    /**
     * 查询全部数量
     *
     * @return
     */
    @Select("select count(*) from question")
    int queryAllNum();

    /**
     * 分页查看全部
     *
     * @param startPos
     * @return
     */
    @Select("select * from question " +
            "order by number/pow(TIMESTAMPDIFF(day, issueTime,now())+1,2) desc " +
            "limit #{startPos},10")
    List<Question> queryAllByPage(@Param("startPos") int startPos);

    /**
     * 查询某类数量
     *
     * @param typeID
     * @return
     */
    @Select("select count(*) from question where typeID=#{typeID}")
    int querySelectedNum(@Param("typeID") int typeID);

    /**
     * 分页查看某类
     *
     * @param typeID
     * @param currentPage
     * @return
     */
    @Select("select * from question " +
            "where typeID = #{typeID} " +
            "order by number/pow(TIMESTAMPDIFF(day, issueTime,now())+1,2) desc " +
            "limit  #{startPos},10")
    List<Question> querySelectedByPage(@Param("typeID") int typeID,
                                       @Param("startPos") int currentPage);





    /**
     * 分页查看全部，按照热门
     *
     * @param startPos
     * @return
     */
    @Select("select * from question " +
            "order by number desc " +
            "limit #{startPos},10")
    List<Question> queryAllByPageByHot(@Param("startPos") int startPos);


    /**
     * 分页查看某类，按照热门
     *
     * @param typeID
     * @param currentPage
     * @return
     */
    @Select("select * from question " +
            "where typeID = #{typeID} " +
            "order by number desc " +
            "limit  #{startPos},10")
    List<Question> querySelectedByPageByHot(@Param("typeID") int typeID,
                                       @Param("startPos") int currentPage);

    /**
     * 分页查看全部，按照最新
     *
     * @param startPos
     * @return
     */
    @Select("select * from question " +
            "order by issueTime desc " +
            "limit #{startPos},10")
    List<Question> queryAllByPageByNew(@Param("startPos") int startPos);


    /**
     * 分页查看某类，按照最新
     *
     * @param typeID
     * @param currentPage
     * @return
     */
    @Select("select * from question " +
            "where typeID = #{typeID} " +
            "order by issueTime desc " +
            "limit  #{startPos},10")
    List<Question> querySelectedByPageByNew(@Param("typeID") int typeID,
                                            @Param("startPos") int currentPage);






    /**
     * 查询搜索结果数量
     *
     * @param word
     * @return
     */
    @Select("select count(*) from question " +
            "where content like concat('%',#{word},'%') ")
    int querySearchNum(@Param("word") String word);

    /**
     * 分页查看搜索的询问
     *
     * @param word
     * @param startPos
     * @return
     */
    @Select("select * from question where content like concat('%',#{word},'%') order by number/pow(TIMESTAMPDIFF(day, issueTime,now())+1,2) desc limit #{startPos},10")
    List<Question> querySearchByPage(@Param("word") String word,
                                     @Param("startPos") int startPos);

    /**
     * 其他用户也有疑问
     *
     * @param questionID
     * @param userID
     * @return
     */
    @Insert("insert into haveQuestion values(#{questionID},#{userID})")
    int haveQuestionToo(@Param("questionID") int questionID,
                        @Param("userID") int userID);

    /**
     * 取消其他用户的疑问
     *
     * @param questionID
     * @param userID
     * @return
     */
    @Delete("delete from haveQuestion where questionID=#{questionID} and userID=#{userID}")
    int cancelhaveQuestionToo(@Param("questionID") int questionID,
                              @Param("userID") int userID);


    /**
     * 查询question详细信息
     *
     * @param questionID
     * @return
     */
    @Select("select * from question where questionID=#{questionID}")
    Question queryDetail(@Param("questionID") int questionID);

    /**
     * 查询某个用户发布的question
     *
     * @param userID
     * @return
     */
    @Select("select * from question where userID=#{userID} and typeID!=2")
    List<Question> queryCertainUserQuestion(@Param("userID") int userID);

    /**
     * 查询某个用户发布的社团活动
     *
     * @param userID
     * @return
     */
    @Select("select * from question where userID=#{userID} and typeID=2")
    List<Question> queryCertainUserActivity(@Param("userID") int userID);

    /**
     * 查询是否疑问了该活动
     *
     * @param userID
     * @param questionID
     * @return
     */
    @Select("select count(*) from haveQuestion where userID=#{userID} and questionID=#{questionID}")
    int queryHaveQuestion(@Param("userID") int userID,
                          @Param("questionID") int questionID);
}
