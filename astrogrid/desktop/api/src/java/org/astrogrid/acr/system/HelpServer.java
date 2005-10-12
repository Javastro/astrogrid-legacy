/*$Id: HelpServer.java,v 1.3 2005/10/12 09:22:16 nw Exp $
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

/**Service Interface to in-program help
 * <p>
 * @service system.help
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2005
 * 
 */
public interface HelpServer {
    /** display the help viewer, with the 'top' help page */ 
    void showHelp();
   /** display a specific help topic in the viewer 
    * 
    * @param target name of the topic to display 
    */
    void showHelpForTarget(String target);

}

/* 
 $Log: HelpServer.java,v $
 Revision 1.3  2005/10/12 09:22:16  nw
 added java help system

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.1  2005/06/20 16:56:40  nw
 fixes for 1.0.2-beta-2
 
 */