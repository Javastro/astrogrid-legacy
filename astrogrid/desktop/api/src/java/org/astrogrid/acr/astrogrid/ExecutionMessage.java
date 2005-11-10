/*$Id: ExecutionMessage.java,v 1.1 2005/11/10 12:13:52 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *@todo document
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

    public String getContent() {
        return this.content;
    }
    public String getLevel() {
        return this.level;
    }
    public String getSource() {
        return this.source;
    }
    public String getStatus() {
        return this.status;
    }
    public Date getTimestamp() {
        return this.timestamp;
    }
}


/* 
$Log: ExecutionMessage.java,v $
Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/