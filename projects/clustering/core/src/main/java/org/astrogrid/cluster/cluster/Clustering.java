/*
 * $Id: Clustering.java,v 1.4 2009/09/17 07:03:50 pah Exp $
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


import no.uib.cipr.matrix.AGDenseMatrix;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.Algorithms.*;
import static org.astrogrid.matrix.MatrixUtils.*;

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
        
    public Retval  clustering( boolean display, 
            double tol,     
            boolean mix_var, 
            int err_dim,
            boolean outlier, 
            boolean mml,     
            int c_dim,   
            int e_dim,   
            int b_dim,   
            int m_dim,   
            int i_dim,   
            int niters,  
            double line_s,  
            int mml_min, 
            int  mml_max, 
            int  mml_reg, 
            CovarianceKind cv_type , AGDenseMatrix data){
// first, read coptions, copti tol     tol    ons gives the variable types, data associated
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
     //           (Default: 0)
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
    if( mml) {
        Util.disp( "At present, MML is only for real data without measurement errors.\n"
         +"Cannot deal with data set with mixed-type variables\n"
         +"To proceed, we only consider the continuous part of the data set");
        mix_var = false;
// consider all real data , let the other parts of the data set to be
// none
        c_dim   = c_dim + e_dim;
        e_dim   = 0;
        b_dim   = 0;
        m_dim   = 0;
        i_dim   = 0;
    }

    int bestk = mml_max;     // the default value for the optimal no. of classes
    CovarianceKind ctype = cv_type;    // the covariance type

    if( !mix_var ){       // no mixed variables
// no mixed-type variables
        if (c_dim != 0 & mml  & !outlier  ){ // for real data with and without errors
// use mml methods to determine the number of clusters
            Util.disp("mml for clustering without outlier");
            
            data = data(:,1:c_dim+e_dim);
            [bestk,bestpp,bestmu,bestcov,R]=mixtures4(data, //was data'
                    mml_min,mml_max,
                mml_reg,tol,cv_type);
            System.out.printf("The optimal number of clusters is %d\n", bestk);
// bestk     -- the optimal number of clusters
// bestpp    -- the mixture probabilities
// bestmu    -- the estimated mean of the clusters
// bestcov   -- the estimated covariance matrix for the clusters
// R         -- the present 
            ndata = size(data,1);
            ndim = c_dim +e_dim;
            if (cv_type == CovarianceKind.free){    // full covariance            
                sizeall = 2+bestk+bestk*ndim+bestk*ndim*ndim+ndata*bestk;
            }else if( cv_type == CovarianceKind.diagonal) {// diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            }else if( cv_type == CovarianceKind.common){ // spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            }
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
            if( rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            }else{
                colnos = sizeall/ndata;
            }
            allsize = ndata*colnos;
            allinfo =[allinfo; zeros(allsize-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for(int k = 0; k <colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if (ini!= allsize){
                Util.disp( "the number of size is not right");
                return;
            }
            
            output = writeascii(text, y, output);
            errlog = [];
        }
        if(c_dim != 0 & mml  & outlier){
            Util.disp ("mml for clustering with outlier");
            
            data = data(:,1:c_dim+e_dim);
            [bestk,bestpp,bestmu,bestcov,R]=tmixtures(data //was dat'
                  ,mml_min,mml_max,
                mml_reg,tol,cv_type,1.0);
            System.out.printf("The optimal number of clusters is %d\n", bestk);
            ndata = size(data,1);
            ndim = c_dim + e_dim;
            if(cv_type == CovarianceKind.free){     // full covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim*ndim+ndata*bestk;
            } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            } else if(cv_type == CovarianceKind.common){ // spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            }        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); bestcov(:); R(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            }else{
                colnos = sizeall/ndata;
            }
            allsize = ndata*colnos;
            allinfo =[allinfo; zeros(allsize-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != allsize){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);
            errlog = [];
        }    
// real data without error information and without outliers
        if(c_dim != 0 & mml == 0 & outlier == 0 & err_dim == 0){
            vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,0},{4,0},{5,0},{6,0}});
           Util.disp("clustering without outlier and without error");
            
            [R, bestpp, bestmu, bestcov, errlog] = cluster_err(data, vartype, mml_max, 
                niters, tol, cv_type);
            ndata = size(data,1);
            
            if(cv_type == CovarianceKind.free){     // full covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim*c_dim+ndata*bestk;
            } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim+ndata*bestk;
            } else if(cv_type == CovarianceKind.common){ // spherical covariance
                sizeall = 2+bestk+bestk*c_dim+bestk+ndata*bestk;
            }  
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); 
                bestcov(:); R(:)];    
// calculate the number of columns needed
            if( rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
           Util.disp("write the output");
            output = writeascii(text, y, output); 
        }
// real data without error information, but with outliers
        if(c_dim != 0 & mml == 0 & outlier == 1 & err_dim == 0){
            vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,0},{4,0},{5,0},{6,0}});
           Util.disp("clustering with outlier and without error");
            
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, O, errlog] = robust_cluster_err(data, vartype, 
                mml_max, niters, tol, cv_type);
            ndata = size(data,1);        
            if(cv_type == CovarianceKind.free){     // full covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim*c_dim+ndata*bestk+ndata;
            } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
                sizeall = 2+bestk+bestk*c_dim+bestk*c_dim+ndata*bestk+ndata;
            } else if(cv_type == CovarianceKind.common){ // spherical covariance
                sizeall = 2+bestk+bestk*c_dim+bestk+ndata*bestk+ndata;
            }        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); 
                bestcov(:); R(:); O(:)];
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
// real data with error information and without outliers
        if(c_dim == 0 & mml == 0 & outlier == 0 & err_dim != 0){
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,e_dim},{3,0},{4,0},{5,0},{6,e_dim}});
            if( cv_type == 0){
                cv_type = 1;    // we only consider diagnoal covariance matrix
            }
           Util.disp("clustering without outlier and with error");
            
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog]=cluster_err(data, vartype, mml_max,
                niters, tol, cv_type);        
            ndim = e_dim;
            ndata = size(data,1)
            if(cv_type == CovarianceKind.free){     // full covariance
               Util.disp("full covariance");
                sizeall = 2+bestk+bestk*ndim+bestk*e_dim*e_dim+ndata*bestk;
            } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
               Util.disp("diag covariance");
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk;
            } else if(cv_type == CovarianceKind.common){ // spherical covariance
               Util.disp("spherical covariance");
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
            }        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); 
                bestcov(:); lbestcov(:); R(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            tall = zeros(alls-sizeall,1);
            allinfo =[allinfo; tall];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
// real data with error information and outlier
        if(c_dim == 0 & mml == 0 & outlier == 1 & err_dim != 0){
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,e_dim},{3,0},{4,0},{5,0},{6,e_dim}});
           Util.disp("clustering with outlier and with error");
            
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, C, errlog] = robust_cluster_err(data, vartype,
                mml_max, niters, tol, cv_type);
            ndim = c_dim + e_dim;
            ndata = size(data,1);
            if(cv_type == CovarianceKind.free){     // full covariance
                sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+ndata*bestk+ndata;
            } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
                sizeall = 2+bestk+bestk*ndim+bestk*ndim+ndata*bestk+ndata;
            } else if(cv_type == CovarianceKind.common){ // spherical covariance
                sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk+ndata;
            }        
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); 
                bestcov(:); lbestcov(:); R(:); C(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
        
        if(b_dim != 0){
// use mixture of binomial distribution
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,b_dim},{4,0},{5,0},{6,0}}); 
           Util.disp("clustering for binary data");
            
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, 
                tol, cv_type);
            ndim = b_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
        if(m_dim != 0){
           Util.disp("clustering for categorical data");
            
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,0},{4,m_dim},{5,0},{6,0}});
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, tol, cv_type);
            ndim = m_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
        if(i_dim != 0){
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,0},{4,0},{5,i_dim},{6,0}});
           Util.disp("clustering for integer data");
            
            [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
                cluster_err(data, vartype, mml_max, niters, 
                tol, cv_type);
            ndim = i_dim;
            ndata = size(data,1);
            sizeall = 2+bestk+bestk*ndim+ndata*bestk;
            allinfo = [sizeall; bestk; bestpp(:); bestmu(:); R(:)];        
// calculate the number of columns needed
            if (rem(sizeall,ndata) != 0){
                colnos = floor(sizeall/ndata)+1;
            } else {
                colnos = sizeall/ndata;
            }
            alls = ndata*colnos;
            allinfo =[allinfo; zeros(alls-sizeall,1)];
            y = [];        ini = 0;        text = [];
            for (int k = 0; k < colnos; k++){
                textk = num2str(k);
                text = [text, "col", textk, ","];
                tk = allinfo(ini+1:ini+ndata);
                y = [y tk];
                ini = ini + ndata;
            }
            textk = num2str(colnos);
            text = [text, "col", textk];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini+ndata;
            if(ini != alls){
               Util.disp("the number of size is not right");
                return;
            }
            output = writeascii(text, y, output);        
        }
    }
// Taking error information into consideration and outliers
    if(mix_var != 0 & err_dim != 0 & outlier == 1){
// with mixed variable types 
// first specify the variable types
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,e_dim},{3,b_dim},{4,m_dim},{5,i_dim},{6,e_dim}});
       Util.disp("clustering for mix-type data with error and with outlier");

        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, C, errlog]=robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);    
        ndim_cv = c_dim + e_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        ndata = size(data,1);
        if(cv_type == CovarianceKind.free){     // full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+
                ndata*bestk+ndata;
        } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk+ndata;
        } else if(cv_type == CovarianceKind.common){ // spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk+ndata;
        }
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:); 
            lbestcv(:); R(:); C(:)];
        sizeall = size(allinfo, 1);
// calculate the number of columns needed
        if (rem(sizeall,ndata) != 0){
            colnos = floor(sizeall/ndata)+1;
        } else {
            colnos = sizeall/ndata;
        }
        alls = ndata*colnos;   
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        
        y = [];        ini = 0;        text = [];
        for (int k = 0; k < colnos; k++){
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        }
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if(ini != alls){
           Util.disp("the number of size is not right");
            return;
        }
        output = writeascii(text, y, output);
    }
// Taking error information into consideration, but no outliers
    if(mix_var != 0 & err_dim != 0 & outlier == 0){
// with mixed variable types  
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,e_dim},{3,b_dim},{4,m_dim},{5,i_dim},{6,e_dim}});
       Util.disp("clustering for mix-type data with error and without outlier");
        
        if(cv_type == CovarianceKind.free){
            cv_type = 1;
        }
        ndata = size(data,1);
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, errlog] = cluster_err(data, 
            vartype, mml_max, niters, tol, cv_type);    
        ndim_cv = c_dim + e_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        if(cv_type == CovarianceKind.free){     // full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*c_dim*c_dim+bestk*e_dim*e_dim+ndata*bestk;
        } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        } else if(cv_type == CovarianceKind.common){ // spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        }
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:);
            lbestcv(:); R(:)];
// calculate the number of columns needed
        if( rem(sizeall,ndata) != 0){
            colnos = floor(sizeall/ndata)+1;
        } else {
            colnos = sizeall/ndata;
        }
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for (int k = 0; k < colnos; k++){
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        }
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        ini = ini+ndata;
        y = [y tk];
        if(ini != alls){
           Util.disp("the number of size is not right");
            return;
        }
        output = writeascii(text, y, output);
    }
// No error information, with outliers
    if(mix_var != 0 & err_dim == 0 & outlier == 1){
// with mixed variable types 
// first specify the variable types
       Util.disp("clustering for mix-type data without error and with outlier");
        
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,b_dim},{4,m_dim},{5,i_dim},{6,0}});
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcv, C, errlog]=robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);
        ndim_cv = c_dim;
        ndim    = b_dim + m_dim + i_dim + ndim_cv; 
        ndata = size(data,1);
        if(cv_type == CovarianceKind.free){     // full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv*ndim_cv+ndata*bestk;
        } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        } else if(cv_type == CovarianceKind.common){ // spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        }        
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); lbestmu(:); bestcov(:);
            lbestcv(:); R(:)];
// calculate the number of columns needed
        if( rem(sizeall,ndata) != 0)
            colnos = floor(sizeall/ndata)+1;
        } else {
            colnos = sizeall/ndata;
        }
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for (int k = 0; k < colnos; k++){
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        }
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if(ini != alls){
           Util.disp("the number of size is not right");
            return;
        }
        output = writeascii(text, y, output);
    }
// No error information, no outliers
    if(mix_var != 0 & err_dim == 0 & outlier == 0){
// with mixed variable types  
       Util.disp("clustering for mix-type data without error and without outlier");
        
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,b_dim},{4,m_dim},{5,i_dim},{6,0}});
        [R, bestpp, bestmu, bestcov, lbestmu, lbestcov, errlog] = 
            cluster_err(data, vartype,mml_max,niters, tol, cv_type);
        ndim_cv = c_dim;    
        ndim    = b_dim + m_dim + i_dim + ndim_cv;
        ndata = size(data,1);
        if(cv_type == CovarianceKind.free){     // full covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv*ndim_cv+ndata*bestk;
        } else if(cv_type == CovarianceKind.diagonal){ // diagonal covariance
            sizeall = 2+bestk+bestk*ndim+bestk*ndim_cv+ndata*bestk;
        } else if(cv_type == CovarianceKind.common){ // spherical covariance
            sizeall = 2+bestk+bestk*ndim+bestk+ndata*bestk;
        }        
        allinfo = [sizeall; bestk; bestpp(:); bestmu(:); bestcov(:); R(:)];
// calculate the number of columns needed
        if (rem(sizeall,ndata) != 0){
            colnos = floor(sizeall/ndata)+1;
        } else {
            colnos = sizeall/ndata;
        }
        alls = ndata*colnos;
        allinfo =[allinfo; zeros(alls-sizeall,1)];
        y = [];        ini = 0;        text = [];
        for (int k = 0; k < colnos; k++){
            textk = num2str(k);
            text = [text, "col", textk, ","];
            tk = allinfo(ini+1:ini+ndata);
            y = [y tk];
            ini = ini + ndata;
        }
        textk = num2str(colnos);
        text = [text, "col", textk];
        tk = allinfo(ini+1:ini+ndata);
        y = [y tk];
        ini = ini+ndata;
        if(ini != alls){
           Util.disp("the number of size is not right");
            return;
        }
        output = writeascii(text, y, output);
    }        
// six programs
// 1. mixture4       --- MML for real data
// 2. tmixtures      --- MML for real data with t-distribution
// 3. cluster_err    --- mixture modelling for real data with error info.
// 4. robust_cluster_err -- mixture modelling for real data with t and err.
}


/*
 * $Log: Clustering.java,v $
 * Revision 1.4  2009/09/17 07:03:50  pah
 * morning update
 *
 * Revision 1.3  2009/09/16 16:53:06  pah
 * daily edit
 *
 * Revision 1.2  2009/09/15 21:40:15  pah
 * safety checkin
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
