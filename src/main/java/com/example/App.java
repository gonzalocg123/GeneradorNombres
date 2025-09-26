package com.example;

/**
 * Clase principal para la aplicación de generación de nombres.
 * Contiene el método main que inicia la aplicación.
 */
public class App {
    public static final int CARAS = 6;
    public static int[] numeros = new int[CARAS];

    public static void main( String[] args ) {

        // generar una persona "a mano"
        System.out.println( "Generando persona.." );
        Persona p = new Persona("Gonzalo", "Rodríguez", "+34123456789", "pepe@gmail.com", null, Genero.HOMBRE);
        System.out.println(p.toString());
        
        // generar una persona aleatoria
        System.out.println("Cargando datos de personas...");
        ListaPersonas lp = new ListaPersonas();

        try {
            lp.loadData(
                "generador-nombres/datos/nombre_mujeres.txt",
                "generador-nombres/datos/nombre_hombres.txt",
                "generador-nombres/datos/apellidos.txt",
                "generador-nombres/datos/all_email.txt"
            );
            System.out.println("Datos cargados.");
            
            int totalGeneradas = lp.generaPersonas(10);
            System.out.println("Se generaron " + totalGeneradas + " personas:");
            
            for (Persona persona : lp.getPersonas()) {
                System.out.println(persona);
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Fin.");

        
        // comprobar que funcione bien el dado
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
    }
}