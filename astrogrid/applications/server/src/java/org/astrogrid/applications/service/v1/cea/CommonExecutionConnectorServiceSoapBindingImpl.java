/*
 * $Id: CommonExecutionConnectorServiceSoapBindingImpl.java,v 1.15 2008/08/21 14:34:03 gtr Exp $
 * 
 * Created on 25-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.service.v1.cea;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.CEAComponentManager;

import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.security.AxisServiceSecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

import org.apache.axis.description.ServiceDesc;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;

import java.rmi.RemoteException;

/**
 * This is the main implementation of the CommonExecutionConnectorService. This is the class that should be referenced in the Axis wsdd file.
 * Its main task is to convert between axis and castor object representations, and then delegate to the appropriate component in the componentManager 
 * <p>
 * Catches all exceptions, propagates them as {@link org.astrogrid.applications.service.v1.cea.CeaFault} messages back to the caller.
 * @author Paul Harrison (pah@jb.man.ac.uk) 25-Mar-2004
 * @author Noel Winstanley
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorServiceSoapBindingImpl implements CommonExecutionConnector {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommonExecutionConnectorServiceSoapBindingImpl.class);


      
      protected  final ExecutionController cec;
      protected final QueryService query;

   /**
    * 
    */
   public CommonExecutionConnectorServiceSoapBindingImpl() {
      try {
          //TODO need to get this service description into pico
         ServiceDesc servicedesc = org.apache.axis.MessageContext.getCurrentContext().getService().getServiceDescription();
 
         cec = CEAComponentManagerFactory.getInstance().getExecutionController();
        
         //nController(servicedesc);
      }
      catch (Throwable e) {         
         logger.fatal("problem instatiating applicationController", e);
         //we're stuffed - no point continuing.
         throw new RuntimeException("Could not instantiate application controller",e);
      }
      try {
          query = CEAComponentManagerFactory.getInstance().getQueryService();
      } catch (Throwable e) {
          logger.fatal("problem instantiating querier",e);
          throw new RuntimeException("Could not instantiate query service",e);
      }
      
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(org.astrogrid.workflow.beans.v1.axis._tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, java.lang.String)
    */
   public String init(_tool tool, JobIdentifierType jobstepID)
      throws RemoteException, CeaFault {           
         try {
           this.cacheSecurityGuard();
           Tool ctool = Axis2Castor.convert(tool);
           this.decide("init", ctool);
           return cec.init(ctool, jobstepID.toString());
         }
         catch (Exception e) {
           logger.error("init(_tool tool = " + tool + ") - Throwable caught:", e);
           throw CeaFault.makeFault(e);
         }
         catch(Throwable e) {
            logger.error("init(_tool tool = " + tool + ") - Exception caught:", e);
            throw CeaFault.makeFault(new Exception("an Throwable occurred in init-"+e.getMessage(), e));
         }
   }

   /**
    * Commits a job for execution.
    *
    * @param job The execution identifier for the job, as set by init().
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(java.lang.String)
    */
   public boolean execute(String job) throws RemoteException, CeaFault {
     try {
       this.cacheSecurityGuard();
       this.decide("execute", job);
       return cec.execute(job);
     } catch (Exception e) {
       logger.error("Operation execute("+ job + ") failed", e);
       throw CeaFault.makeFault(e);
     } catch (Throwable e) {
       logger.error("Operation execute("+ job +") failed", e);
       throw CeaFault.makeFault(new Exception("a throwable occurred in execute-"+e.getMessage(),e)); 
     }
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws RemoteException, CeaFault {
      try {
        this.cacheSecurityGuard();
        this.decide("abort", executionId);
        return cec.abort(executionId);
      } catch (Exception e) {
        logger.error("abort(" + executionId + ")", e);
          throw CeaFault.makeFault(e);
      } catch (Throwable t) {
        logger.error("abort(" + executionId+")", t);
          throw CeaFault.makeFault(new Exception("a throwable occurred in abort",t));
   }
   }


   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId)
      throws RemoteException, CeaFault {
         try {
           this.cacheSecurityGuard();
           this.decide("queryExecutionStatus", executionId);
           org.astrogrid.applications.beans.v1.cea.castor.MessageType mess = query.queryExecutionStatus(executionId);
           return  Castor2Axis.convert(mess);
         }
         catch (Exception e) {
            logger.error("queryExecutionStatus(" + executionId+")", e);
            throw CeaFault.makeFault(e);
         }
         catch(Throwable e)
         {
            logger.error("queryExecutionStatus(" + executionId+")", e);
            throw CeaFault.makeFault(new Exception("an Throwable occurred in query status-"+e.getMessage(), e));
         }
   }
     
/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#registerResultsListener(java.lang.String, org.apache.axis.types.URI)
 */
public boolean registerResultsListener(String arg0, URI arg1) throws RemoteException, CeaFault {
    try {
        return query.registerResultsListener(arg0,new java.net.URI(arg1.toString()));
    } catch (Exception e) {
        logger.error("registerResultsListener(" + arg0 + ", " + arg1+")", e);
        throw CeaFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("registerResultsListener(" + arg0 + ", " + arg1 +")", e);
        throw CeaFault.makeFault(new Exception("a throwable occurred in registerResultsListener-"+e.getMessage(),e));
    }
}

/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#registerProgressListener(java.lang.String, org.apache.axis.types.URI)
 */
public boolean registerProgressListener(String arg0, URI arg1) throws RemoteException, CeaFault {
    try {
        return query.registerProgressListener(arg0,new java.net.URI(arg1.toString()));
    } catch (Exception e) {
        logger.error("registerProgressListener(" + arg0 + ", " + arg1+")", e);
        throw CeaFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("registerProgressListener(" + arg0 + ", " + arg1 +")", e);

        throw CeaFault.makeFault(new Exception("a throwable occurred in registerProgressListener-"+e.getMessage(),e));
    }
}

/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getExecutionSummary(java.lang.String)
 */
public ExecutionSummaryType getExecutionSummary(String arg0) throws RemoteException, CeaFault {
    try {
        this.cacheSecurityGuard();
        this.decide("getExecutionSummary", arg0);
        return Castor2Axis.convert(query.getSummary(arg0));
    } catch (Exception e) {
        logger.error("getExecutionSummary("+arg0+")", e);
        throw CeaFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("getExecutionSummary("+arg0+")", e);
        throw CeaFault.makeFault(new Exception("a throwable occurred in getExecutionSummary-"+e.getMessage(),e));
    }
}
/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getResults(java.lang.String)
 */
public ResultListType getResults(String arg0) throws RemoteException, CeaFault {
    try {
        this.cacheSecurityGuard();
        this.decide("getResults", arg0);
        return Castor2Axis.convert(query.getResults(arg0));
    } catch (Exception e) {
        logger.error("getResults("+arg0+")", e);
        throw CeaFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("getResults("+arg0+")", e);

        throw CeaFault.makeFault(new Exception("a throwable occurred in getResults-"+e.getMessage(),e));
    }
}
   
/** 
  * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#returnRegistryEntry()
  */
 public String returnRegistryEntry() throws RemoteException, CeaFault
 {
     try {
         return XMLUtils.DocumentToString(CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry());
     } catch (Exception e) {
		 logger.error("returnRegistryEntry()", e);
         throw CeaFault.makeFault(e);       
     } catch (Throwable e) {
		 logger.error("returnRegistryEntry()", e);
         throw CeaFault.makeFault(new Exception("A throwable occured in return registry entry-"+e.getMessage(),e));
     }
  }
 
  /**
   * Stores a security guard where worker threads can find it.
   * The guard is obtained from the Axis message-context, where it has been
   * loaded with the results of authentication. The delegated credentials for
   * the authenticated identity, if any, are loaded. If the request has not
   * been authenticated, this method stores a security guard with no
   * principals or credentials.
   */
   private void cacheSecurityGuard() throws ClassNotFoundException, 
                                            InstantiationException, 
                                            IllegalAccessException, 
                                            CertificateException {
     AxisServiceSecurityGuard g = AxisServiceSecurityGuard.getInstanceFromContext();
     g.loadDelegation();
     CeaSecurityGuard.setInstanceInContext(g);
   }

  /**
   * Checks the requested action against the access policy.
   * This form of the overloaded method is called from init(), where the
   * Tool is available.
   */
  private void decide(String operationName, Tool tool) throws GeneralSecurityException, Exception {
    HashMap h = new HashMap();
    h.put("cea.soap.operation", operationName);
    h.put("cec.tool", tool);
    CeaSecurityGuard.getInstanceFromContext().decide(h);
  }
  
  /**
   * Checks the requested action against the access policy.
   * This form of the overloaded method is called from methods other than 
   * init(), where the ownership of the execution is already established.
   */
  private void decide(String operationName, String job) throws GeneralSecurityException, Exception {
    Application a = this.getApplication(job);
    HashMap h = new HashMap();
    h.put("cea.soap.operation", operationName);
    h.put("cea.application", a);
    h.put("cea.job.owner", (a == null)? null : a.getUser().getPrincipal());
    CeaSecurityGuard.getInstanceFromContext().decide(h);
  }
  
  /**
   * Retrives the Application object for a named execution.
   *
   * @return The Application; never null.
   * @throws ExecutionIDNotFoundException If the named execution is not current.
   */
  private Application getApplication(String job) throws ExecutionIDNotFoundException, PersistenceException {
    CEAComponentManager m = CEAComponentManagerFactory.getInstance();
    ExecutionHistory h = (ExecutionHistory) m.getContainer().getComponentInstance(ExecutionHistory.class);
    if (h.isApplicationInCurrentSet(job)) {
      return h.getApplicationFromCurrentSet(job);
    } 
    else { 
      return null;
    }
  }
  
}
