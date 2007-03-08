/*$Id: ColumnBean.java,v 1.7 2007/03/08 17:46:56 nw Exp $
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

/** describes a single column in a tabular database.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-Sep-2005
 * @since 1.2
 * @todo may need to model shape, as well as type, at this level.
 */
public class ColumnBean implements Serializable{

    /** Construct a new ColumnInformation
     * 
     */
    public ColumnBean(String name,String description,String ucd, String datatype,String unit) {
        super();
        this.name = name;
        this.description = description;
        this.UCD = ucd;
        this.datatype = datatype;
        this.unit = unit;
                
    }
    
    static final long serialVersionUID = -2671716048567347051L;
    protected final String name;
    protected final String description;
    protected final String UCD;
    protected final String datatype;
    protected final String unit;

    /**
     * the type of this column     
     */
    public String getDatatype() {
        return this.datatype;
    }
/**
 * human-readable description of this column
 */
    public String getDescription() {
        return this.description;
    }
    /** name of this column */
    public String getName() {
        return this.name;
    }
    /** Universal column descriptor for this column */
    public String getUCD() {
        return this.UCD;
    }
    /** description of theunits of this column */
    public String getUnit() {
        return this.unit;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("ColumnBean[");
        sb.append("name: ");
        sb.append(name);
        sb.append(" description: ");
        sb.append(description);
        sb.append(" UCD: ");
        sb.append(UCD);
        sb.append(" datatype: ");
        sb.append(datatype);
        sb.append(" unit: ");
        sb.append(unit);
        sb.append("]");
        return sb.toString();
    }
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.UCD == null) ? 0 : this.UCD.hashCode());
		result = PRIME * result + ((this.datatype == null) ? 0 : this.datatype.hashCode());
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + ((this.unit == null) ? 0 : this.unit.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ColumnBean other = (ColumnBean) obj;
		if (this.UCD == null) {
			if (other.UCD != null)
				return false;
		} else if (!this.UCD.equals(other.UCD))
			return false;
		if (this.datatype == null) {
			if (other.datatype != null)
				return false;
		} else if (!this.datatype.equals(other.datatype))
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
		if (this.unit == null) {
			if (other.unit != null)
				return false;
		} else if (!this.unit.equals(other.unit))
			return false;
		return true;
	}
}


/* 
$Log: ColumnBean.java,v $
Revision 1.7  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.6  2007/01/24 14:04:44  nw
updated my email address

Revision 1.5  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.2.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.3.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.3  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.2  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.1  2005/09/12 15:21:43  nw
added stuff for adql.
 
*/