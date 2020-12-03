
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.common.helper.AssertHelper;
import com.ddcoding.framework.console.exception.ExceptionForward;
import com.ddcoding.framework.persistent.entity.MasterSlaveJobSummary;
import com.ddcoding.framework.service.MasterSlaveJobService;
import com.ddcoding.framework.service.MasterSlaveJobSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 */
@Controller
@RequestMapping("/masterSlaveJobSummaries")
public class MasterSlaveJobSummaryController extends AbstractController {

    @Autowired
    private MasterSlaveJobSummaryService masterSlaveJobSummaryService;

    @Autowired
    private MasterSlaveJobService masterSlaveJobService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("jobSummaries", masterSlaveJobSummaryService.getAllJobSummaries());
        return "master_slave_job_summary_list";
    }

    @RequestMapping(value = "/{id}")
    public String input(@PathVariable String id, Model model) {
        AssertHelper.notEmpty(id, "cron can't be empty.");
        MasterSlaveJobSummary masterSlaveJobSummary = masterSlaveJobSummaryService.getJobSummary(id);
        model.addAttribute("jobSummary", masterSlaveJobSummary);
        model.addAttribute("jarFileNameList", masterSlaveJobService.getJarFileNameList(masterSlaveJobSummary.getGroupName(), masterSlaveJobSummary.getJobName()));
        return "master_slave_job_summary_input";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ExceptionForward("/masterSlaveJobSummaries")
    public String update(MasterSlaveJobSummary masterSlaveJobSummary) {
        AssertHelper.notEmpty(masterSlaveJobSummary.getJobCron(), "cron can't be empty.");
        masterSlaveJobSummaryService.saveJobSummary(masterSlaveJobSummary);
        return success("/masterSlaveJobSummaries");
    }

    @RequestMapping(value = "/{id}/synchronize")
    @ExceptionForward("/masterSlaveJobSummaries")
    public String synchronize(@PathVariable String id) {
        AssertHelper.notEmpty(id, "id can't be empty.");
        masterSlaveJobSummaryService.updateJobSummary(id);
        return success("/masterSlaveJobSummaries");
    }
}
