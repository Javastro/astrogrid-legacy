/*
 * $Id: CommandLineDescriptionsLoader.java,v 1.15 2007/03/01 18:28:27 gtr Exp $
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
import org.astrogrid.applications.beans.v1.parameters.OptionList;
import org.astrogrid.applications.commandline.CommandLineConfiguration;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.common.bean.v1.Namespaces;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;

/** ApplicationDescrptions Library for use with commandline apps.
 * Loads the application descriptions from the description file into a 
 * set of {@link ApplicationDescription} objects. It uses the 
 * {@link org.apache.commons.digester.Digester} to parse the XML file. 
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
  
  // Paths in the XML structure of the application-description file.
  private static final String NAME_ATTR = "name";
  private static final String APPLICATIONCONTROLLER_ELEMENT = "CommandLineExecutionControllerConfig";
  private static final String APPLICATION_ELEMENT = APPLICATIONCONTROLLER_ELEMENT + "/Application";
  private static final String EXPATH_ELEMENT = APPLICATION_ELEMENT + "/ExecutionPath";
  private static final String LONGNAME_ELEMENT = APPLICATION_ELEMENT + "/LongName";
  private static final String DESCRIPTION_ELEMENT = APPLICATION_ELEMENT + "/Description";
  private static final String URL_ELEMENT = APPLICATION_ELEMENT + "/ReferenceURL";
  private static final String PARAMETER_ELEMENT = APPLICATION_ELEMENT+"/Parameters/CmdLineParameterDefn";
  private static final String UI_NAME_ELEMENT = PARAMETER_ELEMENT + "/UI_Name";
  private static final String UI_DESC_ELEMENT = PARAMETER_ELEMENT + "/UI_Description";
  private static final String UI_DESC_CHILDREN = PARAMETER_ELEMENT + "/UI_Description/*";
  private static final String UCD_ELEMENT = PARAMETER_ELEMENT + "/UCD";
  private static final String DEFVAL_ELEMENT = PARAMETER_ELEMENT + "/DefaultValue";
  private static final String UNITSL_ELEMENT = PARAMETER_ELEMENT + "/Units";
  private static final String INTERFACE_ELEMENT= APPLICATION_ELEMENT + "/Interfaces/Interface";
  private static final String INPUT_PREFS = INTERFACE_ELEMENT + "/input/pref";
  private static final String OUTPUT_PREFS = INTERFACE_ELEMENT + "/output/pref";
  private static final String OPTIONLIST_ELEMENT = PARAMETER_ELEMENT + "/OptionList";
  private static final String OPTIONVAL_ELEMENT = OPTIONLIST_ELEMENT + "/OptionVal";
   
  private Exception waserror = null;

    public interface DescriptionURL {
        URL getURL();
    }


   public CommandLineDescriptionsLoader(CommandLineConfiguration configuration, 
                                        CommandLineApplicationDescriptionFactory appDescFactory, 
                                        ApplicationDescriptionEnvironment envin) 
   {
     super(envin);
     try {
       this.configFile = configuration.getApplicationDescriptionUrl();
       this.appDescFactory = appDescFactory;
       this.loadDescription();
     } catch (Exception e) {
       logger.warn("Can't load the command-line application-description file.", e);
       waserror = e;
     }
   }
   protected URL configFile;
   protected CommandLineApplicationDescriptionFactory appDescFactory; 
   
  /**
   * Loads the application descriptions from file.
   * Runs the pre-configurured digester on the input stream of the 
   * app-description.xml file.
   */
  public final void loadDescription() 
      throws ApplicationDescriptionNotLoadedException {
    logger.info("Loading application descriptions from " + configFile.toString());

    try {
      Digester digester = createDigester();         
      digester.parse(configFile.openStream());
    }
    catch (Exception e) {
      throw new ApplicationDescriptionNotLoadedException("failed to load descriptions from "+configFile.toString(),e);
    }
                    
    if (getApplicationNames().length == 0 ) {
      throw new ApplicationDescriptionNotLoadedException("0 descriptions loaded from "+configFile.toString());          
    } 
  }
  
  /**
   * Creates the digester suitable for reading the application description file. 
   * The correct funtioning of this method is highly dependent on the details of the schema.
   * @return
   */
  private Digester createDigester() throws ParserConfigurationException {

    Digester digester = new Digester();
    digester.setValidating(false); //TODO would be better to make this validate...validation does get done later though...
    digester.setNamespaceAware(true);

    // At the outermost level, objects created by the digester are added to
    // this current object.
    digester.push(this);
    digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
    digester.addFactoryCreate(APPLICATION_ELEMENT,appDescFactory);
    
    // set the appropriate attributes
    digester.addSetProperties(APPLICATION_ELEMENT);
    digester.addCallMethod(EXPATH_ELEMENT,      "setExecutionPath",  0);
    digester.addCallMethod(LONGNAME_ELEMENT,    "setUIName",         0);      
    digester.addCallMethod(DESCRIPTION_ELEMENT, "setAppDescription", 0);
    digester.addCallMethod(URL_ELEMENT,         "setReferenceURL",   0);

    // add the appropriate paramter element and set its properties
    digester.addObjectCreate(PARAMETER_ELEMENT,CommandLineParameterDescription.class);
    digester.addSetProperties(PARAMETER_ELEMENT,"type","typeString");
      
    //set some extra property values from the body elements of children
    digester.setRuleNamespaceURI(Namespaces.CEAPD);
    digester.addCallMethod(UI_NAME_ELEMENT, "setDisplayName", 0);
    digester.addRule(UI_DESC_ELEMENT, new NonPoppingNodeCreateRule(Node.ELEMENT_NODE));
    digester.addRule(UI_DESC_ELEMENT, new AllBodyIncElementsRule("displayDescription", true));
    digester.addCallMethod(UCD_ELEMENT,    "setUcd",          0);
    digester.addCallMethod(DEFVAL_ELEMENT, "setDefaultValue", 0);
    digester.addCallMethod(UNITSL_ELEMENT, "setUnits",        0);
      
    // Add the option list to the parameter.
    digester.setRuleNamespaceURI(Namespaces.CEAPD);
    digester.addObjectCreate(OPTIONLIST_ELEMENT, OptionList.class);
    digester.addCallMethod(OPTIONVAL_ELEMENT, "addOptionVal", 0);
      
           
    // add the parameter to the list of paramters  (could perhaps be put above)
    digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
    digester.addSetNext(PARAMETER_ELEMENT, "addParameterDescription");

    digester.setRuleNamespaceURI(Namespaces.CEAB);           
    digester.addFactoryCreate(INTERFACE_ELEMENT,new BaseApplicationInterfaceFactory());

    digester.addSetProperties(INTERFACE_ELEMENT);      
      
    //input and output parameter references
    digester.addCallMethod(INPUT_PREFS, "addInputParameterAsPref", 1, new Class[]{ParameterRef.class});
    digester.addObjectCreate(INPUT_PREFS, ParameterRef.class);
    digester.addSetProperties(INPUT_PREFS);
    digester.addCallParam(INPUT_PREFS, 0, true);
      
      
    //input and output parameter references
    digester.addCallMethod(OUTPUT_PREFS, "addOutputParameterAsPref", 1, new Class[]{ParameterRef.class});
    digester.addObjectCreate(OUTPUT_PREFS, ParameterRef.class);
    digester.addSetProperties(OUTPUT_PREFS);
    digester.addCallParam(OUTPUT_PREFS, 0, true);

    digester.addSetNext(INTERFACE_ELEMENT, "addInterface");

    // finally add the application description to the list
    digester.setRuleNamespaceURI(Namespaces.CEAIMPL);
    digester.addSetNext(APPLICATION_ELEMENT, "addApplicationDescription");
      
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
