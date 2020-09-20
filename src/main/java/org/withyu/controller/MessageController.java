package org.withyu.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.withyu.domain.Message;
import org.withyu.service.MessageService;
import org.withyu.service.UserService;
import org.withyu.util.CurrentUserID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    /**
     * 查询用户收到的所有信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/get")
    public void getMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int uID = CurrentUserID.getUserID(request);

        List<Message> messages = messageService.queryMessage(uID);

        JSONArray jsonObject = JSONArray.fromObject(messages);// 查询结果
        JSONArray temp = new JSONArray();// 中间结果
        for (int i = 0; i < jsonObject.size(); i++) {
            JSONObject job = jsonObject.getJSONObject(i);
            JSONObject rs = new JSONObject();
            int messageID = job.getInt("messageID");
            rs.put("messageID", messageID);
            String title = job.getString("title");
            rs.put("title", title);
            String content = job.getString("content");
            rs.put("content", content);
            String sendTime = job.getString("sendTime");
            rs.put("sendTime", sendTime);
            int receiver = job.getInt("receiver");
            rs.put("receiver", receiver);
            int isRead = job.getInt("isRead");
            rs.put("isRead", isRead);
            temp.add(rs);
        }

        JSONObject result = new JSONObject();// 输出结果
        result.put("status", 1);
        result.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result.toString());
    }

    /**
     * 将用户消息设置为已读，并设置new——msg——count为0
     * @param
     * @param request
     */
    @RequestMapping("/setRead")
    public void setRead(HttpServletRequest request) {
        int userID = CurrentUserID.getUserID(request);
        userService.resetNewMsgCount(userID);
        messageService.setMessageRead(userID);
    }
}
