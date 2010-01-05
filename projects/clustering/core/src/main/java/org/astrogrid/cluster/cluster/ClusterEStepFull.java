/*
 * $Id: ClusterEStepFull.java,v 1.5 2010/01/05 21:27:13 pah Exp $
 * 
 * Created on 11 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
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
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;



/**
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Dec 2008 - translated  from matlab clustering_e_step_full
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public class ClusterEStepFull {

    // this is the E-step of the variational EM algoorithm for Louisa's data. In
    // the data set, some variables are continuous with measurement errors, some
    // variables are continous without measurement errors, some variables are
    // binary, in the parameters, these data are separated as data, data_noerr,
    // bin_data, S is the error associated
    //function [output ab q C] = clustering_e_step_full(data, datatype, K, latent,...
    //    ab, mu, cv, lmu, lcv, p, cv_type)
    
    public static class Retval {
        public final AGDenseMatrix output;
        public final AGDenseMatrix ab;
        public final AGDenseMatrix q;
        public final AGDenseMatrix C;
        
        public Retval(AGDenseMatrix output, AGDenseMatrix ab, AGDenseMatrix q, AGDenseMatrix C) {
            this.output = output;
            this.ab = ab;
            this.q = q; 
            this.C = C;
        }
    }
    
    public static Retval clustering_e_step_full(Matrix data, Matrix datatype, int K, Matrix latent,
            AGDenseMatrix ab, Matrix mu, Matrix cv, Matrix lmu, Matrix lcv, Vector p, CovarianceKind cv_type
           
    ){
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // E-step of the VB method
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    int ndata = data.numRows();
    int no_of_data_types = datatype.numRows();
    int outpos = 0; //the position of the pointer in the output array.
    int abpos= 0;

    int n0 = 0, n1 = 0, nm = 0, ne = 0, d = 0;
    Matrix data_nr = null, data_er = null, data_bin = null, data_mul = null, data_int = null;
    Matrix gmu_nr = null,gcv_nr_f[] = new AGDenseMatrix[K], gcv_nr_d = null,  gmu = null, gcv_f[] = new AGDenseMatrix[K], gcv_d = null, S = null;
    Vector  gcv_nr_c= null, gcv_c = null;
    Matrix bp = null, mp = null, ip = null;
    AGDenseMatrix C = new AGDenseMatrix(ndata, K);
   
    int ndim_nr = 0, ndim_er = 0,ndim_bin=0,ndim_mul = 0,ndim_int = 0;
    for (int i = 0; i < no_of_data_types; i++){
        if (datatype.get(i,0) == 1){     // continuous data without errors
            if((ndim_nr = (int) datatype.get(i,1)) > 0){
            data_nr = data.sliceCol(d,ndim_nr );
            gmu_nr = reshape(mu.asVector(nm,nm+K*ndim_nr-1), K, ndim_nr);
            nm = nm + K*ndim_nr;
            switch (cv_type){
                case free:
                    for (int k = 0; k < K; k++){
                        gcv_nr_f[k] = reshape(cv.asVector(n0,n0+ndim_nr*ndim_nr -1),
                            ndim_nr, ndim_nr);
                        n0 = n0 + ndim_nr *ndim_nr;
                    }
                    break;
                case diagonal:
                    gcv_nr_d = reshape(cv.asVector(n0,n0+K*ndim_nr -1), K, ndim_nr);
                    n0 = n0 + K*ndim_nr;
                    break;
                case common:
                    gcv_nr_c = cv.asVector(n0);
                    n0 = n0 + K;
                    break;
            }
            }
            d = d + ndim_nr;
        }
        else if (datatype.get(i,0) == 2){ // continous data with errors
            if((ndim_er = (int) datatype.get(i,1))!=0){
            data_er = data.sliceCol(d,ndim_er);
            gmu = reshape(lmu.asVector(ne,ne+K*ndim_er-1),K, ndim_er);
            ne = ne + K*ndim_er;
            switch (cv_type) {
                case free:
                    for (int k = 0; k < K ; k++){
                        gcv_f[k] = reshape(lcv.asVector(n1,n1+ndim_er*ndim_er-1), 
                            ndim_er,ndim_er);
                        n1 = n1 + ndim_er * ndim_er;
                    }
                    break;
                case diagonal:
                    gcv_d = reshape(lcv.asVector(n1,n1+K*ndim_er-1), K, ndim_er);
                    n1 = n1 + K*ndim_er;
                    break;
                case common:
                    gcv_c = lcv.asVector(n1);
                break;
            }
            d = d + ndim_er;
            }
        }
        else if (datatype.get(i,0) == 3){
             ndim_bin = (int) datatype.get(i,1);
            data_bin = data.sliceCol(d,ndim_bin);
            bp = reshape(mu.asVector(nm,nm+K*ndim_bin-1), K, ndim_bin);
            nm = nm + K*ndim_bin;
            d = d  + ndim_bin;
        }
        else if (datatype.get(i,0) == 4){
             ndim_mul = (int) datatype.get(i,1);
            data_mul = data.sliceCol(d, ndim_mul );
            mp = reshape(mu.asVector(nm,nm+K*ndim_mul -1), K, ndim_mul);
            nm = nm + K*ndim_mul;
            d =  d + ndim_mul;
        }
        else if (datatype.get(i,0) == 5){
            ndim_int = (int) datatype.get(i,1);       
            data_int = data.sliceCol(d,ndim_int);
            ip = reshape(mu.asVector(nm,nm+K*ndim_int-1), K, ndim_int);
            nm = nm + K*ndim_int;
            d= d + ndim_int;
        }
        else if (datatype.get(i,0) == 6){
            int ndim_error = (int) datatype.get(i,1);
            if (ndim_error != ndim_er){
                throw new IllegalArgumentException( "The dimension of measurement errors and ");
            }        
            S = data.sliceCol(d,ndim_error);
            // error inforamtion
            S.add(ones(ndata, ndim_er).scale(1.0e-6));
            d = d + ndim_error;
        }
    }   

    AGDenseMatrix a = null, b = null;
    Vector v = null;
    if (ndim_er != 0 || ndim_nr != 0){
        a = reshape(ab.asVector(0,ndata*K-1), ndata, K);
        b = reshape(ab.asVector(ndata*K), ndata, K);
        v = reshape(latent, K, 1).asVector();
    }

    
    //--------------------------------------------------------------------------
    // THE POSTERIOR OF THE LATENT VARIABLES INCLUDING U AND W FOR THE VARIABLES
    // WITH MEASUREMENT ERRORS
    //--------------------------------------------------------------------------
    int ndim= data.numColumns();       // data is 
    AGDenseMatrix output = new AGDenseMatrix(ndim_er*(ndim_er+1)*K*ndata,1);

    AGDenseMatrix qcv[][]= new AGDenseMatrix[ndata][K];
    DenseVector qmu[][]= new DenseVector[ndata][K]; //actually always a vector
    if (ndim_er != 0) {
        // (1) the centre of the auxiliary distribution q
        Matrix epu = divide(a , b);         // the expectation of u
        // qmu -- the centre of the q-distribution w.r.t w
        // qcv -- the covariance of the q-distribution w.r.t w
        for (int n=0 ; n <ndata; n++){
            for (int k =0; k <K; k++){  
                switch (cv_type){
                    case free:
                        Matrix gcv_fe = new AGDenseMatrix(gcv_f[k]);
                        gcv_fe.scale(1.0/epu.get(n,k));
                        qcv[n][k] = mult(mult(diag(S.sliceRow(n)),inv(add(diag(S.sliceRow(n)),gcv_fe))),gcv_fe);
                        qmu[n][k] = add(mult(gmu.sliceRowM(k),mult(diag(S.sliceRow(n)),inv(add(diag(S.sliceRow(n)),gcv_fe))))
                            
                                ,mult(data_er.sliceRowM(n),mult(gcv_fe,inv(add(diag(S.sliceRow(n)),gcv_fe))))).asVector();
                        outpos = output.insert( qcv[n][k], outpos);
                        outpos = output.insert(qmu[n][k], outpos);
                        
                        break;
                    case diagonal:
                        Matrix gcv_scale = (Matrix) diag(gcv_d.sliceRow(k)).scale(1.0/epu.get(n,k));
                        qcv[n][k]=mult(mult(diag(S.sliceRow(n)),inv(add(gcv_scale,
                            diag(S.sliceRow(n))))),gcv_scale);
                        qmu[n][k]=add(multBt(gmu.sliceRowM(k),mult(diag(S.sliceRow(n)),inv(add(gcv_scale,diag(S.sliceRow(n))))))
                        ,multBt(data_er.sliceRowM(n),mult(gcv_scale,inv(add(gcv_scale,diag(S.sliceRow(n))))))).asVector();
                        outpos = output.insert( qcv[n][k], outpos);
                        outpos = output.insert(qmu[n][k], outpos);
                        break;
                    case common:                    
                        no.uib.cipr.matrix.Matrix tcv = eye(ndim_er).scale(gcv_c.get(k)/epu.get(n,k));
                        qcv[n][k]=mult(diag(S.sliceRow(n)).scale((gcv_c.get(k))/epu.get(n,k)),inv(
                            diag(S.sliceRow(n)).add(gcv_c.get(k)/epu.get(n,k))));
    //                     qcv[n][k] = diag(S.sliceRow(n))*inv(tcv/
    //                         epu.get(n,k)+diag(S.sliceRow(n)))*tcv/epu.get(n,k);
                        qmu[n][k]=add(multBt(gmu.sliceRowM(k),mult(diag(S.sliceRow(n)),inv(add(tcv ,diag(S.sliceRow(n))))))
                                ,mult(multBt(data_er.sliceRowM(n),tcv),inv(add(tcv,diag(S.sliceRow(n)))))).asVector();
                        outpos = output.insert( qcv[n][k], outpos);
                        outpos = output.insert(qmu[n][k], outpos);
                        break;
                }
            }
        }
        Matrix epdw = new AGDenseMatrix(ndata, K);
        // update q-distribution w.r.t u
        for (int n = 0; n < ndata; n++){
            for (int k = 0; k < K; k++){
                switch (cv_type){
                    case free:
                        epdw.set(n,k, trace(mult(inv(gcv_f[k]),qcv[n][k])) +      
                                multATBA(qmu[n][k],inv(gcv_f[k])).asScalar()
                            );
                        // the expectation of w^T Sigma^{-1}w
                        
                        C.set(n,k, epdw.get(n,k)-2*multBt(mult(qmu[n][k],inv(gcv_f[k]))
                            ,gmu.sliceRowM(k)).get(0, 0)+multABAT(gmu.sliceRowM(k),inv(gcv_f[k])).get(0,0));
                        break;
                    case diagonal:
                        epdw.set(n,k,trace(mult(inv(diag(gcv_d.sliceRow(k))),qcv[n][k]))
                            +multATBA(qmu[n][k],inv(diag(gcv_d.sliceRow(k)))).asScalar());
                        // the expectation of w^T Sigma^{-1}w
                        C.set(n,k,epdw.get(n,k)-2*multBt(multAt(qmu[n][k],inv(diag(gcv_d.sliceRow(k))))
                            ,gmu.sliceRowM(k)).get(0, 0)+multABAT(gmu.sliceRowM(k),inv(diag(gcv_d.sliceRow(k)))).get(0, 0));
                        break;
                    case common:
                        Vector tqmu = new DenseVector(qmu[n][k]);
                        tqmu.add(-1, gmu.sliceRow(k));
                        
                        C.set(n,k, tqmu.dot(tqmu)/gcv_c.get(k)+ 
                            trace(qcv[n][k])/gcv_c.get(k));
    //                     epdw(n,k)=trace(qcv[n][k])/gcv(k)
    //                         +qmu[n][k]'*qmu[n][k]/gcv(k);
    //                     // the expectation of w^T Sigma^{-1}w
    //                     C(n,k)=epdw(n,k)-2*qmu[n][k]'*gmu(k,:)'/gcv(k)
    //                         +gmu(k,:)*gmu(k,:)'/gcv(k);
                        break;
                }
            }
        }
    }
    Matrix n3 = null;
    if (ndim_nr != 0){
        //----------------------------------------------------------------------
        // THE POSTERIOR OF THE LATENT VARIABLE U FOR THE DATA WITH NO ERROR
        //----------------------------------------------------------------------

        //gcv_nr has different forms for the different models
        switch (cv_type){
        case free: 

            n3 = dist3_free(data_nr, gmu_nr, gcv_nr_f);

            break;
            
        case common:
            n3 = dist3_common(data_nr, gmu_nr, gcv_nr_c);
           break;
           
        case diagonal:
            n3 = dist3_diag(data_nr, gmu_nr, gcv_nr_d);
           break;
           

        }
    }


   
    if (datatype.get(0,1) !=0 && datatype.get(1,1) == 0 ){// continuous data without errors
    //     disp 'estimate parameters of u without measurement errors'
        a = (AGDenseMatrix) (repmatt(v, ndata, 1).add(ndim_nr).scale(0.5));
        b = (AGDenseMatrix) (repmatt(v, ndata, 1).add(n3).scale(0.5));
        C = (AGDenseMatrix) n3;
        abpos = ab.insert(a,abpos);
        abpos = ab.insert(b,abpos);

    //     pause
    }
    else if( datatype.get(1,1) !=0 && datatype.get(0,1) == 0){
    //     disp 'estimate parameter of u for variable with measurement errors'
        a = (AGDenseMatrix) (repmatt(v, ndata, 1).add(ndim_er).scale(0.5));
        b = (AGDenseMatrix) (repmatt(v, ndata, 1).add(C)).scale(0.5);
        abpos = ab.insert(a,abpos);
        abpos = ab.insert(b,abpos);
    //     C = C;
    }
    else if (datatype.get(0,1)!=0 && datatype.get(1,1) !=0){
    //     disp 'estimate parameter of u for combined variables'
        a = (AGDenseMatrix) (repmatt(v, ndata, 1).add(ndim_nr+ndim_er).scale(0.5));
        b = (AGDenseMatrix) (repmatt(v, ndata, 1).add(C).add(n3).scale(0.5));
        abpos = ab.insert(a,abpos);
        abpos = ab.insert(b,abpos);
        C.add(n3);
    }

    Matrix aux1 = new AGDenseMatrix(ndata,K), aux2 = new AGDenseMatrix(ndata,K), aux3 = new AGDenseMatrix(ndata,K), aux4 = new AGDenseMatrix(ndata,K), 
    aux5 = new AGDenseMatrix(ndata,K), aux6 = new AGDenseMatrix(ndata,K), 
     aux7 = new AGDenseMatrix(ndata,K), aux8 = new AGDenseMatrix(ndata,K), aux9 = new AGDenseMatrix(ndata,K), aux10 = new AGDenseMatrix(ndata,K), 
     aux11 = new AGDenseMatrix(ndata,K);
    aux1 = zeros(ndata, K);
    aux2 = zeros(ndata, K);
    aux3 = zeros(ndata, K);
    aux4 = zeros(ndata, K);
    aux5 = zeros(ndata, K);
    aux6 = zeros(ndata, K);
    aux9 = zeros(ndata, K);
    aux10 = zeros(ndata, K);
    aux11 = zeros(ndata, K);

    //--------------------------------------------------------------------------
    //   CALCULATE THE RESPONSIBILITIES
    //--------------------------------------------------------------------------
    if (ndim_er != 0){
    //     disp 'calculate components of continuous variable with errors for responsibilities'
        for (int n = 0; n < ndata; n++){
            for (int k = 0; k <K; k++){
                switch (cv_type){
                    case free:                  
                        aux1.set(n,k, -ndim_er/2.0*log(2*PI)-log(det(diag(S.sliceRow(n))))/2.0-
                            (multBt(mult(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() -  
                            multBt(multAt(qmu[n][k],inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar()*2 + 
                            trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k]))
                            + multATBA(qmu[n][k],inv(diag(S.sliceRow(n)))).asScalar()
                            )/2.0);
                        aux2.set(n,k, -ndim_er/2.0*log(2*Math.PI)-log(det(gcv_f[k]
                            ))/2.0+ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                            0.5*a.get(n,k)/b.get(n,k)*(                            
                            multATBA(qmu[n][k],inv(gcv_f[k])).asScalar()+ (trace(mult(inv(gcv_f[k]),qcv[n][k])))
                            -2*multBt(multAt(qmu[n][k],inv(gcv_f[k])),gmu.sliceRowM(k)).asScalar()
                            + multABAT(gmu.sliceRowM(k),inv(gcv_f[k])).asScalar() ));
                        // -E_q[ LOG q(W|K) ]
                        aux4.set(n,k, ndim_er/2.0*log(2*Math.PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0);     
                            break;
                    case diagonal:
                        aux1.set(n,k, -ndim_er/2.0*log(2*Math.PI)-log(det(diag(S.sliceRow(n))))/2.0-
                            (multABAT(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))).asScalar() - 
                            2*multBt(multAt(qmu[n][k],inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() +
                            trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k])) 
                            + multATBA(qmu[n][k],inv(diag(S.sliceRow(n)))).asScalar() )/2.0);                    
                        Vector covk = gcv_d.sliceRow(k);                    
                        aux2.set(n,k, -ndim_er/2.0*log(2*Math.PI)-sum(log(covk))/2.0 +
                            ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                            0.5*a.get(n,k)/b.get(n,k)*C.get(n,k));
                        // -E_q[ LOG q(W|K) ]
                        aux4.set(n,k, ndim_er/2.0*log(2*Math.PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0);
                        break;
                    case common:
                        aux1.set(n,k, -ndim_er/2.0*log(2*Math.PI)-log(det(diag(S.sliceRow(n))))/2.0-
                            (multABAT(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))).asScalar() - 
                            2*multBt(mult(qmu[n][k],inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() +
                            trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k])) 
                            + multATBA(qmu[n][k],inv(diag(S.sliceRow(n)))).asScalar()
                             )/2.0);
                        double covkd = gcv_c.get(k);
                        aux2.set(n,k, -ndim_er/2.0*log(2*Math.PI)-ndim_er*log(covkd)/2.0 +
                            ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                            0.5*a.get(n,k)/b.get(n,k)*C.get(n,k));
                        // -E_q[ LOG q(W|K) ]
                        aux4.set(n,k, ndim_er/2.0*log(2*Math.PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0);
                        break;
                    default:
                        throw new IllegalArgumentException( "Unknown noise/covariance model");
                }
                // E_q [LOG(P(U|K))]
                aux3.set(n,k, v.get(k)/2.0*log(v.get(k)/2.0)+(v.get(k)/2.0-1)*(psi(a.get(n,k))-log(b.get(n,k)))
                    -v.get(k)/2.0*a.get(n,k)/b.get(n,k)-log(gamma(v.get(k)/2.0)));
                // -E_q[ LOG q(U|K) ]
                aux5.set(n,k, -((a.get(n,k)-1)*psi(a.get(n,k))+log(b.get(n,k))-a.get(n,k)-
                    log(gamma(a.get(n,k)))));
            }
        }
    }
    if (ndim_nr != 0){
    //     disp 'calculate components of continus variable without errors for responsibilities'
        // THE SECOND PART IS RELATED TO THE DATA WITHOUT MEASUREMENT ERRORS
        for(int n = 0; n <ndata; n++){
            for (int k = 0; k <K; k++){
               
                if (ndim_er != 0){
                    // E_q[ log(p(t|u, k)) ]
                    switch (cv_type){
                        case diagonal:
                            Vector covkd = gcv_nr_d.sliceRow(k);
                            aux6.set(n,k, -ndim_nr/2.0*log(2*Math.PI)-sum(log(covkd))/2.0+
                                ndim_nr/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                0.5*a.get(n,k)/b.get(n,k)*n3.get(n,k));
                            break;
                        case common:
                            double covkc = gcv_nr_c.get(k);
                            aux6.set(n,k, -ndim_nr/2.0*log(2*Math.PI)-ndim_nr*log(covkc)/2.0+
                                ndim_nr/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                0.5*a.get(n,k)/b.get(n,k)*n3.get(n,k));
                            break;
                        case free:
                            aux6.set(n,k, -ndim_nr/2.0*log(2*Math.PI)-log(det(
                                gcv_nr_f[k]))/2.0+ndim_nr/2.0*(psi(a.get(n,k))
                                -log(b.get(n,k)))-0.5*a.get(n,k)/b.get(n,k)*n3.get(n,k));
                            break;
                        default:
                            throw new IllegalArgumentException( "Unknown noise model");
                    }
                }
                if (ndim_er == 0){ 
    //                 disp 'e-step, ndim_er == 0'
                    switch (cv_type){
                        case common:
    //                         disp 'e-step, ndim_er ~=0, spherical'
                            double covk = gcv_nr_c.get(k);
                            aux6.set(n,k, log(gamma((v.get(k)+ndim_nr)/2.0))- 
                                ndim_nr/2.0*log(covk)-ndim_nr/2.0*log(Math.PI*v.get(k)) 
                                -log(gamma(v.get(k)/2.0))-(v.get(k) + ndim_nr)/2.0* 
                                log(1+n3.get(n,k)/(covk*v.get(k))));
                            break;
                        case diagonal:
    //                         disp 'e-step, ndim er ~=0, diagonal'
                            Vector covkd = gcv_nr_d.sliceRow(k);
                            
                            double dist = sum(divide(pow(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),2.0).asVector() , covkd));
                            aux6.set(n,k,  log(gamma((v.get(k)+ndim_nr)/2.0))-
                                sum(log(covkd))/2.0-
                                ndim_nr/2.0*log(Math.PI * v.get(k))-log(gamma(v.get(k)/2.0))-
                                (v.get(k) + ndim_nr)/2.0*log(1 + dist/v.get(k)));
                            break;
                        case free:
                            Matrix covkf = gcv_nr_f[k];
                            aux6.set(n,k, log(gamma((v.get(k)+ndim_nr)/2.0))-log(det(
                                covkf))/2.0-ndim_nr/2.0*log(Math.PI*v.get(k))-log(gamma(v.get(k)/2.0))-
                                ((v.get(k)+ndim_nr)/2.0)*log(1+n3.get(n,k)/v.get(k)));
                            break;
                    }
                }
            }
        }
    }

    
    Vector sgm;
    Vector datai;
    if (ndim_bin !=0){
    //     disp 'calculate components of binary variable for responsibilities'
        //  THE THIRD PART IS RELATED TO THE BINARY DATA
        //  we need first calculate its mean
        for (int i = 0 ; i < ndata; i++){
            for (int j = 0 ; j < K; j++){
                sgm = add(bp.sliceRow(j), eps);
                datai = data_bin.sliceRow(i);
                aux9.set(i,j, sum(add(times(datai,log(sgm)),times(sub(1.0,datai),log(sub(1.0,sgm))))));
            }
        }
    }

    if (ndim_mul != 0){
    //     disp 'calculate components of category variable for responsibilities'
    //     pause    
        for(int n = 0 ; n < ndata; n++){
            for(int k = 0 ; k< K; k++){
                Vector temp = add(mp.sliceRow(k),eps);
                Vector log_sgm = log(temp);
                Vector datat = data_mul.sliceRow(n);
                aux10.set(n,k, sum(times(datat,log_sgm) ));
            }
        }
    }

    if (ndim_int !=0){
    //     disp 'calculate components of int variable for responsibilities'
        for(int t=0 ; t <ndata; t++){
            for (int i = 0 ; i < K; i++){
                sgm = ip.sliceRow(i);
                datai = data_int.sliceRow(t);
                double poiss = sum(sub(times(datai,log(sgm)),sgm));
                aux11.set(t,i,  poiss-sum(log(seq(ndim_int))));//FIXME - put ndim_int instead of datai here possible mistake in original?
            }
        }
    }

    Matrix aux = add(add(add(add(add(add(add(add(aux1 , aux2) ,aux3),  aux4), aux5), aux6), aux9), aux10) , aux11);

    AGDenseMatrix q = (AGDenseMatrix) times(multBt(ones(ndata, 1),new AGDenseMatrix(p)),exp(aux));
    Matrix s = new AGDenseMatrix(sum(q, 2));
    // Set any zeros to one before dividing 
    for (MatrixEntry matrixEntry : s) {
        if(matrixEntry.get() == 0.0)
        {
            matrixEntry.set(1.0);
        }
    }
    q = (AGDenseMatrix) divide(q,mult(s,ones(1, K)));

    Retval retval = new Retval(output, ab, q, C);
    
    return retval;

    }
    
    
    
 
    
 
}

/*
 * $Log: ClusterEStepFull.java,v $
 * Revision 1.5  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
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
