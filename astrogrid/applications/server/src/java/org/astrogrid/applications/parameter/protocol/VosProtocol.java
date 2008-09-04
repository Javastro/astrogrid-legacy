/*
 * $Id: VosProtocol.java,v 1.1 2008/09/04 19:10:53 pah Exp $
 * 
 * Created on 3 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import junit.framework.Test;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegate;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegateResolver;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegateResolverImpl;
import org.astrogrid.vospace.v11.client.exception.ConnectionException;
import org.astrogrid.vospace.v11.client.transfer.export.ExportConnection;
import org.astrogrid.vospace.v11.client.transfer.export.ExportTransferResponse;
import org.astrogrid.vospace.v11.client.transfer.inport.InportConnection;
import org.astrogrid.vospace.v11.client.transfer.inport.InportTransferResponse;

/**
 * A {@link Protocol} for communicating with VOSpace. 
 * @TODO at the moment this has a very simplistic implementation - should be looked at again when the VOSpace delegate improves.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class VosProtocol implements Protocol, ComponentDescriptor {

    
    private final AGVOSpaceDelegateResolver resolver;

    /**
     * Standard constuctor
     * @param regEndpoint the endpoint URL of a registry query service.
     */
    public VosProtocol(URL regEndpoint) {
       	resolver = new AGVOSpaceDelegateResolverImpl(regEndpoint);
    }
    
    public ExternalValue createIndirectValue(final URI reference, final SecurityGuard secGuard)
	    throws InaccessibleExternalValueException {
       	return new ExternalValue(){
            AGVOSpaceDelegate delegate = resolver.resolve(secGuard);
          	 
	    public InputStream read() throws InaccessibleExternalValueException {
		try {
		    ExportTransferResponse req = delegate.export(reference);
		    Iterator<ExportConnection> conns = req.connections().iterator();
		    if (conns.hasNext()) {
		        ExportConnection conn = conns.next();//IMPL just choose the first - if the delegate can always produce a stream what do I care what the protocol is...
		        return conn.stream();
		    } else {
		        throw new ConnectionException("no connection to VOSpace available");
		    }
		} catch (Exception e) { 
		   throw new InaccessibleExternalValueException("Error communicating with VOSpace", e);
		} 
		
	    }

	    public OutputStream write()
		    throws InaccessibleExternalValueException {
		try {
		InportTransferResponse req = delegate.inport(reference);
		Iterator<InportConnection> conns = req.connections().iterator();
		    if (conns.hasNext()) {
		        InportConnection conn = conns.next();//IMPL just choose the first - if the delegate can always produce a stream what do I care what the protocol is...
		        return conn.stream();
		    } else {
		        throw new ConnectionException("no connection to VOSpace available");
		    }
		} catch (Exception e) {
		    throw new InaccessibleExternalValueException("Error communicating with VOSpace", e);
		}
		
		
	    }
       	    
       	};
   }

    public String getProtocolName() {
	return "vos";
    }

    public String getDescription() {
	return "protocol adapter for vos: VOSpace urns";
    }

    public Test getInstallationTest() {
	return null;
    }

    public String getName() {
	return "VOSProtocol";
    }
}


/*
 * $Log: VosProtocol.java,v $
 * Revision 1.1  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 */
