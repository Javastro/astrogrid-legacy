/*
 * $Id: Matrix.java,v 1.1 2009/09/07 16:06:12 pah Exp $
 * 
 * Created on 21 Nov 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.matrix;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

public interface Matrix extends no.uib.cipr.matrix.Matrix {
    /**
     * slice an array - indices are 0 based. Creates a new array storage - original data is untouched.
     * @param rowstart
     * @param rowend
     * @param colstart
     * @param colend
     * @return
     * @deprecated too complex - use {@link #slicev(int, boolean)} or {@link #sliceCol(int, int)} instead.
     */
    Matrix slice(int rowstart, int rowend, int colstart, int colend);
 
  
    /**
     * Return the sum of a matrix along a dimension.
     * @param dim the dimension along which to sum 1=sum the columns 2 = sum the rows
     * @return
     */
    Vector sum(int dim);

    /**
     * Return a matrix with elements raised to the power.
     * @param i
     * @return
     */
    Matrix pow(double i);

    /**
     * Return a single column or row of the matrix
     * @param idx the 0 based index of the row or column to return;
     * @param dorow if true return a row - if false return a column.
     * @return
     */
    public Vector slicev(int idx, boolean dorow);
    
    
    /**
     * Returns a Vector of the row.
     * matlab syntax mat(idx, :)
     * @param idx
     * @return
     */
    public Vector sliceRow(int idx);
    /**
     * Returns a matrix consisting of only the row.
     * matlab syntax mat(idx, :).
     * @param k
     * @return
     */
    public Matrix sliceRowM(int k);
   
    /**
     * Returns a new Matrix consisting of only the specified columns
     * @param colstart
     * @param ncols
     * @return
     */
    public Matrix sliceCol(int colstart, int ncols);
    
    /**
     * @return
     */
    public Matrix ones();
    
    
    /**
     * Reshapes the matrix to have the new size. Elements are copied columnwise.
     * @param irow
     * @return
     */
    public Matrix reshape(int irow, int icol);

    
    /**
     * return a copy of the data as a vector. The storage is assumed to be column major. This is similar to the A(:) 'linear' addressing in matlab.
     * @return
     */
    public Vector asVector();
    
    /**
     * return a copy of the data as a vector. The storage is assumed to be column major. This is similar to the A(:) 'linear' addressing in matlab.
     * @return
     */
    public Vector asVector(int istart, int idend);

    /**
     * return a copy of the data as a vector. The storage is assumed to be column major. a matrix treating as one large columnwise array. - Indices are 0 based.
     * @param istart
     * @return
     */
    public Vector asVector(int istart);
    
    /**
     * Return the determinant of the matrix.
     * @return
     */
    double det();
    
    /**
     * Produce the trace of the matrix.
     * @return
     */
    public double trace();
    
    /**
     * Computes the inverse of the matrix.
     * B = A^-1
     * @return
     */
    Matrix inv();

    /**
     * C= A/B where the division is elementwise.
     * @param b
     * @param c
     * @return
     */
    public Matrix divide(Matrix b, Matrix c);

    public Matrix add(double a);
    
    
    /**
     * Set a complete column in the matrix to the value given.
     * @param idx
     * @param v
     * @return
     */
    public Matrix setColumn(int idx, Vector v);
    /**
     * Set a complete column in the matrix to the value given. Note that the matrix must have only one column.
     * @param idx
     * @param v
     * @return
     */
    public Matrix setColumn(int idx, Matrix v);

    
    
    /**
     * Append the matrix as new rows onto the existing matrix.
     * @param m
     * @return
     */
    public AGDenseMatrix append(Matrix m);
 
    
    /**
     * Append the matrix as new rows onto the existing matrix.
     * @param m
     * @return
     */
    public AGDenseMatrix append(Vector m);

    
    public double asScalar();

    /**
     * append double value. 
     * @param d
     */
    public AGDenseMatrix append(double d);
}


/*
 * $Log: Matrix.java,v $
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
