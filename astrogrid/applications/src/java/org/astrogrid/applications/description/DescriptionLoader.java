/*
 * $Id: DescriptionLoader.java,v 1.6 2003/12/04 13:26:25 pah Exp $
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
import org.apache.commons.digester.SetTopRule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.astrogrid.applications.description.exception.ParameterTypeNotDefinedException;
import org.astrogrid.applications.manager.AbstractApplicationController;

/**
 * Loads the application descriptions from the description file into a set of {@link ApplicationDescription} objects. It uses the {@link org.apache.commons.digester.Digester} to parse the XML file. 
 * The schema for these definitions is located at <a href="http://www.astrogrid.org/viewcvs/*checkout*\/astrogrid/applications/schema/AGParameterDefinition.xsd?rev=HEAD&content-type=text/plain">schema</a>
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO make namespace aware and validate the input xml
 */
public class DescriptionLoader {
   private AbstractApplicationController appController;

   private Digester digester;
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
      ApplicationDescriptionConstants.APPLICATIONCONTROLLER_ELEMENT,
         ApplicationDescriptions.class);

      digester.addObjectCreate(ApplicationDescriptionConstants.APPLICATION_ELEMENT, ApplicationDescription.class);
      // set the appropriate attributes
      digester.addSetProperties(ApplicationDescriptionConstants.APPLICATION_ELEMENT);
      digester.addCallMethod(ApplicationDescriptionConstants.EXPATH_ELEMENT, "setExecutionPath", 0);

      // add the appropriate paramter element and set its properties
      digester.addFactoryCreate(ApplicationDescriptionConstants.PARAMETER_ELEMENT, new ParameterCreationFactory());
      digester.addSetProperties(ApplicationDescriptionConstants.PARAMETER_ELEMENT);
      
      //set some extra property values from the body elements of children
      digester.addCallMethod(ApplicationDescriptionConstants.UI_NAME_ELEMENT, "setDisplayName", 0);
      digester.addRule(ApplicationDescriptionConstants.UI_DESC_ELEMENT, new NodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(ApplicationDescriptionConstants.UI_DESC_ELEMENT, new AllBodyIncElementsRule("displayDescription", true));
      // add the parameter to the list of paramters      
      digester.addSetNext(ApplicationDescriptionConstants.PARAMETER_ELEMENT, "addParameter");
      
      //deal with the interface defintions
      digester.addObjectCreate(ApplicationDescriptionConstants.INTERFACE_ELEMENT, ApplicationInterface.class);
      digester.addSetProperties(ApplicationDescriptionConstants.INTERFACE_ELEMENT);
      digester.addRule(ApplicationDescriptionConstants.INTERFACE_ELEMENT, new SetTopRuleAtStart("setApplication","org.astrogrid.applications.description.ApplicationDescription"));
      
      //input and output parameter references
      digester.addCallMethod(ApplicationDescriptionConstants.INPUT_PREFS, "addInputParameter",1);
      digester.addCallParam(ApplicationDescriptionConstants.INPUT_PREFS, 0, "ref");
      
      //input and output parameter references
      digester.addCallMethod(ApplicationDescriptionConstants.OUTPUT_PREFS, "addOutputParameter",1);
      digester.addCallParam(ApplicationDescriptionConstants.OUTPUT_PREFS, 0, "ref");
      
      
      
      digester.addSetNext(ApplicationDescriptionConstants.INTERFACE_ELEMENT, "addInterface");

      // finally add the application description to the list
      digester.addSetNext(ApplicationDescriptionConstants.APPLICATION_ELEMENT, "addDescription");
      
 
     
   }
   

   /**
    * This is the same as a {@link org.apache.commons.digester.SetTopRule} except that it does the call in the {@link #begin()} phase.
    * @author Paul Harrison (pah@jb.man.ac.uk)
    * @version $Name:  $
    * @since iteration4
    */
   private static class SetTopRuleAtStart extends SetTopRule
   {

       public SetTopRuleAtStart(String methodName, String argType) {
         super(methodName, argType);
      }
      
      /* (non-Javadoc)
       * @see org.apache.commons.digester.Rule#end()
       */
      public void end() throws Exception {
         // do nothing
      }

      /* (non-Javadoc)
       * @see org.apache.commons.digester.Rule#begin(java.lang.String, java.lang.String, org.xml.sax.Attributes)
       */
      public void begin(String arg0, String arg1, Attributes arg2) throws Exception{
        // do what normally is done at the end
        
        super.end();
      }

   }

}
