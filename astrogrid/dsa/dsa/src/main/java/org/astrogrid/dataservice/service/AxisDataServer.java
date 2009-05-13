/*
 * $Id: AxisDataServer.java,v 1.1.1.1 2009/05/13 13:20:27 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.server.AxisServer;
import org.apache.axis.transport.http.HTTPConstants;
import org.astrogrid.io.account.LoginAccount;

/**
 * A class for serving data through an Axis webservice implementation.  Implementations
 * are the Axis interface classes and should 'have' it rather than 'subclass' it; if
 * they 'subclass' it then this classes methods get exposed
 * <p>
 * It can be a singleton; state comes from the Queriers.

 * @author M Hill
 * @author Noel Winstanly
 *
 */

public class AxisDataServer extends DataServer {
   
   /** Constant for makeFault - input from client has caused problem */
   public final static boolean CLIENTFAULT = true;
   /** Constant for makeFault - problem with server (or unknown) */
   public final static boolean SERVERFAULT = false;

   
   /** Works out the url stem from the Message axis context  */
   public static String getMessageContextUrlStem() {
      MessageContext context = MessageContext.getCurrentContext();
      if (context == null) {
         return null;
      }
      HttpServletRequest req = (HttpServletRequest) context.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
      try {
         log.debug("AxisDataServer server "+req.getServerName()+":"+req.getServerPort()+"/"+req.getContextPath());
         URL server = new URL("http", req.getServerName(), req.getServerPort(), req.getContextPath());
//         log.debug("AxisDataServer finds url stem "+server+" in request "+req);
         return server.toString();
      }
      catch (MalformedURLException e) {
         //shouldn't happen
         log.error(e+" getting url from HttpServletRequest",e);
         throw new RuntimeException("URL from HttpServletRequest was malformed??! (AxisDataService_v05): ",e);
      }
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

   /** Returns some ID of the client making the call to this axis service */
   public static String getSource() {
      MessageContext context = MessageContext.getCurrentContext();

      if (context != null) {
         //get http request that contained the message
         HttpServletRequest request = (HttpServletRequest) context.getProperty(org.apache.axis.transport.http.HTTPConstants.MC_HTTP_SERVLETREQUEST);
   
         //return remove address of request
         return request.getRemoteHost();
      }
      return "";
   }

   /** Returns user given in SOAP message */
   public Principal getUser() {
      if (MessageContext.getCurrentContext() != null) {
         String username = MessageContext.getCurrentContext().getUsername();
         if ((username != null) && (username.trim().length() >0)) {
            return new LoginAccount(username, MessageContext.getCurrentContext().getPassword());
         }
      }
      
      return LoginAccount.ANONYMOUS;
   }

   /**
    * Axis provides an AxisFault for reporting errors through SOAP.  This method
    * creates a fault from a message and a cause, and includes in the detail
    * the cause's (relevent) stack trace
    * @blameClient - true if the error is known to be caused by an input parameter - such as an
    * invalid query ID.
    */
   public AxisFault makeFault(boolean blameClient, String message, Throwable cause)  {
      
      log.error(message+" [...will 'throw' AxisFault '"+cause+"' to client]", cause);

      AxisFault fault = new AxisFault(message);

      /* these fault codes are deprecated
      if (blameClient) {
         fault.setFaultCode("Client");
      } else {
         fault.setFaultCode("Server");
      }
       */
      
      fault.clearFaultDetails();
      while (cause != null) {
         StringWriter writer = new StringWriter();
         cause.printStackTrace(new PrintWriter(writer));
         fault.addFaultDetailString(writer.toString());
         cause = cause.getCause();
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
Revision 1.1.1.1  2009/05/13 13:20:27  gtr


Revision 1.3  2005/05/27 16:21:02  clq2
mchv_1

Revision 1.2.24.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.2  2005/03/11 15:17:52  mch
removed calls to deprecated setFault

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.7.12.4  2004/12/07 21:21:09  mch
Fixes after a days integration testing

Revision 1.7.12.3  2004/12/05 19:33:16  mch
changed skynode to 'raw' soap (from axis) and bug fixes

Revision 1.7.12.2  2004/11/29 22:33:42  mch
added better logging and fix for protocol oops

Revision 1.7.12.1  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.7  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.6.8.1  2004/11/02 19:41:26  mch
Split TargetIndicator to indicator and maker

Revision 1.6  2004/10/08 15:16:04  mch
More on providing status

Revision 1.5  2004/10/06 11:35:35  mch
A bit of tidying up around the web service interfaces.First stage SkyNode implementation

Revision 1.4  2004/10/05 19:23:07  mch
Added Kevin's url getter

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

