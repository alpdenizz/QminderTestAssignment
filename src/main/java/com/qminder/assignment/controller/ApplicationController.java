package com.qminder.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.qminder.assignment.service.BurgerJointService;

@Controller
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired
	private BurgerJointService service;
	
	/**
	 * 
	 * HTTP GET Endpoint, after this it will try to get the burger joints
	 */
	@GetMapping("/")
    public String index(Model model) throws Exception {
		long before = System.currentTimeMillis();
		
		//sets the joints for thymeleaf engine
		model.addAttribute("joints", service.getBurgerJointsForDisplay());
		
		//reporting how long it took
		logger.info("[+] took secs: "+(System.currentTimeMillis()-before)/1000.0);
		
		//to index.html for display
		return "index";
    }
}
