/*$Id: ActivityStatus.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class ActivityStatus {

    /** Construct a new ActivityStatus
     * 
     */
    public ActivityStatus() {
        super();
    }

    protected String key;
    protected Status status = Status.UNSTARTED;
    protected Vars env = new Vars();

    /**
     * @return Returns the env.
     */
    public Vars getEnv() {        
        return this.env;
    }
    
    public void setEnv(Vars env) {
       this.env = env;
    }
  
    
    /**
     * @return Returns the key.
     */
    public String getKey() {
        return this.key;
    }
    /**
     * @param key The key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @return Returns the status.
     */
    public Status getStatus() {
        return this.status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ActivityStatus:");
        buffer.append(" key: ");
        buffer.append(key);
        buffer.append(" status: ");
        buffer.append(status);
        buffer.append(" env: ");
        buffer.append(env);
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Returns <code>true</code> if this <code>ActivityStatus</code> is the same as the o argument.
     *
     * @return <code>true</code> if this <code>ActivityStatus</code> is the same as the o argument.
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
        ActivityStatus castedObj = (ActivityStatus) o;
        return ((this.key == null ? castedObj.key == null : this.key
            .equals(castedObj.key))
            && (this.status == null ? castedObj.status == null : this.status
                .equals(castedObj.status)) && (this.env == null
            ? castedObj.env == null
            : this.env.equals(castedObj.env)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (key == null ? 0 : key.hashCode());
        hashCode = 31 * hashCode + (status == null ? 0 : status.hashCode());
        hashCode = 31 * hashCode + (env == null ? 0 : env.hashCode());
        return hashCode;
    }
}


/* 
$Log: ActivityStatus.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.3  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

Revision 1.1.2.2  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.
 
*/