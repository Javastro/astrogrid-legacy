/*
 * $Id: AbstractMultiSourceIndirectParameterWorkflow.java,v 1.1 2004/10/06 13:33:31 pah Exp $
 * 
 * Created on 30-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.externaldep.commandline;

import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 30-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractMultiSourceIndirectParameterWorkflow extends
      AbstractTestForWorkflow {
   
   final static String[] locationPrefixes = new String[]{"http://www.jb.man.ac.uk/~pah/testdata/","ftp://ftp.jb.man.ac.uk/pub/pah/testdata/"};
   private Ivorn resultLocationBase;

   /**
    * @param applications
    * @param arg0
    */
   public AbstractMultiSourceIndirectParameterWorkflow(String[] applications,
         String arg0) {
      super(applications, arg0);
      
   }

   protected void setUp() throws Exception {
     super.setUp();
     String classname=this.getClass().getName();
     classname = classname.substring(classname.lastIndexOf('.')+1);
     resultLocationBase = createIVORN("/"+classname+"_result_");
   }
   /* (non-Javadoc)
    * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
    */
   protected final void buildWorkflow() throws Exception {
      
      wf.setName(this.getClass().getName());
      for (int i = 0; i < locationPrefixes.length; i++) {
         Step step = buildStep(locationPrefixes[i], resultLocationBase.toString()+i);
         wf.getSequence().addActivity(step);
      }
   }

   /**
    * create a step to run the application using the location as a prefix from which the data should be loaded.
    * @param location
    * @param resultLocation the location prefix for the results of running the tool
    * @return
    */
   protected abstract Step buildStep(String location, String resultLocation);
}
