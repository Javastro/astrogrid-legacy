/*
 * $Id: InternallyConfiguredApplicationDescription.java,v 1.3 2008/09/10 23:27:16 pah Exp $
 * 
 * Created on 13 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.base;

import net.ivoa.resource.cea.CeaApplication;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.AppMetadataAdapter;

/**
 * Adds helper methods for static creation of application definitions.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public abstract class InternallyConfiguredApplicationDescription extends
	AbstractApplicationDescription {

    protected static BaseParameterDefinition addParameter(CeaApplication app, String id,
	    ParameterTypes type, String name,  String description) {
	        BaseParameterDefinition param = new BaseParameterDefinition();
	        param.setId(id);
	        param.setType(type);
	        param.setName(name);
	        param.setDescription(description);
	        app.getApplicationDefinition().getParameters().getParameterDefinition().add(param);
	        return param;
	    }
    protected static InterfaceDefinition addInterface(CeaApplication app, String id) {
        InterfaceDefinition intf = new InterfaceDefinition();
        intf.setId(id);
        intf.setDescription(id);
        app.getApplicationDefinition().getInterfaces().getInterfaceDefinition().add(intf);
        return intf;
    }
    public InternallyConfiguredApplicationDescription(CeaApplication base,
	     Configuration configuration) {
	super(new AppMetadataAdapter(base), configuration);
	
	
    }
 

}


/*
 * $Log: InternallyConfiguredApplicationDescription.java,v $
 * Revision 1.3  2008/09/10 23:27:16  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.2  2008/09/03 14:18:42  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.6  2008/08/29 07:28:27  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.5  2008/08/02 13:33:56  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.4  2008/06/11 14:31:42  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.1.2.3  2008/04/08 14:45:10  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 * Revision 1.1.2.2  2008/03/26 17:15:38  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:52  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
