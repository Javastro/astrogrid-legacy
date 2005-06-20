/*$Id: HelpServer.java,v 1.1 2005/06/20 16:56:40 nw Exp $
 * Created on 17-Jun-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.system;

import javax.swing.Action;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2005
 *
 */
public interface HelpServer {
    void showHelp();

    void showHelpForTarget(String target);

    void showHelpFromFocus();

    void trackFieldHelp();

    Action getShowHelpForTargetInstance(String target);

    Action getShowIDInstance(String actionName, String helpId);
}

/* 
 $Log: HelpServer.java,v $
 Revision 1.1  2005/06/20 16:56:40  nw
 fixes for 1.0.2-beta-2
 
 */