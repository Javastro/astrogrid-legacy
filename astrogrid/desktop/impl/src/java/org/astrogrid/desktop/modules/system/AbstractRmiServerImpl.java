/*$Id: AbstractRmiServerImpl.java,v 1.12 2008/11/04 14:35:49 nw Exp $
 * Created on 27-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.Registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.Finder;

/** Abstract class for implementations of the RmiServer.
 * implements the scanning bit.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jul-2005
 *
 */
public abstract class AbstractRmiServerImpl implements RmiServerInternal{


    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(AbstractRmiServerImpl.class);
    protected int port  = -1;
    private File connectionFile = Finder.configurationFile();
    public static final int SCAN_START_PORT_DEFAULT = Registry.REGISTRY_PORT;
    public static final int SCAN_END_PORT_DEFAULT = SCAN_START_PORT_DEFAULT + 1000;
    protected int scanStartPort = SCAN_START_PORT_DEFAULT;
    protected int scanEndPort = SCAN_END_PORT_DEFAULT;
    protected boolean disableConnectionFile = false;


    //private String connection
    
    protected void recordDetails() throws IOException {
        if (connectionFile.exists()) {
            connectionFile.delete();
        }
        connectionFile.deleteOnExit();
        final PrintWriter pw = new PrintWriter(new FileWriter(connectionFile));
        pw.println(port);
        pw.close();
    }

    /**
     * @see org.astrogrid.acr.system.RmiServer#getPort()
     */
    public int getPort() {
        return port;
    }
    
    /** setters - only for use while constructing */
    public void setPort(final int port) {
        this.port = port;
    }

    /** setters - only for use while constructing */
    public void setScanEndPort(final int scanEndPort) {
        this.scanEndPort = scanEndPort;
    }

    /** setters - only for use while constructing */
    public void setScanStartPort(final int scanStartPort) {
        this.scanStartPort = scanStartPort;
    }

    public void setConnectionFile(final String connectionFile) {
        this.connectionFile = new File(connectionFile);
    }

	/**
	 * @return the disableConnectionFile
	 */
	public boolean isDisableConnectionFile() {
		return this.disableConnectionFile;
	}

	/**
	 * @param disableConnectionFile the disableConnectionFile to set
	 */
	public void setDisableConnectionFile(final boolean disableConnectionFile) {
		this.disableConnectionFile = disableConnectionFile;
	}

	

}


/* 
$Log: AbstractRmiServerImpl.java,v $
Revision 1.12  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.11  2008/08/04 16:37:23  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.10  2008/07/30 11:05:34  nw
Complete - task 79: connect to localhost or to network port??

Revision 1.9  2007/10/22 10:29:54  nw
factored common inet-address code into separate helper class.

Revision 1.8  2007/10/12 11:02:49  nw
added code for selftesting

Revision 1.7  2007/06/17 17:03:17  jl99
Merge of branch workbench-jl-2152b.
First Query Builder bug fix after VOExplorer development.

Revision 1.6.6.2  2007/06/08 18:09:13  jl99
Further addition to logging the address on INFO level

Revision 1.6.6.1  2007/06/05 07:53:20  jl99
Enhanced exception thrown by findSparePort() to report address as well as start and end port.

Revision 1.6  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.5  2006/06/27 10:38:34  nw
findbugs tweaks

Revision 1.4  2006/04/26 15:56:18  nw
made servers more configurable.added standalone browser launcher

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.60.3  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.2.60.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/