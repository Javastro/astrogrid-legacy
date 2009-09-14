package org.astrogrid.clustertool;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;

import org.astrogrid.samp.gui.GuiHubConnector;

/**
 * Window for display of SAMP operations.
 *
 * @author   Mark Taylor
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) Sep 14, 2009
 * @since    10 Dec 2008
 */
public class SampWindowOld extends JFrame {

    private final GuiHubConnector connector_;

    /**
     * Constructor.
     *
     * @param   parent   parent component
     * @param   hubConnector  hub connector
     */
    public SampWindowOld( Component parent, GuiHubConnector hubConnector ) {
        super( "SAMP Control" );
        connector_ = hubConnector;
        JComponent main = this.getRootPane();
        main.setLayout( new BorderLayout() );
        main.add( hubConnector.createMonitorPanel(), BorderLayout.CENTER );

        /* Set up window-specific actions. */
        Action connectAct =
            hubConnector.createRegisterOrHubAction( this, null );
//        connectAct.putValue( Action.SMALL_ICON, ResourceIcon.CONNECT );

        /* Connection menu. */
        JMenu connectMenu = new JMenu( "Connect" );
        connectMenu.setMnemonic( KeyEvent.VK_C );
        connectMenu.add( connectAct );
//        getJMenuBar().add( connectMenu );

    }
}
