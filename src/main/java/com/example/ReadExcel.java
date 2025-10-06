package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReadExcel {
    public static void main(String[] args) throws IOException {

        try ( FileInputStream fis = new FileInputStream(
            "datos/personas.xlsx")  ) {

            Workbook wb = new XSSFWorkbook(fis);
            Sheet hoja = wb.getSheetAt(0);

            List<Persona> lista = new ArrayList<Persona>();
            
            Row cabecera = hoja.getRow(0);
            // deberíamos ver cada celda de la 
            // cabecera para hacer los getters y setters "a medida"

            int nFilas = hoja.getLastRowNum();
            for (int i = 1; i < nFilas; i++) {
                Row filaActual = hoja.getRow(i);
                Persona p = new Persona(
                    filaActual.getCell(0).getStringCellValue(), 
                    filaActual.getCell(1).getStringCellValue(), 
                    filaActual.getCell(2).getStringCellValue(), 
                    filaActual.getCell(3).getStringCellValue(), 
                    Genero.valueOf(filaActual.getCell(4).getStringCellValue()));
                lista.add(p);
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
