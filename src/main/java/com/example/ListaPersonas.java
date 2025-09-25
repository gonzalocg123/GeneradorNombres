package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la carga de datos y la generación de personas.
 * Contiene métodos para leer archivos de texto con nombres, apellidos y dominios de email,
 * así como para generar personas de forma aleatoria.
 */
public class ListaPersonas {

    // atributo con la lista "actual" de personas
    // esta es la información que me interesa
    /** Lista con las personas generadas */
    private List<Persona> personas;
    
    // atributos para el generador
    // es algo temporal, no es la información que me interesa
    private List<String> nombresMasculinos;
    private List<String> nombresFemeninos;
    private List<String> apellidos;
    private List<String> dominioEmails;

    /**
     * Carga en una lista las líneas de un archivo de texto.
     *
     * @param filename Ruta del archivo a leer.
     * @return Lista con las líneas leídas.
     * @throws IllegalArgumentException Si el archivo no existe o no es legible.
     */
    private List<String> cargarLineasDesdeArchivo(String filename) {
        List<String> lista = new ArrayList<>();
        Path pathArchivo = Paths.get(filename);

        if (!Files.exists(pathArchivo)) {
            throw new IllegalArgumentException("El archivo no existe: " + filename);
        }
        
        if (!Files.isReadable(pathArchivo)) {
            throw new IllegalArgumentException("El archivo no es legible: " + filename);
        }

        try {
            lista = Files.readAllLines(pathArchivo);
        } catch (IOException e) {
        }

        return lista;
    }

    /**
     * Carga los datos necesarios para generar personas desde los archivos de texto.
     *
     * @param nombreArchivoMujeres Archivo con nombres femeninos.
     * @param nombreArchivoHombres Archivo con nombres masculinos.
     * @param nombreArchivoApellidos Archivo con apellidos.
     * @param nombreArchivoEmails Archivo con dominios de email.
     */
    public void loadData(String nombreArchivoMujeres, String nombreArchivoHombres, String nombreArchivoApellidos, String nombreArchivoEmails) {
        // cargar los nombres femeninos
        this.nombresFemeninos = cargarLineasDesdeArchivo(nombreArchivoMujeres);
        // cargar los nombres masculinos
        this.nombresMasculinos = cargarLineasDesdeArchivo(nombreArchivoHombres);
        // cargar los apellidos
        this.apellidos = cargarLineasDesdeArchivo(nombreArchivoApellidos);
        // cargar los emails
        this.dominioEmails = cargarLineasDesdeArchivo(nombreArchivoEmails);
    }

    /**
     * Genera un número aleatorio entre 0 (inclusive) y el número de caras indicado (exclusivo).
     *
     * @param caras Número de caras del dado.
     * @return Número aleatorio.
     */
    public int dado(int caras){
        return (int) Math.floor(Math.random() * caras);
    }

    /**
     * Genera una cantidad de personas aleatorias y las guarda en la lista interna.
     *
     * @param numeroPersonas Número de personas a generar (1 - 100000).
     * @return Número de personas generadas.
     * @throws IllegalStateException Si los datos no han sido cargados previamente.
     * @throws IllegalArgumentException Si el número de personas es inválido.
     */
    public int generaPersonas(int numeroPersonas) {
        if (this.nombresFemeninos == null || this.nombresMasculinos == null || this.apellidos == null || this.dominioEmails == null) {
            throw new IllegalStateException("No se han cargado los datos necesarios para generar personas");
        }
        if (numeroPersonas <= 0 || numeroPersonas > 100000) {
            throw new IllegalArgumentException("El número de personas a generar debe ser mayor que cero y menor o igual que 100000");
        }
        // inicializar la lista de personas
        this.personas = new ArrayList<>();

        for (int i = 0; i < numeroPersonas; i++) {
            personas.add(generaPersona());
        }


        return 0;
    }

    /**
     * Genera una persona aleatoria asignando nombre, apellidos, teléfono y email.
     *
     * @return Persona generada.
     */
    private Persona generaPersona() {
        Persona p = new Persona();
        // asignar apellidos
        p.setApellidos(apellidos.get(dado(apellidos.size())) + " " + apellidos.get(dado(apellidos.size())));
        
        // asignar género
        Genero genero = Genero.values()[dado(Genero.values().length)];
        p.setGenero(genero);

        // asignar nombre según género
        switch (genero) {
            case HOMBRE -> p.setNombre(nombresMasculinos.get(dado(nombresMasculinos.size())));
            case MUJER -> p.setNombre(nombresFemeninos.get(dado(nombresFemeninos.size())));
            default -> {
                // prefiero no decirlo
                if (dado(2) == 0) {
                    p.setNombre(nombresMasculinos.get(dado(nombresMasculinos.size())));
                } else {
                    p.setNombre(nombresFemeninos.get(dado(nombresFemeninos.size())));
                }
            }
        }

        // generar teléfono
        String telefono = "+34" + (int)(Math.random() * 900000000 + 100000000);
        p.setTelefono(telefono);

        /**
         * generar email con primera letra del nombre y tres primeras letras de los dos apellidos. 
         * Todas las letras en minúscula.
         * Se quitan los acentos y la ñ se cambia por n.
         * El dominio del email se elige aleatoriamente de la lista de dominios cargados
         * Ejemplo: Gonzalo Chica Godino -> gchigod@dominio
         */

        String nombreSinAcentos = p.getNombre().toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ñ", "n").replace("ç", "c");
        String apellidosSinAcentos = p.getApellidos().toLowerCase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("ñ", "n").replace("ç", "c");
        String[] partesApellidos = apellidosSinAcentos.split(" ");
        String email = nombreSinAcentos.charAt(0) + "";
        email += partesApellidos[0].substring(0, Math.min(3, partesApellidos[0].length()));
        if (partesApellidos.length > 1) {
            email += partesApellidos[1].substring(0, Math.min(3, partesApellidos[1].length()));
        } else {
            email += "xxx";
        }
        email += "@" + dominioEmails.get(dado(dominioEmails.size()));
        p.setEmail(email); 


        return p;
    }

}
