package org.astrogrid.security;

import org.apache.ws.axis.security.WSDoAllReceiver;


/**
 * An Axis handler to check signatures on messages inbound to a service.
 * This is a trivial extension of the handler from WSS4J. It exists to
 * make it adjust the behaviour in later versions of the facade.
 *
 * @author Guy Rixon
 */
public class AxisServiceCredentialHandler extends WSDoAllReceiver {
}