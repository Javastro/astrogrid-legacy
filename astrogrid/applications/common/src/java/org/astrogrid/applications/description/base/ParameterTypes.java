/*
 * $Id: ParameterTypes.java,v 1.2 2009/04/04 20:38:08 pah Exp $
 * 
 * Created on 10 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 


package org.astrogrid.applications.description.base;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for ParameterTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ParameterTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="real"/>
 *     &lt;enumeration value="complex"/>
 *     &lt;enumeration value="text"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="anyURI"/>
 *     &lt;enumeration value="VOTable"/>
 *     &lt;enumeration value="angle"/>
 *     &lt;enumeration value="MJD"/>
 *     &lt;enumeration value="DateTime"/>
 *     &lt;enumeration value="ADQL"/>
 *     &lt;enumeration value="STC-S"/>
 *     &lt;enumeration value="binary"/>
 *     &lt;enumeration value="FITS"/>
 *     &lt;enumeration value="XML"/>
 *     &lt;enumeration value="Table"/>
 *     &lt;enumeration value="Image"/>
 *     &lt;enumeration value="Spectrum"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 */
@XmlEnum
public enum ParameterTypes {

    @XmlEnumValue("integer")
    INTEGER("integer"),
    @XmlEnumValue("real")
    REAL("real"),
    @XmlEnumValue("complex")
    COMPLEX("complex"),
    @XmlEnumValue("text")
    TEXT("text"),
    @XmlEnumValue("boolean")
    BOOLEAN("boolean"),
    @XmlEnumValue("anyURI")
    ANY_URI("anyURI"),
    @XmlEnumValue("VOTable")
    VO_TABLE("VOTable"),
    @XmlEnumValue("angle")
    ANGLE("angle"),
    MJD("MJD"),
    @XmlEnumValue("DateTime")
    DATE_TIME("DateTime"),
    ADQL("ADQL"),
    @XmlEnumValue("STC-S")
    STC_S("STC-S"),
    @XmlEnumValue("binary")
    BINARY("binary"),
    FITS("FITS"),
    XML("XML"),
    @XmlEnumValue("Table")
    TABLE("Table"),
    @XmlEnumValue("Image")
    IMAGE("Image"),
    @XmlEnumValue("Spectrum")
    SPECTRUM("Spectrum"),
    UNKNOWN("unknown"); //Special default value that does not get into the XML hopefully.
    
    private final String value;

    ParameterTypes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParameterTypes fromValue(String v) {
        for (ParameterTypes c: ParameterTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

    @Override
    public String toString() {
	return value;
    }

}
