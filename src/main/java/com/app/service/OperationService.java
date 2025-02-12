package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.OperationData;
import com.app.repo.OperationRepo;

@Service
public class OperationService {

	  @Autowired
	    private OperationRepo operationRepo;
	  @Autowired
	  private AllowanceService allowanceService;

	    public OperationData createData(OperationData data) {
	        // ✅ Ensure operatorId is unique
	        Optional<OperationData> existingData = operationRepo.findByOperatorId(data.getOperatorId());
	        if (existingData.isPresent()) {
	            throw new IllegalArgumentException("Operator ID already exists: " + data.getOperatorId());
	        }

	        if (data.getLapsMS() == null || data.getLapsMS().isEmpty()) {
	            throw new IllegalArgumentException("LapsMS list cannot be empty");
	        }

	        // ✅ Convert lapsMS (milliseconds) to laps (mm:ss:SSS format)
	        List<String> formattedLaps = data.getLapsMS().stream()
	                .map(this::formatDuration)
	                .collect(Collectors.toList());

	        // ✅ Calculate average time in milliseconds
	        long avgMillis = calculateAverageTime(data.getLapsMS());
	        String avgTime = formatDuration(avgMillis);

	        // ✅ Calculate allowance time
	        long allowanceMillis = avgMillis + ((allowanceService.getAllowance() * avgMillis) / 100);
	        String allowanceTime = formatDuration(allowanceMillis);

	        // ✅ Calculate PPH (Production Per Hour)
	        int pph = (int) ((3600 * 1000) / allowanceMillis);  // 3600 sec * 1000 ms

	        // ✅ Set values in object
	        data.setLaps(formattedLaps);
	        data.setAvgTime(avgTime);
	        data.setAllowanceTime(allowanceTime);
	        data.setPph(pph);

	        return operationRepo.save(data);
	    }
	    
	    

	    // ✅ Convert milliseconds to mm:ss:SSS format
	    private String formatDuration(long millisStr) {
	        long millis = millisStr;
	        long minutes = (millis / 60000) % 60;
	        long seconds = (millis / 1000) % 60;
	        long centiseconds = (millis % 1000) / 10; // Convert ms to centiseconds (SS)

	        return String.format("%02d:%02d:%02d", minutes, seconds, centiseconds);
	    }

	    // ✅ Calculate the average time from a list of milliseconds
	    private long calculateAverageTime(List<Long> lapsMS) {
	        if (lapsMS == null || lapsMS.isEmpty()) {
	            return 0; // Handle case where list is empty to avoid division by zero
	        }
	        
	        long totalMillis = lapsMS.stream().mapToLong(Long::longValue).sum();
	        return totalMillis / lapsMS.size();
	    }


	    // ✅ Fetch data by operator ID
	    public OperationData getDataByOperatorId(String operatorId) {
	        return operationRepo.findByOperatorId(operatorId)
	                .orElseThrow(() -> new IllegalArgumentException("No data found for operator ID: " + operatorId));
	    }

	    // ✅ Get all operation data
	    public List<OperationData> getAllData() {
	        return operationRepo.findAll();
	    }

	    // ✅ Delete data by operator ID
	    public String deleteDataByOperatorId(String operatorId) {
	        OperationData data = getDataByOperatorId(operatorId);
	        operationRepo.delete(data);
	        return "Data of " + data.getName() +", Operator Id"+ data.getOperatorId() +" deleted"; 
	    }



	    public List<OperationData> updateAllowance(int updatedAllowance) {
	        // ✅ Fetch all records from the database
	        List<OperationData> operations = getAllData();

	        for (OperationData data : operations) {
	            // ✅ Calculate new allowance time
	            long avgMillis = calculateAverageTime(data.getLapsMS());
	            long newAllowanceMillis = avgMillis + ((updatedAllowance * avgMillis) / 100);
	            String newAllowanceTime = formatDuration(newAllowanceMillis);

	            // ✅ Calculate new PPH (Production Per Hour)
	            int newPph = (int) ((3600 * 1000) / newAllowanceMillis);

	            // ✅ Update values in the object
	            data.setAllowanceTime(newAllowanceTime);
	            data.setPph(newPph);
	        }

	        // ✅ Save updated records back into the database
	        return operationRepo.saveAll(operations);
	    }



		public void deleteAll() {
			// TODO Auto-generated method stub
			operationRepo.deleteAll();
			
		}



		public boolean operatorExists(String operatorId) {
			// TODO Auto-generated method stub
			return operationRepo.existsByOperatorId(operatorId);
		}


		
		
}
