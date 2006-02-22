/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.astrogrid.common.namegen.NameGen;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;
import junit.framework.TestCase;

/**
 * @author jdt
 *
 */
public class AbstractPlasticTestBase extends TestCase{

	protected MessengerImpl messenger;
	protected Executor executor;
	protected NameGen idGenerator;
	protected WebServer web;
	protected RmiServer rmi;

	public void setUp() {
		messenger = new MessengerImpl();
		executor = new DirectExecutor();
		idGenerator = new InMemoryNameGen();
		web = new WebServer() {
		
					public String getUrlRoot() {
						return "http://127.0.0.1:"+getPort()+"/"+getKey()+"/";
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
		
	}

}
