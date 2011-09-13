/*
 * $Id: AbstractResourceName.java,v 1.2 2011/09/13 13:43:30 pah Exp $
 * 
 * Created on 11 Jun 2011 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2011 Manchester University. All rights reserved.
 *
 * This software is published under the terms of the Academic 
 * Free License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.resource;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceName", propOrder = {
    "name"
})
public abstract class AbstractResourceName implements ResourceName {

    @XmlValue
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "ivo-id")
    protected String ivoId;

    /**
     * Gets the name of the name property.
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
     * Sets the value of the value property.
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
     * Gets the value of the ivoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIvoId() {
        return ivoId;
    }

    /**
     * Sets the value of the ivoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIvoId(String value) {
        this.ivoId = value;
    }

}


/*
 * $Log: AbstractResourceName.java,v $
 * Revision 1.2  2011/09/13 13:43:30  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 */
