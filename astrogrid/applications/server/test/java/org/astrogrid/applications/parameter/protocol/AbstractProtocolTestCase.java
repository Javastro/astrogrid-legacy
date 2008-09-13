/*
 * $Id: AbstractProtocolTestCase.java,v 1.3 2008/09/13 09:51:04 pah Exp $
 * 
 * Created on 25 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.parameter.protocol;

import org.astrogrid.registry.client.RegistryDelegateFactory;

import junit.framework.TestCase;

public abstract class AbstractProtocolTestCase extends TestCase {

    public AbstractProtocolTestCase() {
	super();
    }

    public AbstractProtocolTestCase(String name) {
	super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(RegistryDelegateFactory.QUERY_URL_PROPERTY, "http://dummy");//TODO should this really be necessary - but in the registry delegate to NPE?
    }

}

/*
 * $Log: AbstractProtocolTestCase.java,v $
 * Revision 1.3  2008/09/13 09:51:04  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:19:00  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 */
