/*
 * $Id: ReturnTable.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.returns;

import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.oldquery.condition.Expression;
import org.astrogrid.oldquery.condition.NumericExpression;
import org.astrogrid.slinger.targets.TargetIdentifier;


/**
 * Used to define what a table of results
 * will look like
 *
 * @author M Hill
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class ReturnTable  extends ReturnSpec {

   boolean all = false; //special case similar to SELECT * (SQL)

   Expression[] colDefs = null;  //list of column definitions

   Expression[] sortOrder = null;
   
   /** Another format particular to tables */
   public static final String CSV          = MimeTypes.CSV;
   public static final String VOTABLE      = MimeTypes.VOTABLE;
   public static final String VOTABLE_FITSLIST  = MimeTypes.VOTABLE_FITSLIST;
   public static final String HTML         = MimeTypes.HTML;
   public static final String TSV          = MimeTypes.TSV;
   public static final String FITS         = MimeTypes.FITS;
   
   /** Creates a definitiont hat will return all columns */
   public ReturnTable(TargetIdentifier aTarget) {
      this.target = aTarget;
      all = true;
   }

   /** Creates a definitiont hat will return all the given columns */
   public ReturnTable(TargetIdentifier aTarget, Expression[] someColDefs) {
      this.target = aTarget;
      setColDefs(someColDefs);
   }
   
   /** Creates a definitiont hat will return all the columns in the requested format */
   public ReturnTable(TargetIdentifier aTarget, String givenFormat) {
      this(aTarget);
      setFormat(givenFormat);
   }

   /** Returns true if there is a table-form in the  given format list */
   public static boolean isTableFormat(String[] formats) {
      for (int i = 0; i < formats.length; i++) {
         if (formats[i].trim().toLowerCase().equals(CSV) ||
             formats[i].trim().toLowerCase().equals(VOTABLE) ||
             formats[i].trim().toLowerCase().equals(HTML) ||
             formats[i].trim().toLowerCase().equals(FITS)) {
            return true;
            }
      }
      return false;
   }
   
   public Expression[] getColDefs() { return colDefs; }

   public void setColDefs(Expression[] cols )  {
      if (cols == null) {
         all = true;
         this.colDefs = null;
      }
      else if ((cols.length == 1) && (cols[0].toString().trim().equals("*"))) {
         all = true;
         this.colDefs = null;
      }
      else {
         this.colDefs = cols;
         all = false;
      }
   }

   public Expression[] getSortOrder() { return sortOrder; }

   public void setSortOrder(NumericExpression[] order )  { this.sortOrder = order; }
   
   public boolean returnAll() {
      return all;
   }

   /** For debug & reference */
   public String toString() {
      String s = "[TableResults: target="+target+", cols=";
      if (all) {
         s = s +"*, ";
      }
      else {
         for (int i=0 ;i<colDefs.length ;i++ ) {
            s = s + colDefs[i]+", ";
         }
      }
      return s+super.toString()+"]";
   }
}
/*
 $Log: ReturnTable.java,v $
 Revision 1.2  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.1.2.2  2006/04/21 11:03:55  kea
 Slapped deprecations on everything.

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.4  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.3.10.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.3  2005/03/30 21:51:25  mch
 Fix to return Votable fits list for url list

 Revision 1.2  2005/03/30 18:25:45  mch
 fix for sql-server jdbc problem

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.7.6.5  2004/11/26 18:17:21  mch
 More status persisting, browsing and aborting

 Revision 1.7.6.4  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.7.6.3  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.7.6.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.7.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.7  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.6  2004/10/08 09:41:51  mch
 Returns cols are expressions not nec numeric

 Revision 1.5  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.4  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.3  2004/08/27 17:47:19  mch
 Added first servlet; started making more use of ReturnSpec

 Revision 1.2  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 Revision 1.2  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages



 */



