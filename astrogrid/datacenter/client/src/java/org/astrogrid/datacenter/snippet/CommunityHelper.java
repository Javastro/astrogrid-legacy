/*
 * $Id: CommunityHelper.java,v 1.1 2003/11/17 12:53:07 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.snippet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.*;
/**
 * Class to help extract community information from a dom
 *
 * @author M Hill
 */

public class CommunityHelper
{
   private static Log logger = LogFactory.getLog(CommunityHelper.class);
   public final static String USERID_TAG = "UserId";
   public final static String COMMUNITYID_TAG = "CommunityId";
   public final static String COMMUNITY_TAG = "community";
   public final static String ACCOUNT = "account";
   public static String account = "";

   public static String getAccount(Element dom)
       {
           account = DocHelper.getTagValue(getCommunitySnippet(dom), ACCOUNT);
           return account;
       }
       
   public static String getUserId(Element dom)
     {
       account = DocHelper.getTagValue(getCommunitySnippet(dom), ACCOUNT);
       logger.debug("account LOG$Juserid:"+account);
       String userid ="";
       if (account != null) userid = account.substring(0,account.indexOf("@"));
       logger.debug("userid LOG$J:"+userid);
       return userid;
     }
 
   public static String getCommunityId(Element dom)
   {
      account = DocHelper.getTagValue(getCommunitySnippet(dom), ACCOUNT);
      logger.debug("account LOG$J:"+account);
      String communityid ="";
       if (account != null) communityid = account.substring(account.indexOf("@")+1,account.length());
      logger.debug("communityid LOG$J:"+communityid);
      return communityid;
   }
   /*
   public static String getUserId(Element dom)
   {
      return DocHelper.getTagValue(getCommunitySnippet(dom), USERID_TAG);
   }

   public static String getCommunityId(Element dom)
   {
      return DocHelper.getTagValue(getCommunitySnippet(dom), COMMUNITYID_TAG);
   }
 */
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

      assert communityNodes.getLength() <= 1 : "Too many community tags in dom";

      if (communityNodes.getLength() == 0)
      {
         return null;
      }

      return (Element) communityNodes.item(0);
   }
}

/*
$Log: CommunityHelper.java,v $
Revision 1.1  2003/11/17 12:53:07  mch
Moving common to snippet

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.4  2003/11/06 22:03:48  mch
Fix to test for null account in snippet and removed generic exception catch

Revision 1.3  2003/10/31 18:02:06  mch
Changed to use commons logging

Revision 1.2  2003/10/24 08:50:56  clq2
added to decouple userid and communityid from tag account.changed "Community" to lowercase "community" to match community snippet.

Revision 1.1  2003/09/22 17:57:38  mch
Methods to help with community xml snippets


*/

