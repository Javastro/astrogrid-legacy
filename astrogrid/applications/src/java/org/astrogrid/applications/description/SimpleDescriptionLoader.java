/*
 * $Id: SimpleDescriptionLoader.java,v 1.3 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 04-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.astrogrid.applications.manager.AbstractApplicationController;

/**
 * Reads the application config file and returns a series of simple application descriptions.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @deprecated replaced by the new common object model/registry mechanisms
 * @TODO this needs to be replaced by the new common object model/registry mechanisms
 */
public class SimpleDescriptionLoader {
   /**
    * @author Paul Harrison (pah@jb.man.ac.uk)
    * @version $Name:  $
    * @since iteration4
    */
   private static class AppDiscFactory extends AbstractObjectCreationFactory {

      /* (non-Javadoc)
       * @see org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
       */
      public Object createObject(Attributes attr) throws Exception {
         String name;
         SimpleApplicationDescription sd = new SimpleApplicationDescription();
         if ((name=attr.getValue(ApplicationDescriptionConstants.NAME_ATTR) )!= null) {
            sd.setName(name);
         }
         return sd;
       }

   }
   private AbstractApplicationController appController;

   private Digester digester;
   
   public SimpleDescriptionLoader(AbstractApplicationController ac)
   {
      appController=ac;
      try {
         createDigester();
      }
      catch (ParserConfigurationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }

   /**
    * 
    */
   private void createDigester() throws ParserConfigurationException {
      digester = new Digester();
      digester.setValidating(false);
      digester.addFactoryCreate(ApplicationDescriptionConstants.APPLICATION_ELEMENT, new AppDiscFactory());
      digester.addSetNext(ApplicationDescriptionConstants.APPLICATION_ELEMENT, "addSimpleDescription");
      digester.addRule(ApplicationDescriptionConstants.APPLICATION_ELEMENT, new NodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(ApplicationDescriptionConstants.APPLICATION_ELEMENT, new AllBodyIncElementsRule("xmlDescriptor", false));

   }
   
   public boolean loadDescription(URL configFile) {
      boolean success = false;
      try {
         digester.clear();
//       push onto the digester stack so that the addSimpleDescription method can be called
         digester.push(appController); 
         digester.parse(configFile.openStream());
         success = true;
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         success = false;
      }
      catch (SAXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         success = false;
      }
       return success;
   }




}
