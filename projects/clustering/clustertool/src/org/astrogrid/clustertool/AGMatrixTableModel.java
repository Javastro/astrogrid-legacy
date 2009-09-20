/*
 * $Id: AGMatrixTableModel.java,v 1.1 2009/09/20 17:18:01 pah Exp $
 * 
 * Created on Sep 20, 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.clustertool;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.astrogrid.matrix.Matrix;

import no.uib.cipr.matrix.AGDenseMatrix;

public class AGMatrixTableModel extends AbstractTableModel implements
        TableModel {

    /** Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3470923868557856271L;
    private AGDenseMatrix data = new AGDenseMatrix(0,0);

    public AGMatrixTableModel(AGDenseMatrix m) {
        data = m;
    }
    
    public AGMatrixTableModel() {
       
    }

    public int getColumnCount() {
      return data.numColumns();
    }

    public int getRowCount() {
        return data.numRows();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex, columnIndex);
    }
    
    public void setMatrix (Matrix m){
       data=(AGDenseMatrix) m;
       fireTableStructureChanged();
    }

}


/*
 * $Log: AGMatrixTableModel.java,v $
 * Revision 1.1  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 */
