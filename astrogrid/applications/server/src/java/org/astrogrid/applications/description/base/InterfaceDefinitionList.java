/*
 * $Id: InterfaceDefinitionList.java,v 1.3 2008/09/13 09:51:04 pah Exp $
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
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;


/**
 * 
 *               A list of interface definitions
 *            
 * 
 * <p>Java class for InterfaceDefinitionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterfaceDefinitionList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interfaceDefinition" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}InterfaceDefinition" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterfaceDefinitionList", propOrder = {
    "interfaceDefinition"
})
public class InterfaceDefinitionList {

    @XmlElement(required = true, type = InterfaceDefinition.class)
    protected MapDecoratedList<InterfaceDefinition> interfaceDefinition;

    /**
     * Gets the value of the interfaceDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interfaceDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterfaceDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InterfaceDefinition }
     * 
     * 
     */
    public List<InterfaceDefinition> getInterfaceDefinition() {
        if (interfaceDefinition == null) {
            interfaceDefinition = new MapDecoratedList<InterfaceDefinition>();
        }
        return this.interfaceDefinition;
    }
    
    public InterfaceDefinition getInterfaceById(String id) throws InterfaceDescriptionNotFoundException{
	if(interfaceDefinition.containsKey(id)){
	    return interfaceDefinition.get(id);
	}
	else throw new InterfaceDescriptionNotFoundException(id);
    }

}
