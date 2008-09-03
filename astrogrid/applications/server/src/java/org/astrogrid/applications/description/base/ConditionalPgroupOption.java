/*
 * $Id: ConditionalPgroupOption.java,v 1.2 2008/09/03 14:18:41 pah Exp $
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
 *             this is the grouping of parameters that are required for a
 *             particular value of a head parameter
 *          
 * 
 * <p>Java class for ConditionalPgroupOption complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionalPgroupOption">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;group ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterReferenceGroup" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConditionalPgroupOption", propOrder = {
    "value",
    "parameterReferenceGroup"
})
public class ConditionalPgroupOption {

    @XmlElement(required = true)
    protected String value;
    @XmlElements({
        @XmlElement(name = "rgroup", type = RadioPGroup.class),
        @XmlElement(name = "pref", type = ParameterRef.class),
        @XmlElement(name = "pgroup", type = PGroup.class),
        @XmlElement(name = "cgroupHead", type = ConditionalPgroup.class)
    })
    protected List<Object> parameterReferenceGroup;
    @XmlAttribute
    protected String name;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the parameterReferenceGroup property.
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
     * {@link RadioPGroup }
     * {@link ParameterRef }
     * {@link PGroup }
     * {@link ConditionalPgroup }
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

}
