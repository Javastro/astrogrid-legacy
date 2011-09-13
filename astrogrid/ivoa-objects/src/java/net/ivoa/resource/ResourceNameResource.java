/*
 * $Id: ResourceNameResource.java,v 1.2 2011/09/13 13:43:30 pah Exp $
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

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Representation of a Resource Name that can occur multiple times and is owned by a resource. The XML representation is identical to {@link ResourceNameSingle} 
 * but it has different JPA annotations.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jun 2011
 * @version $Revision: 1.2 $ $date$
 */
@Entity
@DiscriminatorColumn(name="KIND")
@DiscriminatorValue("RES")
@Table(name = "ResourceName")
public class ResourceNameResource extends AbstractIndependentResourceName implements ResourceName {


}


/*
 * $Log: ResourceNameResource.java,v $
 * Revision 1.2  2011/09/13 13:43:30  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.2  2011/06/15 13:55:49  pah
 * writing to DB almost works - seems to be a bug in eclipselink @OneToMany inside @Embeddable does not work....
 *
 * Revision 1.1.2.1  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 */
