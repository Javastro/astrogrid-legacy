/*
 * $Id: CommonExecutionConnectorServiceSoapBindingImpl.java,v 1.21 2009/06/10 12:40:46 pah Exp $
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

import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import org.apache.axis.AxisFault;
import org.apache.axis.description.ServiceDesc;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.component.CEAComponentContainer;
import org.astrogrid.applications.component.CEAComponents;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.common.beanjaxb.Axis2JAXB;
import org.astrogrid.common.beanjaxb.JAXB2Axis;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.security.AxisServiceSecurityGuard;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * This is the main implementation of the CommonExecutionConnectorService. This is the class that should be referenced in the Axis wsdd file.
 * Its main task is to convert between axis and jaxb object representations, and then delegate to the appropriate component in the componentManager 
 * <p>
 * Catches all exceptions, propagates them as {@link org.astrogrid.applications.service.v1.cea.CeaFault} messages back to the caller.
 * @author Paul Harrison (pah@jb.man.ac.uk) 25-Mar-2004
 * @author Noel Winstanley
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorServiceSoapBindingImpl extends org.springframework.remoting.jaxrpc.ServletEndpointSupport implements CommonExecutionConnector {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommonExecutionConnectorServiceSoapBindingImpl.class);


      
      protected   ExecutionController cec; //impl would like to reinstate final.
      protected final QueryService query;



    private CEAComponents components;

   /**
    * 
    */
   public CommonExecutionConnectorServiceSoapBindingImpl() {
      try {
         
         ServiceDesc servicedesc = org.apache.axis.MessageContext.getCurrentContext().getService().getServiceDescription();
 
         cec = CEAComponentContainer.getInstance().getExecutionController();
        
         //nController(servicedesc);
      }
      catch (Throwable e) {         
         logger.fatal("problem instatiating applicationController", e);
         //we're stuffed - no point continuing.
         throw new RuntimeException("Could not instantiate application controller",e);
      }
      try {
          query = CEAComponentContainer.getInstance().getQueryService();
      } catch (Throwable e) {
          logger.fatal("problem instantiating querier",e);
          throw new RuntimeException("Could not instantiate query service",e);
      }
      
   }

   
   @Override
protected void onInit() {//IMPL can this be done in the constructor?
		this.components = (CEAComponents) CEAComponentContainer.getInstance();
		this.cec = components.getExecutionController();
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(org.astrogrid.workflow.beans.v1.axis._tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, java.lang.String)
    */
   public String init(_tool tool, JobIdentifierType jobstepID)
      throws RemoteException, CeaFault {           
         try {
           this.cacheSecurityGuard();
           Tool ctool = Axis2JAXB.convert(tool);
           if(!ctool.getId().startsWith("ivo://")){//old clients leave this off
               StringBuffer sb = new StringBuffer("ivo://");
               sb.append(ctool.getId());
               ctool.setId(sb.toString());
           }
           return cec.init(ctool, jobstepID.toString(), CeaSecurityGuard.getInstanceFromContext());
         }
         catch (Exception e) {
           logger.error("init(_tool tool = " + tool + ") - Throwable caught:", e);
           throw AxisFault.makeFault(e);
         }
         catch(Throwable e) {
            logger.error("init(_tool tool = " + tool + ") - Exception caught:", e);
            throw AxisFault.makeFault(new Exception("an Throwable occurred in init-"+e.getMessage(), e));
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
      
       return cec.execute(job, this.cacheSecurityGuard());
     } catch (Exception e) {
       logger.error("Operation execute("+ job + ") failed", e);
       throw AxisFault.makeFault(e);
     } catch (Throwable e) {
       logger.error("Operation execute("+ job +") failed", e);
       throw AxisFault.makeFault(new Exception("a throwable occurred in execute-"+e.getMessage(),e)); 
     }
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws RemoteException, CeaFault {
      try {
        
        return cec.abort(executionId, this.cacheSecurityGuard());
      } catch (Exception e) {
        logger.error("abort(" + executionId + ")", e);
          throw AxisFault.makeFault(e);
      } catch (Throwable t) {
        logger.error("abort(" + executionId+")", t);
          throw AxisFault.makeFault(new Exception("a throwable occurred in abort",t));
   }
   }


   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId)
      throws RemoteException, CeaFault {
         try {
           
           org.astrogrid.applications.description.execution.MessageType mess = query.queryExecutionStatus(executionId, this.cacheSecurityGuard());
           return  JAXB2Axis.convert(mess);
         }
         catch (Exception e) {
            logger.error("queryExecutionStatus(" + executionId+")", e);
            throw AxisFault.makeFault(e);
         }
         catch(Throwable e)
         {
            logger.error("queryExecutionStatus(" + executionId+")", e);
            throw AxisFault.makeFault(new Exception("an Throwable occurred in query status-"+e.getMessage(), e));
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
        throw AxisFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("registerResultsListener(" + arg0 + ", " + arg1 +")", e);
        throw AxisFault.makeFault(new Exception("a throwable occurred in registerResultsListener-"+e.getMessage(),e));
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
        throw AxisFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("registerProgressListener(" + arg0 + ", " + arg1 +")", e);

        throw AxisFault.makeFault(new Exception("a throwable occurred in registerProgressListener-"+e.getMessage(),e));
    }
}

/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getExecutionSummary(java.lang.String)
 */
public ExecutionSummaryType getExecutionSummary(String arg0) throws RemoteException, CeaFault {
    try {
        return JAXB2Axis.convert(query.getSummary(arg0, this.cacheSecurityGuard()));
    } catch (Exception e) {
        logger.error("getExecutionSummary("+arg0+")", e);
        throw AxisFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("getExecutionSummary("+arg0+")", e);
        throw AxisFault.makeFault(new Exception("a throwable occurred in getExecutionSummary-"+e.getMessage(),e));
    }
}
/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getResults(java.lang.String)
 */
public ResultListType getResults(String arg0) throws RemoteException, CeaFault {
    try {
        return JAXB2Axis.convert(query.getResults(arg0, this.cacheSecurityGuard()));
    } catch (Exception e) {
        logger.error("getResults("+arg0+")", e);
        throw AxisFault.makeFault(e);
    } catch (Throwable e) {
        logger.error("getResults("+arg0+")", e);

        throw AxisFault.makeFault(new Exception("a throwable occurred in getResults-"+e.getMessage(),e));
    }
}
   
/** 
  * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#returnRegistryEntry()
  */
 public String returnRegistryEntry() throws RemoteException, CeaFault
 {
     try {
         return XMLUtils.DocumentToString(CEAComponentContainer.getInstance().getMetadataService().returnRegistryEntry());
     } catch (Exception e) {
		 logger.error("returnRegistryEntry()", e);
         throw AxisFault.makeFault(e);       
     } catch (Throwable e) {
		 logger.error("returnRegistryEntry()", e);
         throw AxisFault.makeFault(new Exception("A throwable occured in return registry entry-"+e.getMessage(),e));
     }
  }
 
  /**
   * Stores a security guard where worker threads can find it.
   * The guard is obtained from the Axis message-context, where it has been
   * loaded with the results of authentication. The delegated credentials for
   * the authenticated identity, if any, are loaded. If the request has not
   * been authenticated, this method stores a security guard with no
   * principals or credentials.
 * @return 
   * @TODO review all this caching business - I think it it broken and not needed anyway as he securityGuard is passed into the {@link Application} (which represents an applicaiton execution) at creation time.
   */
   private SecurityGuard cacheSecurityGuard() throws ClassNotFoundException, 
                                            InstantiationException, 
                                            IllegalAccessException, 
                                            CertificateException {
     AxisServiceSecurityGuard g = AxisServiceSecurityGuard.getInstanceFromContext();
     if(!g.isSignedOn())g.loadDelegation();
     CeaSecurityGuard.setInstanceInContext(g);
     return g;
   }

 
  /**
   * Retrives the Application object for a named execution.
   *
   * @return The Application; never null.
   * @throws ExecutionIDNotFoundException If the named execution is not current.
   */
  private Application getApplication(String job) throws ExecutionIDNotFoundException, PersistenceException {
    CEAComponents m = CEAComponentContainer.getInstance();
    ExecutionHistory h = m.getExecutionHistoryService();
    if (h.isApplicationInCurrentSet(job)) {
      return h.getApplicationFromCurrentSet(job);
    } 
    else { 
      return null;
    }
  }
  
}
