/*$Id: ServiceBean.java,v 1.3 2006/06/15 09:42:04 nw Exp $
 * Created on 15-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.hivemind;

import org.apache.hivemind.internal.Module;
/** description of one acr service - these beans are read from the hivemind descriptors */
public class ServiceBean {

    public ServiceBean() {
        super();
    }

    protected String id;
    protected Class iface;
    protected Module module;
    
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
    	if (id == null || id.trim().length() == 0) {
    		throw new IllegalArgumentException("Empty Id");
    	}
    	this.id = id;
    }
    public Class getInterface() {
        return this.iface;
    }
    public void setInterface(Class iface) {
    	if (! iface.isInterface()) {
    		throw new IllegalArgumentException("Expected an interface, got " + iface.getName());
    	}
        this.iface = iface;
    }
    public Module getModule() {
        return this.module;
    }
    public void setModule(Module module) {
    	if (module == null) {
    		throw new IllegalArgumentException("Null module");
    	}
        this.module = module;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ServiceBean[");
        sb.append("id: ");
        sb.append(id);
        sb.append(" interface: ");
        sb.append(iface != null ? iface.getName() : null);
        sb.append(" module: ");
        sb.append(module != null ? module.getModuleId() : null);
        sb.append("]");
        
        return sb.toString();
    }

}


/* 
$Log: ServiceBean.java,v $
Revision 1.3  2006/06/15 09:42:04  nw
improvements coming from unit testing

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/