/*$Id: ApplicationInterface.java,v 1.2 2011/09/02 21:55:52 pah Exp $
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
/** Defines a calling 'interface' to an {@link org.astrogrid.applications.description.ApplicationDefinition}
 * <p>
 * An interface comprises a name, a set of input parameters, and a set of output parameters.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Mar 2008
 * @see org.astrogrid.applications.description.ApplicationDefinition
 *
 */
public interface ApplicationInterface extends Identify {
    /** access an array of input parameter names 
     * @deprecated parameter groups make this not so simple
     * */
    public abstract String[] getArrayofInputs();
    /** access an array of output parameter names
     * @deprecated parameter groups make this not so simple
     *  */
    public abstract String[] getArrayofOutputs();
    /** for an input parameter name (such as returned by {@link #getArrayofInputs()} etc), find the corresponding 
     * parameter descrption
     * @param name the name of the parameter.
     * @return the description of this parameter.
     * @throws ParameterNotInInterfaceException if tis parametername is not present in the interface.
     */
    public abstract ParameterDescription getInputParameter(String name) throws ParameterNotInInterfaceException;
    /** @see #getInputParameter(String)*/
    public abstract ParameterDescription getOutputParameter(String name) throws ParameterNotInInterfaceException;
    
    /** access the application description that this interface belongs to */ 
    public ApplicationDefinition getApplicationDescription();
    
    /** access the cardinality of a parameter */
    public Cardinality getParameterCardinality(String name) throws ParameterNotInInterfaceException;
    
}
/* 
$Log: ApplicationInterface.java,v $
Revision 1.2  2011/09/02 21:55:52  pah
result of merging the 2931 branch

Revision 1.1.2.1  2009/07/16 19:45:21  pah
NEW - bug 2950: rework parameterAdapter
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950

Revision 1.1  2009/02/26 12:25:48  pah
separate more out into cea-common for both client and server

Revision 1.5  2008/09/03 14:18:43  pah
result of merge of pah_cea_1611 branch

Revision 1.4.250.2  2008/03/26 17:15:38  pah
Unit tests pass

Revision 1.4.250.1  2008/03/19 23:10:53  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.4  2004/08/16 11:03:07  nw
added classes to model cardinality of prefs.

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