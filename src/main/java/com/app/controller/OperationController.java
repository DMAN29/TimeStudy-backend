package com.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.model.OperationData;
import com.app.service.OperationService;
import com.app.service.ExcelExportService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OperationController {

    @Autowired
    private OperationService operationService;
    
    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping("/submit")
    public ResponseEntity<OperationData> createData(@RequestBody OperationData data){
        return new ResponseEntity<>(operationService.createData(data), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<OperationData>> getAllData(){
        return new ResponseEntity<>(operationService.getAllData(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteData(@PathVariable String id){
        return new ResponseEntity<>(operationService.deleteDataByOperatorId(id), HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-table/{val}")
    public ResponseEntity<List<OperationData>> updateAllowance(@PathVariable int val) {
        return new ResponseEntity<>(operationService.updateAllowance(val), HttpStatus.OK);
    }

    // ✅ New API to Export Data to Excel
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        byte[] excelData = excelExportService.exportOperationsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("OperationData.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
    @GetMapping("/export-and-clear")
    public ResponseEntity<byte[]> exportAndClear() throws IOException {
        byte[] excelData = excelExportService.exportOperationsToExcel();

        // Clear database after exporting
        operationService.deleteAll();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("OperationsData.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
    
    @GetMapping("/exists/{operatorId}")
    public ResponseEntity<Boolean> checkIfOperatorExists(@PathVariable String operatorId) {
        boolean exists = operationService.operatorExists(operatorId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    private int lapCount = 10; // Default lap count

    @GetMapping("/lap-count")
    public ResponseEntity<Integer> getLapCount() {
        return new ResponseEntity<>(lapCount, HttpStatus.OK);
    }

    @PutMapping("/update-lap-count")
    public ResponseEntity<Void> updateLapCount(@RequestBody Map<String, Integer> request) {
        lapCount = request.get("lapCount");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
