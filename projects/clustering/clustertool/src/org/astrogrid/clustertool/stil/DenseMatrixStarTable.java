/*
 * $Id: DenseMatrixStarTable.java,v 1.1 2010/01/19 21:25:14 pah Exp $
 * 
 * Created on Sep 14, 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.clustertool.stil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RandomStarTable;

/**
 * Minimal implementation that is based on an {@link AGDenseMatrix}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) Sep 14, 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DenseMatrixStarTable extends RandomStarTable {

    List<ColumnInfo> ci = new ArrayList<ColumnInfo>();
    private final AGDenseMatrix mat;

    public DenseMatrixStarTable(AGDenseMatrix mat) {
        this.mat = mat;
        for (int i = 0; i < mat.numColumns(); i++) {
            ci.add(new ColumnInfo("col"+i, Double.class,"a column of the matrix"));
        }
    }
    @Override
    public long getRowCount() {
       return mat.numRows();
    }

    @Override
    public int getColumnCount() {
        return mat.numColumns();
    }

    @Override
    public ColumnInfo getColumnInfo(int icol) {
       return ci.get(icol);
    }
    @Override
    public Object getCell(long irow, int icol) throws IOException {
        return new Double(mat.get((int) irow, icol));
     }

}


/*
 * $Log: DenseMatrixStarTable.java,v $
 * Revision 1.1  2010/01/19 21:25:14  pah
 * moved here for neatness
 *
 * Revision 1.2  2009/09/20 17:17:59  pah
 * checking just prior to bham visit
 *
 * Revision 1.1  2009/09/14 19:09:26  pah
 * basic framework of GUI mostly working
 *
 */
