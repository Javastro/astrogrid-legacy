/*$Id: AbstractInformation.java,v 1.7 2008/08/21 11:34:35 nw Exp $
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

/** Base class - all 'information' structures returned by AR extend this class.
 * @xmlrpc returned as a struct, with keys corresponding to bean names
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Aug-2005
 *
 */
public abstract class AbstractInformation implements Serializable {

    /** Construct a new AbstractInformation
     * 
     */
    protected AbstractInformation(final String name,final URI id) {
        super();
        this.id = id;
        this.name =name;
    }
    
    static final long serialVersionUID = 6860597279255333361L;
    protected final URI id;
    protected final String name;
    /** The unique identifier for the resource this bean provides information about. 
     * @return some form of URI - for example an <tt>ivo://</tt> scheme URI
     * @xmlrpc structure key will be <tt>id</tt>
     */
    public final URI getId() {
        return this.id;
    }
    
    /**The name of this resource.
     * 
     * @return some kind of human-friendly name. - unlike the machine-friendly {@link #getId()}
     * @xmlrpc structure key will be <tt>name</tt>
     */
    public final String getName() {
        return this.name;
    }

    /**
     * tests two information beans for equality - determined by equality of {@link #getId()}
        */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        final AbstractInformation castedObj = (AbstractInformation) o;
        return ((this.id == null ? castedObj.id == null : this.id.equals(castedObj.id)));
    }
    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }

}


/* 
$Log: AbstractInformation.java,v $
Revision 1.7  2008/08/21 11:34:35  nw
doc tweaks

Revision 1.6  2007/01/24 14:04:45  nw
updated my email address

Revision 1.5  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.4.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.4  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.3  2005/08/16 13:14:42  nw
added 'name' as a common field for all information objects

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/