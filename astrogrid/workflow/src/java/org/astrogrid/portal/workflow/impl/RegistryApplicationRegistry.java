/*$Id: RegistryApplicationRegistry.java,v 1.3 2004/04/05 15:14:59 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.beans.resource.ResourceType;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/** Implementation of ApplicationRegistry that resolves lookups using an astrogrid registry
 * <p>
 * @todo hacked at the moment to use the DOM-based queries. later use castor-object model ones.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class RegistryApplicationRegistry implements ApplicationRegistry {
    private static final Log logger = LogFactory.getLog(RegistryApplicationRegistry.class);

   /** construct a new RegistryApplicationRegistry
    * let registry-delegate self-configure */
   public RegistryApplicationRegistry() {
     logger.info("Creating an astrogrid-backed application registry");
     logger.info("Letting delegate determine own endpoint");
     service = RegistryDelegateFactory.createQuery();
     assert service != null;
   }

    /** Construct a new RegistryApplicationRegistry
     * @param endpoint endpoint for the astrogrid registry web service
     */
    public RegistryApplicationRegistry(URL endpoint) {
        logger.info("Creating an astrogrid-backed application registry");
        service  = RegistryDelegateFactory.createQuery(endpoint);
        assert service != null;
    }
    protected final RegistryService service;
    /** string query to to pass to registry to get list of tools back 
     */
    public final static String LIST_QUERY_STRING= "<query><selectionSequence>" +
    "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
    "<selectionOp op='$and$'/>" +
    "<selection item='@*:type' itemOp='EQ' value='CeaApplicationType'/>"  +
    /* don't think that we need these....
    "<selectionOp op='OR'/>" +
    "<selection item='@*:type' itemOp='EQ' value='CeaServiceType'/>"  +
    */
    "</selectionSequence></query>";

    
    
    public String[] listApplications() throws WorkflowInterfaceException {
        try {           
            Document doc = service.submitQueryStringDOM(LIST_QUERY_STRING);
            assert doc != null;
            NodeList nl = doc.getElementsByTagNameNS("*","Identifier");
            
            String[] names = new String[nl.getLength()];
            for (int i = 0; i < nl.getLength(); i++) {
                IdentifierType it = (IdentifierType)Unmarshaller.unmarshal(IdentifierType.class,nl.item(i));
                names[i] = it.getAuthorityID() + "/" + it.getResourceKey();
            }
            return names;
        } catch (CastorException e) {
            logger.error("list applications failed - castor failed to parse result",e);
            throw new WorkflowInterfaceException(e);                          
        } catch (RegistryException e) {
            logger.error("listApplications Failed with exception from registry",e);
            throw new WorkflowInterfaceException(e);
        }
    }
    
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     */
    public ApplicationDescription getDescriptionFor(String applicationName) throws WorkflowInterfaceException {
        try {
            Document doc = service.getResourceByIdentifierDOM(applicationName);
            assert doc != null;
            // navigate down to the bit we're interested in.
            NodeList nl = doc.getElementsByTagNameNS("*","ApplicationDefinition");
            if (nl.getLength() == 0) {                
                throw new WorkflowInterfaceException("Registry entry for '" + applicationName + "' has no ApplicationDefinition Element");
            }
            Element n = (Element)nl.item(0);
          
            n.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); // bug-fix work around.
            ApplicationDefinition def = (ApplicationDefinition) Unmarshaller.unmarshal(ApplicationDefinition.class,n);
            // now mangle across to the required types.
            ApplicationBase appBase = new ApplicationBase();
            appBase.setName(applicationName);
            appBase.setInterfaces(def.getInterfaces());
            BaseParameterDefinition[] paramdef = def.getParameters().getParameterDefinition();
            Parameters params = new Parameters();
           appBase.setParameters(params);
           params.setParameter(paramdef);
            return new ApplicationDescription(appBase);   
        } catch (CastorException e) {
            logger.error("getDescriptionFor " + applicationName + " - castor failed to parse result",e);
            throw new WorkflowInterfaceException(e);         
        } catch (RegistryException e) {
            logger.error("getDescriptionFor " + applicationName + " - exception from registry",e);
            throw new WorkflowInterfaceException(e);
        }
    }
    
}


/* 
$Log: RegistryApplicationRegistry.java,v $
Revision 1.3  2004/04/05 15:14:59  nw
implemented

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/