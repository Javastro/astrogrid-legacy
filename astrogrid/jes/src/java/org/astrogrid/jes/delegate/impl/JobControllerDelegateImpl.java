package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.ListCriteria;
import org.astrogrid.jes.types.v1.SubmissionResponse;
import org.astrogrid.jes.types.v1.WorkflowList;
import org.astrogrid.workflow.beans.v1.Workflow;

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
    
    public JobURN submitJob(Workflow j) throws JesDelegateException {
 
            
        try {
            StringWriter sw = new StringWriter();
            j.marshal(sw);
            sw.close();
            String req = sw.toString();
            org.astrogrid.jes.delegate.v1.jobcontroller.JobController jc= getBinding(); 
            SubmissionResponse response = jc.submitJob(req);
            if( ! response.isSubmissionSuccessful()) {
                throw new JesDelegateException(response.getMessage() ) ;
            }
            return response.getJobURN();
        }
        catch( IOException rex) {
            rex.printStackTrace() ;
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
     * @see org.astrogrid.jes.delegate.JobController#listJobs()
     */
    public Workflow[] listJobs(String userId,String community) throws JesDelegateException {    
        try {    
        org.astrogrid.jes.delegate.v1.jobcontroller.JobController jc = getBinding();
        ListCriteria criteria = new ListCriteria();
        criteria.setCommunity(community);
        criteria.setUserId(userId);
        WorkflowList wl = jc.readJobList(criteria);
        if (wl.getMessage() != null) {
            throw new JesDelegateException(wl.getMessage());
        }
        String[] rawArr = wl.getWorkflow();
        Workflow[] result = new Workflow[rawArr.length];
        for (int i = 0 ; i < rawArr.length; i++) {
            result[i] = Workflow.unmarshalWorkflow(new StringReader(rawArr[i]));
        }
        return result;
        } catch (IOException ioe) {
            throw new JesDelegateException(ioe);
        } catch (CastorException e) {
            throw new JesDelegateException(e);
        }
        
        
    } // end of submitJob()
    
   

} // end of class JobControllerDelegateImpl