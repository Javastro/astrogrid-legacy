/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import org.picocontainer.Startable;
import org.votech.plastic.PlasticHubListener;

/**
 * @author jdt
 *
 */
public class PresetupHub extends AbstractPlasticTestBase {

	protected PlasticHubListener hub;

	public void setUp() {
		super.setUp();
		hub = new PlasticHubImpl(executor , idGenerator, messenger,  rmi, web, new PrettyPrinterImpl(browser), config, shutdown);
		((Startable)hub).start();	
	}

	public void tearDown() throws Exception {
		super.tearDown();
		((Startable)hub).stop();
		hub = null;
	}

}
