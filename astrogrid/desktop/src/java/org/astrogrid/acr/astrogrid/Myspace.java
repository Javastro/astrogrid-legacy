/*$Id: Myspace.java,v 1.4 2005/07/08 11:08:02 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI.MalformedURIException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public interface Myspace {
    // access
    NodeIvorn home() throws FileManagerFault, NodeNotFoundFault, RemoteException, RegistryException,
            CommunityException, URISyntaxException;

    public FileManagerNode createFile(Ivorn ivorn) throws FileManagerFault, DuplicateNodeFault, RemoteException, RegistryException, CommunityException, URISyntaxException ;

    public FileManagerNode createFolder(Ivorn ivorn) throws FileManagerFault, DuplicateNodeFault, RemoteException, RegistryException, CommunityException, URISyntaxException ;
       
    FileManagerNode node(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault,
            RegistryException, CommunityException, RemoteException, URISyntaxException;

    boolean exists(Ivorn ivorn) throws FileManagerFault, RemoteException, RegistryException,
            CommunityException, URISyntaxException;

    NodeTypes getType(Ivorn ivorn) throws FileManagerFault, RemoteException, RegistryException,
            CommunityException, URISyntaxException;

    //creation
    NodeIvorn newFolder(Ivorn parentIvorn, String name) throws IOException, FileManagerFault,
            NodeNotFoundFault, DuplicateNodeFault, RemoteException, UnsupportedOperationException,
            RegistryException, CommunityException, URISyntaxException;

    NodeIvorn newFile(Ivorn parentIvorn, String name) throws FileManagerFault, NodeNotFoundFault,
            DuplicateNodeFault, RemoteException, UnsupportedOperationException, RegistryException,
            CommunityException, URISyntaxException;

    // content
    URL readContent(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault,
            UnsupportedOperationException, MalformedURLException, RemoteException,
            RegistryException, CommunityException, URISyntaxException;

    /** @throws URISyntaxException
     * @throws CommunityException
     * @throws RegistryException
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws UnsupportedOperationException
     * @throws FileManagerFault
     * @throws NodeNotFoundFault
     * @todo unsure whether this is correct */
    URL writeContent(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault,
            UnsupportedOperationException, MalformedURLException, RemoteException,
            RegistryException, CommunityException, URISyntaxException;

    void copyContentToURL(Ivorn ivorn, URL destination) throws NodeNotFoundFault, FileManagerFault,
            UnsupportedOperationException, RemoteException, MalformedURIException,
            RegistryException, CommunityException, URISyntaxException, FileNotFoundException, IOException;

    void copyURLToContent(URL src, Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault,
            UnsupportedOperationException, RemoteException, MalformedURIException,
            RegistryException, CommunityException, URISyntaxException, IOException;

    //navigation
    NodeIvorn getParent(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException,
            RegistryException, CommunityException, URISyntaxException;
    
    FileManagerNode getParentNode(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException, CommunityException, URISyntaxException ;
        
    NodeIvorn[] getChildren(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault, RemoteException,
            RegistryException, CommunityException, URISyntaxException;

    FileManagerNode[] getChildrenNodes(Ivorn ivorn) throws FileManagerFault, NodeNotFoundFault,
            RemoteException, RegistryException, CommunityException, URISyntaxException;

    // metadata
    void refresh(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException,
            RegistryException, CommunityException, URISyntaxException;

    // management
    void delete(Ivorn ivorn) throws NodeNotFoundFault, FileManagerFault, RemoteException,
            RegistryException, CommunityException, URISyntaxException;

    void rename(Ivorn srcIvorn, String newName) throws FileManagerFault, NodeNotFoundFault,
            RemoteException, RegistryException, CommunityException, URISyntaxException;

    void move(Ivorn srcIvorn, Ivorn newParentIvorn, String newName) throws FileManagerFault,
            NodeNotFoundFault, RemoteException, RegistryException, CommunityException,
            URISyntaxException;

    void changeStore(Ivorn srcIvorn, Ivorn storeIvorn) throws DuplicateNodeFault,
            NodeNotFoundFault, FileManagerFault, RemoteException, RegistryException,
            CommunityException, URISyntaxException;

    void copy(Ivorn srcIvorn, Ivorn newParentIvorn, String newName) throws FileManagerFault,
            NodeNotFoundFault, RemoteException, RegistryException, CommunityException,
            URISyntaxException;

    /**
     * @return
     * @throws RegistryException
     * @throws ParserConfigurationException
     */
    ResourceData[] listAvailableStores() throws RegistryException;

}

/* 
 $Log: Myspace.java,v $
 Revision 1.4  2005/07/08 11:08:02  nw
 bug fixes and polishing for the workshop

 Revision 1.3  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.1.2.1  2005/05/09 14:51:02  nw
 renamed to 'myspace' and 'workbench'
 added confirmation on app exit.

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2.2.1  2005/04/22 10:54:36  nw
 added missing methods to vospace.
 made a start at getting applications working again.

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */