/*$Id: CeaHelper.java,v 1.8 2007/07/26 18:21:45 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

/** helper object for working with cea services.
 * <P>
 * tries to hid difference between local and remote cea apps.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 20-Oct-2005
 */
public class CeaHelper {

    /** Construct a new RemoteCeaHelper
     * 
     */
    public CeaHelper(Registry reg, CommunityInternal community) {
        super();
        this.reg = reg;
        this.community = community;
    }
    private final CommunityInternal community;
    private final Registry reg;


    private boolean hasDigitalSignatureCredentials() {
        try {
          SecurityGuard g = this.community.getSecurityGuard();
          g.getCertificateChain(); // throws if credentials are absent
          g.getPrivateKey(); // throws if credentials are absent
          return true;
        }
        catch (Exception e) {
          return false;
        }
      }
    
    

/** create a delegate to connect tot he server described in a resource information
 * delegate will be authenticated if the user is logged in.
 * @throws CEADelegateException 
 * @throws IllegalArgumentException if resource information does not prodvdide an access url */
    public CommonExecutionConnectorClient createCEADelegate(CeaService server) throws CEADelegateException {
		if (server == null 
				|| server.getCapabilities().length == 0 
				|| server.getCapabilities()[0].getInterfaces().length == 0
				|| server.getCapabilities()[0].getInterfaces()[0].getAccessUrls().length == 0) { //@todo need to check this still works in reg v1.0
    		throw new IllegalArgumentException("invalid resource information: " + server);
    	}
		final URL endpoint = server.getCapabilities()[0].getInterfaces()[0].getAccessUrls()[0].getValue();
        CommonExecutionConnectorClient del = DelegateFactory.createDelegate(endpoint.toString());
        if (community.isLoggedIn() && hasDigitalSignatureCredentials()) {
            SecurityGuard guard = this.community.getSecurityGuard();
            del.setCredentials(guard);
        }
        return del;
     }
    
    /** create a delegate to connect to a server descried in an exec Id 
     * @throws URISyntaxException
     * @throws NotApplicableException
     * @throws ServiceException
     * @throws NotFoundException
     * @throws CEADelegateException */
public CommonExecutionConnectorClient createCEADelegate(URI executionId) throws NotFoundException, ServiceException, CEADelegateException {
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
     * @return the application id portion of this execution id
     */
    public String getAppId(URI executionId) {
        return executionId.getFragment();
    }



    /** returns true if this exec Id is local
     * @param executionId
     * @return true if this execution id is local.
     */
    public boolean isLocal(URI executionId) {
        return executionId.getScheme().equals("local");
    }
    
    /** parse a document into a tool, performing any necessary adjustments */
	public Tool parseTool(Document doc) throws InvalidArgumentException{
		try {
		Tool tool = (Tool)Unmarshaller.unmarshal(Tool.class, doc);
		// munge name in document, if incorrect..       
		// The application name is supposed to be an IVOID without the
		// ivo:// prefix. Strip the prefix if it is present.
		if (tool.getName().startsWith("ivo://")) {
			tool.setName(tool.getName().substring(6));
		}
		return tool;
    	} catch (MarshalException e) {
    		throw new InvalidArgumentException(e);
    	} catch (ValidationException e) {
    		throw new InvalidArgumentException(e);
    	}
	}

}


/* 
$Log: CeaHelper.java,v $
Revision 1.8  2007/07/26 18:21:45  nw
merged mark's and noel's branches

Revision 1.7.2.1  2007/07/26 13:40:29  nw
Complete - task 96: get authenticated access working

Revision 1.7  2007/07/13 23:14:54  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.6  2007/06/18 16:25:34  nw
javadoc

Revision 1.5  2007/01/29 11:11:35  nw
updated contact details.

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