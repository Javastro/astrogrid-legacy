/*
 * $Id: GetHostnameTask.java,v 1.1 2004/09/10 18:26:26 pah Exp $
 * 
 * Created on 10-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.deployment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Ant task to find the full hostname. This should hopefully work on unix and
 * windows, better than trying to rely on environment variables.
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 10-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class GetHostnameTask extends Task {

    private boolean ip = false;
    /**
     *  
     */
    public GetHostnameTask() {
        super();
       
    }
    
    public void execute() throws BuildException  {
       
            try {
                InetAddress addr = InetAddress.getLocalHost();
                String hostname = "";
                if (ip) {
                    byte[] raw = addr.getAddress();
                    for (int i = 0; i < raw.length; i++) {
                        int b = (int)raw[i];
                        if (b < 0)
                            b += 256;
                        hostname += String.valueOf(b);
                        if (i < raw.length - 1)
                            hostname += ".";
                    }
                }
                else {
                    hostname = addr.getCanonicalHostName();
                }
                getProject().setUserProperty("astrogrid.hostname", hostname);
            }
            catch (UnknownHostException e) {
               throw new BuildException("cannot get the hostname", e );
            }
        }
         /**
          * set this to true to make the output a pure ip address rather than the dns name.
         * @param ip
         */
        public void setIp(boolean ip) {
        this.ip = ip;
    }
}

    

