/*$Id: SampleLegacyService.java,v 1.1 2003/10/12 21:40:37 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import org.w3c.dom.Element;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public interface SampleLegacyService {
    public String searchGoogle(String q) throws LegacyServiceException;
    public Element slashdotRdf() throws LegacyServiceException ;
    public Element versionWsdl() throws LegacyServiceException ;
    public String getVersion() throws LegacyServiceException ;

}


/* 
$Log: SampleLegacyService.java,v $
Revision 1.1  2003/10/12 21:40:37  nw
first import
 
*/