/*$Id: Vars.java,v 1.5 2004/11/05 16:52:42 jdt Exp $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** represents set of variable bindings in scope.
 * allows additional scopes to be added and removed.
 * scopes introduce new bindings.
 * asignements to existing bindings in parent scopes update, rather than shadow.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class Vars{

    /** Construct a new Bindings
     * 
     */
    public Vars() {
        super();
        l = new ArrayList();
        l.add(new HashMap());
    }

    protected List l;

    
    
    public Object get(String name) {
        Map e = findMapWithKey(name);
        if (e == null) {
            return null;
        }
        return e.get(name);
    }

    public void set(String name,Object value) {
        Map e = findMapWithKey(name);
        if (e == null) {
            e = getInnermost();
        }
        e.put(name,value);
    }
    
    private Map getInnermost() {
        return (Map)l.get(l.size()  -1);
    }
    private Map findMapWithKey(String name) {
        for (int i = l.size() - 1; i >= 0; i--) {
            Map m = (Map)l.get(i);
            if (m.containsKey(name)) {
                return m;
            }
        }
        return null;
    }
    
    
    public void unset(String name) {
        Map e = findMapWithKey(name);
        if (e !=null) {
            e.remove(name);
        }
    }
    /** Branch these vars
     * this is a very shallow copy. existing scopes are shared between the original and copy.
     * however, new scopes in the copy do not appear in the original.
     */
    public Vars branchVars() {
        Vars copy = new Vars();
        copy.l.clear();
        copy.l.addAll(this.l);
        return copy;
    }
    
public void newScope() {
    l.add(new HashMap());
}

public void removeScope()     {
    l.remove(l.size() - 1);
}
    

    /** Add the variables defined in this collection to a binding, plus the 'vars' object itself.
     * @param bodyBinding
     */
    public void addToBinding(Binding bodyBinding) {
        bodyBinding.setVariable("vars",this);
        for (Iterator j = l.iterator(); j.hasNext(); ) {
            Map e = (Map)j.next();
            for (Iterator i = e.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry var = (Map.Entry)i.next();
                bodyBinding.setVariable(var.getKey().toString(),var.getValue());
            }
        }
    }

    /** Find the variables defined in this collection in the binding, copy back across again.
     *<p>
     *any new variables defined in the script must have been done directly - through accessing this object already. so just need to look for the values of existing vars.
     * @param bodyBinding
     */
    public void readFromBinding(Binding bodyBinding) {
        for (int j = 0; j < l.size(); j++){
            Map e = (Map)l.get(j);           
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
    }
    

    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (l == null ? 0 : l.hashCode());
        return hashCode;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[Vars:");
        buffer.append(" l: ");
        buffer.append(l);
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
        return ((this.l == null ? castedObj.l == null : this.l
            .equals(castedObj.l)));
    }
}


/* 
$Log: Vars.java,v $
Revision 1.5  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.4.58.1  2004/11/05 16:03:57  nw
optimized: uses int rather than iterator

Revision 1.4  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore

Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

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