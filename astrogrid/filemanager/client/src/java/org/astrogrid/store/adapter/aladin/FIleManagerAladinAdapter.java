/*$Id: FIleManagerAladinAdapter.java,v 1.3 2008/08/21 09:01:27 gtr Exp $
 * Created on 25-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.adapter.aladin;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filestore.common.file.FileProperties;
import org.astrogrid.store.Ivorn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.AladinAdapterFileManagerClientFactory} instead.
 */
public class FIleManagerAladinAdapter implements AladinAdapter {

    /** Construct a new FIleManagerTreeClient
     * 
     */
    public FIleManagerAladinAdapter() {
        super();
        this.fac = new FileManagerClientFactory();
    }
    
    protected final FileManagerClientFactory fac;
    protected FileManagerClient client;
    

    /**
     * @see org.astrogrid.store.tree.TreeClient#login(org.astrogrid.store.Ivorn, java.lang.String)
     */
    public void login(Ivorn ivorn, String password) throws AladinAdapterLoginException, AladinAdapterServiceException {
        try {
            this.client = fac.login(ivorn,password);
        } catch (Exception e) {
            throw new AladinAdapterServiceException(e.getMessage(),e);
        }
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#logout()
     */
    public void logout() throws AladinAdapterServiceException {
        this.client = null;
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#getToken()
     * Unsupported operation - returns null;
     */
    public SecurityToken getToken() {
        return null;
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#getRoot()
     */
    public AladinAdapterContainer getRoot() throws AladinAdapterSecurityException, AladinAdapterServiceException {
        try {
            return new FMNodeAladinAdapterContainer(client.home());
        } catch (Exception e) {
            throw new AladinAdapterServiceException("getRoot()",e);
        }
    }
    
    
    public class FMNodeAladinAdapterNode implements AladinAdapterNode {
        public FMNodeAladinAdapterNode(FileManagerNode n) {
            this.n = n;
        }
        protected final FileManagerNode n;
        /**
         * @see org.astrogrid.store.tree.AladinAdapterNode#getName()
         */
        public String getName() {
            return n.getName();
        }

        /**
         * @see org.astrogrid.store.tree.AladinAdapterNode#isAladinAdapterFile()
         */
        public boolean isAladinAdapterFile() {
            return n.isFile();
        }

        /**
         * @see org.astrogrid.store.tree.AladinAdapterNode#isFolder()
         */
        public boolean isContainer() {
            return n.isFolder();
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#isFile()
         */
        public boolean isFile() {
            return n.isFile();
        }
    }
    /**
     * 
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.AladinAdapterFileManagerClientFactory} instead.
     * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
     *
     */
    class FMNodeAladinAdapterContainer extends FMNodeAladinAdapterNode implements AladinAdapterContainer {
        public FMNodeAladinAdapterContainer(FileManagerNode n) {
            super(n);
        }

        /**
         * @see org.astrogrid.store.tree.Container#getChildAladinAdapterNodes()
         */
        public Collection getChildNodes() {
            try {
            int num = n.getChildCount();
            ArrayList l = new ArrayList(num);
            for (Iterator i = n.iterator(); i.hasNext(); ) {
                FileManagerNode c = (FileManagerNode)i.next();
                if (c.isFile()) {
                    l.add(new FMNodeAladinAdapterFile(c));
                } else {
                    l.add(new FMNodeAladinAdapterContainer(c));
                }
            }
            return l;
            } catch (RemoteException f) {
                throw new RuntimeException(f);
            }
        }

        /**
         * @see org.astrogrid.store.tree.Container#addContainer(java.lang.String)
         */
        public AladinAdapterContainer addContainer(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            try {
                return new FMNodeAladinAdapterContainer(n.addFolder(name));
            } catch (DuplicateNodeFault e) {
                throw new AladinAdapterDuplicateException(e.getMessage());
            } catch (Exception e) {
                throw new AladinAdapterServiceException("addContainer",e);
            }
        }

        /**
         * @see org.astrogrid.store.tree.Container#addAladinAdapterFile(java.lang.String)
         */
        public AladinAdapterFile addFile(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            try {
                return new FMNodeAladinAdapterFile(n.addFile(name));
            } catch (DuplicateNodeFault e) {
                throw new AladinAdapterDuplicateException(e.getMessage());
            } catch (Exception e) {
                throw new AladinAdapterServiceException("addFile",e);
            }
        }

        /**
         * 
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.AladinAdapterFileManagerClientFactory} instead.
         * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
         *
         */
        class FMNodeAladinAdapterFile extends FMNodeAladinAdapterNode implements AladinAdapterFile {
            public FMNodeAladinAdapterFile(FileManagerNode n) {
                super(n);
            }

            /** @todo add to AladinAdapterNode Metadata to support this.
             * @see org.astrogrid.store.tree.AladinAdapterFile#getMimeType()
             */
            public String getMimeType() {
                Object value =  n.getMetadata().getAttributes().get(FileProperties.MIME_TYPE_PROPERTY);
                return value != null ? value.toString() : null;
            }

            /**
             * @see org.astrogrid.store.tree.AladinAdapterFile#getOutputStream()
             */
            public OutputStream getOutputStream() throws AladinAdapterServiceException {
                try {
                    return n.writeContent();
                } catch (Exception e) {
                    throw new AladinAdapterServiceException("getOutputStream",e);           
                }
            }

            /**
             * @see org.astrogrid.store.tree.AladinAdapterFile#getInputStream()
             */
            public InputStream getInputStream() throws AladinAdapterServiceException {
                try {
                    return n.readContent();
                } catch (Exception e) {
                    throw new AladinAdapterServiceException("getOutputStream",e);           
                }                
            }

            /**
             * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getURL()
             */
            public URL getURL() throws AladinAdapterServiceException {
                try {
                    return n.contentURL();
                } catch (Exception e) {
                    throw new AladinAdapterServiceException("getURL()",e);
                }
            }
        }
        
    }

}


/* 
$Log: FIleManagerAladinAdapter.java,v $
Revision 1.3  2008/08/21 09:01:27  gtr
Branch fm-gtr-2815 is merged. This fixes BZ2815.

Revision 1.2.100.1  2008/08/20 12:58:12  gtr
I removed redundant fields and checked exceptions.

Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore
 
*/