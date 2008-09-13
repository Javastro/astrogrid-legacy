/*
 * $Id: SimpleApplicationDescriptionLibrary.java,v 1.3 2008/09/13 09:51:05 pah Exp $
 * 
 * Created on 16 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.contracts.Configuration;

/**
 * Simple description library that only contains the applications that are added to it with the {@link #setApplications(List)} method. Will always contain a {@link BuiltInApplicationDescription}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class SimpleApplicationDescriptionLibrary extends
	BaseApplicationDescriptionLibrary {

    public SimpleApplicationDescriptionLibrary(
	     Configuration conf) {
	super( conf);
	   try {
	       // Every application-description library carries one built-in application.
	       this.addApplicationDescription(new BuiltInApplicationDescription( conf));
	     } catch (CeaException ex) {
	       throw new RuntimeException("Can't add the built-in application!", ex);
	     }
	
    }
    
    
    /**
     * Sets the applications in the library.
     * @param apps
     */
    public void setApplications(List<ApplicationDescription> apps)
    {
	for (ApplicationDescription applicationDescription : apps) {
	    this.addApplicationDescription(applicationDescription);
	}
    }

}


/*
 * $Log: SimpleApplicationDescriptionLibrary.java,v $
 * Revision 1.3  2008/09/13 09:51:05  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/08/29 07:28:26  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/06/16 21:58:58  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 */
