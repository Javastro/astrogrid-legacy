/*
 * $Id: ApplicationBase.java,v 1.1 2009/02/26 12:25:47 pah Exp $
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.jaxb.DescriptionValidator;


/**
 * 
 *             The base application description
 *          
 * 
 * <p>Java class for ApplicationBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applicationType" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ApplicationKind" maxOccurs="unbounded"/>
 *         &lt;element name="parameters" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterDefinitionList"/>
 *         &lt;element name="interfaces" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}InterfaceDefinitionList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationBase", propOrder = {
    "applicationType",
    "parameters",
    "interfaces"
})
public class ApplicationBase  {

    @XmlElement(required = true, type = ApplicationKind.class)
    protected List<ApplicationKind> applicationType;
    @XmlElement(required = true)
    protected ParameterDefinitionList parameters;
    @XmlElement(required = true)
    protected InterfaceDefinitionList interfaces;

    public ApplicationBase() {
	parameters = new ParameterDefinitionList();
	interfaces = new InterfaceDefinitionList();
    }
    
    /**
     * Gets the value of the applicationType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicationKind }
     * FIXME are multiple application kinds appropriate?
     * 
     */
    public List<ApplicationKind> getApplicationType() {
        if (applicationType == null) {
            applicationType = new ArrayList<ApplicationKind>();
        }
        return this.applicationType;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterDefinitionList }
     *     
     */
    public ParameterDefinitionList getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterDefinitionList }
     *     
     */
    public void setParameters(ParameterDefinitionList value) {
        this.parameters = value;
    }

    /**
     * Gets the value of the interfaces property.
     * 
     * @return
     *     possible object is
     *     {@link InterfaceDefinitionList }
     *     
     */
    public InterfaceDefinitionList getInterfaces() {
        return interfaces;
    }

    /**
     * Sets the value of the interfaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterfaceDefinitionList }
     *     
     */
    public void setInterfaces(InterfaceDefinitionList value) {
        this.interfaces = value;
    }

    public boolean isValid()
    {
	return DescriptionValidator.validate(this);
       
    }
}
