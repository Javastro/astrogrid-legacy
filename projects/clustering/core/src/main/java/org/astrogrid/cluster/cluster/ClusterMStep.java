/*
 * $Id: ClusterMStep.java,v 1.2 2010/01/05 21:27:13 pah Exp $
 * 
 * Created on 21 Sep 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
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

public class ClusterMStep {
    public static class Retval {
        public final AGDenseMatrix mu;
        public final AGDenseMatrix cv;
        public final AGDenseMatrix lmu;
        public final AGDenseMatrix lcv;
        public final Vector p; 
        public Retval(AGDenseMatrix mu,AGDenseMatrix cv, AGDenseMatrix lmu, AGDenseMatrix lcv, Vector p ) {

            this.mu = mu;
            this.cv = cv;
            this.lmu = lmu;
            this.lcv = lcv;
            this.p = p;            
        }

    }

    // -------------------------------------------------------------------------
    // Re-estimate the global parameters of the model
    //function  [mu cv lmu lcv p]
    //--------------------------------------------------------------------------
    static Retval  clustering_m_step(Matrix alldata, Matrix datatype, int K, AGDenseMatrix q,
            AGDenseMatrix lcv, CovarianceKind cv_type){

        ////%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        // M-step of the VB method
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        int ndata = alldata.numRows();
        int no_of_data_types = datatype.numRows();
        Matrix data_nr = null, data_bin = null, data_mul = null, data_int = null, data_er = null;
        Vector gcv_c = new DenseVector(no_of_data_types);
        Matrix   S = null;//note that this is only initialized to something sensible in the case of data errors.
        int ndim_nr = 0, ndim_er = 0,ndim_bin = 0,ndim_mul = 0,ndim_int = 0;

        int n1 = 0, d = 0;
        Matrix qcv[][] = new AGDenseMatrix[ndata][K];//FIXME these are probably not correct
        DenseVector qmu[][] = new DenseVector[ndata][K];
        Matrix gmu_nr = null,gcv_nr[] = new AGDenseMatrix[K], gcv_nr_d = new AGDenseMatrix(K,ndim_nr), gmu = null, gcv_f[] = new AGDenseMatrix[K], gcv_d = new AGDenseMatrix(K, ndim_er);
        for ( int i = 0; i < no_of_data_types; i++){
            if(datatype.get(i,1) == 1     ){ // continuous data without errors
                ndim_nr = (int)datatype.get(i,2);
                data_nr = alldata.sliceCol(d,ndim_nr);
                d = d + ndim_nr;
            }
            else if(datatype.get(i,1) == 2 ){ // continous data with errors
                ndim_er = (int)datatype.get(i,2);
                data_er = alldata.sliceCol(d,ndim_er);
                switch(cv_type) {

                case free:
                    for ( int k = 0; k < K; k++){
                        gcv_f[k] = reshape(lcv.asVector(n1,n1+ndim_er*ndim_er-1), 
                                ndim_er,ndim_er);
                        n1 = n1 + ndim_er*ndim_er;
                    }
                    break;
                case diagonal:
                    for ( int k = 0; k < K; k++){
                        gcv_d.setRow(k,  reshape(lcv.asVector(n1,n1+ndim_er-1), 1, ndim_er));
                        n1 = n1 + ndim_er;
                    }
                    break;
                case common:
                    gcv_c = lcv.asVector(n1); 
                }
                d = d + ndim_er;
            }
            else if (datatype.get(i,1) == 3) {
                ndim_bin = (int)datatype.get(i,2);
                data_bin = alldata.sliceCol(d,ndim_bin);
                d = d  + ndim_bin;
            }
            else if (datatype.get(i,1) == 4) {
                ndim_mul =(int) datatype.get(i,2);
                data_mul = alldata.sliceCol(d,ndim_mul);
                d = d + ndim_mul;
            }
            else if (datatype.get(i,1) == 5) {
                ndim_int = (int)datatype.get(i,2);       
                data_int = alldata.sliceCol(d,ndim_int);
                d = d + ndim_int;
            }
            else if (datatype.get(i,1) == 6) {
                int ndim_error =(int) datatype.get(i,2);
                if(ndim_error != ndim_er){ //
                    throw new IllegalArgumentException( "The dimension of measurement errors and ");
                }        
                S = alldata.sliceCol(d,ndim_error);
                // error inforamtion
                S.add(1.0e-6, ones(ndata, ndim_er));
                d = d + ndim_error;
            }
        }
        // maximize for the parameters gmu, gcv for the data with measurement errors
        // [ndata ndim] = size(data);
        ////%%%%%%%%%%%%%%%%%%
        //   gmu
        //%%%%%%%%%%%%%%%%%%%
        AGDenseMatrix mu = new AGDenseMatrix(0,0) ,cv = new AGDenseMatrix(0,0), lmu = new AGDenseMatrix(0,0);
        lcv = new AGDenseMatrix(0,0);
        gmu = new AGDenseMatrix(K, ndim_er);
        if(ndim_er != 0  ){ //
            for ( int k = 0; k < K; k++){
                AGDenseMatrix tmpg = zeros(ndim_er,1);
                AGDenseMatrix tmpt = zeros(ndim_er, ndim_er);
                for ( int n = 0; n < ndata; n++){
                    switch(cv_type) {
                    case free:
                        tmpg.add(q.get(n,k),multBt(inv(add((gcv_f[k]),diag(  
                                S.sliceRow(n)))),data_er.sliceRowM(n)));
                        tmpt.add(q.get(n,k),inv(add((gcv_f[k]),diag(S.sliceRow(n)))));
                        break;
                    case diagonal:
                        tmpg.add( q.get(n,k),multBt(inv(add(diag(gcv_d.sliceRow(k)),diag(S.sliceRow(n))))
                                ,data_er.sliceRowM(n)));
                        tmpt.add(q.get(n,k),inv(add(diag(gcv_d.sliceRow(k)),diag(S.sliceRow(n)))));
                        break;
                    case common:
                        tmpg.add(q.get(n,k),multBt(inv(add(gcv_c.get(k),diag(S.sliceRow(n)))),data_er.sliceRowM(n)));
                        tmpt.add(q.get(n,k),inv(add(gcv_c.get(k),diag(S.sliceRow(n)))));
                        break;
                    }
                }
                gmu.setRow(k , multBt(inv(tmpt),tmpg));        
            }
            lmu.append( gmu.asVector());
            //%%%%%%%%%%%%%%%%%%
            //gcv
            //%%%%%%%%%%%%%%%%%%%
            for ( int k = 0; k < K; k++){
                switch(cv_type) {

                case free:
                    Matrix cvk = pow((gcv_f[k]),0.5);
                    Vector tmpcv = Minimize.minimize(cvk.asVector(), "objectiveFull", 10, q.sliceCol(k), 
                            gmu.sliceRow(k), data_er, S);
                    Matrix tmpcvm = pow(reshape(tmpcv,ndim_er,ndim_er),2.0);
                    lcv.append(tmpcvm.asVector());
                    break;
                case diagonal:

                    Vector cvkv = pow(gcv_d.sliceRow(k),0.5);               
                    tmpcv =Minimize.minimize(cvkv,"objectiveDiag",10,q.sliceCol(k),gmu.sliceRow(k),
                            data_er, S);                
                    lcv.append(pow(tmpcv,2));
                    break;

                case common:
                    Vector cvk_c = new DenseVector(new double[]{gcv_c.get(k)});
                    tmpcv = Minimize.minimize(cvk_c,"objectiveSpherical",10,q.sliceCol(k), gmu.sliceRow(k),
                            data_er, S);
                    gcv_c.set(k , pow(tmpcv.get(0),2.0));
                    lcv.append(gcv_c.get(k));
                }
            }
        }

        if(ndim_nr != 0){ 
            // maximize for the parameters gmu, gcv for the data without errors

            //%%%%%%%%%%%%%%%%%%%
            //   gmu
            //%%%%%%%%%%%%%%%%%%%%
            gmu_nr = new AGDenseMatrix(K,ndim_nr);
            for ( int k = 0; k < K; k++){
                Vector tmp = new DenseVector(ndim_nr).zero();
                for ( int n = 0; n < ndata; n++){
                    tmp.add(q.get(n,k),data_nr.sliceRow(n));
                }
                gmu_nr.setRow(k, tmp.scale(1.0/sum(q.sliceCol(k))));
            }
            mu.append( gmu_nr.asVector());
            //%%%%%%%%%%%%%%%%%%
            // gcv
            //%%%%%%%%%%%%%%%%%%
            for ( int k = 0; k < K; k++){
                switch(cv_type) {


                case free:
                    Matrix gcvt = zeros(ndim_nr, ndim_nr);
                    for ( int n = 0; n < ndata; n++){
                        Vector tmp = sub(gmu_nr.sliceRow(k),data_nr.sliceRow(n));

                        gcvt.add(q.get(n,k),vprod(tmp));
                    }
                    gcv_nr[k] = (Matrix) gcvt.scale(1.0 / sum(add(q.sliceCol(k),eps)));

                    cv.append(gcv_nr[k].asVector());
                    break;
                case diagonal:
                    for ( int i = 0; i < ndim_nr; i++){
                        double tmp = 0.0;
                        for ( int n = 0; n < ndata; n++){
                            tmp = tmp + q.get(n,k)*pow(data_nr.get(n,i)-gmu_nr.get(k,i),2.0);
                        }
                        gcv_nr_d.set(k,i, tmp/ sum(add(q.sliceCol(k),eps)));
                    }
                    cv.append(gcv_nr_d.sliceRow(k));
                    break;
                case common:
                    double tmpt = 0.0;
                    for ( int n = 0; n < ndata; n++){

                        Vector tmp = sub(data_nr.sliceRow(n), gmu_nr.sliceRow(k));
                        tmpt=tmpt+q.get(n,k)*tmp.dot(tmp);
                    }
                    tmpt = tmpt/(ndim_nr*sum(add(q.sliceCol(k),eps)));
                    if(tmpt < eps){ 
                        tmpt = eps;
                    }
                    cv.append(tmpt);

                }
            }
        }

        //--------------------------------------------------------------------------
        // the parameters for the binary data
        //--------------------------------------------------------------------------

        if(ndim_bin != 0){ //
            //     [ndata bin_dim] = size(bin_data);
            int  bin_dim = data_bin.numColumns();
            Matrix bp = new AGDenseMatrix(K,bin_dim);
            ndata = data_bin.numRows();
            for ( int k = 0; k < K; k++){
                for ( int j = 0; j < ndim_bin; j++){
                    //             trialno = 1;
                    //             bp(k,j) = sum((q.sliceCol(k).*data_bin(:,j)))/(trialno*sum(q.sliceCol(k)+eps));
                    bp.set(k,j, sum(times(q.sliceCol(k),data_bin.sliceCol(j)))/(sum(add(q.sliceCol(k),eps))));
                }
            }    
            mu.append(bp.asVector());
        }


        if(ndim_mul != 0){ //
            int dim_mul = data_mul.numColumns();
            ndata = data_mul.numRows();
            Matrix mp = new AGDenseMatrix(K,dim_mul);
            for ( int k = 0; k < K; k++){
                for ( int i = 0; i < dim_mul; i++){
                    mp.set(k,i, sum(times(q.sliceCol(k),data_mul.sliceCol(i))));
                }
                mp.setRow(k, mp.sliceRow(k).scale(1.0/sum(mp.sliceRow(k))));
            }
            mu.append(mp.asVector());
        }

        if(ndim_int != 0){ //
            int dim_int = data_int.numColumns();
            Matrix ip = new AGDenseMatrix(K,dim_int);
            for ( int k = 0; k < K; k++){
                for ( int i = 0; i < dim_int; i++){
                    ip.set(k,i , sum(times(q.sliceCol(k),data_int.sliceCol(i)))/sum(q.sliceCol(k)));
                }
            }
            mu.append(ip.asVector());
        }

        //%%%%%%%%%%%%%%%%%
        // priors
        //%%%%%%%%%%%%%%%%%
        Vector p = mean(q, 1);

        
        return new Retval(mu, cv, lmu, lcv, p);

    }

}


/*
 * $Log: ClusterMStep.java,v $
 * Revision 1.2  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
 * Revision 1.1  2009/09/22 07:04:16  pah
 * daily checkin
 *
 */
