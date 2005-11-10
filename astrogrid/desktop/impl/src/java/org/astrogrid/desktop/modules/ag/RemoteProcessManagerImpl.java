/*$Id: RemoteProcessManagerImpl.java,v 1.2 2005/11/10 16:28:26 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.StatusChangeExecutionMessage;
import org.astrogrid.desktop.modules.system.ScheduledTask;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

/** implementation of a remote process manager.
 *  - handles running cea / jes / whatever else.
 * 
 *  - kind of a central clearing house - delegates to appropriate strategy based on uri / document type.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *
 */
public class RemoteProcessManagerImpl implements RemoteProcessManager, NewModuleListener, MessageRecorderInternal.RecorderListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RemoteProcessManagerImpl.class);

    /** Construct a new RemoteProcessManagerImpl
     * 
     */
    public RemoteProcessManagerImpl(MessageRecorderInternal recorder, MutableACR reg, MyspaceInternal vos
             ) {
        super();
        this.recorder = recorder;
        this.vos = vos;
        strategies = new ArrayList();
        reg.addNewModuleListener(this);
        recorder.addRecorderListener(this);
    }
    final MessageRecorderInternal recorder;
    final List strategies;
    final MyspaceInternal vos;

    public void newModuleRegistered(NewModuleEvent e) {
        DefaultModule nu = (DefaultModule)e.getModule();
        List l = nu.getComponentInstancesOfType(RemoteProcessStrategy.class);        
        logger.info("Registering " + l.size() +" process strategies");
        strategies.addAll(l);
        
    }    
    
    private RemoteProcessStrategy selectStrategy(URI uri) throws InvalidArgumentException {
        for (Iterator i = strategies.iterator(); i.hasNext(); ) {
            RemoteProcessStrategy s= (RemoteProcessStrategy)i.next();
            if (s.canProcess(uri)) {
                return s;
            }
        }
        throw new InvalidArgumentException("Could not find handler for " +uri);
    }
    
    private RemoteProcessStrategy selectStrategy(Document doc) throws InvalidArgumentException {
        for (Iterator i = strategies.iterator(); i.hasNext(); ) {
            RemoteProcessStrategy s= (RemoteProcessStrategy)i.next();
            if (s.canProcess(doc)) {
                return s;
            }
        }
        throw new InvalidArgumentException("Could not find handler document ");        
    }
    
    public URI[] list() throws ServiceException {
        try {
            return recorder.listLeaves();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    public URI submit(Document arg0) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
        RemoteProcessStrategy s = selectStrategy(arg0);
        return s.submit(arg0);
    }


    public URI submitTo(Document arg0, URI arg1) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        RemoteProcessStrategy s = selectStrategy(arg0);
        return s.submitTo(arg0,arg1);
    }


    private Document loadDocument(URI location) throws NotFoundException, InvalidArgumentException {
        InputStream is = null;
        try {
            is = vos.getInputStream(location);
            return XMLUtils.newDocument(is);
        } catch (ACRException e) {
            throw new NotFoundException("Failed to load document from " + location,e);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to parse document from " + location,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.warn("Failed to close read stream",e);
                }
            }
        }
            
    }
    
    public URI submitStored(URI arg0) throws NotFoundException, InvalidArgumentException,
            SecurityException, ServiceException {
        Document doc = loadDocument(arg0);
        RemoteProcessStrategy s = selectStrategy(doc);
        return s.submit(doc);
    }


    public URI submitStoredTo(URI arg0, URI arg1) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        Document doc = loadDocument(arg0);
        RemoteProcessStrategy s = selectStrategy(doc);
        return s.submitTo(doc,arg1);
    }


    public void halt(URI arg0) throws NotFoundException, InvalidArgumentException,
            ServiceException, SecurityException {
        RemoteProcessStrategy s = selectStrategy(arg0);
        s.halt(arg0);        
    }


    public void delete(URI arg0) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
        RemoteProcessStrategy s = selectStrategy(arg0);
        // problem here about which to delete first - would like to do both really..
        // curent solution isn't perfect - complicated because shouldn't throw exceptions from within finally blocks

        try {
            Folder f = recorder.getFolder(arg0);
        if (f == null) {
            // just try to delete frm the server then..
            s.delete(arg0);            
            throw new NotFoundException(arg0.toString());        
        }
        recorder.deleteFolder(f);
        } catch (IOException e) {
            s.delete(arg0);
            throw new ServiceException("Failed to delete local records",e);
        }
        s.delete(arg0);
        
    }


    public ExecutionInformation getExecutionInformation(URI arg0) throws ServiceException,
            NotFoundException, SecurityException, InvalidArgumentException {
        try {
        Folder f= recorder.getFolder(arg0);
        if (f == null) {
            throw new NotFoundException(arg0.toString());
        }
        return f.getInformation();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }


    public ExecutionMessage[] getMessages(URI arg0) throws ServiceException, NotFoundException {
        try {
            Folder f= recorder.getFolder(arg0);
            if (f == null) {
                throw new NotFoundException(arg0.toString());
            }
            MessageContainer[] ms = recorder.listFolder(f);
            // only safe to return plain messages.
            List result = new ArrayList(ms.length);
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getMessage().getClass() == ExecutionMessage.class) {
                    result.add(ms[i].getMessage());
                }
            }
            return (ExecutionMessage[])result.toArray(new ExecutionMessage[result.size()]);
            } catch (IOException e) {
                throw new ServiceException(e);
            }
    }


    public Map getResults(URI arg0) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
        try {
            Folder f= recorder.getFolder(arg0);
            if (f == null) {
                throw new NotFoundException(arg0.toString());
            }
            MessageContainer[] ms = recorder.listFolder(f);
            // search for result.
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getMessage() instanceof ResultsExecutionMessage) {
                    return convert( ((ResultsExecutionMessage)ms[i].getMessage()).getResults());
                }
            }
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        // if recorder doesn't have it..
        RemoteProcessStrategy s = selectStrategy(arg0);
        return s.getLatestResults(arg0);
    }

    // map of listeners - key is the exec id, value is a list of listeners.
    private Map  listenerMap = LazyMap.decorate(new HashMap(),new Factory() {
        public Object create() {
            return new HashSet();
        }
    });
    // set of listeners that listen to everything
    private Set wildcardListeners = new HashSet();
    
    public void addRemoteProcessListener(URI arg0, RemoteProcessListener arg1) {
        if (arg0 == null) {
            wildcardListeners.add(arg0);
        } else {
            Set s = (Set)listenerMap.get(arg0);
            s.add(arg1);
        }
    }


    public void removeRemoteProcessListener(URI arg0, RemoteProcessListener arg1) {
        if (arg0 == null) {
            wildcardListeners.remove(arg0);
        } else {
            Set s = (Set)listenerMap.get(arg0);
            s.remove(arg1);
        }
    }
    // implementation of recorder listener
    public void messageReceived(Folder f, MessageContainer msg) {
        // check whether we've gt anything that's registered an interest in this.
        final URI id = f.getInformation().getId();
        if( wildcardListeners.size() > 0 || listenerMap.containsKey(id)) {
            // might as well get on with it then.
            Iterator listeners = listenerMap.containsKey(id) ? 
                     new IteratorChain(wildcardListeners.iterator(), ((Set)listenerMap.get(id)).iterator()) 
                     : wildcardListeners.iterator();
            ExecutionMessage m = msg.getMessage();
            if (m instanceof ResultsExecutionMessage) {
                Map result = convert(((ResultsExecutionMessage)m).getResults());               
                while(listeners.hasNext()) {
                    RemoteProcessListener l = (RemoteProcessListener)listeners.next();
                    l.resultsReceived(id,result);
                }                    
            } else if (m instanceof StatusChangeExecutionMessage) {
                String status = m.getStatus();
                while(listeners.hasNext()) {
                    RemoteProcessListener l = (RemoteProcessListener)listeners.next();
                    l.statusChanged(id,status);
                }                
            } else {
                while(listeners.hasNext()) {
                    RemoteProcessListener l = (RemoteProcessListener)listeners.next();
                    l.messageReceived(id,m);
                }                
            }
        }
    }

   private Map convert(ResultListType rs) {
    Map result = new HashMap();        
    for (int i =0 ; i < rs.getResultCount(); i++) {
        ParameterValue val = rs.getResult(i);
        result.put(val.getName(),val.getValue());
    }
    return result;
   }

}


/* 
$Log: RemoteProcessManagerImpl.java,v $
Revision 1.2  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/