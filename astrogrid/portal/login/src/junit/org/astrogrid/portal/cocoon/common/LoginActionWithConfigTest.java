/* $Id: LoginActionWithConfigTest.java,v 1.2 2004/04/21 13:56:29 jdt Exp $
 * Created on Apr 2, 2004 by jdt
 * The portal-head project
 * (c) 2004 
 *
 */
package org.astrogrid.portal.cocoon.common;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.astrogrid.community.common.security.service.SecurityServiceMock;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * Tests that play with the config need
 * to be in a separate class otherwise they
 * interfere with each other
 * @author jdt
 */
public class LoginActionWithConfigTest extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(LoginActionWithConfigTest.class);
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
     * Try bad config.  Give a registry url which is nonsense
     */
    public void testBadlyConfiguredBadURL() {
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
            assertTrue(le.getCause() instanceof MalformedURLException);
            return; //expected
        }
    }
    /**
     * Try bad config - good URL, but doesn't point to a Registry
     */
    public void testBadlyConfiguredGoodURL() {
        final Config config = SimpleConfig.getSingleton();
        config.setProperty(
                LoginAction.ORG_ASTROGRID_PORTAL_REGISTRY_URL,
                "http://www.google.com");  //A url that's safe as the bank of england
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
            //I hoped for a more informative exception, but this is what
            //we actually get.
            assertTrue(le.getCause() instanceof CommunityResolverException);
            return; //expected
        }
    }
    
    /**
     * Try bad config - good URL, but doesn't point to anything
     */
    public void testBadlyConfiguredNoneExistentURL() {
        final Config config = SimpleConfig.getSingleton();
        config.setProperty(
                LoginAction.ORG_ASTROGRID_PORTAL_REGISTRY_URL,
                "http://wwww.astrogrid.org/thisregistrydoesnotexist"); //need a url that returns a 404
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
            //I hoped for a more informative exception, but this is what
            //we actually get.
            assertTrue(le.getCause() instanceof CommunityResolverException);
            return; //expected
        }
    }
}


/*
 *  $Log: LoginActionWithConfigTest.java,v $
 *  Revision 1.2  2004/04/21 13:56:29  jdt
 *  moved a test to integration testing.
 *
 *  Revision 1.1  2004/04/05 16:39:52  jdt
 *  Moved tests that use configuration to a separate class, 
 *  otherwise they interfere with the unconfigured tests.  Added 
 *  news tests to exercise the CommunityPasswordResolver
 *
 */