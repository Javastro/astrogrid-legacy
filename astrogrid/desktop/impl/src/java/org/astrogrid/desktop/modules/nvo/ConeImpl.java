/*$Id: ConeImpl.java,v 1.1 2005/10/17 16:02:44 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ivoa.DALImpl;
import org.astrogrid.io.Piper;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;

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
        return "Select * from Registry where " +
        " @xsi:type like '%ConeSearch'  " +
        " and @status = 'active'";
    }

}


/* 
$Log: ConeImpl.java,v $
Revision 1.1  2005/10/17 16:02:44  nw
added siap and cone interfaces
 
*/