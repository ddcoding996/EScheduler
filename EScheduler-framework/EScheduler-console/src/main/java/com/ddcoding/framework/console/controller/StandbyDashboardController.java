
package com.ddcoding.framework.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/standbyDashboard")
public class StandbyDashboardController extends AbstractController {

    @RequestMapping(value = "/index")
    public String index() {
        return "standby_dashboard_index";
    }

}
