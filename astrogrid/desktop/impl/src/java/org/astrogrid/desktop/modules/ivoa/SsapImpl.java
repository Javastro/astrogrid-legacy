/*$Id: SsapImpl.java,v 1.3 2006/04/21 13:48:11 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

/** implementation of a component that does ssap queries.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 * @todo - at the moment, just a cut-n-paste of the siap code - nee
 */
public class SsapImpl extends DALImpl implements Ssap {

    /** Construct a new SsapImpl
     * @param reg
     * @param ms
     */
    public SsapImpl(Registry reg, MyspaceInternal ms) {
        super(reg, ms);
    }

    /**
     * @see org.astrogrid.acr.ivoa.Ssap#getRegistryQuery()
     */
    public String getRegistryQuery() {
        return "Select * from Registry where " +
        " @xsi:type like '%SimpleSpectrumAccess'  " ;
        //@todo        " and (not (@status = 'inactive' or @status='deleted') )";
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
 
    public URL constructQuery(URI service, double ra, double dec, double size)
            throws InvalidArgumentException, NotFoundException {
        return addOption(constructQueryPrim(service,ra,dec),"SIZE",Double.toString(size));
    }

    public URL constructQueryS(URI service, double ra, double dec, double ra_size, double dec_size)
            throws InvalidArgumentException, NotFoundException {        
        if (ra_size == dec_size) {
            return constructQuery(service,ra,dec,ra_size);
        } else {
            String sizeStr = Double.toString(ra_size) + "," + Double.toString(dec_size);
            return addOption(constructQueryPrim(service,ra,dec),"SIZE",sizeStr);
        }
    }    
    
}


/* 
$Log: SsapImpl.java,v $
Revision 1.3  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:32  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2006/02/02 14:49:49  nw
added starter implementation of ssap.
 
*/