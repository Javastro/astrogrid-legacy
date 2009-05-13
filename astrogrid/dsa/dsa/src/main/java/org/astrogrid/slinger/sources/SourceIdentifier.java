/*
 * $Id: SourceIdentifier.java,v 1.1.1.1 2009/05/13 13:20:41 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.Principal;

/**
 * Indicates anything that can be resolved into an inputstream, or an input reader
 * <p>
 * Essentially TargetIndicators provide a suitably typed and validated way of
 * passing around where things might come from, rather than having to pass around
 * Strings to indicate Identifiers, and separate methods to handle Readers/Streams.
 * <p>
 * For those implementations that need secure access, include the right stuff in
 * the constructor [it used to be in the methods]
 */

public interface SourceIdentifier  {


   /** All sources must be able to resolve to a reader.  */
   public Reader openReader() throws IOException;
   
   /** All sources must be able to resolve to a stream.   */
   public InputStream openInputStream() throws IOException;
   
   /** Used to get the mime type of the data about to be read.  Note that many
    * implementations (such as disk files?) do not have this
    * capability, and so often this will
    * do nothing. Return null if unknown */
   public String getMimeType() throws IOException;
   
}
/*
 $Log: SourceIdentifier.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:41  gtr


 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

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



