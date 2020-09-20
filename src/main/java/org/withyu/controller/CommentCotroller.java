package org.withyu.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.withyu.domain.Comment;
import org.withyu.domain.Message;
import org.withyu.domain.Question;
import org.withyu.domain.User;
import org.withyu.service.CommentService;
import org.withyu.service.MessageService;
import org.withyu.service.QuestionService;
import org.withyu.service.UserService;
import org.withyu.util.CurrentTime;
import org.withyu.util.CurrentUserID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentCotroller {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private MessageService messageService;

    /**
     * 发布评论
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/issue")
    public void issue(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String content = js.getString("content");
        int questionID = js.getInt("questionID");
        int userID = CurrentUserID.getUserID(request);
        //System.out.println(questionID);

        Comment comment = new Comment(questionID, userID, content);
        boolean rs = commentService.issueComment(comment);
        JSONObject jsonObject = new JSONObject();
        if (rs) {
            jsonObject.put("status", 1);

            // 向活动发布者发送message
            User user = userService.queryUserByID(userID);// 查询当前用户信息
            Question question = questionService.queryDetail(questionID);// 查询当前评论的活动信息
            int publisherID = question.getUserID();// 当前活动的发布者ID

            String title = "有人评论了你的问题";
            String temp = question.getContent();
            if (temp.length() > 10)
                temp = temp.substring(0, 10);
            if (content.length() > 10)
                content = content.substring(0, 10);
            String contents = "昵称为：" + user.getNickname() + " 的用户评论了您内容为：" + temp + " ...的问题。" +
                    " 评论内容为：" + content + "...";
            String sendTime = CurrentTime.getCurrentTime();
            int receiver = publisherID;
            Message msg = new Message(title, contents, sendTime, receiver);

            messageService.sendMessage(msg);


        } else
            jsonObject.put("status", 0);
        //System.out.println(jsonObject.toString());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 删除评论
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/delete")
    public void delete(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int commentID = js.getInt("commentID");

        boolean rs = commentService.deleteComment(commentID);
        JSONObject jsonObject = new JSONObject();
        if (rs) {
            jsonObject.put("status", 1);
        } else
            jsonObject.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }


    /**
     * 修改评论
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/alter")
    public void alter(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int commentID = js.getInt("commentID");
        String content = js.getString("content");

        boolean rs = commentService.alterComment(commentID, content);
        JSONObject jsonObject = new JSONObject();
        if (rs) {
            jsonObject.put("status", 1);
        } else
            jsonObject.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 查看前十个评论，同时返回页数
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("queryNum")
    public void queryNum(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");

        int num = commentService.queryNum(questionID);
        List<Comment> comments = commentService.queryByPage(questionID, 1);

        JSONArray result = JSONArray.fromObject(comments);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < result.size(); i++) {
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();
            User user = userService.queryUserByID(job.getInt("userID"));

            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("commentID", job.getInt("commentID"));
            rs.put("userID", job.getInt("userID"));
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("imagePath", user.getAvator());
            temp.add(rs);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);
        jsonObject.put("totalPage", num);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 分页查看评论
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("queryPage")
    public void queryPage(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        int currentPage = js.getInt("currentPage");

        List<Comment> comments = commentService.queryByPage(questionID, currentPage);

        JSONArray result = JSONArray.fromObject(comments);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < result.size(); i++) {
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();
            User user = userService.queryUserByID(job.getInt("userID"));

            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("commentID", job.getInt("commentID"));
            rs.put("userID", job.getInt("userID"));
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("imagePath", user.getAvator());
            temp.add(rs);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

}
