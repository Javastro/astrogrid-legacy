/*$Id: ServiceKindEntry.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 11-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A component to represent a service kind entry.
 *  @modified nww broke out of registry helper
 */
class ServiceKindEntry {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory.getLog(ServiceKindEntry.class);

    /**
     * The authority identifier.
     *  
     */
    private final String authority;

    /**
     * The resource identifier.
     *  
     */
    private final String resource;

    /**
     * Public constructor.
     *  
     */
    public ServiceKindEntry(String authority, String resource) {
       log.debug("ServiceKindEntry()");
        log.debug("  Auth  : " + authority);
        log.debug("  Ident : " + resource);
        this.authority = authority;
        this.resource = resource;
    }

    /**
     * Acces to the authority identifier.
     *  
     */
    public String getAuthority() {
        return this.authority;
    }

    /**
     * Acces to the resource identifier.
     *  
     */
    public String getResource() {
        return this.resource;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ServiceKindEntry:");
        buffer.append(" authority: ");
        buffer.append(authority);
        buffer.append(" resource: ");
        buffer.append(resource);
        buffer.append("]");
        return buffer.toString();
    }
}

/* 
$Log: ServiceKindEntry.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/02/11 14:28:28  nw
refactored, split out candidate classes.
 
*/