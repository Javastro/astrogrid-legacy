/*
 * $Id: InternalCEAComponents.java,v 1.2 2008/09/03 14:18:57 pah Exp $
 * 
 * Created on 26 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import org.astrogrid.applications.manager.AppAuthorityIDResolver;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/**
 * Components of CEA subsystem that are almost "fixed". Hence these components need not appear at the top level injection.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 26 Aug 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface InternalCEAComponents {
    
    ProtocolLibrary getProtocolLibrary();
    IdGen getIdGenerator();
    AppAuthorityIDResolver getAuthIDResolver();

}


/*
 * $Log: InternalCEAComponents.java,v $
 * Revision 1.2  2008/09/03 14:18:57  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
