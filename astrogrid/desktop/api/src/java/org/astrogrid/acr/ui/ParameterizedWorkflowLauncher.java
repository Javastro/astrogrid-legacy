/*$Id: ParameterizedWorkflowLauncher.java,v 1.5 2007/01/24 14:04:45 nw Exp $
 * Created on 23-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

/**Control the parameterized workflows launcher .
 *
 * <img src="doc-files/pw-choice.png" />
 * @service userInterface.parameterizedWorkflows 
  @see org.astrogrid.acr.ui.JobMonitor
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Mar-2005
 *
 */
public interface ParameterizedWorkflowLauncher {
    /** run the launcher  wizard*/
    void run();
}

/* 
 $Log: ParameterizedWorkflowLauncher.java,v $
 Revision 1.5  2007/01/24 14:04:45  nw
 updated my email address

 Revision 1.4  2006/10/12 02:22:33  nw
 fixed up documentaiton

 Revision 1.3  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:01  nw
 finished split

 Revision 1.4  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:18  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/29 16:10:59  nw
 integration with the portal
 
 */