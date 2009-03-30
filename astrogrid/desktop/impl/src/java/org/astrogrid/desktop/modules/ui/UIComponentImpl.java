/*$Id: UIComponentImpl.java,v 1.25 2009/03/30 15:04:55 nw Exp $
 * Created on 07-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.AbstractCloseAction;




/** Implementation of {@code UIComponent} that can be used as a baseclass for <b>non-dialogue windows</b>.
 *<p>
 *adds a progress bar / status message at the bottom, and
 *provides a worker class that indicates progress using these.
 *
 *Also provides a place to have convenient common functionality - definitions of a 
 *close operation, help menu, etc.
 *@see org.astrogrid.desktop.modules.ui.BackgroundWorker
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 07-Apr-2005
 *
 */ 
public class UIComponentImpl extends JFrame implements UIComponent {


    /**
     * Commons Logger for this class - can be used by subclasses too.
     */
    protected static final Log logger = LogFactory.getLog(UIComponentImpl.class);

    private final UIContext context;
    private final UIComponentAssist assist;

    protected final String applicationName;

    protected final String helpKey;
     /** Construct a new UIComponentImpl
     * @param context
     * @throws HeadlessException
     */
    public UIComponentImpl(final UIContext context, final String name, final String helpId) throws HeadlessException {
        this.context = context;
        this.applicationName = name;
        setTitle(applicationName);
        this.helpKey = helpId;
        context.registerWindow(this);
        context.getHelpServer().enableHelpKey(this.getRootPane(),helpId);
        assist = new UIComponentAssist(this);
        setContentPane(assist.getMainPanel());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(final WindowEvent e) {
            	getContext().unregisterWindow(UIComponentImpl.this);
            	setVisible(false);
            	// detach myself from the context.
            	assist.cleanup();
            	try {
            	    dispose();
            	} catch (final NullPointerException x) {
            	    // work around for bugzilla 2758
            	}
            }
        });        
    }
    
    public Component getComponent() {
		return this;
	}



	/** Convenience class - a local subclass of {@link BackgroundWorker} that ties
     * into the enclosing UIComponentImpl instance.
     * Prefer {@link BackgroundWorker} if there's any chance that operations may be resuable.
     * */
     protected abstract class BackgroundOperation extends BackgroundWorker {
         public BackgroundOperation(final String msg) {
             super(UIComponentImpl.this,msg);
         }
         
         public BackgroundOperation(final String msg,final int priority) {
             super(UIComponentImpl.this,msg,priority);
         }
         public BackgroundOperation(final String msg,final TimeoutEnum timeout) {
             super(UIComponentImpl.this,msg,timeout);
         }
         public BackgroundOperation(final String msg,final TimeoutEnum timeout, final int priority) {
             super(UIComponentImpl.this,msg,timeout,priority);
         }
     }

	/** close window action for ui components. */
     public final class CloseAction extends AbstractCloseAction {

         public void actionPerformed(final ActionEvent e) {
             hide();
             getContext().unregisterWindow(UIComponentImpl.this);
             dispose();
         }
     }
     public UIContext getContext() {
        return context;
    }
// from here on, just delegate to assistant.


    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getMainPanel()
     */
    public final JPanel getMainPanel() {
        return this.assist.getMainPanel();
    }


    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getProgressMax()
     */
    public final int getProgressMax() {
        return this.assist.getProgressMax();
    }


    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getProgressValue()
     */
    public final int getProgressValue() {
        return this.assist.getProgressValue();
    }


    /**
     * 
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#haltMyTasks()
     */
    public final void haltMyTasks() {
        this.assist.haltMyTasks();
    }



    /**
     * @param i
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setProgressMax(int)
     */
    public final  void setProgressMax(final int i) {
        this.assist.setProgressMax(i);
    }


    /**
     * @param i
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setProgressValue(int)
     */
    public final void setProgressValue(final int i) {
        this.assist.setProgressValue(i);
    }


    /**
     * @param s
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setStatusMessage(java.lang.String)
     */
    public final void setStatusMessage(final String s) {
        this.assist.setStatusMessage(s);
    }


    /**
     * @param msg
     * @param e
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showError(java.lang.String, java.lang.Throwable)
     */
    public final void showError(final String msg, final Throwable e) {
        this.assist.showError(msg, e);
    }

    public final void showError(final String msg) {
        this.assist.showError(msg);
    }

    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientError(java.lang.String, java.lang.String)
     */
    public final void showTransientError(final String title, final String message) {
        this.assist.showTransientError(title, message);
    }


    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientMessage(java.lang.String, java.lang.String)
     */
    public final void showTransientMessage(final String title, final String message) {
        this.assist.showTransientMessage(title, message);
    }

    public final void showTransientMessage(final Icon icon,final String title, final String message) {
        this.assist.showTransientMessage(icon,title, message);
    }

    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientWarning(java.lang.String, java.lang.String)
     */
    public final void showTransientWarning(final String title, final String message) {
        this.assist.showTransientWarning(title, message);
    }

}


/* 
$Log: UIComponentImpl.java,v $
Revision 1.25  2009/03/30 15:04:55  nw
added new method to allow provision of icon in popup.

Revision 1.24  2009/03/26 18:04:11  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.23  2008/11/04 14:35:51  nw
javadoc polishing

Revision 1.22  2008/05/09 11:32:48  nw
NEW - bug 2758: bug from help desk system - ubuntu and java version?
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2758
Incomplete - task 391: get to grips with new CDS

Revision 1.21  2007/11/26 12:01:48  nw
added framework for progress indication for background processes

Revision 1.20  2007/11/13 04:51:03  nw
Complete - task 229: menuing redesign.

Revision 1.19  2007/10/12 11:04:00  nw
refactored uiComponent implementations.

Revision 1.18  2007/09/21 16:35:14  nw
improved error reporting,
various code-review tweaks.

Revision 1.17  2007/09/13 13:45:56  nw
removed obsolete superclass.

Revision 1.16  2007/08/02 00:16:26  nw
nicer progress indicator

Revision 1.15  2007/07/12 10:15:42  nw
added icon to close action

Revision 1.14  2007/06/18 16:47:58  nw
javadoc fixes.

Revision 1.13  2007/05/02 15:38:29  nw
changes for 2007.3.alpha1

Revision 1.12  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/03/08 17:43:59  nw
first draft of voexplorer

Revision 1.10  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.9  2007/01/12 13:20:05  nw
made sure every ui app has a help menu.

Revision 1.8  2007/01/11 18:15:49  nw
fixed help system to point to ag site.

Revision 1.7  2006/08/04 15:30:22  jdt
typo

Revision 1.6  2006/07/20 12:32:56  nw
altered position of help button, so is visible on mac.

Revision 1.5  2006/06/27 19:14:56  nw
adjusted todo tags.

Revision 1.4  2006/06/27 10:37:10  nw
added methods to provide a default help menu

Revision 1.3  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.12.6.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.12  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.11  2005/12/02 13:41:20  nw
improved task-reporting system

Revision 1.10  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.9  2005/11/22 12:14:20  pjn3
Bug #1472 - clear status message once background task finishes

Revision 1.8  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.7  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.6  2005/10/18 16:52:49  nw
fixes to error-reporting

Revision 1.5  2005/10/18 08:37:44  nw
finished error reporting.

Revision 1.4  2005/10/17 10:49:03  KevinBenson
First draft of the change to the error dialog box.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.2.10.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.4  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.3  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.2  2005/04/22 10:55:32  nw
implemented vospace file chooser dialogue.

Revision 1.2.2.1  2005/04/15 13:00:45  nw
got vospace browser working.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/04/13 12:23:29  nw
refactored a common base class for ui components
 
*/