/*$Id: CheckJDICSystemTrayPresent.java,v 1.2 2005/08/05 11:46:55 nw Exp $
 * Created on 21-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jun-2005
 *
 */
public class CheckJDICSystemTrayPresent implements Check {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CheckJDICSystemTrayPresent.class);

    /** Construct a new TestJDICPresent
     * 
     */
    public CheckJDICSystemTrayPresent() {
        super();
    }
    
    public boolean check() {
        try {
            logger.info("Checking for JDIC system tray class");
            Class jdic = Class.forName("org.jdesktop.jdic.tray.SystemTray");
            if (jdic != null) {
                return true;
                }      
        } catch (NoClassDefFoundError e) {
            logger.info("JDIC extensions not present - cannot start system tray");
        } catch (ClassNotFoundException e) {            
            logger.info("JDIC extensions not present - cannot start system tray");
        } catch (UnsatisfiedLinkError e) {
            logger.info("JDIC classes present, but binary libraries are not - cannot start system tray");
        }
        return false;
    }

}


/* 
$Log: CheckJDICSystemTrayPresent.java,v $
Revision 1.2  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.1  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1
 
*/