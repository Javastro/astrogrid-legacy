/*$Id: SkyNodeTableBean.java,v 1.4 2007/03/08 17:48:06 nw Exp $
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

import java.util.Map;

import org.astrogrid.acr.astrogrid.TableBean;
/** extension of table bean for Sky Node services, which return further metadata 
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Feb-2006
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
    static final long serialVersionUID = -868967333231244844L;
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
   
    public String toString() {
        StringBuffer sb = new StringBuffer("SkyNodeTableBean[");
        sb.append("name: ");
        sb.append(name);
        sb.append(" description: ");
        sb.append(description);
        sb.append(" primaryKey: ");
        sb.append(primaryKey);
        sb.append(" rows: ");
        sb.append(rows);
        sb.append(" rank: ");
        sb.append(rank);
        sb.append(" relations: ");
        sb.append(relations);
        sb.append(" columns[");
        for (int i = 0; i < columns.length; i++) {
            sb.append(columns[i]);
            sb.append(" ");
        }
        sb.append("]]");
        return sb.toString();
    }
    
    

}


/* 
$Log: SkyNodeTableBean.java,v $
Revision 1.4  2007/03/08 17:48:06  nw
tidied.

Revision 1.3  2007/01/24 14:04:45  nw
updated my email address

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/