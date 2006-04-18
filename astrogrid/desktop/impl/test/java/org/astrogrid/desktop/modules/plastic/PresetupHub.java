/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import org.votech.plastic.PlasticHubListener;

/**
 * @author jdt
 *
 */
public class PresetupHub extends AbstractPlasticTestBase {

	protected PlasticHubListener hub;

	public void setUp() {
		super.setUp();
		hub = new PlasticHubImpl(executor , idGenerator, messenger,  rmi, web, new PrettyPrinterImpl(browser), config);
		((PlasticHubImpl)hub).start();	
	}

	public void tearDown() throws Exception {
		super.tearDown();
		((PlasticHubImpl)hub).halting();
		hub = null;
	}

}
