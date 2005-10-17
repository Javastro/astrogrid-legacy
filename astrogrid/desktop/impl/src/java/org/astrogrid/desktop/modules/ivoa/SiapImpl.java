/*$Id: SiapImpl.java,v 1.1 2005/10/17 16:02:45 nw Exp $
 * Created on 17-Oct-2005
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
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/** Implementaiton of a component that does siap queries.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class SiapImpl extends DALImpl implements Siap {

    /** Construct a new SiapImpl
     * 
     */
    public SiapImpl(Registry reg, MyspaceInternal ms) {
        super(reg,ms);
        
    }

    private URL constructQueryPrim(URI arg0, double ra,double dec) throws InvalidArgumentException, NotFoundException {
        URL endpoint = null;
        if (arg0.getScheme().equals("http")) {
            try {
                endpoint = arg0.toURL();
            } catch (MalformedURLException e) {
                throw new InvalidArgumentException(e);
            } 
        } else if (arg0.getScheme().equals("ivo")) {
            try {
                endpoint = reg.resolveIdentifier(arg0);
            } catch (ServiceException e) {
                throw new NotFoundException(e);
            } catch (NotApplicableException e) {
                throw new NotFoundException(e);
            }
    } else {
        throw new InvalidArgumentException("Don't know what to do with this: " + arg0);
        }
        // ok. one way or another, we've got an endpoint. now add parameters onto it.
        String url = endpoint.toString();
        StringBuffer urlSB = new StringBuffer(url);
        // add a query part, if not already there
        char lastch = url.charAt(url.length() - 1);
        if (lastch != '?' && lastch != '&')
            urlSB.append((url.indexOf('?') > 0) ? '&' : '?');
        urlSB.append("POS=").append(Double.toString(ra)).append(",").append(Double.toString(dec));  
        try {
            return new URL(urlSB.toString());
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException("Failed to construct query URL",e);            
        }        
    }
    
    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI service, double ra, double dec, double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,ra,dec),"SIZE",Double.toString(size));
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryF(java.net.URI, double, double, double, java.lang.String)
     */
    public URL constructQueryF(URI service, double ra, double dec, double size, String format)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQuery(service,ra,dec,size),"FORMAT",format);
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQueryS(java.net.URI, double, double, double, double)
     */
    public URL constructQueryS(URI service, double ra, double dec, double ra_size, double dec_size)
            throws InvalidArgumentException, NotFoundException {        
        if (ra_size == dec_size) {
            return constructQuery(service,ra,dec,ra_size);
        } else {
            String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(constructQueryPrim(service,ra,dec),"SIZE",sizeStr);
        }
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#constructQuerySF(java.net.URI, double, double, double, double, java.lang.String)
     */
    public URL constructQuerySF(URI service, double ra, double dec, double ra_size, double dec_size, String format)
            throws InvalidArgumentException, NotFoundException {
            return addOption(constructQueryS(service,ra,dec,ra_size,dec_size),"FORMAT",format);        
    }

    /**
     * @see org.astrogrid.acr.ivoa.Siap#getRegistryQueryToListSiap()
     */
    public String getRegistryQuery() {
        return "Select * from Registry where " +
        " @xsi:type like '%SimpleImageAccess'  " +
        " and @status = 'active'";
    }

}


/* 
$Log: SiapImpl.java,v $
Revision 1.1  2005/10/17 16:02:45  nw
added siap and cone interfaces
 
*/