/*
 * $Id: AbstractIndependentResourceName.java,v 1.2 2011/09/13 13:43:29 pah Exp $
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

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;

@MappedSuperclass
public abstract class AbstractIndependentResourceName extends AbstractResourceName {
    @XmlTransient
    @Id
    @GeneratedValue
    protected int id;

}


/*
 * $Log: AbstractIndependentResourceName.java,v $
 * Revision 1.2  2011/09/13 13:43:29  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.1  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 */
