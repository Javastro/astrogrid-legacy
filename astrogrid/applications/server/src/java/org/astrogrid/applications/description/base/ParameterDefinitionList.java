/*
 * $Id: ParameterDefinitionList.java,v 1.3 2008/09/13 09:51:04 pah Exp $
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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.MapDecoratedList;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;


/**
 * 
 *               A list of parameter definitions
 *            
 * 
 * <p>Java class for ParameterDefinitionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParameterDefinitionList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameterDefinition" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}BaseParameterDefinition" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterDefinitionList", propOrder = {
    "parameterDefinition"
})
public class ParameterDefinitionList {

    @XmlElement(type = BaseParameterDefinition.class)
    protected MapDecoratedList<BaseParameterDefinition> parameterDefinition;

    /**
     * Gets the value of the parameterDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BaseParameterDefinition }
     * 
     * 
     */
    public List<BaseParameterDefinition> getParameterDefinition() {
        if (parameterDefinition == null) {
            parameterDefinition = new MapDecoratedList<BaseParameterDefinition>();
        }
        return this.parameterDefinition;
    }
    
    
    public BaseParameterDefinition getParameterDefinitionbyId(String id) throws ParameterDescriptionNotFoundException
    {
	if(parameterDefinition.containsKey(id))
	{
	    return parameterDefinition.get(id);
	    
	}else
	{
	    throw new ParameterDescriptionNotFoundException(id);
	}
    }
}
