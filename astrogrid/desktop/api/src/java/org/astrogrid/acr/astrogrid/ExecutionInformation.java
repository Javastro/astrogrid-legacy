/*$Id: ExecutionInformation.java,v 1.10 2008/09/25 16:02:04 nw Exp $
 * Created on 04-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.util.Date;

/**  Description of  the progress of a remote process - e.g. a workflow job, or CEA application.
 *  
 * <tt>getId()</tt> will return the execution identifier - either a job urn (for workfows) or an execution ivorn (for cea and other remote appilications).
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 04-Aug-2005
  * @xmlrpc returned as a struct, with keys corresponding to bean names
  * @bean
 *
 */
public class ExecutionInformation extends AbstractInformation {

    /** Construct a new JobInformation
     * @xmlrpc test
     */
    public ExecutionInformation(final URI id, final String name, final String description, final String status, final Date startTime,final Date finishTime) {
        super(name,id);
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    
    static final long serialVersionUID = -1822085536926552178L;
    /** constant value for {@link #getStatus()} */
    public static final String PENDING = "PENDING";
    /** constant value for {@link #getStatus()} */
    public static final String INITIALIZING = "INITIALIZING";
    /** constant value for {@link #getStatus()} */
    public static final String RUNNING = "RUNNING";
    /** constant value for {@link #getStatus()} */
    public static final String COMPLETED = "COMPLETED";
    /** constant value for {@link #getStatus()} */
    public static final String ERROR = "ERROR";
    /** constant value for {@link #getStatus()} */
    public static final String UNKNOWN = "UNKNOWN";

    protected final String description;
    protected final String status;
    protected final Date startTime;
    protected final Date finishTime;
    //@todo later add messages.

 

    
    /** description of the process being executed.
     * @return a string
     * @xmlrpc key will e <tt>description</tt>*/
    public String getDescription() {
        return this.description;
    }    
    
    /** the time the exection started 
     * @return  may be null if still pending. Also null for application executions at the moment 
     * @xmlrpc key will be <tt>startTime</tt>, type will be 'date'*/
    public Date getStartTime() {
        return this.startTime;
    }
    /** the time that execution finished 
     * @return may be null if still running. Also null for application execution at the moment 
     * @xmlrpc key will be <tt>startTime</tt>, type will be 'date'*/     
    public Date getFinishTime() {
        return this.finishTime;
    }    
    /** current status of this execution
     * @return one of the constants in this class */
    public String getStatus() {
        return this.status;
    }
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("[ExecutionInformation:");
        buffer.append(" id: ");
        buffer.append(id);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append(" status: ");
        buffer.append(status);
        buffer.append(" startTime: ");
        buffer.append(startTime);
        buffer.append(" finishTime: ");
        buffer.append(finishTime);
        buffer.append("]");
        return buffer.toString();
    }

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.finishTime == null) ? 0 : this.finishTime.hashCode());
		result = PRIME * result + ((this.startTime == null) ? 0 : this.startTime.hashCode());
		result = PRIME * result + ((this.status == null) ? 0 : this.status.hashCode());
		return result;
	}

	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (!super.equals(obj)) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		final ExecutionInformation other = (ExecutionInformation) obj;
		if (this.description == null) {
			if (other.description != null) {
                return false;
            }
		} else if (!this.description.equals(other.description)) {
            return false;
        }
		if (this.finishTime == null) {
			if (other.finishTime != null) {
                return false;
            }
		} else if (!this.finishTime.equals(other.finishTime)) {
            return false;
        }
		if (this.startTime == null) {
			if (other.startTime != null) {
                return false;
            }
		} else if (!this.startTime.equals(other.startTime)) {
            return false;
        }
		if (this.status == null) {
			if (other.status != null) {
                return false;
            }
		} else if (!this.status.equals(other.status)) {
            return false;
        }
		return true;
	}
}


/* 
$Log: ExecutionInformation.java,v $
Revision 1.10  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.9  2008/02/12 17:35:24  pah
added bean annotation

Revision 1.8  2007/11/12 13:36:28  pah
change parameter name to structure

Revision 1.7  2007/01/24 14:04:44  nw
updated my email address

Revision 1.6  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.5  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.4.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.4  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.3  2005/08/16 13:14:42  nw
added 'name' as a common field for all information objects

Revision 1.2  2005/08/12 08:45:16  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/