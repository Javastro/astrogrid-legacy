/*$Id: Vars.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 28-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import groovy.lang.Binding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** represents set of variable bindings in scope.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class Vars{

    /** Construct a new Bindings
     * 
     */
    public Vars() {
        super();
    }

    protected Map e = new HashMap();
    
    public Object get(String name) {
        return e.get(name);
    }
    /*
    public void set(String name,String value) {
        e.put(name,value);
    }*/
    
    public void set(String name,Object value) {
        e.put(name,value);
    }
    /** take a copy of the vars.
     * note -this is a shallow(ish) copy. values of bindings are not copied, but
     * are shared between the new and existing var objects.
     * <p>
     * for value objects, this means that they will diverge on the next assignment to the binding
     * <p>
     * for statefule objects, a change in state through one var will be visible in the other.
     * @return
     */
    public Vars cloneVars() {
        Vars copy = new Vars();
        copy.e = new HashMap(this.e);
        return copy;
    }
    


    /** Add the variables defined in this collection to a binding, plus the 'vars' object itself.
     * @param bodyBinding
     */
    public void addToBinding(Binding bodyBinding) {
        bodyBinding.setVariable("vars",this);
        for (Iterator i = e.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry var = (Map.Entry)i.next();
            bodyBinding.setVariable(var.getKey().toString(),var.getValue());
        }
    }

    /** Find the variables defined in this collection in the binding, copy back across again.
     *<p>
     *any new variables defined in the script must have been done directly - through accessing this object already. so just need to look for the values of existing vars.
     * @param bodyBinding
     */
    public void readFromBinding(Binding bodyBinding) {
        for (Iterator i = e.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry var = (Map.Entry)i.next();
            Object newValue = bodyBinding.getVariable(var.getKey().toString());
            if (newValue == null) {
                // dunno if its been explicitly set to null, or whether its a new binding added by the script. arse.
                // safest thing is to ignore this.
                //var.setValue(null);
            } else {
                var.setValue(newValue);
            }
        }
    }
    
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        return hashCode;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Vars:");
        buffer.append(" e: ");
        buffer.append(e);
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Returns <code>true</code> if this <code>Vars</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>Vars</code> is the same as the o argument.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Vars castedObj = (Vars) o;
        return ((this.e == null ? castedObj.e == null : this.e
            .equals(castedObj.e)));
    }
}


/* 
$Log: Vars.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation
 
*/