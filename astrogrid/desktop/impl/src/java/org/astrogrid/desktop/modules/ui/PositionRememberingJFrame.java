/*$Id: PositionRememberingJFrame.java,v 1.11 2007/04/18 15:47:05 nw Exp $
 * Created on 04-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** Extended jFrame baseclass that remembers positioning of the window.
 * <p>
 * on hide / setVisible(false) stores current window size and position in the
 * {@link org.astrogrid.acr.system.Configuration} component
 * <p>
 * on show / setVisible(true) reloads this position information to initialize the component.
 *iif can't get previously-stored position information, will set position relative to the ui component.
 * <p>
 * key used in configuration for this info is the classname
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Apr-2005
 *
 */
public class PositionRememberingJFrame extends JFrame {


    /** Conostruct a new PositionRememberingJFrame that will persist window position 
     * @param context @todo*/
    public PositionRememberingJFrame(UIContext context) throws HeadlessException {
    	if (context == null) {
    		throw new IllegalArgumentException("UIContext must not be null");
    	}
        this.context = context;
        addWindowListener(new WindowAdapter() {
        	public void windowOpened(WindowEvent e) {
        		loadConfiguration();
        	}
        	public void windowClosing(WindowEvent e) {
        		saveConfiguration();
        	}
        });
    }
 

    private final UIContext context;
    
    public final UIContext getContext() {
    	return context;
    }
	    
    
    /** load position from configuration */
    private void loadConfiguration() {
        Configuration configuration = context.getConfiguration();
            String xString = configuration.getKey(this.getClass().getName()+".x");
            String yString = configuration.getKey(this.getClass().getName()+".y");
            Dimension dim = getToolkit().getScreenSize();            
           
            if (xString != null && yString != null){
                try {
                    int x = Integer.parseInt(xString);
                    int y = Integer.parseInt(yString);
                    if (x >= 0 && x <dim.width && y >= 0 && y < dim.height) {
                        this.setLocation(x,y);
                        return;
                    }
                } catch (NumberFormatException e) {
                    // oh well, fall back then..
                }
            }
       

    }
        
   /** save position to configuration */
   private void saveConfiguration() {
       Configuration configuration = context.getConfiguration();
        Point p = null;
        try {
            p = getLocationOnScreen();
        } catch (IllegalComponentStateException e) { // not visible - not to worry.
            return;
        }
        Dimension dim = getToolkit().getScreenSize();
        if (p != null && p.x >= 0 && p.x <= dim.width && p.y >= 0 && p.y <= dim.height) {
            configuration.setKey(this.getClass().getName() + ".x","" + p.x);
            configuration.setKey(this.getClass().getName()+".y","" + p.y);
        }
    }
    
    
    public void setVisible(boolean b) {
        if(this.isVisible() != b) {
        	if (b) {
            	loadConfiguration();         
        	}  else {
        		saveConfiguration();
        	}
        }
        super.setVisible(b);
        if (b) {
            toFront();
            requestFocus();        	
        }
    }

    public void show() {
        if (!this.isVisible()) {
            loadConfiguration();
        }
        super.show();
        toFront();
        requestFocus();
    }
    
    public void hide() {
    	saveConfiguration();
    	super.hide();
    }
}


/* 
$Log: PositionRememberingJFrame.java,v $
Revision 1.11  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.10  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.9  2006/06/27 10:36:13  nw
findbugs tweaks

Revision 1.8  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.7.26.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.7.26.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.7  2005/12/02 17:04:28  nw
minor change

Revision 1.6  2005/11/15 19:39:07  nw
merged in improvements from release branch.

Revision 1.5.10.1  2005/11/15 19:34:41  nw
added cleanup on window close.

Revision 1.5  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.4  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.10.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.3  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:50  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/05 11:42:40  nw
tidied imports

Revision 1.1.2.1  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust
 
*/