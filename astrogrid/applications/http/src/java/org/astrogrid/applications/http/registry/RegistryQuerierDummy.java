/* $Id: RegistryQuerierDummy.java,v 1.2 2004/07/30 14:54:47 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 * Created on Jul 29, 2004package org.astrogrid.applications.http.registry;
 */

package org.astrogrid.applications.http.registry;

import org.astrogrid.applications.beans.v1.WebHttpApplication;

/**
 * A dummy RegistryQuerier that doesn't use a registry - it simply returns some
 * in-built apps that come bundled in the war.
 * 
 * @author jdt
 */
public class RegistryQuerierDummy extends AbstractRegistryQuerier {

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[RegistryQuerierDummy:");
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * ctor Initialises with a few testapps.
     */
    public RegistryQuerierDummy() {
        WebHttpApplication helloWorld = new WebHttpApplication();
        helloWorld.setName("HelloWorldHttpApp");
        helloWorld
                .setURL("http://127.0.0.1:8080/astrogrid-cea-http/testapps/helloWorld.jsp"); //@TODO
        // fix
        // this

        applications.add(helloWorld);
    }
}