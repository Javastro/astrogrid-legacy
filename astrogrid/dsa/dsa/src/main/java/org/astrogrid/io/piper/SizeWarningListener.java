/**
 * $Id: SizeWarningListener.java,v 1.1.1.1 2009/05/13 13:20:36 gtr Exp $
 *
 */

package org.astrogrid.io.piper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.InputStream;

/**
 * Warns if the size of the piped data seems excessive
 */


public class SizeWarningListener implements PipeListener {
   
    /** Logger for this class   */
    private static final Log logger = LogFactory.getLog(SizeWarningListener.class);

   /** level above which warnings will be issued about transfer sizes*/
   private static final int EXCESSIVE_SIZE_WARNING_LEVEL=1024*1024*30;
   
   /** used to step up the warning levels */
   private int step = 1;
   
   String name = null;
   
   /** Called when the pipe starts */
   public void pipeStarted(InputStream givenIn, String aName, int streamSize) {
      name = aName;
   }
   
   
   /** Called when a block has been piped */
   public void blockPiped(long bytesRead) {
      if(bytesRead > step * EXCESSIVE_SIZE_WARNING_LEVEL)
      {
          logger.warn("piper has read "+ bytesRead+" bytes from "+name);
          step++;
      }
   }
   
   /** Does nothing */
   public void pipeComplete() {}
   
   /** Called when an Exception occurs */
   public void thrown(Throwable th) {
      // doe snothing
   }
}

