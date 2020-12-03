
package com.ddcoding.framework.console.controller;

import com.ddcoding.framework.service.StandbyNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 */
@Controller
@RequestMapping("/standbyNodes")
public class StandbyNodeController extends AbstractController {

    @Autowired
    private StandbyNodeService standbyNodeService;

    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("nodes", standbyNodeService.getAllNodes());
        return "standby_node_list";
    }

}
