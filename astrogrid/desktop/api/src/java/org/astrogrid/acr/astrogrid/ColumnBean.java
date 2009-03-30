/*$Id: ColumnBean.java,v 1.10 2009/03/30 15:02:55 nw Exp $
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

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.BaseParam;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.TableDataType;

/** Metadata for a column in a table.
 * 
 * @note most fields are inherited from the parent class - {@link BaseParam}
 * @see TableBean
 * @see Catalog
 * @see Registry Querying for registry resources
 * @bean
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 12-Sep-2005
 */
public class ColumnBean extends BaseParam implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 8307167670049520934L;

    /** Construct a new ColumnInformation
     *@exclude 
     *@deprecated 
     */
    @Deprecated
    public ColumnBean(final String name,final String description,final String ucd, final String datatype,final String unit) {
        super();
        setName(name);
        setDescription(description);
        setUcd(ucd);
        if (datatype != null) {
            
            this.tableDataType = new TableDataType();
            this.tableDataType.setType(datatype);
        } else {
            this.tableDataType = null;
        }
        setUnit(unit);
        this.std = false;
                
    }
    /** @exclude */
    public ColumnBean(final String name,final String description,final String ucd, final TableDataType datatype,final String unit,final boolean std) {
        super();
        setName(name);
        setDescription(description);
        setUcd(ucd);
        this.tableDataType = datatype;
        setUnit(unit);
        this.std = std;
                
    }    
    
    protected final boolean std;

    protected final TableDataType tableDataType;
    
    /** Universal column descriptor for this column
     * @exclude
     *  @deprecated - use `{@link #getUcd()} */
    @Deprecated
    public String getUCD() {
        return super.getUcd();
    }

    /**                     If true, the meaning and use of this column is
                     reserved and defined by a standard model.  
                     
                     If false, 
                     it represents a database-specific parameter 
                     that effectively extends beyond the standard.  If
                     not provided, then the value is unknown.*/
    public final boolean isStd() {
        return this.std;
    }
    /**@exclude */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.tableDataType == null) ? 0 : this.tableDataType.hashCode());
        result = prime * result + (this.std ? 1231 : 1237);
        return result;
    }
    /** @exclude */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnBean other = (ColumnBean) obj;
        if (this.tableDataType == null) {
            if (other.tableDataType != null) {
                return false;
            }
        } else if (!this.tableDataType.equals(other.tableDataType)) {
            return false;
        }
        if (this.std != other.std) {
            return false;
        }
        return true;
    }
    /** the data type for this column */
    public final TableDataType getColumnDataType() {
        return this.tableDataType;
    }
    
    /** @exclude 
     * @deprecated use {@link #getColumnDataType} */
    @Deprecated
    public final String getDatatype() {
        if (tableDataType == null) {
            return null;
        } 
        return tableDataType.getType();
    }
	
}


/* 
$Log: ColumnBean.java,v $
Revision 1.10  2009/03/30 15:02:55  nw
Added override annotations.

Revision 1.9  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.8  2008/01/21 09:47:26  nw
Incomplete - task 134: Upgrade to reg v1.0

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