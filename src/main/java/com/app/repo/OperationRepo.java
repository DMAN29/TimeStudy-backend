package com.app.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.app.model.OperationData;

@Repository
public interface OperationRepo extends MongoRepository<OperationData, String>{
	Optional<OperationData> findByOperatorId(String operatorId);

	boolean existsByOperatorId(String operatorId);
}