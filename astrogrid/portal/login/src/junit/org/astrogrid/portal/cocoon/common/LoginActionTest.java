/* $Id: LoginActionTest.java,v 1.2 2004/03/24 18:31:33 jdt Exp $
 * Created on Mar 23, 2004 by jdt Copyright (C) AstroGrid. All rights reserved.
 * It's my birthday!
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *
 */
package org.astrogrid.portal.cocoon.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import junit.framework.TestCase;

/**
 * JUnit test
 * @author jdt
 */
public final class LoginActionTest extends TestCase {
    /**
     * Fire up the text ui
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LoginActionTest.class);
    }
    /**
     * Usual setup method.  In this case initialise any
     * config params.
     * @see junit.framework.TestCase#setUp()
     *
     */
    public void setUp() {
        final Config config = SimpleConfig.getSingleton();
        config.setProperty(LoginAction.ORG_ASTROGRID_PORTAL_COMMUNITY_URL,
                "dummy");
    }
    /**
     * Try a successful login
     * @throws LoginException - it shouldn't!
     */
    public void testAct() throws LoginException {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.NAME_PARAM,"John");
        request.addParameter(LoginAction.COMMUNITY_PARAM,"roe");
        request.addParameter(LoginAction.PASS_PARAM,"secret");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        
        final Map results = action.act(null,null,objectModel,null,null);
        assertNotNull(results);
    }
}


/*
 *  $Log: LoginActionTest.java,v $
 *  Revision 1.2  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *  Revision 1.1.2.1  2004/03/24 18:15:02  jdt
 *  new
 *
 */