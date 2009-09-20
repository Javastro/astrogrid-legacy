/*
 * $Id: ClusterMStepFull.java,v 1.4 2009/09/20 17:18:01 pah Exp $
 * 
 * Created on 12 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;


/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Dec 2008 -  translated  from matlab clustering_m_step_full
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public class ClusterMStepFull {

    
    public static class Retval {
        public final AGDenseMatrix latent;
        public final AGDenseMatrix mu;
        public final AGDenseMatrix cv;
        public final AGDenseMatrix lmu;
        public final AGDenseMatrix lcv;
        public final Vector p; 
        public Retval(AGDenseMatrix latent,AGDenseMatrix mu,AGDenseMatrix cv, AGDenseMatrix lmu, AGDenseMatrix lcv, Vector p ) {
            this.latent = latent;
            this.mu = mu;
            this.cv = cv;
            this.lmu = lmu;
            this.lcv = lcv;
            this.p = p;            
        }
        
    }
    static Retval  cluster_m_step_full(Matrix data, Matrix datatype, 
                int K, AGDenseMatrix output, AGDenseMatrix latent, AGDenseMatrix ab, AGDenseMatrix q, AGDenseMatrix lcv, CovarianceKind cv_type){
        // -------------------------------------------------------------------------
        // Re-estimate the global parameters of the model
        //--------------------------------------------------------------------------
        // function [latent mu cv lmu lcv p] = clustering_m_step_full(data, datatype, ...
        //    K, output, latent, ab, q, lcv, cv_type)
        //---
        // M-step of the VB method
        //IMPL note that lcv is cleared before being used, so is not really a parameter...
        //IMPL latent not changing
        //---
        int ndata = data.numRows();
        int no_of_data_types = datatype.numRows();

        Matrix data_nr = null, data_bin = null, data_mul = null, data_int = null;
        Vector gcv_c = new DenseVector(no_of_data_types);
        Matrix   S;
        int ndim_nr = 0, ndim_er = 0,ndim_bin = 0,ndim_mul = 0,ndim_int = 0;

        int ini = 0, d = 0;
        Matrix qcv[][] = new AGDenseMatrix[ndata][K];//FIXME these are probably not correct
        DenseVector qmu[][] = new DenseVector[ndata][K];
        for(int i = 0; i <no_of_data_types; i++){
            if (datatype.get(i,0) == 1){     // continuous data without errors
                ndim_nr = (int) datatype.get(i,1);
                data_nr = data.sliceCol(d, ndim_nr );
                d = d + ndim_nr;
            } else if (datatype.get(i,0) == 2){ // continous data with errors
                ndim_er = (int) datatype.get(i,1);
                for (int n = 0; n <ndata; n++){
                    for (int k = 0; k <K; k++){
                        switch (cv_type) {
                            case free:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*
                                    ndim_er -1), ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k]= reshape(output.asVector(ini, ini+ndim_er -1),1,
                                    ndim_er).asVector();
                                ini = ini + ndim_er;
                                break;
                            case diagonal:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*
                                    ndim_er -1),ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k]= reshape(output.asVector(ini, ini+ndim_er -1),1,
                                    ndim_er).asVector();
                                ini = ini + ndim_er;
                                break;
                            case common:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*ndim_er -1),
                                    ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k]=reshape(output.asVector(ini, ini+ndim_er -1), 1, ndim_er).asVector();
                                ini = ini + ndim_er;
                                break;
                        }
                    }
                }            
                d = d + ndim_er;
            } else if (datatype.get(i,0) == 3){
                ndim_bin = (int) datatype.get(i,1);
                data_bin = data.sliceCol(d, ndim_bin );
                d = d  + ndim_bin;
            } else if (datatype.get(i,0) == 4){
                ndim_mul = (int) datatype.get(i,1);
                data_mul = data.sliceCol(d, ndim_mul );
                d = d + ndim_mul;
            } else if (datatype.get(i,0) == 5){
                ndim_int = (int) datatype.get(i,1);       
                data_int = data.sliceCol(d, ndim_int);
                d = d + ndim_int;
            } else if (datatype.get(i,0) == 6){
                int ndim_error = (int) datatype.get(i,1);
                if (ndim_error != ndim_er){
                    throw new IllegalArgumentException( "The dimension of measurement errors and ");
                }        
                S = data.sliceCol(d, ndim_error);
                // error inforamtion
                S.add( 1.0e-6,ones(ndata, ndim_er));
                d = d+ ndim_error;
            }
        }
       Matrix a = null,b = null;
       AGDenseMatrix v;
        if( ndim_er != 0 || ndim_nr != 0){
            a = reshape(ab.asVector(0, ndata*K - 1), ndata, K);
            b = reshape(ab.asVector(ndata*K), ndata, K);
            v = reshape(latent, K, 1);
        }
        Matrix gmu_nr = null,gcv_nr[] = new AGDenseMatrix[K], gcv_nr_d = new AGDenseMatrix(K,ndim_nr), gmu = null, gcv_f[] = new AGDenseMatrix[K], gcv_d = new AGDenseMatrix(K, ndim_er);

        AGDenseMatrix mu = new AGDenseMatrix(0,0) ,cv = new AGDenseMatrix(0,0), lmu = new AGDenseMatrix(0,0);
        lcv = new AGDenseMatrix(0,0);
        gmu = new AGDenseMatrix(K, ndim_er);
        if (ndim_er != 0 ){ 
            for (int k = 0; k <K; k++){
                gmu.setRow(k, zeros(1, ndim_er));
                Vector tmp = gmu.sliceRow(k);
                for (int n = 0; n <ndata; n++){
                    
                    tmp.add(q.get(n,k)*a.get(n,k)/b.get(n,k), qmu[n][k]);
                    
                }
                tmp.scale(1.0/sum( divide(times(q.sliceCol(k, 1), a.sliceCol(k, 1)), b.sliceCol(k, 1)).asVector()));
                gmu.setRow(k, tmp);
                
            }
            
            lmu.append((DenseVector)gmu.asVector());
            //----
            // gcv
            //----
            for (int k = 0; k <K; k++){
                switch (cv_type) {
                    case free:
                        Matrix gcvt = zeros(ndim_er, ndim_er);
                        for (int n = 0; n <ndata; n++){
                            Vector tmp = gmu.sliceRow(k);
                            tmp = tmp.add(-1.0,qmu[n][k]);
                            Matrix mtmp = new AGDenseMatrix(tmp);
                            Matrix mtmp2 = new AGDenseMatrix(tmp.size(), tmp.size());
                            mtmp.transBmult(1.0, mtmp, mtmp2);
                            gcvt = (Matrix) gcvt.add(q.get(n,k)*a.get(n,k)/b.get(n,k), add(qcv[n][k],mtmp2));
                        }
                        gcv_f[k] = (Matrix) gcvt.scale(1.0/(sum(q.sliceCol(k, 1).asVector())+eps));
                        lcv.append((DenseVector)gcv_f[k].asVector());
                        break;
                    case diagonal:
                        for (int i = 0; i <ndim_er; i++){
                            Double tmp = 0.0;
                            for (int n = 0; n <ndata; n++){
                                tmp = tmp + q.get(n,k)*a.get(n,k)/b.get(n,k)*(Math.pow(qmu[n][k].get(i)- 
                                    gmu.get(k,i),2)+qcv[n][k].get(i,i));
                            }
                            gcv_d.set(k,i, tmp/ (sum(q.sliceCol(k, 1).asVector())+eps));
                        }
                        lcv.append(gcv_d.sliceRow(k));
                        break;
                    case common:
                        double tmpt = 0.0;
                        for (int n = 0; n <ndata; n++){
                            Vector tqmu = qmu[n][k];
                            tqmu.add(-1.0, gmu.sliceRow(k));
                            tmpt = tmpt + q.get(n,k)*(( tqmu.dot(tqmu)+ 
                                trace(qcv[n][k]))*a.get(n,k)/b.get(n,k));
                        }
                        gcv_c.set(k, tmpt/(ndim_er*sum(q.sliceCol(k, 1).asVector())));
                        lcv.append(gcv_c.get(k));
                        break;
                }
            }
        }

        if (ndim_nr != 0){
            // maximize for the parameters gmu, gcv for the data without errors
            //-----------
            //   gmu
            //-----------
            gmu_nr = new AGDenseMatrix(K,ndim_nr);
            for (int k = 0; k <K; k++){
                Vector tmp = zeros(1, ndim_nr).asVector();
                for (int n = 0; n <ndata; n++){
                    tmp.add(q.get(n,k)*a.get(n,k)/b.get(n,k), data_nr.sliceRow(n));
                }
                tmp.scale(1.0/sum( divide(times(q.sliceCol(k, 1), a.sliceCol(k, 1)), b.sliceCol(k, 1)).asVector()));
                gmu_nr.setRow(k,tmp); 
            }
            mu.append(gmu_nr.asVector());
            //-----------
            // gcv
            //-----------
            for (int k = 0; k <K; k++){
                switch (cv_type) {
                    case free:
                        Matrix gcvt = zeros(ndim_nr, ndim_nr);
                        for (int n = 0; n <ndata; n++){
                            Matrix tmp = (Matrix) gmu_nr.sliceRowM(k).add(-1.0, data_nr.sliceRowM(n));
                            tmp = multAt(tmp,tmp);
                            gcvt.add(q.get(n,k)*a.get(n,k)/b.get(n,k), tmp);
                        }
                        gcv_nr[k] = (Matrix) gcvt.scale(1.0/(sum(q.sliceCol(k, 1).asVector())+eps));
                        
                        cv.append(gcv_nr[k].asVector());
                        break;
                    case diagonal:
                        for (int i = 0; i <ndim_nr; i++){
                            double tmp = 0.0;
                            for (int n = 0; n <ndata; n++){
                                tmp = tmp + q.get(n,k)*a.get(n,k)/b.get(n,k)*Math.pow(data_nr.get(n,i)-
                                    gmu_nr.get(k,i),2);
                            }
                            gcv_nr_d.set(k,i,tmp/ (sum(q.sliceCol(k, 1).asVector())+eps));
                        }
                        cv.append(gcv_nr_d.sliceRow(k));
                        break;
                    case common:
                        double tmpt = 0.0;
                        for (int n = 0; n <ndata; n++){
                            Vector tmp = sub(data_nr.sliceRow(n), gmu_nr.sliceRow(k));
                            tmpt=tmpt+q.get(n,k)*tmp.dot(tmp);
                        }
                        
                        cv.append(tmpt/(ndim_nr*sum(q.sliceCol(k, 1).asVector())+eps));
                        break;
                }
            }
        }

        //--------------------------------------------------------------------------
        // the parameters for the binary data
        //--------------------------------------------------------------------------
        if (ndim_bin != 0){
            
            int  bin_dim = data_bin.numColumns();
            Matrix bp = new AGDenseMatrix(K,bin_dim);
            ndata = data_bin.numRows();
            for (int k = 0; k <K; k++){
                for (int j = 0; j <bin_dim; j++){
                    bp.set(k,j, (sum(times(q.sliceCol(k, 1), data_bin.sliceCol(j,1)).asVector())+1)/(sum(q.sliceCol(k, 1).asVector())+2));
                }
            }
            mu.append(bp.asVector());
        }

        if (ndim_mul != 0){
            int dim_mul = data_mul.numColumns();
            ndata = data_mul.numRows();
            Matrix mp = new AGDenseMatrix(K,dim_mul);
            for (int k = 0; k <K; k++){
                for (int i = 0; i <dim_mul; i++){
                    mp.set(k,i, sum(times(q.sliceCol(k, 1),data_mul.sliceCol(i, 1)).asVector()));
                }
                mp.setColumn(k, mp.sliceRow(k).scale(1.0/(sum(mp.sliceRow(k))+eps)));
            }
            mu.append(mp.asVector());
        }

        if (ndim_int != 0){
            int dim_int = data_int.numColumns();
            Matrix  ip = new AGDenseMatrix(K, dim_int);
            ndata = data_int.numRows();
            for (int k = 0; k <K; k++){
                for (int i = 0; i <dim_int; i++){
                    ip.set(k,i,sum(times(q.sliceCol(k,1),data_int.sliceCol(i,1)).asVector())/(sum(q.sliceCol(k,1).asVector())+eps));
                }
            }
            mu.append(ip.asVector());
        }

        // if ndim_er != 0 | ndim_nr != 0
        //     // ---------------------------------------------------------------------
        //     //   re-estimate v(k)
        //     // ---------------------------------------------------------------------
        //     v_old = v;
        //     for k = 1:K
        //         v(k) = fzero(@(x) vfunc(x,a(:,k),b(:,k),q(:,k)), v(k));
        //         if isnan(v(k))
        //             // in this case, we use a conjugate method to search for v
        //             v(k) = v_old(k);
        //             s = minimize(sqrt(v(k)), 'v_obj', 5, a(:,k), b(:,k), q(:,k));
        //             v(k) = s^2;
        //         end
        //     end
        //     latent = v;
        // end

        //-----------
        // priors
        //-----------
        Vector p = mean(q, 1);

       return new Retval(latent, mu, cv, lmu, lcv, p);
    }
    
//    static double vfunc(v, a, b, q){
//    double f = sum(q)*(log(v/2)+1-psi(v/2)) + sum(q .* ((psi(a)-log(b))-a./b)); 
//    return f;
//    }
   
}


/*
 * $Log: ClusterMStepFull.java,v $
 * Revision 1.4  2009/09/20 17:18:01  pah
 * checking just prior to bham visit
 *
 * Revision 1.3  2009/09/14 19:08:43  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.2  2009/09/08 19:23:30  pah
 * got rid of npe and array bound problems....
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
