/*$Id: AbstractInformation.java,v 1.2 2005/08/12 08:45:16 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;
import java.net.URI;

/** Abstract Base class for all information beans returned by ACR.
 * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
 *
 */
public abstract class AbstractInformation implements Serializable {

    /** Construct a new AbstractInformation
     * 
     */
    protected AbstractInformation(URI id) {
        super();
        this.id = id;
    }
    
    protected final URI id;
    /** access the unique identifier for the resource this bean provides information on 
     * @return some form of URI - possibly an ivo:// form
     * @xmlrpc structure key will be <tt>id</tt>
     */
    public URI getId() {
        return this.id;
    }

    /**
     * tests two information beans for equality - determined by equality of {@link #getId()}
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
        AbstractInformation castedObj = (AbstractInformation) o;
        return ((this.id == null ? castedObj.id == null : this.id.equals(castedObj.id)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }

}


/* 
$Log: AbstractInformation.java,v $
Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/