/*$Id: ConstantColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
 * Created on 07-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting.table;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;

/** create a column containing a constant value in each cell.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class ConstantColumnWrapperTable extends AbstractColumnWrapperTable
        implements ScriptStarTable {

    /** Construct a new ConstantColumnWrapperTable
     * @param meta metadata for this column
     * @param val constant value for this column
     * @param original original table.
     */
    public ConstantColumnWrapperTable(ColumnInfo meta, Object val,StarTable original) {
        super(meta, original);
        this.val = val;
    }
    protected final Object val;

    /**
     * @see org.astrogrid.scripting.table.AbstractColumnWrapperTable#computeValue(java.lang.Object[])
     */
    protected Object computeValue(Object[] row) {
        return val;
    }

}


/* 
$Log: ConstantColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/