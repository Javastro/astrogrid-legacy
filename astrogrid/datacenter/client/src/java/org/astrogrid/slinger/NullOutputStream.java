/*
 * $Id: NullOutputStream.java,v 1.2 2004/10/18 13:30:03 mch Exp $
 */
package org.astrogrid.slinger;

/**
 * Throws away everything written to it - used for testing
 */


import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
   
   /**
    * throws away the given byte
    */
   public void write(int b) {
   }
   
}
   
