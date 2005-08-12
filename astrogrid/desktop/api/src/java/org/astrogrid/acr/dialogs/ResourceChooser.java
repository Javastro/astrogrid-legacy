/*$Id: ResourceChooser.java,v 1.2 2005/08/12 08:45:15 nw Exp $
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

/**Service Interface to the  Resource Chooser Dialoge 
 * - a generalisation of the 'open/save file' browser, that also allows a myspace resource, or arbitrary URL to be selected
 * <p>
 * <img src="doc-files/myspacechooser.png">
 * <p>
 * @service  dialogs.resourceChooser
 * @see org.astrogrid.acr.astrogrid.Myspace
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2005
 *
 */
public interface ResourceChooser {

    /** show the resource chooser, and block until user selects.
     * 
     * @param title title for the dialogue - e.g.'choose file to open'
     * @param enableMySpace - if true, enable the myspace tab. if false, don't display this.
     * @return URI of the selected resource, or null if the user cancelled.
     */
    URI chooseResource(String title, boolean enableMySpace);

    /** fully-configurable resource chooser - a generalization of {@link #chooseResource}
     * @param title title for the dialogue
     * @param enableMySpace if true, enable the myspace tab
     * @param enableLocalFile if true, enable the local file tab
     * @param enableURI if true, enable the 'enter a URL' tab
     * @return the URI of the selected resource, or null if the user cancelled
     */
    URI fullChooseResource(String title, boolean enableMySpace, boolean enableLocalFile, boolean enableURI);
}

/* 
 $Log: ResourceChooser.java,v $
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