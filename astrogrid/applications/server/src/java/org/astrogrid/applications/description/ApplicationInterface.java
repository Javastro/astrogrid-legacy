/*$Id: ApplicationInterface.java,v 1.3 2004/07/26 00:57:46 nw Exp $
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
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
/** Defines a calling 'interface' to an {@link org.astrogrid.applications.description.ApplicationDescription}
 * <p>
 * An interface comprises a name, a set of input parameters, and a set of output parameters.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @see org.astrogrid.applications.description.ApplicationDescription
 *
 */
public interface ApplicationInterface {
    /** access the name of this interface */
    public abstract String getName();
    /** access an array of input parameter names */
    public abstract String[] getArrayofInputs();
    /** access an array of output parameter names */
    public abstract String[] getArrayofOutputs();
    /** for an input parameter name (such as returned by {@link #getArrayofInputs()} etc), find the corresponding 
     * parameter descrption
     * @param name the name of the parameter.
     * @return the description of this parameter.
     * @throws ParameterNotInInterfaceException if tis parametername is not present in the interface.
     */
    public abstract ParameterDescription getInputParameter(String name) throws ParameterNotInInterfaceException;
    /** determine whether this parameter is an input or output parameter in this interface */
    public abstract ParameterDirection getParameterDirection(String name) throws ParameterNotInInterfaceException;
    /** @see #getInputParameter(String)*/
    public abstract ParameterDescription getOutputParameter(String name) throws ParameterNotInInterfaceException;
    
    /** access the application description that this interface belongs to */ 
    public ApplicationDescription getApplicationDescription();
}
/* 
$Log: ApplicationInterface.java,v $
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

Revision 1.6.22.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/