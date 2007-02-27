/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.DescriptionUtils;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.registry.IvornUtil;
import org.astrogrid.common.bean.v1.Namespaces;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.registry.beans.v10.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.v10.cea.CeaApplicationType;
import org.astrogrid.registry.beans.v10.cea.CeaServiceType;
import org.astrogrid.registry.beans.v10.cea.ManagedApplications;
import org.astrogrid.registry.beans.v10.cea.Parameters;
import org.astrogrid.registry.beans.v10.resource.AccessURL;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;
import org.astrogrid.test.AstrogridAssert;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Standard implementation of the
 * {@link org.astrogrid.applications.manager.MetadataService}component.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 21-May-2004
 * @author pharriso@eso.org 02-Jun-2005
 * @TODO - might want some more thought wrt overriding in other cards.
  */
public class DefaultMetadataService implements MetadataService,
      ComponentDescriptor {
   private static final Log logger = LogFactory
         .getLog(DefaultMetadataService.class);

   private static final String FORMATTER_XSL = "registryFormatter.xsl";

   /** configuration settings */
   private Configuration configuration;

   /** library to generate description for */
   private final ApplicationDescriptionLibrary lib;


   /**
    * Construct a new DefaultMetadataService
    * 
    * @param lib
    *           The library of descriptions for which to build a registry entry.
    * @param urls
    *           URLs needed for configuration.
    */
   public DefaultMetadataService(ApplicationDescriptionLibrary lib,
                                 Configuration configuration) {
     this.lib = lib;
     this.configuration = configuration;
   }

   /**
    * Create the registry entry....
    * 
    * @return a VOResources.
    */
   public VOResources makeEntry() 
       throws ApplicationDescriptionNotFoundException,
              MarshalException,
              ValidationException, 
              ParserConfigurationException,
              FactoryConfigurationError, 
              TransformerException, 
              IOException {
     
      // Make a new template, parsing the input file. This picks up any
      // recent changes to the template file.
      VOResources template = this.makeTemplate();
      
      VOResources vodesc = new VOResources();
      CeaApplicationType applicationTemplate = (CeaApplicationType) template
            .getResource(0);
      CeaServiceType serviceTemplate = (CeaServiceType) template.getResource(1);

      CeaServiceType service = cloneTemplate(serviceTemplate);
      ManagedApplications managedApplications = new ManagedApplications();
      service.setManagedApplications(managedApplications);
      ApplicationList applist = makeApplist(lib);
      
      //add each of the application definitions.
      for (int i = 0; i < applist.getApplicationDefnCount(); i++) {
        ApplicationBase theapp = applist.getApplicationDefn(i);
        String applicationName = theapp.getName();
        
        // Applications without names break the container, so don't use them.
        if (applicationName == null) continue;
        
        // Applications in the org.astrogrid.unregistered authority are
        // for special tests and shouldn't go in the registry. Don't use them.
        if (applicationName.startsWith("org.astrogrid.unregistered")) continue;

         
         ApplicationDescription theAppDesc = 
             lib.getDescription(applicationName);

         CeaApplicationType appentry = 
             makeApplicationEntry(applicationTemplate, theapp);

         appentry.getContent()
             .setDescription(theAppDesc.getAppDescription());
         
         appentry.getContent().setReferenceURL(theAppDesc.getReferenceURL());
         appentry.setTitle(theAppDesc.getUIName());
         //TODO getting short name from ui name - probably not appropriate
         String shortname = theAppDesc.getUIName();
         if (shortname.length() > 16) {
           logger.warn("truncating "+shortname+"to 16 characters to fit in VO shortname");
           shortname = shortname.substring(0, 15);
         }
         appentry.setShortName(shortname);
         vodesc.addResource(appentry);
         
        //add this application to the list of managed applications.
         managedApplications.addApplicationReference(appentry.getIdentifier());

      }
      //add the service description
      AccessURL accessurl = new AccessURL();
      accessurl.setContent(this.configuration.getServiceEndpoint().toString());
      service.get_interface(0).setAccessURL(accessurl);
      vodesc.addResource(service);
      return vodesc;

   }

   /**
    * Create and populate a new application entry.
    * 
    * @param template
    *           The template on which the general application information in the
    *           entry is based.
    * @param app
    *           Specific application information to be added to the entry.
    * @return
    * @throws MarshalException
    * @throws ValidationException
    */
   private CeaApplicationType makeApplicationEntry(CeaApplicationType template,
         ApplicationBase app) throws MarshalException, ValidationException {
      
      CeaApplicationType entry = cloneTemplate(template);
      //FIXME the identifier needs to be rationalized...
      entry.setIdentifier(makeIvorn(IvornUtil.extractAuthorityFragment(app
            .getName()), IvornUtil.extractIDFragment(app.getName())));
      //TODO need to get the long description in here too
      ApplicationDefinition applicationDefinition = new ApplicationDefinition();
      // set the interfaces - easy it is the same type...
      applicationDefinition.setInterfaces(app.getInterfaces());

      //parameters not quite so nice REFACTORME - perhaps the schema could be
      // refactored....
      Parameters regpar = new Parameters();
      regpar.setParameterDefinition(app.getParameters().getParameter());
      applicationDefinition.setParameters(regpar);

      entry.setApplicationDefinition(applicationDefinition);
      return entry;
   }

   /**
    * Create a clone of the given object. Does this by using the castor
    * marshalling/unmarshalling on the object.
    * 
    * @param app
    * @return
    * @throws MarshalException
    * @throws ValidationException
    */
   private CeaApplicationType cloneTemplate(CeaApplicationType app)
         throws MarshalException, ValidationException {
      StringWriter sw = new StringWriter();
      CeaApplicationType newapp = null;
      app.marshal(sw);
      StringReader sr = new StringReader(sw.toString());
      InputSource is = new InputSource(sr);
      Unmarshaller um = new Unmarshaller(CeaApplicationType.class);
      newapp = (CeaApplicationType) um.unmarshal(is);

      return newapp;

   }

   /**
    * Create a clone of the given object. Does this by using the castor
    * marshalling/unmarshalling on the object.
    * 
    * @param serv
    * @return
    * @throws MarshalException
    * @throws ValidationException
    */
   private CeaServiceType cloneTemplate(CeaServiceType serv)
         throws MarshalException, ValidationException {
      CeaServiceType newserv = null;
      StringWriter sw = new StringWriter();
      serv.marshal(sw);
      //need to put in the castor hack to make the interface a web service type
      String sb = sw
            .toString()
            .replaceAll(
                  "xsi:type=\"WebService\"",
                  "xsi:type=\"java:org.astrogrid.registry.beans.v10.resource.dataservice.WebService\"");

      StringReader sr = new StringReader(sb);
      InputSource is = new InputSource(sr);
      Unmarshaller um = new Unmarshaller(CeaServiceType.class);
      newserv = (CeaServiceType) um.unmarshal(is);

      return newserv;
   }

   /**
    * Make an ApplicationList from a full configuration. This is a deep copy -
    * new instances of the ApplicationBase objecst are created.
    * 
    * @param clecConfig
    *           a configuration for a command line execution controller
    * @return
    * @TODO might be better to refactor the original schema so that there was a
    *       base type for the common execution contoller configs...
    */
   private ApplicationList makeApplist(ApplicationDescriptionLibrary lib)
         throws ApplicationDescriptionNotFoundException {
      ApplicationList result = new ApplicationList();
      String names[] = lib.getApplicationNames();
      for (int i = 0; i < names.length; i++) {
         ApplicationDescription descr = lib.getDescription(names[i]);
         ApplicationBase base = DescriptionUtils
               .applicationDescription2ApplicationBase(descr);
         result.addApplicationDefn(base);
      }
      return result;
   }
   
   /**
    * Gets a URL leading to the current registration-template. The
    * location of the template is set during construction.
    */
   public URL getRegistrationTemplate() {
     return this.configuration.getRegistryTemplate();
   }
   
   /**
    * This should potentially be overriden by subclasses.
    * @see org.astrogrid.applications.component.ProvidesVODescription#getDescription()
    * @todo could cache the result.
    */
   public VOResources getVODescription() throws Exception {
      return makeEntry();
   }

   /**
    * loads template fron url, builds objects from it. Has to "hack" the
    * template to change it from being valid to allow castor to actually read
    * it.
    * 
    * @throws IOException
    * @throws TransformerException
    * @throws FactoryConfigurationError
    * @throws ParserConfigurationException
    */
   private VOResources makeTemplate() 
       throws MarshalException,
              ValidationException, 
              ParserConfigurationException,
              FactoryConfigurationError, 
              TransformerException, 
              IOException {
      logger.info("Registry template is read from " + 
                  this.configuration.getRegistryTemplate());
      String hackedtemplate
          = transformTemplateForCastor(this.configuration.getRegistryTemplate().openStream(), 
                                       this.getClass().getResourceAsStream("/CastorHacker.xsl"));

      Unmarshaller um = new Unmarshaller(VOResources.class);
      um.setIgnoreExtraAttributes(true);
      um.setIgnoreExtraElements(true);
      StringReader sr = new StringReader(hackedtemplate);
      InputSource is = new InputSource(sr);
      VOResources temp = (VOResources) um.unmarshal(is);
      return temp;
   }

   private String makeIvorn(String auth, String id) {
      StringBuffer sb = new StringBuffer("ivo://");
      sb.append(auth);
      sb.append("/");
      sb.append(id);
      return sb.toString();
   }

   /**
    * takes an xml valid template and "hacks" to form that castor can read. This
    * involves setting xsi:type=java:class for each derived class of Resource,
    * and is achieved via an xsl translation in an external file.
    * 
    * @param in
    * @param inxsl
    * @return
    * @throws ParserConfigurationException
    * @throws FactoryConfigurationError
    * @throws TransformerException
    */
   private String transformTemplateForCastor(InputStream in, InputStream inxsl)
         throws ParserConfigurationException, FactoryConfigurationError,
         TransformerException {
      // Create transformer factory
      TransformerFactory factory = TransformerFactory.newInstance();

      // Use the factory to create a template containing the xsl file
      Templates template = factory.newTemplates(new StreamSource(inxsl));

      // Use the template to create a transformer
      Transformer xformer = template.newTransformer();

      // Prepare the input file
      Source source = new StreamSource(in);

      //
      StringWriter sw = new StringWriter();

      // Create a new document to hold the results
      Result result = new StreamResult(sw);

      // Apply the xsl file to the source file and create the DOM tree
      xformer.transform(source, result);
      return sw.toString();
   }


   /**
    * This is final because the intention is that all differences between implementations are expressed by overriding @link DefaultMetadataService#getVODescription()
    * @see org.astrogrid.applications.manager.MetadataService#returnRegistryEntry()
    */
   public final Document returnRegistryEntry() throws CeaException {

      InputStream formatterXSL = null;
      Document finalDoc = null;

      try {
        
         // Serialize the registration metadata into an XML document using
         // Castor. Note that the metadata are actually accessed on the very
         // last call of this block, via getVODescription(); everything else
         // in the block is setting up Castor. Leave the serialized XML in 
         // the StringWriter sw.
         DocumentBuilder builder = DocumentBuilderFactory.newInstance()
               .newDocumentBuilder();
         StringWriter sw = new StringWriter(1000);
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
         marshaller.marshal(this.getVODescription());

         // Transform the contents of sw to make a cleaned-up registration.
         // Extract the result as a DOM.
         TransformerFactory fac = TransformerFactory.newInstance();
         String xslpath = DefaultMetadataService.class.getPackage() + FORMATTER_XSL;
         formatterXSL = DefaultMetadataService.class.getResourceAsStream(FORMATTER_XSL);
         Source formatter = new StreamSource(formatterXSL);
         Templates xsltTemplate = fac.newTemplates(formatter);
         Transformer xformer = xsltTemplate.newTransformer();
         StringReader sr = new StringReader(sw.toString());
         Source source = new StreamSource(sr);
         finalDoc = builder.newDocument();
         Result result = new DOMResult(finalDoc);
         xformer.transform(source, result);
      } catch (Exception e) {
         logger.error("could not marshal VODescription", e);
         throw new CeaException("could not marshal VODescription", e);
      }

      return finalDoc;
   }

   /**
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
    */
   public String getName() {
      return "Standard CEA Server Description";
   }

   /**
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
    */
   public String getDescription() {
      StringBuffer sb = new StringBuffer("Standard implementation of the service description component`n");
      try {
         Document doc = this.returnRegistryEntry();
         StringWriter sw = new StringWriter();
         XMLUtils.PrettyDocumentToWriter(doc, sw);
         sb.append("VODescription \n" + XMLUtils.xmlEncodeString(sw.toString()));
      } catch (Exception e) {
         sb.append( "Could not display description: " + e.getMessage());
      }

      return sb.toString();
   }

   /**
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
    */
   public Test getInstallationTest() {
      return new InstallationTest("testGetRegistryEntry");
   }

   public class InstallationTest extends TestCase {

      public InstallationTest(String arg0) {
         super(arg0);
      }

      public void testGetRegistryEntry() throws Exception {
        System.out.println(Namespaces.VODATASERVICE);
        System.out.println(Namespaces.CEAIMPL);
         Document entry = returnRegistryEntry();
         assertNotNull(entry);
         AstrogridAssert.assertSchemaValid(entry, "VOResources", SchemaMap.ALL);
      }

   }
}