/* $Id: Configuration.java,v 1.1 2003/08/20 14:42:59 nw Exp $
 * Created on 19-Aug-2003
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.datacenter.config;
import org.astrogrid.AstroGridException;
/**
 * System configuration object.
 * <p>
 * A wrapper around a very ugly static configuration system. {@link DTC} which in turn builds upon the 
 * static {@link org.astrogrid.common.Configurator} class. Statics are generall a bad idea - they make a system less modular
 * by introducing hidden dependancies; and break locality of data -  any class may access the contents of a static class.
 * <p>
 *  The aim of this class is to hide it all - make sure only accessible though this configuration object.  
 * The mess we're hiding can then be cleaned up later.
 
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 *
 */
public class Configuration {

	public Configuration() {
		wrapped = DTC.getInstance();

	}
	
	private final DTC wrapped;

	/**
	 * Verify the configuration properties have loaded correctly
     * (and load these properties in the process)
     * <p>FUTURE - rename to something more descriptive (e.g. load)
     * @throws AstroGridException if the configuration cannot be loaded
	 */
	public void checkPropertiesLoaded() throws AstroGridException{
		wrapped.checkPropertiesLoaded();
		
	}

    /**
    * Lookup a  property in the component's configuration.
    * 
    * @param key - the property key within category
    * @param category - the category within the configuration
    * @return the String value of the property, or the empty string if null
    * 
    * @see org.jconfig.jConfig */
	public String getProperty(String key, String category) {
		return wrapped.getProperty(key,category);
	}
	
    /** generate a string representation of this object
     * @return details of the filename this configuration was loaded from.
     */
	public String toString() {
		return wrapped.getConfigFileName();
	
	}
}
/*
 * Log
 */
