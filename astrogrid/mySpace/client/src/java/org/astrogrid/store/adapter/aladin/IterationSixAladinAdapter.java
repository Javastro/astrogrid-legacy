/*$Id: IterationSixAladinAdapter.java,v 1.4 2005/01/13 11:27:39 jdt Exp $
 * Created on 05-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.adapter.aladin;

import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.tree.Container;
import org.astrogrid.store.tree.File;
import org.astrogrid.store.tree.IterationSixTreeClient;
import org.astrogrid.store.tree.Node;
import org.astrogrid.store.tree.TreeClientDuplicateException;
import org.astrogrid.store.tree.TreeClientLoginException;
import org.astrogrid.store.tree.TreeClientSecurityException;
import org.astrogrid.store.tree.TreeClientServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/** Wrapper around a {@link org.astrogrid.store.tree.TreeClient} to support the deprecated interface in this package
 * @deprecated use {@link org.astrogrid.store.tree} instead
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
 */
public class IterationSixAladinAdapter implements AladinAdapter {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(IterationSixAladinAdapter.class);

    /** Construct a new IterationSixAladinAdapter
     * 
     */
    public IterationSixAladinAdapter() {
        this.treeclient = new IterationSixTreeClient();

    }
    protected final IterationSixTreeClient treeclient;
    

    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#login(org.astrogrid.store.Ivorn, java.lang.String)
     */
    public void login(Ivorn communityIvorn, String password)
            throws AladinAdapterLoginException, AladinAdapterServiceException {
        try {
            treeclient.login(communityIvorn,password);
        } catch (TreeClientLoginException e) {
            throw new AladinAdapterLoginException(e.getMessage(),e);
        } catch (TreeClientServiceException e) {
            throw new AladinAdapterServiceException(e.getMessage(),e);
        }
    }

    public StoreClient getStoreClient() {
        return treeclient.getStoreClient();
    }
    
    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#logout()
     */
    public void logout() throws AladinAdapterServiceException {
        try {
            treeclient.logout();
        } catch (TreeClientServiceException e) {
            throw new AladinAdapterServiceException(e.getMessage(),e);
        }
        
    }    

    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#getToken()
     */
    public SecurityToken getToken() {
        return treeclient.getToken();
    }

    /** queries server, rebuilds tree.
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#getRoot()
     */
    public AladinAdapterContainer getRoot()
            throws AladinAdapterSecurityException,
            AladinAdapterServiceException {

            Container root;
            try {
                root = treeclient.getRoot();
            } catch (TreeClientSecurityException e) {
               throw new AladinAdapterSecurityException(e.getMessage(),e);
            } catch (TreeClientServiceException e) {
                throw new AladinAdapterServiceException(e.getMessage(),e);
            }
            return wrapRoot(root);
    }

    private final AladinAdapterContainer wrapRoot(Container c) {
        return new IterationSixContainer(c);
    }

    /**
     * Iteration-6 version of an aladin adapter node.
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
     *
     */
    protected class IterationSixNode implements AladinAdapterNode {
        
        public IterationSixNode(Node n) {
            this.node = n;      
        }

        protected final Node node;
        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#getName()
         */
        public String getName() {
            return node.getName();
        }

        
        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#isFile()
         */
        public boolean isFile() {
            return node.isFile();
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#isContainer()
         */
        public boolean isContainer() {
            return node.isContainer();
        }
        
        
        public boolean equals(Object obj) {
            IterationSixNode casted = (IterationSixNode)obj;            
            return this.node.equals(casted.node);
        }
        public String toString() {
            return "AladinAdapter : " + node.toString();
        }
    }
    /** 
     * Iteration 6 version of an aladin adapter container
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
     *
     */
    protected class IterationSixContainer extends IterationSixNode implements AladinAdapterContainer {

        /** Construct a new IterationSixContainer
         * @param wrapped
         */
        public IterationSixContainer(Container c) {
            super(c);
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#getChildNodes()
         */
        public Collection getChildNodes()  {
            Collection orig = ((Container)node).getChildNodes();
            Collection wrapped = new ArrayList(orig.size());
            for (Iterator i = orig.iterator(); i.hasNext(); ) {
                wrapped.add(wrap(i.next()));
            }
            return Collections.unmodifiableCollection(wrapped);
        }

        private IterationSixNode wrap(Object o) {
            if (o instanceof Container) {
                return new IterationSixContainer((Container)o);
            } else if (o instanceof File) {
                return new IterationSixFile((File)o);
            } else {
                throw new RuntimeException("Programming Error - unknown object type" + o.getClass().getName());
            }
        }
        
        /** 
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#addContainer(java.lang.String)
         */
        public AladinAdapterContainer addContainer(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            try {
                return new IterationSixContainer( ((Container)node).addContainer(name));
            } catch (TreeClientServiceException e) {
                    throw new AladinAdapterServiceException(e.getMessage(),e);
            } catch (TreeClientDuplicateException e) {
                    throw new AladinAdapterDuplicateException(e.getMessage());                            
            }
        }


        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#addFile(java.lang.String)
         */
        public AladinAdapterFile addFile(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            try {
                return new IterationSixFile( ((Container)node).addFile(name));
            } catch (TreeClientServiceException e) {
                    throw new AladinAdapterServiceException(e.getMessage(),e);
            } catch (TreeClientDuplicateException e) {
                    throw new AladinAdapterDuplicateException(e.getMessage());                            
            }  
                
        }
  
    }
    

    
    /** 
     * Iteration 6 implementation of an aladin adapter file.
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
     *
     */
    protected class IterationSixFile extends IterationSixNode implements AladinAdapterFile {

        /** Construct a new IterationSixFile
         * @param wrapped
         */
        public IterationSixFile(File node){
            super(node);
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getMimeType()
         */
        public String getMimeType() {
            return ((File)node).getMimeType();
        }

        /**
         * @throws IOException
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getOutputStream()
         */
        public OutputStream getOutputStream() throws AladinAdapterServiceException {
            try {
                return ((File)node).getOutputStream();
            } catch (TreeClientServiceException e) {
                throw new AladinAdapterServiceException(e.getMessage(),e);
            }
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getInputStream()
         */
        public InputStream getInputStream() throws AladinAdapterServiceException {
            try {
                return ((File)node).getInputStream();
            } catch (TreeClientServiceException e) {
                    throw new AladinAdapterServiceException(e.getMessage(),e);
            }
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getURL()
         */
        public URL getURL() throws AladinAdapterServiceException {
            try {
                return  getStoreClient().getUrl(((IterationSixTreeClient.IterationSixNode)node).getPath());
            } catch (IOException e) {
                throw new AladinAdapterServiceException("getURL",e);
            }
        }
    }
    

}


/* 
$Log: IterationSixAladinAdapter.java,v $
Revision 1.4  2005/01/13 11:27:39  jdt
Merges from myspace-nww-890

Revision 1.3.8.1  2005/01/12 17:08:20  nw
implemented getURL

Revision 1.3  2004/11/17 16:22:53  clq2
nww-itn07-704

Revision 1.2.2.2  2004/11/16 17:27:58  nw
tidied imports

Revision 1.2.2.1  2004/11/16 16:47:28  nw
copied aladinAdapter interfaces into a neutrally-named package.
deprecated original interfaces.
javadoc

Revision 1.2  2004/11/11 17:50:42  clq2
Noel's aladin stuff

Revision 1.1.2.2  2004/11/11 13:21:14  nw
working implementation. wasn't easy.

Revision 1.1.2.1  2004/11/09 13:56:38  nw
first stab at an iteration6 implementation
 
*/