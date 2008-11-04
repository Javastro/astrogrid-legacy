/**
 * 
 */
package org.astrogrid.desktop.modules.test;

import org.astrogrid.acr.test.SessionTest;

/** integration test support for sessioning.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 22, 20074:45:54 PM
 */
public class SessionTestImpl implements SessionTest {

	public String get() {
		return theString;
	}

	public void put(final String arg0) {
		theString = arg0;
	}
	
	protected String theString;

}
