/*$Id: Vospace.java,v 1.2 2005/02/22 01:10:31 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.corba.se.connection.GetEndPointInfoAgainException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.UnrecognizedProtocolException;
import org.astrogrid.community.User;
import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.tree.Container;
import org.astrogrid.store.tree.Node;
import org.astrogrid.store.tree.TreeClient;
import org.astrogrid.store.tree.TreeClientLoginException;
import org.astrogrid.store.tree.TreeClientSecurityException;
import org.astrogrid.store.tree.TreeClientServiceException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 *@@ServiceDoc("vospace","browse and manipulate files in vospace.\n Whenever an ivorn is required, it may be given in full or abridged syntax")
 */
public class Vospace implements Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Vospace.class);

    /** Construct a new Vospace
     * 
     */
    public Vospace(Community community) {
        super();
        this.community = community;
        community.addObserver(this);
    }
    protected final Community community;
    protected Container root;
    protected VoSpaceClient client;
    protected VospaceUtils utils;
    
    protected synchronized VoSpaceClient getVospaceClient() {
        if (client == null) {
        User u = community.getEnv().getUser();
        client =  community.getEnv().getAstrogrid().createVoSpaceClient(u);
        utils = new VospaceUtils(community.getEnv());
        }
        return client;
    }
    
    protected ExternalValue getExternalValue(URL u) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        return community.getEnv().getAstrogrid().getIoHelper().getExternalValue(u);
    }
    
    protected synchronized Container getRoot() throws TreeClientSecurityException, TreeClientServiceException, TreeClientLoginException {
        if (root == null) {
            Ivorn user = community.getEnv().getUserIvorn();
            String pass = community.getEnv().getPassword();
            root =  community.getEnv().getAstrogrid().createTreeClient(user,pass).getRoot();          
        }
        return root;
    }
/**@@MethodDoc("delete","delete a vospace resource")
     * @@.ivorn("ivorn","location of resource to delete");
 *      * @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
 * @param ivorn
 * @return
 * @throws URISyntaxException
 * @throws IOException
 */
    public boolean delete(String ivorn) throws URISyntaxException, IOException {
       VoSpaceClient client = getVospaceClient();
       Ivorn ivo = utils.mkFull(ivorn);
       client.delete(ivo);
       return true;       
    }
    
    /**@@MethodDoc("move","move a vospace resource")
     * @@.ivorn("src","location to move from");     
     * @@.ivorn("dest","location to move to");
     *      * @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
     * @param src
     * @param dest
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public boolean move(String src,String dest) throws URISyntaxException, IOException {
        VoSpaceClient client = getVospaceClient();
        Ivorn s = utils.mkFull(src);
        Ivorn d = utils.mkFull(dest);
        client.move(s,d);
        return true;        
    }
    
    /** @@MethodDoc("copy","copy between vospace, http, ftp and file resources")
     * @@.src ParamDoc("src","source to copy from - full or abridged vospace reference, http / ftp / file url, local filepath")
     * @@.dest ParamDoc("dest","source to copy to - full or abridged vospace reference, http / ftp / file url, local filepath")

     *      * @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance()) 
     * 
     * @param src
     * @param dest
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws UnrecognizedProtocolException
     * @throws InaccessibleExternalValueException
     */
    public boolean copy(String src,String dest) throws IOException, InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        VoSpaceClient client = getVospaceClient();
        Object s = expandReference(src);
        Object d = expandReference(dest);
        if (s instanceof Ivorn && d instanceof Ivorn) { // remoe move.
            client.copy((Ivorn)s,(Ivorn)d);
            return true;
        } 
        if (s instanceof URL) {// special optimization..
            URL srcURL = (URL)s;        
            if (d  instanceof Ivorn 
                    && (srcURL.getProtocol().equals("http") || srcURL.getProtocol().equals("ftp"))){
                client.putUrl(srcURL,(Ivorn)d,true);
            }
        }
        // oh well.. have to do it ourselves then..
        // @todo clean up streams properly.
        InputStream is = getExternalValue((URL)s).read();
        OutputStream os = getExternalValue((URL)d).write();
        Piper.bufferedPipe(is,os);
        return true;
    }     
    
    private Object expandReference(String s) throws MalformedURLException, URISyntaxException {
        if (s.startsWith("ivo://") || s.startsWith("#")) {
            return utils.mkFull(s);
        }
        if (s.startsWith("http://") || s.startsWith("file:") || s.startsWith("ftp://")) {
            return new URL(s);
        }
        // assume it to be a file reference.
        return (new File(s)).toURL();
    }
    
    /**
     * @@MethodDoc("newFolder","create a new vospace folder")
     * @@.ivorn ParamDoc("ivorn","location of the new folder");
     * @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance()) 
     * @param ivorn
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public boolean newFolder(String ivorn) throws URISyntaxException, IOException {
        VoSpaceClient client = getVospaceClient();
        Ivorn s = utils.mkFull(ivorn);
        client.newFolder(s);
        return true;                
    }
    
   
    
    /**
     * @@MethodDoc("listRoot","list contents of user's vospace root")
     * @@.return ReturnDoc("Root container object")
     * @return
     * @throws TreeClientSecurityException
     * @throws TreeClientServiceException
     * @throws TreeClientLoginException
     */
    public Container listRoot() throws TreeClientSecurityException, TreeClientServiceException, TreeClientLoginException {
        return getRoot();
    }
    /**
     * 
    * @@MethodDoc("list","list metadata of a vospace location")
    * @@.ivorn ParamDoc("ivorn","Location to list")
    * @@.return ReturnDoc("Requested vospace file object.")
    * */    
    public Node list(String ivorn) throws TreeClientSecurityException, TreeClientServiceException, TreeClientLoginException, URISyntaxException {
        Node location = getRoot();
        Ivorn i = utils.mkFull(ivorn);
        StringTokenizer path = new StringTokenizer(i.getFragment(),"/");
        path.nextToken() ; // discard root
        while (path.hasMoreTokens()) {
            String name = path.nextToken();
            if (! location.isContainer()) {
                throw new RuntimeException("Not a container");
            }            
            for (Iterator children = ((Container)location).getChildNodes().iterator(); children.hasNext(); ) {// inefficient, but don't care for now..
                Node c = (Node)children.next();
                if (c.getName().equals(name)) {
                    location = c;
                }
            }                        
        }
        return location;                        
    }

        
    /** @@MethodDoc("refresh","refresh cached vospace tree")
     * @@.return ReturnDoc("Success code",rts=BooleanResultTransformerSet.getInstance())
     * @return
     */
    public boolean refresh() {
        root = null;
        getVospaceClient();
        return true;
    }
    
    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        if (community.isLoggedIn()) {
            getVospaceClient(); // initializes objects.
        } /*else {
            root = null;
            client = null;
            utils = null;
        }*/
    }
    

    

}


/* 
$Log: Vospace.java,v $
Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/