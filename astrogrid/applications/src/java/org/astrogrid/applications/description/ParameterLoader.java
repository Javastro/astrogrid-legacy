/*
 * $Id: ParameterLoader.java,v 1.6 2004/04/01 09:53:02 pah Exp $
 * 
 * Created on 08-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;


import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.description.exception.ParameterValuesParseError;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterLoader {
   
   //treat input and output parameters just the same for now - the information as to what they are is in the interface definition
   //TODO should treat them separately to do better error check of the submitted parameter specifications
   private static final String INPUTPARAMETER_ELEMENT = "*/parameter";
   private Digester digester;
   private AbstractApplication application;
   
   public ParameterLoader(AbstractApplication application) throws ParameterValuesParseError
   {
      this.application = application;
      try {
         createDigester();
      }
      catch (ParserConfigurationException e) {
         throw new ParameterValuesParseError("problem creating the parameter value digester", e);
      }
   }

   private void createDigester() throws ParserConfigurationException{
      digester = new Digester();
      digester.setValidating(false);
      digester.addFactoryCreate(INPUTPARAMETER_ELEMENT, new ParameterFactory(application));
      digester.addSetNext(INPUTPARAMETER_ELEMENT, "addParameter");
      digester.addRule(INPUTPARAMETER_ELEMENT, new NodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(INPUTPARAMETER_ELEMENT, new AllBodyIncElementsRule("rawValue", true));
      
   }
   
   public boolean loadParameters(String parameterspec) throws ParameterValuesParseError
   {
      boolean success = false;
      
      try {
         digester.clear();
         digester.push(application);
         StringReader sb = new StringReader(parameterspec);
         digester.parse(new InputSource(sb));
         success=true;
      }
      catch (IOException e) {
         throw new ParameterValuesParseError("error loading parameter values", e);
      }
      catch (SAXException e) {
         throw new ParameterValuesParseError("error loading parameter values", e);
     }
      
      
      return success;
      
   }

}
