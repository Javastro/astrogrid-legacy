/*$Id: HttpProtocol.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.indirect;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import junit.framework.Test;

/** Protocol implementation for http:/
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class HttpProtocol implements Protocol, ComponentDescriptor {
    /** Construct a new HttpProtocol
     * 
     */
    public HttpProtocol() {
        super();
    }
    /**
     * @see org.astrogrid.applications.parameter.indirect.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return "http";
    }
    /**
     * @see org.astrogrid.applications.parameter.indirect.Protocol#createIndirectValue(java.net.URI)
     */
    public IndirectParameterValue createIndirectValue(final URI reference) throws InaccessibleIndirectParameterException {
        return new IndirectParameterValue() {

            public InputStream read() throws InaccessibleIndirectParameterException {
                try {
                return reference.toURL().openStream();
                } catch (IOException e) {
                    throw new InaccessibleIndirectParameterException(reference.toString(),e );
                }
            }

            public OutputStream write() throws InaccessibleIndirectParameterException {              
              try {
                return reference.toURL().openConnection().getOutputStream();
              } catch (IOException e) {
                    throw new InaccessibleIndirectParameterException(reference.toString(),e );
                }
            }
        };
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "HttpProtocol";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Protocol adapter for http:/ protocol";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: HttpProtocol.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/