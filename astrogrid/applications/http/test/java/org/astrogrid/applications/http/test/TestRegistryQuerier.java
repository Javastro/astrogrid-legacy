/* $Id: TestRegistryQuerier.java,v 1.8 2006/03/07 21:45:26 clq2 Exp $
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;

import junit.framework.Test;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.astrogrid.applications.description.registry.IvornUtil;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.applications.http.registry.AbstractRegistryQuerier;
import org.astrogrid.common.bean.v1.Namespaces;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;

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
    */
   public TestRegistryQuerier(RegistryQueryLocator locator)
         throws MarshalException, ValidationException {
      super(locator);
      addApplication("helloWorld-app.xml");
      addApplication("Echoer-app.xml");
      addApplication("HelloYou-app.xml");
      addApplication("Adder-app.xml");
      addApplication("Adder-post-app.xml");
      addApplication("Bad404-app.xml");
      addApplication("BadTimeOut-app.xml");
      addApplication("BadMalformedURL-app.xml");
      addApplication("Adder-preprocess-app.xml");

   }

   /**
    * Get a specific application by its name. The name needed as an argument
    * is the IVOID of the application with the ivo:// prefix removed.
    *
    * @param name
    *           Name of application
    * @return the unmarshalled CeaHttpApplicationType
    */
   public CeaHttpApplicationType getHttpApplication(final String name) {
      return (CeaHttpApplicationType) applications.get(name);
   }

   /**
    * Add a CeaHttpApplicationType to our "registry" given the object serialised
    * to xml
    *
    * @param file
    *           filename of serialised object
    * @throws MarshalException
    * @throws ValidationException
    */
   private void addApplication(String file) throws MarshalException,
         ValidationException {
      FileUnmarshaller unmarshaller = new FileUnmarshaller(
            CeaHttpApplicationType.class);
      logger.debug("loading http application from file=" + file);
      CeaHttpApplicationType testApplication = (CeaHttpApplicationType) unmarshaller
            .unmarshallFromFile(file);
      //take the ivo:// part off the identifier
      String ivorn = testApplication.getIdentifier();
      String name = IvornUtil.removeProtocol(ivorn);
      testApplication.setIdentifier(name);
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
         CeaHttpApplicationType element = (CeaHttpApplicationType) iter.next();
         vor.addResource(element);

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
         Marshaller marshaller = new Marshaller(sw);
         marshaller.setDebug(true);
         marshaller.setMarshalExtendedType(true);
         marshaller.setSuppressXSIType(false);
         marshaller.setMarshalAsDocument(true);

         //     TODO write a castor wiki page about this.... it is useful to stop
         // castor putting namespace declarations all over the place, and
         // essential to get the correct declarations inserted for some derived
         // types, which castor does not do properly.
         marshaller.setNamespaceMapping("cea", Namespaces.VOCEA);
         marshaller.setNamespaceMapping("vr", Namespaces.VORESOURCE);
         marshaller.setNamespaceMapping("ceapd", Namespaces.CEAPD);
         marshaller.setNamespaceMapping("ceab", Namespaces.CEAB);
         marshaller.setNamespaceMapping("vs", Namespaces.VODATASERVICE);
         marshaller.marshal(vor);
         logger.debug(sw.toString());
         StringReader sr = new StringReader(sw.toString());
         InputSource input = new InputSource(sr);
         finalDoc = XMLUtils.newDocument(input);
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
      String id = arg0.substring(6); // remove the ivo://prefix as internally the names are stored without it.
      CeaHttpApplicationType app = (CeaHttpApplicationType) applications.get(id);
      logger.info("cannot find application="+id);

      VOResources vor = new VOResources();
      vor.addResource(app);
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

}