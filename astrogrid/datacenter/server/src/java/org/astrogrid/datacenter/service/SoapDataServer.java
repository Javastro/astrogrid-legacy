/*
 * $Id: SoapDataServer.java,v 1.1 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import org.astrogrid.datacenter.queriers.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.astrogrid.io.Piper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

   protected static Log log = LogFactory.getLog(AxisDataServer.class);
   
   protected DataServer server = new DataServer();
   
   public SoapDataServer() {
   }
   
   /**
    * Returns the metadata file as a string
    */
   public String getMetadataString() {
      try  {
         StringWriter sw = new StringWriter();
         Piper.pipe(new InputStreamReader(server.getMetadataStream()), sw);
         return sw.toString();
      }
      catch (Throwable e)  {
         throw makeSoapFault("Could not access metadata", e);
     }
   }

   /**
    * Sets up a SOAP Fault Exception suitable for passing the original cause of
    * the error across the web interface
    */
   protected SOAPFaultException makeSoapFault(String message, Throwable cause) {
      
      log.error("Throwing "+cause+" to client, message="+message, cause);

      QName faultCode = new QName("NoCode"); //haven't thought what to do about this yet

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

      return makeSoapFault(message, null);
   }
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    */
   public void abortQuery(Account user, String queryId) {
      try {
         server.abortQuery(user, queryId);
      }
      catch (Throwable th) {
         throw makeSoapFault("Aborting "+queryId, th);
      }
   
   }
   
   /**
    * Returns the status of the service with the given id
    */
   public QuerierStatus getQueryStatus(Account user, String queryId) {
      try {
         return server.getQueryStatus(user, queryId);
      }
      catch (Throwable th) {
         throw makeSoapFault("Aborting "+queryId, th);
      }
   }
   
   
}

/*
$Log: SoapDataServer.java,v $
Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

