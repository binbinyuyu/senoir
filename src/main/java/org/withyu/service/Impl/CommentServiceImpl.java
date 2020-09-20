package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.ICommentDao;
import org.withyu.domain.Comment;
import org.withyu.service.CommentService;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private ICommentDao iCommentDao;
    int count = 10;

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Override
    public boolean issueComment(Comment comment) {
        int num = iCommentDao.issueComment(comment);
        return num==1;
    }

    /**
     * 删除评论
     * @param commentID
     * @return
     */
    @Override
    public boolean deleteComment(int commentID) {
        int num = iCommentDao.deleteComment(commentID);
        return num==1;
    }

    /**
     * 修改评论
     * @param commentID
     * @param content
     * @return
     */
    @Override
    public boolean alterComment(int commentID, String content) {
        int num = iCommentDao.alterComment(commentID,content);
        return  num==1;
    }

    /**
     * 查询评论页数
     * @return
     */
    @Override
    public int queryNum(int questionID) {
        int totalPage = iCommentDao.queryNum(questionID);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 分页查看评论
     * @param questionID
     * @param startPos
     * @return
     */
    @Override
    public List<Comment> queryByPage(int questionID,int startPos) {
       return iCommentDao.queryCommentByPage(questionID,(startPos-1)*count);
    }

    /**
     * 查询评论数量
     * @param questionID
     * @return
     */
    @Override
    public int getCommentNum(int questionID) {
        return iCommentDao.queryNum(questionID);
    }

}
