/*
 * $Id: DocMessageHelper.java,v 1.5 2003/09/11 11:03:52 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.astrogrid.log.Log;

/**
 * A helper class for constructing and relaying the documents that are common
 * to both the client and server.
 *
 * @author M Hill
 */

public class DocMessageHelper
{

   public static final String COMMUNITY_TAG = "Community";
   public static final String LISTENER_TAG = "NotifyMe";
   public static final String JOBLISTENER_TAG = "NotifyJob";
   /** NWW - needs to be lower case to fit with legacy xml query language */
   public static final String QUERY_TAG = "query";

}

