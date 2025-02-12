package com.app.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="time study")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationData {

	@Id
	private String id;

	@Indexed(unique = true)
	private String operatorId;
	private String operation;
	private String name;
	private String section;
	private List<String> laps; // mm:ss:SSS format
	private List<Long> lapsMS; // Received in request (milliseconds)
	private String avgTime; // Average time in mm:ss:SSS
	private int pph; // Production Per Hour
	private String allowanceTime; // Allowance-adjusted time

}
