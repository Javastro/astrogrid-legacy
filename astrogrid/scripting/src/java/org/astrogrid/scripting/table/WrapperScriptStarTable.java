/*$Id: WrapperScriptStarTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperStarTable;

/** wrap an existing StarTable to make it a script star table.
 * uses the multiple-inheritance trick to reuse the definition of the extra methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
class WrapperScriptStarTable extends WrapperStarTable implements
        ScriptStarTable {

    /** Construct a new WrapperScriptStarTable
     * @param arg0
     */
    public WrapperScriptStarTable(StarTable arg0) {
        super(arg0);
    }

    protected final ExtraTableMethods methods = new ExtraTableMethodsImpl() {

        protected StarTable getTable() {
            return WrapperScriptStarTable.this;
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
$Log: WrapperScriptStarTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/