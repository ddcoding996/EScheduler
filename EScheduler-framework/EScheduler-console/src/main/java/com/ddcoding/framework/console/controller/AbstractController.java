
package com.ddcoding.framework.console.controller;

import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 */
public abstract class AbstractController {

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected String getUsername() {
        HttpSession session = getSession();
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("userName");
    }

    protected String getUsernameAndCheck() {
        String username = getUsername();
        if (username == null) {
            throw new UnknownAccountException();
        }
        return username;
    }

    protected String success(String url) {
        getRequest().setAttribute("message",
                "<div class=\"alert alert-success alert-block\"> " +
                        "<a class=\"close\" data-dismiss=\"alert\" href=\"#\">×</a>" +
                        "<h4 class=\"alert-heading\">Success!</h4>Operation successfully!</div>");
        return "forward:" + url;
    }

    protected void failed(String message) {
        getRequest().setAttribute("message", message);
    }

    protected String getDirectoryRealPath(String path) {
        String dirPath = getRequest().getServletContext().getRealPath(path);
        if (dirPath.endsWith("/")) {
            dirPath = dirPath.substring(0, dirPath.length() - 1);
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

}
