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
package org.astrogrid.mySpace.mySpaceServer;

import org.astrogrid.Configurator;

/**
 * The <code>MSC</code> class represents 
 *
 * @author  CLQ
 * @version 1.0 24-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class MSC extends Configurator {
	
	private static final String JNDI_NAME="java:comp/env/mscConfigFileURL";

  public static final String 
	/** Three letter acronym for this subsystem within the overall AstroGrid system... 
	 *  "MSC" stands for MySpaceServerComponent  */  
		SUBSYSTEM_ACRONYM = "MSC" ; 
			
	public static final String 
	/** Configuration file for this component. */  
		CONFIG_FILENAME = "ASTROGRID_myspaceserverconfig.xml" ;
		
	public static final String
	/** Catalog name for mySpaceServer, required for jConfig. */
		CATLOG = "MYSPACESERVER";
		
	public static String 
	/** location on MySpaceServer where dataholders are stroed.*/
	    dataHolderFolder = "DATAHOLDERFOLDER",
	/** copy command when transfering large files.*/ 
	    copyCommand = "COPY_COMMAND",
	/** upper limit to NOT user copy command to transfer file.*/
	    sizeLimit = "SIZELIMIT";
	
	public static String
	/** response string if SUCCESS*/
		SUCCESS = "SUCCESS",
	/** response string if fail*/
		FAULT = "FAULT";

        
	private static MSC
		singletonMMC = new MSC() ;
        
        
	private MSC(){
		super() ;
	}
    
    
	public static MSC getInstance() {
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
        
	public String getConfigFileName() { return CONFIG_FILENAME ;	}
	protected String getSubsystemAcronym() { return SUBSYSTEM_ACRONYM ; }
	
  /** 
   * Get JNDI name of URL of configuration file
   * @see org.astrogrid.Configurator#getJNDIName()
   */
  protected String getJNDIName() {
    return JNDI_NAME;
  }

}
