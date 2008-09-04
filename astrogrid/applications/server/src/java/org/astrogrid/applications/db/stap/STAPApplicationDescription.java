/*
 * $Id: STAPApplicationDescription.java,v 1.3 2008/09/04 19:10:53 pah Exp $
 * 
 * Created on 16 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db.stap;

import net.ivoa.resource.cea.CeaApplication;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.db.DBApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ServiceMetadataAdapter;
import org.astrogrid.applications.description.AppMetadataAdapter;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.security.SecurityGuard;

public class STAPApplicationDescription extends DBApplicationDescription {

   
    private static CeaApplication base;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
	    .getLog(STAPApplicationDescription.class);
    static {
	try {
	    //read the definition from an external file
	    base = CEAJAXBUtils.unmarshall(STAPApplicationDescription.class.getResourceAsStream("StapConfig.xml"), CeaApplication.class);
	} catch (Exception e) {
	    logger.error("Problem reading STAP application definition",e);
	}
    }

    public STAPApplicationDescription(
	     Configuration conf, javax.sql.DataSource ds) {
	super(new AppMetadataAdapter(base),  conf, ds);
    }

    @Override
    public Application initializeApplication(String callerAssignedID,
	    SecurityGuard secGuard, Tool tool) throws Exception {
	ApplicationInterface appInterface = this.getInterface(tool.getInterface());
	    if (appInterface == null) { // go for default then..
	        appInterface = this.getInterfaces()[0];
	    }
	
	return new STAPApplication(tool,appInterface, ds, new ApplicationEnvironment(callerAssignedID, secGuard, getInternalComponentFactory().getIdGenerator(), conf),  lib);
	
    }
    
    
    

}


/*
 * $Log: STAPApplicationDescription.java,v $
 * Revision 1.3  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 * Revision 1.2  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 */
