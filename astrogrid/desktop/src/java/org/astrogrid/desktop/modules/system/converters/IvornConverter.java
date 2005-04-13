/*$Id: IvornConverter.java,v 1.2 2005/04/13 12:59:11 nw Exp $
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

import java.net.URISyntaxException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class IvornConverter implements Converter {

    /** Construct a new IvornConverter
     * 
     */
    private IvornConverter() {
        super();
    }

    /**
     * @throws URISyntaxException
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(Class arg0, Object arg1) {
        if (arg0 != Ivorn.class) {
            throw new RuntimeException("Can only convert to Ivorns" + arg0.getName());
        }        
        try {
            return (new IvornParser(arg1.toString())).getIvorn();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot convert " + arg1 + " to ivorn");
        }
    }
    
    private static Converter theInstance = new IvornConverter();
    
    public static Converter getInstance() {
        return theInstance;
    }

}


/* 
$Log: IvornConverter.java,v $
Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/