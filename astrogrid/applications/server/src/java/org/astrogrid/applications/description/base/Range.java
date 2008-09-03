/*
 * $Id: Range.java,v 1.2 2008/09/03 14:18:42 pah Exp $
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             used to specify that a parameter value should lie in a
 *             particular range.
 *          
 * 
 * <p>Java class for Range complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Range">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="min" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}RangeBound" minOccurs="0"/>
 *         &lt;element name="max" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}RangeBound" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Range", propOrder = {
    "min",
    "max"
})
public class Range {

    protected RangeBound min;
    protected RangeBound max;

    /**
     * Gets the value of the min property.
     * 
     * @return
     *     possible object is
     *     {@link RangeBound }
     *     
     */
    public RangeBound getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     * 
     * @param value
     *     allowed object is
     *     {@link RangeBound }
     *     
     */
    public void setMin(RangeBound value) {
        this.min = value;
    }

    /**
     * Gets the value of the max property.
     * 
     * @return
     *     possible object is
     *     {@link RangeBound }
     *     
     */
    public RangeBound getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     * @param value
     *     allowed object is
     *     {@link RangeBound }
     *     
     */
    public void setMax(RangeBound value) {
        this.max = value;
    }

}
