/*$Id: MethodColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;

/** Define a new column, based on values returned by a {@link java.lang.reflect.Method}
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class MethodColumnWrapperTable extends AbstractColumnWrapperTable implements
        ScriptStarTable {

    /** Construct a new MethodColumnWrapperTable
     * @param meta
     * @param original
     */
    public MethodColumnWrapperTable(ColumnInfo meta,Method method, StarTable original) {
        super(meta, original);
        this.method=method;
    }
    protected final Method method;

    /**
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @see org.astrogrid.scripting.table.AbstractColumnWrapperTable#computeValue(java.lang.Object[])
     */
    protected Object computeValue(Object[] row) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return method.invoke(null,row);
    }

}


/* 
$Log: MethodColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/