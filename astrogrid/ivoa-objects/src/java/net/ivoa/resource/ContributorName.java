/*
 * $Id: ContributorName.java,v 1.2 2011/09/13 13:43:29 pah Exp $
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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CONTRIBUTOR")
public class ContributorName extends ResourceNameResource {

}


/*
 * $Log: ContributorName.java,v $
 * Revision 1.2  2011/09/13 13:43:29  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.2  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 * Revision 1.1.2.1  2011/06/09 22:18:52  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 */
