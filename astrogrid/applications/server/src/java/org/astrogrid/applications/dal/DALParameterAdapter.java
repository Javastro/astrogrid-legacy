/*
 * $Id: DALParameterAdapter.java,v 1.2 2011/09/02 21:55:50 pah Exp $
 * 
 * Created on 7 Apr 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.dal;

import org.astrogrid.applications.db.DBParameterAdapter;

/**
 * Functions that a DAL Parameter adapter needs.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public interface DALParameterAdapter extends DBParameterAdapter {
    
    
    public String selectClausePart();

}


/*
 * $Log: DALParameterAdapter.java,v $
 * Revision 1.2  2011/09/02 21:55:50  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:53:02  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 */
