/*
 * $Id: SsapResults.java,v 1.1 2004/11/11 23:23:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.ssap;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.UrlListResults;

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
 Revision 1.1  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP


 */
