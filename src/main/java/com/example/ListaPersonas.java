package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    private final Pattern domainPattern = Pattern.compile("^[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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
        
        this.personas = new ArrayList<>();

        for (int i = 0; i < numeroPersonas; i++) {
            personas.add(generaPersona());
        }

    return personas.size(); // ✅ Devolver cuántas personas se generaron
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

        // crear email seguro
        String nombreSinAcentos = quitarAcentos(p.getNombre().toLowerCase());
        String apellidosSinAcentos = quitarAcentos(p.getApellidos().toLowerCase());
        String[] partesApellidos = apellidosSinAcentos.split("\\s+");

        String local = "" + nombreSinAcentos.charAt(0);
        local += partesApellidos[0].substring(0, Math.min(3, partesApellidos[0].length()));
        if (partesApellidos.length > 1) {
            local += partesApellidos[1].substring(0, Math.min(3, partesApellidos[1].length()));
        } else {
            local += "xxx";
        }
        // permitir sólo caracteres válidos en local-part
        local = local.replaceAll("[^a-z0-9+_.-]", "");

        // elegir dominio (limpiado en loadData)
        String dominio = dominioEmails.get(dado(dominioEmails.size())).toLowerCase();
        dominio = dominio.replaceAll("^\\.+", ""); // quitar puntos al inicio si existen por error

        String email = local + "@" + dominio;

        // intentar asignar; si falla, hacer fallback
        try {
            p.setEmail(email);
        } catch (IllegalArgumentException e) {
            // intentar reconstruir dominio con los últimos dos segmentos (co.uk -> co.uk)
            String[] segs = dominio.split("\\.");
            if (segs.length >= 2) {
                String fallbackDomain = segs[segs.length-2] + "." + segs[segs.length-1];
                String email2 = local + "@" + fallbackDomain;
                try {
                    p.setEmail(email2);
                } catch (IllegalArgumentException e2) {
                    // versión segura por defecto
                    p.setEmail(local + "@example.com");
                }
            } else {
                p.setEmail(local + "@example.com");
            }
        }


        return p;
    }

    private String quitarAcentos(String texto) {
        if (texto == null) return "";
        String norm = Normalizer.normalize(texto, Normalizer.Form.NFD);
        // eliminar marcas diacríticas
        String sin = norm.replaceAll("\\p{M}", "");
        // asegurarse de convertir ñ a n si fuese necesario
        return sin.replace('ñ', 'n').replace('Ñ', 'N');
    }


    /** 
     * Devuelve la lista de personas generadas.
     * @return Lista de personas. 
     */    
    public List<Persona> getPersonas() {
    return personas;
}

}
