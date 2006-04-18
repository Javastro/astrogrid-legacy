/*$Id: TabularDatabaseInformation.java,v 1.4 2006/04/18 23:25:45 nw Exp $
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
    static final long serialVersionUID = -3229984290051480122L;
    protected final DatabaseBean[] databases;
    
    /** list the databases described by this registry entry */
    public DatabaseBean[] getDatabases() {
        return databases;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("TabularDatabaseInformation[");
        sb.append(super.toString());
        sb.append(" databases:[");
        for (int i =0; i < databases.length; i++) {
            sb.append(databases[i]);
        }
        sb.append("]]");
        return sb.toString();
        
    }
    
    

}


/* 
$Log: TabularDatabaseInformation.java,v $
Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.6.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.3.6.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/11/04 14:38:58  nw
added logo field

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/