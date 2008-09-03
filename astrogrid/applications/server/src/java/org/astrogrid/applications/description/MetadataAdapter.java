/*
 * $Id: MetadataAdapter.java,v 1.2 2008/09/03 14:18:43 pah Exp $
 * 
 * Created on 19 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.description.base.ApplicationBase;

import net.ivoa.resource.Resource;
import net.ivoa.resource.Service;
import net.ivoa.resource.cea.CeaApplication;
import net.ivoa.resource.dataservice.DataService;

/**
 * Produces uniform access to the metadata for an application. Applications can be pure {@link CeaApplication}s which need to be registered separately to the server
 * or they can be some sort of {@link DataService}
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 19 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface MetadataAdapter {
    
    /*
     * @TODO need to add a get CEAApplication again to get just the application if appropriate...
     */
   
    public boolean needApplication();
    
    /**
     * is the underlying application a {@link Service}? Usual
     * @return
     */
    public boolean isService();       
    
    /**
     * The full {@link Resource} - implementation version - not suitable for publishing to the registry.
     * @TODO probably want to make this a list of resources - because service based CEAApps could be more than one resource.
     * @return
     */
    public Resource getResource();
    
    /**
     * The {@link ApplicationBase} or a subclass specific to the type of application definition.
     * @return
     */
    public ApplicationBase getApplicationBase();

}


/*
 * $Log: MetadataAdapter.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/08/29 07:28:25  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:33:56  pah
 * safety checkin - on vacation
 *
 */
