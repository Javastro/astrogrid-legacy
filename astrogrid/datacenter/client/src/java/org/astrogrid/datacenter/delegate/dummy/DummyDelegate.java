/*
 * $Id: DummyDelegate.java,v 1.14 2004/03/12 20:00:11 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.dummy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
   
/**
 * An implementation of the ConeSearcher and AdqlQuerier implementations that validates inputs and
 * returns valid results, but does not call any datacenter services.
 * Provided for unit and integration test purposes, so applications can run
 * against a realistic 'data center' without having to set one up.
 *
 * @todo not properly tidied after It04 refactoring
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DummyDelegate implements QuerySearcher, ConeSearcher {
   
   
   /** used for generating random results... */
   
   private static java.util.Random random = new java.util.Random();

   /** Status string - initialising/starting */
   public static final String STARTING = "Starting";
   /** Status string - query submitted, waitinf for response */
   public static final String WAITING = "Waiting for Server";
   /** Status string - resposne received, being processed */
   public static final String POST_PROCESSING = "Processing Results";
   /** Status string - response dealt with */
   public static final String FINISHED = "Finished";

   /** Pretend service id */
   public static final String QUERY_ID = "DummyId";

   /** Sample results file */
   public static final String RESULTSFILENAME = "ExampleResults.xml";
   
   /** Generally speaking don't use this directly - use the factory
    * method DatacenterDelegate.makeDelegate(null), which is a
    * so that it can make decisions on new sorts
    * of datacenter delegates in the future...
    */
   public DummyDelegate()
   {
   }

   /**
    * Dummy call does nothing
    */
   public void setTimeout(int givenTimeout)
   {
      //nothing needs done
   }

   
   /**
    * Attempt to stop a query
    */
   public void abortQuery(String id) throws IOException {
      // TODO
   }
   
   /**
    * Simple blocking query; submit Query
    */
   public InputStream askQuery(Query query, String resultsFormat) throws IOException {
      // TODO
      return null;
   }
   
   /**
    * Submits a query (asynchronous), returning a string identifying the query
    */
   public String submitQuery(Query query, Agsl resultsTarget, String resultsFormat) throws IOException {
      // TODO
      return null;
   }
   
   /**
    * Get Status of query
    */
   public String getQueryStatus(String id) throws IOException {
      // TODO
      return null;
   }
   
   /**
    * Simple cone-search call.
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius.
    * @return VOTable
    * @todo returns set example results, not those in the given ra/dec
    */
   public InputStream coneSearch(double ra, double dec, double sr) throws IOException
   {
      //hmmm needs to do more than this if the results are going to be realistic...
      URL  url = DummyDelegate.class.getResource(RESULTSFILENAME);
      return url.openConnection().getInputStream();
   }
   
   
   /**
    * private method for loading the example results document
    */
   public static Element getSampleResults()
   {
      //load example response votable
      URL url = null;
      try
      {
         url = DummyDelegate.class.getResource(RESULTSFILENAME);
         Document resultsDoc = DomHelper.newDocument(url.openConnection().getInputStream());

         return resultsDoc.getDocumentElement();
      }
      catch (Exception e)
      {
         if (url == null)
         {
            throw new RuntimeException("ExampleResults.xml not found",e);
         }
         else
         {
            throw new RuntimeException("Failed to create dummy/example results",e);
         }
      }
   }

   /**
    * returns example metadata file.
    */
   public String getMetadata() throws IOException
   {
      InputStream is = getClass().getResourceAsStream("ExampleMetadata.xml");
      StringWriter sw = new StringWriter();
      Piper.pipe(new InputStreamReader(is), sw);
      return sw.toString();
   }

   public String getStatus(String id) {
      return ""+QueryState.UNKNOWN;
   }
   
   
}

/*
$Log: DummyDelegate.java,v $
Revision 1.14  2004/03/12 20:00:11  mch
It05 Refactor (Client)

Revision 1.13  2004/03/08 15:54:57  mch
Better exception passing, removed Metdata

Revision 1.12  2004/03/07 21:11:43  mch
Changed apache XMLUtils to implementation-independent DomHelper

Revision 1.11  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.10  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.8  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.7.10.2  2004/01/08 09:42:26  nw
tidied imports

Revision 1.7.10.1  2004/01/07 15:32:32  nw
updated to implement full searcher interface

Revision 1.7  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.6  2003/11/25 15:47:47  mch
Tidied up and added SqlQuerier implementation

Revision 1.5  2003/11/18 10:43:55  mch
Removed dependency on ResponseHelper

Revision 1.4  2003/11/17 16:59:12  mch
ConeSearcher.coneSearch now returns stream not parsed element, throws IOException

Revision 1.3  2003/11/17 12:46:15  mch
Moving common to snippet

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.12  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.11  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.10  2003/09/18 13:12:14  nw
renamed delegate methods to match those in web service

Revision 1.9  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.8  2003/09/16 12:53:40  mch
DocHelper.wrap now throws IllegalArgumentException (runtime error) rather than SAXException, as XML is all softwired

Revision 1.7  2003/09/15 22:56:02  mch
Test fixes

Revision 1.6  2003/09/15 22:38:42  mch
Split spawnQuery into make and start, so we can add listeners in between

Revision 1.5  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.4  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.3  2003/09/15 16:11:44  mch
Fixes to handle updates when multiple queries are running through one delegate

Revision 1.2  2003/09/15 15:22:20  mch
Implemented asynch queries; improved dummy results

Revision 1.1  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

Revision 1.6  2003/09/08 16:34:04  mch
Added documentation

Revision 1.5  2003/09/07 18:52:39  mch
Added typesafe ServiceStatus

Revision 1.4  2003/09/05 12:01:56  mch
Minor doc/error changes

Revision 1.3  2003/08/29 07:57:01  maven
- changed '&' to '&amp;'

Revision 1.2  2003/08/27 23:54:20  mch
test bug fixes

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/


