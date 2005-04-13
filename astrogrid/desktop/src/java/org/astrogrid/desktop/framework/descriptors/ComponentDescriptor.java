/*$Id: ComponentDescriptor.java,v 1.2 2005/04/13 12:59:13 nw Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework.descriptors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Descriptor for a component
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class ComponentDescriptor extends Descriptor{

    /** Construct a new ComponentDescriptor
     * 
     */
    public ComponentDescriptor() {
        super();
    }
    protected final Map methods = new HashMap();
    public MethodDescriptor getMethod(String name) {
        return (MethodDescriptor)methods.get(name);
    }
    
    public void addMethod(MethodDescriptor m) {
        methods.put(m.getName(),m);
    }
    
    public Iterator methodIterator() {
        return methods.values().iterator();
    }
    
    protected Class interfaceClass;
    protected Class implementationClass;
    

    public Class getImplementationClass() {
        return this.implementationClass;
    }
   public void setImplementationClass(Class implementationClass) {
        this.implementationClass = implementationClass;
    }
    public Class getInterfaceClass() {
        return this.interfaceClass;
    }
    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
    
    public void setInterfaceClass(String name) throws ClassNotFoundException {
        this.interfaceClass = Class.forName(name);
    }
    
    public void setImplementationClass(String name) throws ClassNotFoundException {
        this.implementationClass = Class.forName(name);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ComponentDescriptor:");
        buffer.append(" methods: ");
        buffer.append(methods);
        buffer.append('\n');        
        buffer.append(" interfaceClass: ");
        buffer.append(interfaceClass);
        buffer.append('\n');        
        buffer.append(" implementationClass: ");
        buffer.append(implementationClass);
        buffer.append('\n');        
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ComponentDescriptor.java,v $
Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/