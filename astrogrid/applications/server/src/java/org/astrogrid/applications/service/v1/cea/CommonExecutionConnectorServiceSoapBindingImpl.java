/*
 * $Id: CommonExecutionConnectorServiceSoapBindingImpl.java,v 1.8 2004/09/10 13:53:14 jdt Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

import org.apache.axis.AxisFault;
import org.apache.axis.description.ServiceDesc;
import org.apache.axis.types.URI;

import java.io.PrintWriter;
import java.io.StringWriter;
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
             Tool ctool = Axis2Castor.convert(tool); 
            return cec.init(ctool, jobstepID.toString());
         }
         catch (Throwable e) {
           logger.error("init(_tool tool = " + tool + ") - Throwable caught:", e);
           //throw CeaFault.makeFault(e);
           throw makeFault("an exception occurred in init",e);
         }
   }

   /**
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(java.lang.String)
    */
   public boolean execute(String arg0) throws RemoteException, CeaFault {
       try {
           return cec.execute(arg0);
       } catch (Throwable e) {
           logger.error("execute( " + arg0 + ") - throwable caught:" ,e);
           throw makeFault("a throwable occurred in execute",e); 
       }
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws RemoteException, CeaFault {
      try {
            return cec.abort(executionId);
      } catch (Throwable t) {
          logger.error("abort(" + executionId + ") - throwable caught:" ,t);
          throw makeFault("a throwable occurred in abort",t);
   }
   }


   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId)
      throws RemoteException, CeaFault {
         try {
            org.astrogrid.applications.beans.v1.cea.castor.MessageType mess = query.queryExecutionStatus(executionId);
            return  Castor2Axis.convert(mess);
         }
         catch(Throwable e)
         {
             logger.error("queryExecutionStatus(" + executionId + ") - throwable caught",e);
            throw makeFault("an Throwable occurred in query status", e);
         }
   }
     
/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#registerResultsListener(java.lang.String, org.apache.axis.types.URI)
 */
public boolean registerResultsListener(String arg0, URI arg1) throws RemoteException, CeaFault {
    try {
        return query.registerResultsListener(arg0,new java.net.URI(arg1.toString()));
    } catch (Throwable e) {
        logger.error("registerResultsListener(" + arg0 + ", " + arg1 + ") - throwable caught",e);
        throw makeFault("a throwable occurred in registerResultsListener",e);
    }
}

/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#registerProgressListener(java.lang.String, org.apache.axis.types.URI)
 */
public boolean registerProgressListener(String arg0, URI arg1) throws RemoteException, CeaFault {
    try {
        return query.registerProgressListener(arg0,new java.net.URI(arg1.toString()));
    } catch (Throwable e) {
        logger.error("registerProgressListener(" + arg0 + ", " + arg1 + ") - throwable caught",e);
        throw makeFault("a throwable occurred in registerProgressListener",e);
    }
}

/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getExecutionSummary(java.lang.String)
 */
public ExecutionSummaryType getExecutionSummary(String arg0) throws RemoteException, CeaFault {
    try {
        return Castor2Axis.convert(query.getSummary(arg0));
    } catch (Throwable e) {
        logger.error("getExecutionSummary(" + arg0 + ") - throwable caught",e);
        throw makeFault("a throwable occurred in getExecutionSummary",e);
    }
}



/**
 * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getResults(java.lang.String)
 */
public ResultListType getResults(String arg0) throws RemoteException, CeaFault {
    try {
        return Castor2Axis.convert(query.getResults(arg0));
    } catch (Throwable e) {
        logger.error("getResults(" + arg0 +") - throwable caught",e);
        throw makeFault("a throwable occurred in getResults",e);
    }
}
   
/** 
  * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#returnRegistryEntry()
  */
 public String returnRegistryEntry() throws RemoteException, CeaFault
 {
	 if (logger.isTraceEnabled()) {
		logger.trace("returnRegistryEntry() -- start");
     }

     try {
         final String registryEntry = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
 		 if (logger.isTraceEnabled()) {
			logger.trace("returnRegistryEntry() - end");
		 }
         return registryEntry;     
     } catch (Throwable e) {
		 logger.error("returnRegistryEntry()", e);
         throw makeFault("A throwable occured in return registry entry",e);
     }
  }

 // cut-n-pasted from http://wiki.astrogrid.org/bin/view/Main/WebServiceExceptions - lets see if this gives any better error messages.
 protected AxisFault makeFault(String message, Throwable cause) {
     return makeFault(false,message,cause);
 }
 
 protected AxisFault makeFault(boolean blameClient, String message, Throwable cause) {

     AxisFault fault = new AxisFault(message);

     //set fault code according to standard (?) - should the client check what was sent
     //or is the problem on the server.
     if (blameClient) {
       fault.setFaultCode("Client");
     } else {
       fault.setFaultCode("Server");
     }

     fault.clearFaultDetails();  
     if (cause != null) {
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        fault.addFaultDetailString(writer.toString());
     }

     return fault;
  } 
 
}
