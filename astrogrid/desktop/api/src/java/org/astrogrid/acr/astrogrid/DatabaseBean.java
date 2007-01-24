/*$Id: DatabaseBean.java,v 1.5 2007/01/24 14:04:44 nw Exp $
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
import java.util.Arrays;

/** describes a  single database in a TablularDB registry entry
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-Sep-2005
 *@since 1.2
 */
public class DatabaseBean implements Serializable{

    private static int hashCode(Object[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}
	/** Construct a new DatabaseBean
     * 
     */
    public DatabaseBean(String name,String description, TableBean[] tables) {
        super();
        this.name = name;
        this.description = description;
        this.tables = tables;
    }
    
    static final long serialVersionUID = -3221050086290430194L;
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
    
    public String toString() {
        StringBuffer sb = new StringBuffer("DatabaseBean[");
        sb.append("name: ");
        sb.append(name);
        sb.append(" description: ");
        sb.append(description);
        sb.append(" tables:[");
        for (int i =0; i < tables.length; i++) {
            sb.append(tables[i]);
            sb.append(" ");
        }
        sb.append("]]");
        return sb.toString();
    }
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + DatabaseBean.hashCode(this.tables);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DatabaseBean other = (DatabaseBean) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (!Arrays.equals(this.tables, other.tables))
			return false;
		return true;
	}
}


/* 
$Log: DatabaseBean.java,v $
Revision 1.5  2007/01/24 14:04:44  nw
updated my email address

Revision 1.4  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2.6.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/