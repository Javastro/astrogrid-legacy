/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;

import org.votech.plastic.PlasticListener;

class TestListener2 implements PlasticListener {
	URI message = null;
	List args;
	URI sender;
	public URI getMessage() {
		return message;
	}
	public Object perform(URI sender, URI message, List args) {
		this.message = message;
		this.args = args;
		this.sender = sender;
		return sender.toString()+message.toString()+args.size();
	}

	public List getArgs() {
		return args;
	}
	/**
	 * @return Returns the sender.
	 */
	public URI getSender() {
		return sender;
	}
	
}