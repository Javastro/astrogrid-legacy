/*
 * $Id: VosProtocol.java,v 1.3 2010/01/27 17:17:04 gtr Exp $
 * 
 * Created on 3 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.dataservice.service.cea;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.Test;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.slinger.sourcetargets.CredentialResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolverImpl;

/**
 * An {@link org.astrogrid.applications.parameter.protocol.Protocol}
 * for communicating with VOSpace.
 * <p>
 * This is a dummy; the Application sub-type for DSA actually has the proper
 * code for VOSpace as a special case and this protocol is not actually used.
 * However, CEA requires that there be a protocol matching the vos scheme so
 * here it is.
 *
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class VosProtocol implements Protocol, ComponentDescriptor {

    private static Log log = LogFactory.getLog(VosProtocol.class);
    
    private final SystemDelegateResolver resolver;
    
    /**
     * Constructs a VosProtocol. This constructor suits the old
     * CEA-library up to v2008.1.xx.
     */
    public VosProtocol() {
      resolver = new SystemDelegateResolverImpl();
    }

    /**
     * Constructs a VosProtocol. This constructor suits the revised
     * CEA-library starting at v2008.2.
     *
     * @param regEndpoint the endpoint URL of a registry query service.
     */
    public VosProtocol(URL regEndpoint) {
       	resolver = new SystemDelegateResolverImpl(regEndpoint);
    }
    
    public ExternalValue createIndirectValue(final URI reference, final SecurityGuard secGuard)
	    throws InaccessibleExternalValueException {
      log.debug("VosProtocol.createIndirectValue(URI, SecurityGuard)");
      log.debug("  URI   [" + reference + "])");
      log.debug("  Guard [" + ((null != secGuard) ? secGuard.getX500Principal() : null) + "])");
      return new ExternalValue(){
            SystemDelegate delegate = resolver.resolve(new CredentialResolver(secGuard));

  public InputStream read() throws InaccessibleExternalValueException {
		try {
		    return delegate.read(reference);
		} catch (Exception e) { 
		   throw new InaccessibleExternalValueException("Error communicating with VOSpace", e);
		} 
		
	    }

	    public OutputStream write()
		    throws InaccessibleExternalValueException {
		try {
		return delegate.write(reference);
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

  public ExternalValue createIndirectValue(URI uri) throws InaccessibleExternalValueException {
    return createIndirectValue(uri, new SecurityGuard());
  }

}