package org.withyu.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.withyu.domain.User;

import java.util.List;

@Repository
public interface IUserDao {
    int count = 10;//每页显示分页数量

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    @Insert("insert into user(name, nickname, password, school, sex, birthdate, email, registerDate, authority, code) " +
            "values(#{user.name}, #{user.nickname}, #{user.password}, " +
            "#{user.school}, #{user.sex}, #{user.birthdate}, " +
            "#{user.email}, #{user.registerDate}, #{user.authority}, #{code})")
    int addUser(@Param("user") User user, @Param("code") String code);

    /**
     * 用户修改昵称、生日、性别
     *
     * @param userID
     * @param nickname
     * @param birthdate
     * @param sex
     * @return
     */
    @Update("update user set nickname = #{nickname} ,birthdate = #{birthdate} ,sex = #{sex} " +
            "where userID = #{userID}")
    int alterInfor(@Param("userID") int userID,
                   @Param("nickname") String nickname,
                   @Param("birthdate") String birthdate,
                   @Param("sex") String sex);

    /**
     * 用户修改邮箱
     *
     * @param userID
     * @param email
     * @return
     */
    @Update("update user set email = #{email} " +
            "where userID = #{userID}")
    int alterEmail(@Param("userID") int userID,
                   @Param("email") String email);

    /**
     * 用户修改密码
     *
     * @param userID
     * @param password
     * @return
     */
    @Update("update user set password = #{password} " +
            "where userID = #{userID}")
    int alterPassword(@Param("userID") int userID,
                      @Param("password") String password);

    /**
     * 用户修改头像
     *
     * @param userID
     * @param filename
     * @return
     */
    @Update("update user set avator = #{filename} " +
            "where userID = #{userID}")
    int alterAvator(@Param("userID") int userID,
                    @Param("filename") String filename);

    /**
     * 根据code，修改用户密码
     *
     * @param code
     * @param password
     * @return
     */
    @Update("update user set password = #{password} " +
            "where code = #{code}")
    int resetPassword(@Param("code") String code,
                      @Param("password") String password);

    /**
     * 根据email，查询用户信息
     *
     * @param email
     * @return
     */
    @Select("select * from user " +
            "where email = #{email}")
    User queryUserByEmail(@Param("email") String email);

    /**
     * 根据userID，查询用户信息
     *
     * @param userID
     * @return
     */
    @Select("select * from user " +
            "where userID = #{userID}")
    User queryUserByID(@Param("userID") int userID);

    /**
     * 激活用户账号
     *
     * @param code
     * @return
     */
    @Update("update user set state = 1 " +
            "where code = #{code}")
    int activeUser(@Param("code") String code);

    /**
     * 取消激活用户账号
     *
     * @param userID
     * @param code
     * @return
     */
    @Update("update user set state = 0,code = #{code} " +
            "where userID = #{userID}")
    int deActiveUser(@Param("userID") int userID,
                     @Param("code") String code);

    /**
     * 重置未读消息为0
     *
     * @param userID
     * @return
     */
    @Update("update user set new_msg_count = 0 " +
            "where userID = #{userID}")
    int resetNewMsgCount(@Param("userID") int userID);


    /**
     * 检查登陆信息
     *
     * @param email
     * @param upwd
     * @return
     */
    @Select("select count(*) from user where email = #{email} and password = #{upwd}")
    int check(@Param("email") String email, @Param("upwd") String upwd);

    /**
     * 查询搜索用户结果的数量
     *
     * @param word
     * @return
     */
    @Select("select count(*) from user" +
            " where nickname like concat('%',#{word},'%') and authority = 1 ")
    int querySearchNum(@Param("word") String word);

    /**
     * 分页搜索用户
     *
     * @param word
     * @param currentPage
     * @return
     */
    @Select("select * from user " +
            "where nickname like concat('%',#{word},'%') and authority = 1 " +
            "order by registerDate desc " +
            "limit #{startPos},10 ")
    List<User> searchUser(@Param("word") String word,
                          @Param("startPos") int currentPage);
}
