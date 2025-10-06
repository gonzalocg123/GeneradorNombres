package com.example;
import java.io.Serializable;
import java.util.regex.Pattern;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 
 * nombre (lo vamos a generar del archivo)
apellidos (lo vamos a generar del archivo)
teléfono (regex + o no, mínimo 5 dígitos, máximo 12)
email (regex loquesea@loquesea.dos_letras_o_más)
empresa  (puede ser NULL)
genero (hombre, mujer, prefiero_no_decirlo)

953 123 456
+34 953 12 34 56
+1 800 213 1235
1 2214 000 9689
*/

@XmlRootElement(name="persona")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persona implements Serializable {

    /*
    * nombre (lo vamos a generar del archivo)
    * apellidos (lo vamos a generar del archivo)
    * teléfono (regex + o no, mínimo 5 dígitos, máximo 12 dígitos)
    * email (regex loquesea@loquesea.dos_letras_o_más)
    * empresa (puede ser null)
    * genero (hombre, mujer, prefiero no decirlo)
    */

    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String empresa;
    private Genero genero;

    @XmlTransient
    private final Pattern telefonoPattern = Pattern.compile("^[+]?[0-9]{5,12}$");
    
    @XmlTransient
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Constructor por defecto
    public Persona() {
    }

    // Constructor con todos los parámetros
    public Persona(
        String nombre, String apellidos, 
        String email, String telefono, Genero genero) {

        if (!emailPattern.matcher(email).matches()){
            throw new IllegalArgumentException(
                "Formato de email incorrecto: " + email
            );
        }

        if (!telefonoPattern.matcher(telefono).matches()){
            throw new IllegalArgumentException(
                "Formato de teléfono incorrecto: "+ telefono);
        }
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.genero = genero;
    }


    // Getters y Setters
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        if (!telefonoPattern.matcher(telefono).matches()) {
            throw new IllegalArgumentException("El teléfono no cumple el formato requerido");
        }
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("El email no cumple el formato requerido");
        }
        this.email = email;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Persona nombre(String nombre) {
        setNombre(nombre);
        return this;
    }

    public Persona apellidos(String apellidos) {
        setApellidos(apellidos);
        return this;
    }

    public Persona telefono(String telefono) {
        setTelefono(telefono);
        return this;
    }

    public Persona email(String email) {
        setEmail(email);
        return this;
    }

    public Persona empresa(String empresa) {
        setEmpresa(empresa);
        return this;
    }

    public Persona genero(Genero genero) {
        setGenero(genero);
        return this;
    }

    
    @Override
    public String toString() {
        return "{" +
            " nombre:'" + getNombre() + "'" +
            ", apellidos:'" + getApellidos() + "'" +
            ", email:'" + getEmail() + "'" +
            ", telefono:'" + getTelefono() + "'" +
            ", genero:'" + getGenero() + "'" +
            "}";
    }



    public Pattern getEmailPattern() {
        return emailPattern;
    }
    
}
