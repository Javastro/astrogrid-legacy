/*$Id: MutableScriptStarTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.table.StarTable;

/** Extension of the standard starlink read-write table that supports the extra scripting methods.
 * See RowListStarTable javadoc <a href="http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/RowListStarTable.html">http://www.star.bristol.ac.uk/~mbt/stil/javadocs/uk/ac/starlink/table/RowListStarTable.html</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
public class MutableScriptStarTable extends RowListStarTable implements
        ScriptStarTable {

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
    /** Construct a new MutableScriptStarTable
     * @param arg0
     */
    public MutableScriptStarTable(ColumnInfo[] arg0) {
        super(arg0);
    }

    /** Construct a new MutableScriptStarTable
     * @param arg0
     */
    public MutableScriptStarTable(StarTable arg0) {
        super(arg0);
    }
    protected final ExtraTableMethods methods = new ExtraTableMethodsImpl() {
        public MutableScriptStarTable asMutableTable() {//oberridden - returns self.
            return MutableScriptStarTable.this;
        }
        protected StarTable getTable() {
            return MutableScriptStarTable.this;
        }
    };


}


/* 
$Log: MutableScriptStarTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/