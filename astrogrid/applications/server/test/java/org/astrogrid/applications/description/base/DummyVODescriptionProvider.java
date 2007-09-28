/*
 * $Id: DummyVODescriptionProvider.java,v 1.10 2007/09/28 18:03:36 clq2 Exp $
 * Created on 02-Jun-2004
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *  
 */
package org.astrogrid.applications.description.base;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.manager.MetadataService;
import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.registry.beans.v10.resource.Contact;
import org.astrogrid.registry.beans.v10.resource.Content;
import org.astrogrid.registry.beans.v10.resource.Creator;
import org.astrogrid.registry.beans.v10.resource.Curation;
import org.astrogrid.registry.beans.v10.resource.Resource;
import org.astrogrid.registry.beans.v10.resource.ResourceName;
import org.astrogrid.registry.beans.v10.resource.Source;
import org.astrogrid.registry.beans.v10.resource.types.ContentLevel;
import org.astrogrid.registry.beans.v10.resource.types.Type;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;

//TODO would probably be better just to read in a dummy instance document....easier next time the schema changes...
public class DummyVODescriptionProvider implements MetadataService {
   private final String AUTH_ID;

   private final String RES_KEY;

   public DummyVODescriptionProvider(String AUTH_ID, String RES_KEY) {
      super();
      this.AUTH_ID = AUTH_ID;
      this.RES_KEY = RES_KEY;
   }

   public VOResources getVODescription() throws Exception {
      // right, this is the minimal valid vodescription. hope the schema doesn't
      // change again soon! It did!!
      VOResources desc = new VOResources();
      Resource resource = new Resource();
      desc.addResource(resource);
      Identifier id = new Identifier();

      resource.setIdentifier("ivo://" + AUTH_ID + "/" + RES_KEY);
      resource.setTitle("test resource");
      Content content = new Content();
      content.addContentLevel(ContentLevel.VALUE_0);
      content.setDescription("test application description");
      content.setReferenceURL("");
      Source source = new Source();
      source.setContent("");
      content.setSource(source);
      content.addType(Type.BASICDATA);
      content.addSubject("");
      resource.setContent(content);
      Curation curation = new Curation();
      Contact contact = new Contact();
      ResourceName name = new ResourceName();
      name.setContent("name");
      contact.setName(name);
      contact.setAddress("address");
      contact.setEmail("email");
      curation.setContact(contact);
      Creator creator = new Creator();
      creator.setName(name);
      curation.setCreator(creator);
      
      curation.addContributor(name);
      curation.setPublisher(name);
      curation.setVersion("dummy");
      resource.setCuration(curation);

      assert desc.isValid() : "Dummy VODescription is not valid";
      return desc;
   }
   
   public URL getRegistrationTemplate() {
     return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.component.ProvidesVOResources#getAuthorityID()
    */
   public String getAuthorityID() {
      return AUTH_ID;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.component.ProvidesVODescription#setServerID(java.lang.String)
    */
   public String setServerID(String id) {
      // TODO implement this and refactor the test that uses it...
      throw new UnsupportedOperationException(
            "DummyVODescriptionProvider.setServerID() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.MetadataService#returnRegistryEntry()
    */
   public Document returnRegistryEntry() throws CeaException {
      try {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = builder.newDocument();
         Marshaller marshaller = new Marshaller(doc);
         marshaller.marshal(this.getVODescription());
         return doc;
         } catch (Exception e) {
        
        throw new CeaException("problem returning registry entry", e);
      }
   }
   
   /**
    * Reveals the IVORNs for the supported applications.
    */
   public String[] getApplicationIvorns() {
     String[] names = new String[1];
     names[0] = "ivo://" + AUTH_ID + "/" + RES_KEY;
     return names;
   }
}

/*
 * $Log: DummyVODescriptionProvider.java,v $
 * Revision 1.10  2007/09/28 18:03:36  clq2
 * apps_gtr_2303
 *
 * Revision 1.9.52.1  2007/08/29 14:31:03  gtr
 * VOSI support.
 *
 * Revision 1.9.44.1  2007/06/07 13:20:00  gtr
 * I added the getApplicationIvorns() method.
 *
 * Revision 1.9  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.7  2006/03/07 21:45:27  clq2
 * gtr_1489_cea
 *
 * Revision 1.4.38.1  2005/12/21 14:44:35  gtr
 * Changed to make the registration template available through the InitServlet.
 *
 * Revision 1.4  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.3.120.1  2005/06/09 08:47:33  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.3.106.2  2005/06/02 14:57:29  pah
 * merge the ProvidesVODescription interface into the MetadataService interface
 *
 * Revision 1.3.106.1  2005/05/31 12:58:26  pah
 * moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"
 * Revision 1.3 2004/08/28 07:17:34
 * pah commandline parameter passing - unit tests ok
 * 
 * Revision 1.2 2004/07/01 11:16:22 nw merged in branch
 * nww-itn06-componentization
 * 
 * Revision 1.1.2.1 2004/06/14 08:56:58 nw factored applications into
 * sub-projects, got packaging of wars to work again
 *  
 */