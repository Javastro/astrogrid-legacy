/*
 * $Id: BinaryEncodings.java,v 1.1 2008/10/06 12:12:36 pah Exp $
 * 
 * Created on 10 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 


package org.astrogrid.applications.description.execution;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for BinaryEncodings.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BinaryEncodings">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="base64"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 */
@XmlEnum
public enum BinaryEncodings {

    @XmlEnumValue("base64")
    BASE_64("base64");
    private final String value;

    BinaryEncodings(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BinaryEncodings fromValue(String v) {
        for (BinaryEncodings c: BinaryEncodings.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}