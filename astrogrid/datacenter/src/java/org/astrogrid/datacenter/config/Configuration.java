/*$Id: Configuration.java,v 1.2 2003/08/21 12:23:00 nw Exp $
 * Created on 20-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.config;

/** Inteface to a system configuration object
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 * @see ConfigurationDefaultImpl
 */
public interface Configuration {
    /**
    * Lookup a  property in the component's configuration.
    * 
    * @param key - the property key within category
    * @param category - the category within the configuration
    * @return the String value of the property, or the empty string if null
    * 
    * @see org.jconfig.jConfig */
    public abstract String getProperty(String key, String category);
}

/* 
$Log: Configuration.java,v $
Revision 1.2  2003/08/21 12:23:00  nw
split Configuration into an interface and default implemeentation
-- allows us to unit test without mucking about with on-disk config files.
 
*/