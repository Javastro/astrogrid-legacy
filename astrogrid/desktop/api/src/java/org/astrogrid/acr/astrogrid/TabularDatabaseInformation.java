/*$Id: TabularDatabaseInformation.java,v 1.3 2006/02/02 14:19:48 nw Exp $
 * Created on 12-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.net.URL;
import java.util.Map;

/** description of a tabular databse registry entry (TabularDB record).
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 *@since 1.2
 */
public class TabularDatabaseInformation extends ResourceInformation {

    /** Construct a new TabularDatabaseInformation
     * @param ivorn
     * @param title
     * @param description
     * @param url
     */
    public TabularDatabaseInformation(URI ivorn, String title, String description, URL url, URL logo,DatabaseBean[] databases) {
        super(ivorn, title, description, url,logo);
        this.databases = databases;
    }
    protected final DatabaseBean[] databases;
    
    /** list the databases described by this registry entry */
    public DatabaseBean[] getDatabases() {
        return databases;
    }

}


/* 
$Log: TabularDatabaseInformation.java,v $
Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/11/04 14:38:58  nw
added logo field

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/