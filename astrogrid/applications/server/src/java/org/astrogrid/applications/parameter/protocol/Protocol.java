/*$Id: Protocol.java,v 1.1 2004/07/26 12:07:38 nw Exp $
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

/** Factory interface for creating {@link org.astrogrid.applications.parameter.protocol.ExternalValue} instances.
 * @see org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface Protocol {
    /** access the name of the protocol this object provides support for 
     * @return name of the protocl this factory can build instances for.*/
    public String getProtocolName();
    /** create a new indirectParameterValue for the passed in URI 
     * @param reference the uri to build an instance for.
     * @return a handler for this uri.
     * @throws InaccessibleExternalValueException*/
    public ExternalValue createIndirectValue(URI reference) throws InaccessibleExternalValueException;
}


/* 
$Log: Protocol.java,v $
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