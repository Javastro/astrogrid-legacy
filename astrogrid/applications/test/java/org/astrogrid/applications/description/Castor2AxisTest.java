/*
 * $Id: Castor2AxisTest.java,v 1.1 2004/03/29 12:38:12 pah Exp $
 * 
 * Created on 26-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.io.StringReader;
import java.io.StringWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.astrogrid.applications.manager.WorkFlowUsingTestCase;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 26-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class Castor2AxisTest extends WorkFlowUsingTestCase {

   /**
    * Constructor for Castor2AxisTest.
    * @param arg0
    */
   public Castor2AxisTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(Castor2AxisTest.class);
   }

   /*
    * @see WorkFlowUsingTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }
   
   public void testCastor2AxisTool() throws MarshalException, ValidationException
   {
      _tool axistool = Castor2Axis.convert(tool);
      assertNotNull(axistool);
      Tool castortool = Axis2Castor.convert(axistool);
      assertNotNull(castortool);
      
      StringWriter writer = new StringWriter();
      tool.marshal(writer);
      
//      System.out.println(writer.toString());
//      System.out.println("**");

      StringWriter writer2 = new StringWriter();
      castortool.marshal(writer2);
//      System.out.println(writer.toString());
      
      
      
      assertTrue(writer.toString().equals(writer2.toString()));
   }
   
   
   public void testCastor2AxisApplicationList()
   {
   }

}
