/*$Id: DefaultQueryService.java,v 1.3 2004/07/02 09:11:13 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.manager.observer.RemoteProgressListener;
import org.astrogrid.applications.manager.observer.RemoteResultsListener;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.SummaryHelper;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.Observer;

import javax.xml.rpc.ServiceException;

import junit.framework.Test;

/** Default implementation of the query service.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class DefaultQueryService implements QueryService, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DefaultQueryService.class);

    /** Construct a new DefaultQueryService
     * @param eh the store to use to service queries
     */
    public DefaultQueryService(ExecutionHistory eh) {
        super();
        this.executionHistory = eh;
    }
    protected final ExecutionHistory executionHistory;
    
    
    public MessageType queryExecutionStatus(String executionId) throws CeaException {     
        logger.debug("Getting execution status for " +executionId);
        MessageType retval = null;
        if (executionHistory.isApplicationInCurrentSet(executionId)) {
           Application app = executionHistory.getApplicationFromCurrentSet(executionId);
            retval = app.createTemplateMessage();
           retval.setContent(app.getStatus().toString());
           retval.setLevel(LogLevel.INFO);
           retval.setPhase(ExecutionPhase.RUNNING);
        }
        else // look in the persistance store
        {
           ExecutionSummaryType summary = executionHistory.getApplicationFromArchive(executionId);
           retval = new MessageType();           
           retval.setContent("The application is no longer running" + summary.getStatus().toString());
           retval.setLevel(LogLevel.INFO);
           retval.setPhase(ExecutionPhase.COMPLETED);
           retval.setSource(summary.getApplicationName() + "\nid" + summary.getExecutionId());
            retval.setTimestamp(new Date());            
        }        
        return retval;

     }
     /**
      * @see org.astrogrid.applications.manager.CommonExecutionController#getResults(java.lang.String)
      */
     public ResultListType getResults(String executionId) throws CeaException {
         logger.debug("Getting results for " + executionId);
         if (executionHistory.isApplicationInCurrentSet(executionId)) {
             return executionHistory.getApplicationFromCurrentSet(executionId).getResult();
         } else { //look in the store
             return executionHistory.getApplicationFromArchive(executionId).getResultList();
         }
    
     }
     /**
      * @see org.astrogrid.applications.manager.CommonExecutionController#getSummary(java.lang.String)
      */
     public ExecutionSummaryType getSummary(String executionId) throws CeaException {
         logger.debug("Getting summary for " + executionId);
         if (executionHistory.isApplicationInCurrentSet(executionId)) {
             Application app = executionHistory.getApplicationFromCurrentSet(executionId);
             return SummaryHelper.summarize(executionId,app);
         } else {
             return executionHistory.getApplicationFromArchive(executionId);
         }
     }
    /**
     * @see org.astrogrid.applications.manager.QueryService#registerProgressListener(java.lang.String, java.net.URI)
     */
    public void registerProgressListener(String executionId, URI endpoint) throws CeaException {
        logger.debug("Registering progress listener for " + executionId + " at " + endpoint);
        if (! executionHistory.isApplicationInCurrentSet(executionId)) {
            logger.warn("applicaiton not in current set - no point registering listener, already finished");
            return;
        }
        Application app = executionHistory.getApplicationFromCurrentSet(executionId);
        Observer obs = new RemoteProgressListener(endpoint);
        app.addObserver(obs); 
    }
    /**
     * @see org.astrogrid.applications.manager.QueryService#registerResultsListener(java.lang.String, java.net.URI)
     */
    public void registerResultsListener(String executionId, URI endpoint) throws CeaException {
        logger.debug("Registering results listener for " + executionId + " at " + endpoint);
        if (! executionHistory.isApplicationInCurrentSet(executionId)) {
            logger.warn("application not in current set - no point registering listener, its finished already");
            return;
        }
        Application app = executionHistory.getApplicationFromCurrentSet(executionId);
        Observer obs;
        try {
            obs = new RemoteResultsListener(endpoint);
        }
        catch (MalformedURLException e) {
            throw new CeaException("could not connect create client for remote service",e);
        }
        catch (ServiceException e) {
            throw new CeaException("Could not create client for remote service",e);
            
        }
        app.addObserver(obs); 
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "DefaultQueryService";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Component that queries status of appliations, and manages callbacks";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

}


/* 
$Log: DefaultQueryService.java,v $
Revision 1.3  2004/07/02 09:11:13  nw
improved logging

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/