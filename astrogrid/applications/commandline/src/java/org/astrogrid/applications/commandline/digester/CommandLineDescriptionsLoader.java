/*
 * $Id: CommandLineDescriptionsLoader.java,v 1.8 2005/07/05 08:27:01 clq2 Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline.digester;


import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.apache.commons.digester.RegexRules;
import org.apache.commons.digester.SimpleRegexMatcher;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.common.bean.v1.Namespaces;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/** ApplicationDescrptions Library for use with commandline apps.
 * Loads the application descriptions from the description file into a set of {@link ApplicationDescription} objects. It uses the {@link org.apache.commons.digester.Digester} to parse the XML file. 
 * The schema for these definitions is located at <a href="http://www.astrogrid.org/viewcvs/%2Acheckout%2A/astrogrid/applications/schema/AGParameterDefinition.xsd?rev=HEAD&content-type=text/plain">schema</a>
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO validate the input xml
 * @TODO really better to use castor, but it deos not do substitution groups well... 
 * @TODO perhaps better to refactor the schema into the commandline project and rename elements to be closer to field names.
 */
public class CommandLineDescriptionsLoader extends BaseApplicationDescriptionLibrary {
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CommandLineDescriptionsLoader.class);
   private Exception waserror = null;

    public interface DescriptionURL {
        URL getURL();
    }


   public CommandLineDescriptionsLoader(DescriptionURL finder, CommandLineApplicationDescriptionFactory appDescFactory, ApplicationDescriptionEnvironment envin) 
   {
          super(envin);
          this.configFile = finder.getURL();
          this.appDescFactory = appDescFactory;
          try {
            loadDescription();
         } catch (ApplicationDescriptionNotLoadedException e) {
            logger.warn("problem loading commandline application definitions", e);
            waserror = e;
         }
   }
   protected final URL configFile;
   protected final CommandLineApplicationDescriptionFactory appDescFactory; 
   
   public final void loadDescription() throws ApplicationDescriptionNotLoadedException {
      logger.info( "loading application descriptions from " + configFile.toString());

      try {
         Digester digester = createDigester();         
         digester.parse(configFile.openStream());              
         if(getApplicationNames().length == 0 ) {
            throw new ApplicationDescriptionNotLoadedException("0 descriptions loaded from "+configFile.toString());          
         } 
      }
      catch (Exception e) {
         throw new ApplicationDescriptionNotLoadedException("failed to load descriptions from "+configFile.toString(),e);
      }
      
   }
   /**
    * Creates the digester suitable for reading the application description file. The correct funtioning ot this method is highly dependent on the details of the schema.
    * @return
    */
   private Digester createDigester() throws ParserConfigurationException {

      Digester digester = new Digester();
      digester.setValidating(false); //TODO would be better to make this validate...validation does get done later though...
      digester.setNamespaceAware(true);
//      digester.setRules(new RegexRules(new SimpleRegexMatcher())); // to allow matches on any children....

    digester.push(this);
    digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
      //digester.addObjectCreate(ApplicationDescriptionConstants.APPLICATION_ELEMENT, CommandLineApplicationDescription.class);
      digester.addFactoryCreate(CommandLineApplicationDescriptionsConstants.APPLICATION_ELEMENT,appDescFactory);
      // set the appropriate attributes
      digester.addSetProperties(CommandLineApplicationDescriptionsConstants.APPLICATION_ELEMENT);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.EXPATH_ELEMENT, "setExecutionPath", 0);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.LONGNAME_ELEMENT, "setUIName", 0);      
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.DESCRIPTION_ELEMENT, "setAppDescription", 0);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.URL_ELEMENT, "setReferenceURL", 0);

      // add the appropriate paramter element and set its properties
      digester.addObjectCreate(CommandLineApplicationDescriptionsConstants.PARAMETER_ELEMENT,CommandLineParameterDescription.class);
      digester.addSetProperties(CommandLineApplicationDescriptionsConstants.PARAMETER_ELEMENT,"type","typeString");
      
      //set some extra property values from the body elements of children
      digester.setRuleNamespaceURI(Namespaces.CEAPD);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.UI_NAME_ELEMENT, "setDisplayName", 0);
      digester.addRule(CommandLineApplicationDescriptionsConstants.UI_DESC_ELEMENT, new NonPoppingNodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(CommandLineApplicationDescriptionsConstants.UI_DESC_ELEMENT, new AllBodyIncElementsRule("displayDescription", true));
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.UCD_ELEMENT, "setUcd", 0);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.DEFVAL_ELEMENT, "setDefaultValue", 0);
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.UNITSL_ELEMENT, "setUnits", 0);
      
           
      // add the parameter to the list of paramters  (could perhaps be put above)
      digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
      digester.addSetNext(CommandLineApplicationDescriptionsConstants.PARAMETER_ELEMENT, "addParameterDescription");

      digester.setRuleNamespaceURI(Namespaces.CEAB);           
      digester.addFactoryCreate(CommandLineApplicationDescriptionsConstants.INTERFACE_ELEMENT,new BaseApplicationInterfaceFactory());

      digester.addSetProperties(CommandLineApplicationDescriptionsConstants.INTERFACE_ELEMENT);      
      //input and output parameter references
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.INPUT_PREFS, "addInputParameterAsPref", 1, new Class[]{ParameterRef.class});
      digester.addObjectCreate(CommandLineApplicationDescriptionsConstants.INPUT_PREFS, ParameterRef.class);
      digester.addSetProperties(CommandLineApplicationDescriptionsConstants.INPUT_PREFS);
      digester.addCallParam(CommandLineApplicationDescriptionsConstants.INPUT_PREFS, 0, true);
      
      
      //input and output parameter references
      digester.addCallMethod(CommandLineApplicationDescriptionsConstants.OUTPUT_PREFS, "addOutputParameterAsPref", 1, new Class[]{ParameterRef.class});
      digester.addObjectCreate(CommandLineApplicationDescriptionsConstants.OUTPUT_PREFS, ParameterRef.class);
      digester.addSetProperties(CommandLineApplicationDescriptionsConstants.OUTPUT_PREFS);
      digester.addCallParam(CommandLineApplicationDescriptionsConstants.OUTPUT_PREFS, 0, true);
     
      
      
      digester.addSetNext(CommandLineApplicationDescriptionsConstants.INTERFACE_ELEMENT, "addInterface");

      // finally add the application description to the list
     digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
     digester.addSetNext(CommandLineApplicationDescriptionsConstants.APPLICATION_ELEMENT, "addApplicationDescription");
      return digester;
 
     
   }
   


   
   private static class BaseApplicationInterfaceFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes arg0) throws Exception {
        String name = "**unknown";
        if (arg0.getValue(CommandLineApplicationDescriptionsConstants.NAME_ATTR) != null) {
            name = arg0.getValue(CommandLineApplicationDescriptionsConstants.NAME_ATTR);
        }
        ApplicationDescription appDescription = (ApplicationDescription)this.digester.peek(); // i.e. the description we want is at the top of the stack.
        return new CommandLineApplicationInterface(name,appDescription);
    }
   }
   
   

   /* (non-Javadoc)
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
    */
   public String getDescription() {
      StringBuffer sb = new StringBuffer();
      // notifiy that there was an error if it has occured.
      if(waserror != null)
      {
         StringWriter sw = new StringWriter();
         sb.append("Error reading descriptions - might not be complete\n" + waserror.toString());
         Throwable cause;
         if(( cause = waserror.getCause())!= null)
         {
            sb.append("\n");
            sb.append(cause.toString());
         }
      }
      sb.append("\n");
      sb.append(super.getDescription());
      return sb.toString();
      
 
   }
   
}
