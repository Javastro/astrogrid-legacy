/*
 * $Id: Config.java,v 1.2 2003/12/09 23:01:15 pah Exp $
 *
 * Created on 13 September 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.common.config;

import javax.sql.DataSource;

/**
 * Defines the fundamental operations that a configuration can perform.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */
public interface Config {
   
   /**
    * Looks up a property indexed by key.
    * @param key a key that is used to look up the property.
    * @return The value of the property found.
    */
   public String getProperty(String key); 
   
   /**
    * Returns a datasource.
    * @param key
    * @return
    */
   public DataSource getDataSource(String key);
}
