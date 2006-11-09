/*$Id: VospaceImpl.java,v 1.15 2006/11/09 12:08:33 nw Exp $
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
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.NodeInformation;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.community.common.exception.CommunityException;
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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;

/** implementation of the vospace componet.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
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
    public VospaceImpl(Community community, BundlePreferences preferences) {
        super();
        this.community = community;
        this.prefs = preferences;
    }
   protected final Community community;
   protected final BundlePreferences prefs;
    protected URI home;
    protected FileManagerClient client;
    
    public synchronized FileManagerClient getClient() throws CommunityException, RegistryException, URISyntaxException {
        if (client == null) {
            FileManagerClientFactory fac = new FileManagerClientFactory(prefs); 
            client = fac.login(new Ivorn(community.getUserInformation().getId().toString()),community.getUserInformation().getPassword());
        } 
        return client;
    }
    
    public void createFile(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        try {
            getClient().createFile(cvt(ivorn));
        } catch (CommunityException e) {
            throw new SecurityException(e);
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("A resource with this name already exists",e);
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }
    
    public void createFolder(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        try {
        getClient().createFolder(cvt(ivorn));
        } catch (CommunityException e) {
            throw new SecurityException(e);
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("A resource with this name already exists",e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }         
    }
    


    // access
    public URI getHome() throws SecurityException, ServiceException, NotFoundException {
        if (home == null) {
        try {
            home = new URI(getClient().home().getIvorn().toString());
        } catch (CommunityException e) {
            throw new SecurityException(e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (Exception e) {
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
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException("this is not a myspace reference: " + uri, e); 
        }
    }
           
    final URI cvt(Ivorn iv) throws ServiceException {
        try {
            return new URI(iv.toString());
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        }
    }
    
    /**
     * @param parentIvorn
     * @return
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws SecurityException
     * @throws ServiceException
     */
    public FileManagerNode node(URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            Ivorn ivo = cvt(ivorn);
            FileManagerNode node =  getClient().node(ivo);
            return node;
        } catch (CommunityException e) {
            throw new SecurityException(e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }    
   
    
    public boolean exists(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {       
        try {
            return getClient().exists(cvt(ivorn)) != null;
        } catch (CommunityException e) {
            throw new SecurityException(e);
        } catch (Exception e) {
            throw new ServiceException(e);  
        }
    }
        
    //creation  
    public URI  createChildFolder(URI parentIvorn, String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
            try {
                return cvt(node(parentIvorn).addFolder(name).getIvorn());
            } catch (FileManagerFault e) {
                throw new ServiceException(e);
            } catch (NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (DuplicateNodeFault e) {
                throw new InvalidArgumentException(e);
            } catch (RemoteException e) {
                throw new ServiceException(e);
            } catch (UnsupportedOperationException e) {
                   throw new InvalidArgumentException(e);
            }        
    }
        


    public URI createChildFile(URI parentIvorn, String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException{
        try {
            return cvt(node(parentIvorn).addFile(name).getIvorn());
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        } catch (UnsupportedOperationException e) {
               throw new InvalidArgumentException(e);
        }        
    }
    // content
    public String read(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        Reader reader = null;
        StringWriter writer = null;
        try {
           reader =new InputStreamReader(node(ivorn).readContent());
           writer = new StringWriter();
            Piper.pipe(reader,writer);
            return writer.toString();
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (IOException e) {
            throw new ServiceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
               }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
               }
            }            
        }                                    
    }


    public void write(URI ivorn, String content) throws InvalidArgumentException, ServiceException, SecurityException {
        Reader r = new StringReader(content);
        Writer w = null;
        if (! exists(ivorn)) {
            createFile(ivorn);
        }
        try {
            w = new OutputStreamWriter (mkOutputStream(ivorn,content.getBytes().length));
            //w = new OutputStreamWriter(node(ivorn).writeContent());
            Piper.pipe(r, w);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
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
    private OutputStream mkOutputStream(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException{
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
    private OutputStream mkOutputStream(final URI ivorn, long dataSize)  throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException{
        // rewrite of FileStoreOutputStream really
        URL url = getWriteContentURL(ivorn);
        try {
        URLConnection c = url.openConnection();
        if (c instanceof HttpURLConnection) {
            final HttpURLConnection conn = (HttpURLConnection)c;
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            if (dataSize > 0) { // set the 1.5 method, if available.
                try {
                Method m= conn.getClass().getMethod("setFixedLengthStreamingMode",new Class[]{int.class});
                m.invoke(conn,new Object[]{new Integer((int)dataSize)}); // mad that this method only takes an int.
                } catch (Exception e) {
                    logger.debug("HttpURLConnection does not support FixesLengthStreamingMode",e);
                }                
            }
            conn.connect();
            return new FilterOutputStream(conn.getOutputStream()) {
                public void close() throws IOException {
                    super.flush();
                    super.close();
                    conn.getResponseCode();
                    try {
                        transferCompleted(ivorn);
                    } catch (ACRException e) {
                        logger.warn("Failed to notify transfer completed",e);
                    }
                }
            };
        } else {
            logger.warn("Some other kind of filestore url - handling it and hoping for the best");
            return new FilterOutputStream( url.openConnection().getOutputStream()) {
                public void close() throws IOException {
                    super.close();
                    try {
                        transferCompleted(ivorn);
                    } catch (ACRException e) {
                        logger.warn("Failed to notify transfer completed",e);
                    }                    
                }
            };
        }
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    public byte[] readBinary(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        InputStream reader = null;
        ByteArrayOutputStream writer = null;
        try {
           reader = node(ivorn).readContent();
           writer = new ByteArrayOutputStream();
            Piper.pipe(reader,writer);
            return writer.toByteArray();
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (IOException e) {
            throw new ServiceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
               }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
               }
            }            
        }   
    }

    public void writeBinary(URI ivorn, byte[] content) throws InvalidArgumentException, ServiceException, SecurityException {
        InputStream r = new ByteArrayInputStream(content);
        OutputStream w = null;
        if (! exists(ivorn)) {
            createFile(ivorn);
        }
        try {
            w = mkOutputStream(ivorn,content.length);
            //w = node(ivorn).writeContent();
            Piper.pipe(r, w);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new ServiceException(e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                }
            }
        }        
    }
    
 
    
    public URL getReadContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            // should follow example in AxisNodeWrapper and always go back to server for this - to be safe.
           // return node(ivorn).contentURL();
            FileManagerNode node = node(ivorn);
            TransferInfo info =  node.getNodeDelegate().readContent(node.getMetadata().getNodeIvorn());
            return new URL(info.getUri().toString());
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    

    public URL getWriteContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            FileManagerNode node = node(ivorn);     
            //From more careful reading of AxisNodeWrapper, seems
            // like we should always go back to the server to get a storage location.
            // then up to server whether it allocates a new storage location or not
            // another advantage: handles the case of this node changing under our feet.
            // a bit less efficient, but more robust.
            
         //   if (node.getMetadata().getContentId() == null) { // no data - need to set up a url
                TransferInfo props= node.getNodeDelegate().writeContent(node.getMetadata().getNodeIvorn());               
                 return new URL(props.getUri().toString());
          /*  } else {
                return node.contentURL();
            }*/
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }         
    }
    

    public void transferCompleted(URI arg0) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException {
        FileManagerNode node = node(arg0);
        try {
            node.transferCompleted();
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }
    public void copyContentToURL(URI ivorn,URL destination) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        FileManagerNode node = node(ivorn);
        if (destination.getProtocol().equals("file")) {
            InputStream is = null;
            OutputStream os = null;
            try {
            
            File file = new File(new URI(destination.toString()));
            if(file.isDirectory()) {
                file = new File(new URI(destination.toString() + node.getName()));
                boolean fileCreated = file.createNewFile();
                //do we care if the above method was false just means your going to overwrite
                //the file which is a typical "cp" like thing to do.                
            }
            os = new FileOutputStream(file);
            is = node.readContent();
            Piper.pipe(is,os);
            } catch (FileNotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (IOException e) {
                throw new ServiceException(e);
            } catch (UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (URISyntaxException e) {
                throw new InvalidArgumentException(e);
            } finally {
                if (os != null) {                    
                        try {
                            os.close();
                        } catch (IOException e1) {                           
                            logger.warn("IOException",e1);
                        }                    
                } 
                if (is != null) {                    
                    try {
                        is.close();
                    } catch (IOException e1) {
                        logger.warn("IOException",e1);
                    }
                }
            }
        } else {            
            try {
                node.copyContentToURL(destination);
            } catch (NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (FileManagerFault e) {
                throw new ServiceException(e);
            } catch (UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (RemoteException e) {
                throw new ServiceException(e);
            } catch (MalformedURIException e) {
                throw new InvalidArgumentException(e);
            }
        }
    }
    
    public void copyURLToContent(URL src,URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        InputStream is = null;
        OutputStream os = null;
        
        if (! exists(ivorn)) {
            createFile(ivorn);
        }        
        FileManagerNode node = node(ivorn);
        
        if (src.getProtocol().equals("file")) {
            try {
            is = src.openStream();
            File f = new File(new URI(src.toString()));            
            os = mkOutputStream(ivorn,f.length());
            //os = node.writeContent();
            Piper.pipe(is,os);
            } catch (FileNotFoundException e) {
                throw new InvalidArgumentException(e);
            } catch (UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (IOException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new InvalidArgumentException(e);
            } finally {
                if (os != null) {                    
                        try {
                            os.close();
                        } catch (IOException e1) {                           
                            logger.warn("IOException",e1);
                        }                    
                } 
                if (is != null) {                    
                    try {
                        is.close();
                    } catch (IOException e1) {
                        logger.warn("IOException",e1);
                    }
                }
            }
        } else {
            try {
                node.copyURLToContent(src);
            } catch (NodeNotFoundFault e) {
                throw new NotFoundException(e);
            } catch (FileManagerFault e) {
                throw new ServiceException(e);
            } catch (UnsupportedOperationException e) {
                throw new InvalidArgumentException(e);
            } catch (RemoteException e) {
                throw new ServiceException(e);
            } catch (MalformedURIException e) {
                throw new InvalidArgumentException(e);
            }        
        }
 
    }

    
    
    //navigation
    
    public URI getParent(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            return cvt(node(ivorn).getParentNode().getIvorn());
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        }  catch (RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    

    public String[] list(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        FileManagerNode node =  node(ivorn);
        String[] result = new String[node.getChildCount()];
        try {
            NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                FileManagerNode child = i.nextNode();
                result[ix] = child.getName();
            }
            return result;
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);
        } catch (NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }

    }

    public URI[] listIvorns(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        FileManagerNode node =  node(ivorn);
        URI[] result = new URI[node.getChildCount()];
        try {
            NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                FileManagerNode child = i.nextNode();
                result[ix] = cvt(child.getIvorn());
            }
            return result;
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }

    }    
    
    

    public NodeInformation[] listNodeInformation(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        FileManagerNode node =  node(ivorn);
        NodeInformation[] result = new NodeInformation[node.getChildCount()];
        try {
            NodeIterator i = node.iterator();
            for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
                FileManagerNode child = i.nextNode();
                result[ix] = getNodeInformation(cvt(child.getIvorn()));
            }
            return result;
        } catch (UnsupportedOperationException e) {
            throw new InvalidArgumentException(e);            
        } catch (NodeNotFoundFault e) {
            throw new ServiceException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
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
    public NodeInformation getNodeInformation(URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
            FileManagerNode node = node(ivorn);
            org.astrogrid.filemanager.client.NodeMetadata md = node.getMetadata();
            return new NodeInformation(node.getName(),ivorn,md.getSize(),md.getCreateDate(),md.getModifyDate(),md.getAttributes(),node.isFile(),
                    node.isFile() ? md.getContentLocation(): null);        
    }


    

    
    
    // metadata
    
    public void refresh(URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            node(ivorn).refresh();
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    // management
    public void delete(URI ivorn) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException  {
        try {
            node(ivorn).delete();
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        } 
    }
    
    
    public URI rename(URI srcIvorn,String newName) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException {
        FileManagerNode node = node(srcIvorn);        
        try {
            node.move(newName,node.getParentNode(),null);
            return cvt(node.getIvorn());
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    public URI move(URI srcIvorn,URI newParentIvorn, String newName) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException  {
        FileManagerNode node = node(srcIvorn);
        FileManagerNode parent = node(newParentIvorn);
        try {
            node.move(newName,parent,null);
            return cvt(node.getIvorn());
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
  
    }
    
    public void changeStore(URI srcIvorn,URI storeIvorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        FileManagerNode node = node(srcIvorn);
        try {
            node.move(null,null,cvt(storeIvorn));
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }
    }
    
    public URI copy(URI srcIvorn,URI newParentIvorn,String newName) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        FileManagerNode node = node(srcIvorn);
        FileManagerNode parent = node(newParentIvorn);
        try {
        node.copy(newName,parent,null);    
        return cvt(node.getIvorn());
        } catch (DuplicateNodeFault e) {
            throw new InvalidArgumentException("Already exists",e);
        } catch (NodeNotFoundFault e) {
            throw new NotFoundException(e);
        } catch (FileManagerFault e) {
            throw new ServiceException(e);
        } catch (RemoteException e) {
            throw new ServiceException(e);
        }        
    }


  public ResourceInformation[] listAvailableStores() throws ServiceException {
      //@todo edit to only select active stores 
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
    }

  }
    
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }



    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public synchronized void userLogout(UserLoginEvent e) {
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
    public InputStream getInputStream(URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try{
        if (u.getScheme() == null || u.getScheme().equals("ivo")) {
            return node(u).readContent();
        } else if (u.getScheme().equals("file")) {
            return new FileInputStream(new File(u));
        } else {
            return u.toURL().openStream();
        }
    } catch (FileNotFoundException e) {
        throw new NotFoundException(e);
    } catch (FileManagerFault e) {
        throw new ServiceException(e);
    } catch (IOException e) {
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
    public OutputStream getOutputStream(URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
       try {
        if (u.getScheme() == null || u.getScheme().equals("ivo")) {
            //return node(u).writeContent();
            return mkOutputStream(u);
        } else if (u.getScheme().equals("file")) {
            return new FileOutputStream(new File(u));
        } else {
            return u.toURL().openConnection().getOutputStream();                      
        }      
       } catch (FileNotFoundException e) {
           throw new NotFoundException(e);
       } catch (FileManagerFault e) {
           throw new ServiceException(e);
       } catch (IOException e) {
           throw new ServiceException(e);
       }
    }

    public OutputStream getOutputStream(URI u, long size) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
        try {
            if (u.getScheme() == null || u.getScheme().equals("ivo")) {
                //return node(u).writeContent();
                return mkOutputStream(u,size);
            } else if (u.getScheme().equals("file")) {
                return new FileOutputStream(new File(u));
            } else {
                return u.toURL().openConnection().getOutputStream();                      
            }      
           } catch (FileNotFoundException e) {
               throw new NotFoundException(e);
           } catch (FileManagerFault e) {
               throw new ServiceException(e);
           } catch (IOException e) {
               throw new ServiceException(e);
           }
    }

	public Service listStores() throws ServiceException {
		return null;
		//@implement this as part of the myspace makeover.
	}    





}


/* 
$Log: VospaceImpl.java,v $
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