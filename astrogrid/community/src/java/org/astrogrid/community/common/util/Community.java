package org.astrogrid.community.common.util;



public class Community {
 
   private static final String COMMUNITY_SPLIT = "@";

   public static String getLocalCommunityUrl() {
      //call the web.xml file for the local community url.
      return " ";
   }
   
   public static String getCommunityUrl(String name) {
    
      return " ";  
   }
   
   public static String getCommunityName() {
      return getCommunityName(getLocalCommunityUrl());
      
   }
   
   public static String getCommunityName(String url) {
      return " ";
   }
   
   public static String getCommunityNameFromString(String val) {
      String []temp = val.split(COMMUNITY_SPLIT);
      if(temp.length > 0) {
         return temp[1];
      }
      return null;
   }
}