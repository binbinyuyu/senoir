package org.withyu.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.withyu.domain.Question;
import org.withyu.domain.User;
import org.withyu.service.CommentService;
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
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    /**
     * 发布询问
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/issue")
    public void issueQuestion(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        String content = js.getString("content");
        String issueTime = CurrentTime.getCurrentTime();
        String image = "unfinished";
        int typeID = js.getInt("typeID");
        Question question = new Question(userID, content, image, issueTime, typeID);

        boolean rs = questionService.issueQuestion(question);
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
     * 表单方式发布询问
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/issueForm")
    public void issueQuestionByForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        public void issueQuestionByForm(@RequestParam("typeID") int typeID, @RequestParam("content") String content, HttpServletRequest request, HttpServletResponse response) throws IOException {

        int userID = CurrentUserID.getUserID(request);
        //int userID = 1;
        String issueTime = CurrentTime.getCurrentTime();
        String image = "unfinished";

        String typeID = request.getParameter("typeID");
        String content = request.getParameter("content");

        //System.out.println(typeID + "||" + content);
        Question question = new Question(userID, content, image, issueTime, Integer.parseInt(typeID));


        boolean rs = questionService.issueQuestion(question);
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
     * 修改询问
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/alter")
    public void alterQuestion(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        String content = js.getString("content");
        //System.out.println(content);
        boolean rs = questionService.alterQuestion(questionID, content);
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
     * 删除询问
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/delete")
    public void deleteQuestion(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");

        boolean rs = questionService.deleteQuestion(questionID);
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
     * 查询某类，同时返回页数
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryNum")
    public void querySelectedPageNum(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID = CurrentUserID.getUserID(request);
        JSONObject js = JSONObject.fromObject(infor);
        int typeID = js.getInt("typeID");
        int sortingMethod = js.getInt("method");//0:综合,1:热议,2:最新
//        System.out.println("typeID："+typeID);
//        System.out.println("sortingMethod:"+sortingMethod);

        List<Question> questions = null;
        int totalPage;
        if (typeID == 0) {//查询全部
            totalPage = questionService.queryAllNum();
            if (sortingMethod == 0) {
                questions = questionService.queryAllByPage(1);
            } else if (sortingMethod == 1) {
                questions = questionService.queryAllByPageByHot(1);
            } else if (sortingMethod == 2) {
                questions = questionService.queryAllByPageByNew(1);
            }
        } else {//查询分类
            totalPage = questionService.querySelectedNum(typeID);
            if (sortingMethod == 0) {
                questions = questionService.querySelectedByPage(typeID, 1);
            } else if (sortingMethod == 1) {
                questions = questionService.querySelectedByPageByHot(typeID, 1);
            } else if (sortingMethod == 2) {
                questions = questionService.querySelectedByPageByNew(typeID, 1);
            }
        }

        JSONArray result = JSONArray.fromObject(questions);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < result.size(); i++) {
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();
            User user = userService.queryUserByID(job.getInt("userID"));
            int questionID = job.getInt("questionID");

            rs.put("questionID", questionID);
            rs.put("userID", job.getInt("userID"));
            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("typeID", job.getString("typeID"));

            rs.put("typeContent", getContent(job.getInt("typeID")));
            rs.put("agreeNum", job.getInt("number"));
            rs.put("commentNum", commentService.getCommentNum(questionID));

            rs.put("haveQuestion", questionService.queryHaveQuestion(userID, questionID));
            temp.add(rs);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);
        jsonObject.put("totalPage", totalPage);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());

        // System.out.println("queryresult:"+jsonObject.toString());
    }

    /**
     * 分页查看询问
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryPage")
    public void queryByPage(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID = CurrentUserID.getUserID(request);

        JSONObject js = JSONObject.fromObject(infor);
        int typeID = js.getInt("type");
        int currentPage = js.getInt("currentPage");
        int sortingMethod = js.getInt("method");//0:综合,1:热议,2:最新
//        System.out.println("typeID："+typeID);
//        System.out.println("sortingMethod："+sortingMethod);
//        System.out.println("currentPage："+currentPage);

        //System.out.println("type:"+typeID+"---currentPage:"+currentPage);
        List<Question> questions = null;
        if (typeID == 0) {//查询全部
            if (sortingMethod == 0) {
                questions = questionService.queryAllByPage(currentPage);
                // System.out.println("method0");
            } else if (sortingMethod == 1) {
                questions = questionService.queryAllByPageByHot(currentPage);
            } else if (sortingMethod == 2) {
                questions = questionService.queryAllByPageByNew(currentPage);
            }
        } else {//查询分类
            if (sortingMethod == 0) {
                questions = questionService.querySelectedByPage(typeID, currentPage);
            } else if (sortingMethod == 1) {
                questions = questionService.querySelectedByPageByHot(typeID, currentPage);
            } else if (sortingMethod == 2) {
                questions = questionService.querySelectedByPageByNew(typeID, currentPage);
            }
        }

        JSONArray result = JSONArray.fromObject(questions);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < result.size(); i++) {
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();
            User user = userService.queryUserByID(job.getInt("userID"));
            int questionID = job.getInt("questionID");

            rs.put("questionID", job.getInt("questionID"));
            rs.put("userID", job.getInt("userID"));
            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("typeID", job.getString("typeID"));
            rs.put("haveQuestion", questionService.queryHaveQuestion(userID, questionID));
            rs.put("typeContent", getContent(job.getInt("typeID")));
            rs.put("agreeNum", job.getInt("number"));
            rs.put("commentNum", commentService.getCommentNum(questionID));
            temp.add(rs);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());

        //System.out.println(jsonObject.toString());
    }

    /**
     * 其他用户也有疑问
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/havetoo")
    public void haveQuestionToo(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        int questionID = js.getInt("questionID");
        //System.out.println("questionID:"+questionID);
        //        System.out.println("userID:"+userID);
        //System.out.println("顶一顶");

        int status = 0;
        if (questionService.queryHaveQuestion(userID, questionID)) {//已经疑问过了,顶失败
            status = 0;
        } else {//没有疑问过,顶成功
            if (questionService.haveQuestionToo(questionID, userID))
                status = 1;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        //System.out.println(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 取消其他用户的疑问
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/cancelhavetoo")
    public void cancelHaveQuestionToo(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        int questionID = js.getInt("questionID");
        int status = 0;
        //System.out.println("取消顶一顶");
        boolean rs = questionService.cancelhaveQuestionToo(questionID, userID);
        if (rs)
            status = 1;
        else
            status = 0;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        //System.out.println(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 查询question的详细信息
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/detail")
    public void queryDetail(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int questionID = js.getInt("questionID");
        //System.out.println(questionID);
        Question question = questionService.queryDetail(questionID);
        User user = userService.queryUserByID(question.getUserID());

        JSONObject message = new JSONObject();
        message.put("questionID", question.getQuestionID());
        message.put("userID", question.getUserID());
        message.put("nickname", user.getNickname());
        message.put("imgPath", user.getAvator());
        message.put("issueTime", question.getIssueTime());
        message.put("content", question.getContent());
        message.put("number", question.getNumber());
        message.put("typeID", question.getTypeID());
        message.put("typeContent", getContent(question.getQuestionID()));
        message.put("agreeNum", question.getNumber());
        message.put("commentNum", commentService.getCommentNum(questionID));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", message);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());

        //System.out.println(jsonObject.toString());
    }

    /**
     * 查询我发布的疑问
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryMy")
    public void queryMyQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID = CurrentUserID.getUserID(request);
        //System.out.println(userID);
        List<Question> questions = questionService.queryCertainUserQuestion(userID);
        JSONArray result = JSONArray.fromObject(questions);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < result.size(); i++) {
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();
            User user = userService.queryUserByID(job.getInt("userID"));

            rs.put("questionID", job.getInt("questionID"));
            rs.put("userID", job.getInt("userID"));
            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("typeID", job.getString("typeID"));
            rs.put("typeContent", getContent(job.getInt("typeID")));
            rs.put("agreeNum", job.getInt("number"));
            rs.put("commentNum", commentService.getCommentNum(job.getInt("questionID")));
            rs.put("flag", true);
            temp.add(rs);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        //System.out.println(jsonObject.toString());
    }

    /**
     * 热门问题
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/hotquestion")
    public void queryhotquestion(HttpServletRequest request, HttpServletResponse response) throws IOException {


        List<Question> questions = null;


        questions = questionService.querySelectedByPage(4, 1);
        JSONArray result = JSONArray.fromObject(questions);
        JSONArray temp = new JSONArray();//中间结果
        for (int i = 0; i < 5; i++) {//result.size()
            JSONObject job = result.getJSONObject(i);
            JSONObject rs = new JSONObject();

            rs.put("questionID", job.getInt("questionID"));

            String content = job.getString("content");
            if (content.length() > 15)
                content = content.substring(0, 15);
            content = content + "...";
            rs.put("content", content);
            rs.put("typeContent", getContent(job.getInt("typeID")));
            rs.put("agreeNum", job.getInt("number"));
            rs.put("commentNum", commentService.getCommentNum(job.getInt("questionID")));

            temp.add(rs);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("message", temp);

        JSONObject js = new JSONObject();
        js.put("status", 1);
        js.put("message", temp);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(js.toString());

        //System.out.println(jsonObject.toString());
    }

    public static String getContent(int i) {
        String content = "其他";
        switch (i) {
            case 1:
                content = "生活疑惑";
                break;
            case 2:
                content = "社团活动";
                break;
            case 3:
                content = "失物招领";
                break;
            case 4:
                content = "课堂学习";
                break;
            case 5:
                content = "其他";
                break;
        }
        return content;
    }
}
