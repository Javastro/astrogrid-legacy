/*
 * $Id: CommunityConstants.java,v 1.3 2003/09/17 19:47:21 dave Exp $
 * 
 * Created on 12-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.common;

/**
 * Constants used in the community workgroup
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public final class CommunityConstants {

   /**
    * Private constructor - this class is just a collection of static constants - should never be instatiated.
    */
   private CommunityConstants() {
      
   }

/**
 * The property name used to retrieve the castor mapping file.
 */
  public static final String MAPPING_CONFIG_KEY = "database.mapping" ;
  
  /**
   * The property key used to retrieve the castor database config.
   */
  public static final String DATABASE_CONFIG_KEY = "database.config" ;
  
  /**
   * The property key used to retrieve the castor database name.
   */
  public static final String DATABASE_NAME_KEY = "database.name" ;
 
}
