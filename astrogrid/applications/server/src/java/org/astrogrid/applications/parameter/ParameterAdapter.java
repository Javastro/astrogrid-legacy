/*$Id: ParameterAdapter.java,v 1.3 2004/07/26 12:07:38 nw Exp $
 * Created on 04-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

/** Abstraction around reading and writing  parameter values.
 * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter()}
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Jun-2004
 *
 */
public interface ParameterAdapter {
    
    /** do what it takes to get the actual value for this parameter (used for input parameters)
     * 
     * @return the actual value for this parameter ( or some symbolic representation of it)
     * @throws CeaException
     */
    Object process() throws CeaException;
    /**
     * write out this parameter (used for output parameters).
     * @param o the value of the parameter to write (or some reference / represenation of it)
     * @throws CeaException
     */
    void writeBack(Object o) throws CeaException;
    
    /** returns the parameter object this adapter is wrapping. 
     * @return the paramter value this adpater wraps.*/
    ParameterValue getWrappedParameter();
}


/* 
$Log: ParameterAdapter.java,v $
Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/