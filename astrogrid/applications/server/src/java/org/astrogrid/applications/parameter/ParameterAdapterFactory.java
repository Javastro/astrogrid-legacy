/*$Id: ParameterAdapterFactory.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;

/** Interface to a factory for parameter adapters.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Jun-2004
 *
 */
public interface ParameterAdapterFactory {
    /** create a new parameter adapter
     * 
     * @param pval the parameter value for the adapter
     * @param desr the corresponding descriptioin for this value.
     * @return a new parameter adapter.
     * @throws ParameterAdapterException
     */
    ParameterAdapter createAdapter(ParameterValue pval,ParameterDescription desr) throws ParameterAdapterException;
}


/* 
$Log: ParameterAdapterFactory.java,v $
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