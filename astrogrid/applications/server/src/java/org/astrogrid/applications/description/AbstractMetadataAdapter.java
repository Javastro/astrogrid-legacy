/*
 * $Id: AbstractMetadataAdapter.java,v 1.2 2008/09/03 14:18:43 pah Exp $
 * 
 * Created on 27 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.contracts.Namespaces;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.ivoa.resource.Resource;

public abstract class AbstractMetadataAdapter implements MetadataAdapter {

    protected final Resource resource;

    public AbstractMetadataAdapter(Resource resource) {
	this.resource = resource;

    }

    public Resource getResource() {
	return resource;
    }


}

/*
 * $Log: AbstractMetadataAdapter.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:26  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
