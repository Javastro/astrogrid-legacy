/*$Id: IvornProtocol.java,v 1.1 2004/07/26 12:07:38 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Test;

/** protocol implementation for ivo:/
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class IvornProtocol implements Protocol , ComponentDescriptor{
    /** Construct a new IvornProtocol
     * 
     */
    public IvornProtocol() {
        super();
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return Ivorn.SCHEME;
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#createIndirectValue(java.net.URI)
     * @todo find nice way to pass correct user value in here.
     */
    public ExternalValue createIndirectValue(final URI reference) throws InaccessibleExternalValueException {
        final Ivorn ivorn;
        try {
            ivorn = new Ivorn(reference.toString());
        }
        catch (URISyntaxException e) {
            throw new InaccessibleExternalValueException(reference.toString(),e);
        }      
        return new ExternalValue() {
            
           VoSpaceClient client = new VoSpaceClient(new User());
           
            public InputStream read() throws InaccessibleExternalValueException {
                try {
                return client.getStream(ivorn);
                } catch (IOException e) {
                    throw new InaccessibleExternalValueException(ivorn.toString(),e);
                }
            }

            public OutputStream write() throws InaccessibleExternalValueException {
                try {
                return client.putStream(ivorn);
                } catch (IOException e) {
                    throw new InaccessibleExternalValueException(ivorn.toString(),e);
                }                
            }
        };
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "IvornProtocol";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "protocol adapter for ivo: urns";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: IvornProtocol.java,v $
Revision 1.1  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/