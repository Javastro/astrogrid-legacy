/*
 * $Id: DocMessageHelper.java,v 1.1 2003/11/17 12:53:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.snippet;

/**
 * A helper class for constructing and relaying the documents that are common
 * to both the client and server.
 *
 * @author M Hill
 */

public class DocMessageHelper
{
   /** The tag that contains the community info */
   public static final String COMMUNITY_TAG = "Community";
   /** The tag used to register a web listener */
   public static final String LISTENER_TAG = "NotifyMe";
   /** The tag used to register a job web listener */
   public static final String JOBLISTENER_TAG = "JobMonitorURL";
   /** NWW - needs to be lower case to fit with legacy xml query language */
   public static final String QUERY_TAG = "query";
   /** when the external tool wants to assign the query id, it sends this tag */
   public static final String ASSIGN_QUERY_ID_TAG = "AssignID";
   /** Key specifying results target - prob myspace server */
   public static final String RESULTS_TARGET_TAG = "ResultsTarget";
}

