/*
 * $Id: DefaultPlugin.java,v 1.3 2004/11/03 00:17:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default plugin has an aborted flag and a logger
 */

public abstract class DefaultPlugin implements QuerierPlugin {

   protected static final Log log = LogFactory.getLog(QuerierPlugin.class);
   
   protected boolean aborted = false;
   
   /** Abort - if this is called, try and top the query and tidy up.   */
   public void abort() {
      aborted = true;
   }
   
}
/*
 $Log: DefaultPlugin.java,v $
 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */




