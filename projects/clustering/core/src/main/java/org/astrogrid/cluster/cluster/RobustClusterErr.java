/*
 * $Id: RobustClusterErr.java,v 1.1 2009/09/07 16:06:11 pah Exp $
 * 
 * Created on 27 Nov 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import java.util.ArrayList;
import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.cluster.cluster.ClusterEStepFull.Retval;
import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;

public class RobustClusterErr {

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(RobustClusterErr.class);
    
    //--------------------------------------------------------------------------
    // This function runs for robust clustering with measurement errors
    //--------------------------------------------------------------------------
    //function [q p mu cv lmu lcv O loglik] =
    
    public static class RobustClusterErrResult {
        public final AGDenseMatrix q;
        public final Vector p;
        public final AGDenseMatrix mu, cv, lmu, lcv;
        public final Vector O;
        public List<Double> loglik;
        public RobustClusterErrResult(AGDenseMatrix q, Vector p, AGDenseMatrix mu, AGDenseMatrix cv, AGDenseMatrix lmu, AGDenseMatrix lcv, Vector O, List<Double> loglik ) {
            this.q = q;
            this.p = p;
            this.mu = mu;
            this.cv = cv;
            this.lmu = lmu;
            this.lcv = lcv;
            this.O = O;
            this.loglik = loglik;
        }
    }
    static RobustClusterErrResult  robust_cluster_err(Matrix data, Matrix datatype, int K, int niters, double tol, CovarianceKind cv_type){
    // data      -- data with measurement errors
    // vartype   -- variable type
    // niters    -- the number of iterations
    // tol       -- termination criterion
    int ndata  = data.numRows();
    int ndimall = data.numColumns();
  

    // DATATYPE, where
    //   DATATYPE = 1:   continous variable without measurement errors
    //   DATATYPE = 2:   continuous variable with measurement errors
    //   DATATYPE = 3:   binary data
    //   DATATYPE = 4:   categorial data
    //   DATATYPE = 5:   integer data
    //   DATATYPE = 6:   measurement errors

    // datatype is a 6x2 matrix, containing the combined data information;
    // data(:,2) = 0 means no such variable type
    int no_of_data_types = datatype.numRows();
    Matrix data_nr = null, data_er = null, data_bin = null, data_int = null,  data_mul = null;
    int n0 = 0, ndim_er = 0, ndim_nr = 0, ndim_bin = 0, ndim_mul = 0 , ndim_int = 0, ndim_error = 0;
    for ( int i = 0; i < no_of_data_types; i++){
        if( datatype.get(i,0) == 1  ){   // continuous data without errors
             ndim_nr = (int) datatype.get(i,1);
            data_nr = data.sliceCol( n0,ndim_nr);
            n0  += ndim_nr;
        }
        else if( datatype.get(i,0) == 2){ // continous data with errors
            ndim_er = (int) datatype.get(i,1);
            data_er = data.sliceCol(n0, ndim_er);
            n0 = n0 + ndim_er;
            }
        else if ( datatype.get(i,1) == 3){
             ndim_bin =(int) datatype.get(i,1);
            data_bin = data.sliceCol(n0,ndim_bin);
            n0 = n0  + ndim_bin;
        }
        else if (datatype.get(i,0) == 4) {
             ndim_mul = (int)datatype.get(i,1);
            data_mul = data.sliceCol(n0,ndim_mul);
            n0 = n0 + ndim_mul;
        }
        else if (datatype.get(i,0) == 5) {
             ndim_int = (int)datatype.get(i,1);       
            data_int = data.sliceCol( n0,ndim_int);
            n0= n0 + ndim_int;
        }
        else if (datatype.get(i,0) == 6) {
             ndim_error = (int) datatype.get(i,1);
            if (ndim_error != ndim_er){
                logger.error ("The dimension of measurement errors and ");
            }
                    
//            S = data.slice(-1,-1, n0, n0+ndim_error -1);
//            // error inforamtion
//            S = S + 1.0e-6*ones(ndata, ndim_er);
            n0 = n0 + ndim_error;
        }
    }

    AGDenseMatrix mu = null, cv = null, lmu =null;
    AGDenseMatrix lcv = null;
    if (ndim_er != 0) { 
        // initialize the global parameters for the model
        Matrix gmu = centre_kmeans(data_er, K, ndim_er);
        
        Vector ini_cov = diag(cov(data_er));
        switch (cv_type) {
            case free:
                int lcvidx = 0;
                lcv = new AGDenseMatrix(diag(ini_cov), 1, K);
              break;
            case diagonal:
                lcv = new AGDenseMatrix(ini_cov,K,1);
                 break;
            case common:
                lcv = (AGDenseMatrix) ones(1,K).scale(max(ini_cov));
                break;
        }
        lmu = new AGDenseMatrix(gmu.asVector());
    }
    if (ndim_nr != 0){
       Matrix gmu_nr = centre_kmeans(data_nr, K, ndim_nr);    
    //     save robust_gmu_nr gmu_nr;
    //     load robust_gmu_nr;
        Vector ini_cov_nr = diag(cov(data_nr));
        switch (cv_type) {
            case free:
                 cv = new AGDenseMatrix(diag(ini_cov_nr).asVector(), 1, K);
                 break;
            case diagonal:
                cv = new AGDenseMatrix(ini_cov_nr, 1, K);
                break;
            case common:
                cv = (AGDenseMatrix) ones(1,K).scale(max(ini_cov_nr));
                break;
        }
        mu = new AGDenseMatrix(gmu_nr.asVector());
    }
    if (ndim_bin != 0){
        Matrix bp = rand(K, ndim_bin);
        mu.append( bp.asVector());
    }
    if (ndim_mul != 0){
        Matrix mp = dirichlet_sample(ones(1,ndim_mul).asVector(),K);
       mu.append( mp.asVector());
    }
    if (ndim_int != 0){
        Matrix ip = (Matrix) rand(K, ndim_int).scale(max(max(data_int)));
        mu.append(ip.asVector());
    }

    // initialize the class prior distribution
    Vector p  =  ones(1,K).asVector().scale(1.0/K);

    AGDenseMatrix v = null , latent = null, ab = null;
    // initialize parameters of the q-distribution
   if (ndim_nr != 0 || ndim_er != 0){
        v  = (AGDenseMatrix) ones(K,1).scale(1.0e-1);      // the degree of freedom of w
        latent = v;
        ab  = (AGDenseMatrix) ones(2*ndata*K, 1);
    }

    // disp 'In robust_cluster_err'
    // mu'
    // pause

    // for iter = 1:niters
    boolean stop = false;
    int iter = 1;
    java.util.List<Double> loglik = new ArrayList<Double>();
    AGDenseMatrix q = null;
    AGDenseMatrix C = null;
    while( !stop){
        //----------------------------------------------------------------------
        //   E-step [output ab q C]
        //----------------------------------------------------------------------
         Retval estepresult = ClusterEStepFull.clustering_e_step_full(data, datatype, K, latent, ab, 
            mu, cv, lmu, lcv, p, cv_type);
         
         AGDenseMatrix output = estepresult.output;
         ab = estepresult.ab;
         q = estepresult.q;
         C = estepresult.C;
         
        // output includes qmu, qcv and q
        // ab includes a and b
        // latent includes v
        // mu     includes gmu_nr, gmu, bp, mp, ip
        //   cv   includes gcv, gcv_nr

        //----------------------------------------------------------------------
        //   M-step [latent mu cv lmu lcv p]
        //----------------------------------------------------------------------
         org.astrogrid.cluster.cluster.ClusterMStepFull.Retval mstepresult = ClusterMStepFull.cluster_m_step_full(data, datatype, 
            K, output, latent, ab, q, lcv, cv_type);
         latent = mstepresult.latent;
         
        //----------------------------------------------------------------------
        //   bound
        //----------------------------------------------------------------------
        double loglike = ClusterBoundFull.cluster_bound_full(data, datatype, K, mu, cv, lmu, lcv,
            p, output, latent, ab, q, cv_type);

        System.out.printf("In generation //d, the log likelihood bound is %f\n", iter, loglike);
        
        loglik.add(loglike);
        if (iter > 1 && abs((loglik.get(iter)-loglik.get(iter-1))/loglik.get(iter-1))<tol){
            stop = true;
        }
        if (iter >= niters){
            stop = true;
        }
        iter = iter + 1;
    }

    // O =[];
    // if C != []
        Vector O = sum(times(q,C), 2);
    // end
      return new RobustClusterErrResult(q, p, mu, cv, lmu, lcv, O, loglik);

    }

    
}


/*
 * $Log: RobustClusterErr.java,v $
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
