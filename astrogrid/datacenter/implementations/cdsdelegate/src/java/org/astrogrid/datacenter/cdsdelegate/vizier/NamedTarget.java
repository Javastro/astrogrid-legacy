/*$Id: NamedTarget.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.vizier;

/** Target designated by object name
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Nov-2003
 *
 */
public class NamedTarget extends Target {

    /** Construct a new named target.
     * 
     */
    public NamedTarget(String name) {
        this.name = name;
    }
    protected String name;
    public String getName() {
        return name;
    }
    public String toString() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        NamedTarget other = (NamedTarget)obj;
        return this.name.equals(other.name);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return name.hashCode();
    }

}


/* 
$Log: NamedTarget.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/