/*
 * $Id: LoginActionTest.java,v 1.5 2004/04/02 11:53:16 jdt Exp $ 
 * Created on Mar 23, 2004 by jdt 
 * Copyright (C) AstroGrid. All rights reserved. 
 * It's my birthday! 
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *  
 */
package org.astrogrid.portal.cocoon.common;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.astrogrid.community.common.security.service.SecurityServiceMock;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
/**
 * JUnit test
 * 
 * @author jdt
 */
public final class LoginActionTest extends TestCase {
    /**
     * Fire up the text ui
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LoginActionTest.class);
    }
    /**
     * Usual setup method. In this case initialise any config params.
     * 
     * @see junit.framework.TestCase#setUp()
     *  
     */
    public void setUp() {
        SecurityServiceMock.setPassword("secret");
    }
    /**
     * Try a successful login
     * 
     * @throws LoginException - it shouldn't!
     */
    public void testLogin() throws LoginException {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "org.astrogrid.mock");
        request.addParameter(LoginAction.PASS_PARAM, "secret");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        final Map results = action.act(null, null, objectModel, null, null);
        assertNotNull(results);
    }
    /**
     * Try an unsuccessful login
     * 
     * @throws LoginException - it shouldn't!
     */
    public void testLoginFailed() throws LoginException {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "org.astrogrid.mock");
        request.addParameter(LoginAction.PASS_PARAM, "wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        final Map results = action.act(null, null, objectModel, null, null);
        assertNull(results);
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams1() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        //request.addParameter(LoginAction.USER_PARAM,"John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "roe");
        request.addParameter(LoginAction.PASS_PARAM, "wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams2() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        //request.addParameter(LoginAction.COMMUNITY_PARAM,"roe");
        request.addParameter(LoginAction.PASS_PARAM, "wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams3() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "roe");
        //request.addParameter(LoginAction.PASS_PARAM,"wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams4() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "roe");
        request.addParameter(LoginAction.PASS_PARAM, "wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams5() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "");
        request.addParameter(LoginAction.PASS_PARAM, "wrongpassword");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try an unsuccessful login due to missing parameters
     */
    public void testMissingParams6() {
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "roe");
        request.addParameter(LoginAction.PASS_PARAM, "");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
    /**
     * Try bad config
     */
    public void testBadlyConfigured() {
        final Config config = SimpleConfig.getSingleton();
        config.setProperty(
            LoginAction.ORG_ASTROGRID_PORTAL_REGISTRY_URL,
            "bad address");
        final LoginAction action = new LoginAction();
        final Map objectModel = new HashMap();
        final DummyRequest request = new DummyRequest();
        request.addParameter(LoginAction.USER_PARAM, "John");
        request.addParameter(LoginAction.COMMUNITY_PARAM, "roe");
        request.addParameter(LoginAction.PASS_PARAM, "pass");
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        try {
            action.act(null, null, objectModel, null, null);
            fail("Expected a LoginException");
        } catch (LoginException le) {
            return; //expected
        }
    }
}
/*
 * $Log: LoginActionTest.java,v $
 * Revision 1.5  2004/04/02 11:53:16  jdt
 * Merge from PLGN_JDT_bz#281a
 *
 *  
 */