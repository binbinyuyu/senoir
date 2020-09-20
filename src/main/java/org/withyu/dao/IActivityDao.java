package org.withyu.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.withyu.domain.Activity;

import java.util.List;

@Repository
public interface IActivityDao {
    int count = 10;//每页显示分页数量

    /**
     * 发布活动的时候，同时自己也加入活动(不能参加自己发布的活动)
     *
     * @param activity
     * @return
     */
    @Insert("insert into activity(userID, content, image, issueTime, number, type) " +
            "values ( #{activity.userID}, #{activity.content}, #{activity.image}," +
            "#{activity.issueTime}, #{activity.number}, #{activity.type})")
    int issueActivity(@Param("activity") Activity activity);

    /**
     * 修改活动内容
     *
     * @param activityID
     * @param content
     * @return
     */
    @Update("update activity set content = #{content} " +
            "where activityID = #{activityID}")
    int alterContent(@Param("activityID") int activityID,
                     @Param("content") String content);

    /**
     * 修改活动图片
     *
     * @param activityID
     * @param imagePath
     * @return
     */
    @Update("update activity set image = #{image} " +
            "where activityID = #{activityID}")
    int saveImage(@Param("activityID") int activityID,
                  @Param("image") String imagePath);

    /**
     * 删除发布的活动
     *
     * @param activityID
     * @return
     */
    @Delete("delete from activity " +
            "where activityID = #{activityID}")
    int deleteActivity(@Param("activityID") int activityID);

    /**
     * 分页搜索活动
     *
     * @param word
     * @param currentPage
     * @return
     */
    @Select("select * from activity " +
            "where content like concat('%',#{word},'%') " +
            "order by issueTime desc " +
            "limit #{startPos},10")
    List<Activity> searchActivity(@Param("word") String word,
                                  @Param("startPos") int currentPage);

    /**
     * 查询搜索活动结果的数量
     *
     * @param word
     * @return
     */
    @Select("select count(*) from activity " +
            "where content like concat('%',#{word},'%') ")
    int querySearchNum(@Param("word") String word);

    /**
     * 根据ID查询活动
     *
     * @param activityID
     * @return
     */
    @Select("select * from activity " +
            "where activityID = #{activityID}")
    Activity queryActivityByID(@Param("activityID") int activityID);

    /**
     * 查询用户发布的所有活动
     *
     * @param userID
     * @return
     */
    @Select("select * from activity " +
            "where userID = #{userID} " +
            "order by issueTime desc")
    List<Activity> queryActivityByUser(@Param("userID") int userID);

    /**
     * 查询所有活动总数量
     *
     * @return
     */
    @Select("select count(*) from activity")
    int queryPageNum();

    /**
     * 查询某分类活动总数量
     *
     * @param type
     * @return
     */
    @Select("select count(*) from activity " +
            "where type = #{type}")
    int querySelectedPageNum(@Param("type") String type);

    /**
     * 分页查看某个分类的活动
     *
     * @param type
     * @param currentPage
     * @return
     */
    @Select("select * from activity " +
            "where type = #{type} " +
            "order by issueTime desc  " +
            "limit  #{startPos},10")
    List<Activity> queryActivityByPageByType(@Param("type") String type,
                                             @Param("startPos") int currentPage);

    /**
     * 分页查看所有活动
     *
     * @param startPos
     * @return
     */
    @Select("select * from activity " +
            "order by issueTime desc  " +
            "limit #{startPos},10")
    List<Activity> queryActivityByPage(@Param("startPos") int startPos);
}
