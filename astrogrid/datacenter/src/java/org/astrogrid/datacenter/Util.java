/*
 * $Id: Util.java,v 1.4 2003/09/03 13:42:04 nw Exp $
 * Created on 19-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.datacenter;


/**
 * Some static helper methods
 * <p>
 * Don't add things to this class willy-nilly -- put them where they belong instead.
 *@todo  remove this class all together, once we've trashed the old classes that use it.
 * @author Noel nw@jb.man.ac.uk
  */
public class Util {

	/** 
     * Copied from Configurator class in astrogrid library. Placing it here prevents all class from having to import Configurator, which I
     * want to hide access to - static methods make isolation difficult
     * FUTURE rip this out.
	 * @param class1 object
	 * @return returns unqualified class name (i.e. all package names stripped off)
	 */
	public static String getComponentName(Class class1) {
        String className = class1.getName();
        char[] chars = className.toCharArray();
        int lastDot = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '.') {
                lastDot = i + 1;
            } else if (chars[i] == '$') {  // handle inner classes
                chars[i] = '.';
            }
        }
        return new String(chars, lastDot, chars.length - lastDot);
	}

}
/*
 * $Log: Util.java,v $
 * Revision 1.4  2003/09/03 13:42:04  nw
 * removed dependency on jconfig library,
 * updated project.xml to reflect this.
 *
 * Revision 1.3  2003/08/28 15:30:32  nw
 * minor fixes to javadoc
 *
 * Revision 1.2  2003/08/22 11:48:34  nw
 * improved docs
 *
 * Revision 1.1  2003/08/20 14:50:46  nw
 * class for holding static methods we don't want to get rid of yet.
 *
 */
