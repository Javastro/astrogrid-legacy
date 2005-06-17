/*$Id: Main.java,v 1.4 2005/06/17 12:06:14 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import org.astrogrid.desktop.framework.Bootloader;
import org.astrogrid.desktop.framework.DefaultModuleRegistry;
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nanocontainer.integrationkit.PicoCompositionException;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Main class of the Astrogrid Workbench
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class Main implements Startable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Main.class);

    /** Construct a new workbench.
     * 
     */
    public Main() {
        super();
        try {
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY,    Boolean.TRUE);            
         } catch (Exception e) {
             logger.warn("Failed to install plastic look and feel - oh well");
             }
        
         // create initial container.
        pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(DefaultModuleRegistry.class);
        pico.registerComponentImplementation(DescriptorParser.class,DigesterDescriptorParser.class);
        pico.registerComponentImplementation(Bootloader.class);        
    }

    
    protected final MutablePicoContainer pico;

    /**
     * @see org.picocontainer.Startable#start()
     * starts the pico container - but on the swing event dispatch thread.
     */
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pico.start();
            }
        });
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }
    
    public static final void main(String[] args) {
        Main d = new Main();
        d.start();
    }


}


/* 
$Log: Main.java,v $
Revision 1.4  2005/06/17 12:06:14  nw
added changelog, made start on docs.
fixed race condition.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2  2005/04/13 12:59:10  nw
checkin from branch desktop-nww-998

Revision 1.1.4.2  2005/04/05 15:13:56  nw
changed default look and feel.

Revision 1.1.4.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/