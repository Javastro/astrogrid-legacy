/*$Id: AgslProtocol.java,v 1.1 2004/07/26 12:07:38 nw Exp $
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
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;

import junit.framework.Test;

/** Protocol implemrantion for Astrogrid Store Locators
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 * @todo this is only a first stab - dunno what I'm meant to be doing here. so really unlikely this protocol will work.
 */
public class AgslProtocol implements Protocol, ComponentDescriptor {
    /** Construct a new AgslProtocol
     * 
     */
    public AgslProtocol() {
        super();
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return Agsl.SCHEME;
    }
    /**
      * @see org.astrogrid.applications.parameter.protocol.Protocol#createIndirectValue(java.net.URI)
      * @todo find nice way to pass correct user value in here.
      */
     public ExternalValue createIndirectValue(final URI reference) throws InaccessibleExternalValueException{
         final Agsl agsl;

             try {
                agsl = new Agsl(reference.toString());
            }
            catch (MalformedURLException e1) {
                throw new InaccessibleExternalValueException(reference.toString(),e1);
            }

            final StoreClient client;
            try {
                client = StoreDelegateFactory.createDelegate(new User(), agsl);
            }
            catch (IOException e2) {

                throw new InaccessibleExternalValueException(reference.toString(),e2);
            }
         return new ExternalValue() {
            
           
             public InputStream read() throws InaccessibleExternalValueException {
                 try {
                 return client.getStream(agsl.toString());
                 } catch (IOException e) {
                     throw new InaccessibleExternalValueException(agsl.toString(),e);
                 }
             }

             public OutputStream write() throws InaccessibleExternalValueException {
                 try {
                 return client.putStream(agsl.toString(),true); //???
                 } catch (IOException e) {
                     throw new InaccessibleExternalValueException(agsl.toString(),e);
                 }                
             }
         };
     }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "AglsProtocol";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Protocol adapter for AGSLs";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: AgslProtocol.java,v $
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