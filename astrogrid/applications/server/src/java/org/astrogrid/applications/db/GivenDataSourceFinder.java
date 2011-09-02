/*
 * $Id: GivenDataSourceFinder.java,v 1.2 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 9 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db;

import javax.sql.DataSource;

public class GivenDataSourceFinder implements DataSourceFinder {

    private DataSource dataSource;
    public GivenDataSourceFinder(DataSource ds) {
        this.dataSource = ds;
    }
    public DataSource locateDataSource() {
        return dataSource;
    }

}


/*
 * $Log: GivenDataSourceFinder.java,v $
 * Revision 1.2  2011/09/02 21:55:52  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:52:17  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 */
