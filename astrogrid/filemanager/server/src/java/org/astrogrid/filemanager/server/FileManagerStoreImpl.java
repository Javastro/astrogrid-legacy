/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/Attic/FileManagerStoreImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:21 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreImpl.java,v $
 *   Revision 1.3  2005/02/10 14:17:21  jdt
 *   merge from  filemanager-nww-903
 *
 *   Revision 1.2.4.1  2005/02/10 13:13:19  nw
 *   implementation of store, using xstream serialization.
 *
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.2  2005/01/12 14:28:46  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerStoreNode;
import org.astrogrid.filemanager.common.FileManagerStoreNodeMock;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;

/**
 * A persistent implementation of the store interface.
 *
 */
public class FileManagerStoreImpl    implements FileManagerStore{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(FileManagerStoreImpl.class);

    /** configuration interface, that defines locations on disk to store serialized files */
    public interface StoreLocations {
        /**
         * @return location on disk to store nodes (will be created if does not exist)
         */
        File getNodeDir();
        /**
         * @return location on disk to store accounts (will be created if does not exist)
         */
        File getAccountDir();
    };
    
    /** implementation of the configuration interface that looksup keys in standard config */
    public static class StoreLocationsFromConfig implements StoreLocations {
        public static final String NODE_DIR_KEY = "org.astrogrid.filemanager.store.nodedir";
        public static final String ACCOUNT_DIR_KEY = "org.astrogrid.filemanager.store.accountdir";
        public File getNodeDir() {
            return new File(SimpleConfig.getSingleton().getString(NODE_DIR_KEY));
        }

        public File getAccountDir() {
            return new File(SimpleConfig.getSingleton().getString(ACCOUNT_DIR_KEY));
        }
    }
    
    /** create default configured store - uses configuration keys in {@link StoreLocationsFromConfig}*/
    public FileManagerStoreImpl() throws ConfigException {
        this(new StoreLocationsFromConfig());
    }
    
    /** create a new file manager store, with default configured xstream 
     * @param locations locations on disk to store data
     * @throws ConfigException if locations specified are inaccessible
     * @throws IllegalArgumentException if a null parameter is passed in.
     * */
    public FileManagerStoreImpl(StoreLocations locations) throws ConfigException, IllegalArgumentException {
        this(locations,new XStream());
        xs.alias("node",FileManagerStoreNodeMock.class);
    }
    
    /** create a new file manager store, passing in your own xstream configuration 
     * @param locations locations on disk to store data
     * @param xs serialization object
     * @throws ConfigException if locations specified are inaccessible
     * @throws IllegalArgumentException if a null parameter is passed in*/
    public FileManagerStoreImpl(StoreLocations locations,XStream xs) throws ConfigException, IllegalArgumentException {
        if (xs == null) {
            throw new IllegalArgumentException("Cannot pass in null xstream");
        }
        if (locations == null || locations.getAccountDir() == null || locations.getNodeDir() == null) {
            throw new IllegalArgumentException("Cannot pass in null StoreLocation, or one that contains nulls");
        }
        this.xs = xs;
        this.locations = locations;
        checkLocations();
    }
    /** serialization object */
    protected final XStream xs;
    /** configuration object */
    protected final StoreLocations locations;
         
    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#hasAccount(java.lang.String)
     */
    public boolean hasAccount(String ident) throws FileManagerServiceException {
        File candidate = makeAccountFile(ident);
        return candidate.exists();
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#addAccount(java.lang.String, org.astrogrid.filemanager.common.FileManagerStoreNode)
     */
    public void addAccount(String ident, FileManagerStoreNode root) throws DuplicateNodeException, FileManagerServiceException {
        if (root == null) {
            throw new IllegalArgumentException("Null account node");
        }
        
        File acc = makeAccountFile(ident);        
        if (acc.exists()) {
            throw new DuplicateNodeException("Account " + ident + " already exists");
        }
        Writer out = null;
        try {
            out = new FileWriter(acc);
            xs.toXML(root,out);
            logger.info("Created account for " + ident);
        } catch (IOException e) {
            throw new FileManagerServiceException("addAccount " + ident,e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("Exception closing output stream",e);
                }
            }
        }
    }
  
    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#getAccount(java.lang.String)
     */
    public FileManagerStoreNode getAccount(String ident) throws NodeNotFoundException, FileManagerServiceException {
        File acc = makeAccountFile(ident);        
        if (! acc.exists()) {
            throw new NodeNotFoundException("Account " + ident + " not found");
        }

        Reader in = null;
        try {
            in = new FileReader(acc);
            Object obj = xs.fromXML(in);
            if (! (obj instanceof FileManagerStoreNode)) {
                logger.error(obj);
                throw new FileManagerServiceException("Deserialized data not in expected format: " + obj.getClass().getName());
            }
            logger.debug(obj);
            return (FileManagerStoreNode)obj;
        } catch (IOException e) {
            throw new FileManagerServiceException("Could not read account for " + ident,e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("Could not close reader",e);
                }
            }
        }
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#delAccount(java.lang.String)
     */
    public void delAccount(String ident) throws NodeNotFoundException, FileManagerServiceException {
        File acc = makeAccountFile(ident);
        if (!acc.exists()) {
            throw new NodeNotFoundException("Account " + ident + " not found");
        }
        if (! acc.delete() && acc.exists() ) { // failed to delete? and checking, its still there..
            throw new FileManagerServiceException("Failed to delete account " + ident);
        }
        logger.info("Deleted account " + ident);
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#hasNode(java.lang.String)
     */
    public boolean hasNode(String ident) throws FileManagerServiceException {
        File node = makeNodeFile(ident);
        return node.exists();
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#addNode(org.astrogrid.store.Ivorn, org.astrogrid.store.Ivorn, java.lang.String, java.lang.String)
     */
    public FileManagerStoreNode addNode(Ivorn parent, Ivorn ivorn, String name, String type) throws DuplicateNodeException, FileManagerServiceException {        
        if (ivorn == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        if (name == null) {
            throw new IllegalArgumentException("Null node name");
        }
        if (type == null) {
            throw new IllegalArgumentException("Null node type");
        }
        FileManagerStoreNode node = new FileManagerStoreNodeMock(parent,ivorn,name,type);
        File nodeFile = makeNodeFile(ivorn.getFragment());
        /* mock doesn't check for duplicates - so we won't either - although this method is specified to throw for duplicates
         * @todo - check for correct behavuour.
         
        if (nodeFile.exists()) {
            throw new DuplicateNodeException("Account " + ident + " already exists");
        }*/
        Writer out = null;
        try {
            out = new FileWriter(nodeFile);
            xs.toXML(node,out);
            logger.info("added node for " + ivorn);
        } catch (IOException e) {
            throw new FileManagerServiceException("addNode " + ivorn,e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("Exception closing output stream",e);
                }
            }
        }        
        return node;
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#getNode(java.lang.String)
     */
    public FileManagerStoreNode getNode(String ident) throws NodeNotFoundException, FileManagerServiceException {
        File node = makeNodeFile(ident);        
        if (! node.exists()) {
            throw new NodeNotFoundException( ident + " not found");
        }

        Reader in = null;
        try {
            in = new FileReader(node);
            Object obj = xs.fromXML(in);
            if (! (obj instanceof FileManagerStoreNode)) {
                logger.error(obj);
                throw new FileManagerServiceException("Deserialized data not in expected format: " + obj.getClass().getName());
            }
            logger.debug(obj);
            return (FileManagerStoreNode)obj;
        } catch (IOException e) {
            throw new FileManagerServiceException("Could not read node for " + ident,e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warn("Could not close reader",e);
                }
            }
        }        
    }

    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#commitNode(org.astrogrid.filemanager.common.FileManagerStoreNode)
     */
    public void commitNode(FileManagerStoreNode node) throws NodeNotFoundException, FileManagerServiceException {
        if (node == null) {
            throw new IllegalArgumentException("Null node");
        }
        File n = makeNodeFile(node.getIdent());
        if (! n.exists()) {
            throw new NodeNotFoundException("Node " + node.getIdent() + " not in store");
        }
        Writer out = null;
        try {
            out = new FileWriter(n);
            xs.toXML(node,out);
        } catch (IOException e) {
            throw new FileManagerServiceException("commitNode " + node.getIdent(),e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warn("Exception closing output stream",e);
                }
            }
        }                
    }
    
    /**
     * @see org.astrogrid.filemanager.common.FileManagerStore#delNode(org.astrogrid.filemanager.common.FileManagerStoreNode)
     */
    public void delNode(FileManagerStoreNode node) throws NodeNotFoundException, FileManagerServiceException {
        if (node == null) {
            throw new IllegalArgumentException("Null node");
        }
        File n = makeNodeFile(node.getIdent());
        if (! n.exists()) {
            throw new NodeNotFoundException("Node " + node.getIdent() + " not in store");            
        }
        if (!n.delete() && n.exists()) {
            throw new FileManagerServiceException("Failed to delete " + node.getIdent());
        }
        logger.info("Deleted " + node.getIdent());
    }
    
    /** construct a file from an account namt
     * @param ident the account name
     * @return a file, rooted in the accontsBaseDir;
     */
    private File makeAccountFile(String ident) throws IllegalArgumentException {
        if (ident == null) {
            throw new IllegalArgumentException("Null account identifier");
        }
        try {
        File acc = new File(locations.getAccountDir(),URLEncoder.encode(ident,"UTF-8"));
        logger.debug(acc);
        return acc;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Really shouldn't happen - jvms must support utf-8",e);
        }
    }
    
    /** construct a file from a node name
     * @param ident the account name
     * @return a file, rooted in the nodeBaseDir;
     */
    private File makeNodeFile(String ident) throws IllegalArgumentException{
        if (ident == null) {
            throw new IllegalArgumentException("Null node identifier");
        }
        try {
        File node = new File(locations.getNodeDir(),URLEncoder.encode(ident,"UTF-8"));
        logger.debug(node);
        return node;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Really shouldn't happen - jvms must support utf-8",e);
        }
    }
        
    /** check the storage locations are valid */
    protected void checkLocations() throws ConfigException{
       
        final File nodes = locations.getNodeDir();
        final File accounts = locations.getAccountDir();
        if ((! nodes.exists()) && (!nodes.mkdirs())) {  // try making it.
            logger.error("Could not create " + nodes);
            throw new ConfigException("Could not create NodesBaseDir at " + nodes);
        }
        if (! (nodes.exists() && nodes.isDirectory() && nodes.canRead() && nodes.canWrite())) {
            logger.error("NodesBaseDir is not a directory that can be read & written to: " + nodes);
            throw new ConfigException("NodesBaseDir is not a directory that can be read & written to: " + nodes);
        }
        
        //same with users.
        if ((!accounts.exists()) && (!accounts.mkdirs())) {
            logger.error("Could not create " + accounts);
            throw new ConfigException("Could not create AccountsBaseDir at " + accounts);
        }
        if (! (accounts.exists() && accounts.isDirectory() && accounts.canRead() && accounts.canWrite())) {
            logger.error("AccountsBaseDir is not a directory that can be read & written to: " + accounts);
            throw new ConfigException("AccountsBaseDir is not a directory that can be read & written to: " + accounts);            
        }  
        //all ok then!
        logger.info("Storage locations seems to be ok");
    }


    
 
}
