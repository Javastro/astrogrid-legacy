/*$Id: ComponentManager.java,v 1.1 2004/05/04 11:00:12 nw Exp $
 * Created on 04-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.component;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

import junit.framework.Test;

/**Interface to a container for components.
 * <p>
 * Based on an underlying <a href="http://www.picocontainer.org/">PicoContainer</a> implementation
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public interface ComponentManager extends Startable{
    
    /** output human-readable description of contents of container as HTML
     *  <p>
     * used for debugging / output to JSP 
     * Uses descriptions from components that implement the {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface*/
    public abstract String informationHTML();
    /** output human-readable description of contents of container */
    public abstract String information();
    /** return a suite of installation tests for the components in the container 
     * <p>
     * this suite is the composition of all installation tests returned by objects in the container that implement the
     * {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface
     * */
    public abstract Test getSuite();
    /** get the picocontainer that manages the components */
    public abstract MutablePicoContainer getContainer();
}


/* 
$Log: ComponentManager.java,v $
Revision 1.1  2004/05/04 11:00:12  nw
moved pico-container component stuff from jes into common, so it can be used in cea too
 
*/