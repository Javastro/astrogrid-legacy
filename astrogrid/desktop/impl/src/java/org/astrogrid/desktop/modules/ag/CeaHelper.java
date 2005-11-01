/*$Id: CeaHelper.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 20-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/** helper object for working with cea services.
 * <P>
 * tries to hid difference between local and remote cea apps.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Oct-2005
 *
 */
public class CeaHelper {

    /** Construct a new RemoteCeaHelper
     * 
     */
    public CeaHelper(Registry reg) {
        super();
        this.reg = reg;
    }
    private final Registry reg;



/** create a delegate to connect tot he server described in a resource information */
    public CommonExecutionConnectorClient createCEADelegate(ResourceInformation server) {
        return  DelegateFactory.createDelegate(server.getAccessURL().toString());       
     }
    
    /** create a delegate to connect to a server descried in an exec Id 
     * @throws URISyntaxException
     * @throws NotApplicableException
     * @throws ServiceException
     * @throws NotFoundException*/
public CommonExecutionConnectorClient createCEADelegate(URI executionId) throws NotFoundException, ServiceException {
    try {    
    URL url = reg.resolveIdentifier(
                    new URI(executionId.getScheme(),executionId.getSchemeSpecificPart(),null)
                    );
        return DelegateFactory.createDelegate(url.toString());
    } catch (URISyntaxException e) {
        throw new ServiceException(e);
    } catch (NotApplicableException e) {
        throw new ServiceException(e);
    }
  }


    /** create an exec Id from a app id from the local inprocess server */
    public  URI mkLocalTaskURI(String ceaid) throws ServiceException {
        try {
            return new URI("local","//",ceaid);
        } catch (URISyntaxException e) {
            throw new ServiceException("Unexpected",e);
        }
    }
    /** create an exec Id from an appId from a remote server */
    public  URI mkRemoteTaskURI(String ceaid, ResourceInformation server) throws ServiceException {
        try {
            return new URI(server.getId().getScheme(),server.getId().getSchemeSpecificPart(),ceaid);
        } catch (URISyntaxException e) {
            throw new ServiceException("Unexpected",e);
        }
    }

    /** extract the appId from an execId 
     * @param executionId
     * @return
     */
    public String getAppId(URI executionId) {
        return executionId.getFragment();
    }



    /** returns true if this exec Id is local
     * @param executionId
     * @return
     */
    public boolean isLocal(URI executionId) {
        return executionId.getScheme().equals("local");
    }
    

}


/* 
$Log: CeaHelper.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/