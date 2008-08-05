/*$Id: ResourceChooserImpl.java,v 1.18 2008/08/05 15:42:18 nw Exp $
 * Created on 21-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.net.URI;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;

/** Implementation of the ResourceChooser component
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Apr-2005
 * wrapper component around the dialog to publish the chooser methods 
 */
public class ResourceChooserImpl implements ResourceChooserInternal {
 

    public ResourceChooserImpl(final UIContext context,final TypesafeObjectBuilder builder) {
    		dialog = new FileExplorerDialog(context, builder);
        getDialog().pack();
    }
    private final FileExplorerDialog dialog;
    
    // public  API
    public  URI chooseResource(final String title,final boolean enableMySpace) {
         final FileExplorerDialog d = getDialog();
        d.setTitle(title);
        d.setVospaceEnabled(enableMySpace);
        d.setLocationRelativeTo(null);
        d.show();
        d.requestFocus();
        d.toFront();
        return d.getUri();        
    }
    
    public  URI fullChooseResource(final String title,final boolean enableMySpace,final boolean enableLocalFile,final boolean enableURI) {
        getDialog().setLocalEnabled(enableLocalFile);
        getDialog().setUrlEnabled(enableURI);
        return chooseResource(title,enableMySpace);        
    }

    public URI chooseFolder(final String arg0, final boolean arg1) {
        getDialog().setChooseDirectories(true);
        return chooseResource(arg0,arg1);
    }

    public URI fullChooseFolder(final String arg0, final boolean arg1, final boolean arg2,
            final boolean arg3) {
        getDialog().setChooseDirectories(true);
        return fullChooseResource(arg0,arg1,arg2,arg3);
    }    
    
///// Internal API

    public  URI chooseResourceWithParent(final String title,final boolean enableMySpace,final boolean enableLocalFile, final boolean enableURI,final Component comp) {
    	getDialog().setLocationRelativeTo(comp);
        getDialog().setTitle(title);
        getDialog().setLocalEnabled(enableLocalFile);
        getDialog().setUrlEnabled(enableURI);
        getDialog().setVospaceEnabled(enableMySpace);
        getDialog().show();
        return getDialog().getUri(); 
    }    

    public  URI chooseDirectoryWithParent(final String title,final boolean enableMySpace,final boolean enableLocalFile, final boolean enableURI,final Component comp) {
        getDialog().setChooseDirectories(true);
        return chooseResourceWithParent(title, enableMySpace, enableLocalFile, enableURI, comp);
    }
    
	/**
	 * @return the dialog
	 */
	protected FileExplorerDialog getDialog() {
		return dialog;
	}

    public FileSystemManager getVFS() {
        return getDialog().getVFS();
    }



}


/* 
$Log: ResourceChooserImpl.java,v $
Revision 1.18  2008/08/05 15:42:18  nw
RESOLVED - bug 2805: mis-leading field when trying to save output file in running a task
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2805

Revision 1.17  2008/02/29 15:06:47  mbt
More sensible dialogue usage

Revision 1.16  2007/10/07 10:40:13  nw
added method to access VFS, as it's present.

Revision 1.15  2007/09/11 12:08:22  nw
services filter, and various layout alterations.

Revision 1.14  2007/08/30 23:46:48  nw
Complete - task 73: upgrade filechooser dialogue to new fileexplorer code
replaced uses of myspace by uses of vfs where sensible

Revision 1.13  2007/05/10 19:35:27  nw
reqwork

Revision 1.12  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.10  2006/11/09 12:08:33  nw
final set of changes for 2006.4.rc1

Revision 1.9  2006/06/27 10:27:12  nw
send-to menu

Revision 1.8  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.7.30.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.7  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.6.14.1  2005/11/23 04:48:33  nw
attempted to improve dialogue behaviour

Revision 1.6  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.3.10.1  2005/10/10 18:12:37  nw
merged kev's datascope lite.

Revision 1.5  2005/10/07 12:12:21  KevinBenson
resorted back to adding to the ResoruceChooserInterface a new method for selecting directories.
And then put back the older one.

Revision 1.4  2005/10/04 20:41:52  KevinBenson
added the ability to select directories on a local file system.  That way myspace can save to a directory.
Only myspacebrowser has this turned on at the moment.

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.4  2005/05/12 15:59:08  clq2
nww 1111 again

Revision 1.2.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.1.2.1  2005/04/22 10:54:03  nw
start of a new module.
 
*/
