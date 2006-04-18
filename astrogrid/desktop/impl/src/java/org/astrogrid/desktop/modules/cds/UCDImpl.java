/*$Id: UCDImpl.java,v 1.4 2006/04/18 23:25:44 nw Exp $
 * Created on 16-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.cds;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.desktop.modules.cds.ucd.UCDService;
import org.astrogrid.desktop.modules.cds.ucd.UCDServiceLocator;

/** Implementaton of the UCD service
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2005
 *
 */
public class UCDImpl implements UCD {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UCDImpl.class);

    /** Construct a new UCDImpl
     * @throws MalformedURLException 
     * 
     */
    public UCDImpl(Glu glu, String endpoint) throws javax.xml.rpc.ServiceException, MalformedURLException  {
        super();
        UCDService serv = new UCDServiceLocator();
        org.astrogrid.desktop.modules.cds.ucd.UCD ucd1 = null;
        if (endpoint == null || endpoint.trim().length() == 0) {
        try {
            String gluEndpoint = glu.getURLfromTag(UCD_WS_GLU_TAG);
            ucd1 = serv.getUCD(new URL(gluEndpoint));
        } catch (Exception e) {
            logger.warn("Exception when finding endpoint via glu - falling back",e);
            ucd1 = serv.getUCD();
        }
        } else {
            ucd1 = serv.getUCD(new URL(endpoint));
        }
        ucd = ucd1;
    
    } 
    protected final org.astrogrid.desktop.modules.cds.ucd.UCD ucd;
    public static final String UCD_WS_GLU_TAG = "CDS/ws/UCD.WS";
    /**
     * @see org.astrogrid.acr.cds.UCD#UCDList()
     */
    public String UCDList() throws ServiceException {
        try {
            String s= ucd.UCDList();
            return s;
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#resolveUCD(java.lang.String)
     */
    public String resolveUCD(String arg0) throws ServiceException {
        try {
            return ucd.resolveUCD(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#UCDofCatalog(java.lang.String)
     */
    public String UCDofCatalog(String arg0) throws ServiceException {
        try {
            return ucd.UCDofCatalog(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#translate(java.lang.String)
     */
    public String translate(String arg0) throws ServiceException {
        try {
            return ucd.translate(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#upgrade(java.lang.String)
     */
    public String upgrade(String arg0) throws ServiceException {
        try {
            return ucd.upgrade(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#validate(java.lang.String)
     */
    public String validate(String arg0) throws ServiceException {
        try {
            return ucd.validate(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#explain(java.lang.String)
     */
    public String explain(String arg0) throws ServiceException {
        try {
            return ucd.explain(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.cds.UCD#assign(java.lang.String)
     */
    public String assign(String arg0) throws ServiceException {
        try {
            return ucd.assign(arg0);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

}


/* 
$Log: UCDImpl.java,v $
Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.60.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/31 15:30:46  nw
added glu tags.

Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/