/*
 * $Id: DescriptionLoader.java,v 1.3 2003/11/29 00:50:14 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.digester.NodeCreateRule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.astrogrid.applications.description.exception.ParameterTypeNotDefinedException;
import org.astrogrid.applications.manager.AbstractApplicationController;

/**
 * Loads the application descriptions from the description file into a set of {@link ApplicationDescription} objects. It uses the {@link org.apache.commons.digester.Digester} to parse the XML file. 
 * The schema for these definitions is located at 
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionLoader {
   private AbstractApplicationController appController;

   private Digester digester;
   private static final String APPLICATIONCONTROLLER_ELEMENT =
      "ApplicationController";
   private static final String APPLICATION_ELEMENT =
      "ApplicationController/Application";
   private static final String PARAMETER_ELEMENT =
      "ApplicationController/Application/Parameters/Parameter";
   private static final String UI_NAME_ELEMENT = PARAMETER_ELEMENT + "/UI_NAME";
   private static final String UI_DESC_ELEMENT = PARAMETER_ELEMENT + "/UI_Description";
   private static final String UI_DESC_CHILDREN = PARAMETER_ELEMENT + "/UI_Description/?";
   private static final String UCD_ELEMENT = PARAMETER_ELEMENT + "/UCD";
   private static final String DEFVAL_ELEMENT = PARAMETER_ELEMENT + "/DefaultValue";
   private static final String UNITSL_ELEMENT = PARAMETER_ELEMENT + "/Units";

   public DescriptionLoader(AbstractApplicationController ac) {
      appController = ac;
      try {
         createDigester();
      }
      catch (ParserConfigurationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   public boolean loadDescription(File configFile) {
      ApplicationDescriptions descriptions = null;
      boolean success = false;
      try {
         digester.clear();
         descriptions = (ApplicationDescriptions)digester.parse(configFile);
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
      appController.setApplicationDescriptions(descriptions);
      return success;
   }
   /**
    * Creates the digester suitable for reading the application description file. The correct funtioning ot this method is highly dependent on the details of the schema.
    * @return
    */
   private void createDigester() throws ParserConfigurationException {
      //create the digester object IMPL don't do it like this!!! just set the main digester

      digester = new Digester();
      digester.setValidating(false); //TODO would be better to make this validate...
      digester.setRules(new ExtendedBaseRules()); // to allow matches on any children....
      
      digester.addObjectCreate(
         APPLICATIONCONTROLLER_ELEMENT,
         ApplicationDescriptions.class);

      digester.addObjectCreate(APPLICATION_ELEMENT, ApplicationDescription.class);
      // set the appropriate attributes
      digester.addSetProperties(APPLICATION_ELEMENT);

      // add the appropriate paramter element and set its properties
      digester.addFactoryCreate(PARAMETER_ELEMENT, new ParameterCreationFactory());
//      digester.addObjectCreate(PARAMETER_ELEMENT, IntParameterDescription.class);
      digester.addSetProperties(PARAMETER_ELEMENT);
      
      //set some extra property values from the body elements of children
      digester.addCallMethod(UI_NAME_ELEMENT, "setDisplayName", 0);
      digester.addRule(UI_DESC_ELEMENT, new NodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(UI_DESC_ELEMENT, new AllBodyIncElementsRule("displayDescription"));
      // add the parameter to the list of paramters      
      digester.addSetNext(PARAMETER_ELEMENT, "addParameter");

      // finally add the application description to the list
      digester.addSetNext(APPLICATION_ELEMENT, "addDescription");

     
   }
   
   

}
