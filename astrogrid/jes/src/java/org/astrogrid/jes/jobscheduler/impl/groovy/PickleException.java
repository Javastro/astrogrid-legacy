/*$Id: PickleException.java,v 1.3 2004/09/06 16:47:04 nw Exp $
 * Created on 28-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.JesException;

/** Exception thrown when something goes wrong at the pickler.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *@see org.astrogrid.jes.jobscheduler.impl.groovy.XStreamPickler
 */
public class PickleException extends JesException {

    /** Construct a new PickleException
     * @param s
     */
    public PickleException(String s) {
        super(s);
    }

    /** Construct a new PickleException
     * @param s
     * @param e
     */
    public PickleException(String s, Exception e) {
        super(s, e);
    }

}


/* 
$Log: PickleException.java,v $
Revision 1.3  2004/09/06 16:47:04  nw
javadoc

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation
 
*/