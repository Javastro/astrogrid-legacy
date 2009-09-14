
/*
 * $$Id: ClusterToolSampControl.java,v 1.2 2009/09/14 19:09:26 pah Exp $$
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
import java.net.MalformedURLException;
import java.net.URL;
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
public ClustertoolView getControlWindow_() {
        return controlWindow_;
    }



/** logger for this class */
private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
        .getLog(ClusterToolSampControl.class);

    public ClusterToolSampControl(HubConnector hubConnector_, ClustertoolView controlWindow_) throws IOException {
        this.hubConnector_ = hubConnector_;
        this.controlWindow_ = controlWindow_;

            Metadata meta = new Metadata();
        ClusterToolServer tcServer = ClusterToolServer.getInstance();
 //       URL tcPkgUrl = tcServer.getTopcatPackageUrl();
        String homepage;
        homepage = "http://software.astrogrid.org/clustertool/";
        try {
            URL tcPkgUrl = new URL("http://localhost/");
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
        } catch (MalformedURLException e) {
           logger.error("problem with identity metadata", e);
        }
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
