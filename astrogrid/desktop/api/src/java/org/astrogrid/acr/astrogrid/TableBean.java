/*$Id: TableBean.java,v 1.5 2007/01/24 14:04:44 nw Exp $
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

/** descripition of a  single table in a TablularDB registry entry
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-Sep-2005
 * @since 1.2
 */
public class TableBean implements Serializable {

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
	/** Construct a new TableInformation
     * 
     */
    public TableBean(String name,String description, ColumnBean[] columns) {
        super();
        this.name = name;
        this.description = description;
        this.columns  = columns;
    }
    static final long serialVersionUID = 6606414688098836079L;
    protected final String name;
    protected final String description;
    protected final ColumnBean[] columns;

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
    
    public String toString() {
        StringBuffer sb = new StringBuffer("TableBean[");
        sb.append("name: ");
        sb.append(name);
        sb.append(" description: ");
        sb.append(description);
        sb.append(" columns[");
        for (int i = 0; i < columns.length; i++) {
            sb.append(columns[i]);
            sb.append(" ");
        }
        sb.append("]]");
        return sb.toString();
    }
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + TableBean.hashCode(this.columns);
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TableBean other = (TableBean) obj;
		if (!Arrays.equals(this.columns, other.columns))
			return false;
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
		return true;
	}
}


/* 
$Log: TableBean.java,v $
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