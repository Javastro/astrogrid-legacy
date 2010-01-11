/*
 * $Id: ClusterErr.java,v 1.5 2010/01/11 21:22:46 pah Exp $
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

import org.astrogrid.cluster.cluster.ClusterMStep.Retval;
import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;

public class ClusterErr {
	   public static class ClusterErrResult {
	        public final AGDenseMatrix q;
	        public final Vector p;
	        public final AGDenseMatrix mu, cv, lmu, lcv;
	        public List<Double> loglik;
	        public ClusterErrResult(AGDenseMatrix q, Vector p, AGDenseMatrix mu, AGDenseMatrix cv, AGDenseMatrix lmu, AGDenseMatrix lcv, List<Double> loglik ) {
	            this.q = q;
	            this.p = p;
	            this.mu = mu;
	            this.cv = cv;
	            this.lmu = lmu;
	            this.lcv = lcv;
	            this.loglik = loglik;
	        }
	    }

    
//    function [q, p, mu, cv, lmu, lcv, loglik] 
     public static ClusterErrResult cluster_err(Matrix alldata, Matrix datatype, int K, 
            int niters, double tol, CovarianceKind cv_type){
        // data      -- data with measurement errors
        // vartype   -- variable type
        // niters    -- the number of iterations
        // tol       -- termination criterion
        int ndata  = alldata.numRows();
        int ndimall = alldata.numColumns();
        // DATATYPE, where
        //   DATATYPE = 1:   continous variable without measurement errors
        //   DATATYPE = 2:   continuous variable with measurement errors
        //   DATATYPE = 3:   binary data
        //   DATATYPE = 4:   categorial data
        //   DATATYPE = 5:   integer data
        //   DATATYPE = 6:   measurement errors
        //   vartype = [1 c_dim; 2 e_dim; 3 b_dim; 4 m_dim; 5 i_dim; 6 err_dim];

        // datatype is a 6x2 matrix, containing the combined data information;
        int no_of_data_types = datatype.numRows();
        int n0 = 0, n1 = 0, d = 0;
        int ndim_nr = 0, ndim_er= 0, ndim_bin = 0, ndim_mul = 0 , ndim_int = 0,
        ndim_error = 0;
        Matrix data_nr = null, data_er = null, data_bin = null, data_int = null,  data_mul = null;
 // not used       AGDenseMatrix datadim = new AGDenseMatrix(0,0);
        Matrix S;
        for (int i = 0 ; i <no_of_data_types; i++){
            if (datatype.get(i,0) == 1 ){    // continuous data without errors
                ndim_nr = (int) datatype.get(i,1);
                data_nr = alldata.sliceCol(d, ndim_nr);
//                datadim = datadim.append(data_nr);
                d = d + ndim_nr;
            }
            else if (datatype.get(i,0) == 2) { // continous data with errors
                ndim_er = (int) datatype.get(i,1);
                data_er = alldata.sliceCol(d, ndim_er);
//                datadim = datadim.append(ndim_er);
                d = d + ndim_er;
            }
            else if (datatype.get(i,0) == 3){ // binary 
                ndim_bin = (int) datatype.get(i,1);
                data_bin = alldata.sliceCol(d, ndim_bin);
//               datadim = datadim.append(ndim_bin);
                d = d  + ndim_bin;
            }
            else if (datatype.get(i,0) == 4) { // multinomial
                ndim_mul = (int) datatype.get(i,1);
                data_mul = alldata.sliceCol(d, ndim_mul);
//                datadim = datadim.append(ndim_mul);
                d = d + ndim_mul;
            }
            else if (datatype.get(i,0) == 5) {   // integer
                ndim_int = (int) datatype.get(i,1);       
                data_int = alldata.sliceCol(d, ndim_int);
//               datadim = datadim.append(ndim_int);
                d= d + ndim_int;
            }
            else if (datatype.get(i,0) == 6 ) {  // error 
                ndim_error = (int) datatype.get(i,1);
                if (ndim_error != ndim_er){
                    throw new IllegalArgumentException( "The dimension of measurement errors and ");
                }        
                S = alldata.sliceCol(d, ndim_error);
                // error inforamtion
                S = add(S, ones(ndata, ndim_er).scale(1.0e-6));
                d = d + ndim_error;
            }
     }

        // initialization
        AGDenseMatrix mu = null, cv = null, lmu =null;
        AGDenseMatrix lcv = new AGDenseMatrix(0,0);
        if (ndim_er != 0 ){
            // initialize the global parameters for the model
            Matrix gmu = centre_kmeans(data_er, K, ndim_er);
            Vector ini_cov = diag(cov(data_er));
            switch (cv_type){
                case free:
                	lcv.append(new AGDenseMatrix(diag(ini_cov), K, 1).asVector());
                    break;
                case diagonal:
                	lcv.append( new AGDenseMatrix(ini_cov,K,1).asVector());
                    break;
                case common:
                	lcv.append(ones(K,1).asVector().scale(max(ini_cov)));
                    break;
            }
            lmu =  new AGDenseMatrix(gmu.asVector());
        }
        if (ndim_nr != 0){
            Matrix gmu_nr = centre_kmeans(data_nr, K, ndim_nr);    
            Vector ini_cov_nr = diag(cov(data_nr));
            switch (cv_type){
                case free:
                	cv = new AGDenseMatrix(diag(ini_cov_nr).asVector(), K, 1);
                    break;
                case diagonal:
                	cv = new AGDenseMatrix(ini_cov_nr, K, 1);
                    break;
                case common:
                	 cv = (AGDenseMatrix) ones(K,1).scale(max(ini_cov_nr));
                    break;
            }
            mu = new AGDenseMatrix(gmu_nr.asVector());
        }
        if (ndim_bin != 0){
            Matrix bp = rand(K, ndim_bin);
        //     bp = ones(K, ndim_bin)/2;
            mu.append(bp.asVector());
        }
        if (ndim_mul != 0){
        //     mp = ones(K, ndim_mul)/K;
            Matrix mp = dirichlet_sample(ones(1,ndim_mul).asVector(),K);
            mu.append(mp.asVector());
        }
        if (ndim_int != 0){
        //     ip = rand(K, ndim_int)*max(max(data_int));
            Matrix ip = rand(K, ndim_int);    
            mu.append(ip.asVector());
        }


        // initialize the class prior distribution
        Vector p  = ones(1,K).asVector().scale(1.0/K);

        // for iter = 1:niters
        boolean stop = false;
        int iter = 0;
        java.util.List<Double> loglik = new ArrayList<Double>();
        AGDenseMatrix q = null;
        while (!stop){
            //----------------------------------------------------------------------
            //   E-step
            //----------------------------------------------------------------------
              q = ClusterEStep.clustering_e_step(alldata, datatype, K, mu, cv, lmu, lcv, p, 
                cv_type);
            //----------------------------------------------------------------------
            //   M-step
            //----------------------------------------------------------------------
             Retval mretval = ClusterMStep.clustering_m_step(alldata, datatype, K, q, lcv, 
                cv_type);
             mu = mretval.mu;
             cv = mretval.cv;
             lmu = mretval.lmu;
             lcv = mretval.lcv;
             p = mretval.p;
             
            //----------------------------------------------------------------------
            //   bound
            //----------------------------------------------------------------------
            double loglike = ClusterBound.clustering_bound(alldata, datatype, K, mu, cv, lmu, lcv, p,
                q, cv_type);

            System.out.printf("In generation %d, the log likelihood bound is %f\n", iter, loglike);
            
            loglik.add(loglike);
            if (iter > 0 && abs((loglik.get(iter)-loglik.get(iter-1))/loglik.get(iter-1))<tol){
                stop = true;
            }
                 if( iter >= niters){
                stop = true;
            }
            iter = iter + 1;    
        }
        
        
        return new ClusterErrResult(q, p, mu, cv, lmu, lcv, loglik);
   
    }
}


/*
 * $Log: ClusterErr.java,v $
 * Revision 1.5  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 * Revision 1.4  2010/01/05 21:27:12  pah
 * basic clustering translation complete
 *
 * Revision 1.3  2009/09/16 16:53:06  pah
 * daily edit
 *
 * Revision 1.2  2009/09/14 19:08:43  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
