/*
 * $Id: RegistryEntryBuilder.java,v 1.2 2004/03/29 12:35:12 pah Exp $
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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.cea.CeaApplicationType;
import org.astrogrid.registry.beans.cea.CeaServiceType;
import org.astrogrid.registry.beans.cea.ManagedApplications;
import org.astrogrid.registry.beans.cea.Parameters;
import org.astrogrid.registry.beans.resource.AccessURLType;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.beans.resource.VOResource;
import org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType;

/**
 * Creates a bean based description of the CEA entries for a CommonExecutionController instance. 
 * Requires a template file of the VODescription basic CeaApplication and CeaService entries to fill in.
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryEntryBuilder {

   private URL endpoint;

   private VODescription VODesctemplate;

   private ApplicationList applist;

   /**
    * @param apps
    * @param template
    * @param serviceEndpoint
    */
   public RegistryEntryBuilder(
      ApplicationList apps,
      VODescription template,
      URL serviceEndpoint) {

      this.applist = apps;
      this.VODesctemplate = template;
      this.endpoint = serviceEndpoint;
   }

   /**
    * Create the registry entry....
    * @return
    */
   public VODescription makeEntry() throws MarshalException, ValidationException {
      VODescription vodesc = new VODescription();
      CeaApplicationType applicationTemplate =
         (CeaApplicationType)VODesctemplate.getResource(0);
      CeaServiceType serviceTemplate = (CeaServiceType)VODesctemplate.getResource(1);

      CeaServiceType service = cloneTemplate(serviceTemplate);
      ManagedApplications managedApplications = new ManagedApplications();
      service.setManagedApplications(managedApplications);
      
      //add each of the application definitions.
      for (int i = 0; i < applist.getApplicationDefnCount(); i++) {

         ApplicationBase theapp = applist.getApplicationDefn(i);
         CeaApplicationType appentry = makeApplicationEntry(applicationTemplate, theapp);
         vodesc.addResource(appentry);
         //add this application to the list of managed applications.
         managedApplications.addApplicationReference(appentry.getIdentifier());

      }
      //add the service description
      AccessURLType accessurl = new AccessURLType();
      accessurl.setContent(endpoint.toString()) ;     
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
   CeaApplicationType makeApplicationEntry(
      CeaApplicationType template,
      ApplicationBase app)
      throws MarshalException, ValidationException {

      CeaApplicationType entry = cloneTemplate(template);
      entry.getIdentifier().setResourceKey(app.getName());
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

   /**
    * Create a clone of the given object. Does this by using the castor marshalling/unmarshalling on the object.
    * @param app
    * @return
    * @throws MarshalException
    * @throws ValidationException
    */
   CeaApplicationType cloneTemplate(CeaApplicationType app)
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
   CeaServiceType cloneTemplate(CeaServiceType serv)
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
    * Make an ApplicationList from a full configuration. This is a not a deep copy - references to the applicationDescription objects are placed in the ApplicationList
    * @param clecConfig a configuration for a command line execution controller
    * @return
    * @TODO might be better to refactor the original schema so that there was a base type for the common execution contoller configs...
    */
   public static ApplicationList makeApplist(CommandLineExecutionControllerConfig clecConfig) {
      ApplicationList result = new ApplicationList();

      for (int i = 0; i < clecConfig.getApplicationCount(); i++) {
         result.addApplicationDefn(clecConfig.getApplication(i));
      }
      return result;
   }
}