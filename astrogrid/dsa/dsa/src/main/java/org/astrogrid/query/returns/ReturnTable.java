/*
 * $Id: ReturnTable.java,v 1.2 2009/10/21 19:00:59 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.returns;

import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * A simple Return type, to be used for tables.
 * This is a cut-down version of the original ReturnTable
 * class, which contains extra column information required to
 * support the old Query model but not the new ADQL-based model.
 *
 * @author M Hill
 * @author K Andrews
 */

public class ReturnTable  extends ReturnSpec {

   /** Formats particular to tables */
   public static final String CSV          = MimeTypes.CSV;
   public static final String VOTABLE      = MimeTypes.VOTABLE;
   public static final String HTML         = MimeTypes.HTML;
   public static final String TSV          = MimeTypes.TSV;
   public static final String FITS         = MimeTypes.FITS;
  // public static final String VOTABLE_FITSLIST  = MimeTypes.VOTABLE_FITSLIST;
   public static final String VOTABLE_BINARY  = MimeTypes.VOTABLE_BINARY;
   public static final String VOTABLE_GENERIC = "application/x-votable+xml";
   public static final String VOTABLE_TEXT = "text/xml";
   
   /** Creates a definition specifying only the target destination */
   public ReturnTable(TargetIdentifier aTarget) {
      this.target = aTarget;
   }
   
   /** Creates a definition specifying target destination and table format */
   public ReturnTable(TargetIdentifier aTarget, String givenFormat) {
      this(aTarget);
      setFormat(givenFormat);
   }

   /** Returns true if there is a table-form in the  given format list */
   public static boolean isTableFormat(String[] formats) {
      for (int i = 0; i < formats.length; i++) {
         if (formats[i].trim().toLowerCase().equals(CSV) ||
             formats[i].trim().toLowerCase().equals(VOTABLE) ||
             formats[i].trim().toLowerCase().equals(VOTABLE_BINARY) ||
             formats[i].trim().toLowerCase().equals(HTML) ||
             formats[i].trim().toLowerCase().equals(FITS) ||
             formats[i].trim().toLowerCase().equals(VOTABLE_GENERIC) ||
             formats[i].trim().toLowerCase().equals(VOTABLE_TEXT)){
            return true;
            }
      }
      return false;
   }
   
   /** For debug and reference */
   public String toString() {
      String s = "[ReturnTable: target="+target+",";
      return s+super.toString()+"]";
   }
}
