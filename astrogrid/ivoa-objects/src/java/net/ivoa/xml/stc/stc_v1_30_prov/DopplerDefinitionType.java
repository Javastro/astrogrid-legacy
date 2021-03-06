/*
 * $Id: DopplerDefinitionType.java,v 1.2 2011/09/13 13:43:25 pah Exp $
 * Copyright 2011 Paul Harrison (paul.harrison@manchester.ac.uk)
 *
 * File originally generated by JAXB, but since hand updated
 */


package net.ivoa.xml.stc.stc_v1_30_prov;

import javax.xml.bind.annotation.XmlEnum;


/**
 * <p>Java class for dopplerDefinitionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="dopplerDefinitionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OPTICAL"/>
 *     &lt;enumeration value="RADIO"/>
 *     &lt;enumeration value="RELATIVISTIC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum DopplerDefinitionType {

    OPTICAL,
    RADIO,
    RELATIVISTIC;

    public String value() {
        return name();
    }

    public static DopplerDefinitionType fromValue(String v) {
        return valueOf(v);
    }

}
