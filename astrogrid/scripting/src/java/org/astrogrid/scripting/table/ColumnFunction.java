/*$Id: ColumnFunction.java,v 1.2 2004/12/07 16:50:33 jdt Exp $
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

/** interface to something that can define a column cell.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Dec-2004
 *
 */
public interface ColumnFunction {
    /** compute a value for this cell.
     * @param row the other cell values in this row
     * @return value for this cell
     * @throws Exception if unable to compute.
     */
    public Object computeValue(Object[] row) throws Exception;

}


/* 
$Log: ColumnFunction.java,v $
Revision 1.2  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.1.2.1  2004/12/07 14:47:58  nw
got table manipulation working.
 
*/