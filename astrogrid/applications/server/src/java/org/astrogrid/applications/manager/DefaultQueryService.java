/*$Id: DefaultQueryService.java,v 1.9 2008/09/03 14:18:56 pah Exp $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Observer;

import javax.xml.rpc.ServiceException;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.ApplicationStillRunningException;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.LogLevel;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.environment.ApplicationEnvironmentUnavailableException;
import org.astrogrid.applications.manager.observer.RemoteProgressListener;
import org.astrogrid.applications.manager.observer.RemoteResultsListener;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.applications.manager.persist.SummaryHelper;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.joda.time.DateTime;

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
           retval.setContent("The application is no longer running" + summary.getPhase().toString());
           retval.setLevel(LogLevel.INFO);
           retval.setPhase(summary.getPhase());
           retval.setSource(summary.getApplicationName() + "\nid" + summary.getJobId());
           retval.setTimestamp(new DateTime());            
        }        
        return retval;

     }

     public org.astrogrid.applications.description.execution.ResultListType getResults(String executionId) throws CeaException {
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
Revision 1.9  2008/09/03 14:18:56  pah
result of merge of pah_cea_1611 branch

Revision 1.8.14.4  2008/08/29 07:28:30  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.8.14.3  2008/06/11 14:31:42  pah
merged the ids into the application execution environment

Revision 1.8.14.2  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.8.14.1  2008/04/04 15:46:07  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.8  2007/03/01 11:53:11  clq2
apps-gtr-2132-2

Revision 1.7.86.1  2007/02/27 17:45:29  gtr
The execution phase reported for an archived job is taken from the execution history instead of being forced to COMPLETED. This fixes BZ2132.

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