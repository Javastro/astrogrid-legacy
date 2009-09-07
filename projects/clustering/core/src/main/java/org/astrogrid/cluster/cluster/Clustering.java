/*
 * $Id: Clustering.java,v 1.1 2009/09/07 16:06:11 pah Exp $
 * 
 * Created on 26 Nov 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import org.astrogrid.matrix.Matrix;


public class Clustering {
// 1. clustering for data sets with mixed-type variables% 
// 3. clustering for real data sets with outliers
// 4. MML for determing the optimal number of clusters.
//    function [output errlog]=
      public static class Retval {
        public final Matrix output;
        public final String errlog;
        public Retval(Matrix output, String errlog) {
            this.output = output;
            this.errlog = errlog;
        }
    };
        
    public Retval  clustering(Vector coptions, Matrix data, Matrix output){
// first, read coptions, coptions gives the variable types, data associated
// and others
// OPTIONS(1)  --  Display parameter 
// OPTIONS(2)  --  Termination tolerance on F.(Default: 1e-4).
// OPTIONS(3)  --  Mixed-type Variables(?)1-yes,0-no (Default: 0)
// OPTIONS(4)  --  Measurement Errors(?)1-yes, 0-no (Default: 0)
// OPTIONS(5)  --  Outliers(?)1-yes, 0-no   (Default 0)
// OPTIONS(6)  --  Minimum Message Length (MML) for optimal cluster   (?)1-yes,0-no (Default 0)
// OPTIONS(7)  --  Continuous variable dimensions (Default: 3, bigger than 2)
// OPTIONS(8)  --  Continuous variable dimensions with errors
// OPTIONS(9)  --  Binary variable dimensions (Default 0)
// OPTIONS(10) --  Categorical variable dimensions (Default 0)
// OPTIONS(11) --  Integer variable dimensions (Default 0)
// OPTIONS(12) --  Number of Iterations (Default 10).
// OPTIONS(13) --  Line Search Step length. (Default 5 or less). 
// OPTIONS(14) --  MML parameter, the minimum number of clusters (Default: 1)
// OPTIONS(15) --  MML parameter, the maximum number of clusters (Default: 20)
// OPTIONS(16) --  MML parameter, the regularizer (Default: 0)
// OPTIONS(17) --  the covaraince type: 
// covoption = 0 means free covariances
// covoption = 1 means diagonal covariances
// covoption = 2 means a common covariance for all components
// covoption = 3 means a common diagonal covarince for all components
//       (Default: 0)
// OPTIONS(18) --  the grid no. for GTM.
// OPTIONS(19) --  do clustering first? 
// OPTIONS(20) --  visualization type
// OPTIONS(21) --  missing data imputation methods
//   % mis_methods = 0   --  KNN 
//   % mis_methods = 1   --  MoG
//   % mis_methods = 2   --  SVD
//   % mis_methods = 3   --  LLS-L2
//   % mis_methods = 4   --  LLS-Pearson-Correlation
// OPTIONS(22) --  missing value identicator    
    display = coptions(1);
    tol     = coptions(2);
    mix_var = coptions(3);  // is the input data has different variable type?
    err_dim = coptions(4);  // do we need to consider errors?
    outlier = coptions(5);  // outliers considered?
    mml     = coptions(6);  // mml methods?
    c_dim   = coptions(7);  // continuous variable dimensions without error
    e_dim   = coptions(8);  // continuous variable dimensions with errors
    b_dim   = coptions(9);  // binary variable dimensions
    m_dim   = coptions(10); // multinomial variable dimensions
    i_dim   = coptions(11); // integer variable dimensions
    niters  = coptions(12); // the number of iterations
    line_s  = coptions(13); // line search step
    mml_min = coptions(14); // the minimum no. of clusters
    mml_max = coptions(15); // the maximum no. of clusters, and the input no. of clusters
    mml_reg = coptions(16); // the MML regularizer
    cv_type = coptions(17); // the covariance matrix type

    System.out.printf("The tolerance value for optimizataion covergence is %f\n", tol);
    System.out.printf("Is mix-variable input? %d\n", mix_var);
    System.out.printf("Do we need to consider error information ? %d\n", err_dim);
    System.out.printf("Do we need to consider outlier ? %d\n", outlier);
    System.out.printf("Do we need to look for optimal number of clusters? %d\n", mml);
    System.out.printf("The number of continuous variables -- %d\n", c_dim);
    System.out.printf("The number of continuous variables with error -- %d\n", e_dim);
    System.out.printf("The number of binary variables -- %d\n", b_dim);
    System.out.printf("The number of category variables -- %d\n", m_dim);
    System.out.printf("The number of integer variables  -- %d\n", i_dim);
    System.out.printf("The number of iterations -- %d\n", niters);
    System.out.printf("The line search steps -- %d\n", line_s);
    System.out.printf("The minimum no. of clusters -- %d\n", mml_min);
    System.out.printf("The maximum no. of clusters -- %d\n", mml_max);
    System.out.printf("The mml regularizer -- %f\n", mml_reg);
    System.out.printf("The covariance matrix type -- %d\n", cv_type);
// if mml == 1 & mix_var == 1
    if( mml == 1) {
        disp "At present, MML is only for real data without measurement errors."
        disp "Cannot deal with data set with mixed-type variables"
        disp "To proceed, we only consider the continuous part of the data set"
        mix_var = 0;
// consider all real data , let the other parts of the data set to be
// none
        c_dim   = c_dim + e_dim;
        e_dim   = 0;
        b_dim   = 0;
        m_dim   = 0;
        i_dim   = 0;
    }

    bestk = mml_max;     // the default value for the optimal no. of classes
    ctype = cv_type;    // the covariance type

    if( mix_var == 0 ){       // no mixed variables
// no mixed-type variables
        if (c_dim != 0 & mml != 0 & outlier == 0 ){ // for real data with and without errors
// use mml methods to determine the number of clusters
            disp "mml for clustering without outlier"
            pause
            data = data(:,1:c_dim+e_dim);
            [bestk,bestpp,bestmu,bestcov,R]=mixtures4(data',
                    mml_min,mml_max,
                mml_reg,tol,cv_type);
            System.out.printf("The optimal number of clusters is %d\n", bestk)
// bestk     -- the optimal number of clusters
// bestpp    -- the mixture probabilities
// bestmu    -- the estimated mean of the clusters
// bestcov   -- the estimated covariance matrix for the clusters
// R         -- the present 
            ndata = size(data,1);
            ndim = c_dim +e_dim;
            if cv_type == 0     % full covariance            
                sizeall = 2+bestk+bestk*ndim+bestk*ndim*ndim+ndata*bestk;
            elseif cv_type == 1 % diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            elseif cv_type == 2 % spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            end
// allinfo --  all the results obtained from the training. The
// results includes
// 1. the size of the information
// 2. covariance type
// 3. the optimal number of clusters
// 4. the mix proportion 
// 5. the estimated mean
// 6. the estimated covariance matrix
// 7. the responsibilities
// all the information will be included in an ascii data file with
// fixed rows (the number of rows is ndata)        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); 
                bestcov(:); R(:)];
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            allsize = ndata*colnos;
            allinfo =[allinfo; zeros(allsize-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= allsize
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);
            errlog = [];
        end
        if c_dim ~= 0 & mml ~=0 & outlier == 1
            disp "mml for clustering with outlier"
            pause
            data = data(:,1:c_dim+e_dim);
            [bestk,bestpp,bestmu,bestcov,R]=tmixtures(data'
                  ,mml_min,mml_max,
                mml_reg,tol,cv_type,1.0);
            System.out.printf("The optimal number of clusters is %d\n", bestk);
            ndata = size(data,1);
            ndim = c_dim + e_dim;
            if cv_type == 0     % full covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim*ndim+ndata*bestk;
            elseif cv_type == 1 % diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            elseif cv_type == 2 % spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            end        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); bestcov(:); R(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            allsize = ndata*colnos;
            allinfo =[allinfo; zeros(allsize-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= allsize
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);
            errlog = [];
        end    
// real data without error information and without outliers
        if c_dim ~= 0 & mml == 0 & outlier == 0 & err_dim == 0
            vartype = [1 c_dim; 2 0; 3 0; 4 0; 5 0; 6 0];
            disp "clustering without outlier and without error"
            pause
            [R, bestpp, bestmu, bestcov, errlog] = cluster_err(data, vartype, mml_max, 
                niters, tol, cv_type);
            ndata = size(data,1);
            
            if cv_type == 0     % full covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim*c_dim+ndata*bestk;
            elseif cv_type == 1 % diagonal covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim+ndata*bestk;
            elseif cv_type == 2 % spherical covariance
                sizeall = 2+bestk+bestk*c_dim+bestk+ndata*bestk;
            end  
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); 
                bestcov(:); R(:)];    
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            disp "write the output"
            output = writeascii(text, y, output); 
        end
// real data without error information, but with outliers
        if c_dim ~= 0 & mml == 0 & outlier == 1 & err_dim == 0
            vartype = [1 c_dim; 2 0; 3 0; 4 0; 5 0; 6 0];
            disp "clustering with outlier and without error"
            pause
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, O, errlog] = robust_cluster_err(data, vartype, 
                mml_max, niters, tol, cv_type);
            ndata = size(data,1);        
            if cv_type == 0     % full covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim*c_dim+ndata*bestk+ndata;
            elseif cv_type == 1 % diagonal covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim+ndata*bestk+ndata;
            elseif cv_type == 2 % spherical covariance
                sizeall = 2+bestk+bestk*c_dim+bestk+ndata*bestk+ndata;
            end        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); 
                bestcov(:); R(:); O(:)];
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
// real data with error information and without outliers
        if c_dim == 0 & mml == 0 & outlier == 0 & err_dim ~= 0
            vartype = [1 0; 2 e_dim; 3 0; 4 0; 5 0; 6 e_dim];
            if  cv_type == 0
                cv_type = 1;    % we only consider diagnoal covariance matrix
            end
            disp "clustering without outlier and with error"
            pause
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog]=cluster_err(data, vartype, mml_max,
                niters, tol, cv_type);        
            ndim = e_dim;
            ndata = size(data,1)
            if cv_type == 0     % full covariance
                disp "full covariance"
                sizeall = 2+bestk+bestk*ndim+bestk*e_dim*e_dim+ndata*bestk;
            elseif cv_type == 1 % diagonal covariance
                disp "diag covariance"
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            elseif cv_type == 2 % spherical covariance
                disp "spherical covariance"
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            end        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); 
                bestcov(:); lbestcov(:); R(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            tall = zeros(alls-sizeall,1);
            allinfo =[allinfo; tall];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
// real data with error information and outlier
        if c_dim == 0 & mml == 0 & outlier == 1 & err_dim ~= 0
            vartype = [1 0; 2 e_dim; 3 0; 4 0; 5 0; 6 e_dim];
            disp "clustering with outlier and with error"
            pause
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, C, errlog] = robust_cluster_err(data, vartype,
                mml_max, niters, tol, cv_type);
            ndim = c_dim + e_dim;
            ndata = size(data,1);
            if cv_type == 0     % full covariance
                sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+ndata*bestk+ndata;
            elseif cv_type == 1 % diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk+ndata;
            elseif cv_type == 2 % spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk+ndata;
            end        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); 
                bestcov(:); lbestcov(:); R(:); C(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
        
        if b_dim ~= 0
// use mixture of binomial distribution
            vartype = [1 0; 2 0; 3 b_dim; 4 0; 5 0; 6 0]; 
            disp "clustering for binary data"
            pause
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, 
                tol, cv_type);
            ndim = b_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
        if m_dim ~= 0
            disp "clustering for categorical data"
            pause
            vartype = [1 0; 2 0; 3 0; 4 m_dim; 5 0; 6 0];
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, tol, cv_type);
            ndim = m_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
        if i_dim ~= 0
            vartype = [1 0; 2 0; 3 0; 4 0; 5 i_dim; 6 0];
            disp "clustering for integer data"
            pause
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, 
                tol, cv_type);
            ndim = i_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if rem(sizeall,ndata) ~= 0
                colnos = floor(sizeall/ndata)+1;
            else
                colnos = sizeall/ndata;
            end
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for k = 1: colnos-1
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            end
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if ini ~= alls
                disp "the number of size is not right"
                return;
            end
            output = writeascii(text, y, output);        
        end
    end
// Taking error information into consideration and outliers
    if mix_var ~= 0 & err_dim ~= 0 & outlier == 1
// with mixed variable types 
// first specify the variable types
        vartype = [1 c_dim; 2 e_dim; 3 b_dim; 4 m_dim; 5 i_dim; 6 e_dim];
        disp "clustering for mix-type data with error and with outlier"

        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, C, errlog]=robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);    
        ndim_cv = c_dim + e_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        ndata = size(data,1);
        if cv_type == 0     % full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+
                ndata*bestk+ndata;
        elseif cv_type == 1 % diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk+ndata;
        elseif cv_type == 2 % spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk+ndata;
        end
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:); 
            lbestcv(:); R(:); C(:)];
        sizeall = size(allinfo, 1);
// calculate the number of columns needed
        if rem(sizeall,ndata) ~= 0
            colnos = floor(sizeall/ndata)+1;
        else
            colnos = sizeall/ndata;
        end
        alls = ndata*colnos;   
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        
        y = [];        ini = 0;        text = [];
        for k = 1: colnos-1
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        end
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if ini ~= alls
            disp "the number of size is not right"
            return;
        end
        output = writeascii(text, y, output);
    end
// Taking error information into consideration, but no outliers
    if mix_var ~= 0 & err_dim ~= 0 & outlier == 0
// with mixed variable types  
        vartype = [1 c_dim; 2 e_dim; 3 b_dim; 4 m_dim; 5 i_dim; 6 e_dim];
        disp "clustering for mix-type data with error and without outlier"
        pause
        if cv_type == 0
            cv_type = 1;
        end
        ndata = size(data,1);
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, errlog] = cluster_err(data, 
            vartype, mml_max, niters, tol, cv_type);    
        ndim_cv = c_dim + e_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        if cv_type == 0     % full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+ndata*bestk;
        elseif cv_type == 1 % diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        elseif cv_type == 2 % spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        end
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:);
            lbestcv(:); R(:)];
// calculate the number of columns needed
        if rem(sizeall,ndata) ~= 0
            colnos = floor(sizeall/ndata)+1;
        else
            colnos = sizeall/ndata;
        end
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for k = 1: colnos-1
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        end
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        ini = ini+ndata;
        y = [y tk];
        if ini ~= alls
            disp "the number of size is not right"
            return;
        end
        output = writeascii(text, y, output);
    end
// No error information, with outliers
    if mix_var ~= 0 & err_dim == 0 & outlier == 1
// with mixed variable types 
// first specify the variable types
        disp "clustering for mix-type data without error and with outlier"
        pause
        vartype = [1 c_dim; 2 0; 3 b_dim; 4 m_dim; 5 i_dim; 6 0];
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, C, errlog]=robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);
        ndim_cv = c_dim;
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        ndata = size(data,1);
        if cv_type == 0     % full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv*ndim_cv+ndata*bestk;
        elseif cv_type == 1 % diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        elseif cv_type == 2 % spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        end        
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:);
            lbestcv(:); R(:)];
// calculate the number of columns needed
        if rem(sizeall,ndata) ~= 0
            colnos = floor(sizeall/ndata)+1;
        else
            colnos = sizeall/ndata;
        end
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for k = 1: colnos-1
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        end
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if ini ~= alls
            disp "the number of size is not right"
            return;
        end
        output = writeascii(text, y, output);
    end
// No error information, no outliers
    if mix_var ~= 0 & err_dim == 0 & outlier == 0
// with mixed variable types  
        disp "clustering for mix-type data without error and without outlier"
        pause
        vartype = [1 c_dim; 2 0; 3 b_dim; 4 m_dim; 5 i_dim; 6 0];
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
            cluster_err(data, vartype,mml_max,niters, tol, cv_type);
        ndim_cv = c_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv;
        ndata = size(data,1);
        if cv_type == 0     % full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv*ndim_cv+ndata*bestk;
        elseif cv_type == 1 % diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        elseif cv_type == 2 % spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        end        
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); bestcov(:); R(:)];
// calculate the number of columns needed
        if rem(sizeall,ndata) ~= 0
            colnos = floor(sizeall/ndata)+1;
        else
            colnos = sizeall/ndata;
        end
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for k = 1: colnos-1
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        end
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if ini ~= alls
            disp "the number of size is not right"
            return;
        end
        output = writeascii(text, y, output);
    end        
// six programs
// 1. mixture4       --- MML for real data
// 2. tmixtures      --- MML for real data with t-distribution
// 3. cluster_err    --- mixture modelling for real data with error info.
// 4. robust_cluster_err -- mixture modelling for real data with t and err.
}


/*
 * $Log: Clustering.java,v $
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
