/*$Id: ComponentDescriptor.java,v 1.2 2005/09/02 14:03:34 nw Exp $
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

import org.apache.commons.collections.map.ListOrderedMap;

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
    protected final Map methods = new ListOrderedMap();
    /** retrieve the descriptor for a named method */
    public MethodDescriptor getMethod(String name) {
        return (MethodDescriptor)methods.get(name);
    }
    
    /** add a method to the descriptor */
    public void addMethod(MethodDescriptor m) {
        methods.put(m.getName(),m);
    }
    
    /** iterate over the method descriptors in this component 
     * 
     * @return iterator over {@link MethodDescriptor} objects
     */
    public Iterator methodIterator() {
        return methods.values().iterator();
    }
    
    protected Class interfaceClass;
    protected transient Class implementationClass;
    
    /** return the implementation class for this component */
    public Class getImplementationClass() {
        return this.implementationClass;
    }
   public void setImplementationClass(Class implementationClass) {
        this.implementationClass = implementationClass;
    }

   /** return the interface class for this component - which, must be implemented by the class returned by {@link #getImplementationClass()}*/   
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
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:07  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 22:48:22  nw
altered datastructure implementation, so order of insertion is preserved.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/