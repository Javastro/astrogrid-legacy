/*$Id: ADQLException.java,v 1.1 2003/11/14 00:36:40 mch Exp $
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

/** Exception type that represents something going wrong with processing an ADQL object model.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2003
 *
 */
public class ADQLException extends Exception {

    /**
     * 
     */
    public ADQLException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     */
    public ADQLException(String message) {
        super(message);
    }



    /**
     * @param message
     * @param cause
     */
    public ADQLException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: ADQLException.java,v $
Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/14 12:43:42  nw
moved from parent datacenter project.

Revision 1.1  2003/09/08 09:34:56  nw
Improved exception handling
 
*/