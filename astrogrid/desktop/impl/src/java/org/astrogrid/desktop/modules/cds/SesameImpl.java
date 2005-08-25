/*$Id: SesameImpl.java,v 1.1 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.desktop.modules.cds.sesame.SesameService;
import org.astrogrid.desktop.modules.cds.sesame.SesameServiceLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.rmi.RemoteException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2005
 *
 */
public class SesameImpl implements Sesame {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SesameImpl.class);

    /** Construct a new SesameImpl
     * @throws javax.xml.rpc.ServiceException
     * 
     */
    public SesameImpl(Glu glu) throws javax.xml.rpc.ServiceException {
        SesameService serv = new SesameServiceLocator();
        org.astrogrid.desktop.modules.cds.sesame.Sesame ses1 = null;
        try {
            String endpoint = glu.getURLfromTag(SESAME_WS_GLU_TAG);
            ses1 = serv.getSesame(new URL(endpoint));
        } catch (Exception e) {
            logger.warn("Exception when finding endpoint via glu - falling back",e);
            ses1 = serv.getSesame();
        }
        ses = ses1;
    }
    protected final org.astrogrid.desktop.modules.cds.sesame.Sesame ses;
    public static final String SESAME_WS_GLU_TAG = "fill me in" ; //@todo


    public String sesame(String arg0, String arg1) throws ServiceException {
        try {
            return ses.sesame(arg0,arg1);
        }catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }



    public String sesameChooseService(String arg0, String arg1, boolean arg2, String arg3)
            throws ServiceException {
        try {
            return ses.sesame(arg0,arg1,arg2,arg3);
        }catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }

  

}


/* 
$Log: SesameImpl.java,v $
Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/