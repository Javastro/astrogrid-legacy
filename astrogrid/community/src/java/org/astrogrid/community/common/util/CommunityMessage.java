package org.astrogrid.community.common.util;



public class CommunityMessage {
 
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
}