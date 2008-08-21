package org.astrogrid.store.tree;

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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public class FileManagerTreeClient implements TreeClient {

    /** Construct a new FIleManagerTreeClient
     * 
     */
    public FileManagerTreeClient() {
        super();
        this.fac = new FileManagerClientFactory();
    }
    
    protected final FileManagerClientFactory fac;
    protected FileManagerClient client;
    
    public FileManagerTreeClient(FileManagerClientFactory fac) {
        super();
        this.fac = fac;
    }
    

    /**
     * @see org.astrogrid.store.tree.TreeClient#login(org.astrogrid.store.Ivorn, java.lang.String)
     */
    public void login(Ivorn ivorn, String password) throws TreeClientLoginException, TreeClientServiceException {
        try {
            this.client = fac.login(ivorn,password);
           
        } catch (IllegalArgumentException e) { // replicate behaviour of original.
            throw e;            
        } catch (Exception e) {
            throw new TreeClientServiceException(e.getMessage(),e);
        }
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#logout()
     */
    public void logout() throws TreeClientServiceException {
        this.client = null;
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#getToken()
     * Unsupported operation - returns null;
     */
    public SecurityToken getToken() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    /**
     * @see org.astrogrid.store.tree.TreeClient#getRoot()
     */
    public Container getRoot() throws TreeClientSecurityException, TreeClientServiceException {
            if (client == null) {
                throw new TreeClientSecurityException("Not logged in");
            }
        try {
            return new FMNodeContainer(client.home());        
        } catch (Exception e) {
            throw new TreeClientServiceException("getRoot()",e);
        }
    }
    
    
    public class FMNodeNode implements Node {
        public FMNodeNode(FileManagerNode n) {
            this.n = n;
        }
        protected final FileManagerNode n;
        /**
         * @see org.astrogrid.store.tree.Node#getName()
         */
        public String getName() {
            return n.getName();
        }

        /**
         * @see org.astrogrid.store.tree.Node#isFile()
         */
        public boolean isFile() {
            return n.isFile();
        }

        /**
         * @see org.astrogrid.store.tree.Node#isContainer()
         */
        public boolean isContainer() {
            return n.isFolder();
        }
    }
    /**
     * 
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
     * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
     *
     */
    class FMNodeContainer extends FMNodeNode implements Container {
        public FMNodeContainer(FileManagerNode n) {
            super(n);
        }

        /**
         * @see org.astrogrid.store.tree.Container#getChildNodes()
         */
        public Collection getChildNodes() {
            try {
            int num = n.getChildCount();
            ArrayList l = new ArrayList(num);
            for (Iterator i = n.iterator(); i.hasNext(); ) {
                FileManagerNode c = (FileManagerNode)i.next();
                if (c.isFile()) {
                    l.add(new FMNodeFile(c));
                } else {
                    l.add(new FMNodeContainer(c));
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
        public Container addContainer(String name) throws TreeClientServiceException, TreeClientDuplicateException {
            try {
                return new FMNodeContainer(n.addFolder(name));
            } catch (DuplicateNodeFault e) {
                throw new TreeClientDuplicateException(e.getMessage());
            } catch (Exception e) {
                throw new TreeClientServiceException("addContainer",e);
            }
        }

        /**
         * @see org.astrogrid.store.tree.Container#addFile(java.lang.String)
         */
        public File addFile(String name) throws TreeClientServiceException, TreeClientDuplicateException {
            try {
                return new FMNodeFile(n.addFile(name));
            } catch (DuplicateNodeFault e) {
                throw new TreeClientDuplicateException(e.getMessage());
            } catch (Exception e) {
                throw new TreeClientServiceException("addFile",e);
            }
        }

        /**
         * 
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
         * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
         *
         */
        class FMNodeFile extends FMNodeNode implements File {
            public FMNodeFile(FileManagerNode n) {
                super(n);
            }

            /**
             * @see org.astrogrid.store.tree.File#getMimeType()
             */
            public String getMimeType() {
                Object value =  n.getMetadata().getAttributes().get(FileProperties.MIME_TYPE_PROPERTY);
                return value != null ? value.toString() : null;
            }

            /**
             * @see org.astrogrid.store.tree.File#getOutputStream()
             */
            public OutputStream getOutputStream() throws TreeClientServiceException {
                try {
                    return n.writeContent();
                } catch (Exception e) {
                    throw new TreeClientServiceException("getOutputStream",e);           
                }
            }

            /**
             * @see org.astrogrid.store.tree.File#getInputStream()
             */
            public InputStream getInputStream() throws TreeClientServiceException {
                try {
                    return n.readContent();
                } catch (Exception e) {
                    throw new TreeClientServiceException("getInputStream",e);           
                }                
            }
        }
        
    }

}


/* 
$Log: FileManagerTreeClient.java,v $
Revision 1.3  2008/08/21 09:01:27  gtr
Branch fm-gtr-2815 is merged. This fixes BZ2815.

Revision 1.2.100.1  2008/08/20 12:58:12  gtr
I removed redundant fields and checked exceptions.

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.2  2005/03/01 15:07:32  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore
 
*/