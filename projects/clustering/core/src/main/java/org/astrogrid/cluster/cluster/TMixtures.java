/*
 * $Id: TMixtures.java,v 1.2 2009/09/22 07:04:16 pah Exp $
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

import no.uib.cipr.matrix.AGDenseMatrix;

public class TMixtures {

    
    function [bestk, bestpp, bestmu, bestcov, R] = tmixtures(AGDenseMatrix y, int kmin, int kmax, 
            regularize,th,covoption, v)
        //
        // Syntax:
        // [bestk,bestpp,bestmu,bestcov,dl,countf] = mdl_tmixtures(y,kmin,kmax,regularize,th,covoption)
        //
        // The mixtures of t-distributions
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
        verb=1; // verbose mode; change to zero for silent mode
        bins = 40; // number of bins for the univariate data histograms for visualization
        dl = []; // vector to store the consecutive values of the cost function
        [dimens,npoints] = size(y);

        switch (covoption){
            case 0:
                npars = (dimens + dimens*(dimens+1)/2);
                break;
                //this is for free covariance matrices
            case 1:
                npars = 2*dimens;
                break;
                //this is for diagonal covariance matrices
            case 2:
                npars = dimens;
                break;
               //this is for a common covariance matrix
                //independently of its structure)
            case 3:
                npars = dimens;
                break;
           default:
                //the default is to assume free covariances
                npars = (dimens + dimens*(dimens+1)/2);
           break;
       }
        nparsover2 = npars / 2;

        // we choose which axes to use in the plot,
        // in case of higher dimensional data (>2)
        // Change this to have other axes being shown
        axis1 = 1;
        axis2 = 2;

        // kmax is the initial number of mixture components
        k = kmax;

        // indic will contain the assignments of each data point to
        // the mixture components, as result of the E-step
        indic = zeros(k,npoints);

        // Initialization: we will initialize the means of the k components
        // with k randomly chosen data points. Randperm(n) is a MATLAB function
        // that generates random permutations of the integers from 1 to n.
        randindex = randperm(npoints);
        randindex = randindex(1:k);
        estmu = y(:,randindex);

        // the initial estimates of the mixing probabilities are set to 1/k
        estpp = (1/k)*ones(1,k);

        // here we compute the global covariance of the data
        globcov = cov(y');

        for(int i=0: i <k; i++){
            // the covariances are initialized to diagonal matrices proportional
            // to 1/10 of the mean variance along all the axes.
            // Of course, this can be changed
            estcov(:,:,i) = diag(ones(1,dimens)*max(diag(globcov/10)));
        }

        // having the initial means, covariances, and probabilities, we can
        // initialize the indicator functions following the standard EM equation
        // Notice that these are unnormalized values
        for(int i=0; i < k; i++){
            semi_indic(i,:) = t_multinorm(y,estmu(:,i),estcov(:,:,i),v);
            indic(i,:) = semi_indic(i,:)*estpp(i);
        }

        // we can use the indic variables (unnormalized) to compute the
        // loglikelihood and store it for later plotting its evolution
        // we also compute and store the number of components
        countf = 1;
        loglike(countf) = sum(log(sum(realmin+indic)));
        dlength = -loglike(countf) + (nparsover2*sum(log(estpp))) + 
            (nparsover2 + 0.5)*k*log(npoints);
        dl(countf) = dlength;
        kappas(countf) = k;

        // the transitions vectors will store the iteration
        // number at which components are killed.
        // transitions1 stores the iterations at which components are
        // killed by the M-step, while transitions2 stores the iterations
        // at which we force components to zero.
        transitions1 = [];
        transitions2 = [];

        // minimum description length seen so far, and corresponding
        // parameter estimates
        mindl = dl(countf);
        bestpp = estpp;
        bestmu = estmu;
        bestcov = estcov;
        bestk = k;
        R = indic./(realmin+kron(ones(k,1),sum(indic,1)));

        k_cont = 1;    // auxiliary variable for the outer loop
        while(k_cont){  // the outer loop will take us down from kmax to kmin components
            cont=1;        // auxiliary variable of the inner loop
            gen  = 1;
            while(cont){    // this inner loop is the component-wise EM algorithm with the
                // modified M-step that can kill components
                if ((verb==1) {) {
                    // in verbose mode, we keep displaying the minimum of the
                    // mixing probability estimates to see how close we are
                    // to killing one component
                    disp(sprintf('k = %2d,  minestpp = %0.5g', k, min(estpp)));
                }

                // we begin at component 1
                comp = 1;
                // and can only go to the last component, k.
                // Since k may change during the process, we can not use a for loop
                while (comp <= k){
                    // we start with the M step
                    // first, we compute a normalized indicator function
                    clear indic
                    clear d
                    clear u

                    for(int i=0; i < k; i++){
                        indic(i,:) = semi_indic(i,:)*estpp(i);
                    }
                    normindic = indic./(realmin+kron(ones(k,1),sum(indic,1)));

                    for (int i = 0; i<k; i++){
                        in = inv(estcov(:,:,i)+ realmin*eye(dimens));   // inv(Sigma)
                        centered = (y-estmu(:,i)*ones(1,npoints));
                        d(i,:) = sum(centered.*(in*centered));
                    }

                    u = zeros(k, npoints);
                    u = (v+dimens) ./ (v+d);
                    normu = u .* normindic;

                    // now we perform the standard M-step for mean and covariance
                    normalize = 1/sum(normu(comp,:));
                    aux = kron(normu(comp,:),ones(dimens,1)).*y;
                    estmu(:,comp)= normalize*sum(aux,2);

                    normalize1 = 1/sum(normindic(comp,:)+realmin);
                    
                    if ((covoption == 0)|(covoption == 2)) {
                        tcov = zeros(dimens, dimens);
                        for ( int j = 0: j <npoints; j++){
                            tcov = tcov + normu(comp,j)*(y(:,j)-estmu(:,comp))*(y(:,j)-estmu(:,comp))';
                        }
                        estcov(:,:,comp) = tcov * normalize1;
                    } else {
                        estcov(:,:,comp) = normalize1*diag(sum(aux.*y,2)) - diag(estmu(:,comp).^2) ;
                    }
                    

                    if (covoption == 2) {
                        comcov = zeros(dimens,dimens);
                        for (int comp2 = 0: comp2 <k; comp2++){
                            comcov = comcov + estpp(comp2)*estcov(:,:,comp2);
                        }
                        for (int comp2 = 0: comp2 <k; comp2++){
                            estcov(:,:,comp2) = comcov;
                        }
                    }
                    if (covoption == 3) {
                        comcov = zeros(dimens,dimens);
                        for (int comp2 = 0: comp2 <k; comp2++){
                            comcov = comcov + estpp(comp2)*diag(diag(estcov(:,:,comp2)));
                        }
                        for (int comp2 = 0: comp2 <k; comp2++){
                            estcov(:,:,comp2) = comcov;
                        }
                    }

                    // this is the special part of the M step that is able to
                    // kill components
                    estpp(comp) = max(sum(normindic(comp,:))-nparsover2,0)/npoints;
                    estpp = estpp/sum(estpp);

                    // this is an auxiliary variable that will be used the
                    // signal the killing of the current component being updated
                    killed = 0;

                    // we now have to do some book-keeping if the current component was killed
                    // that is, we have to rearrange the vectors and matrices that store the
                    // parameter estimates
                    if (estpp(comp)==0) {
                        killed = 1;
                        // we also register that at the current iteration a component was killed
                        transitions1 = [transitions1 countf];
                        if (comp==1) {
                            estmu = estmu(:,2:k);
                            estcov = estcov(:,:,2:k);
                            estpp = estpp(2:k);
                            semi_indic = semi_indic(2:k,:);
                        } else {
                            if (comp==k) {
                                estmu = estmu(:,1:k-1);
                                estcov = estcov(:,:,1:k-1);
                                estpp = estpp(1:k-1);
                                semi_indic = semi_indic(1:k-1,:);
                            } else {
                                estmu = [estmu(:,1:comp-1) estmu(:,comp+1:k)];
                                newcov = zeros(dimens,dimens,k-1);
                                for (int kk=0; kk <comp-1; kk++){
                                    newcov(:,:,kk) = estcov(:,:,kk);
                                }
                                for (int kk=comp: kk < k; kk++){
                                    newcov(:,:,kk-1) = estcov(:,:,kk);
                                }
                                estcov = newcov;
                                estpp = [estpp(1:comp-1) estpp(comp+1:k)];
                                semi_indic = semi_indic([1:comp-1,comp+1:k],:);
                            }
                        }

                        // since we've just killed a component, k must decrease
                        k=k-1;
                    }

                    if ((covoption == 2)|(covoption == 3)) {
                        for (int kk = 0; kk <k; kk++){
                            semi_indic(kk,:) = t_multinorm(y,estmu(:,kk),estcov(:,:,kk), v);
                        }
                    }

                    if (killed==0) {
                        // if the component was not killed, we update the corresponding
                        // indicator variables
                        semi_indic(comp,:) = t_multinorm(y,estmu(:,comp),estcov(:,:,comp),v);
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

                clear indic
                clear semi_indic
                for(int i=0; i < k; i++){
                    semi_indic(i,:) = t_multinorm(y,estmu(:,i),estcov(:,:,i),v);
                    indic(i,:) = semi_indic(i,:)*estpp(i);
                }

                if (k~=1) {
                    // if the number of surviving components is not just one, we compute
                    // the loglikelihood from the unnormalized assignment variables
                    loglike(countf) = sum(log(realmin+sum(indic)));
                } else {
                    // if it is just one component, it is even simpler
                    loglike(countf) = sum(log(realmin+indic));
                }

                // compute and store the description length and the current number of components
                dlength = -loglike(countf) + (nparsover2*sum(log(estpp))) + 
                    (nparsover2 + 0.5)*k*log(npoints);
                dl(countf) = dlength;
                kappas(countf) = k;

                // compute the change in loglikelihood to check if we should stop
                deltlike = loglike(countf) - loglike(countf-1);
                if ((verb~=0)) {
                    disp(sprintf('deltaloglike/th = %0.7g', abs(deltlike/loglike(countf-1))/th));
                }
                gen = gen + 1;
                if ((abs(deltlike/loglike(countf-1)) < th | gen >= 10)) {
                    // if the relative change in loglikelihood is below the threshold, we stop CEM2
                    cont=0;
                }
            } // this end is of the inner loop: "while(cont)"

            // now check if the latest description length is the best;
            // if it is, we store its value and the corresponding estimates
            if (dl(countf) < mindl) {
                bestpp = estpp;
                bestmu = estmu;
                bestcov = estcov;
                bestk = k;
                mindl = dl(countf);
            }

            // at this point, we may try smaller mixtures by killing the
            // component with the smallest mixing probability and then restarting CEM2,
            // as long as k is not yet at kmin
            if (k>kmin) {
                [minp indminp] = min(estpp);
                // what follows is the book-keeping associated with removing one component
                if (indminp==1) {
                    estmu = estmu(:,2:k);
                    estcov = estcov(:,:,2:k);
                    estpp = estpp(2:k);
                } else {
                    if (indminp==k) {
                        estmu = estmu(:,1:k-1);
                        estcov = estcov(:,:,1:k-1);
                        estpp = estpp(1:k-1);
                    } else {
                        estmu = [estmu(:,1:indminp-1) estmu(:,indminp+1:k)];
                        newcov = zeros(dimens,dimens,k-1);
                        for kk=1:indminp-1
                            newcov(:,:,kk) = estcov(:,:,kk);
                        }
                        for kk=indminp+1:k
                            newcov(:,:,kk-1) = estcov(:,:,kk);
                        }
                        estcov = newcov;
                        estpp = [estpp(1:indminp-1) estpp(indminp+1:k)];
                    }
                }
                k=k-1;

                // we renormalize the mixing probabilities after killing the component
                estpp = estpp/sum(estpp);

                // and register the fact that we have forced one component to zero
                transitions2 = [transitions2 countf];

                //increment the iterations counter
                countf = countf+1

                // and compute the loglikelihhod function and the description length
                clear indic
                clear semi_indic
                for(int i=0; i < k; i++){
                    semi_indic(i,:) = t_multinorm(y,estmu(:,i),estcov(:,:,i),v);
                    indic(i,:) = semi_indic(i,:)*estpp(i);
                }
                if (k~=1) {
                    loglike(countf) = sum(log(realmin+sum(indic)));
                } else {
                    loglike(countf) = sum(log(realmin+indic));
                }
                dl(countf) = -loglike(countf) + (nparsover2*sum(log(estpp))) + 
                    (nparsover2 + 0.5)*k*log(npoints);

                kappas(countf) = k;

            } else { //this else corresponds to "if k > kmin"
                //of course, if k is not larger than kmin, we must stop
                k_cont = 0;
            }
        } // this is the end of the outer loop "while(k_cont)"

        lastpp = estpp;
        lastmu = estmu;
        lastcov = estcov;
        }

        
        public static double multinorm(x,m,covar,v){
        // Evaluates a multidimensional student t- of mean m and covariance matrix covar
        // at the array of points x
        //
        //   y -- 1 x T
        [dim npoints] = size(x);
        dd = det(covar+ realmin*eye(dim));   // det(Sigma)
        in = inv(covar+ realmin*eye(dim));   // inv(Sigma)
        ff = gamma(0.5*(v+dim))/((pi*v)^(0.5*dim) * gamma(v/2) * sqrt(dd));
        centered = (x-m*ones(1,npoints));
        if( dim ~= 1){
            for(int j = 0; j <npoints; j++){
                d(j) = centered(:,j)'*in*centered(:,j);
                y(j) = ff / (  (1 + d(j)/v) ^(0.5*(v + dim))    );
            end
            //y = ff * exp(-0.5*sum(centered.*(in*centered)));
            //y = ff * ( (1.0 + sum(centered.*(in*centered)) / v) .^(-0.5*(v+dim)) );
        }else{
           y = ff * ( (1.0 + in*centered .^2 /v) .^ (-0.5*(v+dim)) );
        }
        }
        
}


/*
 * $Log: TMixtures.java,v $
 * Revision 1.2  2009/09/22 07:04:16  pah
 * daily checkin
 *
 * Revision 1.1  2009/09/17 07:03:50  pah
 * morning update
 *
 */