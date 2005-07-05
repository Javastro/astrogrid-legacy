/*$Id: DefaultQueryService.java,v 1.7 2005/07/05 08:27:00 clq2 Exp $
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
import org.astrogrid.applications.ApplicationEnvironmentUnavailableException;
import org.astrogrid.applications.ApplicationStillRunningException;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.manager.observer.RemoteProgressListener;
import org.astrogrid.applications.manager.observer.RemoteResultsListener;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.applications.manager.persist.SummaryHelper;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.Observer;

import javax.xml.rpc.ServiceException;

import junit.framework.Test;

/** Default implementation of the {@link org.astrogrid.applications.manager.QueryService}
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
    public DefaultQueryService(ExecutionHistory eh, ApplicationEnvironmentRetriver envret) {
        super();
        this.executionHistory = eh;
        this.environmentRetriever = envret;
    }
    protected final ExecutionHistory executionHistory;
    protected final ApplicationEnvironmentRetriver environmentRetriever;
  
    
    public MessageType queryExecutionStatus(String executionId) throws CeaException {     
        logger.debug("Getting execution status for " +executionId);
        MessageType retval = null;
        if (executionHistory.isApplicationInCurrentSet(executionId)) {
           Application app = executionHistory.getApplicationFromCurrentSet(executionId);
            retval = app.createTemplateMessage();
           retval.setContent(app.getStatus().toString());
           retval.setLevel(LogLevel.INFO);
           retval.setPhase(app.getStatus().toExecutionPhase());
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

     public ResultListType getResults(String executionId) throws CeaException {
         logger.debug("Getting results for " + executionId);
         if (executionHistory.isApplicationInCurrentSet(executionId)) {
             return executionHistory.getApplicationFromCurrentSet(executionId).getResult();
         } else { //look in the store
             return executionHistory.getApplicationFromArchive(executionId).getResultList();
         }
    
     }

     public ExecutionSummaryType getSummary(String executionId) throws CeaException {
         logger.debug("Getting summary for " + executionId);
         if (executionHistory.isApplicationInCurrentSet(executionId)) {
             Application app = executionHistory.getApplicationFromCurrentSet(executionId);
             return SummaryHelper.summarize(executionId,app);
         } else {
             return executionHistory.getApplicationFromArchive(executionId);
         }
     }

    public boolean registerProgressListener(String executionId, URI endpoint) throws CeaException {
        logger.debug("Registering progress listener for " + executionId + " at " + endpoint);
        if (! executionHistory.isApplicationInCurrentSet(executionId)) {
            logger.warn("applicaiton not in current set - no point registering listener, already finished");
            return false;
        }
        Application app = executionHistory.getApplicationFromCurrentSet(executionId);
        Observer obs = new RemoteProgressListener(endpoint);
        app.addObserver(obs);
        return true; 
    }

    public boolean registerResultsListener(String executionId, URI endpoint) throws CeaException {
        logger.debug("Registering results listener for " + executionId + " at " + endpoint);
        if (! executionHistory.isApplicationInCurrentSet(executionId)) {
            logger.warn("application not in current set - no point registering listener, its finished already");
            return false;
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
        return true; 
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

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.QueryService#getLogFile(java.lang.String, org.astrogrid.applications.manager.QueryService.StdIOType)
    */
   public File getLogFile(String executionId, ApplicationEnvironmentRetriver.StdIOType type) throws ApplicationEnvironmentUnavailableException, PersistenceException, FileNotFoundException, ApplicationStillRunningException, ApplicationEnvironmentUnavailableException  {
      if (type == ApplicationEnvironmentRetriver.StdIOType.out) {
      
         return environmentRetriever.retrieveStdOut(executionId);
      }
      else if(type == ApplicationEnvironmentRetriver.StdIOType.err)
      {
         return environmentRetriever.retrieveStdErr(executionId);
      }
      else { 
      
         //should not be able to happen
         throw new InternalError("unknown ApplicationEnvironmentRetriver.StdIOType");
       
      }
   }

}


/* 
$Log: DefaultQueryService.java,v $
Revision 1.7  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.6.152.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.6.138.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.6  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.5  2004/07/23 08:42:57  nw
fixed phase reporting bug.

Revision 1.4  2004/07/09 14:48:24  nw
updated to match change in type of register*Listener methods in cec wsdl

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