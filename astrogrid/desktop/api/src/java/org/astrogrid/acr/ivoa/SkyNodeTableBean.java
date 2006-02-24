/*$Id: SkyNodeTableBean.java,v 1.1 2006/02/24 12:17:52 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;

import java.util.Map;
/** extension of table bean for Sky Node services, which return further metadata 
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Feb-2006
 *@since 1.9
 */
public class SkyNodeTableBean extends TableBean {

    public SkyNodeTableBean(String name, String description, SkyNodeColumnBean[] columns,String primaryKey, int rows, int rank, Map relations) {
        super(name, description, columns);
        this.primaryKey = primaryKey;
        this.rows = rows;
        this.rank = rank;
        this.relations = relations;
    }
    private final String primaryKey;
    private final int rows;
    private final int rank;
    private final Map relations;
    /** access name of the primary key for this table */
    public String getPrimaryKey() {
        return this.primaryKey;
    }
    /** access the rank of this table (measure of importance) */
    public int getRank() {        
        return this.rank;
    }
    /** access the relations for this table
     * 
     * @return a map of foreign key name -> table name mappings
     */
    public Map getRelations() {
        return this.relations;
    }
    /** access the number of rows in the table
     * 
     * @return
     */
    public int getRows() {
        return this.rows;
    }
   
    
    

}


/* 
$Log: SkyNodeTableBean.java,v $
Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/