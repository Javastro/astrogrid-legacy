/*$Id: AbstractTestForSimpleWorkflow.java,v 1.4 2004/11/19 14:17:56 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

/** Abstract base class for tests that fire a single-step workflow into jes.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public abstract class AbstractTestForSimpleWorkflow extends AbstractTestForWorkflow {
    /** Construct a new AbstractTestForSimpleWorkflow
     * @param arg0
     */
    public AbstractTestForSimpleWorkflow(ServerInfo info,String arg0) {
        super(new String[]{info.getApplicationName()}, arg0);
        this.info = info;      
    }
    protected final ServerInfo info;
    
    /** build a simple one-step workflow */
      protected void buildWorkflow() throws WorkflowInterfaceException {
          wf.setName(this.getClass().getName());
          ApplicationDescription descr = reg.getDescriptionFor(info.getApplicationName());
          assertNotNull("could not get application description", descr);
          Tool tool = descr.createToolFromDefaultInterface();
          assertNotNull("tool is null", tool);
          configureToolParameters(tool);
          try {
              descr.validate(tool);
          }
          catch (ToolValidationException e) {
              softFail("Tool didn't validate" + e.getMessage());
          }
          // add a step to the workflow.
          Step step = new Step();
          step.setDescription("single step");
          step.setName("test step");
          step.setTool(tool);
          step.setResultVar("resultVar"); // needs to be set if we want to get results back.
          wf.getSequence().addActivity(step);
      }

    /**
     * @param tool
     */
    protected abstract void configureToolParameters(Tool tool);
    
}


/* 
$Log: AbstractTestForSimpleWorkflow.java,v $
Revision 1.4  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.2  2004/08/22 01:43:18  nw
improved concurrent behaviour

Revision 1.1  2004/07/01 11:47:39  nw
cea refactor
 
*/