package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestCase;

import org.astrogrid.acr.ACRException;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;

/**
 * Factor out general tests of the hub, leaving subclsses to determine
 * the plastic client's choice of comms, and how the hub is found.
 * @author jdt
 *
 */
public abstract class AbstractPlasticBase extends TestCase {
    protected PlasticHubListener hub;
    protected static final String DEFAULTAPPNAME = "Test Application";

    /**
     * Subclasses responsible for creating a hub, either as a mockup, or during
     * system tests using the full (though still in-process) AR.
     * @return
     * @throws ACRException 
     */
    protected abstract PlasticHubListener getHub() throws Exception;
    /**
     * Create an application of the appropriate type e.g. RMI or XML-RPC
     * You can supply a set of properties defining name, description etc.
     * If you don't then defaults will be used.
     * @param appData values for name etc...see TestPlasticApplication for keys.  Can be null
     * @param handler default message handler (ie first in the chain).  May be null.
     * @return
     */
    protected abstract TestPlasticApplication getApplication(Properties appData, MessageHandler handler);

    protected TestPlasticApplication getApplication(Properties appData) {
        return getApplication(appData,null);
    }

    /**
     * Set up the test env.
     * Must be called by any subclasses that override it.
     */
    public void setUp() throws Exception {
        // Any subclasses that override this method, must delegate to us too.
        hub = getHub();

    }
    
    public void tearDown() throws Exception {
        //clear up as best as we can
        List plids = hub.getRegisteredIds();
        URI hubId = hub.getHubId();
        plids.remove(hubId); 
        for (Iterator it = plids.iterator();it.hasNext();) {
            //Note: Noel found the following line gave classcasts due to the iterator returning
            //Strings.  Works for me though.  JOHN
        	URI plid = (URI) it.next();
            hub.unregister(plid);
        }
        List newPlids = hub.getRegisteredIds();
        if (newPlids.size()!=1) {
            throw new RuntimeException("Hub hasn't cleaned up properly between tests");
        }
    }
    
    


    
    /** 
     * Check that any subclasses have set us up OK.
     *
     */
    public void testSetUpOK() {
        assertNotNull(hub);
    }
    
    public void testHubId() {
        
        TestPlasticApplication app = getApplication(null);
        URI appId = app.registerWith(hub);
        String hubName = basicHubMetadataCheck(CommonMessageConstants.GET_NAME, appId);
        //assertEquals("AstroGrid Workbench", hubName); NWW it varties. when run in isolation it's 'Astro Runtime', when run asa part of all integration tests, its' AstroGrid Workencnh'
       // assertTrue(hubName.equals("AstroGrid Workbench") || hubName.equals("Astro Runtime") || hubName.equals("Plastic Hub"));
        // just check it's something..
        assertNotNull(hubName);
        basicHubMetadataCheck(CommonMessageConstants.GET_DESCRIPTION, appId);
        basicHubMetadataCheck(CommonMessageConstants.GET_IVORN, appId);
        basicHubMetadataCheck(CommonMessageConstants.GET_ICON, appId);
        String hubVersion = basicHubMetadataCheck(CommonMessageConstants.GET_VERSION, appId);
        assertEquals(PlasticListener.CURRENT_VERSION, hubVersion);
    }
    private String basicHubMetadataCheck(URI message, URI appId) {
        URI hubId = hub.getHubId();
        Map names = hub.request(appId, message ,new Vector());
        String hubResponse = (String) names.get(hubId);
        //we should at least get something back
        assertNotNull(hubResponse);
        assertFalse(CommonMessageConstants.RPCNULL.equals(hubResponse));
        return hubResponse;
    }
    
    public void testRegistersOK() {
        final String TESTAPPNAME = "Test Application";
        Properties props = new Properties();
        props.put(TestPlasticApplication.NAME, TESTAPPNAME);
        TestPlasticApplication app = getApplication(props);
        
        URI plid = app.registerWith(hub); 
        assertNotNull(plid);
        
        String name = hub.getName(plid);
        assertEquals(TESTAPPNAME,name);
        
        List regIds = hub.getRegisteredIds();
        assertTrue("Registered apps doesn't include the new one",regIds.contains(plid)); //currently fails for the xml-rpc version due to type conversion problems
    }
    
    public void testUnregistersOK() {
        TestPlasticApplication app = getApplication(null);
        URI plid = app.registerWith(hub); 
        assertNotNull(plid);
        
        hub.unregister(plid);
        
        String name = hub.getName(plid);
        assertEquals(CommonMessageConstants.RPCNULL,name);
        
        List regIds = hub.getRegisteredIds();
        assertFalse("App hasn't unregistered",regIds.contains(plid)); //currently fails for the xml-rpc version due to type conversion problems
    }
    
    /**
     * Check how vulnerable we are to
     * badly written clients.
     *
     */
    public void testHardened() {
        hub.unregister(hub.getHubId());
        hub.unregister(null);
        hub.unregister(URI.create("http://news.bbc.co.uk"));
        
        hub.getName(null);
        hub.getName(URI.create("http://foo"));
        
        hub.getMessageRegisteredIds(null);
        hub.getMessageRegisteredIds(URI.create("http://foo"));
        
        hub.getUnderstoodMessages(null);
        hub.getUnderstoodMessages(URI.create("http://foo"));
        
        hub.registerNoCallBack(null);
        hub.registerRMI(null,null,null);
        hub.registerXMLRPC(null,null,null);
        
        hub.requestToSubset(null,null,null,null);
        
        //test that registering these items doesn't screw things up
        hub.getRegisteredIds();
        hub.request(hub.getHubId(), CommonMessageConstants.GET_NAME, new Vector());
        
    }
    
    public void testApplicationsUnderstoodMessages() {
        final String TESTAPPNAME = "Test Application";
        Properties props = new Properties();
        props.put(TestPlasticApplication.NAME, TESTAPPNAME);
        TestPlasticApplication app = getApplication(null);
        
        URI plid = app.registerWith(hub);
        
        List messages = hub.getUnderstoodMessages(plid);
        
        assertEquals("Hub's message list not same as app's",app.getMessages(), messages);
        
    }
    
    //TODO YUK
    private Map createdAppCaches = new HashMap() ;
    private Map createdApps = new HashMap() ;
    protected URI  createAndRegisterCleanApp(int i, MessageHandler handler) {
        String name = "Application "+i;
        CachingMessageHandler cache = new CachingMessageHandler();
        Properties papp = new Properties();
        papp.setProperty(TestPlasticApplication.NAME, name);
        TestPlasticApplication app = getApplication(papp, cache);
        
        if (handler!=null) app.appendHandler(handler);
        
        URI plid = app.registerWith(hub);
        System.out.println("Registered application: "+app.getName()+" with plid "+plid);
        createdAppCaches.put(plid, cache);
        createdApps.put(plid, app);
//      Just check there's nothing cached
        cache.clearMessages();
        return plid;
    }
    
    /* commented out until we investigate wha't going wrong with it
    public void testBroadcastMessage() {
        URI plid1 = createAndRegisterCleanApp(1,null);
        URI plid2 = createAndRegisterCleanApp(2,null);
        URI plid3 = createAndRegisterCleanApp(3,null);

        //TestPlasticApplication app1 = (TestPlasticApplication) createdApps.get(plid1);
        TestPlasticApplication app2 = (TestPlasticApplication) createdApps.get(plid2);
        TestPlasticApplication app3 = (TestPlasticApplication) createdApps.get(plid3);

        //broadcast the message from app1
        Map results = hub.request(plid1, CommonMessageConstants.GET_NAME, new Vector());
        
        String name1 = (String) results.get(plid1);
        String name2 = (String) results.get(plid2); 
        String name3 = (String) results.get(plid3);
        
        assertNull(name1);// no result returned for sender
        
        //TODO delete
        System.out.println("Application 2: "+app2.getName()+" with ID "+plid2+" returned "+name2);
        
        
        if (!app2.isDeaf()) {
            assertEquals("Application 2", name2);
        } else {
            assertNull(name2); //cannot hear you!
        }
        
        if (!app3.isDeaf()) {
            assertEquals("Application 3", name3);
        } else {
            assertNull(name3); //cannot hear you!
        }
         
        CachingMessageHandler cache1 = (CachingMessageHandler) createdAppCaches.get(plid1); //TODO build the cache into the test app...we use it all the time
        CachingMessageHandler cache2 = (CachingMessageHandler) createdAppCaches.get(plid2);
        CachingMessageHandler cache3 = (CachingMessageHandler) createdAppCaches.get(plid3);
      
        CachingMessageHandler.Message sentMessage = new CachingMessageHandler.Message(plid1, CommonMessageConstants.GET_NAME, new Vector());
        
        //Only apps 2 and 3 should have got it
        Collection app1Messages = cache1.getMessages();
        assertFalse(app1Messages.contains(sentMessage));
        Collection app2Messages = cache2.getMessages();
        assertTrue(app2.isDeaf() != app2Messages.contains(sentMessage));
        Collection app3Messages = cache3.getMessages();
        assertTrue(app3.isDeaf() != app3Messages.contains(sentMessage));
        
    }
    */
  

}
