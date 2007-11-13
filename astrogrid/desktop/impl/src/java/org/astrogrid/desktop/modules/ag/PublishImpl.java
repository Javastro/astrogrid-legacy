/*
 * $Id: PublishImpl.java,v 1.1 2007/11/13 16:50:51 pah Exp $
 * 
 * Created on 12 Nov 2007 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2007 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.desktop.modules.ag;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Publish;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.admin.UpdateRegistry;
import org.w3c.dom.Document;

public class PublishImpl implements Publish {

    private Registry reg;

    public PublishImpl(Registry reg) {
	this.reg = reg;
    }

    public void register(URI arg0, Document arg1)
	    throws InvalidArgumentException, ServiceException,
	    NotFoundException {

	try {
	    Resource regu = reg.getResource(arg0);
	    if (regu instanceof RegistryService) {
		RegistryService regserv = (RegistryService) regu;
		Interface[] intf = regserv.findSearchCapability()
			.getInterfaces();
		if (intf.length > 0) {
		    URL url = intf[0].getAccessUrls()[0].getValueURI().toURL();
		    UpdateRegistry upreg = new UpdateRegistry(url);
		    upreg.update(arg1);
		} else {
		    throw new ServiceException("registry not queriable");
		}
	    } else {
		throw new InvalidArgumentException(arg0 + " is not a registry");
	    }
	} catch (MalformedURLException e) {
	    throw new InvalidArgumentException("error in registry location", e);

	} catch (RegistryException e) {
	    throw new ServiceException("error updating registry", e);
	}
    }

}

/*
 * $Log: PublishImpl.java,v $
 * Revision 1.1  2007/11/13 16:50:51  pah
 * added first implmentation at publishing code - think it needs v1.0 registry though.
 *
 */
