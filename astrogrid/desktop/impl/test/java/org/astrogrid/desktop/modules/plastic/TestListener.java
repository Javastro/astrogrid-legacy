/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

import org.votech.plastic.PlasticListener;

class TestListener implements PlasticListener {
	public TestListener(String name) {
		this.name = name;
	}
	private String name;
	public Object perform(URI sender, URI message, List args) {
		return name+sender.toString()+message.toString()+args.size();
	}
	
}