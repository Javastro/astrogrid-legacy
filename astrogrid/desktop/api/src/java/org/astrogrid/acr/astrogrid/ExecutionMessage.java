/*$Id: ExecutionMessage.java,v 1.8 2009/03/30 15:02:55 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;
import java.util.Date;

/** A single message returned by a remote process - e.g.
 * a workflow job or remote application
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 * @bean
 */
public class ExecutionMessage implements Serializable{

    /** Construct a new ExecutionMessage
     * 
     */
    public ExecutionMessage(String source, String level, String status, Date timestamp, String content) {
        this.content = content;
        this.level = level;
        this.status = status;
        this.timestamp = timestamp;
        this.source = source;
    }
    
    static final long serialVersionUID = 2339344360407684926L;
    private final String content;
    private final String level;
    private final String status;
    private final Date timestamp;
    private final String source;

    /** the message body - text */
    public String getContent() {
        return this.content;
    }
    /** the level/ importance of the message.
     * 
     * Expected values <tt>information|warning|error</tt>*/
    public String getLevel() {
        return this.level;
    }
    /** the originator of this message */
    public String getSource() {
        return this.source;
    }
    /** the status of the process when this message was sent.
     * 
     * Expected values <tt>unknown|pending|initializing|running|completed|error</tt>*/
    public String getStatus() {
        return this.status;
    }
    /** the time this method was sent */
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("ExecutionMessage[");
        sb.append(" content: ");
        sb.append(content);
        sb.append(" level: ");
        sb.append(level);
        sb.append(" status: ");
        sb.append(status);
        sb.append(" timestamp: ");
        sb.append(timestamp);
        sb.append(" source: ");
        sb.append(source);
        sb.append("]");
        return sb.toString();
    }
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.content == null) ? 0 : this.content.hashCode());
		result = PRIME * result + ((this.level == null) ? 0 : this.level.hashCode());
		result = PRIME * result + ((this.source == null) ? 0 : this.source.hashCode());
		result = PRIME * result + ((this.status == null) ? 0 : this.status.hashCode());
		result = PRIME * result + ((this.timestamp == null) ? 0 : this.timestamp.hashCode());
		return result;
	}
	@Override
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ExecutionMessage other = (ExecutionMessage) obj;
		if (this.content == null) {
			if (other.content != null)
				return false;
		} else if (!this.content.equals(other.content))
			return false;
		if (this.level == null) {
			if (other.level != null)
				return false;
		} else if (!this.level.equals(other.level))
			return false;
		if (this.source == null) {
			if (other.source != null)
				return false;
		} else if (!this.source.equals(other.source))
			return false;
		if (this.status == null) {
			if (other.status != null)
				return false;
		} else if (!this.status.equals(other.status))
			return false;
		if (this.timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!this.timestamp.equals(other.timestamp))
			return false;
		return true;
	}
}


/* 
$Log: ExecutionMessage.java,v $
Revision 1.8  2009/03/30 15:02:55  nw
Added override annotations.

Revision 1.7  2008/02/12 17:35:24  pah
added bean annotation

Revision 1.6  2007/01/24 14:04:44  nw
updated my email address

Revision 1.5  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.6.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.3.6.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.3  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.2  2005/11/11 10:09:01  nw
improved javadoc

Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/