package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.Collection;
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
    private PlasticHubListener hub;
    private static final String DEFAULTAPPNAME = "Test Application";

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
     * If you don't defaults will be used.
     * @param appData
     * @return
     */
    protected abstract TestPlasticApplication getApplication(Properties appData);

    public interface TestPlasticApplication {
        /**
         * Keys into the properties file to define application metadata
         */
        public static final String NAME="NAME";
        public static final String DESC="DESC";
        public static final String IVORN="IVORN";
        public static final String LOGOURL="LOGOURL";
        public static final String VERSION="VERSION";
        /**
         * Different clients will have slightly different registration methods,
         * so we leave the registration to them.
         * @param hub
         * @return
         */
        public URI registerWith(PlasticHubListener hub, String name);
        public List getMessages();
        public void addHandler(MessageHandler h) ;
    }
    
    
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
        URI appId = app.registerWith(hub,"app");
        String hubName = basicHubMetadataCheck(CommonMessageConstants.GET_NAME, appId);
        assertEquals("Astro Runtime", hubName);
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
        TestPlasticApplication app = getApplication(null);
        final String TESTAPPNAME = "Test Application";
        URI plid = app.registerWith(hub, TESTAPPNAME); 
        assertNotNull(plid);
        
        String name = hub.getName(plid);
        assertEquals(TESTAPPNAME,name);
        
        List regIds = hub.getRegisteredIds();
        assertTrue("Registered apps doesn't include the new one",regIds.contains(plid)); //currently fails for the xml-rpc version due to type conversion problems
    }
    
    public void testUnregistersOK() {
        TestPlasticApplication app = getApplication(null);
        URI plid = app.registerWith(hub, DEFAULTAPPNAME); 
        assertNotNull(plid);
        
        hub.unregister(plid);
        
        String name = hub.getName(plid);
        assertEquals(CommonMessageConstants.RPCNULL,name);
        
        List regIds = hub.getRegisteredIds();
        assertFalse("App hasn't unregistered",regIds.contains(plid)); //currently fails for the xml-rpc version due to type conversion problems
    }
    
    public void testApplicationsUnderstoodMessages() {
        TestPlasticApplication app = getApplication(null);
        final String TESTAPPNAME = "Test Application";
        URI plid = app.registerWith(hub, TESTAPPNAME);
        
        List messages = hub.getUnderstoodMessages(plid);
        
        assertEquals("Hub's message list not same as app's",app.getMessages(), messages);
        
    }
    
    private Map createdApps = new HashMap() ;
    private URI  createAndRegisterCleanApp(int i, MessageHandler handler) {
        String name = "Application "+i;
        CachingMessageHandler cache = new CachingMessageHandler();
        Properties papp = new Properties();
        papp.setProperty(TestPlasticApplication.NAME, name);
        TestPlasticApplication app = getApplication(papp);
        app.addHandler(cache);
        if (handler!=null) app.addHandler(handler);
        
        URI plid = app.registerWith(hub, name);
        createdApps.put(plid, cache);
//      Just check there's nothing cached
        cache.clearMessages();
        return plid;
    }
    public void testBroadcastMessage() {
        URI plid1 = createAndRegisterCleanApp(1,null);
        URI plid2 = createAndRegisterCleanApp(2,null);
        URI plid3 = createAndRegisterCleanApp(3,null);
        
        //broadcast the message from app1
        Map results = hub.request(plid1, CommonMessageConstants.GET_NAME, new Vector());
        
        String name1 = (String) results.get(plid1);
        String name2 = (String) results.get(plid2); 
        String name3 = (String) results.get(plid3);
        
        assertNull(name1);// no result returned for sender
        assertEquals("Application 2", name2);
        assertEquals("Application 3", name3);
        
        CachingMessageHandler cache1 = (CachingMessageHandler) createdApps.get(plid1);
        CachingMessageHandler cache2 = (CachingMessageHandler) createdApps.get(plid2);
        CachingMessageHandler cache3 = (CachingMessageHandler) createdApps.get(plid3);
      
        CachingMessageHandler.Message sentMessage = new CachingMessageHandler.Message(plid1, CommonMessageConstants.GET_NAME, new Vector());
        
        //Only apps 2 and 3 should have got it
        Collection app1Messages = cache1.getMessages();
        assertFalse(app1Messages.contains(sentMessage));
        Collection app2Messages = cache2.getMessages();
        assertTrue(app2Messages.contains(sentMessage));
        Collection app3Messages = cache3.getMessages();
        assertTrue(app3Messages.contains(sentMessage));
        
    }
    
  

}
