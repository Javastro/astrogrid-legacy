/*$Id: UnrecognizedIndirectParameterProtocolException.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.parameter.ParameterAdapterException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class UnrecognizedIndirectParameterProtocolException extends ParameterAdapterException {
    /** Construct a new UnrecognizedIndirectParameterProtocolException
     * @param message
     */
    public UnrecognizedIndirectParameterProtocolException(String message) {
        super(message);
    }
    /** Construct a new UnrecognizedIndirectParameterProtocolException
     * @param message
     * @param cause
     */
    public UnrecognizedIndirectParameterProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: UnrecognizedIndirectParameterProtocolException.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/