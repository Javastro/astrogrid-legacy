/*
 * $Id: NullOutputStream.java,v 1.1.1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.OutputStream;

/**
 * Sink - anything written out is lost forever...
 *
 */

public class NullOutputStream extends OutputStream {

   /** throws away the given byte */
   public void write(int b) { }
}
/*
 $Log: NullOutputStream.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:35  gtr


 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.1  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.2  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/27 12:47:04  mch
 separated out null outputsream


 */



