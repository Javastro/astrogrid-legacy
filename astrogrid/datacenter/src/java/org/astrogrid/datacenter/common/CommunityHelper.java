/*
 * $Id: CommunityHelper.java,v 1.1 2003/09/22 17:57:38 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.common;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.astrogrid.log.Log;

/**
 * Class to help extract community information from a dom
 *
 * @author M Hill
 */

public class CommunityHelper
{
   public final static String USERID_TAG = "UserId";
   public final static String COMMUNITYID_TAG = "CommunityId";
   public final static String COMMUNITY_TAG = "Community";

   public static String getUserId(Element dom)
   {
      return DocHelper.getTagValue(getCommunitySnippet(dom), USERID_TAG);
   }

   public static String getCommunityId(Element dom)
   {
      return DocHelper.getTagValue(getCommunitySnippet(dom), COMMUNITYID_TAG);
   }

   public static Element getCommunitySnippet(Element dom)
   {
      //allow for nulls in case this snippet is extracted from another snippet
      //that doesn't exist - so nested getSnippets can be used without having
      //to continually check for nulls
      if (dom == null)
      {
         return null;
      }

      NodeList communityNodes = dom.getElementsByTagName(COMMUNITY_TAG);

      Log.affirm(communityNodes.getLength() <= 1, "Too many community tags in dom");

      if (communityNodes.getLength() == 0)
      {
         return null;
      }

      return (Element) communityNodes.item(0);
   }
}

/*
$Log: CommunityHelper.java,v $
Revision 1.1  2003/09/22 17:57:38  mch
Methods to help with community xml snippets


*/

