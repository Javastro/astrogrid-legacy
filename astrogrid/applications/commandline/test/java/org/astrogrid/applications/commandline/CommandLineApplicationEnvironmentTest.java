/*
 * $Id: CommandLineApplicationEnvironmentTest.java,v 1.2 2004/07/01 11:07:59 nw Exp $
 * 
 * Created on 11-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.manager.idgen.InMemoryIdGen;

import java.io.File;

import junit.framework.TestCase;

/** test behaviour of the commandline application environment.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationEnvironmentTest extends TestCase {

   protected CommandLineApplicationEnvironment env;
    protected File workingDir;
   /**
    * Constructor for ApplicationEnvironmentTest.
    * @param arg0
    */
   public CommandLineApplicationEnvironmentTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CommandLineApplicationEnvironmentTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      workingDir = File.createTempFile("CmdLineApplicationEnvironmentTest",null);
      workingDir.delete();
      workingDir.mkdir();
      assertTrue(workingDir.exists());
      workingDir.deleteOnExit();
      env = new CommandLineApplicationEnvironment(new InMemoryIdGen(),new CommandLineApplicationEnvironment.WorkingDir() {

        public File getDir() {
            return workingDir;
        }
      });
      assertNotNull(env);
      
      
   }

   final public void testGetErrorLog() {
     File f = env.getErrorLog();
     assertNotNull(f);
      
   }

   final public void testGetExecutionDirectory() {
     File f = env.getExecutionDirectory();
     assertNotNull(f);
   }

   final public void testGetExecutionId() {
     String id = env.getExecutionId();
     String id2 = env.getExecutionId();
     
     assertTrue("a particular environment should always return the same executionid - " + id  + " " + id2, id.equals(id2));
   }

   final public void testGetOutputLog() {
      File f = env.getOutputLog();
      assertNotNull(f);
   }
   
   final public void testTempFiles() {
       File f = env.getTempFile();
       assertNotNull(f);
       assertTrue(f.getParentFile().exists());
       
       File f1 = env.getTempFile();
       assertNotNull(f1);
       
       assertFalse(f.equals(f1));
   }

}
