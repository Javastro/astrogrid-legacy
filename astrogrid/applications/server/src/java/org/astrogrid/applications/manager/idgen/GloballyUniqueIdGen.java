/*$Id: GloballyUniqueIdGen.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.idgen;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.net.InetAddress;
import java.util.Random;

import junit.framework.Test;

/** Implementation of idgen that should produce globally-unique urn-style identifiers.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class GloballyUniqueIdGen implements IdGen, ComponentDescriptor {
    /** Construct a new GloballyUniqueIdGen
     * 
     */
    public GloballyUniqueIdGen() {
        super();
    }

    
    /** stuff for generating a unique job urn */
    protected static String hostname;
    static {
        hostname = null;
        try {
            hostname = InetAddress.getLocalHost().toString();
        } catch (Exception e) {
            hostname="unavailable";
        }
    }
    private static Random rand = new Random();

    /**
     * @see org.astrogrid.applications.manager.idgen.IdGen#getNewID()
     */
    public synchronized String getNewID() {

        StringBuffer
            buffer = new StringBuffer(128);
        //Account acc = job.getCredentials().getAccount();
        buffer
            .append("cea:")
            .append(hostname) // should ensure its globally unique
            /* if I passed in user / account info , could add all this..
            .append('/')         
           .append( acc.getName() )
           .append( '@' )
           .append( acc.getCommunity() )
           */
           .append( '/' )          // should ensure its system-unique - we'd need to have 2 simultaneous (in same milli) concurrent requests, that both generate the same random number. 
           .append( System.currentTimeMillis()) 
           .append( ':' )
           .append(Math.abs(rand.nextInt()));
        return buffer.toString();
        
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Globally-Unique ID generator";
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "generates identifiers such as " + getNewID();
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }    
}


/* 
$Log: GloballyUniqueIdGen.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/