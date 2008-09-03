/*
 * $Id: InternalCeaComponentFactory.java,v 1.2 2008/09/03 14:18:57 pah Exp $
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
 * This factory is used to reduce the amount of dependency propagation that is used within the components. Some common, effectively fixed internal
 * components can be accessed from here. It is still expected that the factory is configured with spring. When needed the components should be called with
 * <code>
 * InternalCeaComponentFactory.getInstance().get...
 * </code>
 * Note that the instance of the class should be obtained early by the clients that want to use it, as the "current" instance is changed every time that it is created - this is ok when working in a spring environment (for which it is designed) - however within a unit testing environment where a large number of unit tests are potentially run within the same VM this can cause problems...
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 26 Aug 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class InternalCeaComponentFactory implements InternalCEAComponents {

    private static InternalCEAComponents instance = null;
    private ProtocolLibrary protoLib;
    private IdGen idgen;
    private AppAuthorityIDResolver authIDresolver;
    
    public InternalCeaComponentFactory(ProtocolLibrary plib, IdGen idg, AppAuthorityIDResolver authID) {
	//do not worry if already instantiated
//	if(instance != null)
//	{
//	    throw new IllegalStateException("component factory has already been instantiated");
//	}
	instance = this;
	this.protoLib = plib;
	this.idgen = idg;
	this.authIDresolver = authID;
    }
    /**
     * Get an instance of the Component factory
     * @return
     */
    public static InternalCEAComponents getInstance(){
	if(instance != null){
	    return instance;
	}
	else
	{
	    throw new IllegalStateException("InternalCeaComponentFactory should already have been instantiated - make sure that an object is created before use");
	}
    }
    public ProtocolLibrary getProtocolLibrary() {
	return protoLib;
    }
    public IdGen getIdGenerator() {
	return idgen;
    }
    public AppAuthorityIDResolver getAuthIDResolver() {
	return authIDresolver;
    }

}


/*
 * $Log: InternalCeaComponentFactory.java,v $
 * Revision 1.2  2008/09/03 14:18:57  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
