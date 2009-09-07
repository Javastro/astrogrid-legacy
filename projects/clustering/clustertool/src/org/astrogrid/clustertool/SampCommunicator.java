/*
 * $$Id: SampCommunicator.java,v 1.1 2009/09/07 16:06:09 pah Exp $$
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


import java.io.IOException;
import org.astrogrid.samp.gui.GuiHubConnector;
import org.astrogrid.samp.gui.MessageTrackerHubConnector;
import org.astrogrid.samp.xmlrpc.HubMode;

/**
 * Handles the SAMP communications.
 *
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 28-Aug-2009
 */
public class SampCommunicator {

       private final GuiHubConnector hubConnector_;
    private final ClusterToolSampControl sampControl_;
    private final Transmitter tableTransmitter_;
    private int imageCount_;
    private static final HubMode INTERNAL_HUB_MODE = HubMode.NO_GUI;
    private static final HubMode EXTERNAL_HUB_MODE = HubMode.MESSAGE_GUI;

    public SampCommunicator(GuiHubConnector hubConnector_ , ClusterToolView controlWindow) throws IOException {
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
            logger_.warning( "SAMP connection attempt failed: " + e );
            return false;
        }
    }

}
