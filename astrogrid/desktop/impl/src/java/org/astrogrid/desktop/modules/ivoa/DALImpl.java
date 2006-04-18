/*$Id: DALImpl.java,v 1.2 2006/04/18 23:25:45 nw Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.io.Piper;
import org.w3c.dom.Document;

/** Abstract class for implemntations of HTTP-GET based DAL standards
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 *
 */
public abstract class DALImpl {
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(DALImpl.class);

    /** Construct a new DALImpl
     * 
     */
    public DALImpl(Registry reg, MyspaceInternal ms) {
        super();
        this.reg = reg;
        this.ms = ms;

    }
    protected final Registry reg;
    protected final MyspaceInternal ms;


    /**
     * @see org.astrogrid.acr.nvo.Cone#addOption(java.net.URL, java.lang.String, java.lang.String)
     */
    public URL addOption(URL arg0, String arg1, String arg2) throws InvalidArgumentException{
        StringBuffer urlSB = new StringBuffer(arg0.toString());
        try {
            String encoded = URLEncoder.encode(arg2,"UTF-8");
            urlSB.append("&").append(arg1).append("=").append(encoded);
            return new URL(urlSB.toString());
        } catch (IOException e) {
            throw new InvalidArgumentException(e);
        } 
    }
    

    /**
     * @see org.astrogrid.acr.nvo.Cone#getResults(java.net.URL)
     */
    public Document getResults(URL arg0) throws ServiceException {
        try {
            return XMLUtils.newDocument(arg0.toString());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @throws InvalidArgumentException
     * @throws ServiceException
     * @throws SecurityException
     * @see org.astrogrid.acr.nvo.Cone#saveResults(java.net.URL, java.net.URI)
     */
    public void saveResults(URL arg0, URI arg1) throws InvalidArgumentException, ServiceException, SecurityException {
        if (arg1.getScheme().equals("ivo")) { // save to myspace - can optimize this
            try {
                ms.copyURLToContent(arg0,arg1);
            } catch (NotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (NotApplicableException e) {
                throw new InvalidArgumentException(e);
            }
        } else {
            OutputStream os = null;
            try {
                os = ms.getOutputStream(arg1);
                InputStream is = arg0.openStream();
                Piper.pipe(is,os);
            } catch (IOException e) {
                throw new ServiceException(e);
            } catch (NotFoundException e) {
                throw new InvalidArgumentException(e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Exception e) {
                        logger.warn("Exception closing output stream",e);
                    }
                }
            }
        }
    }


}


/* 
$Log: DALImpl.java,v $
Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.48.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2005/10/17 16:02:45  nw
added siap and cone interfaces
 
*/