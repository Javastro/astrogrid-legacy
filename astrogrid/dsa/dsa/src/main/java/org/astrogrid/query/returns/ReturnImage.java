/*
 * $Id: ReturnImage.java,v 1.1 2009/05/13 13:20:40 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.returns;

import org.astrogrid.slinger.targets.TargetIdentifier;


/**
 * Used to define what an image result should be; the list of formats are
 * a set of requests, in order of priority.
 *
 * @author M Hill
 */

public class ReturnImage  extends ReturnSpec {

   /** image formats  */
   public static final String JPG     = "image/jpeg";
   public static final String FITS    = "image/fits";
   public static final String GIF     = "image/gif";

//   private String[] formats = new String[] { DEFAULT }; //by default
   
   /** Creates a definition with the default format */
   public ReturnImage(TargetIdentifier aTarget) {
      setTarget(target);
   }

   /** Creates a definition with the given formats request */
   public ReturnImage(TargetIdentifier aTarget, String someFormats) {
      setTarget(aTarget);
      this.format = someFormats;
   }

   /** Returns true if there is a table-form in the  given format list */
   public static boolean isImageFormat(String[] formats) {
      for (int i = 0; i < formats.length; i++) {
         if (formats[i].trim().toLowerCase().equals(JPG) ||
             formats[i].trim().toLowerCase().equals(FITS) ||
             formats[i].trim().toLowerCase().equals(GIF)) {
            return true;
            }
      }
      return false;
   }

   /** For debug & reference */
   public String toString() {
      return "[ImageResults: target="+target+", "+super.toString()+"]";
   }
}
/*
 $Log: ReturnImage.java,v $
 Revision 1.1  2009/05/13 13:20:40  gtr
 *** empty log message ***

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.3  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.1.2.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.1.2.1  2004/11/17 11:15:46  mch
 Changes for serving images



 */



