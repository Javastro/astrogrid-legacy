/*
 * $Id: DocMessageHelper.java,v 1.6 2003/09/15 15:44:44 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

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
   public static final String JOBLISTENER_TAG = "NotifyJob";
   /** NWW - needs to be lower case to fit with legacy xml query language */
   public static final String QUERY_TAG = "query";

}

