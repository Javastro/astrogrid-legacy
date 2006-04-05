package org.astrogrid.desktop.modules.plastic;

import org.jdesktop.jdic.desktop.DesktopException;
import org.jdesktop.jdic.desktop.Message;

public interface EmailClientControlInternal {

	/**
	 * Send a message using jdic
	 * @param message
	 * @throws DesktopException
	 * TODO consider abstracting this away from jdic-specific message
	 */
	void send(Message message) throws DesktopException;

}