/*
 * $Id: ParameterLoader.java,v 1.1 2003/12/08 17:06:35 pah Exp $
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

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterLoader {
   
   private static final String INPUTPARAMETER_ELEMENT = "app/input/parameter";
   private Digester digester;
   private AbstractApplication application;
   
   public ParameterLoader(AbstractApplication application)
   {
      this.application = application;
      try {
         createDigester();
      }
      catch (ParserConfigurationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private void createDigester() throws ParserConfigurationException{
      digester = new Digester();
      digester.setValidating(false);
      digester.addFactoryCreate(INPUTPARAMETER_ELEMENT, new ParameterFactory(application.getApplicationDescription()));
      digester.addSetNext(INPUTPARAMETER_ELEMENT, "addParameter");
      digester.addRule(INPUTPARAMETER_ELEMENT, new NodeCreateRule(Node.ELEMENT_NODE));
      digester.addRule(INPUTPARAMETER_ELEMENT, new AllBodyIncElementsRule("rawValue", false));
      
   }
   
   public boolean loadParamters(String parameterspec)
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
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (SAXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      
      return success;
      
   }

}
