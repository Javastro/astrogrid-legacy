/* $Id: TestRegistryQuerier.java,v 1.12 2008/09/03 14:18:51 pah Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.astrogrid.applications.http.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import junit.framework.Test;
import net.ivoa.resource.registry.iface.VOResources;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.registry.IvornUtil;
import org.astrogrid.applications.description.registry.NamespacePrefixMapperImpl;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.applications.http.registry.AbstractRegistryQuerier;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Returns pretend meta data about the TestWebServer test services. Acts as both
 * a test registryQuerier, and a RegistryService.
 *
 * @author jdt
 * @author pharriso@eso.org 07-Jun-2005
 */
public class TestRegistryQuerier extends AbstractRegistryQuerier implements
      RegistryService {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(TestRegistryQuerier.class);

   /**
    * Ctor
    *
    * @throws ValidationException
    * @throws MarshalException
 * @throws MetadataException 
 * @throws javax.xml.stream.FactoryConfigurationError 
 * @throws XMLStreamException 
 * @TODO does this really need the RegistyQueryLocator?
    */
   public TestRegistryQuerier(RegistryQueryLocator locator)
         throws MarshalException, ValidationException, MetadataException, XMLStreamException, javax.xml.stream.FactoryConfigurationError {
      super(locator);
      addApplication("/helloWorld-app.xml");
      addApplication("/Echoer-app.xml");
      addApplication("/HelloYou-app.xml");
      addApplication("/Adder-app.xml");
      addApplication("/Adder-post-app.xml");
      addApplication("/Bad404-app.xml");
      addApplication("/BadTimeOut-app.xml");
      addApplication("/BadMalformedURL-app.xml");
      addApplication("/Adder-preprocess-app.xml");

   }

   /**
    * Get a specific application by its name. The name needed as an argument
    * is the IVOID of the application with the ivo:// prefix removed.
    *
    * @param name
    *           Name of application
    * @return the unmarshalled CeaHttpApplicationDefinition
    */
   public CeaHttpApplicationDefinition getHttpApplication(final String name) {
      return (CeaHttpApplicationDefinition) applications.get(name);
   }

   /**
    * Add a CeaHttpApplicationDefinition to our "registry" given the object serialised
    * to xml
    *
    * @param file
    *           filename of serialised object
    * @throws MarshalException
    * @throws ValidationException
 * @throws MetadataException 
 * @throws javax.xml.stream.FactoryConfigurationError 
 * @throws XMLStreamException 
    */
   private void addApplication(String file) throws MarshalException,
         ValidationException, MetadataException, XMLStreamException, javax.xml.stream.FactoryConfigurationError {
      FileUnmarshaller unmarshaller = new FileUnmarshaller(
            CeaHttpApplicationDefinition.class);
      logger.debug("loading http application from file=" + file);
      
      CeaHttpApplicationDefinition testApplication = (CeaHttpApplicationDefinition) unmarshaller
            .unmarshallFromFile(file);
      //take the ivo:// part off the identifier
      String name = testApplication.getIdentifier();
      logger.debug("Loaded " + name);
      applications.put(name, testApplication);
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
    */
   public String getName() {
      return "TestRegistryQuerier";
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
    */
   public String getDescription() {
      return "A test RegistryQuerier for unit testing";
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
    *      No installation tests required, since only used for unit testing.
    */
   public Test getInstallationTest() {
      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getRegistries()
    */
   public Document getRegistries() throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getRegistries() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#search(java.lang.String)
    */
   public Document search(String arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.search() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#search(org.w3c.dom.Document)
    */
   public Document search(Document arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.search() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#searchFromSADQL(java.lang.String)
    */
   public Document searchFromSADQL(String arg0) throws RegistryException {
      logger.debug("doing dummy search for " + arg0);
      VOResources vor = new VOResources();
      for (Iterator iter = applications.values().iterator(); iter.hasNext();) {
         CeaHttpApplicationDefinition element = (CeaHttpApplicationDefinition) iter.next();
         vor.getResource().add(element);

      }
      return marshallResources(vor);
   }

   public Document xquerySearch(String xquery) throws RegistryException {

       return null;
   }

   /**
    * @param vor
    * @return
    * @throws FactoryConfigurationError
    * @throws RegistryException
    */
   private Document marshallResources(VOResources vor) throws  RegistryException {
      //now use castor to marshall..
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true); // very important!
         StringWriter sw = new StringWriter();
         DocumentBuilder builder = factory.
               newDocumentBuilder();

         // more bloody castor bugs - the marshaller will not marshal correctly namespaces to a dom tree - do to a string first.
         Document finalDoc = builder.newDocument();
	JAXBContext jc = CEAJAXBContextFactory.newInstance();

        
        Marshaller marshaller = jc.createMarshaller();
	    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
		    	new NamespacePrefixMapperImpl());
         marshaller.marshal(vor,finalDoc);
         return finalDoc;
      } catch (Exception e) {
         // TODO Auto-generated catch block
         throw new RegistryException("could not return dummy registry entry", e);
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#submitQuery(java.lang.String)
    */
   public Document submitQuery(String arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.submitQuery() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#submitQuery(org.w3c.dom.Document)
    */
   public Document submitQuery(Document arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.submitQuery() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#loadRegistry()
    */
   public Document loadRegistry() throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.loadRegistry() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#managedAuthorities()
    */
   public HashMap managedAuthorities() throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.managedAuthorities() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getResourceByIdentifier(org.astrogrid.store.Ivorn)
    */
   public Document getResourceByIdentifier(Ivorn arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getResourceByIdentifier() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getResourceByIdentifier(java.lang.String)
    */
   public Document getResourceByIdentifier(String arg0)
         throws RegistryException {
      String id = arg0; 
      CeaHttpApplicationDefinition app = (CeaHttpApplicationDefinition) applications.get(id);
      logger.info("found application="+id);

      VOResources vor = new VOResources();
      vor.getResource().add(app);
      return marshallResources(vor);

   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getResourceDataByIdentifier(org.astrogrid.store.Ivorn)
    */
   public ResourceData getResourceDataByIdentifier(Ivorn arg0)
         throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getResourceDataByIdentifier() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getResourceDataByRelationship(org.astrogrid.store.Ivorn)
    */
   public ResourceData[] getResourceDataByRelationship(Ivorn arg0)
         throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getResourceDataByRelationship() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getResourceDataByRelationship(java.lang.String)
    */
   public ResourceData[] getResourceDataByRelationship(String arg0)
         throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getResourceDataByRelationship() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getEndPointByIdentifier(org.astrogrid.store.Ivorn)
    */
   public String getEndPointByIdentifier(Ivorn arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getEndPointByIdentifier() not implemented");
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.registry.client.query.RegistryService#getEndPointByIdentifier(java.lang.String)
    */
   public String getEndPointByIdentifier(String arg0) throws RegistryException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "TestRegistryQuerier.getEndPointByIdentifier() not implemented");
   }

   public Document keywordSearch(String keywords,boolean orValues) throws RegistryException {
     throw new RegistryException("keywordSearch(String keywords,boolean orValues) is not implemented.");
   }

    public Document keywordSearch(String keywords) throws RegistryException {
      throw new RegistryException("keywordSearch(String keywords) is not implemented.");
   }

  public Document getIdentity() throws RegistryException {
    return null;
  }

}