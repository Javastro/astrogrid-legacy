/*
 * $Id: DocMessageHelper.java,v 1.3 2003/09/10 17:57:31 mch Exp $
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

}

