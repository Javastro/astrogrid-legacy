/*$Id: ProcessingException.java,v 1.1 2003/09/08 09:34:56 nw Exp $
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

/** Subclass of ADQLException, used to wrap exceptions thrown from processing code (i.e. the <tt>visit**</tt> methods
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2003
 *
 */
public class ProcessingException extends ADQLException {

    /**
     * @param cause
     */
    public ProcessingException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public ProcessingException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}


/* 
$Log: ProcessingException.java,v $
Revision 1.1  2003/09/08 09:34:56  nw
Improved exception handling
 
*/