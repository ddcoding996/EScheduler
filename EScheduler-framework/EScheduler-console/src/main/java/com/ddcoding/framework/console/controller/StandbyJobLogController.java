
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.service.StandbyJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/standbyJobLogs")
public class StandbyJobLogController extends AbstractController {

    @Autowired
    private StandbyJobLogService standbyJobLogService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("jobLogs", standbyJobLogService.getAllJobLogs());
        return "standby_job_log_list";
    }

}
