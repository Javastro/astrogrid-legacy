/*$Id: InvalidUcdException.java,v 1.1 2004/10/05 19:19:18 mch Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl.cds.ucdresolver;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class InvalidUcdException extends Exception {

    /**
     * 
     */
    public InvalidUcdException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public InvalidUcdException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidUcdException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidUcdException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}


/* 
$Log: InvalidUcdException.java,v $
Revision 1.1  2004/10/05 19:19:18  mch
Merged CDS implementation into PAL

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/
