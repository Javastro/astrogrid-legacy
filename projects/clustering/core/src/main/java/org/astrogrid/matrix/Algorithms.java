/*
 * $Id: Algorithms.java,v 1.3 2009/09/20 17:18:01 pah Exp $
 * 
 * Created on 8 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.matrix;

import static org.astrogrid.matrix.MatrixUtils.*;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.math.special.Gamma;
import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseCholesky;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;

/**
 * Some general matrix based algorithms.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 8 Dec 2008
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public class Algorithms {

    public static final double eps= 2.2204e-16 ; //FIXME what is eps
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(Algorithms.class);
    
    /** Comment for <code>kmeans</code>
     * %KMEANS  Trains a k means cluster model.

       Description
        CENTRES = KMEANS(CENTRES, DATA, OPTIONS) uses the batch K-means
       algorithm to set the centres of a cluster model. The matrix DATA
       represents the data which is being clustered, with each row
       corresponding to a vector. The sum of squares error function is used.
       The point at which a local minimum is achieved is returned as
       CENTRES.  The error value at that point is returned in OPTIONS(8).

       [CENTRES, OPTIONS, POST, ERRLOG] = KMEANS(CENTRES, DATA, OPTIONS)
       also returns the cluster number (in a one-of-N encoding) for each
       data point in POST and a log of the error values after each cycle in
       ERRLOG.    The optional parameters have the following
       interpretations.

       OPTIONS(1) is set to 1 to display error values; also logs error
       values in the return argument ERRLOG. If OPTIONS(1) is set to 0, then
       only warning messages are displayed.  If OPTIONS(1) is -1, then
       nothing is displayed.

       OPTIONS(2) absprec is a measure of the absolute precision required for the
       value of CENTRES at the solution.  If the absolute difference between
       the values of CENTRES between two successive steps is less than
       OPTIONS(2), then this condition is satisfied.

       OPTIONS(3) errprec is a measure of the precision required of the error
       function at the solution.  If the absolute difference between the
       error functions between two successive steps is less than OPTIONS(3),
       then this condition is satisfied. Both this and the previous
       condition must be satisfied for termination.

       OPTIONS(14) is the maximum number of iterations; default 100.

     */
    public static void kmeans(Matrix centres, Matrix data, int niters, double absprec, double errprec, boolean fromData, boolean verbose) {
 
        int ndata = data.numRows(), data_dim = data.numColumns();
        int ncentres = centres.numRows(), dim= centres.numColumns();
        double sumsq;

        if (dim != data_dim)
        {
          logger.error("Data dimension does not match dimension of centres");
          return; //TODO need to make this into some sort of exception.
        }

        if (ncentres > ndata){
          logger.error("More centres than data");
          return;
        }

 
        int store = 1;
       
         Matrix errlog = (Matrix) new AGDenseMatrix(1, niters).zero();
        

        // Check if centres and posteriors need to be initialised from data
        if (fromData){
          // Do the initialisation
          int[] perm = randperm(ndata-1);

          // Assign first ncentres (permuted) data points as centres
          for (int i = 0; i < ncentres; i++) {
            for(int j = 0; j < data_dim; j++)
            {
              centres.set(i, j, data.get(perm[i], j));   
            }
            }
        }

        // Main loop of algorithm
        Matrix old_centres = new AGDenseMatrix(ncentres, dim);
        double e = 0, old_e = 0;

        for(int n = 0; n < niters; n++){

          // Save old centres to check for termination 
          old_centres.set(centres);
          
          // Calculate posteriors based on existing centres
          Matrix d2 = dist2(data, centres);
          // Assign each point to nearest centre
          MvRet mv = minvals(d2);
          

          // Adjust the centres based on new posteriors          
          double[][] sums = new double[data_dim][ncentres];
          int[] num_points = new int[ncentres];
          e = 0;
          for(int i = 0; i < ndata; i++)
          {
              for (int j = 0; j < data_dim; j++){
                  sums[j][mv.index[i]] += data.get(i, j);
              }
              num_points[mv.index[i]]++;
              e += mv.minvals[i];
          }
          for (int j = 0; j < ncentres; j++){
              if (num_points[j] > 0){
                  for (int i=0; i < data_dim; i++){
                    centres.set(j,i, sums[i][j]/num_points[j]);
                  }
              }
            }

          // Error value is total squared distance from cluster centres
//          if (store){
//            errlog(n) = e;
//          }
          if (verbose){
            System.out.printf( "Cycle %4d  Error %11.6f\n", n, e);
          }

          if ( n > 1 ){
            // Test for termination
            if (old_centres.add(-1.0, centres).norm(Matrix.Norm.Maxvalue) < absprec && 
                Math.abs(old_e - e) < errprec){
              sumsq = e;
              return;
            }
          }
          old_e = e;
         }

        // If we get here, then we haven't terminated in the given number of 
        // iterations.
        sumsq = e;
        if (verbose){
          logger.warn("Warning: Maximum number of iterations has been exceeded");
        }


        
    }
    
    
    public static Matrix centre_kmeans(Matrix data, int nclus, int ndim){
        
        
        Matrix centres= (Matrix) new AGDenseMatrix(nclus, ndim).zero();
        kmeans(centres, data, 15, 0.001, 0.001, true, true);
        return centres;
        
    }
    
    private static class MvRet {
        int[] index;
        double[] minvals;
    }
    private static  MvRet minvals(Matrix d) {
     
        MvRet retval = new MvRet();
        retval.index = new int[d.numRows()];
        retval.minvals = new double[d.numRows()];
        for (int i = 0; i < d.numRows(); i++) {
            int icolmin = 0;
            retval.minvals[i] = d.get(i, 0);
            for (int j = 1; j < d.numColumns(); j++){
                double val = d.get(i, j);
                if(retval.minvals[i] > val){
                    retval.minvals[i] = val;
                    icolmin = j;
                }
                
            }
            retval.index[i] = icolmin;
            
        }
        return retval;
    }
    
    /**
     * %DIST2   Calculates squared distance between two sets of points.

*       Description
*       D = DIST2(X, C) takes two matrices of vectors and calculates the
*       squared Euclidean distance between them.  Both matrices must be of
*       the same column dimension.  If X has M rows and N columns, and C has
*       L rows and N columns, then the result has M rows and L columns.  The
*       I, Jth entry is the  squared distance from the Ith row of X to the
*       Jth row of C.
*

     * @param data
     * @param centres
     * @return
     */
    static public Matrix dist2(Matrix x, Matrix c) {
        int ndata= x.numRows(), dimx = x.numColumns() ;
        int ncentres = c.numRows(), dimc = c.numColumns();
        assert dimx == dimc:
                "Data dimension does not match dimension of centres";
        Matrix y = AGDenseMatrix.repeatColumn(x.pow(2).sum(2),ncentres);
        Matrix d = AGDenseMatrix.repeatRow(c.pow(2).sum(2), ndata);
        Matrix result = new AGDenseMatrix(ndata,ncentres);
        
        x.transBmult(-2.0, c, result);
        result.add(y.add(d));
        
        // Rounding errors occasionally cause negative entries in n2
        for (MatrixEntry e : result) {
            if(e.get() < 0){
                e.set(0.0);
            }
        }
        return result;
    }
    
    
    /*
     * slternative implentation.
     *    static Matrix dist2(Matrix x, Matrix c){
        
        int ndata = x.numRows(), dimx =x.numColumns();
        int ncentres = c.numRows(), dimc = c.numColumns();


        if (x.numColumns() == c.numColumns()) {
            Matrix retval = new AGDenseMatrix(ndata, ncentres);
            Matrix ret2 = (Matrix) mult(sumsq(c), ones(1,ndata)).transpose(retval);
            retval.add(mult(sumsq(x),ones(1, ncentres)));
            retval.add(multBt(x, c).scale(-2.0));
            return retval;
            
            
        } else {
            throw new IllegalArgumentException("each matrix must have he same number of columns");

        }
    }

     */

    /**
     * RANDPERM Random permutation.
      RANDPERM(n) is a random permutation of the integers from 1 to n.
      For example, RANDPERM(6) might be [2 4 5 6 1 3].
     * @param n
     * @return
     */
    public static int[] randperm(int n){
      SortedMap<Double, Integer> sortmap = new TreeMap<Double, Integer>();
      for (int i = 0; i < n; i++) {
        sortmap.put(Math.random(), i+1);
    }
      
      int[] retval = new int[n];
      int i=0;
      for (Integer ii : sortmap.values()) {
        retval[i++] = ii;
    }
      return retval;
       
    }
    
    
    static public Matrix dist3_common(Matrix x, Matrix centres, Vector covars){
     
        int ndata = x.numRows(), ndim = x.numColumns();
        int K = centres.numRows(), T =centres.numColumns();
        Matrix n2;
        // Calculate squared norm matrix, of dimension (ndata, ncentres)
        n2 = divide(dist2(x, centres) , repmatt(covars, ndata, 1));
        return n2;

    }
 
    static public Matrix dist3_diag(Matrix x, Matrix centres, Matrix covars){
        int ndata = x.numRows(), ndim = x.numColumns();
        int K = centres.numRows(), T =centres.numColumns();
        Matrix n2 = new AGDenseMatrix(ndata, K);
        for (int i = 0; i < K ;i++){
        Matrix diffs = sub(x , mult(ones(ndata, 1) , centres.sliceRowM(i)));
        Vector sums = sum(divide(pow(diffs,2.0),(mult(ones(ndata, 1) , 
                covars.sliceRowM(i)))), 2);
        for (int j = 0; j < ndata ; j++)
        n2.set(j, i, sums.get(j) );  
        }
       
        
        return n2;
    }
    static public Matrix dist3_free(Matrix x, Matrix centres, Matrix[] covars){
        
        int ndata = x.numRows(), ndim = x.numColumns();
        int K = centres.numRows(), T =centres.numColumns();
        Matrix n2 = new AGDenseMatrix(ndata, K);
         
                for (int i = 0 ;i<K; i++){
                    AGDenseMatrix diffs = (AGDenseMatrix) sub(x , mult(ones(ndata, 1) , centres.sliceRowM(i)));
                    // Use Cholesky decomposition of covariance matrix to speed computation
                    
                    DenseCholesky c = DenseCholesky.factorize(covars[i].transpose());
                   
                    DenseMatrix temp = c.solve(transpose(diffs));
                    for(int j = 0; j < ndata; j++){
                        double sum = 0;
                        for (int k =0 ; k < K; k++){
                            sum += temp.get(k,j)*temp.get(k,j);
                        }
                    n2.set(j,i, sum);
                    }
        //             n2(:,i) = sum(diffs 
                }
        return n2;
        }
    

    public static double gamma(double s){
      return  Math.exp(Gamma.logGamma(s));
    }
    
    public static Vector mean(Matrix m, int i){
        Vector retval = sum(m, i);
        if(i == 1){
            return retval.scale(1.0/m.numRows());
        }else {
            return retval.scale(1.0/m.numColumns());
        }
    }
    
    public static int rem(int a, int b){
        return a % b;
    }
}


/*
 * $Log: Algorithms.java,v $
 * Revision 1.3  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 * Revision 1.2  2009/09/14 19:08:42  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
