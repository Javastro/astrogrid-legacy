/*
 * $Id: VosProtocol.java,v 1.6 2009/06/10 10:40:08 pah Exp $
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
import java.security.cert.CertificateException;

import junit.framework.Test;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolverImpl;

/**
 * A {@link Protocol} for communicating with VOSpace. 
 * @TODO at the moment this has a very simplistic implementation - should be looked at again when the VOSpace delegate improves.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 4 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class VosProtocol implements Protocol, ComponentDescriptor {

    
    private final SystemDelegateResolver resolver;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(VosProtocol.class);
    

    /**
     * Standard constuctor
     * @param regEndpoint the endpoint URL of a registry query service.
     */
    public VosProtocol(URL regEndpoint) {
       	resolver = new SystemDelegateResolverImpl(regEndpoint);//IMPL is there a factory for this - pointless having different implementations otherwise.
    }
    
    public ExternalValue createIndirectValue(final URI reference, final SecurityGuard secGuard)
	    throws InaccessibleExternalValueException {
       	try {
            return new VOSExternalValue(secGuard,reference);
        } catch (CertificateException e) {
           throw new InaccessibleExternalValueException("cannot access "+ reference,e);
        }
   }

    
    private class VOSExternalValue implements ExternalValue {
        private final SystemDelegate delegate;
        private final URI reference;
        VOSExternalValue(SecurityGuard secGuard, URI ref) throws CertificateException{
            this.reference = ref;
            logger.debug("secguard signedon="+secGuard.isSignedOn() +" id="+ secGuard.getX500Principal());
            if(secGuard.isSignedOn()){
                delegate = resolver.resolve(secGuard);
            }
            else 
            {
                delegate = resolver.resolve();
            }
            return;
        }
        public InputStream read() throws InaccessibleExternalValueException {
            try {
                return  delegate.read(reference);
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
 * Revision 1.6  2009/06/10 10:40:08  pah
 * RESOLVED - bug 2917: update to latest VOSpace delegate
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2917
 *
 * upgrade to latest VOSpace delegate & and ensure that proxy delegation does not happen here.
 *
 * Revision 1.5  2009/05/22 14:47:21  pah
 * try to force the loaddelegation earlier
 *
 * Revision 1.4  2009/05/18 09:19:47  pah
 * NEW - bug 2903: update to latest VOSpace client
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2903
 *
 * only use the secure vospace delegate if the securityGuard is signed on.
 *
 * Revision 1.3  2008/09/15 19:26:18  pah
 * remove unused imports
 *
 * Revision 1.2  2008/09/15 19:25:43  pah
 * use new simpler delegate interface.
 *
 * Revision 1.1  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 */
