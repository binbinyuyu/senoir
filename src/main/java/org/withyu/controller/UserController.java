package org.withyu.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.withyu.domain.Question;
import org.withyu.domain.User;
import org.withyu.service.QuestionService;
import org.withyu.service.UserService;
import org.withyu.util.CurrentUserID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Base64.Decoder;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    /**
     * 判断用户名密码是否匹配，并将userID保存到session中
     * 返回status， 1：登陆成功，0：失败,2：账户未激活，3：账户未注册
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/login")
    public void login(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);//转换为json
        //获取参数
        String email = js.getString("email");
        String password = js.getString("password");

        User user = userService.queryUserByEmail(email);
        boolean rs = userService.checkLogin(email, password);

        JSONObject jsonObject = new JSONObject();
        if (user != null) {//账户已注册
            if (user.getState() == 1) {//已激活
                if (rs) {
                    //把用户登陆数据保存在session域对象中
                    HttpSession session = request.getSession();
                    session.setAttribute("userID", user.getUserID());
                    jsonObject.put("status", "1");//邮箱密码匹配
                } else
                    jsonObject.put("status", "0");//邮箱密码不匹配
            } else
                jsonObject.put("status", "2");//未激活

        } else
            jsonObject.put("status", "3");//账户未注册

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 注册,并且朝用户发送激活邮件
     * 返回status，1：注册成功，0：注册失败(系统错误)，2：注册未激活，3：注册已经激活
     *
     * @param infor
     */
    @RequestMapping("/register")
    public void register(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String name = js.getString("name");
        String nickname = js.getString("nickname");
        String password = js.getString("password");
        String school = js.getString("school");
        String sex = js.getString("sex");
        String birthdate = js.getString("birthdate");
        if (birthdate.length() < 2)
            birthdate = "1800-01-01";
        String email = js.getString("email");
        int authority = 1;// 普通用户
        int state = 0;// 未激活
        User user = new User(name, nickname, password, school, sex, birthdate, email, authority, state);

        boolean rs = userService.register(user);

        JSONObject jsonObject = new JSONObject();
        if (rs) {
            jsonObject.put("status", 1);// 注册成功，发送邮件
        } else {// 注册失败，判断失败原因（邮箱已经注册或系统错误）
            user = userService.queryUserByEmail(email);
            if (user != null) {
                if (user.getState() == 1)
                    jsonObject.put("status", 3);// 注册已激活
                else if (user.getState() == 0)
                    jsonObject.put("status", 2);// 注册未激活
            } else {
                jsonObject.put("status", 0);// 系统错误
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 激活账户，将数据库中的state由0变为1
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/active")
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        boolean rs = userService.activeUser(code);
        if (rs)
            response.sendRedirect("/withyou/success.html");
        else
            response.sendRedirect("/withyou/fail.html");
    }

    /**
     * 根据session判断是否已经登陆
     * 返回status，1：已经登陆，0：未登录
     *
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping("/isLogin")
    public void isLogin(HttpSession session, HttpServletResponse response) throws IOException {
        Object user = session.getAttribute("userID");
        int status = 0;
        if (user == null)
            status = 0;//未登录
        else
            status = 1;//已经登陆

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 修改用户密码
     *
     * @param infor
     * @param response
     */
    @RequestMapping("/alterPassword")
    public void alterPassword(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        String password = js.getString("password");

        boolean rs = userService.alterUserPassword(userID, password);

        JSONObject jsonObject = new JSONObject();
        if (rs == true)
            jsonObject.put("status", 1);
        else
            jsonObject.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 修改用户邮箱
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/alterEmail")
    public void alterEmail(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        String email = js.getString("email");

        boolean rs = userService.alterUserEmail(userID, email);

        JSONObject jsonObject = new JSONObject();
        if (rs == true)
            jsonObject.put("status", 1);
        else
            jsonObject.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 修改用户个人信息
     *
     * @param infor
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("/alterUserInfor")
    public void alterEmail(@RequestBody String infor, HttpServletResponse response, HttpServletRequest request) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);
        String nickname = js.getString("nickname");
        String birthdate = js.getString("birthdate");
        String sex = js.getString("sex");

        boolean rs = userService.alterUserInfor(userID, nickname, birthdate, sex);

        JSONObject jsonObject = new JSONObject();
        if (rs == true)
            jsonObject.put("status", 1);
        else
            jsonObject.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 忘记密码
     * 返回status，1：成功，0：失败，2该用户未注册
     *
     * @param infor
     * @param response
     */
    @RequestMapping("/forgetPassword")
    public void forgetPassword(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String email = js.getString("email");

        User user = userService.queryUserByEmail(email);
        JSONObject jsonObject = new JSONObject();

        if (user == null) {//该用户未注册
            jsonObject.put("status", 2);
        } else {//该用户注册了
            userService.forgetPassword(email);
            jsonObject.put("status", 1);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 获取当前用户的ID
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getUserID")
    public void getCurrentUserID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userID = CurrentUserID.getUserID(request);
        int new_msg_count = userService.queryUserByID(userID).getNew_msg_count();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("userID", userID);
        jsonObject.put("new_msg_count", new_msg_count);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        //System.out.println("request userID:"+userID);
    }

    /**
     * 获取当前用户的密码
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getPassword")
    public void getPassword(HttpSession session, HttpServletResponse response) throws IOException {
        int userID = (int) session.getAttribute("userID");
        User user = userService.queryUserByID(userID);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 1);
        jsonObject.put("password", user.getPassword());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
    }

    /**
     * 查询用户全部信息
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/queryAllInfor")
    public void queryUserInfor(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);

        int userID = Integer.parseInt(js.getString("userID"));
        // System.out.println(userID);
        User user = userService.queryUserByID(userID);

        JSONObject rs = new JSONObject();
        rs.put("name", user.getName());
        rs.put("nickname", user.getNickname());
        rs.put("password", user.getPassword());
        rs.put("school", user.getSchool());
        rs.put("sex", user.getSex());
        rs.put("birthdate", user.getBirthdate());
        rs.put("imgPath", user.getAvator());
        rs.put("email", user.getEmail());

        JSONObject resultJson = new JSONObject();// 输出结果
        resultJson.put("status", 1);
        resultJson.put("user", rs);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resultJson.toString());
        //System.out.println(resultJson.toString());
    }

    /**
     * 根据code，重新设置用户密码 返回status，1：成功，0：失败
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/resetPassword")
    public void resetPassword(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String code = js.getString("code");
        String password = js.getString("password");
        boolean rs = userService.resetPassword(code, password);
        JSONObject result = new JSONObject();
        if (rs) {
            result.put("status", 1);
        } else
            result.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result.toString());
    }

    /**
     * 搜索相关用户与询问，同时返回页数
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/searchActAndUser")
    public void searchActivityAndUser(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String word = js.getString("keywords");

        // 查询用户
        List<User> users = userService.searchUser(word, 1);
        JSONArray jsonArray = JSONArray.fromObject(users);// 查询结果
        JSONArray temp1 = new JSONArray();// 中间结果
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            JSONObject rs = new JSONObject();
            rs.put("userID", job.getInt("userID"));
            rs.put("nickname", job.getString("nickname"));
            rs.put("imgPath", job.getString("avator"));
            temp1.add(rs);
        }
        int userPage = userService.querySearchNum(word);

        //查询询问
        List<Question> questions = questionService.querySearchByPage(word, 1);
        jsonArray = JSONArray.fromObject(questions);
        JSONArray temp2 = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            JSONObject rs = new JSONObject();
            int userID = Integer.parseInt(job.getString("userID"));
            User user = userService.queryUserByID(userID);
            int questionID = job.getInt("questionID");
            rs.put("questionID", questionID);
            rs.put("userID", job.getInt("userID"));
            rs.put("nickname", user.getNickname());
            rs.put("imgPath", user.getAvator());
            rs.put("issueTime", job.getString("issueTime"));
            rs.put("content", job.getString("content"));
            rs.put("typeID", job.getString("typeID"));
            rs.put("haveQuestion", questionService.queryHaveQuestion(userID, questionID));

            temp2.add(rs);
        }
        int questionPage = questionService.querySearchNum(word);

        JSONObject resultJs = new JSONObject();// 输出结果
        resultJs.put("status", 1);
        resultJs.put("user", temp1);
        resultJs.put("userPage", userPage);
        resultJs.put("question", temp2);
        resultJs.put("questionPage", questionPage);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(resultJs.toString());
    }

    /**
     * 分页查询相关用户、询问
     *
     * @param infor
     * @param response
     * @throws IOException
     */
    @RequestMapping("/searchActAndUserByPage")
    public void searchActivityAndUserByPage(@RequestBody String infor, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        String word = js.getString("keywords");
        String type = js.getString("type");
        int currentPage = js.getInt("currentPage");
        // System.out.println(word+"||"+type+"||"+currentPage);

        User user = null;
        List<User> users = null;
        List<Question> questions = null;

        JSONObject result = new JSONObject();// 输出结果
        JSONArray jsonObject = null;// 查询结果
        JSONArray temp = new JSONArray();// 中间结果

        if (type.equals("question")) {//查询询问
            questions = questionService.querySearchByPage(word, currentPage);
            jsonObject = JSONArray.fromObject(questions);

            for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject job = jsonObject.getJSONObject(i);
                JSONObject rs = new JSONObject();
                int userID = Integer.parseInt(job.getString("userID"));
                user = userService.queryUserByID(userID);
                int questionID = job.getInt("questionID");

                rs.put("questionID", questionID);
                rs.put("userID", job.getInt("userID"));
                rs.put("nickname", user.getNickname());
                rs.put("issueTime", job.getString("issueTime"));
                rs.put("content", job.getString("content"));
                rs.put("imgPath", user.getAvator());
                rs.put("typeID", job.getInt("typeID"));
                rs.put("haveQuestion", questionService.queryHaveQuestion(userID, questionID));

                temp.add(rs);
            }
            result.put("status", 1);
            result.put("question", temp);
        } else {//查询用户
            users = userService.searchUser(word, currentPage);
            jsonObject = JSONArray.fromObject(users);
            for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject job = jsonObject.getJSONObject(i);
                JSONObject rs = new JSONObject();
                rs.put("userID", job.getInt("userID"));
                rs.put("nickname", job.getString("nickname"));
                rs.put("imgPath", job.getString("avator"));
                temp.add(rs);
                result.put("status", 1);
                result.put("user", temp);
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result.toString());
        // System.out.println(result.toString());
    }


    /**
     * 上传头像
     *
     * @param infor
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/uploadHead")
    public void uploadHead(@RequestBody String infor, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject js = JSONObject.fromObject(infor);
        int userID = CurrentUserID.getUserID(request);

        // 去掉base64编码的头部 如："data:image/jpeg;base64," 如果不去，转换的图片不可以查看
        String file = js.getString("file");
        file = file.substring(23);

        // 解码
        Decoder decoder = Base64.getDecoder();
        byte[] imgByte = decoder.decode(file);

        // 文件名
        String filename = getPhotoNewName("png");

        try {
            String path = request.getSession().getServletContext().getRealPath("/");
            //System.out.println(path);
            FileOutputStream out2 = new FileOutputStream(path + "img/" + filename); // 输出文件路径
            out2.write(imgByte);
            out2.close();
            //System.out.println("头像上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean rs = userService.alterAvator(userID, filename);

        JSONObject result = new JSONObject();
        if (rs == true)
            result.put("status", 1);
        else
            result.put("status", 0);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result.toString());
    }

    /**
     * 这个函数的功能是获取当前时间点与1970年的间隔秒数
     *
     * @param date
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        System.out.println(timestamp);
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    /**
     * 得到新的照片名称
     *
     * @param ext
     * @return
     */
    public static String getPhotoNewName(String ext) {
        Date date = new Date();
        int second = getSecondTimestamp(date);
        String fileName = String.valueOf(second) + "." + ext;
        return fileName;
    }
}
