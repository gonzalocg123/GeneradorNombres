package com.example;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Genero {
    @XmlEnumValue("masculino")
    MASCULINO, 
    @XmlEnumValue("femenino")
    FEMENINO, 
    @XmlEnumValue("otro")
    OTRO, 
    @XmlEnumValue("neutro")
    NEUTRO
}