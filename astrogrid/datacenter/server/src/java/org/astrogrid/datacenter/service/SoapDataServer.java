/*
 * $Id: SoapDataServer.java,v 1.2 2004/03/08 00:31:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.QuerierStatus;
import org.astrogrid.io.Piper;

/**
 * Provides methods suitable for a SOAP implementation of the Datacenter.  This
 * is an abstract class as we want to *force* people to subclass.  That means
 * that if the interface changes, a new subclass can be added without
 * interfering with the old one.
 *
 * @author M Hill
 * @author Noel Winstanly
 *
 */

public abstract class SoapDataServer    {

   protected static Log log = LogFactory.getLog(SoapDataServer.class);
   
   protected DataServer server = new DataServer();
   
   public SoapDataServer() {
   }
   
   /**
    * Returns the metadata file as a string
    */
   public String getMetadata() throws SOAPFaultException {
      try  {
         StringWriter sw = new StringWriter();
         Piper.pipe(new InputStreamReader(MetadataServer.getMetadataUrl().openStream()), sw);
         return sw.toString();
      }
      catch (Throwable e)  {
         throw makeSoapFault("Server", "Could not access metadata", e);
     }
   }

   /**
    * This routine is public so that we can test our client-side error
    * reporting */
   public void testFault(String message) throws SOAPFaultException {
      throw makeSoapFault(message);
   }
   
   /**
    * Sets up a SOAP Fault Exception suitable for passing the original cause of
    * the error across the web interface.
    * Note that it is not sufficient to throw this; the fault handler requires
    * some work to include the details in the fault response.
    * @see summary at http://www.w3schools.com/SOAP/soap_fault.asp
    * @see .Net-related article at http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnservice/html/service09172002.asp
    * @see JAX-RPC article (low level) at http://www.fawcette.com/javapro/2004_01/online/webservices_kjones_01_21_04/page2.aspx
    *
    * <p>
    * The actor is assumed to be this service.
    * <p>
    * This can be overridden by subclasses to throw faults more appropriate to
    * their publication mechanism.  For example AxisFault is more useful for
    * Axis services. Unfortunately AxisFault subclasses RuntimeException and
    * SOAPFaultException subclasses RemoteException (->IOException).
    * <p>
    * @code - Client, Server depending on whether the error is caused by
    * something wrong being sent from the client, or a failure inside the server
    */
   protected SOAPFaultException makeSoapFault(String code, String message, Throwable cause)  {
      
      log.error("An error has occured, so throwing "+cause+" to client, message="+message, cause);
      
      QName faultCode = new QName(code); //haven't thought what to do about this yet

      try {
         Detail detail = SOAPFactory.newInstance().createDetail();
         //put exception information into the detail nodes
         if (cause != null) {
            detail.addTextNode(cause.getClass().toString());
            detail.addTextNode(cause.toString());

            //stack trace
            StringWriter writer = new StringWriter();
            cause.printStackTrace(new PrintWriter(writer));
            detail.addTextNode(writer.toString());
         }

        return new SOAPFaultException(faultCode, message, cause.toString(), detail);
      }
      catch (SOAPException se) {
         log.error(se+" trying to build SOAPFaultException("+faultCode+", "+message+", "+cause.toString()+")");
         return new SOAPFaultException(faultCode, message, cause.toString(), null);
      }
      
   }
   
   /**
    * Convenience method for makeSoapFault(String, Throwable=null)
    */
   protected SOAPFaultException makeSoapFault(String message) {

      return makeSoapFault("Server", message, null);
   }
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    */
   public void abortQuery(Account user, String queryId) throws SOAPFaultException {
      try {
         server.abortQuery(user, queryId);
      }
      catch (IllegalArgumentException iae) {
         throw makeSoapFault("Client", "Aborting "+queryId, iae);//*probably* incorrect queryId
      }
      catch (Throwable th) {
         throw makeSoapFault("Server", "Aborting "+queryId, th);
      }
   
   }
   
   /**
    * Returns the status of the service with the given id
    */
   public QuerierStatus getQueryStatus(Account user, String queryId) throws RemoteException {
      try {
         return server.getQueryStatus(user, queryId);
      }
      catch (IllegalArgumentException iae) {
         throw makeSoapFault("Client", "Aborting "+queryId, iae);//*probably* incorrect queryId
      }
      catch (Throwable th) {
         throw makeSoapFault("Server", "Aborting "+queryId, th);
      }
   }
   
   
}

/*
$Log: SoapDataServer.java,v $
Revision 1.2  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

