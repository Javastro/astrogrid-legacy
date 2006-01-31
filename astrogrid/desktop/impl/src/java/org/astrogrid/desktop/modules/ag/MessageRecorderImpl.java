/*$Id: MessageRecorderImpl.java,v 1.5 2006/01/31 14:50:33 jdt Exp $
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

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.desktop.modules.ag.recorder.FolderImpl;
import org.astrogrid.desktop.modules.ag.recorder.Folders;
import org.astrogrid.desktop.modules.ag.recorder.MessageContainerImpl;
import org.astrogrid.desktop.modules.ag.recorder.Messages;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.StatusChangeExecutionMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;

/** Implementation a message recorder - main consumer of  JMS messages from the 
 * event notification queue.
 * Collates and records them to a persistent store.
 * datastructures used are in the 'recorder' sub-package. this class defines the message listeners
 * that receive the messages, and the public api.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 */
public class MessageRecorderImpl implements UserLoginListener,MessageRecorderInternal{

    static final Log logger = LogFactory.getLog(MessageRecorderImpl.class);

    public MessageRecorderImpl(MessagingInternal m, StoreInternal store, Registry reg, Community comm) throws IOException {
        super(); 
        this.store = store;
        this.m = m;
        this.reg= reg;
        this.comm = comm;
        comm.addUserLoginListener(this);
        
    }
    protected final Community comm;
    protected final StoreInternal store;
    protected final Registry reg;
    protected final MessagingInternal m;
    private Folders folders;
    private Messages messages;

    public TreeModel getFolderList() {
        return getFolder();
    }

    protected Messages getMessages() {
        // force a login
        comm.getUserInformation();
        return messages;
    }

    Folders getFolder() {
        // will force a login here.
        comm.getUserInformation();
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
    public final static URI ALERTS = URI.create("folder:alerts");
    public final static URI TASKS = URI.create("folder:tasks");
    public final static URI QUERIES = URI.create("folder:queries");
    public final static URI JOBS = URI.create("folder:jobs");

    /** simple recorder - listens to all alert messages
     * expects them to be text messages
     * disabled for now - as no-one is firing alerts.
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Oct-2005
     *
     */
    private class AlertRecorder implements MessageListener {

        public void onMessage(javax.jms.Message arg0) {
            try {
            if (arg0 instanceof TextMessage) {
                TextMessage t = (TextMessage)arg0;
                ExecutionMessage msg = parseMessage(t.getText());
                MessageContainerImpl m = new MessageContainerImpl(
                        t.getStringProperty(MessageUtils.SUMMARY_PROPERTY)
                        ,msg);
                FolderImpl f = (FolderImpl) getFolder().getFolder(ALERTS);
                // must always be here.
                f.setUnreadCount(f.getUnreadCount()+1);
                getFolder().updateFolder(f);                
                getMessages().addMessage(ALERTS,m);
                notifyMessageReceived(f,m);
            } else {
                logger.warn("Unrecognized message type " + arg0.toString());
            }
            } catch (JMSException e) {
                logger.error("Failed to process message",e);
            } catch (CastorException e) {
                logger.error("Failed to parse message",e);
            } catch (IOException e) {
                logger.error("Failed to persist message",e);
            }
        }
    }
    
    //deserialize xmlstring to Message, then copy across to an execution message
    ExecutionMessage parseMessage(String s) throws CastorException {
        Reader r = new StringReader(s);
        MessageType mt= MessageType.unmarshalMessageType(r);
        return new ExecutionMessage(
                mt.getSource()
                ,mt.getLevel().toString()
                ,mt.getPhase().toString()
                ,mt.getTimestamp()
                ,mt.getContent()
                );
    }
    
    ResultListType parseResultList(String s) throws CastorException {
        Reader r = new StringReader(s);
        ResultListType rs = ResultListType.unmarshalResultListType(r);
        return rs;

    }
     
    /** records all events on the user's queue */
    private class NotificationRecorder implements MessageListener {

        private boolean isAdqlApplication(ResourceInformation ri) {
            if (! (ri.getClass() ==  ApplicationInformation.class) ) { // has to be an exact match, not a subclass.
                return false;
            }
            ApplicationInformation app = (ApplicationInformation)ri;
            for (Iterator i = app.getParameters().values().iterator(); i.hasNext(); ) {
                ParameterBean pb = (ParameterBean)i.next();
                if ("adql".equalsIgnoreCase(pb.getType())) {
                    return true;
                }
            }
            return false;
        }
        
        private Folder createFolderFor(URI id, javax.jms.Message msg) throws JMSException, IOException {
            // a little hacky - relies on parsing the uri to know what kind of thing we're dealing with.
            // so only works for known uri types.
            URI parent = TASKS;
            ResourceInformation ri = null;
                    String name= msg.getStringProperty(MessageUtils.PROCESS_NAME_PROPERTY);
            if ("jes".equals(id.getScheme())) {
                parent = JOBS;
            } else {
                // assume it's a cea id...               
                try {   
                    URI uri = new URI (name.startsWith("ivo") ? name : "ivo://" + name);
                ri = reg.getResourceInformation(uri);
                if (ri instanceof SiapInformation 
                        || ri instanceof ConeInformation
                        || isAdqlApplication(ri)) {
                    parent = QUERIES;
                }
                } catch (Exception e) {
                    //oh well, never mind - will defaults to tasks.
                }
            }
                      
            Date startDate = msg.getStringProperty(MessageUtils.START_TIME_PROPERTY) != null
                ? new Date(msg.getStringProperty(MessageUtils.START_TIME_PROPERTY))
                : new Date(msg.getJMSTimestamp());
            Date endDate =msg.getStringProperty(MessageUtils.END_TIME_PROPERTY) != null // unlikely, but still..
                ? new Date(msg.getStringProperty(MessageUtils.END_TIME_PROPERTY))
                : null;
                        
            ExecutionInformation nfo = new ExecutionInformation(
                    id
                    ,name
                    ,ri != null ? ri.getDescription() :   "" //@todo get descirption for workfow and local app.
                    , ExecutionInformation.UNKNOWN
                    ,startDate
                    ,endDate);
            return getFolder().createFolder(parent,nfo);
        }
        
        public void onMessage(javax.jms.Message arg0) {
            try {
                URI id = new URI(arg0.getStringProperty(MessageUtils.PROCESS_ID_PROPERTY));
                String type = arg0.getStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY);
                Folder f = getFolder().getFolder(id);
                if (f == null) {
                    f = createFolderFor(id,arg0);
                }
                if (arg0 instanceof TextMessage) { 
                    TextMessage t = (TextMessage)arg0;         
                    ExecutionMessage msg= null;
                    if (type.equals(MessageUtils.RESULTS_MESSAGE)) {
                        msg = new ResultsExecutionMessage(
                        id.toString()
                        ,new Date(t.getJMSTimestamp())
                        ,parseResultList(t.getText())
                        );

                    } else if (type.equals(MessageUtils.STATUS_CHANGE_MESSAGE)) {
                        msg = new StatusChangeExecutionMessage(
                                id.toString()
                                ,t.getText()
                                ,new Date(t.getJMSTimestamp())
                                );                        
                        ExecutionInformation orig = f.getInformation();
                        f.setInformation(new ExecutionInformation(
                                orig.getId()
                                ,orig.getName()
                                ,orig.getDescription()
                                ,t.getText()
                                , t.getStringProperty(MessageUtils.START_TIME_PROPERTY) != null
                                   ? new Date(t.getStringProperty(MessageUtils.START_TIME_PROPERTY))
                                   : orig.getStartTime()
                                , t.getStringProperty(MessageUtils.END_TIME_PROPERTY) != null
                                   ? new Date(t.getStringProperty(MessageUtils.END_TIME_PROPERTY))
                                   : orig.getFinishTime()        
                                ));                        
                    } else { // hope for the best
                        logger.debug(t.getText());
                        msg = parseMessage(t.getText());
                    }
                    MessageContainer m = new MessageContainerImpl(type,msg); 
                    f.setUnreadCount(f.getUnreadCount()+1);
                    getFolder().updateFolder(f);                                
                    getMessages().addMessage(id,m);
                    notifyMessageReceived(f,m);
                } else {
                    logger.warn("Unrecognized message type " + arg0.toString());
                    return;
                }
            } catch (JMSException e) {
                logger.error("Failed to process message",e);
            } catch (IOException e) {
                logger.error("Failed to persist message",e);
            } catch (URISyntaxException e) {
                logger.error("Failed to process message",e);
            } catch (CastorException e) {
                logger.error("Failed to parse message",e);
            }
        }
    }

   

    public void userLogin(UserLoginEvent arg0) {
        try {         
            this.folders = Folders.findOrCreate(store.getManager());
            this.messages = new Messages(store.getManager());
//unused at present            this.alertRecorder = new AlertRecorder();
            this.notificationRecorder = new NotificationRecorder();            
//unused at present            m.addAlertSubscriber(alertRecorder);
            // this condition should catch all messages - but leaves options open in the future.
            m.addEventProcessor(MessageUtils.PROCESS_ID_PROPERTY +" is not null",notificationRecorder);
            } catch (Exception e) {
                logger.error("Could not start recorder",e);
            }        
    }
    
    //unused private AlertRecorder alertRecorder;
    private NotificationRecorder notificationRecorder;

    public void userLogout(UserLoginEvent arg0) {
        try {
//unused        m.removeAlertSubscriber(alertRecorder);
        m.removeEventProcessor(notificationRecorder);
        } catch (JMSException e) {
            logger.error("Could not stop recorders");
        }
    }

    private Set listeners =  new HashSet();
    public void addRecorderListener(RecorderListener listener) {
        listeners.add(listener);
    }

    public void removeRecorderListener(RecorderListener listener) {
        listeners.remove(listener);
    }
    
    void notifyMessageReceived(Folder f, MessageContainer c) {
        for (Iterator i = listeners.iterator(); i.hasNext(); ) {
            RecorderListener rl = (RecorderListener)i.next();
            rl.messageReceived(f,c);
        }
    }


}


/* 
$Log: MessageRecorderImpl.java,v $
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