/*$Id: ApplicationsInternal.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;

/** Internal interface to further applications operations.
 * 
 * move stuff here that is too low-level for public interface, or which can't be easily remoted.
 * @todo review if any of this stuff is used whatsoever
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2005
 *
 */
public interface ApplicationsInternal extends Applications {
    /** get the application descrption for a named application */
    public ApplicationDescription getApplicationDescription(String name) throws WorkflowInterfaceException ;
    /** get the description for an application description */
    public String getInfoFor(ApplicationDescription descr) ;
     
}


/* 
$Log: ApplicationsInternal.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/