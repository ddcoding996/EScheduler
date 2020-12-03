
package com.ddcoding.framework.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/masterSlaveDashboard")
public class MasterSlaveDashboardController extends AbstractController {

    @RequestMapping(value = "/index")
    public String index() {
        return "master_slave_dashboard_index";
    }

}
