/*
 * $Id: TargetIdentifier.java,v 1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;

/**
 * Indicates the target where the results are to be sent.  TargetIndicators
 * represent anything that can be resolved into a stream or writer.  Thus they
 * might be URLsMay be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved.
 * <p>
 * Essentially TargetIndicators provide a suitably typed and validated way of
 * passing around where results are going to go, rather than having to pass around
 * Strings to indicate Identifiers, and separate methods to handle Writers/Streams.
 *
 */

public interface TargetIdentifier  {


   /** All targets must be able to resolve to a writer. The user is required
    * for permissions */
   public Writer openWriter() throws IOException;
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream() throws IOException;
   
   /** Used to set the mime type of the data about to be sent to the target. *Must*
    * be set before any data is set, as some targets cannot cope with it afterwards.
    * Note that many target implementations (such as disk files?) do not have this
    * capability, and so often this will
    * do nothing. */
   public void setMimeType(String mimeType) throws IOException;
}
/*
 $Log: TargetIdentifier.java,v $
 Revision 1.1  2009/05/13 13:20:42  gtr
 *** empty log message ***

 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:47  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.2  2005/05/27 16:21:01  clq2
 mchv_1

 Revision 1.1.20.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2005/01/26 14:35:28  mch
 Separating slinger and scapi

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.6.2  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.3.6.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets


 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */



