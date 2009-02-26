/*
 * $Id: ParameterRef.java,v 1.1 2009/02/26 12:25:47 pah Exp $
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.Identify;


/**
 * 
 *             reference to an application parameter. Used in the interface
 *             definitions.
 *          
 * 
 * <p>Java class for ParameterRef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParameterRef">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}cardinality"/>
 *       &lt;attGroup ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterReferenceAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterRef")
public class ParameterRef implements Identify {

    @XmlAttribute
    protected Integer maxOccurs;
    @XmlAttribute
    protected Integer minOccurs;
    @XmlAttribute
    protected Boolean hidden;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String ref;
    
    public ParameterRef(){}

    /**  
     * Create a simple parameter reference that is not repeated nor optional.
     * @param ref
     */
    public ParameterRef(String ref) {
	this.ref = ref;
	maxOccurs = 1;
	minOccurs = 1;
	hidden = false;
    }
    /**  
     * Create a simple parameter reference with the given cardinality.
     * @param ref
     */
    public ParameterRef(String ref, Cardinality car) {
	this.ref = ref;
	maxOccurs = car.getMinOccurs();
	minOccurs = car.getMaxOccurs();
	hidden = false;
    }

	

    /**
     * Gets the value of the maxOccurs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getMaxOccurs() {
        if (maxOccurs == null) {
            return  1;
        } else {
            return maxOccurs;
        }
    }

    /**
     * Sets the value of the maxOccurs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxOccurs(Integer value) {
        this.maxOccurs = value;
    }

    /**
     * Gets the value of the minOccurs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getMinOccurs() {
        if (minOccurs == null) {
            return  1;
        } else {
            return minOccurs;
        }
    }

    /**
     * Sets the value of the minOccurs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinOccurs(Integer value) {
        this.minOccurs = value;
    }

    /**
     * Gets the value of the hidden property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHidden() {
        if (hidden == null) {
            return false;
        } else {
            return hidden;
        }
    }

    /**
     * Sets the value of the hidden property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHidden(Boolean value) {
        this.hidden = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }
    /** 
     * actually returns the identifier of the parameter that this refers to.
     * @see org.astrogrid.applications.description.Identify#getId()
     */
    public String getId() {
        return ref;
    }

}
