/*$Id: PositionRememberingJFrame.java,v 1.4 2005/06/20 16:56:40 nw Exp $
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
import org.astrogrid.acr.system.UI;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;

import javax.swing.JFrame;

/** Extended jFrame baseclass that remembers positioning of the window.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Apr-2005
 *
 */
public class PositionRememberingJFrame extends JFrame {

    /** Construct a new PositionRememberingJFrame
     * @throws java.awt.HeadlessException
     */
    public PositionRememberingJFrame() throws HeadlessException {
        super();
        this.configuration = null;
        this.ui = null;
        this.help = null;
    }

    public PositionRememberingJFrame(Configuration conf,HelpServer help,UI ui) throws HeadlessException {
        this.configuration = conf;
        this.ui = ui;
        this.help = help;
    }
 
    
    protected final Configuration configuration;
    protected final UI ui;
    protected final HelpServer help;

    protected void loadConfiguration() {
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
        
        
   protected void saveConfiguration() {
        if (configuration == null) {
            return;
        }
        Point p = getLocationOnScreen();  
        Dimension dim = getToolkit().getScreenSize();
        if (p != null && p.x >= 0 && p.x <= dim.width && p.y >= 0 && p.y <= dim.height) {
            configuration.setKey(this.getClass().getName() + ".x","" + p.x);
            configuration.setKey(this.getClass().getName()+".y","" + p.y);
        }
    }
    
    
    /*
    public void dispose() {
        saveConfiguration();
        super.dispose();
    }*/
    public void hide() {
        saveConfiguration();
        super.hide();
    }
    public void show() {
        loadConfiguration();
        super.show();
    }
}


/* 
$Log: PositionRememberingJFrame.java,v $
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