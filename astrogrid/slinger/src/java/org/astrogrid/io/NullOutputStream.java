/*
 * $Id: NullOutputStream.java,v 1.1 2005/02/14 17:53:38 mch Exp $
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
 Revision 1.1  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.2  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/27 12:47:04  mch
 separated out null outputsream


 */



