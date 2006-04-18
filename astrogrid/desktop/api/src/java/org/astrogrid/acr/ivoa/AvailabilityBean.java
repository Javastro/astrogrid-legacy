/*$Id: AvailabilityBean.java,v 1.2 2006/04/18 23:25:45 nw Exp $
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

import java.io.Serializable;
import java.util.Date;

/**
 * description of the availability of a server
 * @since 1.9
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Feb-2006
 *
 */
public class AvailabilityBean implements Serializable {


    static final long serialVersionUID = 3561008426242896164L;
    private final String location;
    private final String message;
    private final String serverName;
    private final String validTo;
    private final String upTime;
    private final String timeOnServer;
    /** Construct a new AvailabilityBean
     * @param location
     * @param message
     * @param serverName
     * @param validTo
     * @param upTime
     * @param timeOnServer
     */
    public AvailabilityBean(String serverName, String location, String message, String validTo, String upTime, String timeOnServer) {
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
    public String getTimeOnServer() {
        return this.timeOnServer;
    }
    /** time server has been continuously running for */
    public String getUpTime() {
        return this.upTime;
    }
    /** @todo find out what this field means */
    public String getValidTo() {
        return this.validTo;
    }
   
    public String toString() {
        StringBuffer sb = new StringBuffer("AvailabilityBean[");
        sb.append("ServerName: ");
        sb.append(serverName);
        sb.append(", Location: ");
        sb.append(location);
        sb.append(", Message: ");
        sb.append(message);
        sb.append(", UpTime: ");
        sb.append(upTime);
        sb.append(", TimeOnServer: ");
        sb.append(timeOnServer);
        sb.append(", ValidTo: ");
        sb.append(validTo);
        sb.append("]");
        return sb.toString();
    }

}


/* 
$Log: AvailabilityBean.java,v $
Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/