/*$Id: UcdResolverDelegate.java,v 1.1 2003/11/18 11:23:49 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate.ucdresolver;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

/** Delegate that wraps the wsdl-geneated client stub.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class UcdResolverDelegate {

    /**
     * 
     */
    public UcdResolverDelegate() throws ServiceException {
        SesameService serv = new SesameServiceLocator();
        res = serv.getUCDResolver();
    }
    
    public UcdResolverDelegate(String endpoint) throws MalformedURLException, ServiceException {
        this(new URL(endpoint));
    }
    public UcdResolverDelegate(URL endpoint) throws ServiceException {
        SesameService serv = new SesameServiceLocator();
        res = serv.getUCDResolver(endpoint);
    }
    
    protected UCDResolver res;
    
    /**
     * Resolve a ucd to a string
     * @param ucd the usc to resolve
     * @return description of the ucd
     * @throws InvalidUcdException if the UCD is not known.
     */
    public String resolve(String ucd) throws InvalidUcdException, RemoteException {
        String result = res.UCDResolver(ucd);
        if (result == null || result.startsWith("Invalid UCD:")) {
            throw new InvalidUcdException();
        }
        return result;
    }


}


/* 
$Log: UcdResolverDelegate.java,v $
Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/