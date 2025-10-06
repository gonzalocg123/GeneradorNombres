package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


/**
 * Clase principal para la aplicación de generación de nombres.
 * Contiene el método main que inicia la aplicación.
 */
public class App {
    public static final int CARAS = 6;
    public static int[] numeros = new int[CARAS];

    public static void main( String[] args ) {
    /*
     * // generar una persona "a mano"
        System.out.println( "Generando persona.." );
        Persona p = new Persona("Gonzalo", "Rodríguez", "+34123456789", "pepe@gmail.com", null, Genero.HOMBRE);
        System.out.println(p.toString());
        
        // generar una persona aleatoria
        System.out.println("Cargando datos de personas...");
        ListaPersonas lp = new ListaPersonas();
     */
        
        ListaPersonas lp = new ListaPersonas();

        try {
            lp.loadData(
                "datos/nombre_mujeres.txt",
                "datos/nombre_hombres.txt",
                "datos/apellidos.txt",
                "datos/all_email.txt"
            );
            // System.out.println("Datos cargados.");
            
            int totalGeneradas = lp.generaPersonas(100);
            System.out.println("Se generaron " + totalGeneradas + " personas:");
            
            for (Persona persona : lp.getPersonas()) {
                System.out.println(persona);
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try {
            System.setProperty(
                "javax.xml.bind.JAXBContextFactory", 
                "org.eclipse.persistence.jaxb.JAXBContextFactory");
            JAXBContext contexto = 
                JAXBContext.newInstance(lp.getClass());

            Marshaller marshaller = 
                contexto.createMarshaller();
            marshaller.setProperty(
                Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, 
                "application/json");
            marshaller.setProperty(
                MarshallerProperties.JSON_INCLUDE_ROOT, true);
            marshaller.marshal(
                lp, new File("datos/personas.json"));

            Unmarshaller unmarshaller = 
                contexto.createUnmarshaller();
            unmarshaller.setProperty(
                UnmarshallerProperties.MEDIA_TYPE, 
                "application/json");
            unmarshaller.setProperty(
                UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
            ListaPersonas lpXml = (ListaPersonas)
                unmarshaller.unmarshal(new File("datos/personas.json"));

            for (Persona p : lpXml.getPersonas()) {
                System.out.println(p.toString());
            }
            
        } catch (Exception e) {
            System.out.println("Imposible generar el archivo XML: "+e.getMessage());
            e.printStackTrace();
        }
        
        // Ejemplo de Serialización y Deserialización

        try (FileOutputStream fos = new FileOutputStream("datos/personas.bin")){
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lp);
            // fos.close(); 
            // No hace falta porque está el try con recursos
        } catch (Exception e) {
            System.out.println("imposible guardar lista de personas");
        }
        

        try (FileInputStream fis = new FileInputStream("datos/personas.bin")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            ListaPersonas lpBinario = (ListaPersonas)ois.readObject();
            for (Persona p : lpBinario.getPersonas()) {
                System.out.println(p.toString());
            }
        } catch (Exception e) {
            System.out.println("Imposible mostrar la lista de personas");
        }

        // Guardar Excel 
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Personas");

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Nombre");
            header.createCell(1).setCellValue("Apellidos");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Teléfono");
            header.createCell(4).setCellValue("Género");

            List<Persona> personas = lp.getPersonas();
            for (int i = 0; i < personas.size(); i++) {
                Persona p = personas.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(p.getNombre());
                row.createCell(1).setCellValue(p.getApellidos());
                row.createCell(2).setCellValue(p.getEmail());
                row.createCell(3).setCellValue(p.getTelefono());
                row.createCell(4).setCellValue(p.getGenero().toString());
            }

            try (FileOutputStream fos = new FileOutputStream("datos/personas.xlsx")) {
                wb.write(fos);
            }

        } catch (Exception e) {
            System.err.println("Error al guardar Excel: " + e.getMessage());
            e.printStackTrace();
        }




        System.out.println("Fin.");

/**
 * // comprobar que funcione bien el dado
        // Primera Forma
        for(int i = 0; i < CARAS; i++) {
            numeros[i] = 0;
        }
        // Segunda Forma
        // Arrays.fill(numeros, 0);

        for (int i = 0; i < 1000; i++) {
            numeros[lp.dado(CARAS)]++;
        }
        for(int i = 0; i < CARAS; i++) {
            System.out.println("Han salido %d numeros del numero %d".formatted(numeros[i], i) );
        }

        for (int i = 0; i < 100; i++) {
            System.out.println("Dado de 6 sale: " + lp.dado(CARAS) );
        }
    
 *  */
    }
}