package org.withyu.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.withyu.domain.Comment;

import java.util.List;

@Repository
public interface ICommentDao {
    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Insert("insert into comment(questionID,userID,content,isToOwner,targetCommentID,issueTime) " +
            "values (#{comment.questionID}, #{comment.userID}, #{comment.content}, " +
            "#{comment.isToOwner}, #{comment.targetCommentID}, #{comment.issueTime})")
    int issueComment(@Param("comment") Comment comment);

    /**
     * 删除评论
     * @param commentID
     * @return
     */
    @Delete("delete from comment where commentID=#{commentID}")
    int deleteComment(@Param("commentID") int commentID);

    /**
     * 修改评论
     * @param commentID
     * @param content
     * @return
     */
    @Update("update comment set content=#{content} where commentID=#{commentID}")
    int alterComment(@Param("commentID")  int commentID,
                     @Param("content") String content);

    /**
     * 查询所有评论数量
     * @return
     */
    @Select("select count(*) from comment where questionID =#{questionID} ")
    int queryNum(@Param("questionID") int questionID);

    /**
     * 分页查看评论
     * @param startPos
     * @return
     */
    @Select("select * from comment where questionID =#{questionID} order by issueTime limit #{startPos},10")
    List<Comment> queryCommentByPage(@Param("questionID") int questionID,
                                     @Param("startPos") int startPos);
}
