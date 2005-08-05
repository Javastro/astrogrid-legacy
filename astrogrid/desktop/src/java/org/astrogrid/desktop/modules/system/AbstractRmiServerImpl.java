/*$Id: AbstractRmiServerImpl.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.system.RmiServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.rmi.registry.Registry;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public abstract class AbstractRmiServerImpl implements RmiServer{

    /** Construct a new AbstractRmiServerImpl
     * @throws Exception
     * 
     */
    public AbstractRmiServerImpl() throws Exception {
        super();
        findSparePort();
        recordDetails();
    }

    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(AbstractRmiServerImpl.class);
    private int port;
    public static final int START_SCAN_PORT = Registry.REGISTRY_PORT;
    public static final int END_SCAN_PORT = Registry.REGISTRY_PORT + 1000;

    private void findSparePort() throws Exception {
        ServerSocket ss = null;
        for (int i = START_SCAN_PORT; i < END_SCAN_PORT; i++) {
            try {
                ss = new ServerSocket(i);            
                port = i;
                logger.info("Rmi Server will listen on port " + port);
                break;
            } catch (IOException e) {    // oh well, that port is already taken. try another.
            } finally {
                if (ss != null) {
                    try {
                        ss.close();
                    } catch (IOException e) {
                        // ignore.
                    }
                }
            }
        } 
        if (port == 0) {
            throw new Exception("Could not find a free port for RMI Server");
        }
    }

    private void recordDetails() throws IOException {
        File f = Finder.configurationFile();
        if (f.exists()) {
            f.delete();
        }
        f.deleteOnExit();
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        pw.println(port);
        pw.close();
    }

    /**
     * @see org.astrogrid.acr.system.RmiServer#getPort()
     */
    public int getPort() {
        return port;
    }

}


/* 
$Log: AbstractRmiServerImpl.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/