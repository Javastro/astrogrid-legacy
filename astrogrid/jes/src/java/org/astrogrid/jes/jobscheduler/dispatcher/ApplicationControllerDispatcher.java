/*$Id: ApplicationControllerDispatcher.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;

import java.net.URL;
import java.rmi.RemoteException;

/** Reimplementation of rough dispatcher.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 * @todo sort out with paul the calling convention for the app controller delegate - would be nicer to have separate parameters for jobURn and step number.
 * step number should be stepID - don't want to imply that steps are sequentially numbered.
 *
 */
public class ApplicationControllerDispatcher implements Dispatcher {
    /** Construct a new ApplicationControllerDispatcher
     * 
     */
    public ApplicationControllerDispatcher(Locator locator, URL monitorURL) {
        this.locator = locator;
        this.monitorURL = monitorURL;
    }
    protected final Locator locator;
    protected final URL monitorURL;
    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(java.lang.String, org.astrogrid.jes.job.JobStep)
     */
    public void dispatchStep(String communitySnippet, JobStep js) throws JesException {
        boolean succeeded = false;
        try {
        String toolLocation = locator.locateTool(js);
        ApplicationController appController = DelegateFactory.createDelegate(toolLocation);

        User user = buildUser(js);
        ParameterValues params = buildParameterValues(js);

            String applicationID = appController.initializeApplication( js.getTool().getName() 
                                                                       , js.getParent().getId() + ":" + js.getStepNumber()
                                                                       , monitorURL.toString()
                                                                       , user
                                                                       , params ) ;
                                                                        
            succeeded = appController.executeApplication( applicationID ) ;
            if (! succeeded) {
                throw new JesException("Application controller failed with unspecified error");
            }
        } catch (RemoteException re) {
            throw new JesException("Failed to communicate with application controller",re);
        } 
         
    }
    
    /** factored into separate method, as have feeling this may change .. */
    protected User buildUser(JobStep js) throws JesException {
        Job parent = js.getParent();
        User user = new User();
        user.setAccount(parent.getUserId()+"@"+parent.getCommunity());
        user.setGroup(parent.getGroup());
        user.setToken(parent.getToken());
        return user;
    }
    
    /** factored out again, for changes sake*/
    protected ParameterValues buildParameterValues(JobStep js) throws JesException{
        ParameterValues params = new ParameterValues();
        params.setMethodName(locator.getToolInterface(js));
        params.setParameterSpec(js.getTool().toXML());
        return params;
    }
}


/* 
$Log: ApplicationControllerDispatcher.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:07  nw
rearranging code
 
*/