/*
 * $Id: NullOutputStream.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets.out;

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
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/27 12:47:04  mch
 separated out null outputsream


 */



