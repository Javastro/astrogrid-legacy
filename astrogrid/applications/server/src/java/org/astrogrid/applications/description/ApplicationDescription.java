/*$Id: ApplicationDescription.java,v 1.4 2004/08/28 07:17:34 pah Exp $
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
/** Description of a CEA Application
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 *
 */
public interface ApplicationDescription {
    /** Access the name of the application. Each application should be uniquely identified by its name.
    * @return a string in format <tt><i>authority-id</i>/<i>application-name</i></tt>
    * @todo wonder if the name of an application should be an ivo:// style thingie? - as its something that is indexable into the registry, etc.
    */
    public  String getName();
    /**
    * Acess the list of all parameters defined for this application (no matter what interface they occur in).
    * @return the array of parameter definitions
    */
    public  ParameterDescription[] getParameterDescriptions();
    /**
    * Access the description for a named parameter
    * @param name the parameter to look up
    * @return the associated description
    * @throws ParameterDescriptionNotFoundException if the application does not recognize the parameter name
    */
    public  ParameterDescription getParameterDescription(String name) throws ParameterDescriptionNotFoundException;
    /**
    * Gets the named interface.
    * @param name the name of the interface to look up.
    * @return the associated <tt>ApplicationInterface</tt>
    * @throws InterfaceDescriptionNotFoundException if the application does not provide an interface of this name.
    */
    public ApplicationInterface getInterface(String name) throws InterfaceDescriptionNotFoundException;
    
    /** list all the interfaces supported by this application */
    public ApplicationInterface[] getInterfaces();
    
    /**
     * Apply this application description to a set of parameters, to create an instance of the application, ready to execute.
     * @param callerAssignedID external identifer for the new application. This identifier is assigned by the caller, but is not used within CEA for distinguishing the application
     * @param user the user whose permissions to execute this tool under
     * @param tool data object that defines which interface to call, and with what parameter values.
     * @return an <tt>Application</tt>
     * @throws Exception
     * @todo should this not be throwing a CEAException?
     */
    public  Application initializeApplication(String callerAssignedID, User user, Tool tool) throws Exception;
}
/* 
$Log: ApplicationDescription.java,v $
Revision 1.4  2004/08/28 07:17:34  pah
commandline parameter passing - unit tests ok

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

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