/*$Id: FtpProtocol.java,v 1.2 2008/09/03 14:18:57 pah Exp $
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

import org.springframework.stereotype.Component;


/** Protocol Implementation for ftp:/
 * @todo replace with more robust implementation based on commons FtpClient.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
@Component
public class FtpProtocol extends HttpProtocol{

    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return "ftp";
    }

  
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
     return "FtpProtocol";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {   
        return "Protocol adapter for ftp:/ protocol";
    }

}


/* 
$Log: FtpProtocol.java,v $
Revision 1.2  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.1.266.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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