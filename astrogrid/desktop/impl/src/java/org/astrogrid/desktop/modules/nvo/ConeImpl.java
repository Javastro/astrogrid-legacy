/*$Id: ConeImpl.java,v 1.3 2006/04/21 13:48:12 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.nvo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ivoa.DALImpl;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public class ConeImpl extends DALImpl implements Cone {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ConeImpl.class);

    /** Construct a new ConeImpl
     * 
     */
    public ConeImpl(Registry reg, MyspaceInternal ms) {
        super(reg,ms);
    }

    /**
     * @see org.astrogrid.acr.nvo.Cone#constructQuery(java.net.URI, double, double, double)
     */
    public URL constructQuery(URI arg0, double arg1, double arg2, double arg3)
            throws InvalidArgumentException, NotFoundException {
        URL endpoint = null;
        if (arg0.getScheme().equals("http")) {
            try {
                endpoint = arg0.toURL();
            } catch (MalformedURLException e) {
                throw new InvalidArgumentException(e);
            }
        } else if (arg0.getScheme().equals("ivo")) {
                try {
                    //endpoint = reg.resolveIdentifier(arg0); NWW - gets incorrect endpoint sometimes.
                    endpoint = reg.getResourceInformation(arg0).getAccessURL();
                } catch (ServiceException e) {
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
        urlSB.append("RA=").append(Double.toString(arg1))
            .append("&DEC=").append(Double.toString(arg2))
            .append("&SR=").append(Double.toString(arg3));
        try {
            return new URL(urlSB.toString());
        } catch (MalformedURLException e) {
            throw new InvalidArgumentException("Failed to construct query URL",e);            
        }
       
    }


    /* 
     * @see org.astrogrid.acr.nvo.Cone#getRegistryQueryToListCone()
     */
    public String getRegistryQuery() {
        return "Select * from Registry where ( " +
        " @xsi:type like '%ConeSearch'  " +
        " or ( @xsi:type like '%TabularSkyService' " + 
		" and vr:identifier like 'ivo://CDS/%' " + 
		" and vs:table/vs:column/vs:ucd = 'POS_EQ_RA_MAIN'  ) " + 
        " )  ";
        //@todo and (not ( @status = 'inactive' or @status='deleted') )";
    }

}


/* 
$Log: ConeImpl.java,v $
Revision 1.3  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.2  2006/03/13 18:29:17  nw
fixed queries to not restrict to @status='active'

Revision 1.1  2005/10/17 16:02:44  nw
added siap and cone interfaces
 
*/