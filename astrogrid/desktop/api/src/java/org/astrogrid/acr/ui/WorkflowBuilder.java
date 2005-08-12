/*$Id: 
 * Created on 28-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

/** Interface to the Workflow Viewer / Builder UI
 * <p>
 * <img src="doc-files/wfbuilder.png">
 * @service userInterface.workflowViewer
 * @author Phil Nicolson pjn3@star.le.ac.uk
 *@see org.astrogrid.acr.ui.JobMonitor
 *@see org.astrogrid.acr.astrogrid.Jobs
 */
public interface WorkflowBuilder {
    /** show the workflow builder UI */
	public void show();
    
    /** hide the workflow builder UI */
    public void hide();
}
