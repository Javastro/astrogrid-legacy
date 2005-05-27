/*
 * $Id: StreamSource.java,v 1.2 2005/05/27 16:21:02 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;



import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;

/**
 * Used to indicate the target where the results are to be sent when that target
 * is a given stream.
 *
 */

public class StreamSource implements SourceIdentifier  {

   protected InputStream in = null;

   
   public StreamSource(InputStream sourceIn) {
      this.in = sourceIn;
   }

   /** Returns an InputStreamReader around the resolved stream */
   public Reader openReader()  {
      return new InputStreamReader(openInputStream());
   }

   public InputStream openInputStream() {
      return in;
   }
   
   public String toString() {
       return in.getClass()+" SourceIndicator ";
   }
   
   /** Cannot be forwarded to remote services */
   public boolean isForwardable() { return false; }
   
   /** Unknown - so returns null */
   public String getMimeType() {
      return null;
   }
}
/*
 $Log: StreamSource.java,v $
 Revision 1.2  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.1.20.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.


 Revision 1.1.2.2  2004/12/03 11:50:19  mch

 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB



 Revision 1.1.2.1  2004/11/22 00:46:28  mch

 New Slinger Package





 */







