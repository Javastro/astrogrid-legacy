/*$Id: VospaceUtils.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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

import org.astrogrid.store.Ivorn;
import org.astrogrid.ui.script.ScriptEnvironment;

import java.net.URISyntaxException;
import java.util.StringTokenizer;

/** static helper methods for expanding out abridged myspace references.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 *
 */
public class VospaceUtils {

    /** Construct a new VospaceUtils
     * 
     */
    public VospaceUtils(ScriptEnvironment env) {
        this.env = env;
    }
    protected final ScriptEnvironment env;
    
    /** parse an ivorn (in full or abridged form) into an Ivorn object, and check it belongs to the current user */
    public Ivorn mkFull(String s) throws URISyntaxException {
       Ivorn result =  _mkFull(s);
       if (!result.getPath().equals(env.getHomeIvorn().getPath())) {
           throw new IllegalArgumentException("Cannot access another user's myspace");
       }      
       return result;
    }
    
    private Ivorn _mkFull(String s) throws URISyntaxException { // make this the full form of myspace reference.
       String u = null;
       if (s.startsWith("ivo://")) {
            return env.getAstrogrid().getObjectBuilder().newIvorn(s);
       }      
       StringTokenizer tok = new StringTokenizer(s,"/");
       if (s.startsWith("#/")) {
           tok.nextElement();// skip junk
            u = tok.nextToken();
            return env.getAstrogrid().getObjectBuilder().newIvorn(env.getAccount().getCommunity(),u,s.substring(1));
       } else  if (s.startsWith( "#")) { // error tolerant, of not qute correct syntax
            u = tok.nextToken().substring(1);
            return env.getAstrogrid().getObjectBuilder().newIvorn(env.getAccount().getCommunity(),u,"/" + s.substring(1));
       } else { // assume to be something else, and pass thru
         throw new IllegalArgumentException("Didn't recognize " + s);
       }
    }

}


/* 
$Log: VospaceUtils.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/