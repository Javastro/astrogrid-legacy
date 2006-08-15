/*$Id: MessageRecorderImpl.java,v 1.7 2006/08/15 10:16:24 nw Exp $
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

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ag.MessagingInternal.MessageListener;
import org.astrogrid.desktop.modules.ag.MessagingInternal.SourcedExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.FolderImpl;
import org.astrogrid.desktop.modules.ag.recorder.Folders;
import org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl;
import org.astrogrid.desktop.modules.ag.recorder.Messages;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.StatusChangeExecutionMessage;

/** Implementation a message recorder - main consumer of  messages from the 
 * event notification queue.
 * Collates and records them to a persistent store.
 * datastructures used are in the 'recorder' sub-package. this class defines the message listeners
 * that receive the messages, and the public api.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 * @todo implement against new registry.
 */
public class MessageRecorderImpl implements UserLoginListener,MessageRecorderInternal{

    static final Log logger = LogFactory.getLog(MessageRecorderImpl.class);

    public MessageRecorderImpl(MessagingInternal m, StoreInternal store, Registry reg) throws IOException {
        super(); 
        this.store = store;
        this.m = m;
        this.reg= reg;
        init();
    }
    protected final StoreInternal store;
    protected final Registry reg;
    protected final MessagingInternal m;
    private Folders folders;
    private Messages messages;

    public TreeModel getFolderList() {
        return getFolder();
    }

    protected Messages getMessages() {
        return messages;
    }

    private Folders getFolder() {
        return folders;
    }

    public List listLeaves() throws IOException {
        return getFolder().listLeaves();
    }

    
    public Folder getFolder(URI id) throws IOException {
        return getFolder().getFolder(id);
    }
    
    public TableModel getMessageList() {
        return getMessages();
    }
    
    public void deleteFolder(Folder f) throws IOException {
        getFolder().deleteFolder(f);
        getMessages().deleteFolder(((FolderImpl)f).getKey());
    }
    
    public void displayMessages(Folder f) throws IOException {
        getMessages().displayFolder(((FolderImpl)f).getKey());
    }
    
    public void deleteMessage(int row) throws IOException {
        getMessages().deleteMessage(row);
    }
    
    public MessageContainer getMessage(int row) {
        return getMessages().getMessage(row);
    }
    
    public void updateMessage(MessageContainer m) throws IOException {
        getMessages().updateMessage(m);
    }
    
    public void updateFolder(Folder f) throws IOException {
        getFolder().updateFolder(f);
    }
    
    public MessageContainer[] listFolder(Folder f) throws IOException{
        return getMessages().listFolder(((FolderImpl)f).getKey());
    }
       
    // id keys for well-known folders.
    public final static URI ROOT = URI.create("folder:root") ;
    public final static URI TASKS = URI.create("folder:tasks");
    public final static URI QUERIES = URI.create("folder:queries");
    public final static URI JOBS = URI.create("folder:jobs");

    

     
    /** records all events on the user's queue */
    private class NotificationRecorder implements MessageListener {

        private boolean isAdqlApplication(Resource ri) {
            if (! (ri instanceof CeaApplication) ) { // has to be an exact match, not a subclass.
                return false;
            }
            CeaApplication app = (CeaApplication)ri;
            for (int i = 0; i <  app.getParameters().length; i++ ) {
                ParameterBean pb = app.getParameters()[i];
                if ("adql".equalsIgnoreCase(pb.getType())) {
                    return true;
                }
            }
            return false;
        }
        
        private Folder createFolderFor(URI id, SourcedExecutionMessage msg) throws IOException {
            // a little hacky - relies on parsing the uri to know what kind of thing we're dealing with.
            // so only works for known uri types.
            URI parent = TASKS;
            Resource ri = null;
                    String name= msg.getProcessName();
            if ("jes".equals(id.getScheme())) {
                parent = JOBS;
            } else {
                // assume it's a cea id...               
                try {   
                    URI uri = new URI (name.startsWith("ivo") ? name : "ivo://" + name);
                ri = reg.getResource(uri);
                if (ri instanceof Service
                        || isAdqlApplication(ri)) {
                    parent = QUERIES;
                }
                } catch (Exception e) {
                    //oh well, never mind - will defaults to tasks.
                }
            }
                      
            Date startDate = msg.getStartTime() != null
                ? msg.getStartTime()
                : new Date();
            Date endDate = msg.getEndTime() != null // unlikely, but still..
                ? msg.getEndTime()
                : null;
                        
            ExecutionInformation nfo = new ExecutionInformation(
                    id
                    ,name
                    ,ri != null && ri.getContent() != null ? ri.getContent().getDescription() :   "" //@todo get descirption for workfow and local app.
                    , ExecutionInformation.UNKNOWN
                    ,startDate
                    ,endDate);
            return getFolder().createFolder(parent,nfo);
        }
        public void onMessage(SourcedExecutionMessage arg0) {
            try {
                URI id = arg0.getProcessId();
               Folder f = getFolder().getFolder(id);               
                if (f == null) {
                    f = createFolderFor(id,arg0);
                }         
                    ExecutionMessage msg = arg0.getMessage();
                    String type;
                    if (msg instanceof StatusChangeExecutionMessage) {
                        ExecutionInformation orig = f.getInformation();
                        f.setInformation(new ExecutionInformation(
                                orig.getId()
                                ,orig.getName()
                                ,orig.getDescription()
                                ,msg.getStatus()
                                , arg0.getStartTime() != null
                                   ? arg0.getStartTime()
                                   : orig.getStartTime()
                                , arg0.getEndTime() != null
                                   ? arg0.getEndTime()
                                   : orig.getFinishTime()        
                                ));   
                    }   
                    MessageContainer m = new MessageContainerImpl(getTypeForMessage(msg),msg); 
                    f.setUnreadCount(f.getUnreadCount()+1);
                    getFolder().updateFolder(f);                                
                    getMessages().addMessage(id,m);
                    notifyMessageReceived(f,m);
            } catch (IOException e) {
                logger.error("Failed to persist message",e);
            } 
        }
        	
        private final String getTypeForMessage(ExecutionMessage m) {
        	if (m instanceof StatusChangeExecutionMessage) {
        		return "Status Change";
        	} 
        	if (m instanceof ResultsExecutionMessage) {
        		return "Result";
        	}
        	return "Information";
        }
    }

    public void userLogin(UserLoginEvent arg0) {
        try {         
            init();
            } catch (Exception e) {
                logger.error("Could not start recorder",e);
            }        
    }

	/** initialize all the recorder machinery for a new user.
	 * @throws IOException
	 */
	private void init() throws IOException {
		this.folders = Folders.findOrCreate(store.getManager());
		this.messages = new Messages(store.getManager());
		this.notificationRecorder = new NotificationRecorder();    
		// this condition should catch all messages - but leaves options open in the future.
		m.addEventProcessor(notificationRecorder);
	}
    
    private NotificationRecorder notificationRecorder;

    public void userLogout(UserLoginEvent arg0) {   	
       m.removeEventProcessor(notificationRecorder);  
       try {
    	   folders.cleanup();
    	   store.getManager().close();
       } catch (IOException e) {
    	   logger.warn("Exception when cleaning up deleted folders",e);
       }
    }

    private Set listeners =  new HashSet();
    public void addRecorderListener(RecorderListener listener) {
        listeners.add(listener);
    }

    public void removeRecorderListener(RecorderListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyMessageReceived(Folder f, MessageContainer c) {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            RecorderListener rl = (RecorderListener)i.next();
            rl.messageReceived(f,c);
        }
    }


}


/* 
$Log: MessageRecorderImpl.java,v $
Revision 1.7  2006/08/15 10:16:24  nw
migrated from old to new registry models.

Revision 1.6  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.5.8.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.5.8.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.5  2006/01/31 14:50:33  jdt
The odd typo and a bit of tidying.

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.3  2005/11/23 18:10:03  nw
reviewed and tuned up.

Revision 1.3.2.2  2005/11/23 04:55:19  nw
tidied,
handled login / logout safely.

Revision 1.3.2.1  2005/11/17 21:06:26  nw
moved store to be user-dependent
debugged message monitoring.

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout

Revision 1.2  2005/11/02 09:29:51  nw
minor bug fix

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/