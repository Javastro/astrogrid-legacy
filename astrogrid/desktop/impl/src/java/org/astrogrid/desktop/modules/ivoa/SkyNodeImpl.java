/*$Id: SkyNodeImpl.java,v 1.1 2006/02/24 16:28:11 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.astrogrid.acr.ivoa.AvailabilityBean;
import org.astrogrid.acr.ivoa.FunctionBean;
import org.astrogrid.acr.ivoa.SkyNode;

import org.w3c.dom.Document;

import java.net.URI;

/** implementation of the skynode interface */
public class SkyNodeImpl implements SkyNode {

    public SkyNodeImpl() {
        super();
    }

    public String getRegistryQuery() {
        return "select * from Registry where @xsi:type like '%OpenSkyNode' ";
    }

    public TabularDatabaseInformation getMetadata(URI arg0) throws InvalidArgumentException, NotFoundException,
            ServiceException {
        return null;
    }

    public String[] getFormats(URI arg0) throws InvalidArgumentException, NotFoundException,
            ServiceException {
        return null;
    }

    public FunctionBean[] getFunctions(URI arg0) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        return null;
    }

    public Document getResults(URI arg0, Document arg1) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        return null;
    }

    public void saveResults(URI arg0, Document arg1, URI arg2) throws InvalidArgumentException,
            NotFoundException, ServiceException, SecurityException {
    }

    public String getResultsF(URI arg0, Document arg1, String arg2)
            throws InvalidArgumentException, NotFoundException, ServiceException {
        return null;
    }

    public void saveResultsF(URI arg0, Document arg1, URI arg2, String arg3)
            throws InvalidArgumentException, NotFoundException, ServiceException, SecurityException {
    }

    public Document getFootprint(URI arg0, Document arg1) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        return null;
    }

    public float estimateQueryCost(long arg0, Document arg1) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        return 0;
    }

    public AvailabilityBean getAvailability(URI arg0) throws InvalidArgumentException,
            NotFoundException, ServiceException {
        return null;
    }

}


/* 
$Log: SkyNodeImpl.java,v $
Revision 1.1  2006/02/24 16:28:11  nw
startings of an immplementation of skynode. not operational yet.
 
*/