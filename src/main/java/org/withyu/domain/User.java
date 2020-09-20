package org.withyu.domain;

import org.withyu.util.CurrentTime;

//-1表示未知
public class User {
    private int userID;
    private String name;
    private String nickname;
    private String password;
    private String school;
    private String sex;
    private String birthdate;
    private String avator;
    private String email;
    private String lastLoginDate;
    private String registerDate;
    private int authority;//0：管理员，1：普通用户
    private int state;// 1：激活。0:未激活
    private String code;//用户激活码
    private int new_msg_count;// 未读消息数量

    /**
     * 注册时用
     *
     * @param name
     * @param nickname
     * @param password
     * @param school
     * @param sex
     * @param birthdate
     * @param email
     * @param authority
     * @param state
     */
    public User(String name, String nickname, String password, String school, String sex, String birthdate, String email, int authority, int state) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.school = school;
        this.sex = sex;
        this.birthdate = birthdate;
        this.email = email;
        this.registerDate = CurrentTime.getCurrentTime();
        this.authority = authority;
        this.state = state;
    }

    public User(int userID, String name, String nickname, String password, String school, String sex, String birthdate, String avator, String email, String lastLoginDate, String registerDate, int authority, int state, String code, int new_msg_count) {
        this.userID = userID;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.school = school;
        this.sex = sex;
        this.birthdate = birthdate;
        this.avator = avator;
        this.email = email;
        this.lastLoginDate = lastLoginDate;
        this.registerDate = registerDate;
        this.authority = authority;
        this.state = state;
        this.code = code;
        this.new_msg_count = new_msg_count;
    }

    public User() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getNew_msg_count() {
        return new_msg_count;
    }

    public void setNew_msg_count(int new_msg_count) {
        this.new_msg_count = new_msg_count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", school='" + school + '\'' +
                ", sex='" + sex + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", avator='" + avator + '\'' +
                ", email='" + email + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", authority=" + authority +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", new_msg_count=" + new_msg_count +
                '}';
    }
}
