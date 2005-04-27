/*$Id: RegistryV10ApplicationRegistry.java,v 1.2 2005/04/27 13:43:17 clq2 Exp $
 * Created on 15-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.query.sql.Sql2Adql;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

/** implementaiton of ApplicationRegistry against a v10 registry schema.
 * @todo handle namespaces better.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2005
 *
 */
public class RegistryV10ApplicationRegistry implements ApplicationRegistry {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryV10ApplicationRegistry.class);

    /** Construct a new RegistryV10ApplicationRegistry
     * 
     */
    public RegistryV10ApplicationRegistry() {
        logger.info("Creating ApplicationRegistry for v10 registry entries");
        logger.info("Creating an astrogrid-backed application registry");
        logger.info("Letting delegate determine own endpoint");
        service = RegistryDelegateFactory.createQuery();
        assert service != null;
    }
public RegistryV10ApplicationRegistry(URL endpoint) {
    logger.info("Creating ApplicationRegistry for v10 registry entries");
    logger.info("Creating an astrogrid-backed application registry");
    logger.info("Letting delegate determine own endpoint");
    service = RegistryDelegateFactory.createQuery();
    assert service != null;
}

protected final RegistryService service;

public final static String LIST_QUERY_STRING= "Select * from Registry where " +
" @xsi:type = 'cea:CeaApplicationType' or " +
" @xsi:type = 'cea:CeaHttpApplicationType'";
    /**
     * 
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#listApplications()
     */
    public String[] listApplications() throws WorkflowInterfaceException {
        try {
            String adqlString = Sql2Adql.translateToAdql074(LIST_QUERY_STRING);
            logger.info("ADQL String in PORTAL for REGISTRY = " + adqlString);

            Document doc = service.search(adqlString);
            assert doc != null;
            NodeList nl = doc.getElementsByTagNameNS("*","identifier");
            
            String[] names = new String[nl.getLength()];
            for (int i = 0; i < nl.getLength(); i++) {
                names[i] =  nl.item(i).getFirstChild().getNodeValue();
            }
            return names;                     
        } catch (RegistryException e) {
            logger.error("listApplications Failed with exception from registry",e);
            throw new WorkflowInterfaceException(e);
        } catch(IOException e) {
            logger.error("IOException Failure probable cause is the sql to adql translator",e);
            throw new WorkflowInterfaceException(e);
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     */
    public ApplicationDescription getDescriptionFor(String applicationName)
            throws WorkflowInterfaceException {
        try {
            Document doc = service.getResourceByIdentifier(applicationName);
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
           // quickly find the vodescription..
           //v0.9 - nl = doc.getElementsByTagNameNS("*","VODescription");
           // is the equivalent of this Resource now??
           nl = doc.getElementsByTagNameNS("*","Resource");
           Element voDesc = null;
           if (nl.getLength() > 0) {
               voDesc = (Element)nl.item(0);
           } else {
               logger.warn("Odd - can't seem to find a Resource " + applicationName + " setting to null");
           }
            return new ApplicationDescription(appBase,voDesc);   
        } catch (CastorException e) {
            logger.error("getDescriptionFor " + applicationName + " - castor failed to parse result",e);
            throw new WorkflowInterfaceException(e);         
        } catch (RegistryException e) {
            logger.error("getDescriptionFor " + applicationName + " - exception from registry",e);
            throw new WorkflowInterfaceException(e);
        }
    }

    public ApplicationDescriptionSummary[] listUIApplications() throws WorkflowInterfaceException {
        try {
            String adqlString = Sql2Adql.translateToAdql074(LIST_QUERY_STRING);
            logger.info("ADQL String in PORTAL for REGISTRY = " + adqlString);
            Document doc = service.search(adqlString);

        assert doc != null;
        NodeList nl = doc.getElementsByTagNameNS("*","Resource");

        ApplicationDescriptionSummary[] descs = new ApplicationDescriptionSummary[nl.getLength()];
        for (int i = 0; i < nl.getLength(); i++) {
            Element resource = (Element)nl.item(i);
            String identifier = resource.getElementsByTagNameNS("*","identifier").item(0).getFirstChild().getNodeValue();
            String title = resource.getElementsByTagNameNS("*","title").item(0).getFirstChild().getNodeValue();

            NodeList interfaces = resource.getElementsByTagNameNS("*","Interface");
            String[] interfaceNames = new String[interfaces.getLength()];
            for (int j = 0; j < interfaces.getLength(); j++) {
                interfaceNames[j] = ((Element)interfaces.item(j)).getAttribute("name");
            }
            descs[i] = new ApplicationDescriptionSummary(identifier,title,interfaceNames);
            
        }
        return descs;
        } catch (RegistryException e){
            logger.error("listUIApplications failed with exception from registry",e);
            throw new WorkflowInterfaceException(e);
        } catch(IOException e) {
            logger.error("IOException Failure probable cause is the sql to adql translator",e);
            throw new WorkflowInterfaceException(e);
        }
    }

}


/* 
$Log: RegistryV10ApplicationRegistry.java,v $
Revision 1.2  2005/04/27 13:43:17  clq2
1082

Revision 1.1.2.1  2005/04/15 17:30:06  nw
added registry implementation for v10.
 
*/