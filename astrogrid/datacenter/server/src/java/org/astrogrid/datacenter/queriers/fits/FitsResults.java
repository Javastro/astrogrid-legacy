/*
 * $Id: FitsResults.java,v 1.5 2004/03/10 02:36:25 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Holds the list of found FITS files.
 *
 * @author M Hill
 */

public class FitsResults implements QueryResults {
   
   /** List of FITS files - probably URLs */
   protected String[] filenames;
   
   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need a workspace
    * for any temporary files created when doing conversions
    */
   public FitsResults(String[] results) {
      this.filenames = results;
   }
   
   /** Returns number of found files */
   public int getCount() throws IOException {
      return filenames.length;
   }

   /**
    * Converts the resultset to VOTable Document.
    */
   public Document toVotable() throws IOException, SAXException {
      try {
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         toVotable(ba);
         ba.close();
         return DomHelper.newDocument(new ByteArrayInputStream(ba.toByteArray()));
      }
      catch (ParserConfigurationException e) {
         RuntimeException ioe = new RuntimeException("Error in program configuration: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
   }
   
   /** Stream version of the writer */
   public void toVotable(OutputStream out) throws IOException {
      toVotable(new OutputStreamWriter(out));
   }
   

  /**
    * Converts results to VOTable to given outputstream.  I (MCH) don't think this
    * is very pleasant, and will break when the votable format changes, but
    * is easy to fix...
    */
   public void toVotable(Writer out) throws IOException {

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
      printOut.println("<TABLE>");
      
      
      printOut.println("<DATA>");
      
      for (int i=0;i<filenames.length;i++) {
         printOut.println("<FITS>");
         printOut.println("   <STREAM>"+filenames+"</STREAM>");
         
         printOut.println("</FITSA>");
      }
      
      printOut.println("</DATA>");
      
      printOut.println("</TABLE>");
      
      printOut.println("</RESOURCE>");
      
      printOut.println("</VOTABLE>");
      
      printOut.flush();
   }
   
   
   
}

/*
 $Log: FitsResults.java,v $
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
