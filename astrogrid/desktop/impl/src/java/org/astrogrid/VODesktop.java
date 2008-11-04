/*$Id: VODesktop.java,v 1.4 2008/11/04 14:35:53 nw Exp $
 * Created on 04-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid;

import org.astrogrid.desktop.SplashWindow;

/** Main class - displays splash screen, then delegates to {@link VODesktop1}
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Jul-2005
 */
public class VODesktop {

    /** Construct a new voexplorer
     * 
     */
    public VODesktop() {
        super();
    }
    
    public static void main(final String[] args) {
        SplashWindow.splash(VODesktop.class.getResource("vodesktop-splash.gif"));//("acr-splash.gif"));
        SplashWindow.invokeMain("org.astrogrid.VODesktop1", args);
        SplashWindow.disposeSplash();
    }

}

