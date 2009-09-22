/*
 * $Id: AGDenseMatrix.java,v 1.5 2009/09/22 07:04:16 pah Exp $
 * 
 * Created on 27 Nov 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package no.uib.cipr.matrix;

import java.io.IOException;
import java.util.Arrays;
import no.uib.cipr.matrix.io.MatrixVectorReader;

import org.astrogrid.matrix.Matrix;
import org.netlib.lapack.LAPACK;
import org.netlib.util.intW;

public class AGDenseMatrix extends DenseMatrix implements Matrix {

    /**
     * @param r
     * @throws IOException
     */
    public AGDenseMatrix(final MatrixVectorReader r) throws IOException {
        super(r);

    }

    /**
     * @param x
     */
    public AGDenseMatrix(final Vector x) {
        super(x);
       
    }

    /**
     * @param values
     */
    public AGDenseMatrix(final double[][] values) {
        super(values);
    }

    /**
     * @param numRows
     * @param numColumns
     */
    public AGDenseMatrix(final int numRows, final int numColumns) {
        super(numRows, numColumns);
    }

    /**
     * @param A
     * @param deep
     */
    public AGDenseMatrix(final Matrix A, final boolean deep) {
        super(A, deep);
        data = null;
    }

    public static AGDenseMatrix repeatColumn(final Vector v, final int n) {
        if (v instanceof DenseVector) {
            final DenseVector dv = (DenseVector) v;
            
        
        final AGDenseMatrix retval = new AGDenseMatrix(v.size(),n);
        for (int i = 0; i < n; i++) {
            System.arraycopy(dv.getData(), 0, retval.data, i*retval.numRows, retval.numRows);
        }
        return retval;
       } else {
            throw new UnsupportedOperationException("this method only works with DenseVectors at the moment");
            //TODO implement a generic slow version.
        }
     }
    
    public static AGDenseMatrix repeatRow(final Vector v, final int n){
        final AGDenseMatrix retval = new AGDenseMatrix(n, v.size());
        for (MatrixEntry e : retval) {
            e.set(v.get(e.column()));
        }
        return retval;
    }

    /**
     * @param A
     */
    public AGDenseMatrix(final no.uib.cipr.matrix.Matrix A) {
        super(A);
    }

    /**
     * Create a new matrix by tiling a vector. The vector is assumed to be copied columnwise.
     * @param v
     * @param ni
     * @param nj
     */
    public AGDenseMatrix(final Vector v, final int ni, final int nj) {
        super(v.size()*ni, nj);
        // copy in the initial data
        int i = 0;
        for (VectorEntry vectorEntry : v) {
            this.data[i++] = vectorEntry.get();
        }
        // then replicate elsewhere using fast array copy.
        final int size = v.size();
        for (i = 1; i < ni * nj ; i++){
            System.arraycopy(data, 0, data, i*size, size);
        }
    }
    
    /**
     * Create a new matrix by tiling the input matrix.
     * @param m
     * @param ni
     * @param nj
     */
    public AGDenseMatrix(final Matrix m, final int ni, final int nj) {
        super(m.numRows()* ni, m.numColumns()*nj);
        // firstly copy into the top left corner
        for (MatrixEntry me : m) {
            this.set(me.row(), me.column(), me.get());
        }
        //then tile the columns downwards
        
        for(int j = 0; j < m.numColumns(); j++){
            for (int i = 1; i < ni; i++) {
                final int src = j*m.numRows()*ni;
                System.arraycopy(data, src, data, src + i * m.numRows(), m.numRows());
            }
        }
        // then copy sideways;
        final int blocksize = m.numRows()*ni*m.numColumns();
        for (int j=1; j < nj; j++){
            System.arraycopy(data, 0, data, j*blocksize, blocksize);
        }
    }

    /**
     * Creates new matrix from existing array. Note that the array is simply used as internal storage
     * @param numRows
     * @param numColumns
     * @param indata
     */
    public AGDenseMatrix(int numRows, int numColumns, double[] indata) {
        super(numRows, numColumns); //IMPL would have been nice to avoid the array creation
        if (numColumns* numRows != indata.length) {
            throw new IllegalArgumentException("datasize not equal to numrows*numcolumns"); //IMPL assert better?
        }
        this.data = indata;
    }

    public Matrix slice(final int rowstart, final int rowend, final int colstart, final int colend) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "Matrix.slice() not implemented");
    }

    /**
     * Return a single column or row of the matrix
     * 
     * @param idx
     *            the 0 based index of the row or column to return;
     * @param dorow
     *            if true return a row - if false return a column.
     * @return
     */
    public Vector slicev(final int idx, final boolean dorow) {
        if (dorow) {
            if (idx < numRows) {
                final Vector retval = new DenseVector(numColumns);
                for (int j = 0; j < numColumns; j++) {
                    retval.set(j, this.get(idx, j));
                }
                return retval;
            } else {
                throw new IllegalArgumentException("row index " + idx
                        + " greater than " + numRows);
            }
        } else {
            if (idx < numColumns) {
                final double[] vals = new double[numRows];
                System.arraycopy(data, idx * numRows, vals, 0, numRows);
                final Vector retval = new DenseVector(vals, false);
                return retval;
            } else {
                throw new IllegalArgumentException("column index" + idx
                        + "greater than " + numColumns);
            }

        }
    }
    
    public Matrix slicem(final int idx, final boolean dorow) {
        if (dorow) {
            if (idx < numRows) {
                final Matrix retval = new AGDenseMatrix(1,numColumns);
                for (int j = 0; j < numColumns; j++) {
                    retval.set(0, j, this.get(idx, j));
                }
                return retval;
            } else {
                throw new IllegalArgumentException("row index " + idx
                        + " greater than " + numRows);
            }
        } else {
            if (idx < numColumns) {
                final double[][] vals = new double[numRows][1];
                System.arraycopy(data, idx * numRows, vals, 0, numRows);
                final Matrix retval = new AGDenseMatrix(vals);
                return retval;
            } else {
                throw new IllegalArgumentException("column index" + idx
                        + "greater than " + numColumns);
            }

        }
    }
    
   

    public Vector sum(final int dim) {
        Vector answ;
        switch (dim) {
        case 1:
            final double[] colsum = new double[this.numColumns()];
            for (MatrixEntry e : this) {
                colsum[e.column()] += e.get();
            }
            answ = new DenseVector(colsum,false);
            break;
        case 2:
            final double[] rowsum = new double[this.numRows()];
            for (MatrixEntry e : this) {
                rowsum[e.row()] += e.get();
            }
            answ = new DenseVector(rowsum, false);
            break;
        default:

            throw new UnsupportedOperationException(
                    "Matrix.sum() not implemented for higher dimensions");

        }

        return answ;
    }

    /**
     * Return a square identity matrix.
     * 
     * @param ncentres
     * @return
     */
    public static Matrix identity(final int ncentres) {
        final Matrix retval = new AGDenseMatrix(ncentres, ncentres);
        for (int i = 0; i < ncentres; i++) {
            retval.set(i, i, 1.0);
        }
        return retval;
    }

    public Matrix pow(final double i) {

        final Matrix retval = new AGDenseMatrix(this.numRows, this.numColumns);
        for (MatrixEntry e : this) {
            retval.set(e.row(), e.column(), Math.pow(e.get(), i));
        }
        return retval;
    }

    public Matrix sliceCol(final int colstart, final int ncols) {
       final AGDenseMatrix retval = new AGDenseMatrix(this.numRows, ncols);
       System.arraycopy(this.data, colstart*this.numRows, retval.data, 0, ncols*this.numRows);
       
       return retval;
    }

    public Matrix ones() {
       Arrays.fill(data, 1.0);
       return this;
    }

    public AGDenseMatrix reshape(final int irow, final int icol) {
        if(this.numColumns !=0 && this.numRows !=0){
        if(irow*icol != data.length){
            throw new IllegalArgumentException("new total size of matrix is different");
        }
        else {
            numColumns = icol;
            numRows = irow;
            //do not need to move the data around.
        }
        }
        return this;
     }

    public DenseVector asVector() {
       return this.asVector(0,data.length - 1);
    }

    public DenseVector asVector(int istart, int iend) {
        
        if( istart < 0){
           istart = 0; 
        }
        int ncopy;
        if( iend <=0 || iend >= data.length){
            iend = data.length -1;
        }
        ncopy = iend - istart + 1;
        final double[] vecarray = new double[ncopy];
        System.arraycopy(data, istart, vecarray, 0, ncopy);
        return new DenseVector(vecarray, false);
        
        
    }
    public Vector asVector(int istart) {
        return this.asVector(istart, data.length -1);
    }


    /**
     * Return the determinant. This is done by using the LU factorization. 
     * @TODO Perhaps could be just directly calculated.
     * @see org.astrogrid.matrix.Matrix#det()
     */
    public double det() {
        if(!isSquare()){
            throw new IllegalArgumentException("matrix must be square for determinant");
        }
        double det;
        final int[] piv = new int[numRows];
        final double[] indata = new double[data.length];
        System.arraycopy(data, 0, indata, 0, data.length);
        final intW info = new intW(0);
        LAPACK.getInstance().dgetrf(numRows, numColumns, indata, Matrices.ld(numColumns), piv, info);
        
        if (info.val > 0)
            throw new MatrixSingularException();
        else if (info.val < 0)
            throw new IllegalArgumentException();
        det = 1;
         for (int i = 0; i < numColumns; i++) {
             if(i+1 == piv[i]){
             det *= indata[i + i*numColumns];   
             } else
             {
                 det *= -indata[i + i*numColumns];
             }
        }
        return det;
        
    }

    public double trace() {
        if (isSquare()) {
            double retval = 0;
            for(int i = 0 ; i < numColumns; i++){
                retval += this.get(i, i);
                
            }
            return retval;
        } else {

        
        throw new  UnsupportedOperationException("Matrix.trace() not only for square matrices");
        }
    }

    public Matrix inv() {
        if(!isSquare()){
            throw new IllegalArgumentException("matrix must be square for inversion");
        }
        final int[] piv = new int[numRows];
        final double[] indata = new double[data.length];
        System.arraycopy(data, 0, indata, 0, data.length);
        final intW info = new intW(0);
        LAPACK.getInstance().dgetrf(numRows, numColumns, indata, Matrices.ld(numColumns), piv, info);
        
        if (info.val > 0)
            throw new MatrixSingularException();
        else if (info.val < 0)
            throw new IllegalArgumentException();
       
        int nb = LAPACK.getInstance().ilaenv( 1, "DGETRI", " ", numRows, -1, -1, -1);
        double[] work = new double[nb*numRows];
        LAPACK.getInstance().dgetri(numRows, indata, numRows, piv, work, nb*numRows, info);
        if (info.val > 0)
            throw new MatrixSingularException();
        else if (info.val < 0)
            throw new IllegalArgumentException();
        
        
        return new AGDenseMatrix(numRows, numColumns, indata);
         
    }
    
    /**
     * C= A/B where the division is elementwise.
     * @param b
     * @param c
     * @return
     */
    public Matrix divide(final Matrix b, final Matrix c) {
        checkSize(b);
        checkSize(c);
        for (MatrixEntry me : c) {
            final int row = me.row(), col = me.column();
            me.set(this.get(row, col) / b.get(row, col));
        }
        return c;
    }

    /**
     * returns a row as a vector.
     * matlab syntax mat(idx, :)
     * @see org.astrogrid.matrix.Matrix#sliceRow(int)
     */
    public Vector sliceRow(final int idx) {
        return this.slicev(idx, true);
    }
    
    public Matrix add(double a) {
        for (MatrixEntry me : this) {
            me.set(me.get()+a);
        }
        return this;
    }
    /**
     * returns a row as a matrix.
     * matlab syntax mat(idx, :)
     * @see org.astrogrid.matrix.Matrix#sliceRowM(int)
     */
   public Matrix sliceRowM(int k) {
       return slicem(k, true);                  
    }
    
    
    public int insert(DenseMatrix m){
        return insert(m,0);
    }

    
    public int insert(DenseMatrix m, int position){
        if(m.numRows * m.numColumns + position > this.numRows*this.numColumns)
        {
            throw new IllegalArgumentException("cannot insert m - too big");
        }
        else {
            System.arraycopy(m.data, 0, this.data, position, m.numColumns*m.numRows);
            return position + m.numRows*m.numColumns;
        }
    }
    public int insert(DenseVector vector, int inpos) {
        if (vector.size + inpos > this.numColumns*this.numRows) {
            throw new IllegalArgumentException("cannot insert m - too big");
        } else {
            System.arraycopy(vector.getData(), 0, this.data, inpos, vector.size);
            return inpos + vector.size;
        }
        
    }

    public AGDenseMatrix append(Matrix mm) {
        
        AGDenseMatrix m = (AGDenseMatrix) mm; //IMPL dangerous cast if used outside this application.
        if(numColumns != 0 && numColumns != m.numColumns){
          throw new IllegalArgumentException("appended matrix must have same number of columns")  ;
        } 
        else {
        double arr[] = new double[numColumns*numRows + m.numColumns* m.numRows];
        System.arraycopy(data, 0, arr, 0, numColumns*numRows);
        System.arraycopy(m.data, 0, arr, numRows*numColumns, m.numColumns*m.numRows);
        numRows += m.numRows;
        data = arr;
        }
        return this;
    }

    public double asScalar() {
    
        if(numColumns == 1 && numRows == 1)
        {
            return data[0];
        }
        else {
            throw new IllegalStateException("cannot take scalar unless matix is 1x1");
        }
    
    }

    public AGDenseMatrix append(Vector mm) {
        DenseVector m = (DenseVector) mm; //IMPL dangerous cast
        if(!(numColumns == 1 || numColumns == 0) ){
            throw new IllegalArgumentException("matrix must have 1 (or 0) column")  ;
        } else  {
            double arr[] = new double[numRows + m.size()];
            System.arraycopy(data, 0, arr, 0, numRows);
            System.arraycopy(m.getData(), 0, arr, numRows*numColumns, m.size());
            numRows += m.size();
            if(numColumns == 0) numColumns = 1; //just in case the initial matrix was empty.
            data = arr;
                
        }
        return this;
    }
    public void appendCol(AGDenseMatrix r) {
        if (r.numRows() != numRows) {
            throw new IllegalArgumentException("matrices must have same number of rows")  ;
        } else {
            double arr[] = new double[numRows*(numColumns+r.numColumns())];
            System.arraycopy(data, 0, arr, 0, numColumns* numRows);
            System.arraycopy(r.data, 0, arr, numRows*numColumns, r.numColumns()*r.numRows());
            numColumns += r.numColumns();
            data = arr;
        }
        
        
    }


    public Matrix setColumn(int idx, Vector v) {
        this.setColumn(idx, new AGDenseMatrix(v));
        return this;
    }

    public Matrix setColumn(int idx, Matrix v) {
        if(v.numColumns() != 1) {
            throw new IllegalArgumentException("setting matrix must have 1 column");
        } else {
            if(v.numRows() != numRows){
                throw new IllegalArgumentException("setting matrix must have same number of rows");
            }
            else {
                for (int i = 0; i < v.numRows(); i++) {
                    this.set(i, idx, v.get(i, 0));
                }
            }
        }
        return this;
    }

    public AGDenseMatrix append(double d) {
        if(!(numColumns == 1 || numColumns == 0)){
            throw new IllegalArgumentException("matrix must have 1 column")  ;
        } else  {
            double arr[] = new double[numRows + 1];
            System.arraycopy(data, 0, arr, 0, numRows);
            arr[numRows] = d;
            numRows += 1;
            numColumns = 1;
            data = arr;
                
        }
        return this;
    }

    public Matrix setRow(int k, Matrix m) {
        
        if (m.numRows() == 1 && m.numColumns() == numColumns) {
            for (int i = 0; i < m.numColumns(); i++) {
                this.set(k, i, m.get(0, i));
                
            }
        } else {
            throw new IllegalArgumentException("The setting matrix is not of the correct dimensions");
        }
        return this;
     }

    public Matrix setRow(int k, Vector v) {
        if (v.size() == numColumns) {
          for (int i = 0; i < v.size(); i++) {
            this.set(k, i, v.get(i));
        }   
        } else {
            throw new IllegalArgumentException("The setting matrix is not of the correct dimensions");
        }
       return this;
    }


    public AGDenseMatrix selectCols(int cols[], int ncols){
    	AGDenseMatrix retval = null;
    	if (ncols <= cols.length && ncols <= numColumns) {
			retval = new AGDenseMatrix(this.numRows, ncols);
			for (int i = 0; i < ncols; i++) {
				int icol = cols[i];
				System.arraycopy(this.data, icol*this.numRows, retval.data, i*this.numRows, this.numRows);
			}
		} else {
           throw new IllegalArgumentException("To many columns specified");
		}
		return retval;
    	
    	
    }
}

/*
 * $Log: AGDenseMatrix.java,v $
 * Revision 1.5  2009/09/22 07:04:16  pah
 * daily checkin
 *
 * Revision 1.4  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 * Revision 1.3  2009/09/14 19:08:43  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.2  2009/09/08 19:24:02  pah
 * further slicing possibilites
 *
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
