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
    * @param account
    * @param credential
    * @return
    */
   public static String getMessage(String token,String account,String credential)  {
      StringBuffer buff = new StringBuffer(100);
      buff.append("<community");
         buff.append("<token>");
            buff.append(token);
         buff.append("</token>");
         buff.append("<credentials>");
            buff.append("<account>");
               buff.append(account);
            buff.append("</account>");
            buff.append("<group>");
               buff.append(credential);
            buff.append("</group>");
        buff.append("</credentials>");
      buff.append("</community>");

      return buff.toString();
   }   
   
   public static String getToken(String xmlMessage) {
      return xmlMessage.substring(xmlMessage.indexOf("<token>") + "<token>".length(),xmlMessage.indexOf("</token>"));
   }

   public static String getAccount(String xmlMessage) {
      return xmlMessage.substring(xmlMessage.indexOf("<account>") + "<account>".length(),xmlMessage.indexOf("</accout>"));
   }

   public static String getGroup(String xmlMessage) {
      return xmlMessage.substring(xmlMessage.indexOf("<group>") + "<group>".length(),xmlMessage.indexOf("</group>"));
   }

   
}