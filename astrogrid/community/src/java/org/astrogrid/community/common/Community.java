package org.astrogrid.community.common;

import org.jconfig.handler.*;
import org.jconfig.*;
import java.io.*;
import javax.naming.Context;
import javax.naming.InitialContext;


public class Community { 
   
   private static final String DEFAULT_CATEGORY = "community";
   
   private static final String CONFIG_NAME = "community_config";
 
   public static Configuration loadConfigFile(String jndiLookup) {
      ConfigurationManager cfgmgr = ConfigurationManager.getInstance();
      Configuration cfg = null;
      try {
         Context initCtx = new InitialContext();
         Context envCtx = (Context) initCtx.lookup("java:comp/env");
         String config = (String)envCtx.lookup(jndiLookup);
         if(config != null && config.startsWith("http")) {
            URLHandler handler = new URLHandler();
            handler.setURL(config);
            cfgmgr.load(handler,CONFIG_NAME);
         }else {
            File file = new File(config);
            XMLFileHandler handler = new XMLFileHandler();
            handler.setFile(file);
            handler.load(file);
            cfgmgr.load(handler,CONFIG_NAME);
         }
         
         cfg = ConfigurationManager.getConfiguration(CONFIG_NAME);
      }catch(Exception e) {
         e.printStackTrace();
         cfg = null;   
      }
      return cfg;
   }
   

   public static String getProperty(String propName, String defaultVal) {
      
      return getProperty(propName,defaultVal,DEFAULT_CATEGORY);
   }   
   
   public static String getProperty(String propName, String defaultVal, String category) {
      Configuration cfg = ConfigurationManager.getConfiguration(CONFIG_NAME);
      return cfg.getProperty(propName,defaultVal,category);
   }

}