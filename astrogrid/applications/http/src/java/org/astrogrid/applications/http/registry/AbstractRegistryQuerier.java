/* $Id: AbstractRegistryQuerier.java,v 1.5 2005/07/05 08:27:01 clq2 Exp $
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

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
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
      } catch (RegistryException e) {
         log.error("could not create registry querier", e);
      }
      }
      else
      {
         service=null;
      }
   }

   /**
     * Unmarshall a node of a Document into a class
     * 
     * @param doc the document
     * @param clazz the class you hope to get back
     * @param elementName the element name in the document you hope matches the
     *            class
     * @return object that will need casting...
     * @throws MarshalException
     * @throws ValidationException
     * @throws RegistryCallException
     */
   protected Object unmarshal(final Document doc, final Class clazz, final String elementName) throws MarshalException, ValidationException, RegistryException {
        if (log.isTraceEnabled()) {
            log.trace("unmarshal(Document doc = " + doc + ", Class clazz = " + clazz + ", String elementName = "
                    + elementName + ") - start");
        }
   
        if (log.isDebugEnabled()) {
        	log.debug("Unmarshalling document:");
        	log.debug(DomHelper.DocumentToString(doc));
        	log.debug("=======================");
        }
        //      navigate down to the bit we're interested in.
        final NodeList nls = doc.getElementsByTagNameNS("*", elementName);
        if (nls.getLength() == 0) {
            throw new RegistryException("Registry entry has no " + elementName + " Element");
        }
        final Element ns = (Element) nls.item(0);
        ns.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); //bug-fix work around.
        Object returnObject = Unmarshaller.unmarshal(clazz, ns);
        if (log.isTraceEnabled()) {
            log.trace("unmarshal(Document, Class, String) - end - return value = " + returnObject);
        }
        return returnObject;
    }

   protected static final Log log = LogFactory.getLog(RegistryQuerierImpl.class);

   /**
     * unmarshall the CeaHttpApplication from DOM registry representation. Include whatever fixes are needed.
     * Firstly only the first resource found is returned
     * @param doc
     * @return
     * @throws MarshalException
     * @throws ValidationException
    * @throws RegistryException
     */
   protected CeaHttpApplicationType unmarshallHttpApplication(Document doc) throws MarshalException, ValidationException, RegistryException {
       final NodeList nls = doc.getElementsByTagNameNS("*","Resource"); 
//       XMLUtils.DocumentToStream(doc, System.err);
       if (nls.getLength() == 0) {
          throw new RegistryException("Registry entry has no Resource Element");
      }
      final Element ns = (Element) nls.item(0);
      ns.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); //TODO castor bug-fix work around.
      ns.removeAttribute("updated");//TODO castor bug fix workaround - cannot handle dates properly
      final CeaHttpApplicationType webApplication = (CeaHttpApplicationType) Unmarshaller.unmarshal(
             CeaHttpApplicationType.class, ns);
       
       return webApplication;
     
    
    }

   protected RegistryService service;

}