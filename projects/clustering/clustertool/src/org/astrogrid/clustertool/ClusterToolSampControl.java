
/*
 * $$Id: ClusterToolSampControl.java,v 1.3 2009/09/17 07:03:19 pah Exp $$
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.astrogrid.clustertool.ClustertoolView.LoadFileTask;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Metadata;
import org.astrogrid.samp.client.AbstractMessageHandler;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.HubConnector;
import org.astrogrid.samp.client.MessageHandler;
import org.jdesktop.application.TaskService;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.util.DataSource;
import uk.ac.starlink.util.FileDataSource;
import uk.ac.starlink.util.URLDataSource;
import uk.ac.starlink.util.URLUtils;
import uk.ac.starlink.util.gui.ErrorDialog;

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
        public Map processCall(HubConnection conn, final String senderId,
                final Message msg) throws Exception {

            /* Attempt to create a table from the message received. */
            Throwable error;
            StarTable table;
            boolean success;
            try {
                table = createTable( format_,
                                    (String) msg.getRequiredParam( "url" ) );
                error = null;
                success = true;
            }
            catch ( Throwable e ) {
                error = e;
                table = null;
                success = false;
            }

            /* Do something with the success or failure of the table creation
             * on the event dispatch thread. */
            final boolean success0 = success;
            final Throwable error0 = error;
            final StarTable table0 = table;
            final String tableId = (String) msg.getParam( "table-id" );
            final String tableName = (String) msg.getParam( "name" );
            
            controlWindow_.setFileName(msg.getRequiredParam( "url" ).toString());
            
            controlWindow_.loadFile(table0);
            
 
            /* Pass success/failure status back to the caller as for a 
             * message handler. */
            if ( success0 ) {
                return null;
            }
            else {
                if ( error0 instanceof Error ) {
                    throw (Error) error0;
                }
                else {
                    throw (Exception) error0;
                }
            }
        }
        /**
         * Constructs a table given a format and a URL.
         *
         * @param   format  table format string (as used by StarTableFactory)
         * @param   url   table location
         */
        private StarTable createTable( String format, String url )
                throws IOException {
            File file = URLUtils.urlToFile( url );
            DataSource datsrc =
                file != null
                    ? (DataSource) new FileDataSource( file )
                    : (DataSource) new URLDataSource( new URL( url ) );
            return controlWindow_.getTableFactory().makeStarTable( datsrc, format );
        }

    }
    


}
