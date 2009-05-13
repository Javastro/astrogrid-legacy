/*
 * $Id: VosProtocol.java,v 1.1 2009/05/13 13:20:32 gtr Exp $
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
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.Test;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.Protocol;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegate;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegateResolver;
import org.astrogrid.vospace.client.delegate.AGVOSpaceDelegateResolverImpl;

/**
 * A {@link Protocol} for communicating with VOSpace. 
 * @TODO at the moment this has a very simplistic implementation - should be looked at again when the VOSpace delegate improves.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class VosProtocol implements Protocol, ComponentDescriptor {

    private static Log log = LogFactory.getLog(VosProtocol.class);
    
    private final AGVOSpaceDelegateResolver resolver;
    
    /**
     * Constructs a VosProtocol. This constructor suits the old
     * CEA-library up to v2008.1.xx.
     */
    public VosProtocol() {
      resolver = new AGVOSpaceDelegateResolverImpl();
    }

    /**
     * Constructs a VosProtocol. This constructor suits the revised
     * CEA-library starting at v2008.2.
     *
     * @param regEndpoint the endpoint URL of a registry query service.
     */
    public VosProtocol(URL regEndpoint) {
       	resolver = new AGVOSpaceDelegateResolverImpl(regEndpoint);
    }
    
    public ExternalValue createIndirectValue(final URI reference, final SecurityGuard secGuard)
	    throws InaccessibleExternalValueException {
log.debug("VosProtocol.createIndirectValue(URI, SecurityGuard)");
log.debug("  URI   [" + reference + "])");
log.debug("  Guard [" + ((null != secGuard) ? secGuard.getX500Principal() : null) + "])");
       	return new ExternalValue(){
            AGVOSpaceDelegate delegate = resolver.resolve(secGuard);
          	 
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


/*
 * $Log: VosProtocol.java,v $
 * Revision 1.1  2009/05/13 13:20:32  gtr
 * *** empty log message ***
 *
 * Revision 1.2  2008/10/13 10:51:35  clq2
 * PAL_KEA_2799
 *
 * Revision 1.1.2.5  2008/09/23 16:03:28  dave
 * Added debug to vos protocol handlers
 *
 * Revision 1.1.2.4  2008/09/22 16:50:39  dave
 * Updated to use default AGVOSpaceDelegateResolverImpl constructor
 *
 * Revision 1.1.2.3  2008/09/22 15:01:13  gtr
 * I added a constructor to support old-style (pre 2008.2) CEA.
 *
 * Revision 1.1.2.2  2008/09/22 13:36:01  dave
 * Changed calls to VOSpace delegate API to use read() and write()
 *
 * Revision 1.1.2.1  2008/09/11 15:22:36  gtr
 * New protocol module, added here for compatibility with old version of CEA server-library. If DSA is updated to use a newer CEA library then this class will not be needed any more.
 *
 * Revision 1.1  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 */
