/*
 * $Id: MimeTypes.java,v 1.2 2005/03/31 09:04:12 mch Exp $
 *
 */


package org.astrogrid.slinger.mime ;

/**
 * Defines some 'standard' mime types used by astrogrid/the IVO.
 * @see http://www.iana.org/assignments/media-types/
 *
 */
public interface MimeTypes  {

   public static final String PLAINTEXT   = "text/plain"  ;
   public static final String XML         = "text/xml"  ;
   public static final String HTML        = "text/html"  ;
   public static final String TSV        = "text/tab-separated-values"  ; //tab separated
   public static final String CSV        = "text/comma-separated-values"  ; //not official?

   
   public static final String FITS        = "application/fits"  ;

   //see ivoa forum comments http://www.ivoa.net/forum/dal/0406/0198.htm.
   public static final String VOTABLE           = "application/x-votable+xml";
   public static final String VOTABLE_FITSLIST  = "application/x-votable+xml+fitslist";
   
   public static final String JOB      = "text/xml +org.astrogrid.job"      ;
   public static final String WORKFLOW = "text/xml +org.astrogrid.workflow" ;

   public static final String ADQL     = "text/xml +org.astrogrid.adql"     ;
//   public static final String VOLIST   = "text/xml +org.astrogrid.volist"  ;

}


