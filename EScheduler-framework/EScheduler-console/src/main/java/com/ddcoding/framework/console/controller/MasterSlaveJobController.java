package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.common.exception.ESchedulerException;
import com.ddcoding.framework.common.helper.AssertHelper;
import com.ddcoding.framework.common.helper.IOHelper;
import com.ddcoding.framework.console.exception.ExceptionForward;
import com.ddcoding.framework.service.MasterSlaveJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 */
@Controller
@RequestMapping("/masterSlaveJobs")
public class MasterSlaveJobController extends AbstractController {

    @Autowired
    private MasterSlaveJobService masterSlaveJobService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("jobs", masterSlaveJobService.getAllJobs());
        return "master_slave_job_list";
    }

    @RequestMapping(value = "/upload")
    public String upload() {
        return "master_slave_job_upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ExceptionForward("/masterSlaveJobSummaries")
    public String upload(String packagesToScan, @RequestParam MultipartFile jobJar) {
        AssertHelper.notNull(jobJar, "jobJar can't be null.");
        AssertHelper.notEmpty(packagesToScan, "packagesToScan can't be empty.");
        String jarFilePath = getDirectoryRealPath("job/masterSlave") + "/" + jobJar.getOriginalFilename();
        try {
            IOHelper.writeFile(jarFilePath, jobJar.getBytes());
            masterSlaveJobService.saveJob(jarFilePath, packagesToScan);
        } catch (IOException e) {
            throw new ESchedulerException(e);
        }
        return "forward:/masterSlaveJobSummaries";
    }

}
