/*
 * $Id: AdqlQueryMaker.java,v 1.2 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Convenience routines for making a Query from an ADQL document, string or SOAP-generated model
 * <p>
 *
 * @author M Hill
 */


public class AdqlQueryMaker  {


   
   /** Constructs a Query from the given ADQL DOM.  */
   public static Query makeQuery(Element adql) throws QueryException {
      //should look at namespace to work out which version to use
      return AdqlXml074Parser.makeQuery(adql);
   }

   /** Convenience routine - creates a Query (object model) from the given ADQL string.
    * Includes a botch fix for linux browsers, which seem to insist on changing closing </Table>
    * and </Select> elements to lower case
    */
   public static Query makeQuery(String adql) throws QueryException, SAXException, IOException {
      //botch fix for linux browsers
      adql.replaceAll("</table>", "</Table>");
      adql.replaceAll("</select>", "</Select>");
      
      return makeQuery(DomHelper.newDocument(adql).getDocumentElement());
   }

   /** Convenience routine - Constructs query from given inputstream
    */
   public static Query makeQuery(InputStream in) throws QueryException, IOException, SAXException {
      return makeQuery(DomHelper.newDocument(in).getDocumentElement());
   }

   public static Query makeQuery(String adql, TargetIdentifier target, String format) throws QueryException, SAXException, IOException, ParserConfigurationException {
      Query query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }

   /** Constructs query from given inputstream
    */
   public static Query makeQuery(InputStream in, TargetIdentifier target, String format) throws QueryException, IOException, SAXException, ParserConfigurationException {
      Query query = makeQuery(in);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
  
   public static Query makeQuery(Element adql, TargetIdentifier target, String format) throws QueryException {
      Query query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
   
   
}
/*
 $Log: AdqlQueryMaker.java,v $
 Revision 1.2  2005/03/21 18:31:50  mch
 Included dates; made function types more explicit

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.2  2005/02/16 21:19:00  mch
 started incoporating into maven

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.8.6.4  2004/12/03 18:30:30  mch
 Removed most dependencies on generated skynode code

 Revision 1.8.6.3  2004/11/22 00:57:15  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.8.6.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.8.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.8  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.7  2004/11/03 05:14:33  mch
 Bringing Vizier back online

 Revision 1.6  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.5.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.5  2004/10/13 01:25:46  mch
 makes ADQL/sql rather than std sql (leave in CIRCLE)

 Revision 1.4  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */



