/*
 * $Id: ApplicationKind.java,v 1.2 2008/09/03 14:18:43 pah Exp $
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
 * <p>Java class for ApplicationKind.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ApplicationKind">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="processing"/>
 *     &lt;enumeration value="http"/>
 *     &lt;enumeration value="dataproducing"/>
 *     &lt;enumeration value="grid"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 */
@XmlEnum
public enum ApplicationKind {

    @XmlEnumValue("processing")
    PROCESSING("processing"),
    @XmlEnumValue("http")
    HTTP("http"),
    @XmlEnumValue("dataproducing")
    DATAPRODUCING("dataproducing"),
    @XmlEnumValue("grid")
    GRID("grid");
    private final String value;

    ApplicationKind(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ApplicationKind fromValue(String v) {
        for (ApplicationKind c: ApplicationKind.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
