/*$Id: DefaultIndirectionProtocolLibrary.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

/** default implementation of the protocol library
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class DefaultIndirectionProtocolLibrary implements IndirectionProtocolLibrary, ComponentDescriptor {
    /** add a protocl to the list supported by this library */
    public void addProtocol(Protocol p) {
        map.put(p.getProtocolName().toLowerCase(),p);
        
    }
    
    
    /** Construct a new DefaultIndirectionProtocolLibrary
     * 
     */
    public DefaultIndirectionProtocolLibrary() {
        super();
        this.map = new HashMap();
    }
    protected final Map map;
    /**
     * @see org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary#getIndirect(org.astrogrid.applications.beans.v1.parameters.ParameterValue)
     */
    public IndirectParameterValue getIndirect(ParameterValue pval)
        throws InaccessibleIndirectParameterException, UnrecognizedIndirectParameterProtocolException{
        URI reference;
        try {
            reference = new URI(pval.getValue());
        }
        catch (URISyntaxException e) {
            throw new UnrecognizedIndirectParameterProtocolException(pval.getValue(),e);
        } 
        Protocol p = (Protocol) map.get(reference.getScheme());
        if (p == null) {
            throw new UnrecognizedIndirectParameterProtocolException(reference.toString());
        }
        return p.createIndirectValue(reference);
    }
    /**
     * @see org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary#listSupportedProtocols()
     */
    public String[] listSupportedProtocols() {
        return (String[])map.keySet().toArray(new String[]{});
    }
    /**
     * @see org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary#isProtocolSupported(java.lang.String)
     */
    public boolean isProtocolSupported(String protocol) {
        return map.containsKey(protocol.toLowerCase());
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "DefaultIndirectionProtocolLibrary";
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Supported Protocols:" + map.keySet();
    }


    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: DefaultIndirectionProtocolLibrary.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/