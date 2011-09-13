/*
 * $Id: ResourceValidation.java,v 1.2 2011/09/13 13:43:29 pah Exp $
 * 
 * Created on 9 Jun 2011 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2011 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.resource;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ResourceValidation")
public class ResourceValidation extends Validation {

    
}


/*
 * $Log: ResourceValidation.java,v $
 * Revision 1.2  2011/09/13 13:43:29  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.2  2011/06/15 13:55:49  pah
 * writing to DB almost works - seems to be a bug in eclipselink @OneToMany inside @Embeddable does not work....
 *
 * Revision 1.1.2.1  2011/06/09 22:18:52  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 */
