/*$Id: IndirectionProtocolLibrary.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.indirect;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;

/** library of indirection handling code, for working with remote parameter values.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface IndirectionProtocolLibrary {
    /** build a indirect parameter value for a particular parameter
     * 
     * @param pval parameter to build indirector for
     * @return an indirection adapter.
     * @throws InaccessibleIndirectParameterException - if the indirect parameter cannot be gained - e.g. cannot resolve, no path
     * @throws UnrecognizedIndirectParameterProtocolException - if the indirection url format is not recognized.
     */
    IndirectParameterValue getIndirect(ParameterValue pval) throws InaccessibleIndirectParameterException, UnrecognizedIndirectParameterProtocolException;
    
    /** list the protocols supported in this library */
    String[] listSupportedProtocols();
    
    /** return true if this library supports the protcol in question */
    boolean isProtocolSupported(String protocol);
    
    
}