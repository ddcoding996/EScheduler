
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.common.helper.AssertHelper;
import com.ddcoding.framework.console.exception.ExceptionForward;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;
import com.ddcoding.framework.service.StandbyJobService;
import com.ddcoding.framework.service.StandbyJobSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 */
@Controller
@RequestMapping("/standbyJobSummaries")
public class StandbyJobSummaryController extends AbstractController {

    @Autowired
    private StandbyJobSummaryService standbyJobSummaryService;

    @Autowired
    private StandbyJobService standbyJobService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("jobSummaries", standbyJobSummaryService.getAllJobSummaries());
        return "standby_job_summary_list";
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public String input(@PathVariable String id, Model model) {
        StandbyJobSummary standbyJobSummary = standbyJobSummaryService.getJobSummary(id);
        model.addAttribute("jobSummary", standbyJobSummary);
        model.addAttribute("jarFileNameList", standbyJobService.getJarFileNameList(standbyJobSummary.getGroupName(), standbyJobSummary.getJobName()));
        return "standby_job_summary_input";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ExceptionForward("/standbyJobSummaries")
    public String update(StandbyJobSummary standbyJobSummary) {
        AssertHelper.notEmpty(standbyJobSummary.getJobCron(), "cron can't be empty.");
        standbyJobSummaryService.saveJobSummary(standbyJobSummary);
        return success("/standbyJobSummaries");
    }

    @RequestMapping(value = "/{id}/synchronize")
    @ExceptionForward("/standbyJobSummaries")
    public String synchronize(@PathVariable String id) {
        AssertHelper.notEmpty(id, "id can't be empty.");
        standbyJobSummaryService.updateJobSummary(id);
        return success("/standbyJobSummaries");
    }

}
