package org.astrogrid.jes.delegate.impl;

import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.beans.v1.axis.executionrecord.WorkflowString;
import org.astrogrid.jes.beans.v1.axis.executionrecord.WorkflowSummaryType;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;


/**
 * SOAP-based implementation of a job controller delegate.
 */
public class JobControllerDelegateImpl extends JobControllerDelegate {

    public JobControllerDelegateImpl( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;      
    }
    
    public JobControllerDelegateImpl( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint;
      this.timeout = timeout;
    }
    
    public JobURN submitWorkflow(Workflow j) throws JesDelegateException {
 
            
        try {
            StringWriter sw = new StringWriter();
            j.marshal(sw);
            sw.close();
            String req = sw.toString();
            org.astrogrid.jes.delegate.v1.jobcontroller.JobController jc= getBinding(); 
            org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN axisURN = jc.submitWorkflow(new WorkflowString(req));
            JobURN result = new JobURN();
            result.setContent(axisURN.toString()); 
            return result;
        }
        catch( IOException rex) {
            throw new JesDelegateException( rex ) ;            
        }
        catch (CastorException e) {
            throw new JesDelegateException(e);
        }

    }

    private org.astrogrid.jes.delegate.v1.jobcontroller.JobController getBinding() throws JesDelegateException {
        try {
        JobControllerServiceSoapBindingStub binding = (JobControllerServiceSoapBindingStub)
            new JobControllerServiceLocator().getJobControllerService( new URL( this.getTargetEndPoint() ) );                        
        binding.setTimeout( this.getTimeout() ) ;   
        return binding;
        } catch (Exception e) {
            throw new JesDelegateException("Could not create service binding",e);
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.JobController#cancelJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void cancelJob(JobURN urn) throws JesDelegateException {
        try {
            JobController jc = getBinding();
            jc.cancelJob(new org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN(urn.getContent()));
        } catch (IOException e) {
            throw new JesDelegateException(e);
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.JobController#deleteJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void deleteJob(JobURN urn) throws JesDelegateException {
        try {
            JobController jc = getBinding();
            jc.deleteJob(new org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN(urn.getContent()));
        } catch (IOException e) {
            throw new JesDelegateException(e);
        }        
    }


    public org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] listJobs(Account acc) throws JesDelegateException {
        try {
            JobController jc = getBinding();
            _Account axisAcc = new _Account();
            Identifier name = new Identifier();            
            Identifier community = new Identifier();
            
            name.setValue(acc.getName());
            community.setValue(acc.getCommunity());
            
            axisAcc.setCommunity(community);
            axisAcc.setName(name);
            
            WorkflowSummaryType[] wl = jc.readJobList(axisAcc);
            if (wl == null) { // returns null if none found - change this to an empy array;
                wl = new WorkflowSummaryType[]{};
            }
            // check for nulls.. unlikely, but may happen.
            int length = 0;
            for (int i = 0; i < wl.length; i++) {
                if (wl[i] != null) {
                    length++;
                }
            }
            org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[] result = new org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType[length];
            for (int i = 0; i < wl.length; i++) {
                if (wl[i] != null) {
                    result[i]= Axis2Castor.convert(wl[i]); 
                }
            }
            return result;
            
        } catch (IOException e) {
            throw new JesDelegateException(e);
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.JobController#readJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public Workflow readJob(JobURN urn) throws JesDelegateException {
        try {
            JobController jc = getBinding();
            String result = jc.readJob(new org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN(urn.getContent())).getValue();
            if (result == null || result.trim().length() == 0) {
                throw new JesDelegateException("Null workflow returned");
            }
            StringReader sw = new StringReader(result);
            return Workflow.unmarshalWorkflow(sw);
        } catch (IOException e) {
            throw new JesDelegateException(e);
        }catch (CastorException e) {
            throw new JesDelegateException(e);
        }
    }

        
    
   

} // end of class JobControllerDelegateImpl