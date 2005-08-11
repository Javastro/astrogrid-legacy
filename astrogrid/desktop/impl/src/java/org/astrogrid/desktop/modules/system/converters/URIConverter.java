/*$Id: URIConverter.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.converters;

import org.astrogrid.filemanager.common.ivorn.IvornParser;
import org.astrogrid.store.Ivorn;

import org.apache.commons.beanutils.Converter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class URIConverter implements Converter {

    /** Construct a new URI converter
     * 
     */
    private URIConverter() {
        super();
    }

    /**
     * @throws URISyntaxException
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(Class arg0, Object arg1) {
        if (arg0 != URI.class) {
            throw new RuntimeException("Can only convert to URI" + arg0.getName());
        }        
        try {
            return new URI(arg1.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot convert " + arg1 + " to URI");
        }
    }
    
    private static Converter theInstance = new URIConverter();
    
    public static Converter getInstance() {
        return theInstance;
    }

}


/* 
$Log: URIConverter.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/