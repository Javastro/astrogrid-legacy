/*
 * $Id: RegistryQueryLocatorImpl.java,v 1.2 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 9 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import java.net.URISyntaxException;

import net.ivoa.resource.registry.Registry;

import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.store.Ivorn;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

/**
 * Simple default registry querier implementation that simply uses the standard registry delegate factories.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Jun 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
@Component
public class RegistryQueryLocatorImpl implements RegistryQueryLocator {
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(RegistryQueryLocatorImpl.class);
    

    public RegistryService getClient() throws RegistryException {
        return RegistryDelegateFactory.createQueryv1_0();// just return the querier that uses the default properties.
    }

    public boolean isRegistered(String id) {
        try {
            Ivorn ivorn = new Ivorn(id);
            ResourceData res = this.getClient().getResourceDataByIdentifier(ivorn);
            return res!=null;//TODO check if this is correct...
        } catch (URISyntaxException e) {
            return false;
        } catch (RegistryException e) {
            logger.debug("failed to find resource in registry "+id);
            return false;
        }
    }

    @SuppressWarnings("deprecation")// appears that getIdentity should not be deprecated
    public RegistryInfo getRegistry() {
       try {
        Document regdom = this.getClient().getIdentity();
        Registry regjaxb = CEAJAXBUtils.unmarshall(regdom, Registry.class);
        return new RegistryInfo(regjaxb);
        
    } catch (Exception e) {
        logger.error("problem getting registry information", e);
        return null;
    }
    }

}


/*
 * $Log: RegistryQueryLocatorImpl.java,v $
 * Revision 1.2  2011/09/02 21:55:52  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2011/09/02 19:39:00  pah
 * not finding something in registry should not result in stack trace!
 *
 * Revision 1.1  2009/06/10 12:40:21  pah
 * ASSIGNED - bug 2934: Improve registration page
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2934
 *
 */
