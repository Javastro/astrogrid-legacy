/*$Id: DefaultProtocolLibraryFactory.java,v 1.2 2004/11/22 18:26:37 clq2 Exp $
 * Created on 22-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

/** Factory object that will create a pre-configured DefaultProtocolLibrary, in which all known protocols are registered.
 * <b>NB: when implementing a new standard protocol, add it to this factory too </b>
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class DefaultProtocolLibraryFactory {

    /** Construct a new DefaultProtocolLibraryFactory
     * 
     */
    public DefaultProtocolLibraryFactory() {
        super();
    }
    
    public DefaultProtocolLibrary createLibrary() {
        DefaultProtocolLibrary lib = new DefaultProtocolLibrary();
        lib.addProtocol(new AgslProtocol());
        lib.addProtocol(new FileProtocol());
        lib.addProtocol(new FtpProtocol());
        lib.addProtocol(new HttpProtocol());
        lib.addProtocol(new IvornProtocol());
        return lib;
    }

}


/* 
$Log: DefaultProtocolLibraryFactory.java,v $
Revision 1.2  2004/11/22 18:26:37  clq2
nww-itn07-715a

Revision 1.1.2.1  2004/11/22 14:27:21  nw
added factory, and create-from-string-uri methods, to make
this package more accessible from other user's code / scripts.
 
*/