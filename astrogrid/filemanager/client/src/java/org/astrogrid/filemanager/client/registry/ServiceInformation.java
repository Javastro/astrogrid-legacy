/*$Id: ServiceInformation.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
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

import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * A component to contain information about a service.
 *  @modified nww split out of registry helper.
 * @modified nww renamed accessors and mutators to fit bean naming conventions.
 */
public class ServiceInformation {
    /**
     * Commons Logger for this class
     */
    private static final Log log = LogFactory
            .getLog(ServiceInformation.class);

    /**
     * Our internal HashMap of entries.
     *  @todo should this really be public?
     */
    protected Map map = new HashMap();

    /**
     * Public constructor.
     *  
     */
    public ServiceInformation(Ivorn ivorn) {
        log.debug("ServiceInformation(Ivorn)");
        log.debug("  Ivorn : " + ivorn);
        this.ivorn = ivorn;
    }

    /**
     * Our service Ivorn.
     *  
     */
    private final Ivorn ivorn;

    /**
     * Access to the service Ivorn.
     *  
     */
    public Ivorn getIvorn() {
        return this.ivorn;
    }

    /**
     * The service endpoint.
     *  
     */
    private String endpoint;

    /**
     * Set the service endpoint.
     *  
     */
    protected void setEndpoint(String endpoint) {
        log.debug("ServiceInformation.endpoint(String)");
        log.debug("  Endpoint : " + endpoint);
        this.endpoint = endpoint;
    }

    /**
     * Access to the service endpoint.
     *  
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * Add an entry to our service type list.
     *  
     */
    protected void addProvides(ServiceKindEntry entry) {
        log.debug("ServiceInformation.provides(ServiceKindEntry)");
        log.debug("  Auth  : " + entry.getAuthority());
        log.debug("  Ident : " + entry.getResource());
        map.put(key(entry.getAuthority(), entry.getResource()), entry);
    }

    /**
     * Check if the service implements a specific service type.
     *  
     */
    public boolean provides(String authority, String resource) {
        return map.containsKey(key(authority, resource));
    }

    /**
     * Create a map key for an entry.
     *  
     */
    protected String key(String authority, String resource) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(authority);
        buffer.append("::");
        buffer.append(resource);
        return buffer.toString();
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ServiceInformation:");
        buffer.append(" map: ");
        buffer.append(map);
        buffer.append(" ivorn: ");
        buffer.append(ivorn);
        buffer.append(" endpoint: ");
        buffer.append(endpoint);
        buffer.append("]");
        return buffer.toString();
    }
}

/* 
$Log: ServiceInformation.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.2  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

Revision 1.1.2.1  2005/02/11 14:28:28  nw
refactored, split out candidate classes.
 
*/