/*$Id: DefaultProtocolLibrary.java,v 1.3 2004/11/29 20:00:56 clq2 Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

/** Default implementation of {@link org.astrogrid.applications.parameter.protocol.ProtocolLibrary}
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class DefaultProtocolLibrary implements ProtocolLibrary, ComponentDescriptor {
    /** add a protocl to the set supported by this library */
    public void addProtocol(Protocol p) {
        map.put(p.getProtocolName().toLowerCase(),p);
        
    }
    
    
    /** Construct a new DefaultIndirectionProtocolLibrary
     * 
     */
    public DefaultProtocolLibrary() {
        super();
        this.map = new HashMap();
    }
    protected final Map map;
    /**
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(org.astrogrid.applications.beans.v1.parameters.ParameterValue)
     */
    public ExternalValue getExternalValue(ParameterValue pval)
        throws InaccessibleExternalValueException, UnrecognizedProtocolException{
        URI reference;
        try {
            reference = new URI(pval.getValue());
        }
        catch (URISyntaxException e) {
            throw new UnrecognizedProtocolException(pval.getValue(),e);
        } 
        return getExternalValue(reference);
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(java.net.URI)
     */
    public ExternalValue getExternalValue(URI reference) throws InaccessibleExternalValueException, UnrecognizedProtocolException {
        Protocol p = (Protocol) map.get(reference.getScheme());
        if (p == null) {
            throw new UnrecognizedProtocolException(reference.toString());
        }
        return p.createIndirectValue(reference);
    }
    


    /**
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(java.lang.String)
     */
    public ExternalValue getExternalValue(String location) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        return getExternalValue(new URI(location));
    }    
    /**
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#listSupportedProtocols()
     */
    public String[] listSupportedProtocols() {
        return (String[])map.keySet().toArray(new String[]{});
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#isProtocolSupported(java.lang.String)
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
$Log: DefaultProtocolLibrary.java,v $
Revision 1.3  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.1.78.1  2004/11/26 15:14:21  nw
stuff brougt over from head

Revision 1.2  2004/11/22 18:26:37  clq2
nww-itn07-715a

Revision 1.1.84.1  2004/11/22 14:27:21  nw
added factory, and create-from-string-uri methods, to make
this package more accessible from other user's code / scripts.

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