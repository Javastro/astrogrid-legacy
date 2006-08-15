/*$Id: CeaHelper.java,v 1.4 2006/08/15 10:15:59 nw Exp $
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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;

/** helper object for working with cea services.
 * <P>
 * tries to hid difference between local and remote cea apps.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Oct-2005
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



/** create a delegate to connect tot he server described in a resource information
 * @throws IllegalArgumentException if resource information does not prodvdide an access url */
    public CommonExecutionConnectorClient createCEADelegate(CeaService server) {
		if (server == null 
				|| server.getCapabilities().length == 0 
				|| server.getCapabilities()[0].getInterfaces().length == 0
				|| server.getCapabilities()[0].getInterfaces()[0].getAccessUrls().length == 0) { //@todo need to check this still works in reg v1.0
    		throw new IllegalArgumentException("invalid resource information: " + server);
    	}
		final URL endpoint = server.getCapabilities()[0].getInterfaces()[0].getAccessUrls()[0].getValue();
        return  DelegateFactory.createDelegate(endpoint.toString());       
     }
    
    /** create a delegate to connect to a server descried in an exec Id 
     * @throws URISyntaxException
     * @throws NotApplicableException
     * @throws ServiceException
     * @throws NotFoundException*/
public CommonExecutionConnectorClient createCEADelegate(URI executionId) throws NotFoundException, ServiceException {
    try {    
    final URI ivorn = new URI(executionId.getScheme(),executionId.getSchemeSpecificPart(),null);
    Resource r = reg.getResource(ivorn);
    if (! (r instanceof CeaService)) {
    	throw new ServiceException(ivorn.toString() + " is not a cea server");
    }
    return createCEADelegate((CeaService)r);
    } catch (URISyntaxException e) {
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
    public  URI mkRemoteTaskURI(String ceaid, CeaService server) throws ServiceException {
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
Revision 1.4  2006/08/15 10:15:59  nw
migrated from old to new registry models.

Revision 1.3  2006/06/15 09:45:04  nw
improvements coming from unit testing

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.42.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/