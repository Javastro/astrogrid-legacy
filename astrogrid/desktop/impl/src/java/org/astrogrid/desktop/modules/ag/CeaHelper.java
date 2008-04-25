/*$Id: CeaHelper.java,v 1.15 2008/04/25 08:57:50 nw Exp $
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

import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

/** helper object for working with cea services.
 * <P>
 * tries to hid difference between local and remote cea apps.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 20-Oct-2005
 */
public class CeaHelper {
  
    public static final Log log = LogFactory.getLog(CeaHelper.class);

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
    
    

/** create a delegate to connect to the server described in a resource information
 * delegate will be authenticated if the user is logged in.
 * @throws CEADelegateException 
 * @throws IllegalArgumentException if resource information does not prodvdide an access url */
    public CommonExecutionConnectorClient createCEADelegate(CeaService server) throws CEADelegateException {
		if (server == null || server.getCapabilities().length == 0) { 
    		throw new IllegalArgumentException("Error: invalid resource information, couldn't find any service capabilities: " + server);
    	}
      URL endpoint = null;
      Capability[] caps = server.getCapabilities();   
      for (int i = 0; i < caps.length; i++) {
         URI standardID = caps[i].getStandardID(); 
         if (standardID != null && StandardIds.CEA_1_0.equals(standardID.toString())) {
            // We've found a CEA capability
            Interface[] ints = caps[i].getInterfaces();
            if (ints.length == 0) {
               throw new IllegalArgumentException("Error: invalid resource information, no service interfaces for CEA capability: " + server);
            }
            for (int j = 0; j < ints.length; j++) {
               // Got some interfaces - let's use the first available 
               // access URL 
               // @todo What about proper round-robin/load-balanced use 
               // of access endpoints?
               AccessURL[] urls = ints[j].getAccessUrls();
               // Just ignore interfaces with no access urls, for now
               // @todo - is this the best thing to do?
               if (urls.length > 0) {
                  endpoint = urls[0].getValue();
                  break;   // Can stop inner loop on URLs now
               }
            }
            // Inner break out to here
            if (endpoint != null) {
               break;   // Can stop outer loop on interfaces now
            }
         }
      }
      // If we get here with no endpoint set, the registrations are invalid
      if (endpoint == null) {
         throw new IllegalArgumentException("Error: Couldn't find CEA service endpoint for server in resource information: " + server);
      }
      // Now go ahead and make the delegate for the endpoint
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

  /**
   * Creates a deep copy of the tool converting identifiers for MySpace locations to the concrete form.
   * Abstract form is the account IVORN with the MySpace path added
   * as a URI fragment. Concrete form is the IVORN of the services hosting
   * the space with the MySpace path added as a URI fragment.
   */
public Tool makeMySpaceIvornsConcrete(Tool intool) throws InvalidArgumentException {
	    Tool tool = null;
	    try {
		Node node = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Marshaller.marshal(intool, node);
		tool = (Tool)Unmarshaller.unmarshal(Tool.class, node);
		Input input = tool.getInput();
		for (int i = 0; i < input.getParameterCount(); i++) {
		    ParameterValue p = input.getParameter(i);
		    makeMySpaceIvornsConcrete(p);
		}

		Output output = tool.getOutput();
		for (int i = 0; i < output.getParameterCount(); i++) {
		    ParameterValue p = output.getParameter(i);
		    makeMySpaceIvornsConcrete(p);
		}
	    } catch (Exception ex) {
		throw new InvalidArgumentException("Failed to make VOSpace references concrete", ex);
	    }
	    return tool;
	}
  
  /**
   * Makes the IVORN of an indirect parameter concrete.
   * If the given parameter is indirect, its value may be
   * an IVORN denoting a location in MySpace. If so, then
   * the IVORN may be either concrete (based on the IVORN
   * for the service hosting the space) or abstract (based
   * on the name of the account owning the space. This method
   * detects indirect parameters with IVORN values and changes
   * the values to be concrete IVORNs; this involves a transaction
   * with registry and one with the community service.
   *
   * @throws URISyntaxException If the indirect-parameter value is not a valid URI.
   * @throws CommunityServiceException If the community service fails to satisfy an information request.
   * @throws CommunityIdentifierException If the parameter value is in scheme ivo:// but is invalid.
   * @throws CommunityResolverException If the client-side resolver-library cannot parse the IVORN.
   * @throws RegistryException If the community indicated in the IVORN cannnot be found in the registry.
   */
  protected void makeMySpaceIvornsConcrete(ParameterValue p) 
      throws URISyntaxException, 
             CommunityServiceException, 
             CommunityIdentifierException, 
             CommunityPolicyException, 
             CommunityResolverException, 
             RegistryException {
    if (p.getIndirect()) {
      String value = p.getValue();
      if (value != null && value.startsWith("ivo://")) {
          CommunityAccountSpaceResolver resolver = new CommunityAccountSpaceResolver();
        Ivorn ivorn1 = new Ivorn(value);
        Ivorn ivorn2 = resolver.resolve(ivorn1);
        p.setValue(ivorn2.toString());
        log.info(ivorn1 + " was resolved to " + ivorn2);
      }
    }
  }
  
}
