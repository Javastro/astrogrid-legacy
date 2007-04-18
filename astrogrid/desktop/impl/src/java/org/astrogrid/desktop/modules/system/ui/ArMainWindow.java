/*$Id: ArMainWindow.java,v 1.1 2007/04/18 15:47:04 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved. 
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.ui;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
/** Main window for AR and Plastic flavours - not much here - mostly built by contributions in 
 * hivemind.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 01-Feb-2005
 */
public class ArMainWindow extends UIComponentImpl {
    private JMenuBar appMenuBar;
    private JTabbedPane tabbedPane;
    
    /** this is the production constructor */
    public ArMainWindow(final Shutdown shutdown, UIContext context,UIContributionBuilder uiBuilder) {     
        super(context);
        this.setJMenuBar(getAppMenuBar());
        
        uiBuilder.populateWidget(getJMenuBar(),this,ArMainWindow.MENUBAR_NAME);
        uiBuilder.populateWidget(getTabbedPane(),this,ArMainWindow.TABS_NAME);
        
        JPanel main = getMainPanel();
        main.add(getTabbedPane(), java.awt.BorderLayout.CENTER);    
        this.setContentPane(main);
        
        getContext().getHelpServer().enableHelpKey(this.getRootPane(),"top");   
        setIconImage(IconHelper.loadIcon("server16.png").getImage());          
        //this.setSize(425, ); // same proportions as A4, etc., and 600 high.   
        this.pack();
    }

	/** parentName for tabs */
	public static final String TABS_NAME = "tabs";

	/** parentName for root menus */
	public static final String MENUBAR_NAME = "menubar";
	private JMenuBar getAppMenuBar() {
        if (appMenuBar == null) {
            appMenuBar = new JMenuBar();
        }
        return appMenuBar;
    }
	private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setBorder(EMPTY_BORDER);
        }
        return tabbedPane;
    }

}


/* 
$Log: ArMainWindow.java,v $
Revision 1.1  2007/04/18 15:47:04  nw
tidied up voexplorer, removed front pane.

Revision 1.22  2007/03/08 17:44:02  nw
first draft of voexplorer

Revision 1.21  2007/01/29 16:45:07  nw
cleaned up imports.

Revision 1.20  2007/01/29 10:49:04  nw
allow null as an execution parameter.

Revision 1.19  2007/01/11 18:15:48  nw
fixed help system to point to ag site.

Revision 1.18  2007/01/10 14:55:14  nw
added support for hiding UI components.

Revision 1.17  2006/10/11 10:39:15  nw
changed icons.

Revision 1.16  2006/08/31 21:32:09  nw
fixed osx button clipping bug.

Revision 1.15  2006/07/20 12:33:28  nw
changed to use image fetching library.

Revision 1.14  2006/06/27 19:18:32  nw
adjusted todo tags.

Revision 1.13  2006/06/27 10:40:40  nw
added new methods

Revision 1.12  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.11  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.10.22.4  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.10.22.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.10.22.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.10.22.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.10  2005/12/16 09:42:47  jl99
Merge from branch desktop-querybuilder-jl-1404

Revision 1.9  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on

Revision 1.8  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.7.2.1  2005/11/23 04:44:35  nw
attempted to improve dialogue behaviour

Revision 1.7  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.6  2005/11/09 15:28:21  jdt
fixed broken help  link

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.16.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.3.16.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.4  2005/10/05 11:52:32  nw
hide module buttons on startup

Revision 1.3  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.2  2005/08/16 13:19:31  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.12  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.11  2005/07/08 14:06:30  nw
final fixes for the workshop.

Revision 1.10  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.9  2005/06/22 08:48:51  nw
latest changes - for 1.0.3-beta-1

Revision 1.8  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.7  2005/06/17 12:06:14  nw
added changelog, made start on docs.
fixed race condition.

Revision 1.6  2005/06/16 12:55:36  nw
updated build process to use new aggregate projects.

Revision 1.5  2005/05/12 15:59:10  clq2
nww 1111 again

Revision 1.3.8.6  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.3.8.5  2005/05/11 14:25:22  nw
javadoc, improved result transformers for xml

Revision 1.3.8.4  2005/05/11 10:59:05  nw
made results selectable, so can be copied and pasted.

Revision 1.3.8.3  2005/05/09 17:49:51  nw
refactored model into separate static class.

Revision 1.3.8.2  2005/05/09 17:30:32  nw
reordered modules for usability, removed most dangerous
methods from the public ui.

Revision 1.3.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.3  2005/04/26 19:10:45  nw
added bugreport menu item.

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:55:01  nw
minor bugfix

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.9  2005/04/06 16:18:50  nw
finished icon set

Revision 1.1.2.8  2005/04/06 15:03:53  nw
added new front end - more modern, with lots if icons.

Revision 1.1.2.7  2005/04/05 11:41:47  nw
added 'hidden.modules',
allowed more methods to be called from ui.

Revision 1.1.2.6  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.5  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.4  2005/04/01 19:03:09  nw
beta of job monitor

Revision 1.1.2.3  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.2  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/