/*$Id: StatusTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications;

import junit.framework.TestCase;

/** Exercise the status class.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class StatusTest extends TestCase {
    /**
     * Constructor for StatusTest.
     * @param arg0
     */
    public StatusTest(String arg0) {
        super(arg0);
    }
    
    public void testToString() {
        assertEquals(Status.COMPLETED,Status.valueOf(Status.COMPLETED.toString()));
        assertEquals(Status.ERROR,Status.valueOf(Status.ERROR.toString()));
        assertEquals(Status.INITIALIZED,Status.valueOf(Status.INITIALIZED.toString()));
        assertEquals(Status.NEW,Status.valueOf(Status.NEW.toString()));   
        assertEquals(Status.RUNNING,Status.valueOf(Status.RUNNING.toString()));
        assertEquals(Status.UNKNOWN,Status.valueOf(Status.UNKNOWN.toString()));
        assertEquals(Status.WRITINGBACK,Status.valueOf(Status.WRITINGBACK.toString()));
    } 
}


/* 
$Log: StatusTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/