/*$Id: PositionRememberingJFrame.java,v 1.5 2005/11/01 09:19:46 nw Exp $
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

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.Point;

import javax.swing.JFrame;

/** Extended jFrame baseclass that remembers positioning of the window.
 * <p>
 * on hide / setVisible(false) stores current window size and position in the
 * {@link org.astrogrid.acr.system.Configuration} component
 * <p>
 * on show / setVisible(true) reloads this position information to initialize the component.
 *iif can't get previously-stored position information, will set position relative to the ui component.
 * <p>
 * key used in configuration for this info is the classname
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Apr-2005
 *
 */
public class PositionRememberingJFrame extends JFrame {

    /** Construct a new PositionRememberingJFrame, that won't persist or restore position
     * @throws java.awt.HeadlessException
     */
    public PositionRememberingJFrame() throws HeadlessException {
        super();
        this.configuration = null;
        this.ui = null;
        this.help = null;
    }
    /** Conostruct a new PositionRememberingJFrame that will persist window position */
    public PositionRememberingJFrame(Configuration conf,HelpServerInternal help,UIInternal ui) throws HeadlessException {
        this.configuration = conf;
        this.ui = ui;
        this.help = help;
    }
 
    
    protected final Configuration configuration;
    protected final UIInternal ui;
    protected final HelpServerInternal help;

    /** convenience method - access the configuraiton componoent */
    public Configuration getConfiguration() {
        return configuration;
    }
    /** convenience mehtod - access the help server component */
    public HelpServerInternal getHelpServer() {
        return help;
    }
    /** convenience method - access the main ui component */
    public UIInternal getUI() {
        return ui;
    }
    
    /** load position from configuration */
    private void loadConfiguration() {
        if (configuration != null) {
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
        if (ui != null) {
            this.setLocationRelativeTo(ui.getComponent());
        }
    }
        
   /** save position to configuration */
   private void saveConfiguration() {
        if (configuration == null) {
            return;
        }
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
        } else {
            saveConfiguration();
        }
        super.setVisible(b);
        }
    }
    public void hide() {
        if (this.isVisible()) {
            saveConfiguration();
            super.hide();
        }
    }
    public void show() {
        if (!this.isVisible()) {
            loadConfiguration();
            super.show();
        }
    }
}


/* 
$Log: PositionRememberingJFrame.java,v $
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