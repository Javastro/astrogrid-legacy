/*
 * $Id: MatrixUtils.java,v 1.1 2009/09/07 16:06:12 pah Exp $
 * 
 * Created on 27 Nov 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.matrix;

import org.apache.commons.math.special.Gamma;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;

/**
 * Utility functions that operate on matrices. These are intended to operate in the same fashion as the similarly named MATLAB functions.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Aug 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class MatrixUtils {

    /**
     * Forms the covariance matrix. For matrices, where each row is an
     * observation, and each column a variable, COV(X) is the covariance matrix.
     * DIAG(COV(X)) is a vector of variances for each column, and
     * SQRT(DIAG(COV(X))) is a vector of standard deviations
     * 
     * COV(X) or COV(X,Y) normalizes by (N-1) if N>1, where N is the number of
     * observations. This makes COV(X) the best unbiased estimate of the
     * covariance matrix if the observations are from a normal distribution. For
     * N=1, COV normalizes by N.
     * 
     * compatible with MatLab definition of the same
     * 
     * also @see http://mathworld.wolfram.com/Covariance.html
     * 
     * @param x
     * @return
     */
    public static Matrix cov(Matrix x) {

        int m = x.numRows();
        int n = x.numColumns();

        if (m == 1) {
            return zeros(n);
        } else {
            // form mean of each column in y
            Vector y = sum(x, 1);
            y.scale(1.0 / m);
            Matrix result = new AGDenseMatrix(m, n);
            Matrix result2 = new AGDenseMatrix(m,n);
            for (MatrixEntry me : x) {
                int col = me.column();
                int row = me.row();
                double value = me.get() - y.get(col);
                result.set(row, col, value); // remove the
                                                                // mean from
                result2.set(row, col, value);                                                // each value

            }
            Matrix cov = new AGDenseMatrix(n,n);
            result.transAmult(1.0/(m-1), result2, cov);//m-1 is the most likely use in matlab - can be overwritten there though..
            
            return cov;
            
        }

    }

    public static Matrix zeros(int n) {
        return (Matrix) new AGDenseMatrix(n, n).zero();
    }
    public static Matrix zeros(int n, int m) {
        return (Matrix) new AGDenseMatrix(n, m).zero();
    }
    
    public static Matrix ones(int n) {
        return new AGDenseMatrix(n, n).ones();
    }
    public static Matrix ones(int n, int m) {
        return new AGDenseMatrix(n, m).ones();
    }

    public static Vector sum(Matrix x, int dim) {
        return x.sum(dim);
    }
    
    public static double sum(Vector v){
        double s = 0;
        for (VectorEntry vectorEntry : v) {
            s += vectorEntry.get();
        }
        return s;
    }
    
    public static Vector sum(Matrix m) {
        return sum(m,1);
    }

    
    public static Vector diag(Matrix x){
        if(x.isSquare()){
            Vector retval = new DenseVector(x.numColumns());
            for (int i = 0; i < retval.size(); i++) {
                retval.set(i, x.get(i, i));
            }
            return retval;
        } else {
            throw new IllegalArgumentException("the matrix must be square");
        }
    }
    
    public static Matrix diag(Vector v){
        Matrix retval = new AGDenseMatrix(v.size(), v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, i, v.get(i));
        }
        return retval;
    }
    
    public static Matrix repmatv(Vector v, int ni, int nj)
    {
        Matrix retval = new AGDenseMatrix(v, ni, nj);
        return retval;
    }
    public static Matrix repmat(Matrix v, int ni, int nj)
    {
        Matrix retval = new AGDenseMatrix(v, ni, nj);
        return retval;
    }
    
    /**
     * Repeat a vector into a matrix treating the vector as a column vector.
     * @param v
     * @param ni
     * @param nj
     * @return
     */
     public static Matrix repmat(Vector v, int ni, int nj)
    {
        Matrix retval = new AGDenseMatrix(v);
        return repmat(retval, ni, nj);
        
    }
    
    /**
     * Repeat a vector into a matrix treating the vector as a row vector.
     * @param v
     * @param ni
     * @param nj
     * @return
     */
    public static Matrix repmatt(Vector v, int ni, int nj)
    {
        Matrix retval = transpose(new AGDenseMatrix(v));
        return repmat(retval, ni, nj);
        
    }
     
    public static AGDenseMatrix reshape(Matrix m, int ni, int nj){
        AGDenseMatrix retval = new AGDenseMatrix(m);
        return retval.reshape(ni, nj);
    }
    
    public static AGDenseMatrix reshape(Vector v, int ni, int nj ){
        AGDenseMatrix retval = new AGDenseMatrix(v);
        return  retval.reshape(ni, nj);
    }
    
    public static double det(Matrix m){
        return m.det();
    }
    
    public static double trace(Matrix m){
        return m.trace();
    }
    
    /**
     * Elementwise divide of matrix.
     * @param a
     * @param b
     * @return
     */
    public static Matrix divide(Matrix  a, Matrix b){
       
         if(a.numRows() == b.numRows() && a.numColumns() == b.numColumns()){ 
         Matrix retval = new AGDenseMatrix(a.numRows(), a.numColumns());
         a.divide(b, retval);
         return retval;
         }
         else {
             throw new IllegalArgumentException("matrices are not same size");
         }
    }
    /**
     * Elementwise multiply of matrix.
     * @param a
     * @param b
     * @return
     */
    public static Matrix times(Matrix  a, Matrix b){
       
         if(a.numRows() == b.numRows() && a.numColumns() == b.numColumns()){ 
         Matrix retval = new AGDenseMatrix(a.numRows(), a.numColumns());
         for (int i = 0; i < retval.numRows(); i++) {
            for (int j = 0; j < retval.numColumns(); j++) {
                retval.set(i, j, a.get(i, j)*b.get(i, j));
            }
        }
         return retval;
         }
         else {
             throw new IllegalArgumentException("matrices are not same size");
         }
    }
    /**
     * Elementwise divide of vector.
     * @param a
     * @param b
     * @return
     */
    public static Vector divide(Vector  a, Vector b){
       
         if(a.size() == b.size() ){ 
         DenseVector retval = new DenseVector(a.size());
         for (int i = 0; i < retval.size(); i++) {
            retval.set(i, a.get(i)/b.get(i));
        }
         return retval;
         }
         else {
             throw new IllegalArgumentException("vectors are not same size");
         }
    }
    /**
     * Elementwise addition of vectors.
     * @param a
     * @param b
     * @return
     */
    public static Vector add(Vector  a, Vector b){
       
         if(a.size() == b.size() ){ 
         DenseVector retval = new DenseVector(a.size());
         for (int i = 0; i < retval.size(); i++) {
            retval.set(i, a.get(i)+b.get(i));
        }
         return retval;
         }
         else {
             throw new IllegalArgumentException("vectors are not same size");
         }
    }
    /**
     * Elementwise product of vectors.
     * @param a
     * @param b
     * @return
     */
    public static Vector times(Vector  a, Vector b){
       
         if(a.size() == b.size() ){ 
         DenseVector retval = new DenseVector(a.size());
         for (int i = 0; i < retval.size(); i++) {
            retval.set(i, a.get(i)*b.get(i));
        }
         return retval;
         }
         else {
             throw new IllegalArgumentException("vectors are not same size");
         }
    }
    /**
     * Elementwise difference of vectors.
     * @param a
     * @param b
     * @return
     */
    public static Vector sub(Vector  a, Vector b){
       
         if(a.size() == b.size() ){ 
         DenseVector retval = new DenseVector(a.size());
         for (int i = 0; i < retval.size(); i++) {
            retval.set(i, a.get(i)-b.get(i));
        }
         return retval;
         }
         else {
             throw new IllegalArgumentException("vectors are not same size");
         }
    }
    
    public static Matrix inv(Matrix a){
        return ((Matrix)a).inv();//IMPL this is the only actual extension to the cipr matrix.
    }
    
    /**
     * C = AB
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix mult(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b){
        AGDenseMatrix c = new AGDenseMatrix(a.numRows(), b.numColumns());
        a.mult(b, c);
        return c;
    }
    
    /**
     * C = AB<sup>T</sup>
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix multBt(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b) {
        Matrix c = new AGDenseMatrix(a.numRows(), b.numRows());
        
        return (AGDenseMatrix) a.transBmult(b, c);
    }
    
    /**
     * C = A<sup>T</sup>B
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix multAt(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b) {
        Matrix c = new AGDenseMatrix(a.numColumns(), b.numColumns());
        
        return (AGDenseMatrix) a.transAmult(b, c);
    }
    
    
    /**
     * C = A*B*A<sup>T</sup>
     * @param a
     * @param b
     * @return
     */
    public static Matrix multABAT(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b)
    {
        Matrix c = new AGDenseMatrix(a.numRows(), b.numColumns());
        a.mult(b, c);
        Matrix d = new AGDenseMatrix(a.numRows(), a.numRows());
        c.transBmult(a, d);
        return d;
    }
    /**
     * C = A<sup>T</sup>*B*A
     * @param a
     * @param b
     * @return
     */
    public static Matrix multATBA(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b)
    {
        Matrix c = new AGDenseMatrix(a.numColumns(), b.numColumns());
        a.transAmult(b, c);
        Matrix d = new AGDenseMatrix(a.numColumns(),a.numColumns());
        c.mult(a, d);
        return d;
    }
    public static Matrix mult(Vector a, no.uib.cipr.matrix.Matrix  b){
        Matrix c = new AGDenseMatrix(a, 1, a.size());
        Matrix d = new AGDenseMatrix(1,a.size());
        return (Matrix) c.mult(b, d);
    }
    public static Matrix multt(Vector a, no.uib.cipr.matrix.Matrix  b){
        Matrix c = new AGDenseMatrix(a, 1, a.size());
        Matrix d = new AGDenseMatrix(1,a.size());
        return (Matrix) c.transBmult(b, d);
    }
    
    /**
     * R = A+B. Elementwise addition of matrices - must be the same size.
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix add(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b){
        AGDenseMatrix c = new AGDenseMatrix(a);
        return   (AGDenseMatrix) c.add(b);
        
    }
    
    public static Vector add(Vector v, double d){
        for (VectorEntry vectorEntry : v) {
            vectorEntry.set(vectorEntry.get()+d); 
        }
        return v;
    }
    
    public static Matrix add(Matrix q, double eps) {
        for (MatrixEntry matrixEntry : q) {
            matrixEntry.set(matrixEntry.get()+eps);
        }
        return q;
    }

    
    /**
     * R = A-B. Elementwise subtraction of matrices - must be the same size.
     * @param a
     * @param b
     * @return
     */
   public static Matrix sub(no.uib.cipr.matrix.Matrix a, no.uib.cipr.matrix.Matrix b){
        Matrix c = new AGDenseMatrix(a);
        return (Matrix) c.add(-1.0, b);
        
    }
   
   /**
    * R= d-v = elementwise subtraction.
 * @param d
 * @param v
 * @return
 */
public static Vector sub(double d, Vector v){
       for (VectorEntry vectorEntry : v) {
        vectorEntry.set(d-vectorEntry.get());
    }
       return v;
   }
    
    /**
     * produces square identity matrix.
     * @param ndim
     * @return
     */
    public static Matrix eye(int ndim){
       Matrix retval = new AGDenseMatrix(ndim,ndim);
       retval.zero();
       for (int i = 0; i < ndim; i++) {
        retval.set(i, i, 1.0);
    }
       return retval;
    }
    
    
    public static AGDenseMatrix transpose(no.uib.cipr.matrix.Matrix a){
        Matrix retval = new AGDenseMatrix(a.numColumns(), a.numRows());
        return  (AGDenseMatrix) a.transpose(retval);
    }
    
    public static double max(Vector iv) {
       double retval = iv.get(0);
       for (int i = 1; i < iv.size(); i++) {
        if (iv.get(i) > retval) {
            retval = iv.get(i);
        }
    }
       return retval;
    }
 
    public static Vector max(Matrix im) {
        Vector retval = new DenseVector(im.numColumns());
        for (int i = 0; i < im.numColumns(); i++) {
            double maxval=im.get(0, i);
            for (int j = 1; j < im.numRows(); j++) {
                if (im.get(j,i) > maxval) {
                    maxval = im.get(j,i);
                }
        
            }
            retval.set(i, maxval);
     }
        return retval;
     }

    
    public static Matrix rand (int i, int j) {
        Matrix retval = new AGDenseMatrix(i, j);
        for (MatrixEntry matrixEntry : retval) {
            matrixEntry.set(Math.random());
        }
        return retval;
    }
    public static Matrix dirichlet_sample (Vector m, int j) {
        Matrix retval = new AGDenseMatrix(m.size(), j);
        for (MatrixEntry matrixEntry : retval) {
            matrixEntry.set(Math.random()); //FIXME - this is not acutually returning dirichlet sampled random numbers.
        }
        return retval;
    }
    
    
    public static Matrix psi(Matrix m) {
        
        for (MatrixEntry matrixEntry : m) {
            matrixEntry.set(Gamma.digamma(matrixEntry.get()));
        }
        return m;
    }
    
    public static double psi(double d){
        return Gamma.digamma(d);
    }
    
    public static Matrix log(Matrix m) {
        for (MatrixEntry matrixEntry : m) {
            matrixEntry.set(Math.log(matrixEntry.get()));
        }
        return m;
    }
    public static Matrix exp(Matrix m) {
        for (MatrixEntry matrixEntry : m) {
            matrixEntry.set(Math.exp(matrixEntry.get()));
        }
        return m;
    }
    
    public static Vector log(Vector v){
        for (VectorEntry vectorEntry : v) {
            vectorEntry.set(Math.log(vectorEntry.get()));
        }
        return v;
        
    }
    public static Matrix pow(Matrix m, double exp){
        for (MatrixEntry matrixEntry : m) {
            matrixEntry.set(Math.pow(matrixEntry.get(), exp));
        }
        return m;
    }
    public static Vector pow(Vector v, double exp) {
       for (VectorEntry vectorEntry : v) {
        vectorEntry.set(Math.pow(vectorEntry.get(), exp));
    }
       return v;
    }


    
    /**
     * produces a matrix wich is the sum of the squares of each row.
     * @param m
     * @return
     */
    public static Matrix sumsq(Matrix m)
    {
        Matrix retval = new AGDenseMatrix(m.numRows(),1);
        for (MatrixEntry matrixEntry : m) {
             Double val = matrixEntry.get();
             int row = matrixEntry.row();
             retval.set(row, 1, retval.get(row, 1) + val* val);
        }
        return retval;
    }
    
    
    /**
     * Return a vector filled with a sequence from 1 to n;
     * @param n
     * @return
     */
    public static Vector seq(int n){
        Vector retval = new DenseVector(n);
        int j = 1;
        for (VectorEntry vectorEntry : retval) {
            vectorEntry.set(j++);
        }
        return retval;
    }
}




/*
 * $Log: MatrixUtils.java,v $
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
