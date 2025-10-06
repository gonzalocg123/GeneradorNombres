package com.example;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class WriteExcel {
    public static final int NPERSONAS = 200000;
    public static void main(String[] args) throws IOException {
    try (Workbook wb = new XSSFWorkbook() ) {
            Sheet sheet = wb.createSheet("personas");

            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow(0);
            // Create a cell and put a value in it.
            Cell cell = row.createCell(0);
            cell.setCellValue("nombre");

            // Or do it on one line.
            row.createCell(1).setCellValue("apellidos");
            row.createCell(2).setCellValue("email");
            row.createCell(3).setCellValue("teléfono");
            row.createCell(4).setCellValue("género");

            ListaPersonas lp = new ListaPersonas();
            lp.loadData(
            "datos/nombre_mujeres.txt",
            "datos/nombre_hombres.txt",
            "datos/apellidos.txt",
            "datos/all_email.txt");
            lp.generaPersonas(NPERSONAS);

            for (int i = 0; i < NPERSONAS; i++) {
                Row fila = sheet.createRow(i+1);
                Persona p = lp.getPersonas().get(i);
                fila.createCell(0).setCellValue(p.getNombre());
                fila.createCell(1).setCellValue(p.getApellidos());
                fila.createCell(2).setCellValue(p.getEmail());
                fila.createCell(3).setCellValue(p.getTelefono());
                fila.createCell(4).setCellValue(p.getGenero().toString());
            }

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("datos/personas.xlsx")) {
                wb.write(fileOut);
            }
        }
    }
}


