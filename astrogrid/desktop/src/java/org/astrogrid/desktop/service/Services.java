/*$Id: Services.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.astrogrid.desktop.ui.BrowserControl;
import org.astrogrid.desktop.ui.Desktop;
import org.astrogrid.desktop.ui.WebstartBrowserControl;

import org.apache.commons.attributes.AttributeUtil;
import org.apache.commons.attributes.Attributes;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** Container for the services this desktop server provides.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class Services extends DefaultPicoContainer {

    /** Construct a new Services
     * 
     */
    public Services() {
        super();
        registerPrimitives();
        registerServices();
    }
    /** test for presence of a service */
    public boolean hasService(String name) {
        try {
            getComponentAdapter(name);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    /** list names of services */
    public Collection getServiceNames() {
        Collection result = new ArrayList() ;
        for (Iterator servs = getServices().iterator(); servs.hasNext(); ) {
            ServiceDoc meta = (ServiceDoc) Attributes.getAttribute(servs.next().getClass(),ServiceDoc.class);
            result.add(meta.getName());
        }       
    return result;
    }
    
    /** get a named service */
    public Object getService(String name) {
        return getComponentInstance(name);
    }
    
    /** get documentation associated with a service name */
    public ServiceDoc getServiceDoc(String name) {
        return (ServiceDoc) Attributes.getAttribute(getComponentInstance(name).getClass(),ServiceDoc.class);
    }
    
    /** list all services present */
    public Collection getServices() {
        return AttributeUtil.getObjectsWithAttributeType(getComponentInstances(),ServiceDoc.class);
    }
    
    /** list all service documents */
    public Collection getServiceDocs() {
        Collection result = new ArrayList() ;
        for (Iterator servs = getServices().iterator(); servs.hasNext(); ) {
            result.add(Attributes.getAttribute(servs.next().getClass(),ServiceDoc.class));
        }       
    return result;
    }
    
    /** populate container with basic components */
    protected final void registerPrimitives() {
        registerComponentImplementation(BrowserControl.class,WebstartBrowserControl.class);
        registerComponentImplementation(Desktop.class);
    }
    
    /** populate container with services */
    protected final void registerServices() {
        // add new services to this array.
        Class[] services = new Class[]{Applications.class, Community.class,Configuration.class,Jobs.class};
        
        for (int i = 0; i < services.length; i++) {
            ServiceDoc doc = MetadataHelper.getServiceDocForClass(services[i]);
            if (doc == null) {
                throw new IllegalStateException("Cannot register an unannotated component");
            }
            registerComponentImplementation(doc.getName(),services[i]);
        }
    }
    
}


/* 
$Log: Services.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/