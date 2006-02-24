/*$Id: AvailabilityBean.java,v 1.1 2006/02/24 12:17:52 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import java.util.Date;

/**
 * description of the availability of a server
 * @since 1.9
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Feb-2006
 *
 */
public class AvailabilityBean {


    private final String location;
    private final String message;
    private final String serverName;
    private final Date validTo;
    private final Date upTime;
    private final Date timeOnServer;
    /** Construct a new AvailabilityBean
     * @param location
     * @param message
     * @param serverName
     * @param validTo
     * @param upTime
     * @param timeOnServer
     */
    public AvailabilityBean(String location, String message, String serverName, Date validTo, Date upTime, Date timeOnServer) {
        super();
        this.location = location;
        this.message = message;
        this.serverName = serverName;
        this.validTo = validTo;
        this.upTime = upTime;
        this.timeOnServer = timeOnServer;
    }
    /** the location of the service */
    public String getLocation() {
        return this.location;
    }
    /** a message from the service */
    public String getMessage() {
        return this.message;
    }
    /** the name of the service */
    public String getServerName() {
        return this.serverName;
    }
    /** current time on server */
    public Date getTimeOnServer() {
        return this.timeOnServer;
    }
    /** time server has been continuously running for */
    public Date getUpTime() {
        return this.upTime;
    }
    /** @todo find out what this field means */
    public Date getValidTo() {
        return this.validTo;
    }

}


/* 
$Log: AvailabilityBean.java,v $
Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/