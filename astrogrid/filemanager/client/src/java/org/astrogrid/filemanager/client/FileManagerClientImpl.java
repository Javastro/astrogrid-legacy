package org.astrogrid.filemanager.client;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.common.ivorn.IvornFactory;
import org.astrogrid.filemanager.common.ivorn.IvornParser;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the FileManager client interface.
 * 
 * @modified nww - removed unused constructors - not accessible from anywhere
 *                   but the factory
 * @modified nww - preserved whole object - factory is passed into constructor,
 *                   as it contains most of what we need.
 * @modified nww - let whole object manage the resolvers,
 * @modified nww - made package private.

 */
final class FileManagerClientImpl implements FileManagerClient {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory.getLog(FileManagerClientImpl.class);

    protected FileManagerClientImpl(FileManagerClientFactory factory, SecurityToken token) throws URISyntaxException {
        if (token == null) {
            throw new IllegalArgumentException(
                    "Can't pass in null security token");
        }
        
        //N.B. token now returns something of the form PaulHarrison@uk.ac.test/community
            String regexp = "(\\w+)@([^/]+)/\\w+";
            Pattern patt = Pattern.compile(regexp);
            Matcher matcher = patt.matcher(token.getAccount());
            if(matcher.find()){
            String user = matcher.group(1);
            String comm = matcher.group(2);
            this.homeIvorn = new Ivorn(comm,user,"");
            }
            else
            {
        	throw new IllegalArgumentException("error parsing account - "+token.getAccount());
            }
            log.info("Creating FileManagerClient with token " + token);
        this.resolvers = factory;
    }

    /** construct a filemanager client that isn't logged in */
    protected FileManagerClientImpl(FileManagerClientFactory factory) {
        this.resolvers = factory;
        this.homeIvorn = null;
    }

    /** back-reference to the factory that created us - as we use it's resolvers, etc */
    private final FileManagerClientFactory resolvers;

    /**
     * Our map of FileManager delegates, indexed by service ivorn.
     *  
     */
    private Map delegates = new HashMap();

    /**
     * Resolve a FileManager Ivorn into a FileManager node.
     * 
     * @param ivorn
     *                    The target ivorn.
     * @param retry
     *                    True to allow recursive lookup via community account.
     * @return A FileManagerNode for the target ivorn.
     * @throws RegistryException
     * @throws CommunityException
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *                     If unable to find the node.
     * @throws FileManagerIdentifierException
     *                     If unable to parse the ivorn.
     * @throws FileManagerResolverException
     *                     If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *                     If a problem occurs when handling the request.
     *  
     */
    private FileManagerNode resolveNode(Ivorn ivorn, boolean retry) throws RegistryException, CommunityException, FileManagerFault, NodeNotFoundFault, RemoteException, URISyntaxException {
        log.debug("FileManagerClientImpl.resolveNode(" + ivorn + "," + retry +")");
        NodeDelegate delegate = resolveDelegate(ivorn);
        if (delegate == null && retry) { // If we can retry via community.
                    log.info("Trying CommunityHome resolver ....");
                    ivorn = resolveHome(ivorn);
                    log.info("Community resolved home to " + ivorn);
                   delegate = resolveDelegate(ivorn);
        }
        if (delegate == null) {
                    throw new RegistryException("Unable to resolve delegate for " + ivorn);
        }
        // one way or another, we've now found a delagate - Call it to resolve
        // the node.
        return delegate.getNode(new NodeIvorn(ivorn.toString()));
    }
    
    /** select the correct delegate for this ivorn */
    protected NodeDelegate resolveDelegate(Ivorn ivorn) throws URISyntaxException {
        // Parse the ivorn as a FileManager ivorn.
        IvornParser parser = new IvornParser(ivorn);
        log.debug("  Ident : " + parser.getServiceIdent());
        if (delegates.containsKey(parser.getServiceIdent())) {        // Check if we already have a delegate in our map.
            log.debug("Found cached manager delegate ....");
            return (NodeDelegate) delegates.get(parser.getServiceIdent());            
        } else {
            log.debug("Unknown ivorn, resolving ....");
            try {
                log.debug("Trying FileManager resolver ....");
                NodeDelegate delegate = resolvers.getManagerResolver().resolve( parser.getServiceIvorn());
                delegates.put(parser.getServiceIdent(), delegate);
                return delegate;
            } catch (Exception ouch) { //we failed to resolve the ivorn to a FileManager.
                log.debug("FileManager resolver failed", ouch);
                //NWW - cache the result that we've found nothing - can reuse this information next time
                delegates.put(parser.getServiceIdent(),null);
                return null;
            }            
        }          
    }

    /**
     * Resolve a Community Ivorn into a FileManager Ivorn.
     * 
     * @param ivorn
     *                    The target ivorn.
     * @return A FileManager Ivorn for the account home.
     * @throws CommunityIdentifierException
     * @throws RegistryException

     *                     If unable to parse the ivorn.
     * @throws URISyntaxException
     * @throws FileManagerResolverException
     *                     If unable to resolve the ivorn into a community account.
     *  
     */
    protected Ivorn resolveHome(Ivorn ivorn) throws CommunityException, RegistryException, URISyntaxException  {
        log.debug("FileManagerClientImpl.resolveHome(" + ivorn +")");
            CommunityIvornParser parser = new CommunityIvornParser(ivorn);
            log.debug("  Account : " + parser.getAccountIdent());

            // Resolve the home ivorn.
            Ivorn home = resolvers.getAccountResolver().resolve(
                    parser.getAccountIvorn()); 
            log.debug("Resolves home to " + home);

            String remainder = parser.getMySpacePath();
            if (remainder != null && remainder.startsWith("#")) { // another little work-around - drop leading #
                log.info("work-around - dropping leading #");
                remainder = remainder.substring(1);
            }
            if (remainder != null && remainder.endsWith("/")) { // anoter work-around - drop trailing slash
                log.info("Work-around - dropping trailing slash");
                remainder = remainder.substring(0,remainder.length()-1);
            }                         
            return IvornFactory.createIvorn(home, remainder);                                    
    }

    /**
     * The Ivorn for our account home.
     */
    private final Ivorn homeIvorn;
    
    public Ivorn getHomeIvorn() {
      return this.homeIvorn;
    }

    /**
     * The node for our account home - lazily initialized.
     *  
     */
    private FileManagerNode homeNode;

    /**
     * Access to the root node for the registered account space.
     * 
     * @return The root node of the account space.
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @throws RegistryException
     * @throws CommunityException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *                     If the node does not exist.
     * @throws FileManagerIdentifierException
     *                     If unable to parse the Ivorn.
     * @throws FileManagerResolverException
     *                     If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *                     If a problem occurs when handling the request.
     * @modified nww - inverted conditionals to make the logic easier.
     */
    public FileManagerNode home() throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException  {
        if (homeIvorn == null) {
            throw new IllegalStateException("Not logged in");
        }

        if (homeNode == null) {
            log.info("Not found home yet, resolving ....");
            homeNode = node(homeIvorn);
        }
        return homeNode;
    }

    /**
     * Access to a node in a file manager service.
     * 
     * @param ivorn
     *                    The identifier for the node.
     * @return The FileManagerNode for the Ivorn.
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @throws RemoteException
     * @throws RegistryException
     * @throws CommunityException
     * @throws URISyntaxException
     * @throws NodeNotFoundException
     *                     If the node does not exist.
     * @throws FileManagerIdentifierException
     *                     If unable to parse the Ivorn.
     * @throws FileManagerResolverException
     *                     If unable to resolve the ivorn into a manager delegate.
     * @throws FileManagerServiceException
     *                     If a problem occurs when handling the request.
     *  
     */
    public FileManagerNode node(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException  {
        return resolveNode(ivorn, true);
    }
    
    /**
     * @see org.astrogrid.filemanager.client.FileManagerClient#node(org.astrogrid.filemanager.common.NodeIvorn)
     */
    public FileManagerNode node(NodeIvorn ivorn) throws FileManagerFault, NodeNotFoundFault, RegistryException, CommunityException, RemoteException, URISyntaxException {
        // user has asserted it's a node-ivorn - so no need to try to resolve via community.
        return resolveNode(new Ivorn(ivorn.toString()),false);
    }
  

    /**
     * @see org.astrogrid.filemanager.client.FileManagerClient#exists(org.astrogrid.store.Ivorn)
     */
    public NodeTypes exists(Ivorn ivorn) throws FileManagerFault, RegistryException, CommunityException, RemoteException, URISyntaxException {
        try {
            FileManagerNode n = this.node(ivorn);
            if (n.isFolder()) {
                return NodeTypes.FOLDER;
            } else {
                return NodeTypes.FILE;
            }
        } catch (NodeNotFoundFault e) {
            return null;
        } 
    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerClient#createFolder(org.astrogrid.store.Ivorn)
     */
    public FileManagerNode createFolder(Ivorn ivorn) throws FileManagerFault, RegistryException, CommunityException, RemoteException, URISyntaxException {

        FileManagerNode parent = createParentFolder(ivorn);
        String nodeName = lastPathNameOf(ivorn);
        return parent.addFolder(nodeName);
    }

    /**
     * @see org.astrogrid.filemanager.client.FileManagerClient#createFile(org.astrogrid.store.Ivorn)
     */
    public FileManagerNode createFile(Ivorn ivorn) throws FileManagerFault, RegistryException, CommunityException, RemoteException, URISyntaxException {
        FileManagerNode parent = createParentFolder(ivorn);
        String nodeName = lastPathNameOf(ivorn);
        return parent.addFile(nodeName);       
    }

    /** search up an ivorn path, creating required parent folders for the parameter ivorn.
     * @param ivorn
     * @return
     * @throws URISyntaxException
     * @throws RegistryException
     * @throws CommunityException
     * @throws RemoteException
     * @throws FileManagerFault
     * @throws MalformedURIException
     */
    private FileManagerNode createParentFolder(Ivorn ivorn) throws CommunityException, RegistryException, URISyntaxException, FileManagerFault, RemoteException{
        // resolve to a node ivorn,   find the appropriate delegate.
        NodeDelegate delegate = resolveDelegate(ivorn);
        if (delegate == null) {
            ivorn = resolveHome(ivorn);
            delegate = resolveDelegate(ivorn);
        }
        if (delegate == null) {
            throw new RegistryException("Could not resolve delegate for " + ivorn.toString());
        }
        NodeIvorn ni = new NodeIvorn(ivorn.toString());
        List missing = new ArrayList();
        int pos;
        URI uri = ni.getValue();
        FileManagerNode ancestor = null;
        while( (pos = uri.getFragment().lastIndexOf("/")) != -1) {
            try {
                missing.add(uri.getFragment().substring(pos+1));         //    -. store unfound ancestors in a list.
                uri.setFragment(uri.getFragment().substring(0,pos));
                ancestor = delegate.getNode(ni);
                if (ancestor != null) {
                    break;
                }
            } catch (NodeNotFoundFault e) {
                // ok, that one's not there.
            } catch (MalformedURIException e) {
                throw new URISyntaxException(uri.getFragment(),e.getMessage());
            }
        }

        // check found ancestor is a folder.
        if (ancestor == null) {
            throw new NodeNotFoundFault("Can't find any nodes in this path");
        }
        if (! ancestor.isFolder()) {
            throw new IllegalArgumentException("Path contains an ancestor that is a file");
        }       
        Collections.reverse(missing);
        // create each folder in the unfound list in turn
        for (Iterator i = missing.iterator(); i.hasNext();) {
           String name = i.next().toString();
           if (i.hasNext()) { // i.e. it's not the final one.
               try {
               ancestor = ancestor.addFolder(name);
               }
               catch (DuplicateNodeFault e)//IMPL not nice to program by exceptions  - inefficient
               {
        	   ancestor = ancestor.getChild(name);
               }
           }
        }
        return ancestor;
    }

    /**
     * @param ivorn
     * @return
     */
    private String lastPathNameOf(Ivorn ivorn) {
       int lastIndex = ivorn.getFragment().lastIndexOf('/');
       return ivorn.getFragment().substring(lastIndex+1);
    }

    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerClientImpl:");
        buffer.append("\n resolvers: ");
        buffer.append(resolvers);
        buffer.append("\n delegates: ");
        buffer.append(delegates);
        buffer.append("\n homeIvorn: ");
        buffer.append(homeIvorn);
        buffer.append("\n homeNode: ");
        buffer.append(homeNode);
        buffer.append("\n]");
        return buffer.toString();
    }

}

