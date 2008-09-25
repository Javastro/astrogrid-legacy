/*$Id: JobMonitor.java,v 1.7 2008/09/25 16:02:09 nw Exp $
 * Created on 31-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.ui;

import java.net.URI;

/** Control the Job Monitor GUI. (Unimplemented)
 * 
 * @service userInterface.jobMonitor
 * @exclude 
 * @deprecated replaced by {@link Lookout} 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Mar-2005
 *@see org.astrogrid.acr.astrogrid.Jobs
 */
@Deprecated
public interface JobMonitor {
    /** show the job monitor window */
    public void show();
    /** hide the job monitor  window*/
    public void hide();
    // refresh the job list.
    /** manually refresh the job list */
    public void refresh();
    /** Add a new application to the monitor
     * @param name user-friendly name of the app
     * @param executionId identifier of the application     
     */
    public void addApplication(String name,URI executionId);
    /**
     *  bring the application tab of the monitor uppermost
     */
    public void displayApplicationTab();
    
    /** bring the jes tab of the monitor uppermost */
    public void displayJobTab() ;
}

/* 
 $Log: JobMonitor.java,v $
 Revision 1.7  2008/09/25 16:02:09  nw
 documentation overhaul

 Revision 1.6  2007/01/24 14:04:45  nw
 updated my email address

 Revision 1.5  2006/10/12 02:22:33  nw
 fixed up documentaiton

 Revision 1.4  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.3  2005/11/24 01:18:42  nw
 merged in final changes from release branch.

 Revision 1.2.16.1  2005/11/23 18:07:22  nw
 improved docs.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:01  nw
 finished split

 Revision 1.6  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.5  2005/06/08 14:51:59  clq2
 1111

 Revision 1.2.8.2  2005/06/02 14:34:32  nw
 first release of application launcher

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:18  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/04/04 08:49:27  nw
 working job monitor, tied into pw launcher.

 Revision 1.1.2.1  2005/04/01 19:03:10  nw
 beta of job monitor
 
 */