/*$Id: VOExplorer.java,v 1.2 2007/06/27 11:09:40 nw Exp $
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

/** Entry point for launching voexplorer
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Jul-2005
 */
public class VOExplorer {

    /** Construct a new voexplorer
     * 
     */
    public VOExplorer() {
        super();
    }
    
    public static void main(String[] args) {
    	//@todo provide a splash screen.
        SplashWindow.splash(VOExplorer.class.getResource("acr-splash.gif"));
        SplashWindow.invokeMain("org.astrogrid.VOExplorer1", args);
        SplashWindow.disposeSplash();
    }

}

