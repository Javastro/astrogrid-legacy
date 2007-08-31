/*$Id: ResourceChooser.java,v 1.5 2007/08/31 00:01:50 nw Exp $
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

/** Prompt the user to select a local file / myspace resource / url by displaying a resource chooser dialogue.
 *  
 * This is a  generalisation of the 'open/save file' browser that also allows local and  remote ( myspace / vospace / URL) resources to be selected.

 * @service  dialogs.resourceChooser
 * @see org.astrogrid.acr.astrogrid.Myspace
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Apr-2005
 *
 */
public interface ResourceChooser {

    /** show the resource chooser, and block until user selects a file
     * 
     * @param title title for the dialogue - e.g.'choose file to open'
     * @param enableRemote - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.
     * @return URI of the selected resource, or null if the user cancelled.
     */
    URI chooseResource(String title, boolean enableRemote);
    
    /** show the resource chooser, and block untiil user selects a folder.
     * @param title title for the dialogue - e.g.'choose file to open'
     * @param enableRemote - if true,allow selection of a remote resource (myspace / vospace / URL). Selection of local resources is enabled always.
     * @return URI of the selected folder, or null if the user cancelled.
     */
    URI chooseFolder(String title, boolean enableRemote);

    /** fully-configurable resource chooser - a generalization of {@link #chooseResource}
     * @param title title for the dialogue
     * @param enableVospace if true,allow selection of a remote myspace / vospace resource.
     * @param enableLocal if true, allow selection of local files
     * @param enableURL if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered.
     * @return the URI of the selected resource, or null if the user cancelled
     */
    URI fullChooseResource(String title, boolean enableVospace, boolean enableLocal, boolean enableURL);
    
    
    /** fully-configurable resource chooser - a generalization of {@link #chooseFolder}
     * @param title title for the dialogue
     * @param enableVospace if true,allow selection of a remote myspace / vospace folder
     * @param enableLocal if true, allow selection of local folders
     * @param enableURL if true, enable the 'enter a URL' tab, so an arbitrary URL can be entered. No verification that this _is_ a folder in some sense is performed.
     * @return the URI of the selected folder, or null if the user cancelled
     */
    URI fullChooseFolder(String title, boolean enableVospace, boolean enableLocal, boolean enableURL);    
}

/* 
 $Log: ResourceChooser.java,v $
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