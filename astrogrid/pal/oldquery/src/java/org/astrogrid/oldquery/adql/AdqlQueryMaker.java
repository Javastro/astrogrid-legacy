/*
 * $Id: AdqlQueryMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.adql;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.QueryException;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * Convenience routines for making a Query from an ADQL document, string or SOAP-generated model
 * <p>
 *
 * @author M Hill
 * @author K Andrews
 *
 * @deprecated This class uses the old query model OldQuery, which has
 * been deprecated and needs to be removed.
 */


public class AdqlQueryMaker  {


   /** Constructs a Query from the given ADQL DOM.  */
   public static OldQuery makeQuery(Element adql) throws QueryException {
      //should look at namespace to work out which version to use
      return AdqlXml074Parser.makeQuery(adql);
   }

   /** Convenience routine - creates a Query (object model) from the given ADQL string.
    * Includes a botch fix for linux browsers, which seem to insist on changing closing </Table>
    * and </Select> elements to lower case
    */
   public static OldQuery makeQuery(String adql) throws QueryException, SAXException, IOException {
      //botch fix for linux browsers
      adql.replaceAll("</table>", "</Table>");
      adql.replaceAll("</select>", "</Select>");
      
      return makeQuery(DomHelper.newDocument(adql).getDocumentElement());
   }

   /** Convenience routine - Constructs query from given inputstream
    */
   public static OldQuery makeQuery(InputStream in) throws QueryException, IOException, SAXException {
      return makeQuery(DomHelper.newDocument(in).getDocumentElement());
   }

   public static OldQuery makeQuery(String adql, TargetIdentifier target, String format) throws QueryException, SAXException, IOException, ParserConfigurationException {
      OldQuery query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }

   /** Constructs query from given inputstream
    */
   public static OldQuery makeQuery(InputStream in, TargetIdentifier target, String format) throws QueryException, IOException, SAXException, ParserConfigurationException {
      OldQuery query = makeQuery(in);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
  
   public static OldQuery makeQuery(Element adql, TargetIdentifier target, String format) throws QueryException {
      OldQuery query = makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
   }
   
   
}
/*
 $Log: AdqlQueryMaker.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.2.84.1  2006/04/10 16:17:44  kea
 Bits of registry still depending (implicitly) on old Query model, so
 moved this sideways into OldQuery, changed various old-model-related
 classes to use OldQuery and slapped deprecations on them.  Need to
 clean them out eventually, once registry can find another means to
 construct ADQL from SQL, etc.

 Note that PAL build currently broken in this branch.

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
