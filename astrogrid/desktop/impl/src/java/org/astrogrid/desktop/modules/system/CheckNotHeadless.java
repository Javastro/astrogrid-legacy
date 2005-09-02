/*$Id: CheckNotHeadless.java,v 1.2 2005/09/02 14:03:34 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.awt.GraphicsEnvironment;

/** check that the environment is not headless (i.e. a display is attached)
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 */
public class CheckNotHeadless implements Check {

    /** Construct a new CheckNotHeadless
     * 
     */
    public CheckNotHeadless() {
        super();
    }

    /** returns true if a GUI display is available.
     * @see org.astrogrid.desktop.modules.system.Check#check()
     */
    public boolean check() {
        return ! GraphicsEnvironment.isHeadless();
    }

}


/* 
$Log: CheckNotHeadless.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/