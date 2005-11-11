/*$Id: ExecutionMessage.java,v 1.2 2005/11/11 10:09:01 nw Exp $
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

/** Information bean describing a single message returned by a running remote process - e.g.
 * a workflow or cea task
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
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
    
    private final String content;
    private final String level;
    private final String status;
    private final Date timestamp;
    private final String source;

    /** access the message body - text */
    public String getContent() {
        return this.content;
    }
    /** access the level/ importance of the message - expected values <tt>information|warning|error</tt>*/
    public String getLevel() {
        return this.level;
    }
    /** the originator of this message */
    public String getSource() {
        return this.source;
    }
    /** current status of the process -expected values <tt>unknown|pending|initializing|running|completed|error</tt>*/
    public String getStatus() {
        return this.status;
    }
    /** access time this method was sent */
    public Date getTimestamp() {
        return this.timestamp;
    }
}


/* 
$Log: ExecutionMessage.java,v $
Revision 1.2  2005/11/11 10:09:01  nw
improved javadoc

Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/