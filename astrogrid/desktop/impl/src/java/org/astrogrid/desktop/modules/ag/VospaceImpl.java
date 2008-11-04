/*$Id: VospaceImpl.java,v 1.29 2008/11/04 14:35:47 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FilesCache;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.NodeInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MemoizingCommunityAccountSpaceResolver;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MemoizingNodeDelegateResolver;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.TransferInfo;
import org.astrogrid.io.Piper;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

/** Myspace client implementation.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Feb-2005
 * @todo aren't always throwing the right kind of exception in all cases - needs testing / inspection of myspace client
 */
public class  VospaceImpl implements UserLoginListener, MyspaceInternal {
    

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(VospaceImpl.class);

    /** Construct a new Vospace
     * 
     */
    public VospaceImpl(final Community community, final Registry reg, final BundlePreferences preferences, final UIContext ui,final FilesCache filesCache) {
        super();
        this.community = community;
        this.prefs = preferences;
        this.ui = ui;
        this.reg = reg;
        this.cache = filesCache;
    }
    protected final UIContext ui;
   protected final Community community;
   protected final Registry reg;
   protected final BundlePreferences prefs;
    protected URI home;
    protected FileManagerClient client;
    private final FilesCache cache;
    
    //@todo move this client creation into it's own service,
    public synchronized FileManagerClient getClient() throws CommunityException, RegistryException, URISyntaxException {
        if (client == null) {
     //       FileManagerClientFactory fac = new FileManagerClientFactory(prefs);
       //NWW - replaced with a version that uses memoization for speedup.
    	final FileManagerClientFactory fac = new FileManagerClientFactory(
    				new MemoizingNodeDelegateResolver(prefs,cache)
    				,new MemoizingCommunityAccountSpaceResolver());        	
            client = fac.login(new Ivorn(community.getUserInformation().getId().toString()),community.getUserInformation().getPassword());
        } 
        return client;
    }
    
    public void createFile(final URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        try {
            getClient().createFile(cvt(ivorn));
        } catch (final CommunityException e) {
            throw new SecurityException(e);
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("A resource with this name already exists",e);
        } catch (final Exception e) {
            throw new ServiceException(e);
        } 
    }
    
    public void createFolder(final URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        try {
        getClient().createFolder(cvt(ivorn));
        } catch (final CommunityException e) {
            throw new SecurityException(e);
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("A resource with this name already exists",e);
        } catch (final Exception e) {
            throw new ServiceException(e);
        }         
    }
    


    // access
    public URI getHome() throws SecurityException, ServiceException, NotFoundException {
        if (home == null) {
        try {
            home = new URI(getClient().home().getIvorn().toString());
        } catch (final CommunityException e) {
            throw new SecurityException(e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final Exception e) {
            throw new ServiceException(e);  
        }
        } 
        return home;
    }
    
    /** convert a uri (possibly relative) to an ivorn
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws ServiceException
     * @throws SecurityException*/
    final  Ivorn cvt(URI uri) throws InvalidArgumentException, SecurityException, ServiceException, NotFoundException {
        uri = getHome().resolve(uri); // if passed in uri was a relative, this appends the home path. otherwise it's unchanged. nice !        
        try {
            return new Ivorn(uri.toString());
        } catch (final URISyntaxException e) {
            throw new InvalidArgumentException("this is not a myspace reference: " + uri, e); 
        }
    }
           
    final URI cvt(final Ivorn iv) throws ServiceException {
        try {
            return new URI(iv.toString());
        } catch (final URISyntaxException e) {
            throw new ServiceException(e);
        }
    }
    

    public FileManagerNode node(final URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            final Ivorn ivo = cvt(ivorn);
            final FileManagerNode node =  getClient().node(ivo);
            return node;
        } catch (final CommunityException e) {
            throw new SecurityException(e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final Exception e) {
            throw new ServiceException(e);
        } 
    }    
   
    
    public boolean exists(final URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {       
        try {
            return getClient().exists(cvt(ivorn)) != null;
        } catch (final CommunityException e) {
            throw new SecurityException(e);
        } catch (final Exception e) {
            throw new ServiceException(e);  
        }
    }
        
    //creation  
    public URI  createChildFolder(final URI parentIvorn, final String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
            try {
                return cvt(node(parentIvorn).addFolder(name).getIvorn());
            } catch (final FileManagerFault e) {
                throw new ServiceException(e);
            } catch (final NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (final DuplicateNodeFault e) {
                throw new InvalidArgumentException(e);
            } catch (final RemoteException e) {
                throw new ServiceException(e);
            } catch (final UnsupportedOperationException e) {
                   throw new InvalidArgumentException(e);
            }        
    }
        


    public URI createChildFile(final URI parentIvorn, final String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException{
        try {
            return cvt(node(parentIvorn).addFile(name).getIvorn());
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        } catch (final UnsupportedOperationException e) {
               throw new InvalidArgumentException(e);
        }        
    }
    // content
    public String read(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        Reader reader = null;
        StringWriter writer = null;
        try {
           reader =new InputStreamReader(node(ivorn).readContent());
           writer = new StringWriter();
            Piper.pipe(reader,writer);
            return writer.toString();
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (final IOException e) {
            throw new ServiceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    // meh
               }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    //meh
               }
            }            
        }                                    
    }


    public void write(final URI ivorn, final String content) throws InvalidArgumentException, ServiceException, SecurityException {
        final Reader r = new StringReader(content);
        Writer w = null;
        if (! exists(ivorn)) {
            createFile(ivorn);
        }
        try {
            w = new OutputStreamWriter (mkOutputStream(ivorn,content.getBytes().length));
            //w = new OutputStreamWriter(node(ivorn).writeContent());
            Piper.pipe(r, w);
        } catch (final IOException e) {
            throw new ServiceException(e);
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (final NotFoundException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (final IOException e) {
                    // meh
                }
            }
        }
    }

    /** wrapper method that takes care of creating an output stream. means that we can control what is created
     *  - working around problems with FileStoreoutputStream at the moment.
     * later, can replace with a pass-thru implementaiton
     * @todo review and remove when possible
     * @param ivorn
     * @return
     * @throws NotFoundException
     * @throws InvalidArgumentException
     * @throws SecurityException
     * @throws ServiceException
     */
    private OutputStream mkOutputStream(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException{
        return mkOutputStream(ivorn,0);
    }
    
    /**
     * variant where we know the size in advance - allows for optimizations.
     * @param ivorn
     * @param dataSize
     * @return
     * @throws NotFoundException
     * @throws InvalidArgumentException
     * @throws SecurityException
     * @throws ServiceException
     */
    private OutputStream mkOutputStream(final URI ivorn, final long dataSize)  throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException{
        // rewrite of FileStoreOutputStream really
        final URL url = getWriteContentURL(ivorn);
        try {
        final URLConnection c = url.openConnection();
        if (c instanceof HttpURLConnection) {
            final HttpURLConnection conn = (HttpURLConnection)c;
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            if (dataSize > 0) { // set the 1.5 method, if available.
                try {
                final Method m= conn.getClass().getMethod("setFixedLengthStreamingMode",new Class[]{int.class});
                m.invoke(conn,new Object[]{Integer.valueOf((int)dataSize)}); // mad that this method only takes an int.
                } catch (final Exception e) {
                    logger.debug("HttpURLConnection does not support FixesLengthStreamingMode",e);
                }                
            }
            conn.connect();
            return new FilterOutputStream(conn.getOutputStream()) {
                @Override
                public void close() throws IOException {
                    super.flush();
                    super.close();
                    conn.getResponseCode();
                    try {
                        transferCompleted(ivorn);
                    } catch (final ACRException e) {
                        logger.warn("Failed to notify transfer completed",e);
                    }
                }
            };
        } else {
            logger.warn("Some other kind of filestore url - handling it and hoping for the best");
            return new FilterOutputStream( url.openConnection().getOutputStream()) {
                @Override
                public void close() throws IOException {
                    super.close();
                    try {
                        transferCompleted(ivorn);
                    } catch (final ACRException e) {
                        logger.warn("Failed to notify transfer completed",e);
                    }                    
                }
            };
        }
        } catch (final IOException e) {
            throw new ServiceException(e);
        }
    }

    public byte[] readBinary(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        InputStream reader = null;
        ByteArrayOutputStream writer = null;
        try {
           reader = node(ivorn).readContent();
           writer = new ByteArrayOutputStream();
            Piper.pipe(reader,writer);
            return writer.toByteArray();
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (final IOException e) {
            throw new ServiceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    // meh
               }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    //meh
               }
            }            
        }   
    }

    public void writeBinary(final URI ivorn, final byte[] content) throws InvalidArgumentException, ServiceException, SecurityException {
        final InputStream r = new ByteArrayInputStream(content);
        OutputStream w = null;
        if (! exists(ivorn)) {
            createFile(ivorn);
        }
        try {
            w = mkOutputStream(ivorn,content.length);
            //w = node(ivorn).writeContent();
            Piper.pipe(r, w);
        } catch (final IOException e) {
            throw new ServiceException(e);
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (final NotFoundException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (final IOException e) {
                    //meh
                }
            }
        }        
    }
    
 
    
    public URL getReadContentURL(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            // should follow example in AxisNodeWrapper and always go back to server for this - to be safe.
           // return node(ivorn).contentURL();
            final FileManagerNode node = node(ivorn);
            final TransferInfo info =  node.getNodeDelegate().readContent(node.getMetadata().getNodeIvorn());
            return new URL(info.getUri().toString());
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (final MalformedURLException e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    

    public URL getWriteContentURL(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            final FileManagerNode node = node(ivorn);     
            //From more careful reading of AxisNodeWrapper, seems
            // like we should always go back to the server to get a storage location.
            // then up to server whether it allocates a new storage location or not
            // another advantage: handles the case of this node changing under our feet.
            // a bit less efficient, but more robust.
            
         //   if (node.getMetadata().getContentId() == null) { // no data - need to set up a url
                final TransferInfo props= node.getNodeDelegate().writeContent(node.getMetadata().getNodeIvorn());               
                 return new URL(props.getUri().toString());
          /*  } else {
                return node.contentURL();
            }*/
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (final MalformedURLException e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }         
    }
    

    public void transferCompleted(final URI arg0) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException {
        final FileManagerNode node = node(arg0);
        try {
            node.transferCompleted();
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }
    }
    public void copyContentToURL(final URI ivorn,final URL destination) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        final FileManagerNode node = node(ivorn);
        if (destination.getProtocol().equals("file")) {
            InputStream is = null;
            OutputStream os = null;
            try {
            
            File file = new File(new URI(destination.toString()));
            if(file.isDirectory()) {
                file = new File(new URI(destination.toString() + node.getName()));
                file.createNewFile();
                //do we care if the above method was false just means your going to overwrite
                //the file which is a typical "cp" like thing to do.                
            }
            os = new FileOutputStream(file);
            is = node.readContent();
            Piper.pipe(is,os);
            } catch (final FileNotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (final IOException e) {
                throw new ServiceException(e);
            } catch (final UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (final URISyntaxException e) {
                throw new InvalidArgumentException(e);
            } finally {
                if (os != null) {                    
                        try {
                            os.close();
                        } catch (final IOException e1) {                           
                            logger.warn("IOException",e1);
                        }                    
                } 
                if (is != null) {                    
                    try {
                        is.close();
                    } catch (final IOException e1) {
                        logger.warn("IOException",e1);
                    }
                }
            }
        } else {            
            try {
                node.copyContentToURL(destination);
            } catch (final NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (final FileManagerFault e) {
                throw new ServiceException(e);
            } catch (final UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (final RemoteException e) {
                throw new ServiceException(e);
            } catch (final MalformedURIException e) {
                throw new InvalidArgumentException(e);
            }
        }
    }
    
    public void copyURLToContent(final URL src,final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        InputStream is = null;
        OutputStream os = null;
        
        if (! exists(ivorn)) {
            createFile(ivorn);
        }        
        
        if (src.getProtocol().equals("file")) {
            try {
            is = src.openStream();
            final File f = new File(new URI(src.toString()));            
            os = mkOutputStream(ivorn,f.length());
            //os = node.writeContent();
            Piper.pipe(is,os);
            } catch (final FileNotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (final UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (final IOException e) {
                throw new ServiceException(e);
            } catch (final URISyntaxException e) {
                throw new InvalidArgumentException(e);
            } finally {
                if (os != null) {                    
                        try {
                            os.close();
                        } catch (final IOException e1) {                           
                            logger.warn("IOException",e1);
                        }                    
                } 
                if (is != null) {                    
                    try {
                        is.close();
                    } catch (final IOException e1) {
                        logger.warn("IOException",e1);
                    }
                }
            }
        } else {
            try {
                final FileManagerNode node = node(ivorn);
                node.copyURLToContent(src);
            } catch (final NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (final FileManagerFault e) {
                throw new ServiceException(e);
            } catch (final UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (final RemoteException e) {
                throw new ServiceException(e);
            } catch (final MalformedURIException e) {
                throw new InvalidArgumentException(e);
            }        
        }
 
    }

    
    
    //navigation
    
    public URI getParent(final URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            return cvt(node(ivorn).getParentNode().getIvorn());
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        }  catch (final RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    

    public String[] list(final URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        final FileManagerNode node =  node(ivorn);
        final String[] result = new String[node.getChildCount()];
        try {
            final NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                final FileManagerNode child = i.nextNode();
                result[ix] = child.getName();
            }
            return result;
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (final NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }

    }

    public URI[] listIvorns(final URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        final FileManagerNode node =  node(ivorn);
        final URI[] result = new URI[node.getChildCount()];
        try {
            final NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                final FileManagerNode child = i.nextNode();
                result[ix] = cvt(child.getIvorn());
            }
            return result;
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (final NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }

    }    
    
    

    public NodeInformation[] listNodeInformation(final URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        final FileManagerNode node =  node(ivorn);
        final NodeInformation[] result = new NodeInformation[node.getChildCount()];
        try {
            final NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                final FileManagerNode child = i.nextNode();
                result[ix] = getNodeInformation(cvt(child.getIvorn()));
            }
            return result;
        } catch (final UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (final NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }

    }

    /**
     * @throws NotFoundException
     * @throws SecurityException
     * @throws ServiceException
     * @throws InvalidArgumentException
     * @see org.astrogrid.acr.astrogrid.Myspace#getNodeInformation(java.net.URI)
     */
    public NodeInformation getNodeInformation(final URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
            final FileManagerNode node = node(ivorn);
            final org.astrogrid.filemanager.client.NodeMetadata md = node.getMetadata();
            return new NodeInformation(node.getName(),ivorn,md.getSize(),md.getCreateDate(),md.getModifyDate(),md.getAttributes(),node.isFile(),
                    node.isFile() ? md.getContentLocation(): null);        
    }


    

    
    
    // metadata
    
    public void refresh(final URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            node(ivorn).refresh();
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    // management
    public void delete(final URI ivorn) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException  {
        try {
            node(ivorn).delete();
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    
    
    public URI rename(final URI srcIvorn,final String newName) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException {
        final FileManagerNode node = node(srcIvorn);        
        try {
            node.move(newName,node.getParentNode(),null);
            return cvt(node.getIvorn());
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    public URI move(final URI srcIvorn,final URI newParentIvorn, final String newName) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException  {
        final FileManagerNode node = node(srcIvorn);
        final FileManagerNode parent = node(newParentIvorn);
        try {
            node.move(newName,parent,null);
            return cvt(node.getIvorn());
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }
  
    }
    
    public void changeStore(final URI srcIvorn,final URI storeIvorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        final FileManagerNode node = node(srcIvorn);
        try {
            node.move(null,null,cvt(storeIvorn));
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    public URI copy(final URI srcIvorn,final URI newParentIvorn,final String newName) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        final FileManagerNode node = node(srcIvorn);
        final FileManagerNode parent = node(newParentIvorn);
        try {
        node.copy(newName,parent,null);    
        return cvt(node.getIvorn());
        } catch (final DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (final NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (final FileManagerFault e) {
            throw new ServiceException(e);
        } catch (final RemoteException e) {
            throw new ServiceException(e);
        }        
    }

       

    public void userLogin(final UserLoginEvent e) {
        //meh
    }


    public synchronized void userLogout(final UserLoginEvent e) {
        this.client = null;
        this.home = null;
    }


    /**
     * @throws NotFoundException
     * @throws ServiceException
     * @throws SecurityException
     * @throws InvalidArgumentException
     * @throws UnsupportedOperationException
     * @see org.astrogrid.desktop.modules.ag.MyspaceInternal#getInputStream(java.net.URI)
     */
    public InputStream getInputStream(final URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try{
        if (u.getScheme() == null || u.getScheme().equals("ivo")) {
            return node(u).readContent();
        } else if (u.getScheme().equals("file")) {
            return new FileInputStream(new File(u));
        } else {
            return u.toURL().openStream();
        }
    } catch (final FileNotFoundException e) {
        throw new NotFoundException(e);
    } catch (final FileManagerFault e) {
        throw new ServiceException(e);
    } catch (final IOException e) {
        throw new ServiceException(e);
    }
   
    }

    /**
     * @throws ServiceException
     * @throws SecurityException
     * @throws NotFoundException
     * @throws InvalidArgumentException
     * @throws UnsupportedOperationException
     * @see org.astrogrid.desktop.modules.ag.MyspaceInternal#getOutputStream(java.net.URI)
     */
    public OutputStream getOutputStream(final URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
       try {
        if (u.getScheme() == null || u.getScheme().equals("ivo")) {
            //return node(u).writeContent();
            return mkOutputStream(u);
        } else if (u.getScheme().equals("file")) {
            return new FileOutputStream(new File(u));
        } else {
            return u.toURL().openConnection().getOutputStream();                      
        }      
       } catch (final FileNotFoundException e) {
           throw new NotFoundException(e);
       } catch (final FileManagerFault e) {
           throw new ServiceException(e);
       } catch (final IOException e) {
           throw new ServiceException(e);
       }
    }

    public OutputStream getOutputStream(final URI u, final long size) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            if (u.getScheme() == null || u.getScheme().equals("ivo")) {
                //return node(u).writeContent();
                return mkOutputStream(u,size);
            } else if (u.getScheme().equals("file")) {
                return new FileOutputStream(new File(u));
            } else {
                return u.toURL().openConnection().getOutputStream();                      
            }      
           } catch (final FileNotFoundException e) {
               throw new NotFoundException(e);
           } catch (final FileManagerFault e) {
               throw new ServiceException(e);
           } catch (final IOException e) {
               throw new ServiceException(e);
           }
    }

	public Service[] listStores() throws ServiceException {
		return new Service[]{};
		//@FIXME implement in reg 1.0
		/*
	      ResourceData[] arr;
	      try {
	          arr =RegistryDelegateFactory.createQuery().getResourceDataByRelationship("ivo://org.astrogrid/FileStoreKind");
	          ResourceInformation[] result = new ResourceInformation[arr.length];
	          for (int i = 0; i < arr.length; i++) {
	              result[i] = new ResourceInformation(new URI(arr[i].getIvorn().toString()),arr[i].getTitle(),arr[i].getDescription(),arr[i].getAccessURL(),null);//@todo add in logo here.
	          }
	          return result;        
	      } catch (RegistryException e) {
	          throw new ServiceException(e);
	      } catch (URISyntaxException e) {
	          throw new ServiceException(e);
	      }*/
	}    





}


/* 
$Log: VospaceImpl.java,v $
Revision 1.29  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.28  2008/08/19 12:47:10  nw
findbugs fixes and improvements.

Revision 1.27  2008/03/10 18:08:45  nw
moved computation inside conditional

Revision 1.26  2008/02/01 07:53:02  nw
documentation fix

Revision 1.25  2007/10/08 08:34:31  nw
added piping to in future allow client to write results direct to vfs cache.

Revision 1.24  2007/09/21 16:35:14  nw
improved error reporting,
various code-review tweaks.

Revision 1.23  2007/08/22 22:33:42  nw
Complete - task 144: clear view when logging out and loggin in as different user

Revision 1.22  2007/07/26 18:21:45  nw
merged mark's and noel's branches

Revision 1.21.2.1  2007/07/24 17:50:11  nw
class re-arrangement

Revision 1.21  2007/06/18 16:27:15  nw
javadoc

Revision 1.20  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.19  2007/03/22 19:00:45  nw
moved all auth components into separate folder, to keep separate from ag stuff.

Revision 1.18  2007/03/08 17:44:04  nw
first draft of voexplorer

Revision 1.17  2007/01/29 10:56:03  nw
prefetches myspace root on login.

Revision 1.16  2007/01/09 16:21:22  nw
minor

Revision 1.15  2006/11/09 12:08:33  nw
final set of changes for 2006.4.rc1

Revision 1.14  2006/08/31 21:28:59  nw
doc fix.

Revision 1.13  2006/08/15 10:16:24  nw
migrated from old to new registry models.

Revision 1.12  2006/06/27 10:23:51  nw
findbugs tweaks

Revision 1.11  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.10  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.9  2006/03/13 18:27:34  nw
fixed queries to not restrict to @status='active'

Revision 1.8  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.7  2005/10/14 14:20:41  nw
work around for problems with FileStoreOutputStream

Revision 1.6  2005/10/13 18:33:47  nw
fixes supporting getWriteContentURL

Revision 1.5  2005/10/06 09:19:26  KevinBenson
Added a writeStream method to pass in a inputstream for storing into myspace.

Revision 1.4  2005/10/04 20:39:10  KevinBenson
added the ability to save a file to a local directory not just overwrite a file

Revision 1.3  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.2  2005/08/16 13:19:32  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.8  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.7  2005/07/08 14:06:30  nw
final fixes for the workshop.

Revision 1.6  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.2  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.3  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.2  2005/04/22 10:54:36  nw
added missing methods to vospace.
made a start at getting applications working again.

Revision 1.2.2.1  2005/04/15 13:00:47  nw
got vospace browser working.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/13 12:23:30  nw
refactored a common base class for ui components

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.3  2005/02/22 13:55:21  nw
got vospace ls working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/