/*
 * $Id: InputParameterReferenceSpecification.java,v 1.1 2009/02/26 12:25:47 pah Exp $
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
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import org.astrogrid.applications.description.Identify;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;


/**
 * 
 *               The allowed structures for input parameters
 *            
 * 
 * <p>Java class for InputParameterReferenceSpecification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * @TODO the implementation really assumes that everything will be a pref at the moment....
 * <pre>
 * &lt;complexType name="InputParameterReferenceSpecification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;group ref="{http://www.ivoa.net/xml/CEA/base/v1.0rc3}ParameterReferenceGroup"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Mar 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputParameterReferenceSpecification", propOrder = {
    "prefOrPgroupOrCgroupHead"
})
public class InputParameterReferenceSpecification {

    @XmlElements({
        @XmlElement(name = "rgroup", type = RadioPGroup.class),
        @XmlElement(name = "pgroup", type = PGroup.class),
        @XmlElement(name = "cgroupHead", type = ConditionalPgroup.class),
        @XmlElement(name = "pref", type = ParameterRef.class)
    })
    protected final List<Object> prefOrPgroupOrCgroupHead;

    /**
     * Gets the value of the prefOrPgroupOrCgroupHead property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prefOrPgroupOrCgroupHead property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrefOrPgroupOrCgroupHead().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RadioPGroup }
     * {@link PGroup }
     * {@link ConditionalPgroup }
     * {@link ParameterRef }
     * 
     * 
     */
    public InputParameterReferenceSpecification()
    {
	prefOrPgroupOrCgroupHead = new ArrayList<Object>();
    }
    public List<Object> getPrefOrPgroupOrCgroupHead() {
        return this.prefOrPgroupOrCgroupHead;
    }
    
    public void addPref(ParameterRef pref)
    {
	prefOrPgroupOrCgroupHead.add(pref);
    }
    /**
     * @param parameterName
     * @future this is very inefficient...
     * @return
     */
    public boolean contains(String parameterName) {
	    for (Iterator iterator = prefOrPgroupOrCgroupHead.iterator(); iterator.hasNext();) {
		Identify type = (Identify) iterator.next();
		if(parameterName.equals(type.getId())){
		    return true;
		}
		
	    }
	    return false;
    }
    public ParameterRef get(String parameterName) throws ParameterNotInInterfaceException {
	    for (Iterator iterator = prefOrPgroupOrCgroupHead.iterator(); iterator.hasNext();) {
		Identify type = (Identify) iterator.next();
		if(parameterName.equals(type.getId())){
		    return (ParameterRef) type;
		}
		
	    }
	    throw new ParameterNotInInterfaceException(parameterName + "not found");
}

}
