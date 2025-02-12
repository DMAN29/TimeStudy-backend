package com.app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.app.model.OperationData;
import com.app.repo.OperationRepo;

@Service
public class ExcelExportService {

    private final OperationRepo operationRepository;

    public ExcelExportService(OperationRepo operationRepository) {
        this.operationRepository = operationRepository;
    }

    public byte[] exportOperationsToExcel() throws IOException {
        List<OperationData> operations = operationRepository.findAll();

        if (operations.isEmpty()) {
            return new byte[0]; // Return empty file if no data exists
        }

        // Determine the lap count from the first record (assuming all have the same count)
        int lapCount = operations.get(0).getLaps().size();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Operations");

            // Create header row dynamically
            Row headerRow = sheet.createRow(0);

            String[] staticHeaders = { "S. No", "Operator ID", "Name", "Operation", "Section" };

            // Write static headers
            for (int i = 0; i < staticHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(staticHeaders[i]);
                applyHeaderStyle(workbook, cell);
            }

            // Add dynamic lap headers
            int columnIndex = staticHeaders.length;
            for (int i = 1; i <= lapCount; i++) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue("Lap " + i);
                applyHeaderStyle(workbook, cell);
            }

            // Add remaining static headers
            String[] finalHeaders = { "Avg Time", "Allowance Time", "PPH" };
            for (String finalHeader : finalHeaders) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue(finalHeader);
                applyHeaderStyle(workbook, cell);
            }

            // Populate data rows
            int rowNum = 1;
            for (OperationData operation : operations) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(rowNum - 1); // S. No
                row.createCell(1).setCellValue(operation.getOperatorId());
                row.createCell(2).setCellValue(operation.getName());
                row.createCell(3).setCellValue(operation.getOperation());
                row.createCell(4).setCellValue(operation.getSection());

                // Insert lap times dynamically
                List<String> lapTimes = operation.getLaps();
                columnIndex = staticHeaders.length;
                for (int i = 0; i < lapCount; i++) {
                    row.createCell(columnIndex++).setCellValue(lapTimes.get(i));
                }

                // Insert remaining fields
                row.createCell(columnIndex++).setCellValue(operation.getAvgTime());
                row.createCell(columnIndex++).setCellValue(operation.getAllowanceTime());
                row.createCell(columnIndex++).setCellValue(operation.getPph());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void applyHeaderStyle(Workbook workbook, Cell cell) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
    }
}
