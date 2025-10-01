package com.example;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Enum que representa los géneros posibles para una persona.
 */
@XmlEnum
public enum Genero {
    @xmlEnumValue("hombre")
    HOMBRE,
    @xmlEnumValue("mujer")
    MUJER,
    @xmlEnumValue("prefiero no decirlo")
    PREFIERO_NO_DECIRLO
}
