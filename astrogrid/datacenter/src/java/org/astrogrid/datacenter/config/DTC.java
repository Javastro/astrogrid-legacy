/*
 * @(#)DTC.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.config;

import org.astrogrid.Configurator;


/**
 * The <code>DTC</code> class extends the {@link org.astrogrid.Configurator} to load
 * the datacenter configuration file ASTROGRID_datacenterconfig.xml
 *<p>
 *<b>NW</b> - refactored this class - removed constants to the {@link ConfigurationKeys} interface, and then closed this class down - now package private
 *and on its way to being removed all together. Never much more than a delegate around {@link org.astrogrid.Configurator} and shares the same
 *static-nature. Access to the configuration system is now though the {@link Configuration} and {@link Configurable} classes.
 * @author  Jeff Lusted
 * @author Noel Winstanley nw@jb.man.ac.uk
 * @version 2.0
 * @see
 * @see
 * @since   AstroGrid 1.2
 */
class DTC// extends org.astrogrid.Configurator
{

    private static DTC
        singletonDTC = new DTC() ;


    private DTC(){
        super();
    }


    public static DTC getInstance() {
        return singletonDTC ;
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
      **
    public static String getProperty( String key, String category ) {
        return Configurator.getProperty( ConfigurationKeys.SUBSYSTEM_ACRONYM, key, category ) ;
    }
     */

    /** Temporary fix for problems with configurator - use SimpleConfig */
    public static String getProperty( String key, String category ) {
        return SimpleConfig.getProperty( key, category ) ;
    }
    /** temporary dummy one - yes properties are loaded */
   public void checkPropertiesLoaded() {}


    protected String getConfigFileName() { return ConfigurationKeys.CONFIG_FILENAME ; }
    protected String getSubsystemAcronym() { return ConfigurationKeys.SUBSYSTEM_ACRONYM ; }

} // end of class DTC
