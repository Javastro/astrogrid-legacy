/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.astrogrid.common.namegen.NameGen;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.alternatives.InThreadExecutor;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.easymock.MockControl;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * @author jdt
 */
public class AbstractPlasticTestBase extends TestCase {

	protected static final URI SENDER_ID = URI.create("ivo://junit");


    protected UIContext ui; 
    
    protected String version = "test";
    
	protected Executor executor;

	protected NameGen idGenerator;

	protected WebServer web;

	protected RmiServer rmi;
	
	protected BrowserControl browser;
	

	
	protected Preference notifyEvents;
	
	protected Shutdown shutdown;

	protected void tearDown() throws Exception {
		super.tearDown();
		executor = null;
		idGenerator = null;
		web = null;
		rmi = null;
		browser = null;
		notifyEvents = null;
		shutdown = null;
		ui = null;
	}
	
	protected void setUp() {
		
		notifyEvents = new Preference();
		notifyEvents.setDefaultValue("true");
		notifyEvents.setName("notifyEvents");
		BackgroundExecutor be = new InThreadExecutor();			
		ui = new UIContextImpl(null,be,null,null);
		executor = new DirectExecutor();
		idGenerator = new InMemoryNameGen();
		web = new WebServer() {

			public String getUrlRoot() {
				return "http://127.0.0.1:" + getPort() + "/" + getKey() + "/";
			}
			
			public URL getRoot() {
				try {
					return new URL(getUrlRoot());
				} catch (MalformedURLException x) {
					throw new RuntimeException(x);
				}
			}

			public String getKey() {
				return "foobar";
			}

			public int getPort() {
				return 8001;
			}

		};
		rmi = new RmiServer() {

			public int getPort() {
				return 1234;
			}

		};
/* NW - unneeded?
		config = new Configuration() {
			Map store = new HashMap();
			public boolean setKey(String arg0, String arg1) {
				store.put(arg0,arg1);
				return true;
			}

			public String getKey(String arg0) {
				return (String) store.get(arg0);
			}

			public String[] listKeys() throws ACRException {
				return (String[]) store.keySet().toArray(new String[]{});
			}

			public Map list() throws ACRException {
				return store;
			}

			public void removeKey(String arg0) {
				store.remove(arg0);
			}
			
		};
		*/
		
		shutdown = new Shutdown() {

			public void halt() {
				// TODO Auto-generated method stub
				
			}

			public void reallyHalt() {
				// TODO Auto-generated method stub
				
			}

			public void addShutdownListener(ShutdownListener arg0) {
				// TODO Auto-generated method stub
				
			}

			public void removeShutdownListener(ShutdownListener arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}
