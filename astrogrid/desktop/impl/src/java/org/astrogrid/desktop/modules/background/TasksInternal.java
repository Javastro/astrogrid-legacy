/*$Id: TasksInternal.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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

import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.QueryService;

/** interface into a minimalistic cea server
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Oct-2005
 *
 */
public interface TasksInternal {
    public ExecutionController getExecutionController();

    public QueryService getQueryService();
    
    public BestMatchApplicationDescriptionLibrary getAppLibrary() ;
}


/* 
$Log: TasksInternal.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/