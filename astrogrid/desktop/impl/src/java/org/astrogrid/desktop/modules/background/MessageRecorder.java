/*$Id: MessageRecorder.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.background;


import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

/** persists messages, and provides a gui-friendly model of them.
 * NB: quite a stateful service- tree model isn't too bad, but the table model might be a problem.
 * maybe a case for factoring this out into a separate class - so different clients can create their own instances.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 *
 */
public interface MessageRecorder {
    /** get a tree model of the message folders. nodes in the tree model will be {@link #Folder} objects */
    TreeModel getFolderList();

    /** get a table model that will display messages in a single folder */
    TableModel getMessageList();

    /** delete a folder, and any messages associated with it */
    void deleteFolder(Folder f) throws IOException;

    /** instruct the messageList table model to display messages in a folder */
    void displayMessages(Folder f) throws IOException;

    /** delete a message in the current folder at specified row in the table model */
    void deleteMessage(int row) throws IOException;

    /** get message object for specified row in the table model */
    Message getMessage(int row) throws IOException;
    
    /** save an updated version of a message */
    void updateMessage(Message m) throws IOException;
    /** save an updated version of a folder */
    void updateFolder(Folder f) throws IOException;

    /** list messages in a folder */
    Message[] listFolder(Folder f) throws IOException;
    
    /** interface to a folder object */
    public static interface Folder {
        /** some kind of user object for this folder */
        public Serializable getUserObject();
        public void setUserObject(Serializable s);
        /** unique key for this folder */
        public String getKey();
    }  
    
    /** interface to a message object */
    public static interface Message {
        /** whether this message has been read yet */
        public boolean isUnread();
        public void setUnread(boolean b);
        public String getSummary();
        public String getText();
        public Date getTimestamp();
    }    
}

/* 
 $Log: MessageRecorder.java,v $
 Revision 1.1  2005/11/01 09:19:46  nw
 messsaging for applicaitons.
 
 */