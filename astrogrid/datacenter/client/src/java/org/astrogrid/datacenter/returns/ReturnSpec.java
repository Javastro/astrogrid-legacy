/*
 * $Id: ReturnSpec.java,v 1.5 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import org.astrogrid.slinger.TargetIndicator;


/**
 * Specifies what will be returned from a query; subclasses implement
 * different return types.
 *
 * @author M Hill
 */

public abstract class ReturnSpec  {

   TargetIndicator target = null;
   String compression = null;
   long limit = -1;
   
   String format = VOTABLE; //defaults to VOTABLE
   
   public static final String VOTABLE  = "VOTable";
   public static final String HTML     = "Html";
   public static final String RAW      = "Raw"; //use for things like count
   
   /** No compression */
   public final static String NONE = "None";
   /** ZIP results */
   public final static String ZIP = "Zip";
   
   /** Set where the results are to be sent to */
   public void setTarget(TargetIndicator aTarget)  { this.target = aTarget; }

   /** Returns where the results are to be sent to */
   public TargetIndicator getTarget()              { return target; }

   /** See class constants for standard compressions, but bear in mind there
    * may be others suported by individual services*/
   public void setCompression(String aCompression) { this.compression = aCompression; }
   
   /** See class constants for standard compressions, but bear in mind there may
    * be other ones supported by individual services */
   public String getCompression()                  { return compression; }
   
   /** Sets Limit to number of result items to be returned */
   public void setLimit(long limit) {  this.limit = limit; }
   
   /** Returns Limit to number of result items to be returned */
   public long getLimit() {      return limit;  }
   
   public void setFormat(String aFormat)  {  this.format = aFormat; }

   public String getFormat()              {  return format; }
   
   
}
/*
 $Log: ReturnSpec.java,v $
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



