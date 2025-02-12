package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Allowance;
import com.app.service.AllowanceService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/allowance")
@CrossOrigin(origins = "http://localhost:5173")
public class AllowanceController {

	@Autowired
	private AllowanceService allowanceService;
	
	@PostMapping("")
	public String setAllowance(@RequestBody Allowance allowance) {
		//TODO: process POST request
		
		return allowanceService.setAllowance(allowance);
	}
	@GetMapping("")
	public int getAllowance() {
		return allowanceService.getAllowance();
	}
	
	
}
