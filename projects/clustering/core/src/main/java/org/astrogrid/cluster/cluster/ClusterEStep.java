/*
 * $Id: ClusterEStep.java,v 1.2 2010/01/05 21:27:13 pah Exp $
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
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.cluster.cluster.ClusterEStepFull.Retval;
import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;

public class ClusterEStep {

 	
	// this is the E-step of the variational EM algorithm for data without
	// considering outliers
	// the data set, some variables are continuous with measurement errors, some
	// variables are continous without measurement errors, some variables are
	// binary, in the parameters, these data are seperated as data, data_noerr,
	// bin_data, S is the error associated
	public static AGDenseMatrix  clustering_e_step(Matrix data, Matrix datatype, int K, Matrix mu, Matrix cv, Matrix lmu, Matrix  lcv, Vector p,
	        CovarianceKind cv_type){
	    
	

	int ndata = data.numRows(),  ndimall = data.numColumns();
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	// E-step of the VB method
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	int no_of_data_types = datatype.numRows();
	
	    int n0 = 0, n1 = 0, nm = 0, ne = 0, d = 0;
	    Matrix data_nr = null, data_er = null, data_bin = null, data_mul = null, data_int = null;
	    Matrix gmu_nr = null,gcv_nr_f[] = new AGDenseMatrix[K], gcv_nr_d = null,  gmu = null, gcv_f[] = new AGDenseMatrix[K], gcv_d = null, S = null;
	    Vector  gcv_nr_c= null, gcv_c = null;
	    Matrix bp = null, mp = null, ip = null;
	    AGDenseMatrix C = new AGDenseMatrix(ndata, K);
	    int ndim_nr = 0, ndim_er = 0,ndim_bin=0,ndim_mul = 0,ndim_int = 0, ndim_error = 0;
	    
	 	for ( int i = 0; i < no_of_data_types; i++){
	    if(datatype.get(i,0) == 1     ){ // continuous data without errors
	        if((ndim_nr = (int) datatype.get(i,1))>0){
	        data_nr = data.sliceCol( d, ndim_nr);
	        gmu_nr = reshape(mu.asVector(nm,nm+K*ndim_nr-1), K, ndim_nr);
	        nm = nm + K*ndim_nr;
	        switch(cv_type) {
	            case free:
	                for ( int k = 0; k < K; k++){
	                    gcv_nr_f[k] =reshape(cv.asVector(n0,n0+ndim_nr*ndim_nr -1), 
	                        ndim_nr, ndim_nr);
	                    n0 = n0+ndim_nr*ndim_nr;
	                }
	                break;
	            case diagonal:
	               
	                    gcv_nr_d = reshape(cv.asVector(n0,n0+K*ndim_nr -1), K, ndim_nr);
	                    n0 = n0 + K*ndim_nr;
	               
	                break;
	            case common:
	                gcv_nr_c = cv.asVector(n0);
	                break;
	        }
	        }
	        d = d + ndim_nr;
	    }
	    else if(datatype.get(i,0) == 2 ){ // continous data with errors
	        if((ndim_er = (int) datatype.get(i,1))>0){
	        data_er = data.sliceCol(d, ndim_er);
	        gmu = reshape(lmu.asVector(ne,ne+K*ndim_er-1), K, ndim_er);
	        switch(cv_type) {
	            case free:
	                for ( int k = 0; k < K; k++){
	                    gcv_f[k] = reshape(lcv.asVector(n1,n1+ndim_er*ndim_er-1), 
	                        ndim_er,ndim_er);
	                    n1 = n1 + ndim_er*ndim_er;
	                }
	                break;
	            case diagonal:
	              
	                    gcv_d = reshape(lcv.asVector(n1,n1+K*ndim_er-1), K, ndim_er);
	                    n1 = n1 + ndim_er;
	               
	                break;
	            case common:
	                gcv_c = lcv.asVector(n1); 
	                break;
	        }
	        }
	        d = d + ndim_er;
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
	        data_mul = data.sliceCol(d,ndim_mul);
	        mp = reshape(mu.asVector(nm,nm+K*ndim_mul-1), K, ndim_mul);
	        nm = nm + K*ndim_mul;
	        d = d + ndim_mul;
	    }
	    else if (datatype.get(i,0) == 5){
	        ndim_int = (int) datatype.get(i,1);
	        data_int = data.sliceCol(d,ndim_int);
	        ip = reshape(mu.asVector(nm,nm+K*ndim_int -1), K, ndim_int);
	        nm = nm + K*ndim_int;
	        d = d + ndim_int;
	    }
	    else if (datatype.get(i,0) == 6){
	        ndim_error = (int) datatype.get(i,1);
	        if(ndim_error != ndim_er){ //
	            throw new IllegalArgumentException("The dimension of measurement errors and ");
	        }        
	        S = data.sliceCol(d,ndim_error);
	        // error inforamtion
	        S.add(1.0e-6,ones(ndata, ndim_er));
	        d = d + ndim_error;
	    }
	}

	//--------------------------------------------------------------------------
	// THE POSTERIOR OF THE LATENT VARIABLES INCLUDING U AND W FOR THE VARIABLES
	// WITH MEASUREMENT ERRORS
	//--------------------------------------------------------------------------
	AGDenseMatrix aux1 = zeros(ndata, K);
	AGDenseMatrix aux2 = zeros(ndata, K);
	AGDenseMatrix aux3 = zeros(ndata, K);
	AGDenseMatrix aux4 = zeros(ndata, K);
	AGDenseMatrix aux5 = zeros(ndata, K);
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
	                    aux1.set(n,k,-ndim_er/2*log(2*PI)-sum(log(covk))/2-1/2*
	                        multABAT(sub(data_er.sliceRowM(n),gmu.sliceRowM(k)),inv(diag(covk))).asScalar());
	                        break;
	                case common:
	                    covk = add(gcv_c.get(k),S.sliceRow(n));
	                    aux1.set(n,k,-ndim_er/2*log(2*PI)-sum(log(covk))/2-1/2*
	                        multABAT(sub(data_er.sliceRowM(n),gmu.sliceRowM(k)),inv(diag(covk))).asScalar());
	                        break;
	                default:
	                    throw new IllegalArgumentException( "Unknown cv_type model");
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
	                    Vector covkd = gcv_nr_d.sliceRow(k);
	                    aux2.set(n,k,-ndim_nr/2*log(2*PI)-sum(log(covkd))/2-
	                        1/2*multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),inv(diag(covkd))).asScalar());
	                        break;
	                case common:
	                    double covk = gcv_nr_c.get(k);
	                    aux2.set(n,k,-ndim_nr/2*log(2*PI)-ndim_nr*log(covk)/2 
	                        -1/2*sub(data_nr.sliceRow(n),gmu_nr.sliceRow(k)).dot(sub(data_nr.sliceRow(n),gmu_nr.sliceRow(k)))/covk);
	                        break;
	                case free:
	                    aux2.set(n,k,-ndim_nr/2*log(2*PI)-log(det((gcv_nr_f[k]
	                        )))/2-1/2*multABAT(sub(data_nr.sliceRowM(n),gmu_nr.sliceRowM(k)),
	                        inv((gcv_nr_f[k]))).asScalar());
	                        break;
	                default:
	                throw new IllegalArgumentException("Unknown noise model");
	            }
	        }
	    }
	}
	    Vector sgm;
	    Vector datai;

	if(ndim_bin !=0){ //
	//     disp 'calculate components of binary variable for responsibilities'
	    //  THE THIRD PART IS RELATED TO THE BINARY DATA
	    //  we need first calculate its mean
	    for ( int i = 1; i < ndata; i++) {
	        for ( int j = 1; j < K; j++) {
	            sgm = add(bp.sliceRow(j),eps);
	            datai = data_bin.sliceRow(i);
	//             tmp1 = 0;
	//             trialno = 1;
	//             for k = 1:ndim_bin                
	//                 dif = trialno - datai(k);
	//                 tmp1 = tmp1 + sum(log(1:datai(k)))+sum(log(1:dif));
	//             }
	            aux3.set(i,j, sum(add(times(datai,log(sgm)),times(sub(1.0,datai),log(sub(1.0,sgm))))));
	//             aux3(i,j)=sum(sort(datai.*log(sgm)+(1.0-datai).*log(1.0-sgm)))
	//                 -tmp1;
	}
	    }
 }

	if(ndim_mul != 0){ //
	//     disp 'calculate components of category variable for responsibilities'
	//     pause    
	//     [ndata ndim_mul] = size(data_mul);
	    for ( int n = 1; n < ndata; n++) {
	        for ( int k = 1; k < K; k++) {
	            Vector temp = add(mp.sliceRow(k),eps);
	            Vector log_sgm = log(temp);
	            Vector datat = data_mul.sliceRow(n);
	            aux4.set(n,k, sum(times(datat,log_sgm) ));
	        }
	    }
 }

	if(ndim_int !=0){ //
	//     disp 'calculate components of int variable for responsibilities'
	//     [ndata ndim_int] = size(data_int);
	    for ( int t = 1; t < ndata; t++) {
	        for ( int i = 1; i < K; i++) {
	            sgm = ip.sliceRow(i);
	            datai = data_int.sliceRow(t);
	            double poiss = sum(sub(times(datai,log(sgm)),sgm));
	            aux5.set(t,i, poiss-(sum(log(seq(ndim_int)))));//FIXME was log(1:datai) what does that do?
	        }
	    }
 }


	Matrix aux = add(add(add(add(aux1 , aux2) , aux3) , aux4) , aux5);

	AGDenseMatrix q = (AGDenseMatrix) times(multBt(ones(ndata, 1),new AGDenseMatrix(p)),exp(aux));
	Matrix s =new AGDenseMatrix( sum(q, 2));
	// Set any zeros to one before dividing
	   for (MatrixEntry matrixEntry : s) {
	        if(matrixEntry.get() == 0.0)
	        {
	            matrixEntry.set(1.0);
	        }
	    }
	 q = (AGDenseMatrix) divide(q,mult(s,ones(1, K)));

	return q;
	// q
	// pause
	}
	
}


/*
 * $Log: ClusterEStep.java,v $
 * Revision 1.2  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
 * Revision 1.1  2009/09/22 07:04:16  pah
 * daily checkin
 *
 */
