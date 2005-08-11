/*$Id: Splasher.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 04-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

/** Bootstrap class - displays splashcreen before starting off main application.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Jul-2005
 *
 */
public class Splasher {

    /** Construct a new Splasher
     * 
     */
    public Splasher() {
        super();
    }
    
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getResource("splash.gif"));
        SplashWindow.invokeMain("org.astrogrid.desktop.Main", args);
        SplashWindow.disposeSplash();
    }

}


/* 
$Log: Splasher.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop
 
*/