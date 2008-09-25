/*$Id: HelpServer.java,v 1.8 2008/09/25 16:02:03 nw Exp $
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

/**AR Service: Control the workbench help viewer.
 * @exclude not interesting to user.
 * @service system.help
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Jun-2005
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
 Revision 1.8  2008/09/25 16:02:03  nw
 documentation overhaul

 Revision 1.7  2007/11/12 13:36:27  pah
 change parameter name to structure

 Revision 1.6  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.5  2007/01/09 15:47:14  nw
 tidied docs

 Revision 1.4  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.3  2005/10/12 09:22:16  nw
 added java help system

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.1  2005/06/20 16:56:40  nw
 fixes for 1.0.2-beta-2
 
 */