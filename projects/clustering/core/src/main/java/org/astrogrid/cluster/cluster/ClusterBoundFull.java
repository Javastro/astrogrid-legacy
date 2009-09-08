/*
 * $Id: ClusterBoundFull.java,v 1.2 2009/09/08 19:23:30 pah Exp $
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
import no.uib.cipr.matrix.Vector;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;

public class ClusterBoundFull {
    
    public static double cluster_bound_full(Matrix data, Matrix datatype, int K, AGDenseMatrix mu, AGDenseMatrix cv, AGDenseMatrix lmu, AGDenseMatrix lcv,
            Vector p, AGDenseMatrix output, AGDenseMatrix latent, AGDenseMatrix ab, AGDenseMatrix q, CovarianceKind cv_type){
        // this is the E-step of the variational EM algorithm for Louisa's data. In
        // the data set, some variables are continuous with measurement errors, some
        // variables are continous without measurement errors, some variables are
        // binary, in the parameters, these data are seperated as data, data_noerr,
        // bin_data, S is the error associated
        //
        // function loglike = clustering_bound_full(data, datatype, K, mu, cv, lmu,
        //    lcv, p, output, latent, ab, q, cv_type)
        //     loglike = clustering_bound_full(data, datatype, K, mu, cv, lmu, lcv,
        //         p, output, latent, ab, cv_type);
        // the first line of the input parameters are known data
        // the second line of the input parameters are the global parameters
        // the third line is the parameters for the latent variables
        // bp is the parameters for the binary data which we assume it follows a
        // binomial distribution

        // for the data with no errors, we assume a mixture of student
        // t-distribution, while for 
        int ndata = data.numRows();
        int no_of_data_types = datatype.numRows();

        int n0 = 0, n1 = 0, d = 0, nm = 0, ne = 0, ini = 0;
        Matrix data_nr = null, data_er = null, data_bin = null, data_mul = null, data_int = null;
        AGDenseMatrix qcv[][] = new AGDenseMatrix[ndata][K];
        AGDenseMatrix qmu[][] = new AGDenseMatrix[ndata][K];
        int ndim_nr = 0, ndim_er = 0,ndim_bin = 0,ndim_mul= 0,ndim_int =0;
        Matrix gmu_nr = null, gcv_nr_f[] = new AGDenseMatrix[K], gcv_nr_d = null, gmu = null;
        Vector gcv_nr_c = null;
        Matrix bp = null, mp = null, ip = null;
        Matrix S = null;
        AGDenseMatrix gcv_f[] = new AGDenseMatrix[K], gcv_d = null;
        Vector gcv_c = null;
        
        
        
        for ( int i = 0; i < no_of_data_types; i++) {
            if (datatype.get(i,0) == 1){     // continuous data without errors
                ndim_nr = (int) datatype.get(i,1);
                data_nr = data.sliceCol(d, ndim_nr );
                gmu_nr = reshape(mu.asVector(nm, nm+K*ndim_nr -1), K, ndim_nr);
                nm = nm + K*ndim_nr;
                switch (cv_type) {
                    case free:
                        for ( int k = 0; k < K; k++) {
                            gcv_nr_f[k] = reshape(cv.asVector(n0, n0+ndim_nr*ndim_nr -1), 
                                ndim_nr, ndim_nr);
                            n0 = n0 + ndim_nr*ndim_nr;
                        }
                        break;
                    case diagonal:
                        gcv_nr_d = reshape(cv.asVector(n0, n0+K*ndim_nr -1), K, ndim_nr);
                        n0 = n0 + K*ndim_nr;
                        break;
                    case common:
                        gcv_nr_c = cv.asVector(n0);
                        break;
                }
                d = d + ndim_nr;
            } else if (datatype.get(i,0) == 2) { // continous data with errors
                ndim_er = (int) datatype.get(i,1);
                data_er = data.sliceCol(d, ndim_er);
                gmu = reshape(lmu.asVector(ne, ne+K*ndim_er -1),K,ndim_er);
                ne = ne + K*ndim_er;        
                for ( int n = 0; n < ndata; n++) {
                    for ( int k = 0; k < K; k++) {
                        switch (cv_type) {
                            case free:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*
                                    ndim_er -1), ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k]=  reshape(output.asVector(ini, ini+ndim_er -1),1,
                                    ndim_er);
                                ini = ini + ndim_er;
                                break;
                            case diagonal:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*
                                    ndim_er -1),ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k] = reshape(output.asVector(ini, ini+ndim_er -1),1,
                                    ndim_er);
                                ini = ini + ndim_er;
                                break;
                            case common:
                                qcv[n][k] = reshape(output.asVector(ini, ini+ndim_er*ndim_er -1),
                                    ndim_er, ndim_er);
                                ini = ini + ndim_er*ndim_er;
                                qmu[n][k]=reshape(output.asVector(ini, ini+ndim_er -1), 1, ndim_er);
                                ini = ini + ndim_er;
                                break;
                        }
                    }
                }
                switch (cv_type) {
                    case free:
                        for ( int k = 0; k < K; k++) {
                          
                            gcv_f[k] = reshape(lcv.asVector(n1, n1+ndim_er*ndim_er -1), 
                                ndim_er,ndim_er);
                            n1 = n1 + ndim_er*ndim_er;
                        }
                        break;
                    case diagonal:
                        gcv_d = reshape(lcv.asVector(n1, n1+K*ndim_er -1), K, ndim_er);
                        n1 = n1 + K*ndim_er;
                        break;
                    case common:
                        gcv_c = lcv.asVector(n1);
                        break;
                }
                d = d + ndim_er;
            } else if (datatype.get(i,0) == 3) {
                ndim_bin = (int) datatype.get(i,1);
                data_bin = data.sliceCol(d, ndim_bin);
                bp = reshape(mu.asVector(nm, nm+K*ndim_bin -1), K, ndim_bin);
                nm = nm + K*ndim_bin;
                d = d  + ndim_bin;
            } else if (datatype.get(i,0) == 4) {
                ndim_mul = (int) datatype.get(i,1);
                data_mul = data.sliceCol(d, ndim_mul );
                mp = reshape(mu.asVector(nm, nm+K*ndim_mul -1), K, ndim_mul);
                nm = nm + K*ndim_mul;
                d = d + ndim_mul;
            } else if (datatype.get(i,0) == 5) {
                ndim_int = (int) datatype.get(i,1);       
                data_int = data.sliceCol(d, ndim_int );
                ip = reshape(mu.asVector(nm, nm+K*ndim_int -1), K, ndim_int);
                nm = nm + K*ndim_int;
                d  = d + ndim_int;
            } else if (datatype.get(i,0) == 6) {
                int ndim_error = (int) datatype.get(i,1);
                if  (ndim_error != ndim_er) {
                    throw new IllegalArgumentException( "The dimension of measurement errors and ");
                }        
                S = data.sliceCol(d, ndim_error );
                // error inforamtion
                S.add(1.0e-6,ones(ndata, ndim_er));
                d = d + ndim_error;
            }
        }
        Matrix a = null, b = null;
        Vector v = null;
        if  (ndim_er != 0 | ndim_nr != 0) {
            a = reshape(ab.asVector(0, ndata*K - 1), ndata, K);
            b = reshape(ab.asVector(ndata*K), ndata, K);
            v = reshape(latent, K, 1).asVector();
        }

        //--------------------------------------------------------------------------
        // THE POSTERIOR OF THE LATENT VARIABLES INCLUDING U AND W FOR THE VARIABLES
        // WITH MEASUREMENT ERRORS
        //--------------------------------------------------------------------------
        Matrix aux1, aux2, aux3, aux4, aux5, aux6, aux7, aux8, aux9, aux10, aux11, aux12;
        aux1 = zeros(ndata, K);
        aux2 = zeros(ndata, K);
        aux3 = zeros(ndata, K);
        aux4 = zeros(ndata, K);
        aux5 = zeros(ndata, K);
        aux6 = zeros(ndata, K);
        aux7 = zeros(ndata, K);
        aux8 = zeros(ndata, K);
        aux9 = zeros(ndata, K);
        aux10 = zeros(ndata, K);
        aux11 = zeros(ndata, K);
        aux12 = zeros(ndata, K);

        //--------------------------------------------------------------------------
        //   CALCULATE THE RESPONSIBILITIES
        //--------------------------------------------------------------------------
        if  (ndim_er != 0) {
        //     disp 'calculate components of continuous variable with errors for responsibilities'
            for ( int n = 0; n < ndata; n++) {
                for ( int k = 0; k < K; k++) {
                    // E_q [LOG P(W|U, K)]
                    switch (cv_type){
                        case free:
                            aux1.set(n,k, -ndim_er/2.0*Math.log(2*PI)-Math.log(det(diag(S.sliceRow(n))))/2.0-
                                (multABAT(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))).asScalar() - 
                                2*multBt(multAt(qmu[n][k],inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() +
                                trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k])) 
                                + multATBA(qmu[n][k],inv(diag(S.sliceRow(n)))
                                ).asScalar())/2.0);
                            aux2.set(n,k,-ndim_er/2.0*log(2*PI)-log(det(gcv_f[k]
                                ))/2.0+ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                0.5*a.get(n,k)/b.get(n,k)*(
                                trace(mult(inv((gcv_f[k])),qcv[n][k]))
                                +multATBA(qmu[n][k],inv(gcv_f[k])).asScalar()
                                -2*multBt(multAt(qmu[n][k],inv(gcv_f[k])),gmu.sliceRowM(k)).asScalar()
                                + multABAT(gmu.sliceRowM(k),inv(gcv_f[k])).asScalar() ));
                            // -E_q[ LOG q(W|K) ]
                            aux4.set(n,k, ndim_er/2.0*log(2*PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0); 
                                break;
                        case diagonal:
                            aux1.set(n,k,-ndim_er/2.0*log(2*PI)-log(det(diag(S.sliceRow(n))))/2.0-
                                (multABAT(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))).asScalar() - 
                                2*multBt(multAt(qmu[n][k],inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() +
                                trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k])) 
                                + multATBA(qmu[n][k],inv(diag(S.sliceRow(n)))).asScalar()
                               )/2.0);                    
                            Vector covk = gcv_d.sliceRow(k);                    
                            aux2.set(n,k,-ndim_er/2.0*log(2*PI)-log(det(diag(gcv_d.sliceRow(k))))/2.0 +
                                ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                0.5*a.get(n,k)/b.get(n,k)*(
                                trace(mult(inv(diag(gcv_d.sliceRow(k))),qcv[n][k]))
                                +multATBA(qmu[n][k],inv(diag(gcv_d.sliceRow(k)))).asScalar()
                                -2*multBt(multAt(qmu[n][k],inv(diag(gcv_d.sliceRow(k)))),gmu.sliceRowM(k)).asScalar()
                                + multABAT(gmu.sliceRowM(k),inv(diag(gcv_d.sliceRow(k)))).asScalar()));
                            // -E_q[ LOG q(W|K) ]
                            aux4.set(n,k,ndim_er/2.0*log(2*PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0);
                                break;
                        case common:
                            aux1.set(n,k,-ndim_er/2.0*log(2*PI)-log(det(diag(S.sliceRow(n))))/2.0-
                                (multABAT(data_er.sliceRowM(n),inv(diag(S.sliceRow(n)))).asScalar() - 
                                2*multBt(mult(qmu[n][k], inv(diag(S.sliceRow(n)))),data_er.sliceRowM(n)).asScalar() +
                                trace(mult(inv(diag(S.sliceRow(n))),qcv[n][k])) 
                                + multABAT(qmu[n][k], inv(diag(S.sliceRow(n)))).asScalar())/2.0);
                            AGDenseMatrix tqmu = qmu[n][k];
                            Matrix diff =  sub(tqmu,gmu.sliceRowM(k));
                            tqmu = (AGDenseMatrix) multBt(diff, diff);
                            aux2.set(n,k,-ndim_er/2.0*log(2*PI)-ndim_er*log(gcv_c.get(k))/2.0 +
                                ndim_er/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-0.5*a.get(n,k)
                                /b.get(n,k)*(tqmu.asScalar()+trace(  
                                qcv[n][k]))/gcv_c.get(k));
                            // -E_q[ LOG q(W|K) ]
                            aux4.set(n,k,ndim_er/2.0*log(2*PI)+log(det(qcv[n][k]))/2.0+ndim_er/2.0);
                            break;
                        default:
                            throw new IllegalArgumentException( "Unknown noise model");
                    }
                    // E_q [LOG(P(U|K))]
                    aux3.set(n,k,v.get(k)/2.0*log(v.get(k)/2.0)+(v.get(k)/2.0-1)*(psi(a.get(n,k))-log(b.get(n,k)))
                        -v.get(k)/2.0*a.get(n,k)/b.get(n,k)-log(gamma(v.get(k)/2.0)));
                    // -E_q[ LOG q(U|K) ]
                    aux5.set(n,k,-((a.get(n,k)-1)*psi(a.get(n,k))+log(b.get(n,k))-a.get(n,k)-
                        log(gamma(a.get(n,k)))));
                }
            }
        }
        Matrix n3 = null;
        if  (ndim_nr != 0) {
        //     disp 'calculate components of continus variable without errors for responsibilities'
            // THE SECOND PART IS RELATED TO THE DATA WITHOUT MEASUREMENT ERRORS
            switch (cv_type) {
            case free:
                n3 = dist3_free(data_nr, gmu_nr, gcv_nr_f);
                break;

            case common:
                n3 = dist3_common(data_nr, gmu_nr, gcv_nr_c);
                break;
            case diagonal:
                n3 = dist3_diag(data_nr, gmu_nr, gcv_nr_d);
                break;
            default:
                break;
            }
            
            for ( int n = 0; n < ndata; n++) {
                for ( int k = 0; k < K; k++) {
                    if  (ndim_er != 0) {
                        // E_q[ log(p(t|u, k)) ]
                        switch (cv_type) {
                            case diagonal:
                                Vector covkd = gcv_nr_d.sliceRow(k);
                                aux6.set(n,k,-ndim_nr/2.0*log(2*PI)-sum(log(covkd))/2.0+
                                    ndim_nr/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                    0.5*a.get(n,k)/b.get(n,k)*n3.get(n,k));
                                break;
                            case common:
                                double covkc = gcv_nr_c.get(k);
                                aux6.set(n,k,-ndim_nr/2.0*log(2*PI)-ndim_nr*log(covkc)/2.0+
                                    ndim_nr/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                    0.5*a.get(n,k)/b.get(n,k)*n3.get(n,k));
                                break;
                            case free:
                                Matrix tmp = multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),inv(gcv_nr_f[k]));
                                aux6.set(n,k,-ndim_nr/2.0*log(2*PI)-log(det(gcv_nr_f[k]))/2.0+
                                    ndim_nr/2.0*(psi(a.get(n,k))-log(b.get(n,k)))-
                                    0.5*a.get(n,k)/b.get(n,k)*tmp.asScalar());
                                    break;
                            default:
                                throw new IllegalArgumentException( "Unknown noise model");
                        }
                    }
                    if  (ndim_er == 0 ) {
                        switch (cv_type) {
                            case common:
                                double covkc = gcv_nr_c.get(k);
                                aux6.set(n,k, log(gamma((v.get(k)+ndim_nr)/2.0))- 
                                    ndim_nr/2.0*log(covkc)-ndim_nr/2.0*log(PI*v.get(k)) 
                                    -log(gamma(v.get(k)/2.0))-(v.get(k) + ndim_nr)/2.0* 
                                    log(1+n3.get(n,k)/(covkc*v.get(k))));
                                break;
                            case diagonal:
                                Vector covkd = gcv_nr_d.sliceRow(k);
                                double dist = sum(divide(pow(sub(data_nr.sliceRow(n),gmu_nr.sliceRow(k)),2.0) , covkd));
                                aux6.set(n,k, log(gamma((v.get(k)+ndim_nr)/2.0))-
                                    sum(log(covkd))/2.0-
                                    ndim_nr/2.0*log(PI * v.get(k))-log(gamma(v.get(k)/2.0))-
                                    (v.get(k) + ndim_nr)/2.0*log(1 + dist/v.get(k)));
                                break;
                            case free:
                                Matrix covkf = gcv_nr_f[k];
                                aux6.set(n,k, log(gamma((v.get(k)+ndim_nr)/2.0))-log(det(
                                    covkf))/2.0-ndim_nr/2.0*log(PI*v.get(k))-log(gamma(v.get(k)/2.0))- 
                                    ((v.get(k)+ndim_nr)/2.0)*log(1+multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)), inv(covkf)).asScalar()
                                    /v.get(k)));
                                    break;
                        }
                    }
                }
            }
        }

        if  (ndim_bin !=0) {
        //     disp 'calculate components of binary variable for responsibilities'
            //  THE THIRD PART IS RELATED TO THE BINARY DATA
            //  we need first calculate its mean
            for ( int i = 0; i < ndata; i++) {
                for ( int j = 0; j < K; j++) {
                    Vector sgm = add(bp.sliceRow(j),eps);
                    Vector datai = data_bin.sliceRow(i);
                    aux9.set(i,j, sum(add(times(datai,log(sgm)),times(sub(1.0,datai),log(sub(1.0,sgm))))));
                }
            }
        }

        if  (ndim_mul != 0) {
        //     disp 'calculate components of category variable for responsibilities'
            for ( int n = 0; n < ndata; n++) {
                for ( int k = 0; k < K; k++) {
                    Vector temp = add(mp.sliceRow(k),eps);
                    Vector log_sgm = log(temp);
                    Vector datat = data_mul.sliceRow(n);
                    aux10.set(n,k, sum(times(datat,log_sgm)));
                }
            }
        }

        if  (ndim_int !=0) {
        //     disp 'calculate components of int variable for responsibilities'
        //     [ndata ndim_int] = size(data_int);
            for ( int t = 0; t < ndata; t++) {
                for ( int i = 0; i < K; i++) {
                    Vector sgm = ip.sliceRow(i);
                    Vector datai = data_int.sliceRow(t);
                    double poiss = sum(sub(times(datai,log(sgm)),sgm));
                    aux11.set(t,i, poiss-sum(log(seq(ndim_int))));; //FIXME - put ndim_int instead of datai here possible mistake in original? double sum seems meaningless also
                }
            }
        }

        for ( int n = 0; n < ndata; n++) {
            for ( int k = 0; k < K; k++) {
                aux12.set(n,k, log(p.get(k)+eps));
            }
        }

        // aux = aux1+aux2+aux3+aux4+aux5+aux6+aux7+aux8+aux9+aux10+aux11;
        Matrix aux = add(add(add(add(add(add(add(add(add(aux1 , aux2) ,aux3),  aux4), aux5), aux6), aux9), aux10) , aux11), aux12);

        double loglike;
        // loglike = sum(sum(q .* aux))+sum(sum(q.*log(p+eps)))-sum(sum(q.*log(q+eps)));
        if  (ndim_er != 0) {
            loglike = sum(sum(times(q ,aux)))-sum(sum(times(q,log(add(q,eps)))));
        }
        else {
            loglike = sum(sum(times(q , aux)));
        }    
        
        return loglike;
        
    }

  

}


/*
 * $Log: ClusterBoundFull.java,v $
 * Revision 1.2  2009/09/08 19:23:30  pah
 * got rid of npe and array bound problems....
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
