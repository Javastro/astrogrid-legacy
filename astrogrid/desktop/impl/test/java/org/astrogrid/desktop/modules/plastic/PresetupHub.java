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
		hub = new PlasticHubImpl(version, ui, executor , idGenerator,   rmi, web, new PrettyPrinterImpl(browser), notifyEvents);
		((PlasticHubImpl)hub).start();	
	}

	public void tearDown() throws Exception {
		super.tearDown();
		((PlasticHubImpl)hub).halting();
		hub = null;
	}

}
