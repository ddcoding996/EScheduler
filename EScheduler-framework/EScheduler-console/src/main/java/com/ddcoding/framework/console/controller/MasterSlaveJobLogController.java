
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.service.MasterSlaveJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/masterSlaveJobLogs")
public class MasterSlaveJobLogController extends AbstractController {

    @Autowired
    private MasterSlaveJobLogService masterSlaveJobLogService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("jobLogs", masterSlaveJobLogService.getAllJobLogs());
        return "master_slave_job_log_list";
    }

}
