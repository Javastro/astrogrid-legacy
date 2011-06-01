/*
 * $Id: MimeTypes.java,v 1.4 2011/06/01 16:00:56 gtr Exp $
 *
 */


package org.astrogrid.io.mime ;

/**
 * Defines some 'standard' mime types used by astrogrid/the IVO.
 * @see http://www.iana.org/assignments/media-types/
 *
 */
public class MimeTypes  {

  /**
   * Parses a given name and returns the corresponding MIME-type. Null or empty
   * (zero-length after stripping leading and trailing white space) names are
   * defaulted to VOtable.
   *
   * @param rawName The given name (case doesn't matter; may be null or empty).
   * @return The MIME type.
   * @throws IllegalArgumentException If the given name is not recognized, null or empty.
   */
  public static String toMimeType(String rawName) {
    String trimName = (rawName == null)? "votable" : rawName.trim();
    String lcName = (trimName.length() == 0)? "votable" : trimName.toLowerCase();

    if ("votable".equals(lcName) ||
        "text/xml".equals(lcName) ||
        "application/x-votable+xml".equals(lcName) || // default encoding
        "application/x-votable".equals(lcName) || // Cone-search can use this
        VOTABLE.equals(lcName)) {
      return VOTABLE;
    }

    else if (VOTABLE_BINARY.equals(lcName) ||
             "votable/binary".equals(lcName)) {
      return VOTABLE_BINARY;
    }

    else if ("csv".equals(lcName) ||
             CSV.equals(lcName)) {
      return CSV;
    }

    else if ("tsv".equals(lcName) ||
             TSV.equals(lcName)) {
      return TSV;
    }

    else {
      throw new IllegalArgumentException (trimName + " is not a supported output-format");
    }
  }

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
   public static final String VOTABLE           = "application/x-votable+xml; encoding=\"tabledata\"";
   public static final String VOTABLE_BINARY = "application/x-votable+xml; encoding=\"binary\"";
   
   
//   public static final String VOLIST   = "text/xml +org.astrogrid.volist"  ;

   public static final String ZIP     = "application/zip"     ;

   public static final String IMAGE    = "image";
   public static final String GIF      = "image/gif";
   public static final String JPG      = "image/jpg";
   public static final String PNG      = "image/png";
}




