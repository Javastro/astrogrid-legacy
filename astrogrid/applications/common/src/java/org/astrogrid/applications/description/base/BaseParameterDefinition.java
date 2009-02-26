/*
 * $Id: BaseParameterDefinition.java,v 1.1 2009/02/26 12:25:47 pah Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.astrogrid.applications.description.Identify;
import org.astrogrid.applications.description.ParameterDescription;


/**
 * 
 *             The basic definition of what a parameter is - this is common
 *             to all of the CEA implementations.
 *          
 * 
 * <p>Java class for BaseParameterDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseParameterDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ivoa.net/xml/VODataService/v1.0}BaseParam">
 *       &lt;sequence>
 *         &lt;element name="UType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultValue" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="optionList" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}OptionList"/>
 *           &lt;element name="range" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}Range"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="array" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}arrayDEF" default="1" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" use="required" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterTypes" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseParameterDefinition", propOrder = {
    "uType",
    "mimeType",
    "defaultValue",
    "optionList",
    "range"
})
public class BaseParameterDefinition
    extends BaseParam implements ParameterDescription,Identify
{
    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory
	    .getLog(BaseParameterDefinition.class);

    @XmlElement(name = "UType")
    protected String uType;
    protected String mimeType;
    @XmlElement(type = String.class)
    protected List<String> defaultValue;
    protected OptionList optionList;
    protected Range range;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String array;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute(required = true)
    protected ParameterTypes type;

    /**
     * Gets the value of the uType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUType() {
        return uType;
    }

    /**
     * Sets the value of the uType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUType(String value) {
        this.uType = value;
    }

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the defaultValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defaultValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefaultValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDefaultValue() {
        if (defaultValue == null) {
            defaultValue = new ArrayList<String>();
        }
        return this.defaultValue;
    }

    public void setDefaultValue(List<String> defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public void setDefaultValue(String val)
    {
      if (defaultValue == null) {
	            defaultValue = new ArrayList<String>();
	}
	this.defaultValue.clear();
	this.defaultValue.add(val);
    }

    /**
     * Gets the value of the optionList property.
     * 
     * @return
     *     possible object is
     *     {@link OptionList }
     *     
     */
    public OptionList getOptionList() {
        return optionList;
    }

    /**
     * Sets the value of the optionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionList }
     *     
     */
    public void setOptionList(OptionList value) {
        this.optionList = value;
    }

    /**
     * Gets the value of the range property.
     * 
     * @return
     *     possible object is
     *     {@link Range }
     *     
     */
    public Range getRange() {
        return range;
    }

    /**
     * Sets the value of the range property.
     * 
     * @param value
     *     allowed object is
     *     {@link Range }
     *     
     */
    public void setRange(Range value) {
        this.range = value;
    }

    /**
     * Gets the value of the array property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArray() {
        if (array == null) {
            return "1";
        } else {
            return array;
        }
    }

    /**
     * Sets the value of the array property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArray(String value) {
        this.array = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterTypes }
     *     
     */
    public ParameterTypes getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterTypes }
     *     
     */
    public void setType(ParameterTypes value) {
        this.type = value;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Parameter: " + getId() + " /" + getType()                         
//FIXME            + (getDefaultValue() != "" ? "::Default Value: " + getDefaultValue(): "")
            + (getName() != "" ? " ::Display Name: " + getName():"")
            + (getDescription() != "" ? "::Display Description: " + getDescription():"")
            + (getUcd() != "" ? "::UCD: " + getUcd():"")
            + (getUnit() != "" ? "::Units: " + getUnit():"")
            + (getMimeType() != "" ? "::Mime: " + getMimeType():"");
                       
    }


}
