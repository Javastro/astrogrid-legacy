/*
 * @(#)MMC.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.mySpace.mySpaceManager;

import org.astrogrid.Configurator;

/**
 * The <code>MMC</code> class represents 
 *
 * @author  CLQ
 * @version 1.0 17-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class MMC extends Configurator {
	
  /**
   * JNDI name of URL of config file
   */
  private static final String JNDI_NAME ="java:comp/env/org.astrogrid.mySpace.config.url";

  public static final String 
	/** Three letter acronym for this subsystem within the overall AstroGrid system... 
	 *  "MMC" stands for MySpaceManagerComponent  */  
		SUBSYSTEM_ACRONYM = "MMC" ; 
			
	public static final String 
	/** Configuration file for this component. */  
		CONFIG_FILENAME = "ASTROGRID_myspacemanagerconfig.xml" ;
		
    public static final String
	/** Catalog name for mySpaceServer, required for jConfig.*/
		CATLOG = "MYSPACEMANAGER";
		
		
	public static String 
	/** Location of MySpace Registry files */  
		REGISTRYCONF = "REGISTRYCONF" ,	
	/** URL Location of MySpaceManager. */
	    mySpaceManagerLoc = "MYSPACEMANAGERURL",
	/** URL Location of MySpaceServer. */
		serverManagerLoc = "MYSPACESERVERURL",
	/** URLs all MySpaceManager within the astrogrid system*/
	    MYSPACEMANAGERURLs = "MYSPACEMANAGERURLs",
            SERVERDEPLOYMENT = "SERVERDEPLOYMENT",
            DEBUG = "DEBUG",
            CHECKPERMISSIONS = "CHECKPERMISSIONS";

    public static String
	/** response template*/
	    RESPONSE = "TEMPLATE.RESPONSE",
	/** response header template*/
		D_RESPONSE_HEAD ="TEMPLATE.RESPONSEHEAD",
    /** response element template*/
		D_RESPONSE_ELEMENT ="TEMPLATE.RESPONSEELEMENT",
    /** response footer template*/
		D_RESPONSE_FOOT ="TEMPLATE.RESPONSEFOOT";
		
    public static String
    /** response string if SUCCESS*/
	    SUCCESS = "SUCCESS",
	/** response string if fail*/
	    FAULT = "FAULT";

        
	private static MMC
		singletonMMC = new MMC() ;
        
        
	private MMC(){
		super() ;
	}
    
    
	public static MMC getInstance() {
		return singletonMMC ;
	}
        
       
	/**
	  *  
	  * Static getter for properties from the component's configuration.
	  * <p>
	  * 
	  * @param key - the property key within category
	  * @param category - the category within the configuration
	  * @return the String value of the property, or the empty string if null
	  * 
	  * @see org.jconfig.jConfig
	  **/       
	public static String getProperty( String key, String category ) {
		return Configurator.getProperty( SUBSYSTEM_ACRONYM, key, category ) ;
	}
  
  /**
   * Set a property
   * @TODO should really tidy up all the static vs nonstatic methods
    * @param key - the property key within category
    * @param category - the category within the configuration
    * @param value - the value to be set
   */
  public static void setProperty( String key, String category, String value ) {
    Configurator.setProperty( SUBSYSTEM_ACRONYM, key, category, value ) ;
  }
        
	public String getConfigFileName() { return CONFIG_FILENAME ;	}
	protected String getSubsystemAcronym() { return SUBSYSTEM_ACRONYM ; }
	
  /**
   * Name to look for under JNDI for location of config file
   * @see org.astrogrid.Configurator#getJNDIName()
   * @return the jndi name
   */
  public String getJNDIName() {
    return JNDI_NAME;
  }

}
