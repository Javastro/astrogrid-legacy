/*$Id: ParameterAdapterException.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.CeaException;

/** Some generic fault concerning parameter adapters
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class ParameterAdapterException extends CeaException {
    /** Construct a new ParameterAdapterException
     * @param message
     */
    public ParameterAdapterException(String message) {
        super(message);
    }
    /** Construct a new ParameterAdapterException
     * @param message
     * @param cause
     */
    public ParameterAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: ParameterAdapterException.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/