/*
 * $Id: SsapResults.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service.ssap;

import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.UrlListResults;

/**
 * Results which are a list of URLs
 *
 * @author M Hill
 */

public class SsapResults extends UrlListResults {

      /**
    * Construct this wrapper around the given list of results
    */
   public SsapResults(Querier parentQuerier, String[] results) {
      super(parentQuerier, results);
   }

}

/*
 $Log: SsapResults.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.1  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP


 */
