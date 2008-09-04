/*$Id: DefaultProtocolLibrary.java,v 1.5 2008/09/04 19:10:53 pah Exp $
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import org.springframework.stereotype.Service;

/** Default implementation of {@link org.astrogrid.applications.parameter.protocol.ProtocolLibrary}
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 2 Apr 2008
 *
 */
@Service
public class DefaultProtocolLibrary implements ProtocolLibrary, ComponentDescriptor {
    /** add a protocol to the set supported by this library */
    public void addProtocol(Protocol p) {
        map.put(p.getProtocolName().toLowerCase(),p);
        
    }
    
    
    /** Construct a new DefaultIndirectionProtocolLibrary
     * n.b. this will not work with picocontainer now
     */
    public DefaultProtocolLibrary(Protocol[]protocols) {
        super();
        this.map = new HashMap<String, Protocol>();
        for (int i = 0; i < protocols.length; i++) {
	    Protocol protocol = protocols[i];
	    addProtocol(protocol);
	}
    }
    protected final Map<String,Protocol>
    map;
    /**
     * @param secGuard 
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(org.astrogrid.applications.beans.v1.parameters.ParameterValue, SecurityGuard)
     */
    public ExternalValue getExternalValue(ParameterValue pval, SecurityGuard secGuard)
        throws InaccessibleExternalValueException, UnrecognizedProtocolException{
        URI reference;
        try {
            reference = new URI(pval.getValue());
        }
        catch (URISyntaxException e) {
            throw new UnrecognizedProtocolException(pval.getValue(),e);
        } 
        return getExternalValue(reference, secGuard);
    }
    /**
     * @param secGuard 
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(java.net.URI, SecurityGuard)
     */
    public ExternalValue getExternalValue(URI reference, SecurityGuard secGuard) throws InaccessibleExternalValueException, UnrecognizedProtocolException {
        Protocol p = (Protocol) map.get(reference.getScheme());
        if (p == null) {
            throw new UnrecognizedProtocolException(reference.toString());
        }
        return p.createIndirectValue(reference, secGuard);
    }
    


    /**
     * @param secGuard 
     * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(java.lang.String, SecurityGuard)
     */
    public ExternalValue getExternalValue(String location, SecurityGuard secGuard) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        return getExternalValue(new URI(location), secGuard);
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
Revision 1.5  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.4  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.3.182.3  2008/06/10 20:01:39  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.3.182.2  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.3.182.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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