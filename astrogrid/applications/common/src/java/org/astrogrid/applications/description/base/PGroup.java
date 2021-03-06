/*
 * $Id: PGroup.java,v 1.1 2009/02/26 12:25:47 pah Exp $
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             A grouping of several parameters - this can be done for
 *             several reasons 1. provides a group that must be repeated 2.
 *             can be used as a visual hint to any applications that use
 *             the group.
 *          
 * 
 * <p>Java class for PGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterReferenceGroup" maxOccurs="unbounded" minOccurs="2"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}cardinality"/>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PGroup", propOrder = {
    "parameterReferenceGroup"
})
public class PGroup {

    @XmlElements({
        @XmlElement(name = "cgroupHead", type = ConditionalPgroup.class),
        @XmlElement(name = "rgroup", type = RadioPGroup.class),
        @XmlElement(name = "pgroup", type = PGroup.class),
        @XmlElement(name = "pref", type = ParameterRef.class)
    })
    protected List<Object> parameterReferenceGroup;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected Integer maxOccurs;
    @XmlAttribute
    protected Integer minOccurs;

    /**
     * 
     *                   These are the elements that occur anywhere that there
     *                   is a parameter reference possible in an interface. -
     *                   should this be a minimum of 2?
     *                Gets the value of the parameterReferenceGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterReferenceGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterReferenceGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConditionalPgroup }
     * {@link RadioPGroup }
     * {@link PGroup }
     * {@link ParameterRef }
     * 
     * 
     */
    public List<Object> getParameterReferenceGroup() {
        if (parameterReferenceGroup == null) {
            parameterReferenceGroup = new ArrayList<Object>();
        }
        return this.parameterReferenceGroup;
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

}
