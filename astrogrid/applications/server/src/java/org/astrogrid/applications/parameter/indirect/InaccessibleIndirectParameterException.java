/*$Id: InaccessibleIndirectParameterException.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

/** thrown when the value of an indirect parameter cannot be accessed - e.g. because the resource cannot be accessed.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class InaccessibleIndirectParameterException extends ParameterAdapterException {
    /** Construct a new InaccessibleIndirectParameterException
     * @param message
     */
    public InaccessibleIndirectParameterException(String message) {
        super(message);
    }
    /** Construct a new InaccessibleIndirectParameterException
     * @param message
     * @param cause
     */
    public InaccessibleIndirectParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: InaccessibleIndirectParameterException.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/