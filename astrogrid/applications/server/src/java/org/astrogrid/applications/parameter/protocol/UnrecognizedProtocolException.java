/*$Id: UnrecognizedProtocolException.java,v 1.1 2004/07/26 12:07:38 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import org.astrogrid.applications.parameter.ParameterAdapterException;

/** Exception thrown when an indirect parameter is encountered whose resource is not a known protocol.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class UnrecognizedProtocolException extends ParameterAdapterException {
    /** Construct a new UnrecognizedIndirectParameterProtocolException
     * @param message
     */
    public UnrecognizedProtocolException(String message) {
        super(message);
    }
    /** Construct a new UnrecognizedIndirectParameterProtocolException
     * @param message
     * @param cause
     */
    public UnrecognizedProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: UnrecognizedProtocolException.java,v $
Revision 1.1  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/