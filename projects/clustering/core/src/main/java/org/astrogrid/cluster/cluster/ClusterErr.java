/*
 * $Id: ClusterErr.java,v 1.1 2009/09/07 16:06:11 pah Exp $
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

import no.uib.cipr.matrix.AGDenseMatrix;

import org.astrogrid.matrix.Matrix;

public class ClusterErr {

    
//    function [q, p, mu, cv, lmu, lcv, loglik] 
     void cluster_err(Matrix alldata, Matrix datatype, int K, 
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
        Matrix datadim = new AGDenseMatrix();
        for (int i = 0 ; i <no_of_data_types; i++){
            if (datatype.get(i,1) == 1 ){    // continuous data without errors
                ndim_nr = datatype.get(i,2);
                data_nr = alldata(:, d+1:d+ndim_nr);
                datadim = [datadim ndim_nr];
                d = d + ndim_nr;
            }
            else if (datatype.get(i,1) == 2) { // continous data with errors
                ndim_er = datatype.get(i,2);
                data_er = alldata(:,d+1:d+ndim_er);
                datadim = [datadim ndim_er];
                d = d + ndim_er;
            }
            else if (datatype.get(i,1) == 3){ // binary 
                ndim_bin = datatype.get(i,2);
                data_bin = alldata(:,d+1:d+ndim_bin);
                datadim = [datadim ndim_bin];
                d = d  + ndim_bin;
            }
            else if (datatype.get(i,1) == 4) { // multinomial
                ndim_mul = datatype.get(i,2);
                data_mul = alldata(:,d+1:d+ndim_mul);
                datadim  = [datadim ndim_mul];
                d = d + ndim_mul;
            }
            else if (datatype.get(i,1) == 5) {   // integer
                ndim_int = datatype.get(i,2);       
                data_int = alldata(:, d+1:d+ndim_int);
                datadim   = [datadim ndim_int];
                d= d + ndim_int;
            }
            else if (datatype.get(i,1) == 6 ) {  // error 
                ndim_error = datatype.get(i,2);
                if (ndim_error != ndim_er){
                    throw new IllegalArgumentException( "The dimension of measurement errors and ");
                }        
                S = alldata(:, d+1:d+ndim_error);
                % error inforamtion
                S = S + 1.0e-6*ones(ndata, ndim_er);
                d = d + ndim_error;
            }
     }

        // initialization
        mu = [];
        lmu = [];
        cv = [];
        lcv =[];
        if (ndim_er != 0 ){
            // initialize the global parameters for the model
            gmu = centre_kmeans(data_er, K, ndim_er);
            ini_cov = diag(cov(data_er))';
            switch (cv_type){
                case full:
                    for(int  k = 0;k < K; k++){
                        gcv(k,:,:) = diag(ini_cov);  
                        tmp = squeeze(gcv(k,:,:));
                        lcv = [lcv; tmp(:)];
                    }
                    break;
                case diag:
                    gcv = repmat(ini_cov, K, 1);
                    for (int k = 0; k <K; k++){
                        tmp  = gcv(k,:);
                        lcv = [lcv; tmp(:)];
                    }
                    break;
                case spherical:
                    gcv = max(ini_cov)*ones(1,K);
                    lcv = [lcv; gcv(:)];
            }
            lmu = [lmu; gmu(:)];
        }
        if (ndim_nr != 0){
            gmu_nr = centre_kmeans(data_nr, K, ndim_nr);    
            ini_cov_nr = diag(cov(data_nr))';
            switch (cv_type){
                case full:
                    for(int k = 0: k<K;k++){
                        gcv_nr(k,:,:) = diag(ini_cov_nr);
                        tmp = squeeze(gcv_nr(k,:,:));
                        cv=[cv; tmp(:)];
                    }
                case diag:
                    gcv_nr = repmat(ini_cov_nr, K, 1);
                    for (int k = 0:k < K; k+){
                        tmp = gcv_nr(k,:);
                        cv = [cv; tmp(:)];
                    }
                case spherical:
                    gcv_nr = max(ini_cov_nr)*ones(1,K);
                    cv =[cv; gcv_nr(:)];
            end
            mu = [mu; gmu_nr(:)];
        end
        if (ndim_bin != 0){
            bp = rand(K, ndim_bin);
        //     bp = ones(K, ndim_bin)/2;
            mu = [mu; bp(:)];
        }
        if (ndim_mul != 0){
        //     mp = ones(K, ndim_mul)/K;
            mp = dirichlet_sample(ones(1,ndim_mul),K);
            mu = [mu; mp(:)];
        }
        if (ndim_int != 0){
        //     ip = rand(K, ndim_int)*max(max(data_int));
            ip = rand(K, ndim_int);    
            mu = [mu; ip(:)];
        }


        // initialize the class prior distribution
        p  = ones(1,K)/K;

        // for iter = 1:niters
        stop = 0;
        iter = 1;
        loglik =[];

        while (!stop){
            //----------------------------------------------------------------------
            //   E-step
            //----------------------------------------------------------------------
            q = clustering_e_step(alldata, datatype, K, mu, cv, lmu, lcv, p, ...
                cv_type);
            //----------------------------------------------------------------------
            //   M-step
            //----------------------------------------------------------------------
            [mu cv lmu lcv p] = clustering_m_step(alldata, datatype, K, q, lcv, ...
                cv_type);
            //----------------------------------------------------------------------
            //   bound
            //----------------------------------------------------------------------
            loglike = clustering_bound(alldata, datatype, K, mu, cv, lmu, lcv, p,...
                q, cv_type);

            fprintf("In generation %d, the log likelihood bound is %f\n", iter, loglike);
            
            loglik = [loglik; loglike];
            if (iter > 1 & abs((loglik(iter)-loglik(iter-1))/loglik(iter-1))<tol){
                stop = true;
            }
            if( iter >= niters){
                stop = true;
            }
            iter = iter + 1;    
        }
   
    }
}


/*
 * $Log: ClusterErr.java,v $
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
