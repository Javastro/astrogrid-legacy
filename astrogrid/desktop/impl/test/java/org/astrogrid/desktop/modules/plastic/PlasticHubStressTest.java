package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.ExtendableHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.messages.hub.ApplicationChangeListener;
import org.votech.plastic.incoming.messages.hub.ApplicationRegisteredMessageInvoker;
import org.votech.plastic.incoming.messages.hub.ApplicationUnregisteredMessageInvoker;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;

/**
 * A simple stress test to hammer the Hub.
 * Register and unregister lots of applications and see what breaks.
 * @author jdt
 *
 */
public class PlasticHubStressTest extends TestCase {
    private PlasticHubListener hub;
    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new Finder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        return listener;
    }
    
    public void setUp() throws Exception {
        super.setUp();
        hub = getHub();
    }
    public void testRegUnReg() throws InterruptedException {
        final int NLISTENERS = 100;
        
        final CountDown gate = new CountDown(NLISTENERS);
        
        /**
         * Wrap any TestPlasticApplication and be aware of AppRegistered/Unregister messages.
         * @author jdt
         *
         */
        class AppRegisteredAwareDecorator implements TestPlasticApplication, ApplicationChangeListener {
            private TestPlasticApplication core;
            public AppRegisteredAwareDecorator(TestPlasticApplication app) {
                this.core = app;
                ExtendableHandler handler = new ExtendableHandler();
                handler.addMessageInvoker(new ApplicationRegisteredMessageInvoker(this));
                handler.addMessageInvoker(new ApplicationUnregisteredMessageInvoker(this));
                core.appendHandler(handler);
            }
            public URI registerWith(PlasticHubListener hub) {
                return core.registerWith(hub);
            }
            public List getMessages() {
                return core.getMessages();
            }
            public void appendHandler(MessageHandler h) {
                core.appendHandler(h);
                
            }
            public boolean isDeaf() {
                return core.isDeaf();
            }
            
            private int registeredAppsThatIKnowAbout = 0;
            public void applicationRegistered(URI plid) {
                // Can't actually do much ... just want to receive the message
                registeredAppsThatIKnowAbout++; //no need for synch...atomic
            }
            public void applicationUnregistered(URI plid) {
                // Can't actually do much ... just want to receive the message
                registeredAppsThatIKnowAbout--;
            }
            /**
             * @return Returns the registeredAppsThatIKnowAbout.
             */
            public int getRegisteredAppsThatIKnowAbout() {
                return registeredAppsThatIKnowAbout;
            }
            /**
             * @param registeredAppsThatIKnowAbout The registeredAppsThatIKnowAbout to set.
             */
            public void setRegisteredAppsThatIKnowAbout(int registeredAppsThatIKnowAbout) {
                this.registeredAppsThatIKnowAbout = registeredAppsThatIKnowAbout;
            }
            public String getName() {
                return core.getName();
            }



            
        }
        
        /**
         * Give a test application, will wait a random time, then register and unregister
         * @author jdt
         *
         */
        class RegisteringThread implements Runnable {
            private TestPlasticApplication listener;
            private PlasticHubListener hub;

            public RegisteringThread(TestPlasticApplication l, PlasticHubListener h) {
                this.listener = l;
                this.hub = h;
            }
            
            public void run() {
                URI plid = null;
                try {
                    Thread.sleep((long) (Math.random()*2000));
                    plid = listener.registerWith(hub);
                    Thread.sleep((long) (Math.random()*2000));
                } catch (InterruptedException e) {
                    // doesn't matter
                } finally {
                    hub.unregister(plid);
                    gate.release();
                }
            }
        }
        
        
        AppRegisteredAwareDecorator monitor = new AppRegisteredAwareDecorator(new TestListenerRMI(null,null));
        
        URI monitorPlid = monitor.registerWith(hub);
        Thread.sleep(1000); //Wait for messages to arrive.  This is fragile, but I can't think of another way to do it without polluting the hub interface.
        assertEquals("I should have received an application registered message about myself",1,monitor.getRegisteredAppsThatIKnowAbout());
        monitor.setRegisteredAppsThatIKnowAbout(0); //reset the clock
        
        //Setup
        List testApps = new ArrayList();
        for (int i=0;i<NLISTENERS;++i) {
            TestPlasticApplication grunt;
            if (i % 2 ==0) {
                grunt = new AppRegisteredAwareDecorator(new TestListenerRMI(null, null)); 
            } else {
                grunt =  new AppRegisteredAwareDecorator(new TestListenerXMLRPC(null, null));
            }
            testApps.add(grunt);
        }       
        
        //run 
        
        Executor executor = new ThreadedExecutor();
        
        for (int i=0;i<NLISTENERS;++i) {
            Runnable runner = new RegisteringThread((TestPlasticApplication) testApps.get(i), hub);
            try {
                executor.execute(runner);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        
        try {
            gate.acquire();
        } catch (InterruptedException e) {
            System.err.println("Interrupted aquiring gate...test result might not be correct");
        }
        hub.unregister(monitorPlid);
        Thread.sleep(20000); //Wait for messages to arrive.  This is fragile, but I can't think of another way to do it without polluting the hub interface.
        List stillRegistered = hub.getRegisteredIds();
        stillRegistered.remove(hub.getHubId());
        
        assertEquals("Only the hub should still be registered",0,stillRegistered.size());
        assertEquals("Monitor should have received equal numbers of register and unregister messages",0,monitor.getRegisteredAppsThatIKnowAbout());
        
    }

}
