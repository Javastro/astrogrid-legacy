/*
 * $Id: WorkFlowUsingTestCase.java,v 1.3 2004/03/23 19:46:04 pah Exp $
 * 
 * Created on 18-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.io.InputStream;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;


import junit.framework.TestCase;
import junit.framework.TestFailure;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 18-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class WorkFlowUsingTestCase extends BaseDBTestCase {


   protected Workflow workflow;
   protected Tool tool;
   /**
    * 
    */
   public WorkFlowUsingTestCase() {
      this("applications workflow");
   }

   /**
    * @param arg0
    */
   public WorkFlowUsingTestCase(String arg0) {
      super(arg0);
      
      InputStream is = this.getClass().getResourceAsStream("/testworkflow.xml");
      InputSource source = new InputSource(is);
      try {
         workflow = (Workflow)Unmarshaller.unmarshal(Workflow.class, source);
         assertNotNull(workflow);
         workflow.validate();
         tool = (Tool)workflow.findXPathValue("//tool"); // there should only be one!
         assertNotNull(tool);
        
      }
      catch (MarshalException e) {
         logger.error("cannot unmarshall the testworkflow", e);
         fail("cannot unmarshall the testworkflow" + e);
      }
      catch (ValidationException e) {
            fail("cannot validate the test workflow");
      }
      
      
   }

   /** 
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}
