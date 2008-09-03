/*
 * $Id: HttpApplicationFactoryProduct.java,v 1.2 2008/09/03 14:19:03 pah Exp $
 * 
 * Created on 21 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http.registry;

import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;

import net.ivoa.resource.Resource;

public interface HttpApplicationFactoryProduct {
    
    public Resource getResource();
    public CeaHttpApplicationDefinition getAppDefinition();

}


/*
 * $Log: HttpApplicationFactoryProduct.java,v $
 * Revision 1.2  2008/09/03 14:19:03  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 */
