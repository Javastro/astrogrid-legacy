
/*
 * $$Id: ClusterToolSampControl.java,v 1.1 2009/09/07 16:06:11 pah Exp $$
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

import java.util.Map;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Metadata;
import org.astrogrid.samp.client.AbstractMessageHandler;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.HubConnector;
import org.astrogrid.samp.client.MessageHandler;

/**
 *
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 28-Aug-2009
 */
public class ClusterToolSampControl {

    private final HubConnector hubConnector_;
    private final ClustertoolView controlWindow_;

    public ClusterToolSampControl(HubConnector hubConnector_, ClustertoolView controlWindow_) {
        this.hubConnector_ = hubConnector_;
        this.controlWindow_ = controlWindow_;

            Metadata meta = new Metadata();
        TopcatServer tcServer = TopcatServer.getInstance();
        URL tcPkgUrl = tcServer.getTopcatPackageUrl();
        String homepage = "http://www.starlink.ac.uk/topcat/";
        meta.setName( "clustertool" );
        meta.setDescriptionText( "Tool for statistical cluster analysis of data" );
        URL docUrl = new URL( tcPkgUrl, "sun253/index.html" );
        meta.setDocumentationUrl( tcServer.isFound( docUrl )
                                  ? docUrl.toString()
                                  : homepage );
        URL logoUrl = new URL( tcPkgUrl, "images/tc3.gif" );
        meta.setIconUrl( tcServer.isFound( logoUrl )
                             ? logoUrl.toString()
                             : homepage + "images/tc3.gif" );
        meta.put( "home.page", homepage );
        meta.put( "author.name", "Paul Harrison" );
        meta.put( "author.affiliation",
                  "AstroGrid, The University of Mancheser" );
        meta.put( "author.email", "paul.harrison@manchester.ac.uk" );
        meta.put( "clustertool.version", ClusterToolUtils.getVersion() );
        hubConnector_.declareMetadata( meta );

        /* Add MType-specific handlers and declare subscriptions. */
        MessageHandler[] handlers = createMessageHandlers();
        for ( int ih = 0; ih < handlers.length; ih++ ) {
            hubConnector_.addMessageHandler( handlers[ ih ] );
        }
        hubConnector_.declareSubscriptions( hubConnector_
                                           .computeSubscriptions() );

    }

    private MessageHandler[] createMessageHandlers() {
        return new MessageHandler[] {
          new TableLoadHandler( "table.load.votable", "votable" )
        };
    }



    private class TableLoadHandler extends AbstractMessageHandler {

        private final String mtype_;
        private final String format_;
        
        private TableLoadHandler(String mtype, String format) {
           super(mtype);
           this.mtype_ = mtype;
           this.format_ = format;
        }

        @Override
        public Map processCall(HubConnection arg0, String arg1, Message arg2) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    


}
