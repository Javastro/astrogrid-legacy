package org.astrogrid.community.common.util;


/**
 * Small util class for handling a small snippet of xml to be sent between the systems.
 * @author Kevin Benson
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CommunityMessage {
 
   /**
    * returns a snippet of xml.
    * @param token
    * @param account in vo reference form - ie individual@community
    * @param credential
    * @return
    */
   public static String getMessage(String token,String account,String group)  {
      
      assert account.indexOf('@')>0 : "Account '"+account+"' not a valid vo reference - should be of the form individual@community";
      
      StringBuffer buff = new StringBuffer(100);
      buff.append("<community>");
         buff.append("<token>");
            buff.append(token);
         buff.append("</token>");
         buff.append("<credentials>");
            buff.append("<account>");
               buff.append(account);
            buff.append("</account>");
            buff.append("<group>");
               buff.append(group);
            buff.append("</group>");
        buff.append("</credentials>");
      buff.append("</community>");

      return buff.toString();
   }
   
   public static String getToken(String xmlMessage) {
      int index = -1;
      index = xmlMessage.indexOf("<token>");
      if(index != -1) {
         return xmlMessage.substring(index + "<token>".length(),xmlMessage.indexOf("</token>"));
      }
      return null;
   }

   public static String getAccount(String xmlMessage) {
      int index = -1;
      index = xmlMessage.indexOf("<account>");
      if(index != -1) {
         return xmlMessage.substring(index + "<account>".length(),xmlMessage.indexOf("</account>"));
      }
      return null;
   }

   public static String getIndividual(String xmlMessage) {
      String voRef = getAccount(xmlMessage);
      int at = voRef.indexOf('@');
      assert at != -1 : "Account not valid VO Reference - no '@' in '"+voRef+"'";
      assert at > 0 : "No individual account given in '"+voRef+"'";
      return voRef.substring(0,at);
   }

   public static String getCommunity(String xmlMessage) {
      String voRef = getAccount(xmlMessage);
      int at = voRef.indexOf('@');
      assert at != -1 : "Account not valid VO Reference - no '@' in '"+voRef+"'";
      assert at > 0 : "No individual account given in '"+voRef+"'";
      return voRef.substring(at+1);
   }
   
   
   public static String getGroup(String xmlMessage) {
      int index = -1;
      index = xmlMessage.indexOf("<group>");
      if(index != -1) {
         return xmlMessage.substring(index + "<group>".length(),xmlMessage.indexOf("</group>"));
      }
      return null;
   }
}
