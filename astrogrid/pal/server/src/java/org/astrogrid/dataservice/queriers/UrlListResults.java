/*
 * $Id: UrlListResults.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLEncoder;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.out.TableWriter;

/**
 * Results which are a list of URLs
 *
 * @author M Hill
 */

public class UrlListResults extends TableResults {
   
   
   
   /** List of URLs to (probably) FITS files  */
   protected String[] urls;
   
   /**
    * Construct this wrapper around the given list of results
    */
   public UrlListResults(Querier parentQuerier, String[] results) {
      super(parentQuerier);
      this.urls = results;
   }
   
   /** Returns number of found files */
   public int getCount() throws IOException {
      return urls.length;
   }

  /**
    * Converts results to VOTable to given outputstream.  I (MCH) don't think this
    * is very pleasant, and will break when the votable format changes, but
    * is easy to fix...
    */
   public void writeVotable(TargetIdentifier target, QuerierProcessingResults statusToUpdate) throws IOException {

      target.setMimeType(ReturnTable.VOTABLE, querier.getUser());
      Writer out = target.resolveWriter(querier.getUser());
      
      PrintWriter printOut = new PrintWriter(new BufferedWriter(out));
      
      printOut.println("<!DOCTYPE VOTABLE SYSTEM 'http://us-vo.org/xml/VOTable.dtd'>");
      printOut.println("<VOTABLE version='1.0'>");
      
      /* don't know where to find this info - in the netadata I expect
       <DEFINITIONS>
       <COOSYS ID="myJ2000" system="eq_FK5" equinox="2000." epoch="2000."/>
       </DEFINITIONS>
       */
      
      printOut.println("<RESOURCE>");
      /* don't know where to find this info - in the netadata I expect
       <PARAM ID="RA" datatype="E" value="200.0"/>
       <PARAM ID="DE" datatype="E" value="40.0"/>
       <PARAM ID="SR" datatype="E" value="30.0"/>
       <PARAM ID="PositionalError" datatype="E" value="0.1"/>
       <PARAM ID="Credit" datatype="A" arraysize="*" value="Charles Messier, Richard Gelderman"/>
       */
      
      
      
      if (statusToUpdate != null) {
         statusToUpdate.newProgress("Adding File", getCount());
      }
      for (int i=0;i<urls.length;i++) {
         if (statusToUpdate != null) {
            statusToUpdate.setProgress(i);
         }
         printOut.println("<TABLE>");
         printOut.println("<DATA><FITS>");
         printOut.println("   <STREAM>"+URLEncoder.encode(urls[i])+"</STREAM>");
         
         printOut.println("</FITS></DATA>");
         printOut.println("</TABLE>");
      }

      if (statusToUpdate != null)   { statusToUpdate.clearProgress(); }
      
      printOut.println("</RESOURCE>");
      
      printOut.println("</VOTABLE>");
      
      printOut.flush();
   }
   
  /**
    * Converts results to VOTable to given outputstream.  I (MCH) don't think this
    * is very pleasant, and will break when the votable format changes, but
    * is easy to fix...
    */
   /** Subclasses implement suitable ways of writing their results to the given TableWriter    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {

      //need to do some better work on the metadata...
      
      tableWriter.startTable(new ColumnInfo[] {});
      
      if (statusToUpdate != null) {
         statusToUpdate.newProgress("Adding file", getCount());
      }
      
      for (int i=0;i<urls.length;i++) {
         if (querier.isAborted()) {
            tableWriter.abort();
            return;
         }
         if (statusToUpdate != null) {
            statusToUpdate.setProgress(i);
         }
         tableWriter.writeRow(new String[] { urls[i] });
      }

      tableWriter.endTable();
      
      if (statusToUpdate != null)   { statusToUpdate.clearProgress(); }
      
      tableWriter.close();
   }
  /**
    * Converts results to HTML to given outputstream.
    */
   public void writeHtml(TargetIdentifier target, QuerierProcessingResults statusToUpdate) throws IOException {
      
      target.setMimeType(ReturnTable.HTML, querier.getUser());
      Writer out = target.resolveWriter(querier.getUser());

      PrintWriter printOut = new PrintWriter(new BufferedWriter(out));
      
      printOut.println("<HTML>");
      
      printOut.println("<HEAD>");
      printOut.println("<TITLE>Query Results</TITLE>");
      printOut.println("</HEAD>");
      
      printOut.println("<BODY>");
      printOut.println("<h1>Query Results</h1>");
      printOut.println("<p>Query "+this.querier.getQuery());
      printOut.println("<p>");
      
      if (statusToUpdate != null) {
         statusToUpdate.newProgress("Adding File", getCount());
      }
      for (int i=0;i<urls.length;i++) {
         if (statusToUpdate != null) {
            statusToUpdate.setProgress(i);
         }
         printOut.println("   <P><a href='"+urls[i]+"'>"+urls[i]+"</a></P>");
      }
      
      if (statusToUpdate != null)   { statusToUpdate.clearProgress(); }

      printOut.println("</BODY>");
      
      printOut.println("</HTML>");
      
      printOut.flush();
   }
   

  /**
    * Converts results to CSV; just lists the files found (ie one col)
    */
   public void writeCSV(TargetIdentifier target, QuerierProcessingResults statusToUpdate) throws IOException {

      target.setMimeType(ReturnTable.CSV, querier.getUser());
      Writer out = target.resolveWriter(querier.getUser());

      PrintWriter printOut = new PrintWriter(new BufferedWriter(out));

      if (statusToUpdate != null) {
         statusToUpdate.newProgress("Adding File", getCount());
      }

      printOut.println("Matched URLs");
      for (int i=0;i<urls.length;i++) {
         if (statusToUpdate != null) {
            statusToUpdate.setProgress(i);
         }
         printOut.println(urls[i]);
      }
      
      if (statusToUpdate != null)   { statusToUpdate.clearProgress(); }

      printOut.flush();
   }
   
}

/*
 $Log: UrlListResults.java,v $
 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.2.2.7  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.2.2.6  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.2.2.5  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.2.2.4  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.2.2.3  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.2.2.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.2.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.2  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.1  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.4  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.6.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.15  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.14  2004/09/03 09:28:51  KevinBenson
 change the table to be in the for loop

 Revision 1.13  2004/09/02 10:26:24  mch
 Fixed XML form

 Revision 1.12  2004/09/01 12:30:34  mch
 Fix to Fits VOtables

 Revision 1.11  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.10  2004/08/05 15:14:22  KevinBenson
 small bug fix in the FitsREsults.  And now uses dates was teh result of the mber of kevin-dev-03-08-04

 Revision 1.9  2004/08/04 07:50:37  KevinBenson
 small change on the FitsResult to put in the url.  IndexGenerator was missing a check for a null

 Revision 1.8  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.7  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.6  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.5  2004/03/10 02:36:25  mch
 Added getCount

 Revision 1.4  2004/03/09 21:54:58  mch
 Added Writer methods to toVotables for JSPs

 Revision 1.3  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.2  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.1.6.1  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.1  2003/12/09 12:30:52  mch
 New Fits Result set

 Revision 1.4  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.3  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.2  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.6  2003/10/02 12:53:49  mch
 It03-Close

 */
