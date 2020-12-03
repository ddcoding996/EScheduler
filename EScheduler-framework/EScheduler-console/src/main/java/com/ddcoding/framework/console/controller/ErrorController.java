
package com.ddcoding.framework.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/error")
public class ErrorController extends AbstractController {

    @RequestMapping(value = "/403")
    public String login() {
        return "error_403";
    }

}
