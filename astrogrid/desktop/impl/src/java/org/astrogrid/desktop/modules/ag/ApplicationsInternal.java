/*$Id: ApplicationsInternal.java,v 1.3 2005/09/12 15:21:16 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import java.net.URI;

/** Internal interface to further applications operations.
 * 
 * move stuff here that is too low-level for public interface, or which can't be easily remoted.
 *x @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
 *
 */
public interface ApplicationsInternal extends Applications {
    /** get the application descrption for a named application 
     * @deprecated soon to be removed - phil -stop using ApplicationDescription, and use methods in {@link org.astrogrid.acr.astrogrid.Applications} instead.
 **/
 //   public ApplicationDescription getApplicationDescription(URI name) throws WorkflowInterfaceException ;
/** hook specially for the parametweized workflow launcher */
    public Tool createTemplateTool(String interfaceName, ApplicationInformation descr) throws IllegalArgumentException;
     
    
    /** shortcut to quickly get the info for a tool 
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws ServiceException*/
    public ApplicationInformation getInfoForTool(Tool t) throws ServiceException, NotFoundException, InvalidArgumentException ;
   /** translate any parameters containing adql/s queries to adql/x queries   
    * @param application descrption of the application
    * @param document tool object - changes will be made in-place.
    * @throws ServiceException
    */
   public void translateQueries(ApplicationInformation application, Tool document) throws ServiceException ;
   
}


/* 
$Log: ApplicationsInternal.java,v $
Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/