/*
 * $Id: ClusterMStep.java,v 1.1 2009/09/22 07:04:16 pah Exp $
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

	// -------------------------------------------------------------------------
	// Re-estimate the global parameters of the model
	//--------------------------------------------------------------------------
	function  [mu cv lmu lcv p] = clustering_m_step(alldata, datatype, K, q,...
	    lcv, cv_type){

	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	// M-step of the VB method
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	[ndata alldim] = size(alldata);

	no_of_data_types = length(datatype);

	

	n0 = 0; n1 = 0;  d = 0;
	for ( int i = 0; i < no_of_data_types; i++){
	    if(datatype(i,1) == 1     ){ % continuous data without errors
	        ndim_nr = datatype(i,2);
	        data_nr = alldata(:, d+1:d+ndim_nr);
	        d = d + ndim_nr;
	    if(datatype(i,1) == 2 ){ % continous data with errors
	        ndim_er = datatype(i,2);
	        data_er = alldata(:,d+1:d+ndim_er);
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
	        data_bin = alldata(:,d+1:d+ndim_bin);
	        d = d  + ndim_bin;
	    elseif datatype(i,1) == 4
	        ndim_mul = datatype(i,2);
	        data_mul = alldata(:,d+1:d+ndim_mul);
	        d = d + ndim_mul;
	    elseif datatype(i,1) == 5
	        ndim_int = datatype(i,2);       
	        data_int = alldata(:, d+1:d+ndim_int);
	        d = d + ndim_int;
	    elseif datatype(i,1) == 6
	        ndim_error = datatype(i,2);
	        if(ndim_error ~= ndim_er){ %
	            error 'The dimension of measurement errors and '
	        end        
	        S = alldata(:, d+1:d+ndim_error);
	        % error inforamtion
	        S = S + 1.0e-6*ones(ndata, ndim_er);
	        d = d + ndim_error;
	    end
	end
	// maximize for the parameters gmu, gcv for the data with measurement errors
	// [ndata ndim] = size(data);
	//%%%%%%%%%%%%%%%%%%%
	//   gmu
	//%%%%%%%%%%%%%%%%%%%
	mu = []; cv = [];
	lmu = []; lcv = [];
	if(ndim_er ~= 0  ){ %
	    for ( int k = 0; k < K; k++){
	        tmpg = zeros(ndim_er,1);
	        tmpt = zeros(ndim_er, ndim_er);
	        for ( int n = 0; n < ndata; n++){
	            switch(noise) {
	                case 'full'
	                    tmpg = tmpg + q(n,k)*inv(squeeze(gcv(k,:,:))+diag(  ...
	                        S(n,:)))*data_er(n,:)';
	                    tmpt=tmpt+q(n,k)*inv(squeeze(gcv(k,:,:))+diag(S(n,:)));
	                case 'diag'
	                    tmpg =tmpg + q(n,k)*inv(diag(gcv(k,:))+diag(S(n,:)))...
	                        *data_er(n,:)';
	                    tmpt = tmpt + q(n,k)*inv(diag(gcv(k,:))+diag(S(n,:)));
	                case 'spherical'
	                    tmpg =tmpg + q(n,k)*inv(gcv(k)+diag(S(n,:)))*data_er...
	                        (n,:)';
	                    tmpt = tmpt + q(n,k)*inv(gcv(k)+diag(S(n,:)));
	            end
	        end
	        gmu(k,:) = (inv(tmpt)*tmpg)';        
	    end
	    lmu = [lmu; gmu(:)];
	    %%%%%%%%%%%%%%%%%%%
	    % gcv
	    %%%%%%%%%%%%%%%%%%%
	    for ( int k = 0; k < K; k++){
	        switch(noise) {
	            case 'full'
	                cvk = sqrt(squeeze(gcv(k,:,:)));
	                tmpcv = minimize(cvk(:), 'obj_full_cv', 10, q(:,k), ...
	                    gmu(k,:), data_er, S);
	                tmpcv = reshape(tmpcv,ndim_er,ndim_er)^2;
	                lcv =[lcv; tmpcv(:)];
	            case 'diag'
	                cvk = gcv(k,:).^(1/2);               
	                tmpcv =minimize(cvk(:),'obj_diag_cv',10,q(:,k),gmu(k,:),...
	                    data_er, S);                
	                lcv=[lcv; tmpcv.^2];
	            case 'spherical'
	                cvk = sqrt(gcv(k));
	                tmpcv = minimize_noise(cvk,'obj_spher_cv',10,q(:,k), gmu(k,:),...
	                    data_er, S);
	                gcv(k) = tmpcv^2;
	                lcv=[lcv; gcv(k)];
	        end
	    end
	end

	if(ndim_nr ~= 0){ %
	    % maximize for the parameters gmu, gcv for the data without errors
	    [ndata ndim_nr] = size(data_nr);
	    %%%%%%%%%%%%%%%%%%%%
	    %   gmu
	    %%%%%%%%%%%%%%%%%%%%
	    for ( int k = 0; k < K; k++){
	        tmp = zeros(1, ndim_nr);
	        for ( int n = 0; n < ndata; n++){
	            tmp = tmp+q(n,k)*data_nr(n,:);
	        end
	        gmu_nr(k,:) = tmp/sum(q(:,k));
	    end
	    mu = [mu; gmu_nr(:)];
	    %%%%%%%%%%%%%%%%%%%
	    % gcv
	    %%%%%%%%%%%%%%%%%%%
	    for ( int k = 0; k < K; k++){
	        switch(noise) {
	            case 'full'
	                gcvt = zeros(ndim_nr, ndim_nr);
	                for ( int n = 0; n < ndata; n++){
	                    gcvt = gcvt + q(n,k)*(gmu_nr(k,:)-data_nr(n,:))'*(gmu_nr(k,:)-...
	                        data_nr(n,:));
	                end
	                gcv_nr(k,:,:) = gcvt / sum(q(:,k)+eps);
	                tmp = squeeze(gcv_nr(k,:,:));
	                cv=[cv; tmp(:)];
	            case 'diag'
	                for ( int i = 0; i < ndim_nr; i++){
	                    tmp = 0.0;
	                    for ( int n = 0; n < ndata; n++){
	                        tmp = tmp + q(n,k)*(data_nr(n,i)-gmu_nr(k,i))^2;
	                    end
	                    gcv_nr(k,i) = tmp/ sum(q(:,k)+eps);
	                end
	                tmp = gcv_nr(k,:);
	                cv = [cv; tmp(:)];
	            case 'spherical'
	                tmpt = 0.0;
	                for ( int n = 0; n < ndata; n++){
	                    tmpt=tmpt+q(n,k)*(data_nr(n,:)-gmu_nr(k,:))*(data_nr(n,:)...
	                        -gmu_nr(k,:))';
	                end
	                gcv_nr(k) = tmpt/(ndim_nr*sum(q(:,k)+eps));
	                if(gcv_nr(k) < eps){ %
	                    gcv_nr(k) = eps;
	                end
	                cv =[cv; gcv_nr(k)];
	                
	        end
	    end
	end

	//--------------------------------------------------------------------------
	// the parameters for the binary data
	//--------------------------------------------------------------------------
	bp =[];
	if(ndim_bin ~= 0){ %
	//     [ndata bin_dim] = size(bin_data);
	    for ( int k = 0; k < K; k++){
	        for ( int j = 0; j < ndim_bin; j++){
	//             trialno = 1;
	//             bp(k,j) = sum((q(:,k).*data_bin(:,j)))/(trialno*sum(q(:,k)+eps));
	            bp(k,j) = sum((q(:,k).*data_bin(:,j)))/(sum(q(:,k)+eps));
	        end
	    end    
	    mu=[mu; bp(:)];
	end

	mp = [];
	if(ndim_mul ~= 0){ %
	    [ndata dim_mul] = size(data_mul);
	    for ( int k = 0; k < K; k++){
	        for ( int i = 0; i < dim_mul; i++){
	            mp(k,i) = sum(q(:,k).*data_mul(:,i));
	        end
	        mp(k,:) = mp(k,:)/sum(mp(k,:));
	    end
	    mu = [mu; mp(:)];
	end

	ip = [];
	if(ndim_int ~= 0){ %
	    [ndata dim_int] = size(data_int);
	    for ( int k = 0; k < K; k++){
	        for ( int i = 0; i < dim_int; i++){
	            ip(k,i) = sum(q(:,k).*data_int(:,i))/sum(q(:,k));
	        end
	    end
	    mu = [mu; ip(:)];
	end

	//%%%%%%%%%%%%%%%%%
	// priors
	//%%%%%%%%%%%%%%%%%
	p = mean(q, 1);


	}
	
}


/*
 * $Log: ClusterMStep.java,v $
 * Revision 1.1  2009/09/22 07:04:16  pah
 * daily checkin
 *
 */
