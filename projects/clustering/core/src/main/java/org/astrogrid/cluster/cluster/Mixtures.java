/*
 * $Id: Mixtures.java,v 1.6 2010/01/09 17:32:38 pah Exp $
 * 
 * Created on Sep 16, 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.Algorithms.*;
import static org.astrogrid.matrix.MatrixUtils.*;
import static java.lang.Math.*;

public class Mixtures {
    // function [bestk, bestpp, bestmu, bestcov, R] =

    static class Retval {
        public final int bestk;
        public final Vector bestpp;
        public final Matrix bestmu;
        public final List<Matrix> bestcov;
        public final Matrix R;
        public final List<Double> loglik;
        public Retval(int bestk, Vector bestpp, Matrix bestmu, List<Matrix> bestcov, Matrix R, List<Double> loglik) {
            this.bestk = bestk;
            this.bestpp = bestpp;
            this.bestmu = bestmu;
            this.bestcov = bestcov;
            this.R = R;
            this.loglik = loglik;
        }

    };


    public enum MixtureKind {Gaussian, TDistribution}

    static Retval mixtures4(Matrix y, int kmin, int kmax, 
            double regularize,double th, CovarianceKind covoption, MixtureKind mk, double v){
        // Syntax:
        // [bestk,bestpp,bestmu,bestcov,dl,countf] = mixtures3(y,kmin,kmax,regularize,th,covoption)
        //
        // Inputs:
        //
        // "y" is the data; for n observations in d dimensions, y must have
        // d lines and n columns.
        //
        // "kmax" is the initial (maximum) number of mixture components
        // "kmin" is the minimum number of components to be tested
        //
        // "covoption" controls the covarince matrix
        // covoption = 0 means free covariances
        // covoption = 1 means diagonal covariances
        // covoption = 2 means a common covariance for all components
        // covoption = 3 means a common diagonal covarince for all components
        //
        // "regularize" is a regularizing factor for covariance matrices; in very small samples,
        // it may be necessary to add some small quantity to the diagonal of the covariances
        //
        // "th" is a stopping threshold
        //
        // Outputs:
        //
        // "bestk" is the selected number of components
        // "bestpp" is the obtained vector of mixture probabilities
        // "bestmu" contains the estimates of the means of the components
        //          it has bestk columns by d lines
        // "bestcov" contains the estimates of the covariances of the components
        //           it is a three-dimensional (three indexes) array
        //           such that bestcov(:,:,1) is the d by d covariance of the first
        //           component and bestcov(:,:,bestk) is the covariance of the last
        //           component
        //
        // "dl" contains the successive values of the cost function
        // "countf" is the total number of iterations performed
        //
        // translated from original written by
        // Written by:
        //   Mario Figueiredo
        //   Instituto Superior Tecnico
        //   1049-001 Lisboa
        //   Portugal
        //
        //   Email: mtf@lx.it.pt
        //
        //   2000, 2001, 2002
        //
        boolean verb=true; // verbose mode; change to zero for silent mode
//        int bins = 40; // number of bins for the univariate data histograms for visualization
        List<Double> dl = new ArrayList<Double>(); // vector to store the consecutive values of the cost function
        int dimens = y.numRows(),npoints = y.numColumns();
        int npars;
        List<Matrix>  estcov=new ArrayList<Matrix>(kmax);
        Matrix estmu= new AGDenseMatrix(dimens,kmax);
        List<Double> loglike = new ArrayList<Double>();
        List<Integer> kappas = new ArrayList<Integer>();


        switch (covoption){
        case free:
            npars = (dimens + dimens*(dimens+1)/2);
            break;
            //this is for free covariance matrices
        case diagonal:
            npars = 2*dimens;
            break;
            //this is for diagonal covariance matrices
        case common:
            npars = dimens;
            break;
            //this is for a common covariance matrix
            //independently of its structure)
        case commondiag:
            npars = dimens;
            break;
        default:
            //the default is to assume free covariances
            npars = (dimens + dimens*(dimens+1)/2);
            break;
        }
        double nparsover2 = npars / 2.0;
        // % we choose which axes to use in the plot,
        // % in case of higher dimensional data (>2)
        // % Change this to have other axes being shown
        // axis1 = 1;
        // axis2 = 2;
        // kmax is the initial number of mixture components
        int k = kmax;
        // indic will contain the assignments of each data point to
        // the mixture components, as result of the E-step
        Matrix indic = new AGDenseMatrix(k,npoints);
        Matrix normindic = new AGDenseMatrix(k,npoints);
        Matrix semi_indic = new AGDenseMatrix(k,npoints);
        // Initialization: we will initialize the means of the k components
        // with k randomly chosen data points. Randperm(n) is a MATLAB function
        // that generates random permutations of the integers from 1 to n.
        int[] randindex = randperm(npoints)
            ;

        for (int i= 0; i < k; i++){
            estmu.setColumn(i, y.sliceCol(randindex[i]-1));
        }
        // the initial estimates of the mixing probabilities are set to 1/k
        DenseVector estpp =  (DenseVector) ones(1,k).asVector().scale(1.0/k);
        // here we compute the global covariance of the data
        Matrix globcov = cov(transpose(y));

        for (int i = 0; i < k; i++) {
            // the covariances are initialized to diagonal matrices proportional
            // to 1/10 of the mean variance along all the axes.
            // Of course, this can be changed
            estcov.add(i,diag(ones(1,dimens).asVector().scale(max(diag(times(globcov,1/10.0))))));
        }
        // having the initial means, covariances, and probabilities, we can
        // initialize the indicator functions following the standard EM equation
        // Notice that these are unnormalized values
        for (int i = 0; i < k; i++) {
            switch(mk){
            case Gaussian:
                semi_indic.setRow(i,  multinorm(y,estmu.sliceCol(i),estcov.get(i)));
                break;
            case TDistribution:
                semi_indic.setRow(i,  t_multinorm(y,estmu.sliceCol(i),estcov.get(i),v));
                break;
            }
            indic.setRow(i,times(semi_indic.sliceRow(i),estpp.get(i)));
        }
        // we can use the indic variables (unnormalized) to compute the
        // loglikelihood and store it for later plotting its evolution
        // we also compute and store the number of components
        int countf = 0;

        double lltmp=sum(log(sum(add(Double.MIN_VALUE,indic))));

        loglike.add(lltmp);

        double dlength = -loglike.get(countf) + (nparsover2*sum(log(estpp))) + 
        (nparsover2 + 0.5)*k*log(npoints);
        dl.add(countf, dlength);
        kappas.add(k);
        // the transitions vectors will store the iteration
        // number at which components are killed.
        // transitions1 stores the iterations at which components are
        // killed by the M-step, while transitions2 stores the iterations
        // at which we force components to zero.
        List<Integer> transitions1 = new ArrayList<Integer>();
        List<Integer> transitions2 = new ArrayList<Integer>();
        // minimum description length seen so far, and corresponding
        // parameter estimates
        double mindl = dl.get(countf);
        Vector bestpp = new DenseVector(estpp);
        Matrix bestmu = new AGDenseMatrix(estmu);
        List<Matrix> bestcov = new ArrayList<Matrix>(estcov) ;//IMPL deeper copy needed?
        int bestk = k;
        Matrix R = divide( indic,(add(Double.MIN_VALUE,repmatt(sum(indic,1),k,1))));

        boolean k_cont = true;   // auxiliary variable for the outer loop
        while(k_cont){ // the outer loop will take us down from kmax to kmin comp.
            boolean cont=true;        // auxiliary variable of the inner loop
            int gen = 0;
            while(cont){    // this inner loop is the component-wise EM algorithm 
                // with the modified M-step that can kill components
                if (verb) {
                    // in verbose mode, we keep displaying the minimum of the
                    // mixing probability estimates to see how close we are
                    // to killing one component
                                 Util.disp(String.format("k = %2d,  minestpp = %10.5f", k, estpp.get(min(estpp))));
                }
                // we begin at component 1
                int comp = 0;
                // and can only go to the last component, k.
                // Since k may change during the process, we can not use a for loop
                while (comp < k ) {
                    // we start with the M step
                    // first, we compute a normalized indicator function
                    indic = new AGDenseMatrix(k,npoints);
                    for (int i = 0; i < k; i++) {
                        indic.setRow(i, times(semi_indic.sliceRow(i),estpp.get(i)));
                    }

                    normindic = divide(indic,(add(Double.MIN_VALUE,repmatt(sum(indic,1),k,1))));
                    Matrix normu = null;
                    if(mk == MixtureKind.TDistribution){
                        Matrix d = new AGDenseMatrix(k,npoints);
                        for (int i =0; i<k; ++i){
                            Matrix in = inv(add(estcov.get(i), eye(dimens,Double.MIN_VALUE)));   // inv(Sigma)
                            Matrix centered = sub(y,mult((DenseVector)estmu.sliceCol(i),ones(1,npoints)));
                            d.setRow(i, sum(times(centered,mult(in,centered))));
                        }

                        Matrix u = (Matrix) divide(ones(k, npoints), d.add(v)).scale(v+dimens);

                        normu = times(u ,normindic);

                    }

                    // now we perform the standard M-step for mean and covariance

                    double normalize = 1.0/sum(add(normindic.sliceRow(comp),Double.MIN_VALUE));
                    Matrix aux = null;
                    switch(mk){
                    case Gaussian:
                        aux = times(repmatt(normindic.sliceRow(comp),dimens,1),y);
                        estmu.setColumn(comp, times(normalize,sum(aux,2)));
                        break;
                    case TDistribution:
                        double normalize1 = 1.0/sum(normu.sliceRow(comp));
                        aux = times(repmatt(normu.sliceRow(comp),dimens,1),y);
                        estmu.setColumn(comp, sum(aux,2).scale(normalize1));
                        break;
                    }
                    if ((covoption == CovarianceKind.free)||(covoption == CovarianceKind.common)) {
                        switch(mk){
                        case Gaussian:
                            estcov.set(comp, add( sub( multBt(aux,y).scale(normalize) , vprod(estmu.sliceCol(comp)))
                                    , eye(dimens,regularize)));
                            break;
                        case TDistribution:
                            Matrix tcov = zeros(dimens, dimens);
                            for (int j = 0; j < npoints; j++){
                                tcov.add(normu.get(comp,j),vprod(sub(y.sliceCol(j),estmu.sliceCol(comp))));
                            }
                            estcov.set(comp, (Matrix) tcov.scale(normalize));

                            break;
                        }
                    } else {
                        estcov.set(comp,  sub( diag(sum(times(aux,y),2)).scale(normalize) ,    
                                diag(pow(estmu.sliceCol(comp),2.0)))) ;
                    }
                    if (covoption == CovarianceKind.common) {
                        AGDenseMatrix comcov = zeros(dimens,dimens);
                        for ( int comp2 = 0; comp2 <k; comp2++){
                            comcov.add(times(estpp.get(comp2),estcov.get(comp2)));
                        }
                        for ( int comp2 = 0; comp2 <k; comp2++){
                            estcov.set(comp2, comcov);
                        }
                    }
                    if (covoption == CovarianceKind.commondiag) {
                        AGDenseMatrix comcov = zeros(dimens,dimens);
                        for ( int comp2 = 0; comp2 <k; comp2++){
                            comcov.add(times(estpp.get(comp2),diag(diag(estcov.get(
                                    comp2)))));
                        }
                        for ( int comp2 = 0; comp2 <k; comp2++){
                            estcov.set(comp2,comcov);
                        }
                    }
                    // this is the special part of the M step that is able to
                    // kill components
                    estpp.set(comp, max(sum(normindic.sliceRow(comp))-nparsover2,0)/npoints);
                    estpp = estpp.scale(1.0/sum(add(estpp,Double.MIN_VALUE)));
                    // this is an auxiliary variable that will be used the
                    // signal the killing of the current component being updated
                    boolean killed = false;
                    // we now have to do some book-keeping if the current component was killed
                    // that is, we have to rearrange the vectors and matrices that store the
                    // parameter estimates
                    if (estpp.get(comp)==0) {
                        killed = true;
                        // we also register that at the current iteration a component was killed
                        transitions1.add(countf);

                        estmu = estmu.delCol(comp);
                        estcov.remove(comp);
                        estpp = delElement(estpp,comp);
                        semi_indic = semi_indic.delRow(comp);

                        // since we've just killed a component, k must decrease
                        k=k-1;
                    }

                    if ((covoption == CovarianceKind.common)|(covoption == CovarianceKind.commondiag)) {
                        for (int kk = 0; kk <k; kk++){
                            switch(mk){
                            case Gaussian:
                                semi_indic.setRow(kk, multinorm(y,estmu.sliceCol(kk),estcov.get(kk)));
                                break;
                            case TDistribution:
                                semi_indic.setRow(kk, t_multinorm(y,estmu.sliceCol(kk),estcov.get(kk),v));
                                break;
                            }
                        }
                    }

                    if (!killed) {
                        // if the component was not killed, we update the corresponding
                        // indicator variables
                        switch(mk){
                        case Gaussian:
                            semi_indic.setRow(comp, multinorm(y,estmu.sliceCol(comp),estcov.get(comp)));
                            break;
                        case TDistribution:
                            semi_indic.setRow(comp, t_multinorm(y,estmu.sliceCol(comp),estcov.get(comp),1.0));
                            break;
                        }
                        // and go on to the next component
                        comp = comp + 1;
                    }
                    // if killed==1, it means the in the position "comp", we now
                    // have a component that was not yet visited in this sweep, and
                    // so all we have to do is go back to the M setp without
                    // increasing "comp"

                } // this is the end of the innermost "while comp <= k" loop
                // which cycles through the components
                // increment the iterations counter
                countf = countf + 1;

                indic = new AGDenseMatrix(k,npoints);
                semi_indic = new AGDenseMatrix(k,npoints);
                for (int i = 0; i < k; i++) {
                    switch(mk){
                    case Gaussian:
                        semi_indic.setRow(i, multinorm(y,estmu.sliceCol(i),estcov.get(i)));
                        break;
                    case TDistribution:
                        semi_indic.setRow(i, t_multinorm(y,estmu.sliceCol(i),estcov.get(i),v));
                        break;
                    }
                    indic.setRow(i, times(semi_indic.sliceRow(i),estpp.get(i)));
                }

                //                if (k!=1) {
                // if the number of surviving components is not just one, we compute
                // the loglikelihood from the unnormalized assignment variables
                loglike.add( sum(log(add(Double.MIN_VALUE,sum(indic)))));
                //                } else {
                // if it is just one component, it is even simpler
                //                    loglike.set(countf, sum(log(add(Double.MIN_VALUE,indic))));
                //                }
                // compute and store the description length and the current number 
                // of components
                dlength = -loglike.get(countf) + (nparsover2*sum(log(estpp))) + 
                (nparsover2 + 0.5)*k*log(npoints);
                dl.add(dlength);
                kappas.add(k);
                // compute the change in loglikelihood to check if we should stop
                double deltlike = loglike.get(countf) - loglike.get(countf-1);
                if ((verb)) {
                                 Util.disp(String.format("deltaloglike/th = %14.7g", abs(deltlike/loglike.get(countf-1))/th));
                }

                if ((abs(deltlike/loglike.get(countf-1)) < th)|| gen++>10) {
                    // if the relative change in loglikelihood is below the 
                    // threshold, we stop CEM2
                    cont=false;
                }

            } // this end is of the inner loop: "while(cont)"
            // now check if the latest description length is the best;
            // if it is, we store its value and the corresponding estimates
            if (dl.get(countf) < mindl) {

                bestpp = new DenseVector(estpp);
                bestmu = new AGDenseMatrix(estmu);
                bestcov = new ArrayList<Matrix>(estcov);//IMPL need deep copy?
                bestk = k;
                R   = new AGDenseMatrix(normindic);
                mindl = dl.get(countf);
            }
            // at this point, we may try smaller mixtures by killing the
            // component with the smallest mixing probability and then restarting CEM2,
            // as long as k is not yet at kmin
            if (k>kmin) {
                int indminp = min(estpp);
                double minp = estpp.get(indminp);
                // what follows is the book-keeping associated with removing one component

                estmu = estmu.delCol(indminp);
                estcov.remove(indminp);
                estpp=delElement(estpp, indminp);

                k=k-1;
                // we renormalize the mixing probabilities after killing the component
                estpp = estpp.scale(1.0/sum(estpp));
                // and register the fact that we have forced one component to zero
                transitions2.add(countf);
                //increment the iterations counter
                countf = countf+1;
                // and compute the loglikelihhod function and the description length
                indic = new AGDenseMatrix(k,npoints);
                semi_indic = new AGDenseMatrix(k,npoints);
                for (int i = 0; i < k; i++) {
                    switch(mk){
                    case Gaussian:
                        semi_indic.setRow(i , multinorm(y,estmu.sliceCol(i),estcov.get(i)));
                        break;
                    case TDistribution:
                        semi_indic.setRow(i , multinorm(y,estmu.sliceCol(i),estcov.get(i)));
                        break;
                    }
                    indic.setRow(i, times(semi_indic.sliceRow(i),estpp.get(i)));
                }

                loglike.add(sum(log(add(Double.MIN_VALUE,sum(indic)))));
                dl.add( -loglike.get(countf) + (nparsover2*sum(log(estpp))) + 
                        (nparsover2 + 0.5)*k*log(npoints));


                kappas.add(k);

            } else { //this else corresponds to "if k > kmin"
                //of course, if k is not larger than kmin, we must stop
                k_cont = false;
            }

        } // this is the end of the outer loop "while(k_cont)"

        return new Retval(bestk, bestpp, bestmu, bestcov, R, dl);
    }


}


/*
 * $Log: Mixtures.java,v $
 * Revision 1.6  2010/01/09 17:32:38  pah
 * make initial mu random again.
 *
 * Revision 1.5  2010/01/09 17:30:44  pah
 * behaving as matlab version
 *
 * Revision 1.4  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
 * Revision 1.3  2009/09/22 07:04:16  pah
 * daily checkin
 *
 * Revision 1.2  2009/09/17 14:13:12  pah
 * evening commit
 *
 * Revision 1.1  2009/09/17 07:03:49  pah
 * morning update
 *
 */
