/*
 * $Id: Clustering.java,v 1.8 2010/01/11 21:22:46 pah Exp $
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


import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.cluster.cluster.ClusterErr.ClusterErrResult;
import org.astrogrid.cluster.cluster.Mixtures.MixtureKind;
import org.astrogrid.cluster.cluster.Mixtures.Retval;
import org.astrogrid.cluster.cluster.RobustClusterErr.RobustClusterErrResult;
import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.Algorithms.*;
import static org.astrogrid.matrix.MatrixUtils.*;
import static java.lang.Math.*;

/**
 * @TODO return the latent variable estimates as well.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Jan 2010
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class Clustering {
// 1. clustering for data sets with mixed-type variables% 
// 3. clustering for real data sets with outliers
// 4. MML for determing the optimal number of clusters.
//    function [output errlog]=  R, bestpp, bestmu, bestcov, errlog
    
    
      public static class ClusteringResults {
        public final Matrix R;
        public final Vector bestpp;
        public final Matrix bestmu;
        public final Matrix bestcov;
        public final List<Double> errlog;
        public ClusteringResults(Matrix R, Vector bestpp, Matrix bestmu, Matrix bestcov, List<Double> errlog) {
            this.R = R;
            this.bestpp = bestpp;
            this.bestmu = bestmu;
            this.bestcov = bestcov;
            this.errlog = errlog;
        }
    };
        
    public ClusteringResults  clustering( boolean display, 
            double tol,     
            boolean mix_var, 
            boolean err_dim,
            boolean outlier, 
            boolean mml,     
            int c_dim,   
            int e_dim,   
            int b_dim,   
            int m_dim,   
            int i_dim,   
            int niters,  
            double line_s,  //not apparently used
            int mml_min, 
            int  mml_max, 
            double  mml_reg, 
            CovarianceKind cv_type , Matrix data){
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
    System.out.printf("Is mix-variable input? %b\n", mix_var);
    System.out.printf("Do we need to consider error information ? %b\n", err_dim);
    System.out.printf("Do we need to consider outlier ? %b\n", outlier);
    System.out.printf("Do we need to look for optimal number of clusters? %b\n", mml);
    System.out.printf("The number of continuous variables -- %d\n", c_dim);
    System.out.printf("The number of continuous variables with error -- %d\n", e_dim);
    System.out.printf("The number of binary variables -- %d\n", b_dim);
    System.out.printf("The number of category variables -- %d\n", m_dim);
    System.out.printf("The number of integer variables  -- %d\n", i_dim);
    System.out.printf("The number of iterations -- %d\n", niters);
    System.out.printf("The line search steps -- %f\n", line_s);
    System.out.printf("The minimum no. of clusters -- %d\n", mml_min);
    System.out.printf("The maximum no. of clusters -- %d\n", mml_max);
    System.out.printf("The mml regularizer -- %f\n", mml_reg);
    System.out.printf("The covariance matrix type -- %s\n", cv_type);
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
    
    Matrix vartype;
    AGDenseMatrix lbestmu, lbestcov;
    int bestk = mml_max;     // the default value for the optimal no. of classes
    ClusteringResults  retval = null;
    
    if( !mix_var ){       // no mixed variables
// no mixed-type variables
        if (c_dim != 0 & mml  & !outlier  ){ // for real data with and without errors
// use mml methods to determine the number of clusters
            Util.disp("mml for clustering without outlier");
            
            data = data.sliceCol(0, c_dim+e_dim);
           
            
             Retval mixret = Mixtures.mixtures4(transpose(data), //was data'
                    mml_min,mml_max,
                mml_reg,tol,cv_type,MixtureKind.Gaussian,1.0);
             
             retval = new ClusteringResults(mixret.R, mixret.bestpp, mixret.bestmu, mixret.bestcov.get(mixret.bestk-1), mixret.loglik);
             System.out.printf("The optimal number of clusters is %d\n", mixret.bestk);
      }
        if(c_dim != 0 & mml  & outlier){
            Util.disp ("mml for clustering with outlier");
          
            
            data = data.sliceCol(0, c_dim+e_dim);
            
            Retval mixret = Mixtures.mixtures4(transpose(data) //was dat'
                  ,mml_min,mml_max,
                mml_reg,tol,cv_type,MixtureKind.TDistribution,1.0);
            System.out.printf("The optimal number of clusters is %d\n", mixret.bestk);
            retval = new ClusteringResults(mixret.R, mixret.bestpp, mixret.bestmu, mixret.bestcov.get(mixret.bestk-1), mixret.loglik);
          
        }    
// real data without error information and without outliers
        if(c_dim != 0 & !mml  & !outlier  & !err_dim){
           vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,0},{4,0},{5,0},{6,0}});
           Util.disp("clustering without outlier and without error");
          
           
             ClusterErrResult rce = ClusterErr.cluster_err(data, vartype, mml_max, 
                niters, tol, cv_type);
             retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);
             
                     
        }
// real data without error information, but with outliers
        if(c_dim != 0 & !mml  & outlier & !err_dim ){
            vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,0},{4,0},{5,0},{6,0}});
           Util.disp("clustering with outlier and without error");
            
            RobustClusterErrResult rce = RobustClusterErr.robust_cluster_err(data, vartype, 
                mml_max, niters, tol, cv_type);
 
            retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);
            // still need lbestmu;lbestcv; C;

            lbestmu = rce.lmu; lbestcov =rce.lcv;
         
        }
// real data with error information and without outliers
        if(c_dim == 0 & !mml  & !outlier  & err_dim ){
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,e_dim},{3,0},{4,0},{5,0},{6,e_dim}});
            if( cv_type ==CovarianceKind.free){
                cv_type = CovarianceKind.diagonal;    // we only consider diagnoal covariance matrix
            }
           Util.disp("clustering without outlier and with error");
           
            
            ClusterErrResult rce = ClusterErr.cluster_err(data, vartype, mml_max,
                niters, tol, cv_type);        
            retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);
       
        }
// real data with error information and outlier
        if(c_dim == 0 & !mml  & outlier & err_dim){
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,e_dim},{3,0},{4,0},{5,0},{6,e_dim}});
           Util.disp("clustering with outlier and with error");
           RobustClusterErrResult rce = RobustClusterErr.robust_cluster_err(
                   data, vartype, mml_max, niters, tol, cv_type);    
                
                retval = new ClusteringResults(rce.q, rce.p, rce.lmu, rce.lcv, rce.loglik);
        
        }
        
        if(b_dim != 0){
// use mixture of binomial distribution
           vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,b_dim},{4,0},{5,0},{6,0}}); 
           Util.disp("clustering for binary data");
           

             ClusterErrResult rce = ClusterErr.cluster_err(data, vartype, mml_max, niters, 
            tol, cv_type);

            retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);

           }
        if(m_dim != 0){
           Util.disp("clustering for categorical data");
            
            vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,0},{4,m_dim},{5,0},{6,0}});
            ClusterErrResult rce = ClusterErr.cluster_err(data, vartype, mml_max, niters, tol, cv_type);
            retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);

        }
        if(i_dim != 0){
           vartype = new AGDenseMatrix(new double[][]{{1,0},{2,0},{3,0},{4,0},{5,i_dim},{6,0}});
           Util.disp("clustering for integer data");
           ClusterErrResult rce = ClusterErr.cluster_err(data, vartype, mml_max, niters, 
        tol, cv_type);
           retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);
 
        }
    }
// Taking error information into consideration and outliers
    if(mix_var  & err_dim  & outlier){
// with mixed variable types 
// first specify the variable types
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,e_dim},{3,b_dim},{4,m_dim},{5,i_dim},{6,e_dim}});
       Util.disp("clustering for mix-type data with error and with outlier");

       
         RobustClusterErrResult rce = RobustClusterErr.robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);    
         
         retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);
         //FIXME still need lbestmu;lbestcv; C;
         
         //R = rce., bestpp; bestmu; bestcov;  errlog;
    }
// Taking error information into consideration, but no outliers
    if(mix_var  & err_dim  & !outlier){
// with mixed variable types  
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,e_dim},{3,b_dim},{4,m_dim},{5,i_dim},{6,e_dim}});
       Util.disp("clustering for mix-type data with error and without outlier");

        if(cv_type == CovarianceKind.free){
            cv_type = CovarianceKind.diagonal;
        }
        ClusterErrResult rce = ClusterErr.cluster_err(data, 
            vartype, mml_max, niters, tol, cv_type);    
        retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);

    }
// No error information, with outliers
    if(mix_var  & !err_dim  & outlier){
// with mixed variable types 
// first specify the variable types
       Util.disp("clustering for mix-type data without error and with outlier");
          
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,b_dim},{4,m_dim},{5,i_dim},{6,0}});
        RobustClusterErrResult rce = RobustClusterErr.robust_cluster_err(
            data, vartype, mml_max, niters, tol, cv_type);
        retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);

    }
// No error information, no outliers
    if(mix_var  & !err_dim  & !outlier){
// with mixed variable types  
       Util.disp("clustering for mix-type data without error and without outlier");
      
        vartype = new AGDenseMatrix(new double[][]{{1,c_dim},{2,0},{3,b_dim},{4,m_dim},{5,i_dim},{6,0}});
        ClusterErrResult rce = ClusterErr.cluster_err(data, vartype,mml_max,niters, tol, cv_type);
        retval = new ClusteringResults(rce.q, rce.p, rce.mu, rce.cv, rce.loglik);

    }  
    
    return retval;
// six programs
// 1. mixture4       --- MML for real data
// 2. tmixtures      --- MML for real data with t-distribution
// 3. cluster_err    --- mixture modelling for real data with error info.
// 4. robust_cluster_err -- mixture modelling for real data with t and err.
}

}
/*
 * $Log: Clustering.java,v $
 * Revision 1.8  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 * Revision 1.7  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
 * Revision 1.6  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 * Revision 1.5  2009/09/17 14:13:13  pah
 * evening commit
 *
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
