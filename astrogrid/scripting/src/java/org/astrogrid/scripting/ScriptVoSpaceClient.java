/*$Id: ScriptVoSpaceClient.java,v 1.3 2004/12/07 16:50:33 jdt Exp $
 * Created on 30-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.community.User;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

/** Wrapper of standard vospace client that has string-friendly scripting methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Nov-2004
 *
 */
public class ScriptVoSpaceClient extends VoSpaceClient {

    /** Construct a new ScriptVoSpaceClient
     * @param arg0
     */
    protected ScriptVoSpaceClient(User operator) {
        super(operator);
    }
    

    public void copy(String fromIvorn, String toIvorn) throws IOException, URISyntaxException {
        super.copy(new Ivorn(fromIvorn), new Ivorn(toIvorn));
    }
    
    public Ivorn createUser(String arg0, String arg1) throws IOException,
            URISyntaxException {
        return super.createUser(new Ivorn(arg0), new Ivorn(arg1));
    }
    public void delete(String arg0) throws IOException, URISyntaxException {
        super.delete(new Ivorn(arg0));
    }
    public void deleteUser(String arg0, String arg1) throws IOException, URISyntaxException {
        super.deleteUser(new Ivorn(arg0), new Ivorn(arg1));
    }
    public StoreClient getDelegate(String arg0) throws IOException, URISyntaxException {
        return super.getDelegate(new Ivorn(arg0));
    }
    public InputStream getStream(String arg0) throws IOException, URISyntaxException {
        return super.getStream(new Ivorn(arg0));
    }
    public void move(String arg0, String arg1) throws IOException, URISyntaxException {
        super.move(new Ivorn(arg0), new Ivorn(arg1));
    }
    public void newFolder(String arg0) throws IOException, URISyntaxException {
        super.newFolder(new Ivorn(arg0));
    }
    public void putBytes(byte[] arg0, int arg1, int arg2, String arg3,
            boolean arg4) throws IOException, URISyntaxException {
        super.putBytes(arg0, arg1, arg2, new Ivorn(arg3), arg4);
    }
    public OutputStream putStream(String arg0) throws IOException, URISyntaxException {
        return super.putStream(new Ivorn(arg0));
    }
    public void putUrl(URL arg0, String arg1, boolean arg2) throws IOException, URISyntaxException {
        super.putUrl(arg0, new Ivorn(arg1), arg2);
    }
    
    public void putUrl(URL arg0, String arg1) throws IOException, URISyntaxException {
        super.putUrl(arg0, new Ivorn(arg1), false);
    }    
}


/* 
$Log: ScriptVoSpaceClient.java,v $
Revision 1.3  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.2.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.2  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.1.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.
 
*/