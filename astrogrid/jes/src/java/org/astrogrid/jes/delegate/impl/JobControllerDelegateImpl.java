package org.astrogrid.jes.delegate.impl;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobInfo;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub;
import org.astrogrid.jes.types.v1.WorkflowString;
import org.astrogrid.jes.types.v1.WorkflowSummary;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;


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
            org.astrogrid.jes.types.v1.JobURN axisURN = jc.submitWorkflow(new WorkflowString(req));
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
            jc.cancelJob(new org.astrogrid.jes.types.v1.JobURN(urn.getContent()));
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
            jc.deleteJob(new org.astrogrid.jes.types.v1.JobURN(urn.getContent()));
        } catch (IOException e) {
            throw new JesDelegateException(e);
        }        
    }

    /**
     * @see org.astrogrid.jes.delegate.JobController#readJobList(org.astrogrid.community.beans.v1.Account)
     */
    public JobInfo[] readJobList(Account acc) throws JesDelegateException {
        try {
            JobController jc = getBinding();
            _Account axisAcc = new _Account();
            Identifier name = new Identifier();            
            Identifier community = new Identifier();
            
            name.setValue(acc.getName());
            community.setValue(acc.getCommunity());
            
            axisAcc.setCommunity(community);
            axisAcc.setName(name);
            
            WorkflowSummary[] wl = jc.readJobList(axisAcc);
            if (wl == null) { // returns null if none found - change this to an empy array;
                wl = new WorkflowSummary[]{};
            }
            JobInfo[] result = new JobInfo[wl.length];
            for (int i = 0; i < wl.length; i++) {
                JobInfo nfo = new JobInfo(wl[i]);
                result[i] = nfo;
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
            System.out.println(urn.getContent());
            String result = jc.readJob(new org.astrogrid.jes.types.v1.JobURN(urn.getContent())).getValue();
            System.out.println("result :" + result + ";");
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