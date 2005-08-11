/*$Id: DescriptorParser.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.framework.descriptors;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/** Inerface to a descrptor parser - something that parses an input stream into a module descriptor.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public interface DescriptorParser {
    /** parse a descriptor into objects 
     * @throws SAXException
     * @throws IOException*/
    public abstract ModuleDescriptor parse(InputStream is) throws IOException, SAXException;
}

/* 
 $Log: DescriptorParser.java,v $
 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.3  2005/04/27 13:42:41  clq2
 1082

 Revision 1.2.2.1  2005/04/22 15:59:26  nw
 made a star documenting desktop.

 Revision 1.2  2005/04/13 12:59:13  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.1  2005/03/18 12:09:32  nw
 got framework, builtin and system levels working.
 
 */