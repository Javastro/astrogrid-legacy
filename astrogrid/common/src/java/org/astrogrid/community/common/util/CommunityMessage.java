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
      buff.append("<community>");
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

   public static String getGroup(String xmlMessage) {
      int index = -1;
      index = xmlMessage.indexOf("<group>");
      if(index != -1) {
         return xmlMessage.substring(index + "<group>".length(),xmlMessage.indexOf("</group>"));
      }
      return null;
   }   
}