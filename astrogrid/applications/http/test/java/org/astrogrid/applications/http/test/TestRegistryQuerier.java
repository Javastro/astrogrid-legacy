/* $Id: TestRegistryQuerier.java,v 1.2 2004/07/30 14:54:47 jdt Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.applications.http.test;

import org.astrogrid.applications.beans.v1.WebHttpApplication;
import org.astrogrid.applications.http.registry.AbstractRegistryQuerier;

/**
 * Returns pretend meta data about the GetEchoerHTTPD test service
 * 
 * @author jdt
 */
public class TestRegistryQuerier extends AbstractRegistryQuerier {

    public TestRegistryQuerier(int port) {
        WebHttpApplication helloWorld = new WebHttpApplication();
        helloWorld.setName("echoer");
        helloWorld.setURL("http://127.0.0.1" + port + ""
                + GetEchoerHTTPD.TEST_URI); //@TODO
        // fix
        // this

        applications.add(helloWorld);
    }
}