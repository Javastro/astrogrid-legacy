/*
 * $Id: MatrixUtils.java,v 1.5 2010/01/11 21:22:46 pah Exp $
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

    public static AGDenseMatrix zeros(int n) {
        return (AGDenseMatrix) new AGDenseMatrix(n, n).zero();
    }
    public static AGDenseMatrix zeros(int n, int m) {
        return (AGDenseMatrix) new AGDenseMatrix(n, m).zero();
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

    
    public static Vector diag(no.uib.cipr.matrix.Matrix x){
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
    public static Matrix times(no.uib.cipr.matrix.Matrix  a, no.uib.cipr.matrix.Matrix b){
       
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
     * Elementwise product of vectors and double
     * @param a
     * @param b
     * @return
     */
    public static Vector times(Vector  a, double b){
       
         
         DenseVector retval = new DenseVector(a);
         
         return retval.scale(b);
     }
    /**
     * Elementwise product of vectors and double
     * @param a
     * @param b
     * @return
     */
    public static Vector times( double b, Vector  a){
       
         
         DenseVector retval = new DenseVector(a);
         
         return retval.scale(b);
     }
    
    public static Matrix times(double b, Matrix a){
        AGDenseMatrix retval = new AGDenseMatrix(a);
        return (Matrix) retval.scale(b);
    }
    public static Matrix times(Matrix a,double b){
        AGDenseMatrix retval = new AGDenseMatrix(a);
        return (Matrix) retval.scale(b);
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
        return ((Matrix)a).inv();
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
     * C = AB
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix mult(no.uib.cipr.matrix.Matrix a, Vector b){
        AGDenseMatrix c = new AGDenseMatrix(a.numRows(), 1);
        a.mult(new AGDenseMatrix(b), c);
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
    public static Matrix multATBA(DenseVector v, no.uib.cipr.matrix.Matrix b)
    {
        Matrix a = new AGDenseMatrix(new double[][]{v.getData()}); //this transposes the vector to be a row vector
        Matrix c = new AGDenseMatrix(1, b.numColumns());
        a.mult(b, c);
        Matrix d = new AGDenseMatrix(1,1);
        c.transBmult(a, d);
        return d;
    }
    /**
     * a*b - treating a as a column vector.
     * @param a
     * @param b
     * @return
     */
    public static Matrix mult(Vector a, no.uib.cipr.matrix.Matrix  b){
        Matrix c = new AGDenseMatrix(a);
        Matrix d = new AGDenseMatrix(a.size(),b.numColumns());
        return (Matrix) c.mult(b, d);
    }
    /**
     * C = A<sup>T</sup>B. Make the vector  row vector before multiplying.
     * @param a
     * @param b
     * @return
     */
    public static AGDenseMatrix multAt(Vector v, no.uib.cipr.matrix.Matrix b) {
        Matrix a = new AGDenseMatrix(v);
        Matrix c = new AGDenseMatrix(a.numColumns(), b.numColumns());
        return (AGDenseMatrix) a.transAmult(b, c);
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
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, v.get(i)+d);
        }
        return retval;
    }
    
    public static Vector add(double d, Vector v ){
        return add(v,d);
    } 
    
    public static Matrix add(Matrix q, double eps) {
        AGDenseMatrix retval = new AGDenseMatrix(q.numRows(), q.numColumns());
        for (MatrixEntry matrixEntry : q) {
            retval.set(matrixEntry.row(), matrixEntry.column(), matrixEntry.get()+eps);
        }
        return retval;
    }
    public static Matrix add(double eps, Matrix q) {
        return add(q,eps);
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
   public static Matrix sub(no.uib.cipr.matrix.Matrix a, double b){
       Matrix c = new AGDenseMatrix(a);
       
       for (MatrixEntry matrixEntry : c) {
        matrixEntry.set(matrixEntry.get()-b);
    }
       return c;
       
   }
   
   /**
    * R= d-v = elementwise subtraction.
 * @param d
 * @param v
 * @return
 */
public static Vector sub(double d, Vector v){
      Vector retval = new DenseVector(v.size());
       for (int i = 0; i < v.size(); i++) {
           retval.set(i, d-v.get(i));
       }
       return retval;
   }
public static Vector sub( Vector v,double d){
    Vector retval = new DenseVector(v.size());
     for (int i = 0; i < v.size(); i++) {
         retval.set(i, v.get(i)-d);
     }
     return retval;
 }
    
    /**
     * produces square diagonal matrix using val.
     * @param ndim
     * @param val - the value to put on the diagonal.
     * @return
     */
    public static Matrix eye(int ndim, double val){
       Matrix retval = new AGDenseMatrix(ndim,ndim);
       retval.zero();
       for (int i = 0; i < ndim; i++) {
        retval.set(i, i, val);
    }
       return retval;
    }
    /**
     * produces square identity matrix.
     * @param ndim - the dimension of the matrix
     * @return
     */
    public static Matrix eye(int ndim){
       return eye(ndim,1.0);
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
        Matrix retval = new AGDenseMatrix(m.numRows(), m.numColumns());
        for (MatrixEntry matrixEntry : m) {
            retval.set(matrixEntry.row(),matrixEntry.column(), Math.log(matrixEntry.get()));
        }
        return retval;
    }
    public static Matrix exp(Matrix m) {
        Matrix retval = new AGDenseMatrix(m.numRows(), m.numColumns());
        for (MatrixEntry matrixEntry : m) {
            retval.set(matrixEntry.row(),matrixEntry.column(), Math.exp(matrixEntry.get()));
        }
        return retval;
    }
    
    public static Vector exp(Vector v){
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, Math.exp(v.get(i)));
        }
        return retval;
        
    }  public static Vector log(Vector v){
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, Math.log(v.get(i)));
        }
        return retval;
        
    }
    /**
     * raise each member to the power.
     * @param m
     * @param exp
     * @return
     */
    public static Matrix pow(Matrix m, double exp){
        Matrix retval = new AGDenseMatrix(m.numRows(), m.numColumns());
        for (MatrixEntry matrixEntry : m) {
            retval.set(matrixEntry.row(),matrixEntry.column(), Math.pow(matrixEntry.get(), exp));
        }
        return retval;
    }
    public static Vector pow(Vector v, double exp) {
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, Math.pow(v.get(i),exp));
        }
        return retval;
    }

    
    public static Matrix recip(Matrix m){
        Matrix retval = new AGDenseMatrix(m.numRows(), m.numColumns());
        for (MatrixEntry matrixEntry : m) {
            retval.set(matrixEntry.row(),matrixEntry.column(), 1.0/matrixEntry.get());
        }
        return retval;
       
    }
    public static Vector recip(Vector v) {
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, 1.0/v.get(i));
        }
        return retval;
    }

    /**
     * Form the elementwise a/m<sub>ij</sub>.
     * @param m
     * @param a
     * @return
     */
    public static Matrix recip( double a, Matrix m){
        Matrix retval = new AGDenseMatrix(m.numRows(), m.numColumns());
        for (MatrixEntry matrixEntry : m) {
            retval.set(matrixEntry.row(),matrixEntry.column(), a/matrixEntry.get());
        }
        return retval;
       
    }
    /**
     * Form the elementwise a/v<sub>i</sub>.
     * @param m
     * @param a
     * @return
     */
   public static Vector recip(double a, Vector v) {
        Vector retval = new DenseVector(v.size());
        for (int i = 0; i < v.size(); i++) {
            retval.set(i, a/v.get(i));
        }
        return retval;
    }

    
    /**
     * produces a matrix which is the sum of the squares of each row.
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
    
    
    /**
     * Forms the vector product of the two vectors -i.e. ab<sup>T</sup>.
     * @param a
     * @param b
     * @return
     */
    public static Matrix vprod(Vector a, Vector b){
        return multBt(new AGDenseMatrix(a), new AGDenseMatrix(b));
    }
    
    /**
     * Forms the vector product of the two vectors -i.e. aa<sup>T</sup>.
     * @param a
     * @param b
     * @return
     */
    public static Matrix vprod(Vector a){
        return multBt(new AGDenseMatrix(a), new AGDenseMatrix(a));
    }
   
    
    public static DenseVector delElement(DenseVector v, int i){
        if(i>= v.size() || i < 0){
            throw new IllegalArgumentException("element to delete - out of range");
        }
        int size = v.size();
        double[] newarr= new double[size-1];
        
        if(i != 0){
                // copy first half
            System.arraycopy(v.getData(), 0, newarr, 0, i);
        }
        //copy second half
        if(i < size -1){
        System.arraycopy(v.getData(), i+1, newarr, i, size -i-1);
        }
        return new DenseVector(newarr, false);
        
    }
    
    public static int min(Vector v){
        int midx = 0, idx=0;
        double val = v.get(idx);
        while(++idx < v.size()){
            if(v.get(idx) < val){
                midx = idx;
                val = v.get(idx);
            }
        }
        return midx;
    }

}




/*
 * $Log: MatrixUtils.java,v $
 * Revision 1.5  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 * Revision 1.4  2010/01/05 21:27:57  pah
 * add delete colum/row functions
 *
 * Revision 1.3  2009/09/22 07:04:16  pah
 * daily checkin
 *
 * Revision 1.2  2009/09/14 19:08:43  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
