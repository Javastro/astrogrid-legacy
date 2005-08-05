/*$Id: AcrPicoContainer.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.opt;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.picocontainer.PicoContainer;
import org.picocontainer.alternatives.ImplementationHidingComponentAdapter;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.Iterator;

/** A pico container that takes as 'parent' an ACR registry, and allows classes to be registered that depend on acr services.
 * 
 * the container will instantiate the classes and pass in the required services - this works both for a local acr and a remote acr via java-rmi (in which case all the 
 * services will be stubs to the remote implementatiioins).
 * 
 * this class is useful for development of code which can either run external to an acr, or maybe bundled up as a plugin to the acr - as this AcrPicoContainer provides the same execution environment
 * for code running outside the acr as is present for plugins running within the acr.
 * 
 * requires the picocontainer library (1.1) to operate.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class AcrPicoContainer extends DefaultPicoContainer {

    /** Construct a new AcrPicoContainer
     * @param arg0
     * @param arg1
     */
    public AcrPicoContainer(ACR acr,ComponentAdapterFactory arg0, PicoContainer arg1) {
        super(arg0, arg1);
        registerServices(acr);
    }

    /** Construct a new AcrPicoContainer
     * @param arg0
     */
    public AcrPicoContainer(ACR acr,PicoContainer arg0) {
        super(arg0);
        registerServices(acr);        
    }

    /** Construct a new AcrPicoContainer
     * @param arg0
     */
    public AcrPicoContainer(ACR acr,ComponentAdapterFactory arg0) {
        super(arg0);
        registerServices(acr);        
    }

    /** Construct a new AcrPicoContainer
     * 
     */
    public AcrPicoContainer(ACR acr) {
        super();
        registerServices(acr);        
    }

    
    /** pre-register the acr services in the container
     * register each behind an implementation-hiding CA - so that the rmi-lite client isn't created until needed.
     * @param acr
     */
    private void registerServices(ACR acr) {
        for (Iterator i = acr.moduleIterator(); i.hasNext(); ) {
            ModuleDescriptor md = ((DefaultModule)i.next()).getDescriptor();
            for (Iterator j = md.componentIterator(); j.hasNext(); ) {
                ComponentDescriptor cd = (ComponentDescriptor)j.next();
                this.registerComponent(
                        new ImplementationHidingComponentAdapter(
                                new CachingComponentAdapter(
                                        new AcrComponentAdapter(acr,cd))
                                ,false)
                                );
            }
        }
    } 
    
   
}


/* 
$Log: AcrPicoContainer.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/