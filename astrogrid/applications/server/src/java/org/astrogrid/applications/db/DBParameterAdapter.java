/*
 * $Id: DBParameterAdapter.java,v 1.2 2008/09/03 14:18:46 pah Exp $
 * 
 * Created on 17 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db;

import org.astrogrid.applications.parameter.ParameterAdapter;

/**
 * Functionality required of parameters for DB Applications.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface DBParameterAdapter extends ParameterAdapter {
    /**
     * Return the where clause involving this parameter.
     * @return
     */
    String whereClausePart();
}


/*
 * $Log: DBParameterAdapter.java,v $
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
