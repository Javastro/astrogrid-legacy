/*$Id: TraversalException.java,v 1.1 2003/10/14 12:43:42 nw Exp $
 * Created on 08-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;

/** Subclas of ADQL exception thrown when an error occurs in the dynamic reflection code.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2003
 *
 */
public class TraversalException extends ADQLException {

    /**
     * @param cause
     */
    public TraversalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public TraversalException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public TraversalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}


/* 
$Log: TraversalException.java,v $
Revision 1.1  2003/10/14 12:43:42  nw
moved from parent datacenter project.

Revision 1.1  2003/09/08 09:34:56  nw
Improved exception handling
 
*/