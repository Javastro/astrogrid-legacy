/*$Id: ActivityStatusStore.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 26-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**Maintains current state of each activity in the workflow.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *@todo find more efficient data structures 
 */
public class ActivityStatusStore {

    /** Construct a new ActivityStatusMap
     * 
     */
    public ActivityStatusStore() {
        super();
    }
    protected Map states = new HashMap();

    /** get state from map, if present, else create one */
    protected ActivityStatus getActivityStatus(String key) {
        ActivityStatus status = (ActivityStatus)states.get(key);
        if (status == null) {
            status = new ActivityStatus();
            status.setKey(key);
            states.put(status.getKey(),status);
        }
        return status;
    }
    /** return the a status code associated with the activity key */
    public Status getStatus(String key) {     
        return getActivityStatus(key).getStatus();
    }
    
    /** set the status code for an actrivity key 
     * */
    public void setStatus(String key,Status status) {
        getActivityStatus(key).setStatus(status);
    }
    
    /** get the environemnt associated aith an acitvity key - may be {} */
    public  Vars getEnv(String key) {
        
        return getActivityStatus(key).getEnv();        
    }
    
    public void setEnv(String key,Vars env) {
        getActivityStatus(key).setEnv(env);
    }

    /** merge environments into  new binding */
    public  Vars mergeVars(List keyList) {
        Vars result = new Vars();
        for (Iterator i = keyList.iterator(); i.hasNext(); ) {
            Vars b = this.getEnv((String)i.next());
            result.e.putAll(b.e);
        }
        return result;
    }

    /**
     * Returns <code>true</code> if this <code>ActivityStatusStore</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>ActivityStatusStore</code> is the same as the o argument.
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
        ActivityStatusStore castedObj = (ActivityStatusStore) o;
        return ( (this.states == null
            ? castedObj.states == null
            : this.states.equals(castedObj.states)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (states == null ? 0 : states.hashCode());
        return hashCode;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ActivityStatusStore:");
        buffer.append(" states: ");
        buffer.append(states);
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: ActivityStatusStore.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.3  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.2  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.
 
*/