/*$Id: FunctionColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

/** Define a new column using values returned by a {@link org.astrogrid.scripting.table.ColumnFunction}
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class FunctionColumnWrapperTable extends AbstractColumnWrapperTable implements
        ScriptStarTable {

    /** Construct a new FunctionColumnWrapperTable
     * @param meta
     * @param original
     */
    public FunctionColumnWrapperTable(ColumnInfo meta, ColumnFunction funct,StarTable original) {
        super(meta, original);
        this.funct = funct;
    }
    protected final ColumnFunction funct;

    /**
     * @throws Exception
     * @see org.astrogrid.scripting.table.AbstractColumnWrapperTable#computeValue(java.lang.Object[])
     */
    protected Object computeValue(Object[] row) throws Exception {
        return funct.computeValue(row);
    }

}


/* 
$Log: FunctionColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/