/*$Id: AbstractColumnWrapperTable.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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
import java.util.List;
import java.util.Map;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperStarTable;

/** abstract class for wrappers that define a new column in some way.
 * overrides all methods to do with meta-data. Extensions of this class just have to define the cell values.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
abstract class AbstractColumnWrapperTable extends WrapperScriptStarTable  {

    /** Construct a new AbstractColumnWrapperTable
     * @param meta metadata for new column
     * @param original original table
     * 
     */
    public AbstractColumnWrapperTable(ColumnInfo meta, StarTable original) {
        super(original);
        this.col = meta;
        this.originalColCount = original.getColumnCount();
    }
    protected final ColumnInfo col;
    protected final int originalColCount;
   
    public Object getCell(long arg0, int arg1) throws IOException {
        if (arg1 == originalColCount) {
            try {
                return computeValue(super.getRow(arg0));
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                IOException ioe = new IOException(e.getMessage());
                ioe.initCause(e);
                throw ioe;
            }
        } else {
            return super.getCell(arg0, arg1);
        }
    }

    public int getColumnCount() {
        return originalColCount + 1;
    }
    public ColumnInfo getColumnInfo(int arg0) {
        if (arg0 == originalColCount) {
            return col;
        } else {
            return super.getColumnInfo(arg0);
        }
    }
    public Object[] getRow(long arg0) throws IOException {
        Object[] row =  super.getRow(arg0);
        try {
        Object val = computeValue(row);
        Object[] newRow = new Object[originalColCount + 1];
        System.arraycopy(row,0,newRow,0,row.length);
        newRow[originalColCount] = val;
        return newRow;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            IOException ioe = new IOException(e.getMessage());
            ioe.initCause(e);
            throw ioe;
        }        
    }
    public RowSequence getRowSequence() throws IOException {
        final RowSequence seq = super.getRowSequence();
        return new RowSequence() {

            public void next() throws IOException {
                seq.next();
            }

            public boolean hasNext() {
                return seq.hasNext();
            }

            public Object getCell(int arg0) throws IOException {
                if (arg0 == originalColCount) {
                    try {
                    return computeValue(seq.getRow());
                    } catch (IOException e) {
                        throw e;
                    } catch (Exception e) {
                        IOException ioe = new IOException(e.getMessage());
                        ioe.initCause(e);
                        throw ioe;
                    }                    
                } else {
                    return seq.getCell(arg0);
                }
            }

            public Object[] getRow() throws IOException {
                Object[] row =  seq.getRow();
                try {
                Object val = computeValue(row);
                Object[] newRow = new Object[originalColCount + 1];
                System.arraycopy(row,0,newRow,0,row.length);
                newRow[originalColCount] = val;
                return newRow;
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    IOException ioe = new IOException(e.getMessage());
                    ioe.initCause(e);
                    throw ioe;
                }                
            }

            public void close() throws IOException {
                seq.close();
            }
        };
    }
    /** define the value for a cell in the new column, based on the values of the other cells in this row */
    protected abstract Object computeValue(Object[] row) throws Exception;
    
}


/* 
$Log: AbstractColumnWrapperTable.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/