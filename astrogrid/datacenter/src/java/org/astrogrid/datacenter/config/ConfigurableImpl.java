/* $Id: ConfigurableImpl.java,v 1.1 2003/08/20 14:42:59 nw Exp $
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
 * Standard implementation of the {@link Configurable} interface.
 * <p>
 * Any classes tha wish to be configurable may simply extend this class.
 * <p>
 * configuration is stored in a private field <code>conf</code>. all methods in this abstract class
 * are final, to prevent accidental overriding. If a class wishes to provide a different implementation of the <tt>Configurable</tt>
 * interface, writing out in full shouldn't be to inconvenient.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 *
 */
public abstract class  ConfigurableImpl implements Configurable {

	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Configurable#setConfiguration(org.astrogrid.datacenter.Configuration)
	 */
	public final void setConfiguration(Configuration conf) {
		this.conf = conf;
	}
	private Configuration conf;
	
	public final Configuration getConfiguration(){
		return conf;
	}
	

}
/*
 * $Log: ConfigurableImpl.java,v $
 * Revision 1.1  2003/08/20 14:42:59  nw
 * added a configuration package -
 * wraps the existing DTC class, and provides somewhere to
 * manage dynamically loaded factories.
 *
 */
