/*$Id: SampleLegacyService.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java;

import org.w3c.dom.Element;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public interface SampleLegacyService {
    public Element slashdotRdf() throws LegacyServiceException ;
    public  Float getVersion() throws LegacyServiceException ;

}


/* 
$Log: SampleLegacyService.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/