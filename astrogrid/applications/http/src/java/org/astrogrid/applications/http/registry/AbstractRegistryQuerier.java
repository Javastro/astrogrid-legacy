/* $Id: AbstractRegistryQuerier.java,v 1.6 2008/09/03 14:19:03 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Jul 30, 2004
 */
package org.astrogrid.applications.http.registry;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.ivoa.resource.registry.iface.VOResources;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.util.DomHelper;

/**
 * Contains stuff likely to be common to all RegistryQueriers...maintains a List
 * of WebHttpApplications etc
 * 
 * @author jdt
 */
public abstract class AbstractRegistryQuerier implements RegistryQuerier {

    protected Map applications = new HashMap();

    /**
     * Returns a list of  HttpApplication beans. 
     */
    public Collection getHttpApplications() throws IOException {
        return applications.values();
    }
    
    /**
    * 
    */
   public AbstractRegistryQuerier(RegistryQueryLocator locator) {
      service = null;
      if(locator != null)
      {
      try {
         service = locator.getClient();
         jc = CEAJAXBContextFactory.newInstance();

      } catch (RegistryException e) {
         log.fatal("could not create registry querier", e);
      } catch (JAXBException e) {
	// TODO want to propagate this up?
	log.fatal("could not create the jaxb context factory", e);
    }
      }
      else
      {
         service=null;
      }
   }

   protected static final Log log = LogFactory.getLog(RegistryQuerierImpl.class);

   /**
     * unmarshall the CeaHttpApplicationDefinition from DOM registry representation. Include whatever fixes are needed.
     * Firstly only the first resource found is returned
     * @param doc
     * @return
    * @throws RegistryException
 * @throws JAXBException 
     */
   protected CeaHttpApplicationDefinition unmarshallHttpApplication(Document doc) throws RegistryException, JAXBException {
    
     Unmarshaller um = jc.createUnmarshaller();
        VOResources u = (VOResources) um.unmarshal(
                doc);
        //TODO need more testing
      final CeaHttpApplicationDefinition webApplication = (CeaHttpApplicationDefinition) u.getResource();
       
       return webApplication;
     
    
    }

   protected RegistryService service;
private JAXBContext jc;

}