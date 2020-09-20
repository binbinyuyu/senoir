package org.withyu.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.withyu.dao.IUserDao;
import org.withyu.domain.User;
import org.withyu.service.UserService;
import org.withyu.util.CodeUtil;
import org.withyu.util.MailUtil;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private IUserDao iUserDao;
    int count = 10;

    /**
     * 检查登陆信息
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean checkLogin(String email, String password) {
        int num = iUserDao.check(email, password);
        return num == 1;
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //生成激活码
        String code = CodeUtil.generateUniqueCode();

        //将用户保存到数据库
        //保存成功则通过线程的方式给用户发送一封邮件
        int num = iUserDao.addUser(user, code);
        if (num == 1) {
            new Thread(new MailUtil(user.getEmail(), code, "register")).start();
            return true;
        } else
            return false;
    }

    /**
     * 根据userID，查询用户信息
     *
     * @param userID
     * @return
     */
    @Override
    public User queryUserByID(int userID) {
        return iUserDao.queryUserByID(userID);
    }

    /**
     * 根据email，查询用户信息
     *
     * @param email
     * @return
     */
    @Override
    public User queryUserByEmail(String email) {
        User user = iUserDao.queryUserByEmail(email);
        return user;
    }

    /**
     * 激活用户账号
     *
     * @param code
     * @return
     */
    @Override
    public boolean activeUser(String code) {
        int num = iUserDao.activeUser(code);
        return num == 1;
    }

    /**
     * 分页搜索用户
     *
     * @param word
     * @param currentPage
     * @return
     */
    @Override
    public List<User> searchUser(String word, int currentPage) {
        return iUserDao.searchUser(word, (currentPage-1)*10);
    }

    /**
     * 用户修改昵称、生日、性别
     *
     * @param userID
     * @param nickname
     * @param birthdate
     * @param sex
     * @return
     */
    @Override
    public boolean alterUserInfor(int userID, String nickname, String birthdate, String sex) {
        int num = iUserDao.alterInfor(userID, nickname, birthdate, sex);
        return num == 1;
    }

    /**
     * 重置未读消息为0
     *
     * @param userID
     * @return
     */
    @Override
    public boolean resetNewMsgCount(int userID) {
        int num = iUserDao.resetNewMsgCount(userID);
        return num == 1;
    }

    /**
     * 用户修改邮箱
     *
     * @param userID
     * @param email
     * @return
     */
    @Override
    public boolean alterUserEmail(int userID, String email) {
        //生成激活码
        String code = CodeUtil.generateUniqueCode();

        int num1 = iUserDao.alterEmail(userID, email);
        int num2 = iUserDao.deActiveUser(userID, code);
        if (num1 == 1 && num2 == 1) {
            new Thread(new MailUtil(email, code, "register")).start();
            return true;
        } else
            return false;
    }

    /**
     * 用户修改密码
     *
     * @param userID
     * @param password
     * @return
     */
    @Override
    public boolean alterUserPassword(int userID, String password) {
        int num = iUserDao.alterPassword(userID, password);
        return num == 1;
    }

    /**
     * 用户修改头像
     *
     * @param userID
     * @param filename
     * @return
     */
    @Override
    public boolean alterAvator(int userID, String filename) {
        int num = iUserDao.alterAvator(userID, filename);
        return num == 1;
    }

    /**
     * 查询搜索结果的页数
     *
     * @param word
     * @return
     */
    @Override
    public int querySearchNum(String word) {
        int totalPage = iUserDao.querySearchNum(word);
        if (totalPage % count == 0)
            totalPage = totalPage / count;
        else
            totalPage = totalPage / count + 1;
        if (totalPage == 0)
            totalPage = 1;
        return totalPage;
    }

    /**
     * 用户忘记密码，发送邮件重置密码
     *
     * @param email
     * @return
     */
    @Override
    public boolean forgetPassword(String email) {
        String code = iUserDao.queryUserByEmail(email).getCode();
        new Thread(new MailUtil(email, code, "resetpsd")).start();
        return true;
    }

    /**
     * 根据code，修改用户密码
     *
     * @param code
     * @param password
     * @return
     */
    @Override
    public boolean resetPassword(String code, String password) {
        int num = iUserDao.resetPassword(code, password);
        return num == 1;
    }
}
