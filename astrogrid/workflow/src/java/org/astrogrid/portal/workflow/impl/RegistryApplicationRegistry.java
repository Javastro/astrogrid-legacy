/*$Id: RegistryApplicationRegistry.java,v 1.15 2006/06/17 18:49:25 clq2 Exp $
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
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.beans.resource.ResourceType;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.oldquery.sql.Sql2Adql;

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
 * NB: this implements the applicationi registry against the 0.9 registry schema. see {@link org.astrogrid.portal.workflow.impl.RegistryV10ApplicationRegistry}
 * for an implementation for the v10 registry schema. Factory chooses correct one depending on environment. 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class RegistryApplicationRegistry implements ApplicationRegistry {
    private static final Log logger = LogFactory.getLog(RegistryApplicationRegistry.class);

   /** construct a new RegistryApplicationRegistry
    * let registry-delegate self-configure */
   public RegistryApplicationRegistry() {
     logger.info("Creating ApplicationRegistry for v0.9 registry entries");
     logger.info("Creating an astrogrid-backed application registry");
     logger.info("Letting delegate determine own endpoint");
     service = RegistryDelegateFactory.createQuery();
     assert service != null;
   }

    /** Construct a new RegistryApplicationRegistry
     * @param endpoint endpoint for the astrogrid registry web service
     */
    public RegistryApplicationRegistry(URL endpoint) {
        logger.info("Creating ApplicationRegistry for v0.9 registry entries");
        logger.info("Creating an astrogrid-backed application registry");
        service  = RegistryDelegateFactory.createQuery(endpoint);
        assert service != null;
    }
    protected final RegistryService service;
    /** string query to to pass to registry to get list of tools back 
    */
    /*
    public final static String LIST_QUERY_STRING= "<query><selectionSequence>" +
    "<selection item='searchElements' itemOp='EQ' value='vr:Resource'/>" +
    "<selectionOp op='$and$'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='CeaApplicationType'/>"  +
    "<selectionOp op='OR'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='cea:CeaApplicationType'/>"  +
    "<selectionOp op='OR'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='CeaHttpApplicationType'/>"  +
    "<selectionOp op='OR'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='cea:CeaHttpApplicationType'/>"  +
    "</selectionSequence></query>";
    */
    
    public final static String LIST_QUERY_STRING= "Select * from Registry where " +
    " (@xsi:type = 'cea:CeaApplicationType' or " +
    " @xsi:type = 'cea:CeaHttpApplicationType') " +
    " and @status = 'active'";
    
    public String[] listApplications() throws WorkflowInterfaceException {
        try {
            String adqlString = Sql2Adql.translateToAdql074(LIST_QUERY_STRING);
            logger.info("ADQL String in PORTAL for REGISTRY = " + adqlString);
            /*
              Document doc = service.submitQuery(LIST_QUERY_STRING); 
            */
            Document doc = service.search(adqlString);
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
        } catch(IOException e) {
            logger.error("IOException Failure probable cause is the sql to adql translator",e);
            throw new WorkflowInterfaceException(e);
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#listUIApplications()
     */
    public ApplicationDescriptionSummary[] listUIApplications() throws WorkflowInterfaceException {
        try {
            String adqlString = Sql2Adql.translateToAdql074(LIST_QUERY_STRING);
            logger.info("ADQL String in PORTAL for REGISTRY = " + adqlString);
            /*
              Document doc = service.submitQuery(LIST_QUERY_STRING); 
            */
            Document doc = service.search(adqlString);

        assert doc != null;
        NodeList nl = doc.getElementsByTagNameNS("*","Resource");

        ApplicationDescriptionSummary[] descs = new ApplicationDescriptionSummary[nl.getLength()];
        for (int i = 0; i < nl.getLength(); i++) {
            Element resource = (Element)nl.item(i);
            String authId =  resource.getElementsByTagNameNS("*","AuthorityID").item(0).getFirstChild().getNodeValue();
            String resourceKey = resource.getElementsByTagNameNS("*","ResourceKey").item(0).getFirstChild().getNodeValue();
            String title = resource.getElementsByTagNameNS("*","Title").item(0).getFirstChild().getNodeValue();
            /* crap - was extracting the wrong thing. wanted title instead
            NodeList uiElement = resource.getElementsByTagNameNS("*","UI_Name");
            String uiName = "unavailable";
            if(uiElement.getLength() > 0) {
                Node n = uiElement.item(0).getFirstChild();
                if (n != null) {
                    uiName = n.getNodeValue();
                }
            }
            */
            NodeList interfaces = resource.getElementsByTagNameNS("*","Interface");
            String[] interfaceNames = new String[interfaces.getLength()];
            for (int j = 0; j < interfaces.getLength(); j++) {
                interfaceNames[j] = ((Element)interfaces.item(j)).getAttribute("name");
            }
            descs[i] = new ApplicationDescriptionSummary(authId + "/" + resourceKey,title,interfaceNames);
            
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
    
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     * @todo make namespace aware.
     */
    public ApplicationDescription getDescriptionFor(String applicationName) throws WorkflowInterfaceException {
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
           nl = doc.getElementsByTagNameNS("*","VODescription");
           Element voDesc = null;
           if (nl.getLength() > 0) {
               voDesc = (Element)nl.item(0);
           } else {
               logger.warn("Odd - can't seem to find a VODescription for " + applicationName + " setting to null");
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


    
}


/* 
$Log: RegistryApplicationRegistry.java,v $
Revision 1.15  2006/06/17 18:49:25  clq2
to match KEA_PAL_1612 change of package name of org.astrogrid.query.sql.Sql2Adql to org.astrogrid.oldquery.sql.Sql2Adql

Revision 1.14  2005/06/09 16:33:56  clq2
1120

Revision 1.13.4.1  2005/06/01 14:22:52  pjn3
LIST_QUERY_STRING now includes check for status = active

Revision 1.13  2005/04/27 13:43:17  clq2
1082

Revision 1.12.24.1  2005/04/15 17:30:06  nw
added registry implementation for v10.

Revision 1.12  2005/03/01 13:23:33  clq2
upgrade to v10 registry

Revision 1.11.16.2  2005/02/28 21:58:50  KevinBenson
Now uses the adql translator to put in adql and query registry using adql.

Revision 1.11.16.1  2005/02/23 15:37:32  KevinBenson
now uses the sql2adql so it queries the registry by adql now.

Revision 1.11  2004/12/13 13:55:05  pjn3
Bug #827 - change by RTP to query string to include cea namespace

Revision 1.10  2004/11/12 18:14:43  clq2
nww-itn07-590b again.

Revision 1.8.4.2  2004/11/11 14:37:16  nw
bugfix. was exracting the wrong field

Revision 1.8.4.1  2004/11/10 13:33:32  nw
added new method to ApplicationRegistry - listUIApplications

Revision 1.8  2004/11/08 18:05:15  jdt
Merges from branch nww-bz#590

Revision 1.7.2.1  2004/10/28 14:53:50  nw
added method getOriginalVODescription() to ApplicationDescription,
adjusted RegistryApplicationRegistry to populate this field.

Revision 1.7  2004/10/12 11:24:21  pah
used explicit namespaces in query for 200x speedup!

Revision 1.6  2004/10/08 20:04:39  pah
optimize the tool query to use namespaces - better performance than using wildcard

Revision 1.5  2004/09/02 13:03:53  jdt
merge from SIAP case3

*/