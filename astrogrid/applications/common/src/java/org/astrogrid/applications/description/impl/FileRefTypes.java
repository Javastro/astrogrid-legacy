//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.07 at 11:53:07 AM BST 
//


package org.astrogrid.applications.description.impl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for FileRefTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FileRefTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="no"/>
 *     &lt;enumeration value="file"/>
 *     &lt;enumeration value="directory"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum FileRefTypes {

    @XmlEnumValue("no")
    NO("no"),
    @XmlEnumValue("file")
    FILE("file"),
    @XmlEnumValue("directory")
    DIRECTORY("directory");
    private final String value;

    FileRefTypes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FileRefTypes fromValue(String v) {
        for (FileRefTypes c: FileRefTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
