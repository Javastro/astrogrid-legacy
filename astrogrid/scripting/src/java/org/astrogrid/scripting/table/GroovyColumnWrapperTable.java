/*$Id: GroovyColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

import java.util.Arrays;

import groovy.lang.Closure;


import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;

/** Define a new column, using values returned from a groovy closure.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class GroovyColumnWrapperTable extends AbstractColumnWrapperTable {

    /** Construct a new GroovyColumnWrapperTable
     * @param meta
     * @param original
     */
    public GroovyColumnWrapperTable(ColumnInfo meta, Closure c,StarTable original) {
        super(meta, original);
        this.c = c;
    }
    protected final Closure c;

    protected Object computeValue(Object[] row) throws Exception {
        return c.call(Arrays.asList(row));
    }

}


/* 
$Log: GroovyColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/