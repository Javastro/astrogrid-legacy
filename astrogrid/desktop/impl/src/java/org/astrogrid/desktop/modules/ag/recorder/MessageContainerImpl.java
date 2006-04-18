/*$Id: MessageContainerImpl.java,v 1.3 2006/04/18 23:25:46 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.Serializable;

import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;

/**
 * implementation of the message data object
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
 *
 */
public class MessageContainerImpl implements Serializable, MessageContainer{
    static final long serialVersionUID = 12345689123456789L;    
    public MessageContainerImpl(String summary,ExecutionMessage msg) {
        this.summary = summary;
        this.msg = msg;
        this.unread = true;
    }
    private final ExecutionMessage msg;
    private boolean unread;
    private Long key;
    private final String summary;

    public boolean isUnread() {
        return unread;
    }
    public void setUnread(boolean b) {
        this.unread= b;
    }
    public String getSummary() {
        return this.summary;
    }

    public ExecutionMessage getMessage() {
        return msg;
    }
    public void setKey(Long key) {
        this.key = key;
    }
    public Long getKey() {
        return key;
    }
}

/* 
$Log: MessageContainerImpl.java,v $
Revision 1.3  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.2.34.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/