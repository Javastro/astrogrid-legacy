//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 03:44:35 PM BST 
//


package net.ivoa.resource.sia;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for ImageServiceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ImageServiceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Cutout"/>
 *     &lt;enumeration value="Mosaic"/>
 *     &lt;enumeration value="Atlas"/>
 *     &lt;enumeration value="Pointed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum ImageServiceType {

    @XmlEnumValue("Cutout")
    CUTOUT("Cutout"),
    @XmlEnumValue("Mosaic")
    MOSAIC("Mosaic"),
    @XmlEnumValue("Atlas")
    ATLAS("Atlas"),
    @XmlEnumValue("Pointed")
    POINTED("Pointed");
    private final String value;

    ImageServiceType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ImageServiceType fromValue(String v) {
        for (ImageServiceType c: ImageServiceType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}