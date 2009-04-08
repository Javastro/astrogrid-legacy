/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.samp.Client;
import org.astrogrid.samp.Message;
import org.astrogrid.samp.Metadata;
import org.astrogrid.samp.RegInfo;
import org.astrogrid.samp.client.AbstractMessageHandler;
import org.astrogrid.samp.client.ClientProfile;
import org.astrogrid.samp.client.HubConnection;
import org.astrogrid.samp.client.HubConnector;
import org.astrogrid.samp.client.SampException;
import org.astrogrid.samp.gui.GuiHubConnector;
import org.astrogrid.samp.xmlrpc.HubMode;
import org.astrogrid.samp.xmlrpc.StandardClientProfile;

import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.matchers.Matcher;

/** Implments the Samp side of messaging.
 * Sadly I can't think of a cute pun a-la tupperware. That's progress.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 25, 200910:26:27 AM
 */
public class SampImpl implements Samp, ShutdownListener{

    protected final UIContext ui;
    private final List<MessageTarget> internalTargets;
    protected final List<MessageType<?>> knownTypes;
    private final EventList<ExternalMessageTarget> sampTargets;
    protected final GuiHubConnector hubConnector;
    private final URL webserverRoot;

    
    public URL getWebserverRoot() {
        return webserverRoot;
    }
    
    public HubConnector getConnector() {
        return hubConnector;
    }
    
    /**
     * 
     */
    public SampImpl(final UIContext ui, final URL webserverRoot
            , final String applicationName, final String description, final String iconUrl
            , final String appUrl, final String version, final String email
            , final List<MessageTarget> internalTargets
            , final List<MessageType<?>> knownTypes
            , final CompositeList<ExternalMessageTarget> allTargets
            ) {
        this.ui = ui;
        this.webserverRoot = webserverRoot;
        this.internalTargets = internalTargets;
        this.knownTypes = knownTypes;
        this.sampTargets = allTargets.createMemberList();
        // put a filter between sampTargets and what's displayed in allTargets
            //- filter excludes self and hub.
        allTargets.addMemberList(
                new FilterList<ExternalMessageTarget>(
                        this.sampTargets,new SelfAndHubFilter()));
        
        
        final ClientProfile profile = StandardClientProfile.getInstance(); //@future adjust xmlrpc engine, to use same libs as everything else.
        this.hubConnector = new GuiHubConnector(profile);
        
        // provide the connector with metadata about this app.
        final Metadata meta = new Metadata();
        meta.setDescriptionText(description);
        meta.setIconUrl(iconUrl);
        meta.setDocumentationUrl(appUrl);
        meta.setName(applicationName);
        meta.put("author.name","Noel Winstanley");
        meta.put("author.affiliation","AstroGrid, University of Manchester");
        meta.put("author.email",email);
        meta.put("application.version",version);
        
        hubConnector.declareMetadata(meta);
        
        // setup message and response handlers
        for (final MessageTarget mt : internalTargets) {
            hubConnector.addMessageHandler(createMessageHandler(mt));
        }
        
        
        // ok. declare the set of mesages we're subscribing to.
        hubConnector.declareSubscriptions(hubConnector.computeSubscriptions());
        
        // map changes to list model into glazed list
        // a bit back to front, but can't behelped
        final ListModel listModel = hubConnector.getClientListModel();
        listModel.addListDataListener(new Bridge(listModel));

        // note the connctor isn't started here. look in Messaging.
    }
    


    /** Matcher that will filter out self and hub from the list.
     * Done like this so that the ListDataListener doesn't need to worry about filtering out
     * items - so that there's a correspondance between indexes in the src and dest lists.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Mar 26, 20093:00:26 PM
     */
    private final class SelfAndHubFilter implements
            Matcher<ExternalMessageTarget> {
        // matches all but self and hub.
        public boolean matches(final ExternalMessageTarget arg0) {
            // if not connected, just return false. List should be empty at this point anyhow.
            if (! hubConnector.isConnected()) {
                return false;
            }
            try {
                final RegInfo regInfo = hubConnector.getConnection().getRegInfo();
                final String id = arg0.getId();
                return ! (id.equals(regInfo.getHubId()) 
                        || id.equals(regInfo.getSelfId()));
            } catch (final SampException x) {
               //shouldn't ever happen - as am already connected.
                return false;
            }
        }
    }

    /** bridge changes from the list model into the event list,
     * performing a map from Client to SampMessageTarget as we go
     * 
     * events are fired on EDT, but we the do the mapping to our target
     * eventList on a background thread (by using the executor). This means that
     * any communication / introspection required happens in the BG.
     */
    private class Bridge implements ListDataListener {

        private final ListModel listModel;

        public Bridge(final ListModel listModel) {
            this.listModel = listModel;
        }

        public void contentsChanged(final ListDataEvent e) {
            try {
                ui.getExecutor().execute(new Runnable() {

                    public void run() {
                        try {
                            sampTargets.getReadWriteLock().writeLock().lock();

                            for (int i = e.getIndex0(); i <= e.getIndex1(); i++) { // inclusive
                                final Client client = (Client) listModel.getElementAt(i);                    
                                final SampMessageTarget messageTarget = new SampMessageTarget(SampImpl.this,client);
                                sampTargets.set(i,messageTarget);
                            }                
                        } finally {
                            sampTargets.getReadWriteLock().writeLock().unlock();
                        }    
                    }
                });
            } catch (final InterruptedException x) {
                //ignore.
            }
        }

        public void intervalAdded(final ListDataEvent e) {
            try {
                ui.getExecutor().execute(new Runnable() {

                    public void run() {            
                        try {
                            sampTargets.getReadWriteLock().writeLock().lock();
                            for (int i = e.getIndex0(); i <= e.getIndex1(); i++) { // inclusive
                                final Client client = (Client) listModel.getElementAt(i);                   
                                final SampMessageTarget messageTarget = new SampMessageTarget(SampImpl.this,client);
                                sampTargets.add(i,messageTarget);                    
                            }
                        } finally {
                            sampTargets.getReadWriteLock().writeLock().unlock();
                        }        
                    }
                });
            } catch (final InterruptedException x) {
                //ignore.
            }
        }

        public void intervalRemoved(final ListDataEvent e) {            
            try {
                ui.getExecutor().execute(new Runnable() {

                    public void run() {            
                        try {
                            sampTargets.getReadWriteLock().writeLock().lock();
                            // work from the last index downward.
                            for (int i = e.getIndex1(); i >= e.getIndex0(); i--) { // inclusive
                                sampTargets.remove(i);
                            }
                        } finally {
                            sampTargets.getReadWriteLock().writeLock().unlock();
                        }                 
                    }
                });
            } catch (final InterruptedException x) {
                //ignore.
            }
        }
    }
    
    /** factory method, as some processing required before can call superconstructor */
    IncomingMessageHandler createMessageHandler(final MessageTarget target) {
        final List<String> mtypes = new ArrayList<String>();
        for(final MessageType t : target.acceptedMessageTypes()) {
            final String sampMType = t.getSampMType();
            if (sampMType != null) { // if null, it could be a plastic-only message
                mtypes.add(sampMType);
            }
        }
        return new IncomingMessageHandler(mtypes.toArray(new String[mtypes.size()]),target);
    }
    /** handles all incoming mesages for a single internal message target */
    private class IncomingMessageHandler extends AbstractMessageHandler {
        private final MessageTarget target;
        /**
         * 
         */
        public IncomingMessageHandler(final String[] mtypes, final MessageTarget target) {
            super(mtypes);
            this.target = target;
        }
        @Override
        public Map processCall(final HubConnection conn, final String senderId, final Message message)
                throws Exception {
            final String mtype = message.getMType();
            // match against correct message type
            for (final MessageType type : target.acceptedMessageTypes()) {
                if (mtype.equals(type.getSampMType())) {
                    
                    //ceate dispatcher and marshaller.
                    final MessageSender dispatch = target.createMessageSender(type);
                    final MessageUnmarshaller<MessageSender> unmarshaller = type.createSampUnmarshaller();
                    
                    // try to find the sender 
                    ExternalMessageTarget source = null;
                    if (senderId != null) {
                        for(final ExternalMessageTarget mt : sampTargets) {
                            if (mt.getId().equals(senderId)) {
                                source = mt;
                                break;
                            }
                        }                        
                    }
                    // convert 
                    final List<Map.Entry<?,?>> args = new ArrayList<Map.Entry<?,?>>(message.getParams().entrySet());
                    // do the call, let exceptions boil out if they occur.
                    final Object result =  unmarshaller.handle(source,args,dispatch);
                    if (result == null) {
                        return (Map)result;
                    } else if (result instanceof Map) {
                        return (Map)result;
                    } else {
                        throw new ProgrammerError("Samp invocation returned something other than a Map:" + result);
                    }
                }
            }
            // no message matched.
            throw new ProgrammerError("Can't handle mtype " + mtype);
        }
    }
    
    public Action connectAction() {
        
      final AbstractAction action = (AbstractAction)hubConnector.createRegisterAction();
      action.putValue(Action.NAME,"Register with SAMP");
      return action;
    }

    public Action disconnectAction() {
        final AbstractAction action = (AbstractAction) hubConnector.createUnregisterAction();
        action.putValue(Action.NAME,"Unregister with SAMP");
        return action;
    }

    public Action startInternalHubAction() {
        final AbstractAction action = (AbstractAction) hubConnector.createHubAction(false,HubMode.NO_GUI);
        action.putValue(Action.NAME,"Start internal SAMP Hub");
        return action;   
    }
    
    public Action showMonitorAction() {
        final AbstractAction action = (AbstractAction) hubConnector.createShowMonitorAction();
        action.putValue(Action.NAME,"Show SAMP Hub Status");
        return action;          
    }
    
    // sutdown listener.
    public void halting() { // disconnect from the hub.
        if (hubConnector.isConnected()) {
            hubConnector.setActive(false);
        }
    }

    public String lastChance() {
        return null; // do nothin
    }

}
