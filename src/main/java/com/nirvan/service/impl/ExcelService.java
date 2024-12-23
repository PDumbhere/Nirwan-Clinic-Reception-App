package com.nirvan.service.impl;

import com.nirvan.entity.Patient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelService {

    public void exportPatientsToExcel(List<Patient> patients, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Patients");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "DOB", "Sex", "Type", "Treatment", "Phone", "Address"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Populate rows with patient data
        int rowIndex = 1;
        for (Patient patient : patients) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(patient.getPatientId());
            row.createCell(1).setCellValue(patient.getName());
            row.createCell(2).setCellValue(patient.getDob().toString());
            row.createCell(3).setCellValue(patient.getSex().toString());
            row.createCell(4).setCellValue(patient.getPatientType());
            row.createCell(5).setCellValue(patient.getTreatment());
            row.createCell(6).setCellValue(patient.getPhone());
            row.createCell(7).setCellValue(patient.getAddress());
        }

        // Adjust column widths
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }


}

