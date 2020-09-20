package org.withyu.service;

import org.withyu.domain.Comment;

import java.util.List;

public interface CommentService {
    boolean issueComment(Comment comment);

    boolean deleteComment(int commentID);

    boolean alterComment(int commentID, String content);

    int queryNum(int questionID);

    List<Comment> queryByPage(int questionID, int startPos);

    int getCommentNum(int questionID);
}
