/*$Id: ParameterAdapter.java,v 1.5 2011/09/02 21:55:51 pah Exp $
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

/** Abstraction around reading and writing  parameter values.
 * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter()}
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Jul 2009 -  changed to be more concrete.
 *
 */
public interface ParameterAdapter {
    
    /** do what it takes to get the actual value for this parameter (used for input parameters)
     * 
     * @return the actual value for this parameter ( or some symbolic representation of it)
     * @throws CeaException
     */
    MutableInternalValue getInternalValue() throws CeaException;
    /**
     * write out this parameter (used for output parameters).
     * 
     * @throws CeaException
     */
    void writeBack() throws CeaException;
    
    /** returns the parameter object this adapter is wrapping. 
     * @TODO eliminate this and add the uses made of the raw parameterValue to this interface.
     * @return the parameter value this adapter wraps.
     * */
    org.astrogrid.applications.description.execution.ParameterValue getWrappedParameter();
}


/* 
$Log: ParameterAdapter.java,v $
Revision 1.5  2011/09/02 21:55:51  pah
result of merging the 2931 branch

Revision 1.4.6.2  2009/07/16 19:48:05  pah
ASSIGNED - bug 2950: rework parameterAdapter
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950

Revision 1.4.6.1  2009/07/15 09:49:36  pah
redesign of parameterAdapters

Revision 1.4  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.3.266.2  2008/06/10 20:01:38  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.3.266.1  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

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