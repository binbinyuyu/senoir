package org.withyu.service;

import org.withyu.domain.User;

import java.util.List;

public interface UserService {

    public boolean checkLogin(String email, String password);

    public boolean register(User user);

    public User queryUserByID(int userID);


    public User queryUserByEmail(String email);

    public boolean activeUser(String code);

    public List<User> searchUser(String word, int currentPage);

    public boolean alterUserInfor(int userID, String nickname, String birthdate, String sex);

    public boolean resetNewMsgCount(int userID);

    public boolean alterUserEmail(int userID, String email);

    public boolean alterUserPassword(int userID, String password);

    public boolean alterAvator(int userID, String filename);

    public int querySearchNum(String word);

    public boolean forgetPassword(String email);

    public boolean resetPassword(String code, String password);
}
