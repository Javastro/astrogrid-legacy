/*
 * $Id: RegistryEntryBuilder.java,v 1.4 2004/08/28 07:17:34 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.DescriptionUtils;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.cea.CeaApplicationType;
import org.astrogrid.registry.beans.cea.CeaServiceType;
import org.astrogrid.registry.beans.cea.ManagedApplications;
import org.astrogrid.registry.beans.cea.Parameters;
import org.astrogrid.registry.beans.resource.AccessURLType;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType;

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
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Creates a VODescription  based on the contents of an ApplicationDescriptionLibrary.
 * 
 * Requires a template file of the VODescription that contains the basic CeaApplication and CeaService entries to fill in.
 * @author Noel Winstanley
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 * @see org.astrogrid.applications.component.ProvidesVODescription
 * @todo this could do with a bit of a refactor - its quite messy still. Probably best to wait until the registry schema changes anyway..
 */
public class RegistryEntryBuilder implements ProvidesVODescription , ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryEntryBuilder.class);
   /** configuration settings */
   private URLs urls;

  /** library to generate description for */
   private final ApplicationDescriptionLibrary lib;

    /** Configuration interface - specifies location of resources required by {@link RegistryEntryBuilder}
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
     *
     */
    public interface URLs {
        /** url of the template registry entry to use */
        URL getRegistryTemplate();
        /** url of the endpoint of the current cea service */
        URL getServiceEndpoint();
    }


    /**
     *  Construct a new RegistryEntryBuilder
     * @param lib the library of descriptions to build a registry entry for
     * @param urls configuration urls
     */
   public RegistryEntryBuilder(ApplicationDescriptionLibrary lib, URLs urls) {
      this.lib = lib;
      this.urls = urls;
      //FIXME need to read the template to get hold of the AuthorityID and the serverID to have those set early
   }

   /**
    * Create the registry entry....
    * @return a vodescription.
    */
   public VODescription makeEntry() throws MarshalException, ValidationException, IOException, ApplicationDescriptionNotFoundException {
      VODescription vodesc = new VODescription();
      VODescription template = makeTemplate();
      CeaApplicationType applicationTemplate =
         (CeaApplicationType)template.getResource(0);
      CeaServiceType serviceTemplate = (CeaServiceType)template.getResource(1);

      CeaServiceType service = cloneTemplate(serviceTemplate);
      ManagedApplications managedApplications = new ManagedApplications();
      service.setManagedApplications(managedApplications);
      ApplicationList applist = makeApplist(lib);
      //add each of the application definitions.
      for (int i = 0; i < applist.getApplicationDefnCount(); i++) {

         ApplicationBase theapp = applist.getApplicationDefn(i);
         
         if (theapp.getName() != null) { //TODO this test is only here to get round a bug in the container, where a null application seems to be instantiated.
            CeaApplicationType appentry = makeApplicationEntry(
                    applicationTemplate, theapp);
            vodesc.addResource(appentry);
            //add this application to the list of managed applications.
            managedApplications.addApplicationReference(appentry
                    .getIdentifier());
        }

      }
      //add the service description
      AccessURLType accessurl = new AccessURLType();
      accessurl.setContent(urls.getServiceEndpoint().toString()) ;     
      accessurl.setUse(AccessURLTypeUseType.BASE);
      service.getInterface().setAccessURL(accessurl);
      vodesc.addResource(service);
      return vodesc;

   }

   /**
    * Create and populate a new application entry. 
    * @param template The template on which the general application information in the entry is based.
    * @param app Specific application information to be added to the entry.
    * @return
    * @throws MarshalException
    * @throws ValidationException
    */
   private CeaApplicationType makeApplicationEntry(
      CeaApplicationType template,
      ApplicationBase app)
      throws MarshalException, ValidationException {

      CeaApplicationType entry = cloneTemplate(template);
      entry.getIdentifier().setResourceKey(stripAuthorityFragment(app.getName()));
      ApplicationDefinition applicationDefinition = new ApplicationDefinition();
      // set the interfaces - easy it is the same type...     
      applicationDefinition.setInterfaces(app.getInterfaces());
      
      //parameters not quite so nice REFACTORME - perhaps the schema could be refactored....
      Parameters regpar = new Parameters();
      regpar.setParameterDefinition(app.getParameters().getParameter());
      applicationDefinition.setParameters(regpar);
      

      entry.setApplicationDefinition(applicationDefinition);
      return entry;
   }
   
   /** strips authority fragment, if present */
   private String stripAuthorityFragment(String s) {
       if (s.indexOf('/') != -1) {
           return s.substring(s.indexOf('/') + 1);
       } else {
           return s;
       }
   }

   /**
    * Create a clone of the given object. Does this by using the castor marshalling/unmarshalling on the object.
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
      newapp = (CeaApplicationType)um.unmarshal(is);

      return newapp;

   }

   /**
    *  Create a clone of the given object. Does this by using the castor marshalling/unmarshalling on the object.
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
      StringReader sr = new StringReader(sw.toString());
      InputSource is = new InputSource(sr);
      Unmarshaller um = new Unmarshaller(CeaServiceType.class);
      newserv = (CeaServiceType)um.unmarshal(is);

      return newserv;
   }

   /**
    * Make an ApplicationList from a full configuration. This is a deep copy - new instances of the ApplicationBase objecst are created.
    * @param clecConfig a configuration for a command line execution controller
    * @return
    * @TODO might be better to refactor the original schema so that there was a base type for the common execution contoller configs...
    */
   private ApplicationList makeApplist(ApplicationDescriptionLibrary lib) throws ApplicationDescriptionNotFoundException { 
      ApplicationList result = new ApplicationList();
      String names[] = lib.getApplicationNames();
      for (int i = 0; i < names.length; i++) {
          ApplicationDescription descr = lib.getDescription(names[i]);
          ApplicationBase base = DescriptionUtils.applicationDescription2ApplicationBase(descr);
         result.addApplicationDefn(base);
      }
      return result;
   }
   
 

/**
    * Create a clone of an ApplicationBase object. This is done via the castor marshalling framework. In most cases this will be a downcast copy.
    * @param in This can (and in most cases will) be one of the derived classes from ApplicationBase
    * @return
    */
/* not used - just left in for reference
   private ApplicationBase cloneApplication(ApplicationBase in) throws IOException, MarshalException, ValidationException
   {
      ApplicationBase result = null;
// TODO write a castor wiki page about this....      
      StringWriter sw = new StringWriter();
      Marshaller mar = new Marshaller(sw);
      mar.setMarshalExtendedType(false);
      mar.setSuppressXSIType(false);
      mar.setMarshalAsDocument(true);
      mar.marshal(in);
//      System.err.print(sw.toString());
      
     Unmarshaller um = new Unmarshaller(ApplicationBase.class);
     um.setIgnoreExtraAttributes(true);
     um.setIgnoreExtraElements(true);
     StringReader sr = new StringReader(sw.toString());
     InputSource is = new InputSource(sr);
     result = (ApplicationBase)um.unmarshal(is);

      
      
      return result;
   }
*/
/**
 * @see org.astrogrid.applications.component.ProvidesVODescription#getDescription()
 * @todo could cache the result.
 */
public VODescription getVODescription() throws CastorException,  ApplicationDescriptionNotFoundException, IOException {    
    return makeEntry();
}
/** loads template fron url, builds objects from it */
private VODescription makeTemplate() throws IOException, MarshalException, ValidationException  {
    logger.info("using " + urls.getRegistryTemplate() + " as registry template");
    InputStreamReader iStream = new InputStreamReader(urls.getRegistryTemplate().openStream());
    VODescription template = VODescription.unmarshalVODescription(iStream);
    return template;
}



    /* (non-Javadoc)
     * @see org.astrogrid.applications.component.ProvidesVODescription#getAuthorityID()
     */
    public String getAuthorityID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "RegistryEntryBuilder.getAuthorityID() not implemented");
    }
    /* (non-Javadoc)
     * @see org.astrogrid.applications.component.ProvidesVODescription#setServerID(java.lang.String)
     */
    public String setServerID(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "RegistryEntryBuilder.setServerID() not implemented");
    }
/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
 */
public String getName() {
    return "Default VODescription Provider";
}

/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
 */
public String getDescription() {
    try {
        VODescription desc = this.getVODescription();
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(desc,doc);
        StringWriter sw = new StringWriter();
        XMLUtils.PrettyDocumentToWriter(doc,sw);
        return "VODescription \n" + XMLUtils.xmlEncodeString(sw.toString());
    } catch (Exception e) {
        return "Could not display description: " + e.getMessage();
    }
    
}

/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
 */
public Test getInstallationTest() {
    return new InstallationTest("testGetDescription");
}

/** Installation test - checks that {@link RegistryEntryBuilder#getVODescription()} works correctly 
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class InstallationTest extends TestCase {

    public InstallationTest(String arg0) {
        super(arg0);
    }
    
    public void testGetDescription() throws Exception {
        VODescription desc = getVODescription();
        assertNotNull(desc);
    }

}

}