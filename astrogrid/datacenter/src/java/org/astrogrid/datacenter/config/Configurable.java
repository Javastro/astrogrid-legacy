/*
 * $Id: Configurable.java,v 1.1 2003/08/20 14:42:59 nw Exp $
 * Created on 19-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.datacenter.config;

/**
 * interface of classes that are configurable - i.e. they maintain a reference to a configuration object
 * <p>
 * interface contains a setter - intended to be used as part of object creation
 * and a getter - intended to be used within the configured classes method bodies, or called by its <i>close</i> friends.
 * @author Noel Winstanley
  */
public interface Configurable {
	/** set the configuration of this object 
     * 
	 * @param conf the configuration object to use
	 */
	public void setConfiguration(Configuration conf);
	
	/** retreive the configuration for this object
     * 
	 * @return the configuration object previously set by {@link #setConfiguration}
	 */
	public Configuration getConfiguration();

}
/*
$Log: Configurable.java,v $
Revision 1.1  2003/08/20 14:42:59  nw
added a configuration package -
wraps the existing DTC class, and provides somewhere to
manage dynamically loaded factories.

*/