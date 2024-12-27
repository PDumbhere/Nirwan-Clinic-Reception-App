package com.nirvan.service.impl;

import com.nirvan.constants.Sex;
import com.nirvan.entity.Patient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {

    public void exportPatientsToExcel(List<Patient> patients, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Patients");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "DOB", "Sex", "Type", "Treatment", "Phone", "Address","Registered On","Updated On"};

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
            row.createCell(8).setCellValue(String.valueOf(patient.getRegistrationDateTime()));
            row.createCell(9).setCellValue(String.valueOf(patient.getUpdatedDateTime()));
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

    public List<Patient> importPatientsFromExcel(File file) {

        List<Patient> patients = new ArrayList<>();
        try(Workbook workbook = WorkbookFactory.create(file)){
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for(Row row : sheet){
                if(row.getRowNum()==0){
                    continue;
                }
                Patient patient = new Patient();
                if(!dataFormatter.formatCellValue(row.getCell(0)).isBlank())
                    patient.setPatientId(Long.parseLong(dataFormatter.formatCellValue(row.getCell(0))));
                patient.setName(dataFormatter.formatCellValue(row.getCell(1)));
                patient.setDob(LocalDateTime.parse(dataFormatter.formatCellValue(row.getCell(2))));
                patient.setSex(Sex.valueOf(dataFormatter.formatCellValue(row.getCell(3))));
                patient.setPatientType(dataFormatter.formatCellValue(row.getCell(4)));
                patient.setTreatment(dataFormatter.formatCellValue(row.getCell(5)));
                patient.setPhone(Long.parseLong(dataFormatter.formatCellValue(row.getCell(6))));
                patient.setAddress(dataFormatter.formatCellValue(row.getCell(7)));
                if(!dataFormatter.formatCellValue(row.getCell(8)).isBlank())
                    patient.setRegistrationDateTime(LocalDateTime.parse(dataFormatter.formatCellValue(row.getCell(8))));
                if(!dataFormatter.formatCellValue(row.getCell(9)).isBlank())
                    patient.setUpdatedDateTime(LocalDateTime.parse(dataFormatter.formatCellValue(row.getCell(9))));
                patients.add(patient);
            }
            return patients;
        }catch (IOException ioException){
            ioException.printStackTrace();
            throw new RuntimeException(ioException.getMessage());
        }
    }
}

