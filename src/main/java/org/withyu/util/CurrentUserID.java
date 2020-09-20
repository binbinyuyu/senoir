package org.withyu.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CurrentUserID {
    /**
     * 获取当前用户userID
     *
     * @param request
     * @return
     */
    public static int getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        return userID;
    }
}
