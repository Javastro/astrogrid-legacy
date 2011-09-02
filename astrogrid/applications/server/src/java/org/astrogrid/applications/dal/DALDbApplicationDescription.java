/*
 * $Id: DALDbApplicationDescription.java,v 1.2 2011/09/02 21:55:50 pah Exp $
 * 
 * Created on 10 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.dal;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.db.DBApplicationDescription;
import org.astrogrid.applications.db.DataSourceFinder;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.security.SecurityGuard;

public class DALDbApplicationDescription extends DBApplicationDescription {

    public DALDbApplicationDescription(MetadataAdapter ma, Configuration conf,
            DataSourceFinder dsf) {
        super(ma, conf, dsf);
       
    }

    public Application initializeApplication(String callerAssignedID,
            SecurityGuard secGuard, Tool tool) throws Exception {
        ApplicationInterface appInterface = this.getInterface(tool.getInterface());
        if (appInterface == null) { // go for default then..
            appInterface = this.getInterfaces()[0];
        }
    
    return new DALDbApplication(tool,appInterface, ds, new ApplicationEnvironment(callerAssignedID, 
            secGuard, getInternalComponentFactory().getIdGenerator(), conf),  getInternalComponentFactory().getProtocolLibrary());

    }

}


/*
 * $Log: DALDbApplicationDescription.java,v $
 * Revision 1.2  2011/09/02 21:55:50  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:53:02  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 */
