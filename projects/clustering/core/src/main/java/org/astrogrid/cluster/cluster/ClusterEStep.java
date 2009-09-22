/*
 * $Id: ClusterEStep.java,v 1.1 2009/09/22 07:04:16 pah Exp $
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

public class ClusterEStep {

	
	// this is the E-step of the variational EM algorithm for data without
	// considering outliers
	// the data set, some variables are continuous with measurement errors, some
	// variables are continous without measurement errors, some variables are
	// binary, in the parameters, these data are seperated as data, data_noerr,
	// bin_data, S is the error associated
	function q = clustering_e_step(data, datatype, K, mu, cv, lmu, lcv,p,...
	    cv_type)

	[ndata ndimall] = size(data);
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	// E-step of the VB method
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	no_of_data_types = length(datatype);
	
	n0 = 0; n1 = 0; d = 0; nm = 0; ne = 0;
	for ( int i = 0; i < no_of_data_types; i++){
	    if(datatype(i,1) == 1     ){ % continuous data without errors
	        ndim_nr = datatype(i,2);
	        data_nr = data(:, d+1:d+ndim_nr);
	        gmu_nr = reshape(mu(nm+1:nm+K*ndim_nr), K, ndim_nr);
	        nm = nm + K*ndim_nr;
	        switch(noise) {
	            case 'full'
	                for ( int k = 0; k < K; k++){
	                    gcv_nr(k,:,:) =reshape(cv(n0+1:n0+ndim_nr*ndim_nr), ...
	                        ndim_nr, ndim_nr);
	                    n0 = n0+ndim_nr*ndim_nr;
	                end
	            case 'diag'
	                for ( int k = 0; k < K; k++){
	                    gcv_nr(k,:) = reshape(cv(n0+1:n0+ndim_nr), 1, ndim_nr);
	                    n0 = n0 + ndim_nr;
	                end
	            case 'spherical'
	                gcv_nr = cv(n0+1:end);
	        end
	        d = d + ndim_nr;
	    else if(datatype(i,1) == 2 ){ % continous data with errors
	        ndim_er = datatype(i,2);
	        data_er = data(:,d+1:d+ndim_er);
	        gmu = reshape(lmu(ne+1:ne+K*ndim_er), K, ndim_er);
	        switch(noise) {
	            case 'full'
	                for ( int k = 0; k < K; k++){
	                    gcv(k,:,:) = reshape(lcv(n1+1:n1+ndim_er*ndim_er), ...
	                        ndim_er,ndim_er);
	                    n1 = n1 + ndim_er*ndim_er;
	                end
	            case 'diag'
	                for ( int k = 0; k < K; k++){
	                    gcv(k,:) = reshape(lcv(n1+1:n1+ndim_er), 1, ndim_er);
	                    n1 = n1 + ndim_er;
	                end
	            case 'spherical'
	                gcv = lcv(n1+1:end); 
	        end
	        d = d + ndim_er;
	    elseif datatype(i,1) == 3
	        ndim_bin = datatype(i,2);
	        data_bin = data(:,d+1:d+ndim_bin);
	        bp = reshape(mu(nm+1:nm+K*ndim_bin), K, ndim_bin);
	        nm = nm + K*ndim_bin;
	        d = d  + ndim_bin;
	    elseif datatype(i,1) == 4
	        ndim_mul = datatype(i,2);
	        data_mul = data(:,d+1:d+ndim_mul);
	        mp = reshape(mu(nm+1:nm+K*ndim_mul), K, ndim_mul);
	        nm = nm + K*ndim_mul;
	        d = d + ndim_mul;
	    elseif datatype(i,1) == 5
	        ndim_int = datatype(i,2);
	        data_int = data(:, d+1:d+ndim_int);
	        ip = reshape(mu(nm+1:nm+K*ndim_int), K, ndim_int);
	        nm = nm + K*ndim_int;
	        d = d + ndim_int;
	    elseif datatype(i,1) == 6
	        ndim_error = datatype(i,2);
	        if(ndim_error ~= ndim_er){ %
	            error 'The dimension of measurement errors and '
	        end        
	        S = data(:, d+1:d+ndim_error);
	        % error inforamtion
	        S = S + 1.0e-6*ones(ndata, ndim_er);
	        d = d + ndim_error;
	    end
	end

	//--------------------------------------------------------------------------
	// THE POSTERIOR OF THE LATENT VARIABLES INCLUDING U AND W FOR THE VARIABLES
	// WITH MEASUREMENT ERRORS
	//--------------------------------------------------------------------------
	aux1 = zeros(ndata, K);
	aux2 = zeros(ndata, K);
	aux3 = zeros(ndata, K);
	aux4 = zeros(ndata, K);
	aux5 = zeros(ndata, K);
	//--------------------------------------------------------------------------
	//   CALCULATE THE RESPONSIBILITIES
	//--------------------------------------------------------------------------
	if(ndim_er ~= 0){ %
	//     disp 'calculate components of continus variable with errors for responsibilities'
	    for ( int n = 0; n < ndata; n++){
	        for ( int k = 0; k < K; k++){
	            % E_q [LOG P(t|K)]
	            switch(noise) {
	                case 'full'
	                    gcvk = squeeze(gcv(k,:,:))+diag(S(n,:));
	                    aux1(n,k)=-ndim_er/2*log(2*pi)-log(det(gcvk))/2-1/2*...
	                        (data_er(n,:)-gmu(k,:))*inv(gcvk)*(data_er(n,:)...
	                        -gmu(k,:))';
	                case 'diag'
	                    covk = gcv(k,:)+S(n,:);
	                    aux1(n,k)=-ndim_er/2*log(2*pi)-sum(log(covk))/2-1/2*...
	                        (data_er(n,:)-gmu(k,:))*inv(diag(covk))*(data_er...
	                        (n,:)-gmu(k,:))';
	                case 'spherical'
	                    covk = gcv(k)+S(n,:);
	                    aux1(n,k)=-ndim_er/2*log(2*pi)-sum(log(covk))/2-1/2*...
	                        (data_er(n,:)-gmu(k,:))*inv(diag(covk))*(data_er(n,:)-gmu(k,:))';
	                otherwise
	                    disp 'Unknown noise model'
	            end
	        end
	    end
	end
	if(ndim_nr ~= 0){ %
	//     disp 'calculate components of continus variable without errors for responsibilities'
	    % THE SECOND PART IS RELATED TO THE DATA WITHOUT MEASUREMENT ERRORS
	    %     n2 = dist2(data_nr, gmu_nr);
	    for ( int n = 0; n < ndata; n++){
	        for ( int k = 0; k < K; k++){
	            % E_q[ log(p(t|u, k)) ]
	            switch(noise) {
	                case 'diag'
	                    covk = gcv_nr(k,:);
	                    aux2(n,k)=-ndim_nr/2*log(2*pi)-sum(log(covk))/2-...
	                        1/2*(data_nr(n,:)-gmu_nr(k,:))*inv(diag(covk))*(...
	                        data_nr(n,:)-gmu_nr(k,:))';
	                case 'spherical'
	                    covk = gcv_nr(k);
	                    aux2(n,k)=-ndim_nr/2*log(2*pi)-ndim_nr*log(covk)/2 ...
	                        -1/2*(data_nr(n,:)-gmu_nr(k,:))*(data_nr(n,:)...
	                        -gmu_nr(k,:))'/covk;
	                case 'full'
	                    aux2(n,k)=-ndim_nr/2*log(2*pi)-log(det(squeeze(gcv_nr...
	                        (k,:,:))))/2-1/2*((data_nr(n,:)-gmu_nr(k,:))*...
	                        inv(squeeze(gcv_nr(k,:,:)))*(data_nr(n,:)-gmu_nr(k,:))');
	                otherwise
	                    disp 'Unknown noise model'
	            end
	        end
	    end
	end

	if(ndim_bin ~=0){ %
	//     disp 'calculate components of binary variable for responsibilities'
	    %  THE THIRD PART IS RELATED TO THE BINARY DATA
	    %  we need first calculate its mean
	    for i = 1 : ndata
	        for j = 1 : K
	            sgm = bp(j,:)+eps;
	            datai = data_bin(i,:);
	//             tmp1 = 0;
	//             trialno = 1;
	//             for k = 1:ndim_bin                
	//                 dif = trialno - datai(k);
	//                 tmp1 = tmp1 + sum(log(1:datai(k)))+sum(log(1:dif));
	//             end
	            aux3(i,j)= sum(sort(datai.*log(sgm)+(1.0-datai).*log(1.0-sgm)));
	//             aux3(i,j)=sum(sort(datai.*log(sgm)+(1.0-datai).*log(1.0-sgm)))...
	//                 -tmp1;
	        end
	    end
	end

	if(ndim_mul ~= 0){ %
	//     disp 'calculate components of category variable for responsibilities'
	//     pause    
	//     [ndata ndim_mul] = size(data_mul);
	    for n = 1 : ndata
	        for k = 1 : K
	            temp = mp(k,:)+eps;
	            log_sgm = log(temp);
	            datat = data_mul(n,:);
	            aux4(n,k) = sum(sort(datat.*log_sgm) );
	        end
	    end
	end

	if(ndim_int ~=0){ %
	//     disp 'calculate components of int variable for responsibilities'
	//     [ndata ndim_int] = size(data_int);
	    for t=1 : ndata
	        for i = 1 : K
	            sgm = ip(i,:);
	            datai = data_int(t, :);
	            poiss(t,i) = sum(datai.*log(sgm)-sgm);
	            aux5(t,i) = poiss(t,i)-sum(sum(log(1:datai)));
	        end
	    end
	end


	aux = aux1 + aux2 + aux3 + aux4 + aux5;

	q = (ones(ndata, 1)*p).*exp(aux);
	s = sum(q, 2);
	// Set any zeros to one before dividing
	s = s + (s==0);
	q = q./(s*ones(1, K));


	// q
	// pause
	
}


/*
 * $Log: ClusterEStep.java,v $
 * Revision 1.1  2009/09/22 07:04:16  pah
 * daily checkin
 *
 */
