package org.astrogrid.registry.common;
import org.astrogrid.registry.common.versionNS.IRegistryInfo;

import org.astrogrid.config.Config;

public class RegistryHelper {
   
   private static final String REGISTRY_VERSION_PROPERTY = "org.astrogrid.registry.version";   

   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
   public static IRegistryInfo loadRegistryInfo() {
      IRegistryInfo iri = null;
      String className = "org.astrogrid.registry.common.versionNS.vr" + conf.getString(REGISTRY_VERSION_PROPERTY) + ".Registry";
      Class reg = null;
      try {
         reg = Class.forName(className);
      }catch(Exception e) {
         System.out.println("died at classforname");
         e.printStackTrace();
      }
      
      try {
         if(reg != null) {
            iri = (IRegistryInfo)reg.newInstance();
         }else {
            System.out.println("reg class was null");
         }
      }catch(Exception e) {
         System.out.println("died at newinstance");
         e.printStackTrace();
      }
      return iri;
   }
}