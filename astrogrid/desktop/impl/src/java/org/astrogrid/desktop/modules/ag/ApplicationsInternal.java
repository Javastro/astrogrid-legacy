/*$Id: ApplicationsInternal.java,v 1.7 2007/03/08 17:44:04 nw Exp $
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
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.workflow.beans.v1.Tool;

/** Internal interface to further applications operations.
 * 
 * move stuff here that is too low-level for public interface, or which can't be easily remoted.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Aug-2005
 * 
 */
public interface ApplicationsInternal extends Applications {


    public Tool createTemplateTool(String interfaceName, CeaApplication descr) throws IllegalArgumentException;
    
    
    
    /** shortcut to quickly get the info for a tool 
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws ServiceException.*/
    public CeaApplication getInfoForTool(Tool t) throws ServiceException, NotFoundException, InvalidArgumentException ;
   /** translate any parameters containing adql/s queries to adql/x queries   
    * @param application descrption of the application
    * @param document tool object - changes will be made in-place.
    * @throws ServiceException
    */
   public void translateQueries(Resource application, Tool document) throws ServiceException ;
   
}


/* 
$Log: ApplicationsInternal.java,v $
Revision 1.7  2007/03/08 17:44:04  nw
first draft of voexplorer

Revision 1.6  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.5  2006/08/15 10:15:59  nw
migrated from old to new registry models.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.56.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/