/*$Id: IterationSixAladinAdapter.java,v 1.2 2004/11/11 17:50:42 clq2 Exp $
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

import org.astrogrid.community.User;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.common.security.service.SecurityService;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.VoSpaceResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/** Implementation of the aladin adapter interfaces against the Iteration6 VoSpace client.
 * 
 * bit fiddly in places - as the underlying storeClient and StoreFile are broken, fragile and inconsistent. Which means that we need to do a lot of fiddling with path expressions
 * to get it to do what we want.
 * 
 * The call to {@link #getRoot()} gets the entire file tree for the user's myspace. We then cache this, and don't requery he server again - opoerations to create files, etc, are mirrored in our file tree.
 * the view can be synchronized from the server by calling getRoot again.
 *  
 * 
 * NB: noticed that there's no way to delete a file / folder - odd.
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

    }
    
    protected SecurityToken token;
    protected final CommunityPasswordResolver security = new CommunityPasswordResolver();
    protected final CommunityAccountSpaceResolver spaceResolver = new CommunityAccountSpaceResolver();
    protected StoreClient client;
    protected String username;

    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#login(org.astrogrid.store.Ivorn, java.lang.String)
     */
    public void login(Ivorn communityIvorn, String password)
            throws AladinAdapterLoginException, AladinAdapterServiceException {
        if (communityIvorn == null) {
            throw new IllegalArgumentException("Null account");
        }
        if (password == null) {
            throw new IllegalArgumentException("Null password");
        }
        logger.info("Logging in " + communityIvorn );
        try {           
            logger.info("Checking password");            
            this.token = security.checkPassword(communityIvorn.toString(),password);
            logger.debug(token);
            CommunityIvornParser ivornParser = new CommunityIvornParser(this.token.getAccount());

            logger.info("user object");
            username =  ivornParser.getAccountName() ;
            User u = new User();            
            u.setAccount(username+ "@" + ivornParser.getCommunityName());
            u.setToken(this.token.getToken());            
            logger.debug(u);
            
            logger.info("Creating vospace client");            
            VoSpaceClient voSpace  = new VoSpaceClient(u);
            
            logger.info("Creating Store Client");    
            Ivorn vospaceIvorn = spaceResolver.resolve(communityIvorn);
            logger.debug(vospaceIvorn);              
            //@todo temporary work-around for bug. - if calculated ivorn ends with null, remove 'null'
            if (vospaceIvorn.toString().endsWith("null")) {
                String s = vospaceIvorn.toString();
                vospaceIvorn = new Ivorn(s.substring(0,s.lastIndexOf("null")));
                logger.warn("mangled vospaceIvorn to " + vospaceIvorn);
            }
            client = voSpace.getDelegate(vospaceIvorn); 
            logger.debug(client);
            
        } catch (CommunitySecurityException e) {
            logger.info("login failed",e);
            throw new AladinAdapterLoginException("Login failed",e);
        } catch (CommunityServiceException e ) { //NB security client seems to throw one of these.
                logger.info("login failed",e);
                throw new AladinAdapterLoginException("Login failed",e);
        } catch (Throwable e) {
            logger.error("Failed to perform request",e);
            throw new AladinAdapterServiceException("Failed to perform request",e);
        }
    }

    public StoreClient getStoreClient() {
        return client;
    }
    
    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#logout()
     * @todo need to call any kind of logout method?
     */
    public void logout() throws AladinAdapterServiceException {
        logger.info("logging out");
        this.token = null; 
        this.client = null;
        
    }    

    /**
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#getToken()
     */
    public SecurityToken getToken() {
        return this.token;
    }

    /** queries server, rebuilds tree.
     * @see org.astrogrid.store.adapter.aladin.AladinAdapter#getRoot()
     */
    public AladinAdapterContainer getRoot()
            throws AladinAdapterSecurityException,
            AladinAdapterServiceException {
        if (this.token == null) {
            throw new AladinAdapterSecurityException("Not logged in");
        }
        try {
            String rootPath = "/" + username + "*"; //NB - note no '/' between username and *, as this fails.
            logger.info("Getting root via query " + rootPath);            
            StoreFile container = client.getFiles(rootPath);
            assert container != null: "Null container returned";
            StoreFile[] children = container.listFiles();
            assert children.length == 1 : "Found multiple roots";
            StoreFile root = children[0];
            assert root != null : "Root is null";
            return new IterationSixContainer(root);            
        } catch (IOException e) {
            logger.fatal("could not get root",e);
              throw new AladinAdapterServiceException("Could not get root",e);
        }
    }

    
    /** helper function for string mangling */
    protected String dropTrailingSlash(String s) {
        if (s.endsWith("/")) {
            return s.substring(0,s.length()-1);
        } else {
            return s;
        }
    }

    /**
     * Iteration-6 version of an aladin adapter node.
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
     *
     */
    protected class IterationSixNode implements AladinAdapterNode {
        
        public IterationSixNode(StoreFile wrapped, String path) {
            this.wrapped = wrapped;
            this.path = path;
            logger.debug("Creating iteration six node for:" + path);            
        }
        
        /** compute path from wrapped StoreFile, mangling the result */
        public IterationSixNode(StoreFile wrapped) {
            this(wrapped,"/" + dropTrailingSlash(wrapped.getPath()).trim());
        }
        protected final StoreFile wrapped;
        protected final String path;
        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#getName()
         */
        public String getName() {
            return wrapped.getName().trim();
        }

        
        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#isFile()
         */
        public boolean isFile() {
            return wrapped.isFile();
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterNode#isContainer()
         */
        public boolean isContainer() {
            return wrapped.isFolder();
        }
        
        protected String getPath() {
            return path;
        }
        
        public boolean equals(Object obj) {
            IterationSixNode casted = (IterationSixNode)obj;            
            return this.getPath().equals(casted.getPath());
        }
        public String toString() {
            return "AladinAdapter (IterationSixNode) for " + (isFile() ? "file " : "folder ") + getPath();
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
        public IterationSixContainer(StoreFile wrapped) {
            super(wrapped);
            buildChildren();
        }

        private Map childMap;
        /** populate the children of the map - will recurse */
        private final void buildChildren() {
            StoreFile[] children = wrapped.listFiles();
            childMap = new HashMap(children.length);
            for (int i = 0; i < children.length; i++) {
                StoreFile f = children[i];
                logger.debug("found child:" + f.getName());
                IterationSixNode n = f.isFile() ?  (IterationSixNode)new IterationSixFile(f) : (IterationSixNode)new IterationSixContainer(f);
                childMap.put(n.getName(),n);
                }                
            }            
        
        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#getChildNodes()
         */
        public Collection getChildNodes()  {
            return Collections.unmodifiableCollection(childMap.values());
        }

        /** 
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#addContainer(java.lang.String)
         */
        public AladinAdapterContainer addContainer(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            if (name == null || name.trim().length() == 0) {
                throw new IllegalArgumentException("Cannot create unnamed container");
            }
            String targetPath = getPath() + "/" + name.trim();
            logger.info("Will add container " + targetPath);
            try {
                if (client.getFile(targetPath) != null){ //although this returns no useful info, it does tell us if the file exists.
                    logger.info("duplicate of " + targetPath + " found");
                    throw new AladinAdapterDuplicateException(targetPath);
                }
                client.newFolder(targetPath);
                IterationSixNode newFolder = findNewNode(targetPath);
                childMap.put(newFolder.getName(),newFolder);
                return (AladinAdapterContainer)newFolder;
            } catch (IOException e) {
                logger.error("addContainer(" + targetPath+")",e);
                throw new AladinAdapterServiceException("addContainer(" + targetPath+")",e);
            }
        }


        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterContainer#addFile(java.lang.String)
         */
        public AladinAdapterFile addFile(String name) throws AladinAdapterServiceException, AladinAdapterDuplicateException {
            if (name == null || name.trim().length() == 0) {
                throw new IllegalArgumentException("Cannot create unnamed file");
            }
            String targetPath = getPath() + "/" + name.trim();
            logger.info("Will add file " + targetPath);
            OutputStream os = null;
            try {
            if (client.getFile(targetPath) != null) {//although this returns no useful info, it does tell us if the file exists.
                logger.info("duplicate of " + targetPath + " found");
                throw new AladinAdapterDuplicateException(targetPath);
            }
            //  there isn't a method to create an empty file. - so we just open an output stream to it, and close again.
            os = client.putStream(targetPath,false);
            IterationSixNode newFile = findNewNode(targetPath);
            childMap.put(newFile.getName(),newFile);
            return (AladinAdapterFile)newFile;
            } catch (IOException e) {
                logger.error("addFile(" + targetPath +")",e);
                throw new AladinAdapterServiceException("addFile(" + targetPath + ")",e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    }catch (IOException e) {
                        logger.warn("failed to close output stream",e);
                    }
                }
            }
                
        }
        
        /** We've just created a new node. Now want to access the store file for that node, so it can be spliced into our tree.
         * 
         * lots of problems here - see  {@link org.astrogrid.store.adapter.aladin.integration.TestOddBehaviourOfStoreClient} for detals. 
         * <br>StoreClient.getFile(targetPath) won't return any information in the file object.
         * <br>StoreClient.getFiles(targetPath) throws an exception if targeetPath contains a '/' 
         * only option is to re-query root, traverse returned tree using target path, and splice target node into existing tree. CRAP!
         * @param targetPath the path to the newly created node
         * @return the new storeFile.
         * @throws AladinAdapterServiceException
         * @todo fix StoreClient so that this can be done efficiently.
         */
        private IterationSixNode findNewNode(String targetPath) throws  AladinAdapterServiceException {
            /* throws
            StoreFile container = client.getFiles(targetPath);
            assert container != null:"null search result returned";
            StoreFile[] contents = container.listFiles();
            assert contents != null && contents.length == 1: "expected a single search result";
            return contents[0];
            */
            try {
                StringTokenizer tok = new StringTokenizer(targetPath,"/");
                tok.nextToken(); // discard root - e.g. 'frog'
                
                IterationSixNode current = (IterationSixNode)getRoot(); // start from the root
                while (tok.hasMoreTokens()) {
                    String name = tok.nextToken();
                    current = (IterationSixNode)  ((IterationSixContainer)current).childMap.get(name); 
                }
                return current;
            } catch (AladinAdapterSecurityException e) {
                // can't possibly happen, but must catch - as return type of this method's clients won't allow us to propagate it. 
                logger.fatal("findNewNode - not logged in",e);
                throw new IllegalStateException("find new Node - not logged in");
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
        public IterationSixFile(StoreFile wrapped) {
            super(wrapped);
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getMimeType()
         */
        public String getMimeType() {
            return wrapped.getMimeType();
        }

        /**
         * @throws IOException
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getOutputStream()
         */
        public OutputStream getOutputStream() throws AladinAdapterServiceException {
            try {
                return client.putStream(this.getPath(),false);
            } catch (IOException e) {
                logger.error("Could not get output stream for " + this.getPath(),e);
                throw new AladinAdapterServiceException("Could not get output stream for " + this.getPath(),e);
            }
        }

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getInputStream()
         */
        public InputStream getInputStream() throws AladinAdapterServiceException {
            try {
                return client.getStream(this.getPath());
            } catch (IOException e) {
                logger.error("Could not get input stream for " + this.getPath(),e);
                throw new AladinAdapterServiceException("Could not get input stream for " + this.getPath(),e);
            }
            
        }
    }
    

}


/* 
$Log: IterationSixAladinAdapter.java,v $
Revision 1.2  2004/11/11 17:50:42  clq2
Noel's aladin stuff

Revision 1.1.2.2  2004/11/11 13:21:14  nw
working implementation. wasn't easy.

Revision 1.1.2.1  2004/11/09 13:56:38  nw
first stab at an iteration6 implementation
 
*/