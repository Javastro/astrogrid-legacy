/*
 * $Id: Util.java,v 1.2 2003/08/22 11:48:34 nw Exp $
 * Created on 19-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.datacenter;
import org.astrogrid.Configurator;

/**
 * Some static helper methods
 * <p>
 * Don't add things to this class willy-nilly -- put them where they belong instead.
 * FUTURE - may well remove this all together.
 * @author Noel nw@jb.man.ac.uk
  */
public class Util {

	/** 
     * Copied from Configurator class in astrogrid library. Placing it here prevents all class from having to import Configurator, which I
     * want to hide access to - static methods make isolation difficult
     * FUTURE rip this out.
	 * @param class object
	 * @return returns unqualified class name (i.e. all package names stripped off)
	 */
	public static String getComponentName(Class class1) {
		return Configurator.getClassName(class1);
	}

}
/*
 * $Log: Util.java,v $
 * Revision 1.2  2003/08/22 11:48:34  nw
 * improved docs
 *
 * Revision 1.1  2003/08/20 14:50:46  nw
 * class for holding static methods we don't want to get rid of yet.
 *
 */
