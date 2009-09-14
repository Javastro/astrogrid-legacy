/*
 * $$Id: SampCommunicator.java,v 1.2 2009/09/14 19:09:26 pah Exp $$
 *
 * Created on 28-Aug-2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid
 * Software License, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */ 
package org.astrogrid.clustertool;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.astrogrid.samp.gui.GuiHubConnector;
import org.astrogrid.samp.gui.MessageTrackerHubConnector;
import org.astrogrid.samp.xmlrpc.HubMode;
import org.astrogrid.samp.xmlrpc.HubRunner;
import org.astrogrid.samp.xmlrpc.XmlRpcKit;


/**
 * Handles the SAMP communications.
 *
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 28-Aug-2009
 */
public class SampCommunicator implements ClusterToolCommunicator {

    private final GuiHubConnector hubConnector_;
    private final ClusterToolSampControl sampControl_;
    private final Transmitter tableTransmitter_;
    private int imageCount_;
    private static final HubMode INTERNAL_HUB_MODE = HubMode.NO_GUI;
    private static final HubMode EXTERNAL_HUB_MODE = HubMode.MESSAGE_GUI;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(SampCommunicator.class);
    

    public SampCommunicator( ClustertoolView controlWindow) throws IOException {
       hubConnector_ =
            new MessageTrackerHubConnector( ClusterToolServer.getInstance()
                                                        .getProfile() );
        sampControl_ = new ClusterToolSampControl( hubConnector_, controlWindow );
        tableTransmitter_ =
            new TableSendActionManager( hubConnector_, sampControl_ );
    }

    
   public boolean setActive() {
        hubConnector_.setActive( true );
        hubConnector_.setAutoconnect( 5 );
        try {
            return hubConnector_.getConnection() != null;
        }
        catch ( IOException e ) {
            logger.warn( "SAMP connection attempt failed: ", e );
            return false;
        }
    }


   public void addConnectionListener( ChangeListener listener ) {
       hubConnector_.addConnectionListener( listener );
   }


   public JComponent createInfoPanel() {
       Box box = Box.createHorizontalBox();
       box.add( Box.createHorizontalStrut( 5 ) );
       int boxHeight = 20;

       /* Add message tracker panel. */
       if ( hubConnector_ instanceof MessageTrackerHubConnector ) {
           JComponent mBox = ((MessageTrackerHubConnector) hubConnector_)
                            .createMessageBox( boxHeight );
           JLabel mLabel = new JLabel( "Messages: " );
           mLabel.setLabelFor( mBox );
           box.add( mLabel );
           box.add( mBox );
           box.add( Box.createHorizontalStrut( 10 ) );
       }

       /* Add client tracker panel. */
       JComponent cBox = hubConnector_.createClientBox( false, boxHeight );
       JLabel cLabel = new JLabel( "Clients: " );
       cLabel.setLabelFor( cBox );
       box.add( cLabel );
       box.add( cBox );
       box.add( Box.createHorizontalStrut( 10 ) );

       /* Add reg/unreg button/indicator. */
       final JButton regButton = new JButton( hubConnector_
                                             .createToggleRegisterAction() );
       hubConnector_.addConnectionListener( new ChangeListener() {
           public void stateChanged( ChangeEvent evt ) {
               regButton.setText( null );
               regButton.setIcon( hubConnector_.isConnected()
                                      ? ResourceIcon.CONNECT
                                      : ResourceIcon.DISCONNECT );
           }
       } );
       regButton.setText( null );
       regButton.setBorder( BorderFactory.createEmptyBorder() );
       box.add( regButton );
       box.add( Box.createHorizontalStrut( 5 ) );

       /* Wrap and return. */
       JComponent panel = new JPanel( new BorderLayout() );
       panel.setBorder( new TitledBorder(getProtocolName()) );
       panel.add( box, BorderLayout.CENTER );
       return panel;
   }


   public Action createWindowAction( final Component parent ) {
       return new BasicAction( "SAMP Status", ResourceIcon.SAMP,
                               "Show window displaying SAMP inter-tool "
                             + "messaging status and configuration" ) {
           private SampWindow sampWindow_;
           public void actionPerformed( ActionEvent evt ) {
               if ( sampWindow_ == null ) {
                   sampWindow_ = new SampWindow( hubConnector_ );
               }
               sampWindow_.setVisible(true);
           }
       };
   }


public Action[] getInteropActions() {
    return new Action[ 0 ];}


public String getProtocolName() {
    return "SAMP";
}


public Transmitter getTableTransmitter() {
    return tableTransmitter_;
}


public boolean isConnected() {
    return hubConnector_.isConnected();
}


public void startHub(boolean external) throws IOException {
    if ( external ) {
        HubRunner.runExternalHub( EXTERNAL_HUB_MODE );
    }
    else {
        HubRunner.runHub( INTERNAL_HUB_MODE, XmlRpcKit.INTERNAL );
    }
}

}
