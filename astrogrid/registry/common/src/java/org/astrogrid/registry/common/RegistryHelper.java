package org.astrogrid.registry.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.registry.common.versionNS.IRegistryInfo;

import org.astrogrid.config.Config;
/** @todo javadoc */
public class RegistryHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryHelper.class);
   
    /**@todo make public? */
   private static final String REGISTRY_VERSION_PROPERTY = "org.astrogrid.registry.version";   

   public static Config conf = null;
   /** @todo don't think this should be holding on to a reference to the singleton */
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
   public static IRegistryInfo loadRegistryInfo() {
      IRegistryInfo iri = null;
      String className = "org.astrogrid.registry.common.versionNS.vr" + conf.getString(REGISTRY_VERSION_PROPERTY) + ".Registry";
      logger.debug("loadRegistryInfo() - attempting to load " + className );
      Class reg = null;
      try {
         reg = Class.forName(className);
      }catch(Exception e) {
        logger.info("loadRegistryInfo() - died at classforname");
        logger.error("loadRegistryInfo()", e);
      }
      
      try {
         if(reg != null) {//@todo don't test this - throw up earlier instead.
            iri = (IRegistryInfo)reg.newInstance();
         }else {             
            logger.info("loadRegistryInfo() - reg class was null");
            
         }
      }catch(Exception e) {
        logger.info("loadRegistryInfo() - died at newinstance");
        logger.error("loadRegistryInfo()", e);
      }
      return iri;
   }
}