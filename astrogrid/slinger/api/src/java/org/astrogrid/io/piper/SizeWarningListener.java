/**
 * $Id: SizeWarningListener.java,v 1.1 2005/03/22 16:17:33 mch Exp $
 *
 */

package org.astrogrid.io.piper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Warns if the size of the piped data seems excessive
 */


public class SizeWarningListener implements PiperProgressListener {
   
    /** Logger for this class   */
    private static final Log logger = LogFactory.getLog(SizeWarningListener.class);

   /** level above which warnings will be issued about transfer sizes*/
   private static final int EXCESSIVE_SIZE_WARNING_LEVEL=1024*1024*30;
   
   /** used to step up the warning levels */
   private int step = 1;
   
   /** Called when a block has been piped */
   public void blockPiped(long bytesRead) {
      if(bytesRead > step * EXCESSIVE_SIZE_WARNING_LEVEL)
      {
          logger.warn("piper has read "+ bytesRead+" bytes");
          step++;
      }
   }

   
}

