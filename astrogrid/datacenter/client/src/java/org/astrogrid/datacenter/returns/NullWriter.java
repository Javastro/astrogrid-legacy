/*
 * $Id: NullWriter.java,v 1.1 2004/09/08 12:52:57 mch Exp $
 */
package org.astrogrid.datacenter.returns;

/**
 * Throws away everything written to it - used for testing
 */

import java.io.Writer;

public class NullWriter extends Writer {
   
   /**
    * Does nothing
    */
   public void write(char[] cbuf, int off, int len)  {   }
   
   /**
    * Does nothing
    */
   public void flush()  {  }
   
   /**
    * Does nothing
    */
   public void close()  {  }
   
}
   
