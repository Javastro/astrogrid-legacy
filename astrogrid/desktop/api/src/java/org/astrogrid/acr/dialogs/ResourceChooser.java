/*$Id: ResourceChooser.java,v 1.6 2008/09/25 16:02:02 nw Exp $
 * Created on 21-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.dialogs;

import java.net.URI;

/** AR Service: Dialogue that prompts the user to select a local file, vospace resource or URL. 
 *  <p />
 * This is a  generalisation of the typical filechooser dialogue that allows local and  remote ( myspace / vospace / URL) resources to be selected.

 * @service  dialogs.resourceChooser
 * @see org.astrogrid.acr.astrogrid.Myspace
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Apr-2005
 *
 */
public interface ResourceChooser {

    /** Prompt the user to select a file.
     * 
     * Blocks until a file is selected, or user presses cancel.
     * 
     * @param title title for the dialogue - e.g.{@code choose file to open}
     * @param enableRemote - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.
     * @return URI of the selected resource, or null if the user pressed cancel
     * @equivalence fullChooseResource(title,enableRemote,true,enableRemote)
     */
    URI chooseResource(String title, boolean enableRemote);
    
    /** Prompt the user to select a folder.
     * 
     * Blocks until a folder is selected, or user presses cancel.
     * 
     * @param title title for the dialogue - e.g.{@code choose file to open}
     * @param enableRemote - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.
     * @return URI of the selected folder, or null if the user pressed cancel.
     * @equivalence fullChooseFolder(title,enableRemote,true,enableRemote)     
     */
    URI chooseFolder(String title, boolean enableRemote);

    /** Fully-configurable resource chooser - a generalization of {@link #chooseResource}.
     * @param title title for the dialogue
     * @param enableVospace if true,allow selection of a remote myspace / vospace resource.
     * @param enableLocal if true, allow selection of local files
     * @param enableURL if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.
     * @return the URI of the selected resource, or null if the user cancelled
     */
    URI fullChooseResource(String title, boolean enableVospace, boolean enableLocal, boolean enableURL);
    
    
    /** Fully-configurable resource chooser - a generalization of {@link #chooseFolder}.
     * @param title title for the dialogue
     * @param enableVospace if true,allow selection of a remote myspace / vospace folder
     * @param enableLocal if true, allow selection of local folders
     * @param enableURL if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered. 
     * {@stickyWarning No verification that an entered URL <i>is</i> a folder is performed.}
     * @return the URI of the selected folder, or null if the user cancelled
     */
    URI fullChooseFolder(String title, boolean enableVospace, boolean enableLocal, boolean enableURL);    
}

/* 
 $Log: ResourceChooser.java,v $
 Revision 1.6  2008/09/25 16:02:02  nw
 documentation overhaul

 Revision 1.5  2007/08/31 00:01:50  nw
 Complete - task 73: upgrade filechooser dialogue to new fileexplorer code

 Revision 1.4  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.3  2006/02/02 14:19:47  nw
 fixed up documentation.

 Revision 1.2  2005/08/12 08:45:15  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.5  2005/07/08 11:08:02  nw
 bug fixes and polishing for the workshop

 Revision 1.4  2005/05/12 15:59:07  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:40  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.1.2.1  2005/04/22 10:54:03  nw
 start of a new module.
 
 */