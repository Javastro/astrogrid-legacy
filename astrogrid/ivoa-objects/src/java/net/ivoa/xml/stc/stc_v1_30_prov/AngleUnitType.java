/*
 * $Id: AngleUnitType.java,v 1.2 2011/09/13 13:43:25 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for angleUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="angleUnitType">
 *   &lt;restriction base="{http://www.ivoa.net/xml/STC/stc-v1.30.xsd}unitType">
 *     &lt;enumeration value="deg"/>
 *     &lt;enumeration value="rad"/>
 *     &lt;enumeration value="h"/>
 *     &lt;enumeration value="arcmin"/>
 *     &lt;enumeration value="arcsec"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum AngleUnitType {

    @XmlEnumValue("deg")
    DEG("deg"),
    @XmlEnumValue("rad")
    RAD("rad"),
    @XmlEnumValue("h")
    H("h"),
    @XmlEnumValue("arcmin")
    ARCMIN("arcmin"),
    @XmlEnumValue("arcsec")
    ARCSEC("arcsec");
    private final String value;

    AngleUnitType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AngleUnitType fromValue(String v) {
        for (AngleUnitType c: AngleUnitType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
