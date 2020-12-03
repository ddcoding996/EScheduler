
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.service.MasterSlaveNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/masterSlaveNodes")
public class MasterSlaveNodeController extends AbstractController {

    @Autowired
    private MasterSlaveNodeService masterSlaveNodeService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("nodes", masterSlaveNodeService.getAllNodes());
        return "master_slave_node_list";
    }

}
