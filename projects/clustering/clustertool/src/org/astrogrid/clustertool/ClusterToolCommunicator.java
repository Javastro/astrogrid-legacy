package org.astrogrid.clustertool;

import java.awt.Component;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * Abstract interface for inter-application messaging requirements of TOPCAT.
 * This can be implemented by SAMP or PLASTIC (or others).
 *
 * @author   Mark Taylor
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) Sep 14, 2009
 * @since    4 Sep 2008
 */
public interface ClusterToolCommunicator {

    /**
     * Returns the name of the protocol over which this object is implemented.
     *
     * @return   protocol name
     */
    String getProtocolName();

    /**
     * Must be called before any of the actions provided by this object
     * are used.  May initiate communication with the messaging system etc.
     *
     * @return  true iff there is a current connection
     */
    boolean setActive();

    /**
     * Returns a list of actions suitable for insertion in a general purpose
     * menu associated with interoperability functionality
     * (register/unregister etc).
     * 
     * @return   action list
     */
    Action[] getInteropActions();

    /**
     * Returns an object that can send send the currently selected
     * table from TOPCAT to other applications.
     *
     * @return  table transmitter
     */
    Transmitter getTableTransmitter();




  



   


  

   

    /**
     * Attempts to start a messaging hub suitable for use with this object.
     *
     * @param  external  true to run hub in external JVM,
     *                   false to run it in the current one
     */
    void startHub( boolean external ) throws IOException;

    /**
     * Optionally returns a panel which can be displayed in the control
     * window to show communications status.
     *
     * @return   status component, or null if unimplemented
     */
    JComponent createInfoPanel();

    /**
     * Constructs an action which will display a control window for this
     * communicator.
     *
     * @param   parent  parent component
     * @return   communicator control window, or null if none is available
     */
    Action createWindowAction( Component parent );

    /**
     * Indicates (without attempting a connection) whether a hub connection is
     * currently in force.
     *
     * @return   whether hub is connected
     */
    boolean isConnected();

    /**
     * Adds a listener which will be notified any time that connection status
     * may have changed.
     *
     * @param   listener   listener to add
     */
    void addConnectionListener( ChangeListener listener );
}
