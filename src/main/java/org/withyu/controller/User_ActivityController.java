package org.withyu.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.withyu.domain.Message;
import org.withyu.domain.Question;
import org.withyu.domain.User;
import org.withyu.service.*;
import org.withyu.util.CurrentTime;
import org.withyu.util.CurrentUserID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/user_activity")
public class User_ActivityController {
    @Autowired
    private User_ActivityService user_activityService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private QuestionService questionService;

    /**
     * 参加活动
     * 返回值status，1：参加成功，0：参加失败(系统错误)，2：已经参与该活动
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/join")
    public void joinActivity(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        int userID = CurrentUserID.getUserID(request);
//System.out.println("questionID:"+questionID);
        JSONObject jsonObject = new JSONObject();
        // 先判断该用户是否参与了这个活动
        boolean isJoined = user_activityService.isJoined(userID, questionID);

        if (isJoined == false) {// 未参加该活动
            boolean result = user_activityService.joinActivity(userID, questionID);
            if (result) {
                jsonObject.put("status", 1);// 参加成功

                // 向活动发布者发送message
                User user = userService.queryUserByID(userID);// 查询当前用户信息
                Question question = questionService.queryDetail(questionID);// 查询当前参加的活动信息
                int publisherID = question.getUserID();// 当前活动的发布者ID

                String title = "活动成员变化";
                String temp = question.getContent();
                if (temp.length() > 10)
                    temp = temp.substring(0, 10);
                String content = "昵称为：" + user.getNickname() + " 的用户参加了您内容为：" + temp + " ...的活动。";
                String sendTime = CurrentTime.getCurrentTime();
                int receiver = publisherID;
                Message msg = new Message(title, content, sendTime, receiver);

                messageService.sendMessage(msg);
            } else
                jsonObject.put("status", 0);
        } else// 已经参加了该活动
            jsonObject.put("status", 2);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
       //System.out.println(jsonObject.toString());
    }

    /**
     * 删除活动
     *
     * @param infor
     * @param response
     */
    @RequestMapping("/delete")
    public void deleteActivity(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");

        Question question = questionService.queryDetail(questionID);//查询当前活动的信息
        List<User> users = user_activityService.queryParticipantByActivityID(questionID);//查询活动的所有参与者

        String title = "活动解散";
        String temp = question.getContent();
        if (temp.length() > 10)
            temp = temp.substring(0, 10);
        String content = "您参加的内容为：" + temp + "...的活动" + "已被活动发布者解散。";
        String sendTime = CurrentTime.getCurrentTime();
        for (User user : users) {
            Message msg = new Message(title, content, sendTime, user.getUserID());
            messageService.sendMessage(msg);
        }

        boolean rs = user_activityService.deleteActivity(questionID);
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
     * 将用户从活动中踢出
     * 返回status，1：成功，0：失败
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/kickUser")
    public void kickUser(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        int userID = js.getInt("userID");

        boolean result = user_activityService.quitActivity(userID, questionID);
        JSONObject resultJson = new JSONObject();

        if (result == true) {
            resultJson.put("status", 1);

            // 向活动参与者发送message
            Question question = questionService.queryDetail(questionID);// 查询当前的活动信息

            String title = "活动成员变化";
            String temp = question.getContent();
            if (temp.length() > 10)
                temp = temp.substring(0, 10);
            String content = "您已被内容为：" + temp + "...的活动" + "的发布者从该活动中踢出。";
            String sendTime = CurrentTime.getCurrentTime();
            Message msg = new Message(title, content, sendTime, userID);

            messageService.sendMessage(msg);
        } else
            resultJson.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resultJson.toString());
    }

    /**
     * 查询活动的所有参与者
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryParticipant")
    public void queryParticipant(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int activityID = js.getInt("questionID");
        List<User> users = user_activityService.queryParticipantByActivityID(activityID);
        JSONArray jsonObject = JSONArray.fromObject(users);// 查询结果
        JSONArray temp = new JSONArray();// 中间结果
        // System.out.println(activityID);

        if (users != null && users.size() >= 0) {

            for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject job = jsonObject.getJSONObject(i);
                JSONObject rs = new JSONObject();

                rs.put("nickname", job.getString("nickname"));
                rs.put("userID", job.getString("userID"));
                rs.put("imgPath", job.getString("avator"));
                rs.put("school", job.getString("school"));
                rs.put("email", job.getString("email"));
                rs.put("joinTime", job.get("jointime"));
                temp.add(rs);
            }
        }
        JSONObject resultJson = new JSONObject();// 输出结果
        resultJson.put("status", 1);
        resultJson.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resultJson.toString());

        //System.out.println(resultJson.toString());
    }

    /**
     * 查询当前用户参与的活动
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryJoined")
    public void queryJoinedActivity(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int uID = js.getInt("userID");

        List<Question> questions = user_activityService.queryJoinedActivity(uID);

        JSONArray jsonObject = JSONArray.fromObject(questions);// 查询结果
        JSONArray result = new JSONArray();// 中间结果

        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject job = jsonObject.getJSONObject(i);
            JSONObject rs = new JSONObject();
            int userID = Integer.parseInt(job.getString("userID"));
            User user = userService.queryUserByID(userID);
            rs.put("nickname", user.getNickname());
            rs.put("questionID", job.getString("questionID"));
            rs.put("userID", job.getString("userID"));
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("imgPath", user.getAvator());
            result.add(rs);
        }

        JSONObject resultJson = new JSONObject();// 输出结果
        resultJson.put("status", 1);
        resultJson.put("message", result);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resultJson.toString());
    }

    /**
     * 退出活动
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/quit")
    public void quitActivity(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        int userID = CurrentUserID.getUserID(request);

        JSONObject jsonObject = new JSONObject();
        // 先判断该用户是否参与了这个活动
        boolean isJoined = user_activityService.isJoined(userID, questionID);

        if (isJoined == true) {// 参加了该活动
            boolean result = user_activityService.quitActivity(userID, questionID);
            if (result) {
                jsonObject.put("status", 1);// 退出成功

                // 向活动发布者发送message
                User user = userService.queryUserByID(userID);// 查询当前用户信息
                Question question = questionService.queryDetail(questionID);// 查询当前退出的活动信息
                int publisherID = question.getUserID();// 当前活动的发布者ID

                String title = "活动成员变化";
                String temp = question.getContent();
                if (temp.length() > 10)
                    temp = temp.substring(0, 10);
                String content = "昵称为：" + user.getNickname() + " 的用户退出了您内容为：" + temp + " ...的活动。";
                String sendTime = CurrentTime.getCurrentTime();
                int receiver = publisherID;
                Message msg = new Message(title, content, sendTime, receiver);
                messageService.sendMessage(msg);

            } else
                jsonObject.put("status", 0);
        } else// 未参加该活动
            jsonObject.put("status", 2);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 查看我发布的社团活动
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/myIssue")
    public void queryMyActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int uID = CurrentUserID.getUserID(request);
        //System.out.println(uID);
        List<Question> questions = questionService.queryCertainUserActivity(uID);
        JSONArray jsonObject = JSONArray.fromObject(questions);// 查询结果
        JSONArray result = new JSONArray();// 中间结果
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject job = jsonObject.getJSONObject(i);
            JSONObject rs = new JSONObject();
            int userID = Integer.parseInt(job.getString("userID"));
            User user = userService.queryUserByID(userID);
            rs.put("nickname", user.getNickname());
            rs.put("questionID", job.getString("questionID"));
            rs.put("userID", job.getString("userID"));
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("imgPath", user.getAvator());
            rs.put("flag", true);
            result.add(rs);
        }
        JSONObject re = new JSONObject();
        re.put("status", 1);
        re.put("message", result);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(re.toString());
        // System.out.println(re.toString());
    }
}
