/*$Id: DefaultProtocolLibraryFactory.java,v 1.5 2008/09/03 14:18:57 pah Exp $
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
        DefaultProtocolLibrary lib = new DefaultProtocolLibrary(new Protocol[]{
        new FileProtocol(),new FtpProtocol(),new HttpProtocol(),new IvornProtocol()});
        return lib;
    }

}


/* 
$Log: DefaultProtocolLibraryFactory.java,v $
Revision 1.5  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.4.156.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.4  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.3.26.1  2005/03/11 11:20:58  nw
replaced VoSpaceClient with FileManagerClient

Revision 1.3  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.2.2.1  2004/11/26 15:14:21  nw
stuff brougt over from head

Revision 1.2  2004/11/22 18:26:37  clq2
nww-itn07-715a

Revision 1.1.2.1  2004/11/22 14:27:21  nw
added factory, and create-from-string-uri methods, to make
this package more accessible from other user's code / scripts.
 
*/