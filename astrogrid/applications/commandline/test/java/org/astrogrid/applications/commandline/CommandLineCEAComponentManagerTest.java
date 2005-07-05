/*$Id: CommandLineCEAComponentManagerTest.java,v 1.3 2005/07/05 08:26:56 clq2 Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.astrogrid.applications.commandline.digester.CommandLineDescriptionsLoader;
import org.astrogrid.applications.component.AbstractComponentManagerTestCase;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.applications.manager.ApplicationEnvironmentRetriver;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.workflow.beans.v1.Tool;

/** Test the component manager assembled for the commandline cea is valid - i.e. has all the necessary components registered.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author pharriso@eso.org 03-Jun-2005 - make it do a "semi-live" test
 *
 */
public class CommandLineCEAComponentManagerTest extends AbstractComponentManagerTestCase {
    /**
     * Constructor for CommandLineCEAComponentManagerTest.
     * @param arg0
     */
    public CommandLineCEAComponentManagerTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    
    protected CEAComponentManager createManager() {
        return new CommandLineCEAComponentManager();
    }
  
   /* (non-Javadoc)
    * @see org.astrogrid.applications.component.AbstractComponentManagerTestCase#setupConfigComponentManager()
    */
   protected void setupConfigComponentManager() {
      basicConfig();
      URL cmdlinesetup = this.getClass().getResource("/TestApplicationConfig.xml");
      assertNotNull(cmdlinesetup);
      SimpleConfig.getSingleton().setProperty(CommandLineCEAComponentManager.DESCRIPTION_URL, cmdlinesetup);
      
   }
   
   public void testUse () throws Exception
   {
      // TODO This is only a very basic stab at a test - main intention was to be able to run the querier test.
      CommandLineDescriptionsLoader dl = (CommandLineDescriptionsLoader) manager.getContainer().getComponentInstanceOfType(CommandLineDescriptionsLoader.class);
      CommandLineApplicationDescription desc = (CommandLineApplicationDescription) dl.getDescription(TestAppConst.TESTAPP_NAME);
      assertNotNull("could not get the application description",desc);
      Toolbuilder.fixupExecutionPath(desc);
      ExecutionController cec = manager.getExecutionController();
      Tool tool = Toolbuilder.buildTool("0", desc);
      Toolbuilder.fillDirect(tool);
      String exid = cec.init(tool, "CommandLineCEAComponentManagerTest");
      cec.execute(exid);
      Thread.sleep(2000); //TODO make this a bit more robust to failures..
      
      QueryService qs = manager.getQueryService(); 
      
      File file = qs.getLogFile(exid, ApplicationEnvironmentRetriver.StdIOType.out);
      assertNotNull(file);
      BufferedReader rd = new BufferedReader(new FileReader(file));
      String line = rd.readLine();
      while(line != null)
      {
         System.err.println(line);
         line = rd.readLine();
      }
    
       
   }
   
   
}


/* 
$Log: CommandLineCEAComponentManagerTest.java,v $
Revision 1.3  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.166.2  2005/06/09 22:17:58  pah
tweaking the log getter

Revision 1.2.166.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.152.2  2005/06/08 22:10:45  pah
make http applications v10 compliant

Revision 1.2.152.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/06/17 09:24:18  nw
intermediate version

Revision 1.1.2.1  2004/06/14 08:57:48  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/