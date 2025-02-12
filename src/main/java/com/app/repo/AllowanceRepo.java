package com.app.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.model.Allowance;

public interface AllowanceRepo extends MongoRepository<Allowance, String> {

}
