/*$Id: RemoveColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

import java.io.IOException;
import java.util.Iterator;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.ColumnPermutedStarTable;
import uk.ac.starlink.table.RowPermutedStarTable;
import uk.ac.starlink.table.StarTable;

/** wrapper that hides a column in a table.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class RemoveColumnWrapperTable extends ColumnPermutedStarTable implements
        ScriptStarTable {

    /** Construct a new RemoveColumnWrapperTable
     * @param table
     * @param arg1
     */
    public RemoveColumnWrapperTable(StarTable table, int colToRemove) {
        super(table, calcArray(table.getColumnCount(),colToRemove));
    }

    /**
     * @param columnCount
     * @param colToRemove
     * @return
     */
    private static int[] calcArray(int columnCount, int colToRemove) {
        if (colToRemove > columnCount -1) {
            throw new IllegalArgumentException("no column #" + colToRemove + "in table - only has " + columnCount );
        }
        int[] arr = new int[columnCount - 1];
        for (int i = 0; i < colToRemove; i++) {
            arr[i] =i;
        }
        for (int i = colToRemove + 1; i < columnCount; i++) {
            arr[i-1] =i;
        }
        return arr;
    }

    protected final ExtraTableMethods methods = new ExtraTableMethodsImpl() {

        protected StarTable getTable() {
            return RemoveColumnWrapperTable.this;
        }
    };
    public ScriptStarTable addColumn(ColumnInfo meta, Object colValue) {
        return this.methods.addColumn(meta, colValue);
    }
    public MutableScriptStarTable asMutableTable() throws IOException {
        return this.methods.asMutableTable();
    }
    public Iterator columnIterator(int col) throws IOException {
        return this.methods.columnIterator(col);
    }
    public Iterator iterator() throws IOException {
        return this.methods.iterator();
    }
    public ScriptStarTable removeColumn(int index) {
        return this.methods.removeColumn(index);
    }
}


/* 
$Log: RemoveColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/