/*$Id: NullUIImpl.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 27-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.system.UI;

/** null implementation of the ui - used when there's a headless configuration.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public class NullUIImpl implements UI {

    /** Construct a new NullUIImpl
     * 
     */
    public NullUIImpl() {
        super();
    }

    /**
     * @see org.astrogrid.acr.system.UI#show()
     */
    public void show() {
    }

    /**
     * @see org.astrogrid.acr.system.UI#hide()
     */
    public void hide() {
    }

    /**
     * @see org.astrogrid.acr.system.UI#startThrobbing()
     */
    public void startThrobbing() {
    }

    /**
     * @see org.astrogrid.acr.system.UI#stopThrobbing()
     */
    public void stopThrobbing() {
    }

    /**
     * @see org.astrogrid.acr.system.UI#setLoggedIn(boolean)
     */
    public void setLoggedIn(boolean value) {
    }

    /**
     * @see org.astrogrid.acr.system.UI#setStatusMessage(java.lang.String)
     */
    public void setStatusMessage(String msg) {
    }

}


/* 
$Log: NullUIImpl.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/