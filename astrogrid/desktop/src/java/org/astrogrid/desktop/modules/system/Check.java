/*$Id: Check.java,v 1.1 2005/06/22 08:48:52 nw Exp $
 * Created on 21-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

/** Interface for test classes used in conditional classloading
 *  - check method returns true if associated implementation class is to be loaded.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2005
 *
 */
public interface Check {
    boolean check();
}

/* 
 $Log: Check.java,v $
 Revision 1.1  2005/06/22 08:48:52  nw
 latest changes - for 1.0.3-beta-1
 
 */