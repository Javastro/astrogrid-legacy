/*$Id: AvailabilityBean.java,v 1.8 2008/10/07 10:00:53 nw Exp $
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

/** @exclude 
 * not provided by any service - yet.
 * Description of the availability of a server
  * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Feb-2006
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
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.location == null) ? 0 : this.location.hashCode());
		result = PRIME * result + ((this.message == null) ? 0 : this.message.hashCode());
		result = PRIME * result + ((this.serverName == null) ? 0 : this.serverName.hashCode());
		result = PRIME * result + ((this.timeOnServer == null) ? 0 : this.timeOnServer.hashCode());
		result = PRIME * result + ((this.upTime == null) ? 0 : this.upTime.hashCode());
		result = PRIME * result + ((this.validTo == null) ? 0 : this.validTo.hashCode());
		return result;
	}
	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		final AvailabilityBean other = (AvailabilityBean) obj;
		if (this.location == null) {
			if (other.location != null) {
                return false;
            }
		} else if (!this.location.equals(other.location)) {
            return false;
        }
		if (this.message == null) {
			if (other.message != null) {
                return false;
            }
		} else if (!this.message.equals(other.message)) {
            return false;
        }
		if (this.serverName == null) {
			if (other.serverName != null) {
                return false;
            }
		} else if (!this.serverName.equals(other.serverName)) {
            return false;
        }
		if (this.timeOnServer == null) {
			if (other.timeOnServer != null) {
                return false;
            }
		} else if (!this.timeOnServer.equals(other.timeOnServer)) {
            return false;
        }
		if (this.upTime == null) {
			if (other.upTime != null) {
                return false;
            }
		} else if (!this.upTime.equals(other.upTime)) {
            return false;
        }
		if (this.validTo == null) {
			if (other.validTo != null) {
                return false;
            }
		} else if (!this.validTo.equals(other.validTo)) {
            return false;
        }
		return true;
	}
	/** Construct a new AvailabilityBean
     * @param location
     * @param message
     * @param serverName
     * @param validTo
     * @param upTime
     * @param timeOnServer
     */
    public AvailabilityBean(final String serverName, 
            final String location, 
            final String message, 
            final String validTo, 
            final String upTime, 
            final String timeOnServer) {
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
        final StringBuffer sb = new StringBuffer("AvailabilityBean[");
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
Revision 1.8  2008/10/07 10:00:53  nw
reformatting

Revision 1.7  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.6  2007/03/08 17:48:06  nw
tidied.

Revision 1.5  2007/01/24 14:04:45  nw
updated my email address

Revision 1.4  2006/08/31 20:22:13  nw
doc fix.

Revision 1.3  2006/06/15 09:01:42  nw
provided implementations of equals()

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/