/*$Id: ApplicationControllerDispatcher.java,v 1.4 2004/03/05 16:16:23 nw Exp $
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
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;

/** Reimplementation of rough dispatcher.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
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
    public void dispatchStep( Workflow job, Step js) throws JesException {
        boolean succeeded = false;
        try {
        String toolLocation = locator.locateTool(js);
        ApplicationController appController = DelegateFactory.createDelegate(toolLocation);

        User user = buildUser(job);
        ParameterValues params = buildParameterValues(js);
            String xpath = null;
            JobIdentifierType id = JesUtil.createJobId(job.getJobExecutionRecord().getJobId(),xpath);
            String applicationID = appController.initializeApplication( js.getTool().getName() 
                                                                       , id.getValue()
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
    protected User buildUser(Workflow parent) throws JesException {
        User user = new User();
        user.setAccount(parent.getCredentials().getAccount().getName()+"@"+parent.getCredentials().getAccount().getCommunity());
        user.setGroup(parent.getCredentials().getGroup().getName());
        user.setToken(parent.getCredentials().getSecurityToken());
        return user;
    }
    
    /** factored out again, for changes sake*/
    protected ParameterValues buildParameterValues(Step js) throws JesException{
        try {
        ParameterValues params = new ParameterValues();
        params.setMethodName(locator.getToolInterface(js));
        StringWriter sw = new StringWriter();
        js.getTool().marshal(sw);
        sw.close();
        params.setParameterSpec(sw.toString());
        return params;
        } catch (Exception e) {
            throw new JesException("cold not serialize to xml",e);
        }
    }
}


/* 
$Log: ApplicationControllerDispatcher.java,v $
Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:07  nw
rearranging code
 
*/