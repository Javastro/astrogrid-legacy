/*$Id: DefaultParameterAdapterFactory.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
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
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;

/** Default parameter adapter factory implementation.
 * Returns instances og {@link DefaultParameterAdapter} . Subclasses can alter 
 * this by overriding {@link #instantiateAdapter}.
 * The rest of this factory is concerned with managing indirect parameters - selecting the correct protocol from
 * the library to handle accessing them.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class DefaultParameterAdapterFactory implements ParameterAdapterFactory {
    /** Construct a new DefaultParameterAdapterFactory
     *  Construct a new DefaultParameterAdapterFactory
     * @param lib library of indirectioin protocol adapters to use to work with indirect parameters.
     */
    public DefaultParameterAdapterFactory(IndirectionProtocolLibrary lib) {
        super();
        this.lib = lib;
    }
    protected final IndirectionProtocolLibrary lib;
    /**
     * @see org.astrogrid.applications.parameter.ParameterAdapterFactory#createAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription)
     */
    public ParameterAdapter createAdapter(ParameterValue pval, ParameterDescription desr) throws ParameterAdapterException {
        IndirectParameterValue indirectVal = null;
        if (pval.getIndirect()) {
            indirectVal = lib.getIndirect(pval);
        } 
        return instantiateAdapter(pval, desr, indirectVal);
    }
    

    /** override this in subclasses as needed 
     * 
     * @param pval the parameter value
     * @param desr the description
     * @param indirectVal an indirect parameter value (if needed, for direct parameters will be null)
     * @return a new parameter adapter.
     */
    protected ParameterAdapter instantiateAdapter(
        ParameterValue pval,
        ParameterDescription desr,
        IndirectParameterValue indirectVal) {
        
        return new DefaultParameterAdapter(pval,desr,indirectVal);
    }
}


/* 
$Log: DefaultParameterAdapterFactory.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/