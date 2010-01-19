/*
 * $Id: AGCovMatrixTableModel.java,v 1.1 2010/01/19 21:27:38 pah Exp $
 * 
 * Created on 9 Jan 2010 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2010 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.clustertool;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.astrogrid.cluster.cluster.CovarianceKind;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.Matrix;

public class AGCovMatrixTableModel extends AbstractTableModel implements
TableModel  {

    private List<Matrix> cov;
    private CovarianceKind cvk;
    private int idx;
    private int ndim;
    public AGCovMatrixTableModel(Matrix m, int ndim, CovarianceKind cvk) {
        super();
        this.cov = domatrix(m,ndim,cvk);
        this.cvk = cvk;
        this.idx = 1;
    }

    private List<Matrix> domatrix(Matrix m, int ndim2, CovarianceKind ck) {

        List<Matrix> retval = new ArrayList<Matrix>();
        switch (ck) {
        case common: //FIXME - this might not be the correct interpretation of common (which I am interpreting as the same covariance for all dimensions in a class)
            for (int i = 0; i < m.numRows(); i++) {
                Matrix mm = new AGDenseMatrix(ndim2,ndim2).zero();
                for(int j = 0; j < ndim2; j++){
                    mm.set(j,j,m.get(i, 0));
                }
                retval.add(mm);
            }

            break;
        case diagonal:
            for (int i = 0; i < m.numRows()/ndim2; i++) {
                Matrix mm = new AGDenseMatrix(ndim2,ndim2).zero();
                for(int j = 0; j < ndim2; j++){
                    mm.set(j,j,m.get(i*ndim2+j, 0));
                }
                retval.add(mm);
            }

            break;

        case free:
            for (int i = 0; i < m.numRows()/(ndim2*ndim2); i++) {
                Matrix mm = new AGDenseMatrix(ndim2,ndim2).zero();
                for(int j = 0; j < ndim2; j++){
                    for(int k = 0; k < ndim2; k++){
                        mm.set(j,k,m.get(i*ndim2*ndim2+j*ndim2+k, 0));
                    }
                    retval.add(mm);
                }
            }

            break;
        }

        return retval;
    }

    public AGCovMatrixTableModel() {

    }

    public int getColumnCount() {
        return ndim;
    }

    public int getRowCount() {
        return ndim;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        return cov.get(idx -1).get(rowIndex, columnIndex);   


    }


    public void setMatrices(Matrix c, int ndim, CovarianceKind ck){
        cov = domatrix(c, ndim, ck);
        cvk = ck;
        this.idx=1;
        this.ndim = ndim;
        fireTableStructureChanged();
    }

    public void setIdx(int i){
        idx = i;
        fireTableDataChanged();
    }

}


/*
 * $Log: AGCovMatrixTableModel.java,v $
 * Revision 1.1  2010/01/19 21:27:38  pah
 * better display of covariances + cli operation
 *
 */
