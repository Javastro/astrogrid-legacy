/*$Id: ScriptStarTable.java,v 1.2 2004/12/06 20:03:03 clq2 Exp $
 * Created on 06-Dec-2004
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

import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperStarTable;

/** A wrapper start table that provides some additional methods, to make it more convenient for groovy scripting.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Dec-2004
 *
 */
public class ScriptStarTable extends WrapperStarTable {

    /** Construct a new ScriptStarTable
     * @param arg0
     */
    public ScriptStarTable(StarTable arg0) {
        super(arg0);
    }

    /** access iterator that will return each row array in turn 
     * @throws IOException*/
    public Iterator iterator() throws IOException {
        final RowSequence seq = super.getRowSequence();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("Can't remove from tables");
            }

            public boolean hasNext() {
                return seq.hasNext();
            }

            public Object next() {                
                try {
                    seq.next();
                    return seq.getRow();
                } catch (IOException e) {
                      throw new RuntimeException("StarTable.iterator().next() throw exception",e);
                }
            }
        };
    }
    
    /** access iterator that will return each value in a column in turn 
     * @throws IOException*/
    public Iterator columnIterator(final int col) throws IOException {
        final RowSequence seq = super.getRowSequence();
        return new Iterator() {

            public void remove() {
                throw new UnsupportedOperationException("Can't remove from tables");
            }

            public boolean hasNext() {
                return seq.hasNext();
            }

            public Object next() {                
                try {
                    seq.next();
                    return seq.getCell(col);
                } catch (IOException e) {
                      throw new RuntimeException("StarTable.iterator().next() throw exception",e);
                }
            }
        };        
    }
    
}


/* 
$Log: ScriptStarTable.java,v $
Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/