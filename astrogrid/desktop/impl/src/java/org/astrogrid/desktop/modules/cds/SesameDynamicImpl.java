/*$Id: SesameDynamicImpl.java,v 1.3 2006/06/15 09:46:47 nw Exp $
 * Created on 28-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.cds;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;

import org.apache.axis.client.Service;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
/**
 * implementaiton of the sesame client, using dynamic webservice invocation - i.e.
 * without using marshalling beans
 * 
 * nice simple xml.rpc style interface - so can call through rpc api - next to no messing with soap.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Feb-2006
 *
 */
public class SesameDynamicImpl implements Sesame {

    public SesameDynamicImpl() {
        super();
        service = new Service();
    }
    protected final Service service;
    protected  String endpoint = DEFAULT_ENDPOINT;
    public final static String DEFAULT_ENDPOINT =  "http://cdsws.u-strasbg.fr/axis/services/Sesame";

    public void setEndpoint(String e) {
        this.endpoint = e;
    }
    
    public String sesame(String arg0, String arg1) throws ServiceException {
    try {
        Call call = (Call)service.createCall();
        call.setTargetEndpointAddress(endpoint);
        call.setOperationName(new QName("urn:Sesame","sesame"));
        return (String)call.invoke(new Object[]{arg0,arg1});
    } catch (javax.xml.rpc.ServiceException e) {
        throw new ServiceException(e);
    } catch (RemoteException e) {
        throw new ServiceException(e);
    }
    }

    public String sesameChooseService(String arg0, String arg1, boolean arg2, String arg3)
            throws ServiceException {
        try {
            Call call = (Call)service.createCall();
            call.setTargetEndpointAddress(endpoint);
            call.setOperationName(new QName("urn:Sesame","sesame"));
            return (String)call.invoke(new Object[]{arg0,arg1,new Boolean(arg2),arg3});
        } catch (javax.xml.rpc.ServiceException e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }        
    }
  
}


/* 
$Log: SesameDynamicImpl.java,v $
Revision 1.3  2006/06/15 09:46:47  nw
removed cruft, added schema for result vaidation.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development
 
*/