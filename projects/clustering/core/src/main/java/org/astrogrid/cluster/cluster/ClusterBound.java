/*
 * $Id: ClusterBound.java,v 1.2 2010/01/05 21:27:13 pah Exp $
 * 
 * Created on 21 Sep 2009 by Paul Harrison (pharriso@eso.org)
 * Copyright 2009 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
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
import static java.lang.Math.*;

public class ClusterBound {

	// this is the E-step of the variational EM algorithm for Louisa's data. In
	// the data set, some variables are continuous with measurement errors, some
	// variables are continous without measurement errors, some variables are
	// binary, in the parameters, these data are seperated as data, data_noerr,
	// bin_data, S is the error associated
	// function loglike = clustering_bound(alldata, datatype, K, mu, cv, lmu, 
	//     lcv, p, q, cv_type, trialno)
	public static double clustering_bound(Matrix alldata, Matrix datatype, int K, AGDenseMatrix mu, AGDenseMatrix cv, AGDenseMatrix lmu, 
	        AGDenseMatrix lcv, Vector p, AGDenseMatrix q, CovarianceKind cv_type){
	// the first line of the input parameters are known data
	// the second line of the input parameters are the global parameters
	// the third line is the parameters for the latent variables
	// bp is the parameters for the binary data which we assume it follows a
	// binomial distribution

	//  for the data with no errors, we assume a mixture of student
	// t-distribution, while for 
	int ndata = alldata.numRows(), alldim=alldata.numColumns();
	    
	int no_of_data_types = datatype.numRows();
	       int n0 = 0, n1 = 0, d = 0, nm = 0, ne = 0, ini = 0;
	        Matrix data_nr = null, data_er = null, data_bin = null, data_mul = null, data_int = null;
	        AGDenseMatrix qcv[][] = new AGDenseMatrix[ndata][K];
	        DenseVector qmu[][] = new DenseVector[ndata][K];
	        int ndim_nr = 0, ndim_er = 0,ndim_bin = 0,ndim_mul= 0,ndim_int =0;
	        Matrix gmu_nr = null, gcv_nr_f[] = new AGDenseMatrix[K], gcv_nr_d = null, gmu = null;
	        Vector gcv_nr_c = null;
	        Matrix bp = null, mp = null, ip = null;
	        Matrix S = null;
	        AGDenseMatrix gcv_f[] = new AGDenseMatrix[K], gcv_d = null;
	        Vector gcv_c = null;

	for ( int i = 0; i < no_of_data_types; i++){
	    if(datatype.get(i,1) == 1     ){ // continuous data without errors
	        ndim_nr = (int)datatype.get(i,2);
	        data_nr = alldata.sliceCol(d,ndim_nr);
	        gmu_nr = reshape(mu.asVector(nm, nm+K*ndim_nr -1), K, ndim_nr);
	        nm = nm + K*ndim_nr;
	        switch(cv_type) {
	            case free:
	                for ( int k = 0; k < K; k++){
	                    gcv_nr_f[k] = reshape(cv.asVector(n0, n0+ndim_nr*ndim_nr -1), 
	                        ndim_nr, ndim_nr);
	                    n0 = n0+ndim_nr*ndim_nr;
	                }
	                break;
	            case diagonal:
	                
	                    gcv_nr_d = reshape(cv.asVector(n0, n0+K*ndim_nr -1), K, ndim_nr);
	                    n0 = n0 + ndim_nr;
	                break;
	            case common:
	                gcv_nr_c = cv.asVector(n0);
                        break;
	        }
	        d = d + ndim_nr;
	    }
	    else if(datatype.get(i,1) == 2 ){ // continous data with errors
	        ndim_er = (int)datatype.get(i,2);
	        data_er = alldata.sliceCol(d, ndim_er);
                gmu = reshape(lmu.asVector(ne, ne+K*ndim_er -1),K,ndim_er);
	        switch(cv_type) {
	            case free:
	                for ( int k = 0; k < K; k++){
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
	                gcv_c =  lcv.asVector(n1);
	                break;
	        }
	        d = d + ndim_er;
	    }
	    else if (datatype.get(i,1) == 3 ) { 
	        ndim_bin = (int) datatype.get(i,2);
	        data_bin = alldata.sliceCol(d, ndim_bin);
                bp = reshape(mu.asVector(nm, nm+K*ndim_bin -1), K, ndim_bin);
	        nm = nm + K*ndim_bin;
	        d = d  + ndim_bin;
	    }
	    else if (datatype.get(i,1) == 4 ) { 
	        ndim_mul = (int) datatype.get(i,2);
	        data_mul = alldata.sliceCol(d, ndim_mul );
                mp = reshape(mu.asVector(nm, nm+K*ndim_mul -1), K, ndim_mul);
	        nm = nm + K*ndim_mul;
	        d = d + ndim_mul;
	    }
	    else if (datatype.get(i,1) == 5 ) { 
	        ndim_int = (int)datatype.get(i,2);       
	        data_int = alldata.sliceCol(d, ndim_int );
                ip = reshape(mu.asVector(nm, nm+K*ndim_int -1), K, ndim_int);
	        nm = nm + K*ndim_int;
	        d  = d + ndim_int;
	    }
	    else if (datatype.get(i,1) == 6 ) { 
	        int ndim_error = (int) datatype.get(i,2);
	        if(ndim_error != ndim_er){ //
	            throw new IllegalArgumentException( "The dimension of measurement errors and ");
	        }        
	        S = alldata.sliceCol(d, ndim_error );
	        // error inforamtion
	        S.add(1.0e-6,ones(ndata, ndim_er));
	        d = d + ndim_error;
	    }
	}

	//--------------------------------------------------------------------------
	// THE POSTERIOR OF THE LATENT VARIABLES INCLUDING U AND W FOR THE VARIABLES
	// WITH MEASUREMENT ERRORS
	//--------------------------------------------------------------------------
	Matrix aux1, aux2, aux3, aux4, aux5, aux6;
	aux1 = zeros(ndata, K);
	aux2 = zeros(ndata, K);
	aux3 = zeros(ndata, K);
	aux4 = zeros(ndata, K);
	aux5 = zeros(ndata, K);
	//--------------------------------------------------------------------------
	//   CALCULATE THE RESPONSIBILITIES
	//--------------------------------------------------------------------------
	if(ndim_er != 0){ //
	//     disp 'calculate components of continus variable with errors for responsibilities'
	    for ( int n = 0; n < ndata; n++){
	        for ( int k = 0; k < K; k++){
	            // E_q [LOG P(t|K)]
	            switch(cv_type) {
	                case free:
	                    Matrix gcvk = add(gcv_f[k],diag(S.sliceRow(n)));
	                    aux1.set(n,k,-ndim_er/2*log(2*PI)-log(det(gcvk))/2-1/2*
	                        multABAT(sub(data_er.sliceRowM(n),gmu.sliceRowM(k)),inv(gcvk)).asScalar());
	                        break;
	                case diagonal:
	                    Vector covk = add(gcv_d.sliceRow(k),S.sliceRow(n));
	                    aux1.set(n,k,-ndim_er/2*log(2*PI)-sum(log(covk))/2-1.0/2*
	                        multABAT(sub(data_er.sliceRowM(n),gmu.sliceRowM(k)),inv(diag(covk))).asScalar());
	                        break;
	                case common:
	                    covk = add(S.sliceRow(n),gcv_c.get(k));
	                    aux1.set(n,k,-ndim_er/2*log(2*PI)-sum(log(covk))/2-1/2*
	                    multABAT(sub(data_er.sliceRowM(n),gmu.sliceRowM(k)),inv(diag(covk))).asScalar());
	                        break;
	                default:
	                    throw new IllegalArgumentException("Unknown noise model");
	            }
	        }
	    }
	}
	if(ndim_nr != 0){ //
	//     disp 'calculate components of continus variable without errors for responsibilities'
	    // THE SECOND PART IS RELATED TO THE DATA WITHOUT MEASUREMENT ERRORS
	    //     n2 = dist2(data_nr, gmu_nr);
	    for ( int n = 0; n < ndata; n++){
	        for ( int k = 0; k < K; k++){
	            // E_q[ log(p(t|u, k)) ]
	            switch(cv_type) {
	                case diagonal:
	                    Vector covk = gcv_nr_d.sliceRow(k);
	                    aux2.set(n,k,ndim_nr/2*log(2*PI)-sum(log(covk))/2-
	                        1/2*multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),inv(diag(covk))).asScalar());
	                    break;
	                case common:
	                    double covkc = gcv_nr_c.get(k);
	                    aux2.set(n,k,-ndim_nr/2*log(2*PI)-ndim_nr*log(covkc)/2 
	                        -0.5*multBt(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k))).asScalar()/covkc);
	                    break;
	                case free:
	                    aux2.set(n,k,-ndim_nr/2*log(2*PI)-log(det(gcv_nr_f[k]))/2 
	                        -0.5*multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),
	                        inv(gcv_nr_f[k])).asScalar());
	                    break;
	                default:
	                    throw new IllegalArgumentException("unknown noise model");
	            }
	        }
	    }
	}

	if(ndim_bin !=0){ //
	//     disp 'calculate components of binary variable for responsibilities'
	    //  THE THIRD PART IS RELATED TO THE BINARY DATA
	    //  we need first calculate its mean
	    for ( int i = 1; i <ndata; i++){
	        for ( int j = 1; j <K; j++){
	            Vector sgm = add(bp.sliceRow(j),eps);
	            Vector datai = data_bin.sliceRow(i);
	//             tmp1 = 0;
	//             trialno = 1;
	//             for ( int k = 1; k <ndim_bin; k++){
	//                 dif = trialno - datai(k);
	//                 tmp1 = tmp1 + sum(log(1:datai(k)))+sum(log(1:dif));
	//             end
	//             aux3(i,j)= sum(sort(datai.*log(sgm)+(1.0-datai).*log(1.0-sgm)))-tmp1;
	            aux3.set(i,j, sum(add(times(datai,log(sgm)),times(sub(1.0,datai),log(sub(1.0,sgm))))));
	        }
	    }
	}

	if(ndim_mul != 0){ //
	//     disp 'calculate components of category variable for responsibilities'
	//     pause    
	//     [ndata ndim_mult] = size(mult_data);
	    for ( int n = 1; n <ndata; n++){
	        for ( int k = 1; k <K; k++){
	            Vector temp = add(mp.sliceRow(k),eps);
	                   Vector log_sgm = log(temp);
	                    Vector datat = data_mul.sliceRow(n);
	 	            aux4.set(n,k, sum(times(datat,log_sgm)));
	        }
	    }
	}

	if(ndim_int !=0){ //
	//     disp 'calculate components of int variable for responsibilities'
	//     [ndata ndim_int] = size(int_data);
	    for ( int t = 1; t <ndata; t++){
	        for ( int i = 1; i <K; i++){
	            Vector sgm = ip.sliceRow(i);
	            Vector datai = data_int.sliceRow(t);
	            aux5.set(t,i,sum(sub(times(datai,log(sgm)),sgm))-(sum(log(datai)))); //FIXME what does this do (1:datai)
	        }
	    }
	}

	aux6 = zeros(ndata, K);
	for ( int n = 0; n < ndata; n++){
	    for ( int k = 0; k < K; k++){
	        aux6.set(n,k, log(p.get(k)));
	    }
	}


	// aux = aux1+aux2+aux3+aux4+aux5+aux6+aux7+aux8+aux9+aux10+aux11;
	Matrix aux = add(add(add(add(add(aux1,aux2),aux3),aux4),aux5),aux6);

	// loglike = sum(sum(q .* aux))+sum(log(p+eps).*sum(q));
	// loglike = sum(sum(q .* aux))+sum(log(p+eps).*sum(q));
	double loglike = sum(sum(times(q,aux)));

	return loglike;
	}
}
