/*
 * $Id: OutputParameterReferenceSpecification.java,v 1.3 2008/09/13 09:51:04 pah Exp $
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
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;


/**
 * 
 *             restricted to prefs at the moment - is there a case for
 *             pgroups...
 *          
 * 
 * <p>Java class for OutputParameterReferenceSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OutputParameterReferenceSpecification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pref" type="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterRef" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutputParameterReferenceSpecification", propOrder = {
    "pref"
})
public class OutputParameterReferenceSpecification {

    @XmlElement(type = ParameterRef.class)
    protected List<ParameterRef> pref;

    /**
     * Gets the value of the pref property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pref property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPref().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterRef }
     * 
     * 
     */
    public List<ParameterRef> getPref() {
        if (pref == null) {
            pref = new ArrayList<ParameterRef>();
        }
        return this.pref;
    }

    /**
     * @param parameterName
     * @future this is very inefficient...need to decorate the ArrayList with a map...
     * @return
     */
    public boolean contains(String parameterName) {
	for (ParameterRef ref : pref) {
	    if (parameterName.equals(ref.getId())) {
		return true;
		
	    }
	}
	return false;
   }
    public ParameterRef get(String parameterName) throws ParameterNotInInterfaceException {
	for (ParameterRef ref : pref) {
	    if (parameterName.equals(ref.getId())) {
		return ref;
		
	    }
	}
	throw new ParameterNotInInterfaceException(parameterName + " not found");
	
    }
}
