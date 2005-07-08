/*$Id: VospaceImpl.java,v 1.7 2005/07/08 14:06:30 nw Exp $
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

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.io.Piper;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.RegistrySearchException;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;
import org.astrogrid.ui.script.ScriptEnvironment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** implementation of the vospace componet.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 */
public class VospaceImpl implements UserLoginListener, Myspace {
    

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(VospaceImpl.class);

    /** Construct a new Vospace
     * 
     */
    public VospaceImpl(Community community) {
        super();
        this.community = community;
    }
    protected final Community community;
    protected FileManagerClient client;
    
    protected FileManagerClient getClient() throws CommunityException, RegistryException, URISyntaxException {
        if (client == null) {
            ScriptEnvironment env = community.getEnv();
          //  client = community.getEnv().getAstrogrid().createFileManagerClient(env.getUserIvorn(),env.getPassword());
            // want to create my own file manager client, so can set up customprefs.
            BundlePreferences prefs = new BundlePreferences();
            prefs.setFetchParents(true);
            prefs.setMaxExtraNodes(new Integer(200));
            prefs.setPrefetchDepth(new Integer(3));
            FileManagerClientFactory fac = new FileManagerClientFactory(prefs);
            client = fac.login(env.getUserIvorn(),env.getPassword());
        } 
        return client;
    }
    
    public FileManagerNode createFile(Ivorn ivorn) throws FileManagerFault, DuplicateNodeFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return getClient().createFile(ivorn);
    }
    
    public FileManagerNode createFolder(Ivorn ivorn) throws FileManagerFault, DuplicateNodeFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return getClient().createFolder(ivorn);
    }
    


    // access
    public NodeIvorn home() throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return getClient().home().getMetadata().getNodeIvorn();
    }
    
    public FileManagerNode node(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RegistryException, CommunityException, RemoteException, URISyntaxException {
        return getClient().node(ivorn);
    }
    
    public boolean exists(Ivorn ivorn) throws FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException {       
        return getClient().exists(ivorn) != null;
    }
    
    public NodeTypes getType(Ivorn ivorn) throws FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return getClient().exists(ivorn);
    }
    
    //creation  
    public NodeIvorn  newFolder(Ivorn parentIvorn, String name) throws IOException, FileManagerFault, NodeNotFoundFault, DuplicateNodeFault, RemoteException, UnsupportedOperationException, RegistryException, CommunityException, URISyntaxException {
        return node(parentIvorn).addFolder(name).getMetadata().getNodeIvorn();
        
    }
        
    public NodeIvorn newFile(Ivorn parentIvorn, String name) throws FileManagerFault, NodeNotFoundFault, DuplicateNodeFault, RemoteException, UnsupportedOperationException, RegistryException, CommunityException, URISyntaxException{
        return node(parentIvorn).addFile(name).getMetadata().getNodeIvorn();
    }
    // content
    public URL readContent(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, UnsupportedOperationException, MalformedURLException, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return node(ivorn).contentURL();
    }
    
    /** @throws URISyntaxException
     * @throws CommunityException
     * @throws RegistryException
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws UnsupportedOperationException
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @todo unsure whether this is correct */
    public URL writeContent(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, UnsupportedOperationException, MalformedURLException, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return node(ivorn).contentURL();        
    }
    

    public void copyContentToURL(Ivorn ivorn,URL destination) throws NodeNotFoundFault, FileManagerFault, UnsupportedOperationException, RemoteException, IOException, RegistryException, CommunityException, URISyntaxException {
        if (destination.getProtocol().equals("file")) {
            OutputStream os = new FileOutputStream(new File(new URI(destination.toString())));
            InputStream is =node(ivorn).readContent();
            Piper.pipe(is,os);
            os.close();
            is.close();
        } else {            
            node(ivorn).copyContentToURL(destination);
        }
    }
    
    public void copyURLToContent(URL src,Ivorn ivorn) throws UnsupportedOperationException, RegistryException, CommunityException, URISyntaxException, IOException {
        logger.info("copyURLToContent(src = " + src + ", ivorn = " + ivorn + ") - start");
        
        
        if (src.getProtocol().equals("file")) {
            InputStream is = src.openStream();
            OutputStream os = node(ivorn).writeContent();
            Piper.pipe(is,os);
            os.close();
            is.close();
        } else {
        node(ivorn).copyURLToContent(src);
        }

        logger.info("copyURLToContent() - end");       
    }
    
    //navigation
    
    public NodeIvorn getParent(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return node(ivorn).getParentNode().getMetadata().getNodeIvorn();
    }
    
    public FileManagerNode getParentNode(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        return node(ivorn).getParentNode();
    }
    
    public NodeIvorn[] getChildren(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        FileManagerNode node =  node(ivorn);
        NodeIvorn[] result = new NodeIvorn[node.getChildCount()];
        NodeIterator  i = node.iterator();
        for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
            FileManagerNode child = i.nextNode();
            result[ix] = child.getMetadata().getNodeIvorn();
        }
        return result;
    }
    
    public FileManagerNode[] getChildrenNodes(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        FileManagerNode node =  node(ivorn);
        FileManagerNode[] result = new FileManagerNode[node.getChildCount()];
        NodeIterator  i = node.iterator();
        for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
            FileManagerNode child = i.nextNode();
            result[ix] = child;
        }
        return result;        
    }
    
    
    // metadata
    
    public void refresh(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        node(ivorn).refresh();
    }
    
    // management
    public void delete(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException  {
        node(ivorn).delete();
    }
    
    
    public void rename(Ivorn srcIvorn,String newName) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        FileManagerNode node = node(srcIvorn);        
        node.move(newName,node.getParentNode(),null);
    }
    
    public void move(Ivorn srcIvorn,Ivorn newParentIvorn, String newName) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException  {
        FileManagerNode node = node(srcIvorn);
        FileManagerNode parent = node(newParentIvorn);
        node.move(newName,parent,null);
  
    }
    
    public void changeStore(Ivorn srcIvorn,Ivorn storeIvorn) throws DuplicateNodeFault, NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        node(srcIvorn).move(null,null,storeIvorn);
    }
    
    public void copy(Ivorn srcIvorn,Ivorn newParentIvorn,String newName) throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException, CommunityException, URISyntaxException {
        FileManagerNode node = node(srcIvorn);
        FileManagerNode parent = node(newParentIvorn);
        node.move(newName,parent,null);        
    }


  public ResourceData[] listAvailableStores() throws RegistryException {
      //@todo edit to only select active stores - get kevin to screw his brain on.
      return community.getEnv().getAstrogrid().createRegistryClient().
      
      getResourceDataByRelationship("ivo://org.astrogrid/FileStoreKind");
               /*
               "select * from Registry where @status='active' "
               +" and vr:content/vr:relationship/vr:relationshipType = 'derived-from' "               
               + " and vr:content/vr:relationship/vr:relatedResource/@ivo-id = 'FileStore' "
               ); */           
  }
    
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }



    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        client = null;
    }
    


    
    
    

    

    

}


/* 
$Log: VospaceImpl.java,v $
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