/*
 * $Id: WebDelegate_v05.java,v 1.11 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agws;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Hashtable;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.axisdataserver.v05.AxisDataServerV05SoapBindingStub;
import org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_ServiceLocator;
import org.astrogrid.datacenter.axisdataserver.v05.QueryStatusSoapyBean;
import org.astrogrid.datacenter.delegate.ClientQueryListener;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.targets.UriTarget;

/**
 * A standard AstroGrid datacenter delegate implementation, based on
 * http messaging with an Apache/Tomcat/Axis server.
 * <p>
 * Provides access to 'ADQL-compliant' web services, such as AstroGrid PAL ones
 * and (hopefully?) skynodes when they are implemented.
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */
  
   
public class WebDelegate_v05 implements QuerySearcher, ConeSearcher {
   
   Log log = LogFactory.getLog(WebDelegate_v05.class);
   
   /** listeners indexed by query id */
   Hashtable listeners = new Hashtable();
   
   /** Generated binding code that mirrors the service's methods */
   private AxisDataServerV05SoapBindingStub binding;
   
   /** endpoint for error messages */
   private URL endpoint = null;
   
   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public WebDelegate_v05(URL givenEndPoint) throws AxisFault
   {
      this.endpoint = givenEndPoint;
      binding = new AxisDataServerV05SoapBindingStub(givenEndPoint, new AxisDataServer_v05_ServiceLocator());
   }
   
   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown, in milliseconds
    */
   public void setTimeout(int millis) {
      binding.setTimeout(millis);
   }
   
   /** Returns the metadata  */
   public String getMetadata() throws IOException {
      try {
         return binding.getMetadata();
      }
      catch (IOException ioe) {
         //add URL
         IOException nioe = new IOException(ioe.getMessage()+", endpoint="+endpoint);
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
   }
   
   
   /**
    * Attempt to stop a query
    */
   public void abortQuery(String id) throws IOException {
      try {
         binding.abortQuery(id);
      }
      catch (IOException ioe) {
         //add URL
         IOException nioe = new IOException(ioe.getMessage()+", endpoint="+endpoint);
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
   }

   /**
    * Submits a query (asynchronous), returning a string identifying the query
    */
   public String submitQuery(Query query) throws IOException {
      assert (query.getResultsDef().getTarget() instanceof UriTarget) : "Specify Target using a URI";

      try {
         return binding.submitAdqlQuery(Adql074Writer.makeAdql(query),
                                     ((UriTarget) query.getResultsDef().getTarget()).toURI().toString(),
                                     query.getResultsDef().getFormat());
      }
      catch (IOException ioe) {
         //add URL
         IOException nioe = new IOException(ioe.getMessage()+", endpoint="+endpoint);
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
   }
   
   /**
    * Simple cone-search call.
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius in decimal degrees.
    * @return InputStream to results document, including votable
    */
   public InputStream coneSearch(double ra, double dec, double sr) throws IOException {
      Query coneQuery = SimpleQueryMaker.makeConeQuery(ra, dec, sr, new ReturnTable(null, "VOTABLE"));
      if (coneQuery.getScope() != null) {
         //make sure no scope set from local config, bit of a botch
         coneQuery.setScope(null);
      }
      return askQuery(coneQuery);
   }
   
   /**
    * Simple blocking query; send Query, return stream to results
    */
   public InputStream askQuery(Query query) throws IOException {
      String results = null;
      
      if (query.getResultsDef().getTarget() != null) {
         throw new IllegalArgumentException("Use v06 delegates to do blocking queries that send results elsewhere.  Otherwise remove target and read returned results");
      }

      try {
         results = binding.askAdqlQuery(Adql074Writer.makeAdql(query), query.getResultsDef().getFormat());
      }
      catch (IOException ioe) {
         //add URL
         IOException nioe = new IOException(ioe.getMessage()+", endpoint="+endpoint);
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
      
      return new StringBufferInputStream(results);
   }
  
   
   /**
    * Get Status of query
    */
   public String getStatus(String id) throws IOException {
      try {
         QueryStatusSoapyBean status = binding.getQueryStatus(id);
         return status.getState();
      }
      catch (IOException ioe) {
         //add URL
         IOException nioe = new IOException(ioe.getMessage()+", endpoint="+endpoint);
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
   }
   

   public void addListener(String queryId, ClientQueryListener aListener) {
   }
   
   public void removeListener(String queryId) {
   }
   
   public void removeListener(ClientQueryListener aListener) {
   }
      
   public class StatusPoller implements Runnable {
      
      public void run() {
         //poll service for statuses of indexed listeners
      }
   }
}

/*
 $Log: WebDelegate_v05.java,v $
 Revision 1.11  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.10  2004/11/03 12:13:26  mch
 Fixes to branch cockup, plus katatjuta Register and get cone (for examples)

 Revision 1.9  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.5.8.3  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.5.8.2  2004/10/29 17:56:14  mch
 Added comment

 Revision 1.5.8.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.5  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.4  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.3  2004/09/28 14:58:45  mch
 Removed obsolete 4.1 web interface

 Revision 1.2  2004/08/31 17:44:02  mch
 Fix to getStatus

 Revision 1.1  2004/04/22 15:14:34  mch
 Introduced WebDelegate_v05

 Revision 1.2  2004/03/13 16:19:38  mch
 Tidying up after refactor

 Revision 1.1  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.28  2004/03/09 13:01:08  mch
 Removed self import

 Revision 1.27  2004/03/08 15:54:57  mch
 Better exception passing, removed Metdata

 Revision 1.26  2004/03/07 21:10:55  mch
 Changed apache XMLUtils to implementation-independent DomHelper

 Revision 1.25  2004/03/06 19:34:21  mch
 Merged in mostly support code (eg web query form) changes

 Revision 1.23  2004/03/02 01:37:50  mch
 Updates from changes to StoreClient and AGSLs

 Revision 1.22  2004/02/24 16:04:29  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.21  2004/02/17 03:37:27  mch
 Various fixes for demo

 Revision 1.20  2004/02/16 23:33:42  mch
 Changed to use Account and AttomConfig

 Revision 1.19  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 Revision 1.18  2004/02/15 23:09:04  mch
 Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

 Revision 1.17  2004/01/13 00:32:47  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.16  2004/01/08 15:48:17  mch
 Allow myspace references to be given
$Log: WebDelegate_v05.java,v $
Revision 1.11  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.10  2004/11/03 12:13:26  mch
Fixes to branch cockup, plus katatjuta Register and get cone (for examples)

Revision 1.9  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.5.8.3  2004/11/02 19:41:26  mch
Split TargetIndicator to indicator and maker

Revision 1.5.8.2  2004/10/29 17:56:14  mch
Added comment

Revision 1.5.8.1  2004/10/21 19:10:24  mch
Removed deprecated translators, moved SqlMaker back to server,

Revision 1.5  2004/10/07 10:34:44  mch
Fixes to Cone maker functions and reading/writing String comparisons from Query

Revision 1.4  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.3  2004/09/28 14:58:45  mch
Removed obsolete 4.1 web interface

Revision 1.2  2004/08/31 17:44:02  mch
Fix to getStatus

Revision 1.1  2004/04/22 15:14:34  mch
Introduced WebDelegate_v05

Revision 1.2  2004/03/13 16:19:38  mch
Tidying up after refactor

Revision 1.1  2004/03/12 20:00:11  mch
It05 Refactor (Client)

Revision 1.28  2004/03/09 13:01:08  mch
Removed self import

Revision 1.27  2004/03/08 15:54:57  mch
Better exception passing, removed Metdata

Revision 1.26  2004/03/07 21:10:55  mch
Changed apache XMLUtils to implementation-independent DomHelper

Revision 1.25  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.23  2004/03/02 01:37:50  mch
Updates from changes to StoreClient and AGSLs

Revision 1.22  2004/02/24 16:04:29  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.21  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.20  2004/02/16 23:33:42  mch
Changed to use Account and AttomConfig

Revision 1.19  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

Revision 1.18  2004/02/15 23:09:04  mch
Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

Revision 1.17  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.13.2.4  2004/01/08 09:42:26  nw
tidied imports

Revision 1.13.2.3  2004/01/08 09:10:20  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.13.2.2  2004/01/07 13:01:44  nw
removed Community object, now using User object from common

Revision 1.13.2.1  2004/01/07 11:49:55  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.13  2003/12/16 16:19:27  mch
minor exception check

 Revision 1.15  2004/01/08 12:55:15  mch
 Finished implementing ApplicationController interface

 Revision 1.14  2004/01/05 19:06:26  mch
 Introduced ApplicationController interface

 Revision 1.13  2003/12/16 16:19:27  mch
 minor exception check

 Revision 1.12  2003/12/15 14:30:50  mch
 Fixes to load doc from string not file, and use correct version of adql

 Revision 1.11  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.10  2003/12/01 16:53:16  nw
 dropped _QueryId, back to string

 Revision 1.9  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.8  2003/11/25 15:47:17  mch
 Added certification

 Revision 1.7  2003/11/25 11:54:41  mch
 Added framework for SQL-passthrough queries

 Revision 1.6  2003/11/21 17:30:19  nw
 improved WSDL binding - passes more strongly-typed data

 Revision 1.5  2003/11/18 14:25:23  nw
 altered types to fit with new wsdl

 Revision 1.4  2003/11/17 20:47:57  mch
 Adding Adql-like access to Nvo cone searches

 Revision 1.3  2003/11/17 12:32:27  mch
 Moved QueryStatus to query pacakge

 Revision 1.2  2003/11/17 12:12:28  nw
 first stab at mavenizing the subprojects.

 Revision 1.1  2003/11/14 00:36:40  mch
 Code restructure

 Revision 1.4  2003/11/06 22:04:48  mch
 Temporary fixes to work with old version of AxisDataServer

 Revision 1.3  2003/11/05 18:52:53  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.2  2003/10/13 14:13:47  nw
 massaged one method to fit wih new delegate. still lots more to do here
 - don't understand intentions here myself

 Revision 1.1  2003/10/06 18:55:21  mch
 Naughtily large set of changes converting to SOAPy bean/interface-based delegates



 */


