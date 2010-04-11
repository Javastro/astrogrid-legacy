/*
 * $Id: MimeTypes.java,v 1.2 2010/04/11 21:19:20 gtr Exp $
 *
 */


package org.astrogrid.io.mime ;

/**
 * Defines some 'standard' mime types used by astrogrid/the IVO.
 * @see http://www.iana.org/assignments/media-types/
 *
 */
public interface MimeTypes  {

   //text ones
   public static final String TEXT = "text";
   public static final String PLAINTEXT   = "text/plain"  ;
   public static final String XML         = "text/xml"  ;
   public static final String HTML        = "text/html"  ;
   public static final String TSV         = "text/tab-separated-values";
   public static final String CSV         = "text/csv";

   public static final String JOB      = "text/xml +org.astrogrid.job"      ;
   public static final String WORKFLOW = "text/xml +org.astrogrid.workflow" ;

   public static final String ADQL     = "text/xml +org.astrogrid.adql"     ;

   
   // application/fits is the general mimetype for FITS.
   // Simple FITS images (which use the subset of FITS described in the
   // original standard) can have mimetype image/fits.  
   // See RFC4047 at http://fits.gsfc.nasa.gov/
   public static final String FITS        = "application/fits"  ;
   public static final String FITS_IMAGE  = "image/fits"  ;

   //see ivoa forum comments http://www.ivoa.net/forum/dal/0406/0198.htm.
   // "encoding=" part added by KEA at MB's suggestion
   // KONA TOFIX - Conesearch says text/xml;content=x-votable
   // http://www.ivoa.net/Documents/PR/DAL/ConeSearch-20070914.html#req
   public static final String VOTABLE           = "application/x-votable+xml; encoding=\"TABLEDATA\"";
   public static final String VOTABLE_BINARY = "application/x-votable+xml; encoding=\"BINARY\"";
   // This one is not in use
   //public static final String VOTABLE_FITSLIST  = "application/x-votable+xml; encoding=\"FITSLIST\"";
   
//   public static final String VOLIST   = "text/xml +org.astrogrid.volist"  ;

   public static final String ZIP     = "application/zip"     ;

   public static final String IMAGE    = "image";
   public static final String GIF      = "image/gif";
   public static final String JPG      = "image/jpg";
   public static final String PNG      = "image/png";
}




