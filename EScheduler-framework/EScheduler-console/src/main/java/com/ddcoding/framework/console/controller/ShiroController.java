package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.console.exception.ExceptionForward;
import com.ddcoding.framework.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 */
@Controller
@RequestMapping("/shiro")
public class ShiroController extends AbstractController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login() {
        return "shiro_login";
    }

    @RequestMapping(value = "/password")
    public String password() {
        return "shiro_password";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String changePassword(String password) {
        String username = getUsernameAndCheck();
        userService.updatePassword(username, password);
        return success("/dashboard/index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ExceptionForward("/shiro/login")
    public String login(HttpServletRequest request) {
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if (UnknownAccountException.class.getName().equals(exception)) {
            failed("Unknown account.");
        } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
            failed("Incorrect password.");
        } else {
            LoggerHelper.error("unknown error : " + exception);
            failed("Unknown error.");
        }
        return "shiro_login";
    }

}
