/*
 * $Id: DBApplicationDescriptionLibrary.java,v 1.3 2008/09/13 09:51:06 pah Exp $
 * 
 * Created on 13 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;

public class DBApplicationDescriptionLibrary extends
	BaseApplicationDescriptionLibrary {

    public DBApplicationDescriptionLibrary(
	    Configuration conf) {
	super( conf);
	// TODO Auto-generated constructor stub
    }

}


/*
 * $Log: DBApplicationDescriptionLibrary.java,v $
 * Revision 1.3  2008/09/13 09:51:06  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:46  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:32:22  pah
 * safety checkin - on vacation
 *
 */
