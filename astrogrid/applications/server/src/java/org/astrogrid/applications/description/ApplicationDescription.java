/*$Id: ApplicationDescription.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;
/** Interface describing an applicationi
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 *
 */
public interface ApplicationDescription {
    /**
    * @return
    */
    public  String getName();
    /**
    * get the full list of possible parameters definitions for this application.
    * @return the array of parameter definitions
    */
    public  ParameterDescription[] getParameterDescriptions();
    /**
    * 
    * @param name
    * @return
    */
    public  ParameterDescription getParameterDescription(String name) throws ParameterDescriptionNotFoundException;
    /**
    * Gets the named interface.
    * @param name
    * @return
    * @throws InterfaceDescriptionNotFoundException
    */
    public ApplicationInterface getInterface(String name) throws InterfaceDescriptionNotFoundException;
    
    public ApplicationInterface[] getInterfaces();
    
    /** create an application instance from this description
     * - represents an instance of the appliation, ready to execute 
     */
    public  Application initializeApplication(String jobStepID, User user, Tool tool) throws Exception;
}
/* 
$Log: ApplicationDescription.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.7.8.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/