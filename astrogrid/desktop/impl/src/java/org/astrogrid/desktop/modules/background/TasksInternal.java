/*$Id: TasksInternal.java,v 1.5 2007/01/29 11:11:36 nw Exp $
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

/**low-level interface into a minimalistic cea server
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 19-Oct-2005
 *
 */
public interface TasksInternal {
    public ManagingExecutionController getExecutionController();

    public QueryService getQueryService();
    
    public IBestMatchApplicationDescriptionLibrary getAppLibrary() ;
}


/* 
$Log: TasksInternal.java,v $
Revision 1.5  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.4  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.3.34.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3.34.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.3  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/