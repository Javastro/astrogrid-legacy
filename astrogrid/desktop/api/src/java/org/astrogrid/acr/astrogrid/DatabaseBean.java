/*$Id: DatabaseBean.java,v 1.2 2006/02/02 14:19:48 nw Exp $
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

import java.io.Serializable;

/** describes a  single database in a TablularDB registry entry
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 *@since 1.2
 */
public class DatabaseBean implements Serializable{

    /** Construct a new DatabaseBean
     * 
     */
    public DatabaseBean(String name,String description, TableBean[] tables) {
        super();
        this.name = name;
        this.description = description;
        this.tables = tables;
    }
    
    private final String name;
    private final String description;
    private final TableBean[] tables;
    
/** human-readable description of the database */
    public String getDescription() {
        return this.description;
    }
    /** name of the database */
    public String getName() {
        return this.name;
    }
    /** list of descriptions of the tables in this database */
    public TableBean[] getTables() {
        return this.tables;
    }
}


/* 
$Log: DatabaseBean.java,v $
Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/