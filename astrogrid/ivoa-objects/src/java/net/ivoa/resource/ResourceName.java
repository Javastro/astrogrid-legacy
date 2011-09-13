/*
 * $Id: ResourceName.java,v 1.2 2011/09/13 13:43:29 pah Exp $
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

public interface ResourceName {
    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName(); 

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) ;

    /**
     * Gets the value of the ivoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIvoId() ;

    /**
     * Sets the value of the ivoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIvoId(String value) ;

}


/*
 * $Log: ResourceName.java,v $
 * Revision 1.2  2011/09/13 13:43:29  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.3  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 */
