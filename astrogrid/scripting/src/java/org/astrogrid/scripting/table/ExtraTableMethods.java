/*$Id: ExtraTableMethods.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

import java.io.IOException;
import java.util.Iterator;

/** This interface defines an additional set of methods that all {@link ScriptStarTable} instances support.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
interface ExtraTableMethods {
    /** iterate over each row in the table. 
     * @return an iterator that will return a {@link java.util.List} of the cells in each row
     * @throws IOException*/
    public abstract Iterator iterator() throws IOException;

    /**iterate over each cell in a column 
     * @param col column to iterate over
     * @return an iteratore that will return the value of each cell in this column.
     * @throws IOException*/
    public abstract Iterator columnIterator(final int col) throws IOException;
    
    /** Return a mutable copy of this table.
     * @return a mutable table that has the same structure and contents as the current table.
     * @throws IOException
     */
    public MutableScriptStarTable asMutableTable() throws IOException;
    
    /**Return a new table, based on this table, but with one column removed
     * @param index index of the column to remove.
     * @return a new table, based on this one, with one less column.
     */
    public ScriptStarTable removeColumn(int index);
    
    /** Return a new table, based on this one with an additional colum
     * @param meta metadata for the new column
     * @param colValue a value that defines the column value. May either be
     * <ul>
     *   <li>a static {@link java.lang.reflect.Method}, of type <code>Object method(Object[] arr)</code>. The method will be passed
     * the values of the other cells in the row, and the result used as the value of the new cell</li>
     *  <li>an instance of {@link ColumnFunction} whose method will be called to define the value of the new cell</li>
     * <li>a <i>groovy closure</i> - which will be passed a single parameter - a list containing the values of the other cells in the row. The result
     * returned by the closure will be used as the value of the new cell.
     * <li>any other object - in which case it is used as a constant value for every cell in this column.
     * </ul>
     * @return a new table, same as this one, with an extra column at the end.
     */
    public ScriptStarTable addColumn(ColumnInfo meta,Object colValue);
}


/* 
$Log: ExtraTableMethods.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/