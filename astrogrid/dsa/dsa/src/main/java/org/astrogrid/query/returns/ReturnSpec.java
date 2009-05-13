/*
 * $Id: ReturnSpec.java,v 1.1 2009/05/13 13:20:40 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.returns;

import org.astrogrid.io.mime.MimeNames;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.query.QueryException;

/**
 * Specifies what will be returned from a query; subclasses implement
 * different return types.
 *
 * KEA: As at 17.02.06, both this class and the Query class provide a 
 * "limit" variable.  Other features needed by ADQL (such as an "Allow" 
 * variable) are missing from the query model.  
 *
 * I feel that this class ought only to store information relating
 * to the formatting and destination of the return data, rather than
 * things which might be required in constructing the actual native
 * (SQL or whatever) query.
 *
 * Therefore, am setting up a new ConstraintSpec class to hold limit,
 * allow variables and other query constraints as required.
 *
 * @author K Andrews
 * @author M Hill
 */

public abstract class ReturnSpec  {

   TargetIdentifier target = null;
   String compression = NONE;
   
//   String[] formats = new String[] { DEFAULT }; //list of requested formats
   String format = DEFAULT; //single result format
   
   public static final String DEFAULT  = "Default";
   public static final String RAW      = "Raw";
   
   /** No compression */
   public final static String NONE = "Uncompressed";
   /** ZIP results */
   public final static String ZIP = "Zip";
   
   /** Set where the results are to be sent to */
   public void setTarget(TargetIdentifier aTarget)  { this.target = aTarget; }

   /** Returns where the results are to be sent to */
   public TargetIdentifier getTarget()              { return target; }

   /** See class constants for standard compressions, but bear in mind there
    * may be others suported by individual services*/
   public void setCompression(String aCompression) throws QueryException {
      if ((aCompression == null) || aCompression.equals(NONE) || aCompression.toUpperCase().equals("NONE")) {
         compression = NONE;
      }
      else if (aCompression.equals(ZIP)) {
         compression = ZIP;
      }
      else {
         //throw new IllegalArgumentException("Unknown compression type '"+aCompression+"'");
         throw new QueryException("Unknown compression type '"+aCompression+"'");
      }
   }
   
   /** See class constants for standard compressions, but bear in mind there may
    * be other ones supported by individual services */
   public String getCompression()                  { return compression; }
   
//   public void setFormats(String[] someFormats)  {  this.formats = someFormats; }

   /** Give human string format (see MimeNames) or mime type */
   public void setFormat(String aFormat)        {
      this.format = MimeNames.getMimeType(aFormat);
      if (this.format == null) {
         throw new IllegalArgumentException("Format "+aFormat+" not understood");
      }
   }

   public String getFormat()                    {  return format; }

   /*
   public String[] getFormats()              {  return formats; }

   /** Returns true if this speficifaction includes the given format
   public boolean includesFormat(String aFormat) {
      for (int i = 0; i < formats.length; i++) {
         if (formats[i].trim().toLowerCase().equals(aFormat.trim().toLowerCase())) {
            return true;
         }
      }
      return false;
   }
   */
   public String toString() {
      String s = "Format "+format+", ";
//      for (int i = 0; i < formats.length; i++) {
//         s = s +formats[i]+", ";
//      }
      if (compression != NONE) {
         s = s + compression;
      }
      return s;
   }
}
/*
 $Log: ReturnSpec.java,v $
 Revision 1.1  2009/05/13 13:20:40  gtr
 *** empty log message ***

 Revision 1.4  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.3.2.1  2006/04/21 11:54:05  kea
 Changed QueryException from a RuntimeException to an Exception.

 Revision 1.3  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.2.62.2  2006/02/20 19:42:08  kea
 Changes to add GROUP-BY support.  Required adding table alias field
 to ColumnReferences, because otherwise the whole Visitor pattern
 falls apart horribly - no way to get at the table aliases which
 are defined in a separate node.

 Revision 1.2.62.1  2006/02/16 17:13:04  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.2  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.1.1.1.24.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.6.6.7  2004/11/26 18:17:21  mch
 More status persisting, browsing and aborting

 Revision 1.6.6.6  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.6.6.5  2004/11/25 08:32:34  mch
 added human-friendly formats

 Revision 1.6.6.4  2004/11/23 17:46:52  mch
 Fixes etc

 Revision 1.6.6.3  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.6.6.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.6.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.6  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.5  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.4  2004/10/05 19:20:00  mch
 Added raw

 Revision 1.3  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.2  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.2  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages



 */



