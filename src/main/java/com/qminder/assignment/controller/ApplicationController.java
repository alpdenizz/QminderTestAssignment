package com.qminder.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.qminder.assignment.service.BurgerJointService;

@Controller
public class ApplicationController {
	
	@Autowired
	private BurgerJointService service;
	
	@GetMapping("/")
    public String index(Model model) {
		long before = System.currentTimeMillis();
		model.addAttribute("joints", service.getBurgerJointsForDisplay());
		//model.addAttribute("joints", List.of(new BurgerJoint("A","B","")));
		System.out.println("[+] took secs: "+(System.currentTimeMillis()-before)/1000.0);
		return "index";
    }
}
