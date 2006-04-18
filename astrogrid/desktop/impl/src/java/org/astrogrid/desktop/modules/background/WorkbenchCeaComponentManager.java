/*$Id: WorkbenchCeaComponentManager.java,v 1.7 2006/04/18 23:25:43 nw Exp $
 * Created on 19-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.applications.manager.QueryService;

/** Custom cea component manager - just the things needed for an in-process cea server.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Oct-2005
 * 
 */
public class WorkbenchCeaComponentManager implements TasksInternal{
   
    
    private final ManagingExecutionController controller;
    private final QueryService query;
    private final IBestMatchApplicationDescriptionLibrary appDescLib;

    public WorkbenchCeaComponentManager( 
             ManagingExecutionController controller
            , QueryService query
            , IBestMatchApplicationDescriptionLibrary appDescLib) { 
        super();
        this.controller = controller;
        this.query = query;
        this.appDescLib = appDescLib;

    }

    public ManagingExecutionController getExecutionController() {
        return controller;
    }

    public QueryService getQueryService() {
        return query;
     
    }
    
    public IBestMatchApplicationDescriptionLibrary getAppLibrary() {
        return appDescLib;
        }


    
}


/* 
$Log: WorkbenchCeaComponentManager.java,v $
Revision 1.7  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.5.26.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.5.26.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.5.26.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.6  2006/03/22 17:24:39  nw
fixes necessary for upgrade to 2006.1 libs

Revision 1.5  2005/12/02 13:43:41  nw
linked internal cea into new thread-pool system.

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.1  2005/11/23 04:51:20  nw
configured to use low-priority threads.

Revision 1.3  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/