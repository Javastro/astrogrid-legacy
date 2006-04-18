/*$Id: ServiceBean.java,v 1.2 2006/04/18 23:25:43 nw Exp $
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
        this.id = id;
    }
    public Class getInterface() {
        return this.iface;
    }
    public void setInterface(Class iface) {
        this.iface = iface;
    }
    public Module getModule() {
        return this.module;
    }
    public void setModule(Module module) {
        this.module = module;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ServiceBean[");
        sb.append("id: ");
        sb.append(id);
        sb.append(" interface: ");
        sb.append(iface.getName());
        sb.append(" module: ");
        sb.append(module.getModuleId());
        sb.append("]");
        
        return sb.toString();
    }

}


/* 
$Log: ServiceBean.java,v $
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/