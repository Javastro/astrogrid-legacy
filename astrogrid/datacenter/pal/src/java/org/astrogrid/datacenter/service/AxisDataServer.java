/*
 * $Id: AxisDataServer.java,v 1.3 2004/10/05 14:56:45 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.server.AxisServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.util.DomHelper;

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

public class AxisDataServer extends DataServer {
   
   /** Constant for makeFault - input from client has caused problem */
   public final static boolean CLIENTFAULT = true;
   /** Constant for makeFault - problem with server (or unknown) */
   public final static boolean SERVERFAULT = false;

   /** set during init to the url stem for this context, eg http://grendel12.roe.ac.uk/pal-6df  */
   protected static String contextUrlStem = null;
   
   
   /** Returns the url stem for this context, eg http://grendel12.roe.ac.uk/pal-6df  */
   public static String getUrlStem() {
      return contextUrlStem;
   }
   
   public String getContext() {
      try {
         AxisEngine engine = AxisServer.getServer(null);
         return engine.getApplicationSession().toString();
      } catch (AxisFault af) {
         log.error("Getting application context",af);
         return null;
      }
   }

   /**
    * Axis provides an AxisFault for reporting errors through SOAP.  This method
    * creates a fault from a message and a cause, and includes in the detail
    * the cause's (relevent) stack trace
    * @blameClient - true if the error is known to be caused by an input parameter - such as an
    * invalid query ID.
    */
   public AxisFault makeFault(boolean blameClient, String message, Throwable cause)  {
      
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
   public AxisFault makeFault(String message) {
      return makeFault(SERVERFAULT, message, null);
   }
   
}

/*
$Log: AxisDataServer.java,v $
Revision 1.3  2004/10/05 14:56:45  mch
Added new web interface and partial skynode

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.46  2004/09/06 20:23:00  mch
Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

Revision 1.45  2004/08/25 23:38:34  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.44  2004/08/18 22:29:21  mch
Take more general TargetIndicator rather than AGSL

Revision 1.43  2004/08/18 18:44:12  mch
Created metadata plugin service and added helper methods

Revision 1.42  2004/08/17 20:19:36  mch
Moved TargetIndicator to client

Revision 1.41  2004/03/18 20:43:07  mch
Context test cpde

Revision 1.40  2004/03/17 00:27:21  mch
Added v05 AxisDataServer

Revision 1.39  2004/03/15 17:12:28  mch
Added memory to status info

Revision 1.38  2004/03/14 04:13:04  mch
Wrapped output target in TargetIndicator

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

