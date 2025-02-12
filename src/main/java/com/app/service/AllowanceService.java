package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.Allowance;
import com.app.repo.AllowanceRepo;

@Service
public class AllowanceService {
	
	@Autowired
	private AllowanceRepo allowanceRepo;
	
	
	public String setAllowance(Allowance allowance) {
		allowanceRepo.deleteAll();
		return "Allowance set to" + allowanceRepo.save(allowance);
	}
	public int getAllowance() {
		if(allowanceRepo.findAll().isEmpty()) {
			return 0;
		}
		return allowanceRepo.findAll().get(0).getAllowancePercentage();
	}
	
	

}
