/*$Id: TableBean.java,v 1.2 2006/02/02 14:19:48 nw Exp $
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

/** descripition of a  single table in a TablularDB registry entry
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 * @since 1.2
 */
public class TableBean implements Serializable {

    /** Construct a new TableInformation
     * 
     */
    public TableBean(String name,String description, ColumnBean[] columns) {
        super();
        this.name = name;
        this.description = description;
        this.columns  = columns;
    }
    private final String name;
    private final String description;
    private final ColumnBean[] columns;

    /** list descriptions of the columns in this table */
    public ColumnBean[] getColumns() {
        return this.columns;
    }
    /** human redable description for this table */
    public String getDescription() {
        return this.description;
    }
    /** the name of this table */
    public String getName() {
        return this.name;
    }
}


/* 
$Log: TableBean.java,v $
Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/