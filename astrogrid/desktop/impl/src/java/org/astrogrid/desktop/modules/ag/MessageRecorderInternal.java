/*$Id: MessageRecorderInternal.java,v 1.2 2005/11/24 01:13:24 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ag;


import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

/** persists messages, and provides a gui-friendly model of them, plus basic query / management methods.
 * <P>
 * a low level service - so only provides an internal interface. User-level services (such as jes ,cea) provide 
 * a task-specific facade onto parts of the functionality of this service.
 * 
 * NB: quite a stateful service- tree model isn't too bad, but the table model might be a problem.
 * maybe a case for factoring this out into a separate class - so different clients can create their own instances.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 *
 */
public interface MessageRecorderInternal {
    
    
    public void addRecorderListener(RecorderListener listener);
    public void removeRecorderListener(RecorderListener listener);
    
    public interface RecorderListener {
        public void messageReceived(Folder f,MessageContainer msg);
    }
    
    /** get a tree model of the message folders. nodes in the tree model will be {@link #Folder} objects */
    TreeModel getFolderList();

    /** get a table model that will display messages in a single folder */
    TableModel getMessageList();

// current-selection oriented methods - depend on current state of display, and so shouldn't be
    // called from backgrund threads.
    //@todo if Ii find I need more than one of these, I just roll the following 3 methods inito a custom
    // subclass of tableModel, and replace getMessageList() with createMessageList()
    /** instruct the messageList table model to display messages in a folder */
    void displayMessages(Folder f) throws IOException;

    /** delete a message in the current folder at specified row in the table model */
    void deleteMessage(int row) throws IOException;

    /** get message object for specified row in the table model */
    MessageContainer getMessage(int row) ;

// stateless methods

    /** get folder by id 
     * @return - the folder, or null*/
    Folder getFolder(URI id) throws IOException;
    
    public List listLeaves() throws IOException ;
    /** save an updated version of a folder */
    void updateFolder(Folder f) throws IOException;    
    /** delete a folder, and any messages associated with it */
    void deleteFolder(Folder f) throws IOException; 
    
    
    /** list messages in a folder */
    MessageContainer[] listFolder(Folder f) throws IOException;    
    /** save an updated version of a message */
    void updateMessage(MessageContainer m) throws IOException;


    /** interface to a folder object */
    public static interface Folder {
        /** summary object for this folder - defines unique id for this folder, etc.
         * in case of system alerts, a lot of this info is 'mocked'
         *  */
        public ExecutionInformation getInformation();
        public void setInformation(ExecutionInformation e);
        /** number of unread messages in this folder */
        public int getUnreadCount();
        public void setUnreadCount(int c);
    }  
    
    /** interface to a message object */
    public static interface MessageContainer {
        /** whether this message has been read yet */
        public boolean isUnread();
        public void setUnread(boolean b);
        public String getSummary();
        public ExecutionMessage getMessage();
    }    
}

/* 
 $Log: MessageRecorderInternal.java,v $
 Revision 1.2  2005/11/24 01:13:24  nw
 merged in final changes from release branch.

 Revision 1.1.2.1  2005/11/23 04:54:34  nw
 changed return type.

 Revision 1.1  2005/11/10 12:05:43  nw
 big change around for vo lookout

 Revision 1.1  2005/11/01 09:19:46  nw
 messsaging for applicaitons.
 
 */