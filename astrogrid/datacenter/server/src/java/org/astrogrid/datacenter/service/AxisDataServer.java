/*
 * $Id: AxisDataServer.java,v 1.37 2004/03/14 00:39:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.*;

import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;

/**
 * A class for serving data through an Axis webservice implementation.  It is
 * abstract as subclasses should inherit from it and implement the appropriate
 * interfaces/use appropriate bindings to the particular version of the interface
 * <p>
 * (When Axis receives a SOAP message from the client it is routed to the subclass for processing.
 * It can be a singleton; state comes from the Queriers).

 * @author M Hill
 * @author Noel Winstanly
 *
 */

public abstract class AxisDataServer  {
   
   protected Log log = LogFactory.getLog(AxisDataServer.class);

   protected DataServer server = new DataServer();
   
   /** Constant for makeFault - input from client has caused problem */
   protected final static boolean CLIENTFAULT = true;
   /** Constant for makeFault - problem with server (or unknown) */
   protected final static boolean SERVERFAULT = false;
   
   /**
    * Axis provides an AxisFault for reporting errors through SOAP.  This method
    * creates a fault from a message and a cause, and includes in the detail
    * the cause's (relevent) stack trace
    * @blameClient - true if the error is known to be caused by an input parameter - such as an
    * invalid query ID.
    */
   protected AxisFault makeFault(boolean blameClient, String message, Throwable cause)  {
      
      log.error("AxisFault being generated: 'Throwing' exception "+cause+" to client, message="+message, cause);

      AxisFault fault = new AxisFault(message);

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
   
   /**
    * Convenience method to generate server error
    */
   protected AxisFault makeFault(String message) {
      return makeFault(SERVERFAULT, message, null);
   }
   
   /**
    * Useful mechanism for testing that clients can receive and process faults.
    * The useful bit of stack trace will be limited to this method but ho hum
    */
   public void throwFault() throws AxisFault {
      throw makeFault(CLIENTFAULT, "Client asked for this", new IOException("Client deliberately threw test fault "));
   }
   
   /**
    * Returns the metadata file as a string
    */
   public String getMetadata() throws AxisFault {
      try  {
         StringWriter sw = new StringWriter();
         InputStream in = MetadataServer.getMetadataUrl().openStream();
         if (in == null) throw new FileNotFoundException("Metadata file at "+MetadataServer.getMetadataUrl()+" missing");
         Piper.pipe(new InputStreamReader(in), sw);
         return sw.toString();
      }
      catch (Throwable e)  {
         throw makeFault(SERVERFAULT, "Could not access metadata", e);
     }
   }
   
   /**
    * Submits given query
    */
   public String submitQuery(Account user, Query query, Agsl resultsTarget, String requestedFormat, QuerierListener listener) throws AxisFault {
      try  {
         Querier querier = Querier.makeQuerier(user, query, resultsTarget, requestedFormat);
         querier.addListener(listener);
         server.querierManager.submitQuerier(querier);
         return querier.getId();
      }
      catch (Throwable e)  {
         throw makeFault(SERVERFAULT, "Submitting "+query+" for user "+user, e);
     }
   }
   
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if the server can't stop it it's a server-end problem.
    */
   public void abortQuery(Account user, String queryId) throws AxisFault {
      try {
         server.abortQuery(user, queryId);
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error aborting Query", e);
      }
   }
   
   /**
    * Returns the state of the query with the given id
    */
   public QuerierStatus getQueryStatus(Account user, String queryId) throws AxisFault {
      try {
         return server.getQueryStatus(user, queryId);
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error aborting Query", e);
      }
   }
   
   /**
    * Returns the state of the server
    */
   public DataServer.ServiceStatus getServerStatus(String queryId) throws AxisFault {
      try {
         return server.getServerStatus();
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error aborting Query", e);
      }
   }
   
   
   
}

/*
$Log: AxisDataServer.java,v $
Revision 1.37  2004/03/14 00:39:55  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.36  2004/03/13 23:38:46  mch
Test fixes and better front-end JSP access

Revision 1.35  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.34  2004/03/12 04:45:26  mch
It05 MCH Refactor

Revision 1.33  2004/03/08 15:57:42  mch
Fixes to ensure old ADQL interface works alongside new one and with old plugins

Revision 1.32  2004/03/08 00:39:02  mch
Minor error message change

Revision 1.31  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.30  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

